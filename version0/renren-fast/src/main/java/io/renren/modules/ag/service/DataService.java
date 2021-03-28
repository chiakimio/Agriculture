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
public interface DataService extends IService<DataEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询数据管理的数据关系
     */
    List<String> dataRelation();
    /**
     * 查询国家-年份-数量的关系
     */
    List<String> countryYearNum();
    /**
     * 查询所有国家
     */
    List allCountry();
    /**
     * 查询所有年份
     * @return
     */
    List<Object> allYear();
    /**
     * 查询所有物种
     * @return
     */
    List<Object> allSpecial();
    /**
     * 查询所有省份
     * @return
     */
    List<Object> allProvince();
    /**
     * 查询所有地级市
     * @return
     */
    List<Object> allCity();
    /**
     * 查询所有指标
     * @return
     */
    List<Object> allIndex();
    /**
     * 查询所有指标
     * @return
     */
    List<Object> allOrganization();
    /**
     * 传入国家-查询省份-年份-数量的关系
     */
    List<String> provinceYearNum(String countryCn);
    /**
     *  查询国家-省份
     */
    List<String> countryProvince(String countryCn);
    /**
     *  查询国家-下属地级市
     */
    List<String> countryCity(String countryCn);
    /**
     *  查询国家-年份
     */
    List<String> countryYear(String countryCn);
    /**
     *  查询国家-物种
     */
    List<String> countrySpecial(String countryCn);

    //第三层接口
    /**
     * 传入国家-省份-查询下属地级市
     */
    List<String> countryProvinceCity(String countryCn,String provinceCn);

    /**
     *  传入国家-省份-查询-年份
     */
    List<String> countryProvinceYear(String countryCn,String provinceCn);
    /**
     *  传入国家-省份查询-物种
     */
    List<String> countryProvinceSpecial(String countryCn,String provinceCn);
    /**
     * 传入国家-省份-查询城市-年份-数量的关系
     */
    List<String> cityYearNum(String countryCn,String provinceCn);
    //第四层接口
    /**
     * 传入国家-省份-地级市-查询物种-年份-数量的关系
     */
    List<String> specialYearCityNum(String countryCn,String provinceCn,String cityCn);


    /**
     *  传入国家-省份-地级市-查询-年份
     */
    List<String> countryProvinceCityYear(String countryCn,String provinceCn,String cityCn);
    /**
     *  传入国家-省份-地级市 查询-物种
     */
    List<String> countryProvinceCitySpecial(String countryCn,String provinceCn,String cityCn);
    //第五层
    /**
     *  传入物种查询-所有国家的所以年份的数量
     */
    List<String> specialCountryYearNum(String species);
    /**
     *  传入物种查国家
     */
    List<String> specialCountry(String species);
    /**
     *  传入物种查年份
     */
    List<String> specialYear(String species);
    /**
     *  传入物种查省份
     */
    List<String> specialProvince(String species);
    /**
     *  传入物种查市
     */
    List<String> specialCity(String species);

    //地图可视化
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
     * 查询某一指标所有国家和对应的平均值
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

