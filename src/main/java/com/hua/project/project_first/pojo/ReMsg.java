package com.hua.project.project_first.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import java.util.Map;

@Data
@ToString
@AllArgsConstructor
public class ReMsg {
    private Code code;
    private String error;
    private Map msgMap;
}

