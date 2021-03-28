package io.renren.modules.ag.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.ag.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 农业数据表
 * 
 * @author Guoping
 * @email 774647840@qq.com
 * @date 2021-02-03 19:48:29
 */
@Mapper
public interface ScatterMapDao extends BaseMapper<DataEntity> {


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
