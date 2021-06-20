package com.zhuyanlin.labappointment2.service;

import com.zhuyanlin.labappointment2.CommonEnum.ProfessionEnum;
import com.zhuyanlin.labappointment2.DO.TeacherDO;
import com.zhuyanlin.labappointment2.entity.Course;
import com.zhuyanlin.labappointment2.entity.Teacher;
import com.zhuyanlin.labappointment2.exception.CommonException;
import com.zhuyanlin.labappointment2.mapper.CourseMapper;
import com.zhuyanlin.labappointment2.mapper.TeacherMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseService courseService;

    public Teacher searchTeacherByUserId(String userId) {
        List<Teacher> teachers = teacherMapper.selectByMap(Map.of("user_id", userId));
        if (teachers.size() == 0) {
            throw new CommonException(501, "没有对应教师");
        }
        return teachers.get(0);
    }

    public String getUserId(int total) {
        DecimalFormat df = new DecimalFormat("0000");
        return "2021" + df.format(total + 1);
    }

    public Teacher addTeacher(String name, String major, String profession)  {
        checkProfession(profession);
        int total = teacherMapper.getTeachersCount();
        String userId = getUserId(total);
        Teacher teacher = Teacher.builder().name(name).userId(userId).major(major).profession(profession).password(passwordEncoder.encode(userId)).build();
        teacherMapper.insert(teacher);
        return teacher;
    }

    public List<Teacher> getTeachersByDO(TeacherDO teacherDO) {
        return teacherMapper.getTeachers(teacherDO);
    }

    public void checkProfession(String profession) {
        for (ProfessionEnum professionEnum : ProfessionEnum.values()) {
            if (professionEnum.getValue().equals(profession)) {
                return;
            }
        }
        throw new CommonException(400, "没有对应职称");
    }

    public void updateTeacher(Long tId, TeacherDO teacherDO) {
        Teacher teacher = Teacher.builder()
                .id(tId).name(teacherDO.getNickName()).major(teacherDO.getMajor()).profession(teacherDO.getProfession())
                .build();
        if (teacherMapper.updateById(teacher) <= 0) {
            throw new CommonException(400, "没有对应教师");
        }
    }

    public void removeTeacher(Long tId) {
        getTeacher(tId);
        List<Course> courses = courseMapper.selectByMap(Map.of("tid", tId));
        for (Course course : courses) {
            courseService.removeCourse(tId, course.getId());
            courseMapper.deleteById(course.getId());
        }
        if (teacherMapper.deleteById(tId) <= 0) {
            throw new CommonException(400, "删除教师失败");
        }
    }

    public Teacher getTeacher(Long tId) {
        Teacher teacher = teacherMapper.selectById(tId);
        if (teacher == null) {
            throw new CommonException(400, "没有对应教师");
        } else {
            return teacher;
        }
    }

    public void updatePwd(Long tId, String pwd) {
        Teacher teacher = getTeacher(tId);
        teacher.setPassword(passwordEncoder.encode(pwd));
        teacherMapper.updateById(teacher);
    }

    public void resetPwd(Long tId) {
        Teacher teacher = getTeacher(tId);
        log.debug("{}",teacher.getId());
        teacher.setPassword(passwordEncoder.encode(teacher.getUserId()));
        teacherMapper.updateById(teacher);
    }
}
