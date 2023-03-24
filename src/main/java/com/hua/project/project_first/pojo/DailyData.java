package com.hua.project.project_first.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DailyData {
    private int step;
    private int water;
    private int height;
    private int weight;
}
