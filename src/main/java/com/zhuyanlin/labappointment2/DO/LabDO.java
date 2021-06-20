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
public class LabDO {
    @Size(max = 10)
    private String name;

    @Max(200)

    private Integer number;
    @Size(max = 255)
    private String description;
}
