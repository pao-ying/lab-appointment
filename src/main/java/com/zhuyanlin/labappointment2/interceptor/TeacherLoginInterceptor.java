package com.zhuyanlin.labappointment2.interceptor;

import com.zhuyanlin.labappointment2.CommonEnum.RoleEnum;
import com.zhuyanlin.labappointment2.entity.Teacher;
import com.zhuyanlin.labappointment2.exception.CommonException;
import com.zhuyanlin.labappointment2.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class TeacherLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private TeacherService teacherService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String role = (String) request.getAttribute("role");
        String userId = (String) request.getAttribute("userId");
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (pathVariables.get("tId") == null) {
            throw new CommonException(403, "url错误");
        }
        Long pathTId = Long.parseLong((String) pathVariables.get("tId")) ;
        log.debug("{}, {}", role, pathTId);
        Teacher teacher = teacherService.getTeacher(pathTId);
        if (!(role.equals(RoleEnum.TEACHER.getValue()) && teacher.getUserId().equals(userId))) {
            throw new CommonException(401, "token验证错误3");
        }
        return true;
    }
}
