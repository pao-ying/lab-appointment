package com.zhuyanlin.labappointment2.DO.Appointment;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AppointmentDetail {

    private Long cid;

    private Long lid;

    private Integer day;

    private Integer lesson;

    private Integer week;
}
