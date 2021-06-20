package com.zhuyanlin.labappointment2.controller.common;

import com.zhuyanlin.labappointment2.entity.Lab;
import com.zhuyanlin.labappointment2.service.LabService;
import com.zhuyanlin.labappointment2.DTO.LabDTO;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = {"获取实验室 Authorization: Login"})
@RestController
@RequestMapping("/api/common/")
public class CommonLabController {
    @Autowired
    private LabService labService;

    @ApiOperation("获取所有实验室")
    @GetMapping("labs")
    public ResultDTO getLabs() {
        List<Lab> labs = labService.getLabs();
        List<LabDTO> labDTOS = new ArrayList<>();
        for (Lab lab : labs) {
            LabDTO labDTO = new LabDTO();
            BeanUtils.copyProperties(lab, labDTO);
            labDTO.setNumber(lab.getNum());
            labDTO.setDescription(lab.getDes());
            labDTOS.add(labDTO);
        }
        return ResultDTO.success(Map.of("laboratoryList", labDTOS), "获取实验室列表成功");
    }

    @ApiOperation("获取指定实验室")
    @GetMapping("lab/{lId}")
    public ResultDTO getLab(@PathVariable Long lId) {
        Lab lab = labService.getLab(lId);
        LabDTO labDTO = new LabDTO();
        BeanUtils.copyProperties(lab, labDTO);
        labDTO.setNumber(lab.getNum());
        labDTO.setDescription(lab.getDes());
        return ResultDTO.success(Map.of("laboratory", labDTO), "获取实验室成功");
    }
}
