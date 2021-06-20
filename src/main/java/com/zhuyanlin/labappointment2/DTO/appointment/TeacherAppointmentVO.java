package com.zhuyanlin.labappointment2.DTO.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherAppointmentVO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ctId;
    private String courseName;
    private String labName;
    private AppointmentVO appointmentVO;
}
