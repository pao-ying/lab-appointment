package com.zhuyanlin.labappointment2.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDO {
    @Size(max = 10)
    private String nickName;

    @Size(max = 20)
    private String major;

    private String profession;
}
