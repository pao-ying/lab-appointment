package com.zhuyanlin.labappointment2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@TableName("course_time")
public class CourseTime {
    private Long id;

    private Long cid;

    private Long lid;

    private Integer day;

    private Integer lesson;
}