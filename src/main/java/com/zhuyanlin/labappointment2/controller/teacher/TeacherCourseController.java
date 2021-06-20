package com.zhuyanlin.labappointment2.controller.teacher;

import com.zhuyanlin.labappointment2.DO.CourseDO;
import com.zhuyanlin.labappointment2.entity.Course;
import com.zhuyanlin.labappointment2.service.CourseService;
import com.zhuyanlin.labappointment2.DTO.CourseDTO;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = {"操作课程 Authorization: Teacher"})
@RestController
@RequestMapping("/api/teacher/{tId}/course")
@Validated
public class TeacherCourseController {
    @Autowired
    private CourseService courseService;

    @ApiOperation("添加课程")
    @PostMapping("")
    public ResultDTO addCourse(@PathVariable Long tId,
                               @NotNull
                              String name,
                               @NotNull
                              Integer number,
                               @NotNull
                              Integer time
                              ) {
        courseService.addCourse(tId, name, number, time);
        return ResultDTO.success(Map.of(), "添加课程成功");
    }

    @ApiOperation("更新课程")
    @PutMapping("/{cId}")
    public ResultDTO updateCourse(@PathVariable Long tId, @PathVariable Long cId, @Valid @RequestBody CourseDO courseDO) {
        courseService.updateCourse(tId, cId, courseDO);
        return ResultDTO.success(Map.of(), "修改课程成功");
    }

    @ApiOperation("删除课程")
    @DeleteMapping("/{cId}")
    public ResultDTO deleteCourse(@PathVariable Long tId, @PathVariable Long cId) {
        courseService.removeCourse(tId, cId);
        return ResultDTO.success(Map.of(), "删除课程成功");
    }

    @ApiOperation("获取指定课程")
    @GetMapping("/{cId}")
    public ResultDTO getCourse(@PathVariable Long tId, @PathVariable Long cId) {
        Course course = courseService.checkCourse(tId, cId);
        return ResultDTO.success(Map.of("course", course), "获取教师对应课程成功");
    }

    @ApiOperation("获取该教师所有课程")
    @GetMapping("")
    public ResultDTO getCourses(@PathVariable Long tId) {
        List<Course> courses = courseService.getCoursesByTId(tId);
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courses) {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(course, courseDTO);
            courseDTO.setNumber(course.getNum());
            courseDTOS.add(courseDTO);
        }
        return ResultDTO.success(Map.of("courseList", courseDTOS), "获取教师所有课程列表成功");
    }
}
