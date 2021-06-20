package com.zhuyanlin.labappointment2.controller.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuyanlin.labappointment2.DO.TeacherDO;
import com.zhuyanlin.labappointment2.entity.Teacher;
import com.zhuyanlin.labappointment2.service.TeacherService;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import com.zhuyanlin.labappointment2.DTO.TeacherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = {"获取课程 Authorization: Login"})
@RestController
@RequestMapping("/api/common/")
public class CommonTeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper mapper;

    @ApiOperation("按条件获取教师")
    @GetMapping("teachers")
    public ResultDTO getTeachers(@RequestParam String teacher) throws JsonProcessingException {
        TeacherDO teacherDO = mapper.readValue(teacher, TeacherDO.class);
        List<Teacher> teachers = teacherService.getTeachersByDO(teacherDO);
        List<TeacherDTO> teacherDTOS = new ArrayList<>();
        for (Teacher teacher1 : teachers) {
            TeacherDTO teacherDTO = new TeacherDTO();
            BeanUtils.copyProperties(teacher1, teacherDTO);
            teacherDTO.setNickName(teacher1.getName());
            teacherDTO.setUserName(teacher1.getUserId());
            teacherDTOS.add(teacherDTO);
        }
        return ResultDTO.success(Map.of("teacherList", teacherDTOS), "获取教师列表成功");
    }

    @ApiOperation("获取教师")
    @GetMapping("teacher/{tId}")
    public ResultDTO getTeacher(@PathVariable Long tId) {
        Teacher teacher = teacherService.getTeacher(tId);
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(teacher, teacherDTO);
        teacherDTO.setPassword(null);
        teacherDTO.setNickName(teacher.getName());
        teacherDTO.setUserName(teacher.getUserId());
        return ResultDTO.success(Map.of("teacher", teacherDTO), "获取教师成功");
    }
}
