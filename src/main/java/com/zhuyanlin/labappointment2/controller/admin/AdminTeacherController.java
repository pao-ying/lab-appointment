package com.zhuyanlin.labappointment2.controller.admin;

import com.zhuyanlin.labappointment2.DO.TeacherDO;
import com.zhuyanlin.labappointment2.DTO.TeacherDTO;
import com.zhuyanlin.labappointment2.entity.Teacher;
import com.zhuyanlin.labappointment2.service.TeacherService;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Api(tags = {"操作教师 Authorization: Admin"})
@RestController
@RequestMapping("/api/admin/teacher")
@Validated
public class AdminTeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("添加教师")
    @PostMapping("")
    public ResultDTO addTeacher(
            @NotNull
                    String nickName,
            @NotNull
                    String major,
            @NotNull
                    String profession
    ) {
        Teacher teacher = teacherService.addTeacher(nickName, major, profession);
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(teacher, teacherDTO);
        teacherDTO.setNickName(teacher.getName());
        teacherDTO.setUserName(teacher.getUserId());
        teacherDTO.setPassword(teacher.getUserId());
        return ResultDTO.success(Map.of("teacher", teacherDTO), "添加教师成功");
    }

    @ApiOperation("更新教师")
    @PutMapping("/{tId}")
    public ResultDTO updateTeacher(@PathVariable Long tId, @Valid @RequestBody TeacherDO teacherDO) {
        teacherService.updateTeacher(tId, teacherDO);
        return ResultDTO.success(Map.of(), "修改教师成功");
    }

    @ApiOperation("删除教师")
    @DeleteMapping("/{tId}")
    public ResultDTO deleteTeacher(@PathVariable Long tId) {
        teacherService.removeTeacher(tId);
        return ResultDTO.success(Map.of(), "删除教师成功");
    }

    @ApiOperation("重置教师密码")
    @PutMapping("/{tId}/password")
    public ResultDTO resetTeacher(@PathVariable Long tId) {
        teacherService.resetPwd(tId);
        return ResultDTO.success(Map.of(), "重置教师密码成功");
    }
}
