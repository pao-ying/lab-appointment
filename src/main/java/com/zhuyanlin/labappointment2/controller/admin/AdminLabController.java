package com.zhuyanlin.labappointment2.controller.admin;

import com.zhuyanlin.labappointment2.DO.LabDO;
import com.zhuyanlin.labappointment2.service.LabService;
import com.zhuyanlin.labappointment2.DTO.ResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Api(tags = {"操作实验室 Authorization: Admin"})
@RestController
@RequestMapping("/api/admin/lab")
@Validated
public class AdminLabController {
    @Autowired
    private LabService labService;

    @ApiOperation("添加实验室")
    @PostMapping("")
    public ResultDTO addLab(
            @NotNull
            String name,
            @NotNull
            Integer number,
            @NotNull
            String description
    ) {
        labService.addLab(name, description, number);
        return ResultDTO.success(Map.of(), "添加实验室成功");
    }

    @ApiOperation("更新实验室")
    @PutMapping("/{lId}")
    public ResultDTO updateLab(@PathVariable Long lId, @Valid @RequestBody LabDO labDO) {
        labService.updateLab(lId, labDO);
        return ResultDTO.success(Map.of(), "修改实验室成功");
    }

    @ApiOperation("删除实验室")
    @DeleteMapping("/{lId}")
    public ResultDTO deleteLab(@PathVariable Long lId) {
        labService.removeLab(lId);
        return ResultDTO.success(Map.of(), "删除实验室成功");
    }
}
