package com.zhuyanlin.labappointment2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@TableName("course_week")
public class CourseWeek {
    private Long id;

    private Long ctid;

    private Integer week;
}