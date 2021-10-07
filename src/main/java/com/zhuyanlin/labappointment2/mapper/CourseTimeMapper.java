package com.zhuyanlin.labappointment2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuyanlin.labappointment2.DO.Appointment.AppointmentDetail;
import com.zhuyanlin.labappointment2.entity.CourseTime;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CourseTimeMapper extends BaseMapper<CourseTime> {
    AppointmentDetail[] getLabAppointment(Long lid);

    AppointmentDetail[] getTeacherAppointment(Long tid);
}