package com.zhuyanlin.labappointment2.DTO.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentVO {
    private Integer lesson;
    private Integer day;
    private List<Integer> weeks;
}
