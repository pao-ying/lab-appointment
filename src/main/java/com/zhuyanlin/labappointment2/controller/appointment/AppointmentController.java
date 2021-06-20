package com.zhuyanlin.labappointment2.controller.appointment;

import com.zhuyanlin.labappointment2.DO.Appointment.AppointmentDO;
import com.zhuyanlin.labappointment2.entity.Lab;
import com.zhuyanlin.labappointment2.service.AppointmentService;
import com.zhuyanlin.labappointment2.DTO.LabDTO;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import com.zhuyanlin.labappointment2.DTO.appointment.AppointmentVO;
import com.zhuyanlin.labappointment2.DTO.appointment.TeacherAppointmentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = {"预约 Authorization: Teacher"})
@RestController
@RequestMapping("/api/")
@Slf4j
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @ApiOperation("查询符合数量的实验室")
    @GetMapping("teacher/{tId}/course/{cId}/appointment")
    public ResultDTO getLabs(@PathVariable Long tId, @PathVariable Long cId) {
        List<Lab> labs = appointmentService.getLabs(tId, cId);
        List<LabDTO> labDTOS = new ArrayList<>();
        for (Lab lab : labs) {
            LabDTO labDTO = new LabDTO();
            BeanUtils.copyProperties(lab, labDTO);
            labDTO.setNumber(lab.getNum());
            labDTOS.add(labDTO);
        }
        return ResultDTO.success(Map.of("laboratoryList", labDTOS), "获取符合要求实验室列表成功");
    }

//    @ApiOperation("添加预约")
//    @PostMapping("teacher/{tId}/course/{cId}/lab/{lId}/appointment")
//    public ResultDTO addAppointment(@PathVariable Long tId, @PathVariable Long cId, @PathVariable Long lId, @Valid @RequestBody AppointmentDO appointmentDO) {
//        appointmentService.addAppointment(tId, cId, lId, appointmentDO);
//        return ResultDTO.success(Map.of(), "预约成功");
//    }

    @ApiOperation("添加预约")
    @PostMapping("teacher/{tId}/course/{cId}/lab/{lId}/appointment")
    public ResultDTO addAppointment(@Valid @RequestBody AppointmentDO[] appointmentDOS, @PathVariable Long tId, @PathVariable Long cId, @PathVariable Long lId) {
        appointmentService.addAppointment(tId, cId, lId, appointmentDOS);
        return ResultDTO.success(Map.of(), "添加成功");
    }

    @ApiOperation("获得该教师的所有预约")
    @GetMapping("teacher/{tId}/appointment")
    public ResultDTO getAppointment(@PathVariable Long tId) {
        List<TeacherAppointmentVO> teacherAppointmentVOS = appointmentService.getAppointment(tId);
        return ResultDTO.success(Map.of("appointmentList", teacherAppointmentVOS), "获得该教师的所有预约");
    }

    @ApiOperation("删除教师的某个预约")
    @DeleteMapping("teacher/{tId}/appointment/{ctId}")
    public ResultDTO deleteAppointment(@PathVariable Long tId, @PathVariable Long ctId) {
        appointmentService.removeAppointment(tId, ctId);
        return ResultDTO.success(Map.of(), "删除该预约成功");
    }

    @ApiOperation("获得该实验室的所有预约")
    @GetMapping("lab/{lId}/appointment")
    public ResultDTO getAppointmentByLId(@PathVariable Long lId) {
        List<AppointmentVO> appointmentVOS = appointmentService.getAppointmentByLId(lId);
        return ResultDTO.success(Map.of("appointmentList", appointmentVOS), "获得该实验室的所有预约");
    }
}
