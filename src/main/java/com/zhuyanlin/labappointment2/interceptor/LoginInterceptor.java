package com.zhuyanlin.labappointment2.interceptor;

import com.zhuyanlin.labappointment2.config.EncryptConfig;
import com.zhuyanlin.labappointment2.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            throw new CommonException(401, "未登录");
        }
        Map<String, Object> result = encryptConfig.decrypt(token);
        request.setAttribute("role", result.get("role"));
        request.setAttribute("userId", result.get("userId"));
        log.debug("{}, {}", result.get("role"), result.get("userId"));
        return true;
    }
}
