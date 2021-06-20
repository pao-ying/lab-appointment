package com.zhuyanlin.labappointment2.service;

import com.zhuyanlin.labappointment2.DO.LabDO;
import com.zhuyanlin.labappointment2.entity.Lab;
import com.zhuyanlin.labappointment2.exception.CommonException;
import com.zhuyanlin.labappointment2.mapper.LabMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LabService {
    @Autowired
    private LabMapper labMapper;

    public void addLab(String name, String des, Integer num) {
        if (labMapper.insert(
                Lab.builder().name(name).des(des).num(num).build()
        ) <= 0) {
            throw new CommonException(400, "实验室名字重复");
        }
    }

    public void updateLab(Long lId, LabDO labDO) {
        Lab lab = Lab.builder()
                .id(lId)
                .name(labDO.getName())
                .num(labDO.getNumber())
                .des(labDO.getDescription())
                .build();
        if (labMapper.updateById(lab) <= 0) {
            throw new CommonException(400, "对应实验室不存在");
        }
    }

    public void removeLab(Long lId) {
        if (labMapper.deleteById(lId) <= 0) {
            throw new CommonException(400, "没有对应实验室");
        }
    }

    public List<Lab> getLabs() {
        return labMapper.selectList(null);
    }

    public Lab getLab(Long lId) {
        Lab lab = labMapper.selectById(lId);
        if (lab == null) {
            throw new CommonException(400, "没有对应实验室");
        } else {
            return lab;
        }
    }

    public List<Lab> getLabsByNum(int num) {
        return labMapper.getLabsByNum(num);
    }
}
