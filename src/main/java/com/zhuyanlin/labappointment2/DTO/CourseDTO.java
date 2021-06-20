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
public class CourseDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private Integer time;

    private Integer number;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
