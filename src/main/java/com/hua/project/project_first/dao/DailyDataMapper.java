package com.hua.project.project_first.dao;

import com.hua.project.project_first.pojo.DailyData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyDataMapper {
    void postData(DailyData dailyData);
}
