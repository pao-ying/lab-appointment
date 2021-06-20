package com.zhuyanlin.labappointment2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuyanlin.labappointment2.entity.CourseWeek;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CourseWeekMapper extends BaseMapper<CourseWeek> {
    @Select("select week from course_week cw where cw.ctid=#{ctid}")
    Integer[] getWeeks(Long ctid);
}