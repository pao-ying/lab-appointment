package com.zhuyanlin.labappointment2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuyanlin.labappointment2.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}