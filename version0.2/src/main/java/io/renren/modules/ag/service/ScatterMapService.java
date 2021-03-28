package io.renren.modules.ag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ag.entity.DataEntity;

import java.util.List;
import java.util.Map;

/**
 * 农业数据表
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
public interface ScatterMapService extends IService<DataEntity> {


    //地图可视化 - 散点图
    /**
     * 查询某一指标的 经纬度和值
     */
    List<String> indexLongDimenZhi(String aIndex);
    //查询某一指标所有国家某一物种和对应的平均值-
    List<String> indexSpeciesLongDimenZhi(String aIndex,String special);
    //查询某一指标所有国家某一年和对应的平均值
    List<String> indexYearLongDimenZhi(String aIndex,String yearJc);
    //查询某一指标所有国家某一物种某一年和对应的平均值
    List<String> indexSpeciesYearLongDimenZhi(String aIndex,String special,String yearJc);



}

