package io.renren.modules.ag.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

    /**
     * 查询全部data
     * @param page: 页数，-1表示查询全部页，页大小200，其他时候page必须>=0
     * @param countryCn: 中文的国家名
     * @param species: 物种
     * @param unusedIds: 要剔除的id的list
     * @return: 满足查询条件的list
     */
    List<Map<String, Object>> queryAllData(int page, String countryCn, String species, int[] unusedIds);

    List<List<Double>> getTwoTargetData(String target1, String target2);

    List<List<List<Double>>> getMoreTargetData(String[] targetList);
}

