package com.hua.project.project_first.pojo;

import lombok.Data;
import lombok.ToString;
import java.util.Map;

@Data
@ToString
public class ReMsg {
    private Boolean status;
    private Exception error = null;
    private Map msgMap = null;
}

