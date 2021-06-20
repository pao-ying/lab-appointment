package com.zhuyanlin.labappointment2.exception;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonException extends RuntimeException {
    private int code;
    public CommonException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
