package com.zhuyanlin.labappointment2.service;

import com.zhuyanlin.labappointment2.DO.Appointment.AppointmentDO;
import com.zhuyanlin.labappointment2.DO.Appointment.Week;
import com.zhuyanlin.labappointment2.entity.Course;
import com.zhuyanlin.labappointment2.entity.CourseTime;
import com.zhuyanlin.labappointment2.entity.CourseWeek;
import com.zhuyanlin.labappointment2.entity.Lab;
import com.zhuyanlin.labappointment2.exception.CommonException;
import com.zhuyanlin.labappointment2.mapper.CourseMapper;
import com.zhuyanlin.labappointment2.mapper.CourseTimeMapper;
import com.zhuyanlin.labappointment2.mapper.CourseWeekMapper;
import com.zhuyanlin.labappointment2.mapper.LabMapper;
import com.zhuyanlin.labappointment2.DTO.appointment.AppointmentVO;
import com.zhuyanlin.labappointment2.DTO.appointment.TeacherAppointmentVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class AppointmentService {
    @Autowired
    private LabService labService;

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseTimeMapper courseTimeMapper;

    @Autowired
    private CourseWeekMapper courseWeekMapper;

    public List<Lab> getLabs(Long tId, Long cId) {
        Course course = courseService.checkCourse(tId, cId);
        return labService.getLabsByNum(course.getNum());
    }

    public void checkAppointment(Long tId, Long cId, Long lId, AppointmentDO appointmentDO) {
        Course course = courseService.checkCourse(tId, cId);
        Lab lab = labMapper.selectById(lId);
        if (lab == null) {
            throw new CommonException(400, "没有对应实验室");
        }
        if (course.getNum() > lab.getNum()) {
            throw new CommonException(400, "实验室人数小于课程人数");
        }
        List<CourseTime> cts = courseTimeMapper.selectByMap(
                Map.of("lesson", appointmentDO.getLesson(), "day", appointmentDO.getDay(), "cid", cId, "lId", lId)
        );
        if (cts.size() != 0) {
            throw new CommonException(400, "在该星期,该节课与该实验室，该课程下该教师已预约，请删除预约后重新选择");
        }
    }

    @CacheEvict(value = "teacherAppointment", key = "#tId", allEntries = true)
    public void addAppointment(Long tId, Long cId, Long lId, AppointmentDO[] appointmentDOS) {
        for (AppointmentDO appointmentDO : appointmentDOS) {
            addAppointment2(tId, cId, lId, appointmentDO);
        }
    }

    @CacheEvict(value = "allAppointment", key = "#lId", allEntries = true)
    public void addAppointment2(Long tId, Long cId, Long lId, AppointmentDO appointmentDO) {
        checkAppointment(tId, cId, lId, appointmentDO);
        List<CourseTime> cts = courseTimeMapper.selectByMap(
                Map.of("lesson", appointmentDO.getLesson(), "day", appointmentDO.getDay(), "lId", lId)
        );
        List<Integer> oldWeeks = new ArrayList<>();
        for (CourseTime ct : cts) {
            Integer[] weeks = courseWeekMapper.getWeeks(ct.getId());
            oldWeeks.addAll(Arrays.asList(weeks));
        }
        HashSet<Integer> weeksSet = new HashSet<Integer>(oldWeeks);
        List<Week> weeks = appointmentDO.getWeeks();
        List<Integer> newWeeks = new ArrayList<>();
        for (Week week : weeks) {
            newWeeks.add(week.getWeek());
        }
        weeksSet.retainAll(newWeeks);
        if (weeksSet.size() > 0) {
            throw new CommonException(400, "选择的周次冲突");
        }
        CourseTime ct = CourseTime.builder()
                .cid(cId)
                .lid(lId)
                .lesson(appointmentDO.getLesson())
                .day(appointmentDO.getDay()).build();
        if (courseTimeMapper.insert(ct) <= 0) {
            throw new CommonException(400, "插入失败1");
        }
        for (Week week : appointmentDO.getWeeks()) {
            if (courseWeekMapper.insert(CourseWeek.builder().ctid(ct.getId()).week(week.getWeek()).build()) <= 0) {
                throw new CommonException(400, "插入失败2");
            }
        }
    }

    @CacheEvict(value = "allAppointment", key = "#lId", allEntries = true)
    public void removeCourseTime(Long ctId, Long lId) {
        List<CourseWeek> cws = courseWeekMapper.selectByMap(Map.of("ctid", ctId));
        for (CourseWeek cw : cws) {
            courseWeekMapper.deleteById(cw.getId());
        }
        courseTimeMapper.deleteById(ctId);
    }

    @CacheEvict(value = "teacherAppointment", key = "#tId", allEntries = true)
    public void removeAppointment(Long tId, Long ctId) {
        CourseTime ct = courseTimeMapper.selectById(ctId);
        if (ct == null) {
            throw new CommonException(400, "没有相应预约");
        }
        courseService.checkCourse(tId, ct.getCid());
        removeCourseTime(ctId, ct.getLid());
    }

    /*
    * 使用 courseTimeMapper.getTeacherAppointment()
    * */
    @Cacheable(value = "teacherAppointment", key = "#tId")
    public List<TeacherAppointmentVO> getAppointment(Long tId) {
        List<Course> courses = courseMapper.selectByMap(Map.of("tid", tId));
        List<TeacherAppointmentVO> TeacherAppointmentVOS = new ArrayList<>();
        for (Course course : courses) {
            List<CourseTime> cts = courseTimeMapper.selectByMap(Map.of("cid", course.getId()));
            for (CourseTime ct : cts) {
                TeacherAppointmentVO teacherAppointmentVO = new TeacherAppointmentVO();
                Lab lab = labMapper.selectById(ct.getLid());
                List<Integer> weeks = new ArrayList<>();
                List<CourseWeek> cws = courseWeekMapper.selectByMap(Map.of("ctid", ct.getId()));
                for (CourseWeek cw : cws) {
                    weeks.add(cw.getWeek());
                }
                teacherAppointmentVO.setCtId(ct.getId());
                teacherAppointmentVO.setCourseName(course.getName());
                teacherAppointmentVO.setLabName(lab.getName());
                teacherAppointmentVO.setAppointmentVO(
                        AppointmentVO.builder()
                        .day(ct.getDay())
                        .lesson(ct.getLesson())
                        .weeks(weeks).build()
                );
                TeacherAppointmentVOS.add(teacherAppointmentVO);
            }
        }
        return TeacherAppointmentVOS;
    }

    /*
    * 可以直接 course_time join course_week
    * 使用 courseTimeMapper.getLabAppointment()
    * */
    @Cacheable(value = "allAppointment", key = "#lId")
    public List<AppointmentVO> getAppointmentByLId(Long lId) {
        List<CourseTime> cts = courseTimeMapper.selectByMap(Map.of("lid", lId));
        List<AppointmentVO> appointmentVOS = new ArrayList<>();
        for (CourseTime ct : cts) {
            List<CourseWeek> cws = courseWeekMapper.selectByMap(Map.of("ctid", ct.getId()));
            List<Integer> weeks = new ArrayList<>();
            for (CourseWeek cw : cws) {
                weeks.add(cw.getWeek());
            }
            appointmentVOS.add(
                    AppointmentVO.builder()
                    .day(ct.getDay())
                    .lesson(ct.getLesson())
                    .weeks(weeks).build()
            );
        }
        return appointmentVOS;
    }
}
