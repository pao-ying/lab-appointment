package com.zhuyanlin.labappointment2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuyanlin.labappointment2.entity.Lab;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LabMapper extends BaseMapper<Lab> {
    List<Lab> getLabsByNum(int num);
}