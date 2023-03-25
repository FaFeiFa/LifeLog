package com.hua.project.project_first;

import com.hua.project.project_first.pojo.Code;
import org.junit.jupiter.api.Test;


public class test {
    @Test
    public void testCODE(){
        String okk = Code.OK_200.toString();
        System.out.println(okk);
        System.out.println(okk.equals("OK_200"));
    }
}
