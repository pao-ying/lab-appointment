package com.zhuyanlin.labappointment2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuyanlin.labappointment2.DO.TeacherDO;
import com.zhuyanlin.labappointment2.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    @Select("select count(*) from teacher")
    Integer getTeachersCount();

    List<Teacher> getTeachers(TeacherDO teacherDO);

    void addTeacher1(TeacherDO teacherDO);
}