package com.huangjun.taskmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huangjun.taskmanager.entity.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
}
