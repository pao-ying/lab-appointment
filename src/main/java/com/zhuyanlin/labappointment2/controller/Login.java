package com.zhuyanlin.labappointment2.controller;

import com.zhuyanlin.labappointment2.config.EncryptConfig;
import com.zhuyanlin.labappointment2.entity.Teacher;
import com.zhuyanlin.labappointment2.service.TeacherService;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import com.zhuyanlin.labappointment2.DTO.TeacherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"登录 Authorization: null"})
@RestController
@RequestMapping("/api/login")
@Validated
public class Login {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EncryptConfig encryptConfig;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.password}")
    private String encryptPassword;

    @ApiOperation("统一登录")
    @PostMapping("")
    public ResultDTO login(
            @NotNull
            String userName,
            @NotNull
            String password,
            HttpServletResponse response
    ) {
        Map<String, Object> token = new HashMap<>();
        if (userName.equals(adminName)) {
            if (!passwordEncoder.matches(password, encryptPassword)) {
                return ResultDTO.error(401, "账号或密码错误");
            }
            token.put("role", "admin");
            token.put("userId", adminName);
            Map<String, String> user = new HashMap<>();
            user.put("nickName", "管理员");
            response.setHeader("token", encryptConfig.encrypt(token));
            return ResultDTO.success(Map.of("role", "admin", "user", user), "管理员登录成功");
        } else {
            Teacher teacher = teacherService.searchTeacherByUserId(userName);
            if (!passwordEncoder.matches(password, teacher.getPassword())) {
                return ResultDTO.error(401, "账号或密码错误");
            }
            token.put("role", "teacher");
            token.put("userId", userName);
            response.setHeader("token", encryptConfig.encrypt(token));
            Map<String, Object> data = new HashMap<>();
            TeacherDTO teacherDTO = new TeacherDTO();
            BeanUtils.copyProperties(teacher, teacherDTO);
            teacherDTO.setUserName(teacher.getUserId());
            teacherDTO.setNickName(teacher.getName());
            teacherDTO.setPassword(null);
            data.put("role", "teacher");
            data.put("user", teacherDTO);
            return ResultDTO.success(data, "教师登录成功");
        }
    }
}
