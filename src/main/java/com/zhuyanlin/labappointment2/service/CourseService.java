package com.zhuyanlin.labappointment2.service;

import com.zhuyanlin.labappointment2.DO.CourseDO;
import com.zhuyanlin.labappointment2.entity.Course;
import com.zhuyanlin.labappointment2.entity.CourseTime;
import com.zhuyanlin.labappointment2.entity.CourseWeek;
import com.zhuyanlin.labappointment2.exception.CommonException;
import com.zhuyanlin.labappointment2.mapper.CourseMapper;
import com.zhuyanlin.labappointment2.mapper.CourseTimeMapper;
import com.zhuyanlin.labappointment2.mapper.CourseWeekMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseTimeMapper courseTimeMapper;

    @Autowired
    private CourseWeekMapper courseWeekMapper;

    public void addCourse(Long tId, String name, Integer number, Integer time) {
        teacherService.getTeacher(tId);
        courseMapper.insert(Course.builder().tid(tId).name(name).num(number).time(time).build());
    }

    public Course getCourse(Long cId) {
        log.debug("{}", cId);
        Course course = courseMapper.selectById(cId);
        if ( course == null) {
            throw new CommonException(400, "没有对应课程");
        }
        return course;
    }

    public Course checkCourse(Long tId, Long cId) {
        Course course = getCourse(cId);
        if (!course.getTid().equals(tId)) {
            throw new CommonException(400, "该课程不属于该教师");
        }
        return course;
    }

    public void updateCourse(Long tId, Long cId, CourseDO courseDO) {
        checkCourse(tId, cId);
        courseMapper.updateById(
                Course.builder()
                .id(cId)
                .name(courseDO.getName())
                .num(courseDO.getNumber())
                .time(courseDO.getTime())
                .build()

        );
    }

    public void removeCourse(Long tId, Long cId) {
        checkCourse(tId, cId);
        List<CourseTime> cts = courseTimeMapper.selectByMap(Map.of("cid", cId));
        for (CourseTime ct : cts) {
            List<CourseWeek> cws = courseWeekMapper.selectByMap(Map.of("ctid", ct.getId()));
            for (CourseWeek cw : cws) {
                courseWeekMapper.deleteById(cw.getId());
            }
            courseTimeMapper.deleteById(ct.getId());
        }
        if (courseMapper.deleteById(cId) <= 0) {
            throw new CommonException(400, "删除课程失败");
        }
    }

    public List<Course> getCourses() {
        return courseMapper.selectList(null);
    }

    public List<Course> getCoursesByTId(Long tId) {
        return courseMapper.selectByMap(Map.of("tid", tId));
    }
}
