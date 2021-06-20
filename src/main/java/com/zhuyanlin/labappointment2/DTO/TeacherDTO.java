package com.zhuyanlin.labappointment2.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String nickName;
    private String major;
    private String password;
    private String userName;
    private String profession;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
