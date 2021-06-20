package com.zhuyanlin.labappointment2.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ResultDTO {
    private int code;
    private String message;
    private Map<String, Object> data;

    public static ResultDTO success(Map<String, Object> data, String msg) {
        return ResultDTO.builder().code(200).data(data).message(msg).build();
    }

    public static ResultDTO error(int code, String msg) {
        return ResultDTO.builder().code(code).message(msg).build();
    }
}