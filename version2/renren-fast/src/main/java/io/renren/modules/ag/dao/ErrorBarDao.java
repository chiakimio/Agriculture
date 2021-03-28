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
public interface ErrorBarDao extends BaseMapper<DataEntity> {


    //误差棒
    /**
     * 查询所有指标的数据
     */
    List<String> allIndexZhi(String aIndex);
    /**
     * 查询某一国家 所有指标的数据
     */
    List<String> countryAllIndexZhi(String aIndex,String countryCn);
    /**
     * 查询某一国家某一物种  所有指标的数据
     */
    List<String> countrySpeciesAllIndexZhi(String aIndex,String countryCn,String species);
    /**
     *  查询某一国家某一年份  所有指标的数据
     */
    List<String> countryYearAllIndexZhi(String aIndex,String countryCn,String yearJc);
    /**
     * 查询某一国家某一年份某一物种  所有指标的数据
     */
    List<String> countryYearSpeciesAllIndexZhi(String aIndex,String countryCn,String yearJc,String species);

    /**
     * 查询某一国家某一省份  所有指标的数据
     */
    List<String> countryProvinceAllIndexZhi(String aIndex,String countryCn,String provinceCn);
    /**
     * 查询某一国家某一省份某一物种  所有指标的数据
     */
    List<String> countryProvinceSpeciesAllIndexZhi(String aIndex,String countryCn,String provinceCn,String species);
    /**
     * 查询某一国家某一省份某一年份  所有指标的数据
     */
    List<String> countryProvinceYearAllIndexZhi(String aIndex,String countryCn,String provinceCn,String yearJc);
    /**
     * 查询某一国家某一省份某一年份某一物种  所有指标的数据
     */
    List<String> countryProvinceYearSpeciesAllIndexZhi(String aIndex,String countryCn,String provinceCn,String yearJc,String species);


    /**
     * 查询某一国家某一省份某一地市  所有指标的数据
     */
    List<String> countryProvinceCityAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn);
    /**
     * 查询某一国家某一省份某一地市某一年份  所有指标的数据
     */
    List<String> countryProvinceCityYearAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn,String yearJc);
    /**
     * 查询某一国家某一省份某一地市某一物种  所有指标的数据
     */
    List<String> countryProvinceCitySpeciesAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn,String species);
    /**
     * 查询某一国家某一省份某一地市某一年某一物种  所有指标的数据
     */
    List<String> countryProvinceCitySpeciesYearAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn,String yearJc,String species);


    /**
     * 查询某一物种  所有指标的数据
     */
    List<String> speciesAllIndexZhi(String aIndex,String species);
    /**
     * 查询某一物种 某一年 所有指标的数据
     */
    List<String> speciesYearAllIndexZhi(String aIndex,String species,String yearJc);
    /**
     * 查询 某一年 所有指标的数据
     */
    List<String> yearAllIndexZhi(String aIndex,String yearJc);

}
