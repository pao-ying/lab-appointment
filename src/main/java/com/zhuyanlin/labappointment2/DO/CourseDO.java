package com.zhuyanlin.labappointment2.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDO {
    @Size(max = 44)
    private String name;

    @Max(200)
    private Integer number;

    @Max(120)
    private Integer time;
}
