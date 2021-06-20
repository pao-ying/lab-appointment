package com.zhuyanlin.labappointment2.DO.Appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AppointmentDO {
    @Min(1)
    @Max(7)
    @NotNull
    private Integer day;
    @Min(1)
    @Max(5)
    @NotNull
    private Integer lesson;
    @NotNull
    @Valid
    private List<Week> weeks;
}
