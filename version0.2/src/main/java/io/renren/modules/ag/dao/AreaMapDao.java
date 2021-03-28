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
public interface AreaMapDao extends BaseMapper<DataEntity> {


    //地图可视化 - 分布图
    /**
     * 查询某一指标所有国家和对应的平均值
     */
    List<String> columnCountryAndAvgData(String aIndex);
    //查询某一指标所有国家某一物种和对应的平均值-
    List<String> columnCountrySpecialAndAvgData(String aIndex,String special);
    //查询某一指标所有国家某一年和对应的平均值
    List<String> columnCountryYearAndAvgData(String aIndex,String yearJc);
    //查询某一指标所有国家某一物种某一年和对应的平均值
    List<String> columnCountrySpecialYearAndAvgData(String aIndex,String special,String yearJc);

    /**
     * 查询某一指标某一国家和对应的平均值
     */
    List<String> columnProvinceAndAvgData(String aIndex,String countryEn);
    //查询某一指标所有国家某一物种和对应的平均值-
    List<String> columnProvinceSpecialAndAvgData(String aIndex,String countryEn,String special);
    //查询某一指标所有国家某一年和对应的平均值
    List<String> columnProvinceYearAndAvgData(String aIndex,String countryEn,String yearJc);
    //查询某一指标所有国家某一物种某一年和对应的平均值
    List<String> columnProvinceSpecialYearAndAvgData(String aIndex,String countryEn,String special,String yearJc);

    /**
     * 查询某一指标某一省和对应的平均值
     */
    List<String> columnCityAndAvgData(String aIndex,String provinceEn);
    //查询某一指标所有国家某一物种和对应的平均值-
    List<String> columnCitySpecialAndAvgData(String aIndex,String provinceEn,String special);
    //查询某一指标所有国家某一年和对应的平均值
    List<String> columnCityYearAndAvgData(String aIndex,String provinceEn,String yearJc);
    //查询某一指标所有国家某一物种某一年和对应的平均值
    List<String> columnCitySpecialYearAndAvgData(String aIndex,String provinceEn,String special,String yearJc);




}
