package com.zhuyanlin.labappointment2.controller.common;

import com.zhuyanlin.labappointment2.entity.Course;
import com.zhuyanlin.labappointment2.service.CourseService;
import com.zhuyanlin.labappointment2.DTO.CourseDTO;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = {"获取课程 Authorization: Login"})
@RestController
@RequestMapping("/api/course")
public class CommonCourseController {
    @Autowired
    private CourseService courseService;

    @ApiOperation("获取所有课程")
    @GetMapping("")
    public ResultDTO getCourses() {
        List<Course> courses = courseService.getCourses();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courses) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTO.setNumber(course.getNum());
            courseDTOS.add(courseDTO);
        }
        return ResultDTO.success(Map.of("courseList", courseDTOS), "获取课程列表成功");
    }

    @ApiOperation("获取指定课程")
    @GetMapping("/{cId}")
    public ResultDTO getCourse(@PathVariable Long cId) {
        Course course = courseService.getCourse(cId);
        return ResultDTO.success(Map.of("course", course), "获取课程成功");
    }
}
