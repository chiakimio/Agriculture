package io.renren.common.utils;

import java.util.List;

public class ListUtils {

    /**
     * 计算list的平均值
     */
    public  static double average(List list){

        double total=0;
        int l = list.size();
        for(int i=0; i<l; i++){
            total = total +(double)list.get(i) ;
        }

        double average = total / l;
        return average;
    }
}
