package com.zhuyanlin.labappointment2.controller.teacher;

import com.zhuyanlin.labappointment2.service.TeacherService;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Api(tags = {"教师的操作 Authorization: Teacher"})
@RestController
@Validated
@RequestMapping("/api/teacher/{tId}")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("修改密码")
    @PutMapping("")
    public ResultDTO updatePwd(@PathVariable Long tId, @NotNull String pwd) {
        teacherService.updatePwd(tId, pwd);
        return ResultDTO.success(Map.of(), "修改密码成功");
    }
}
