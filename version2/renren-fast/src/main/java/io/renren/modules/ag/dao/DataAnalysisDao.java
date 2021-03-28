package io.renren.modules.ag.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ag.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 农业数据表
 * 
 * @author Guoping
 * @email 774647840@qq.com
 * @date 2021-02-03 19:48:29
 */
@Mapper
public interface DataAnalysisDao extends BaseMapper<DataEntity> {


    /**
     * 数据来源-物种-年份 三层递进关系
     */
    List dataFromSpecialYear();

    /**
     * 查询 国家-一个属性的盒须图
     */
    List  oneIndexBox(String provinceCn,String aIndex);
    /**
     * 查询 国家-一个属性的盒须图
     */
    List  oneIndexErrorBox(String provinceCn,String aIndex);
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

    List<Map<String, Object>> queryAllData(String countryCn, String species, int limitLeft, int limitRight);

}
