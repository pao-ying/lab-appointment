package com.zhuyanlin.labappointment2.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuyanlin.labappointment2.exception.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EncryptConfig {
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${security.key}")
    private String secretKey;
    @Value("${security.salt}")
    private String salt;
    @Autowired
    private TextEncryptor encryptor;


    /**
     * 直接基于密钥/盐值创建单例TextEncryptor对象。避免反复创建
     *
     */
    @Bean
    public TextEncryptor getTextEncryptor() {
        return Encryptors.text(secretKey, salt);
    }

    public String encrypt(Map<String, Object> payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            return encryptor.encrypt(json);
        } catch (JsonProcessingException e) {
            throw new CommonException(500, "服务器端错误");
        }
    }

    /**
     * 无法验证/解密/反序列化，说明数据被篡改，判定无权限
     *
     */
    public Map<String, Object> decrypt(String auth) {
        try {
            String json = encryptor.decrypt(auth);
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new CommonException(403, "无权限");
        }
    }
}
