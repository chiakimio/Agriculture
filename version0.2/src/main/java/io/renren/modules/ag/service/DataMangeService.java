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
public interface DataMangeService extends IService<DataEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 表中所有数据
     */
    List allData();

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
     * 查询所有字段
     * @return
     */
    List<Object> allColumn();
    /**
     * 查询所有字段
     * @return
     */
    List allExcelHeaders() ;


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
    /**
     *  传入物种查国家
     */
    List<String> yearCountry(String year);
    /**
     *  传入物种查省份
     */
    List<String> yearProvince(String year);
    /**
     *  传入物种查市
     */
    List<String> yearCity(String year);
    /**
     *  传入物种查市
     */
    List<String> yearSpecial(String year);


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




}

