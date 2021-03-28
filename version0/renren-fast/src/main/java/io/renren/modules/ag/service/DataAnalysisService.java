package io.renren.modules.ag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.ag.entity.DataEntity;

import java.util.List;

/**
 * 农业数据表
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
public interface DataAnalysisService extends IService<DataEntity> {
    /**
     * 数据来源-物种-年份 三层递进关系
     */
    List dataFromSpecialYear();
    List dataFromSpecialYear1();

    /**
     * 查询 国家-一个属性的盒须图
     */
    List  oneIndexBox(String province,String aIndex);
    /**
     * 查询 国家-一个属性的盒须图
     * @return
     */
    List<Double> oneIndexErrorBox(String provinceCn, String aIndex);
    /**
     * 查询 国家-一个属性的散点
     * @return
     */
    List<Double> oneIndexScatterPlot(String countryCn, String aIndex);

    /**
     * 查询 国家-一个属性的散点
     * @return
     */
    List<Double> oneIndexRadarPlot(String countryCn, String aIndex);



}

