package com.zhuyanlin.labappointment2.interceptor;

import com.zhuyanlin.labappointment2.CommonEnum.RoleEnum;
import com.zhuyanlin.labappointment2.config.EncryptConfig;
import com.zhuyanlin.labappointment2.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Value("${admin.name}")
    private Object adminName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String role = (String) request.getAttribute("role");
        String userId = (String) request.getAttribute("userId");
        if (!(role.equals(RoleEnum.ADMIN.getValue()) && userId.equals(adminName))) {
            throw new CommonException(401, "token验证错误2");
        }
        return true;
    }
}
