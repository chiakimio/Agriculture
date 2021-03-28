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
public interface DataManageDao extends BaseMapper<DataEntity> {

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
     * 查询所有省份
     */
    List<String> allProvince();
    /**
     * 查询所有地级市
     */
    List<String> allCity();
    /**
     * 查询所有年份
     */
    List<String> allYear();
    /**
     * 查询所有物种
     */
    List<String> allSpecial();

    /**
     * 查询所有指标
     */
    List<String> allIndex();

    /**
     * 查询所有机构
     */
    List<String> allOrganization();

    //第二个接口
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
     * 传入国家-省份-查询物种-年份-数量的关系
     */
    List<String> cityYearNum(String countryCn,String provinceCn);
    /**
     *  传入国家-省份-查询-地级市
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
    //第五层接口
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
    List<String> yearCountry(String yearJc);

    /**
     *  传入物种查省份
     */
    List<String> yearProvince(String yearJc);
    /**
     *  传入物种查市
     */
    List<String> yearCity(String yearJc);
    /**
     *  传入物种查市
     */
    List<String> yearSpecial(String yearJc);





}
