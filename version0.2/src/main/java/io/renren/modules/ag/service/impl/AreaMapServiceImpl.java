package io.renren.modules.ag.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.ag.dao.AreaMapDao;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.AreaMapService;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("areaMapService")
public class AreaMapServiceImpl extends ServiceImpl<AreaMapDao, DataEntity> implements AreaMapService {


    //地图可视化
    /**
     * 查询某一指标所有国家和对应的平均值
     */
    @Override
    public List<String> columnCountryAndAvgData(String aIndex) {
        return baseMapper.columnCountryAndAvgData(aIndex);
    };
    //查询某一指标所有国家某一物种和对应的平均值-
    @Override
    public List<String> columnCountrySpecialAndAvgData(String aIndex,String special) {
        return baseMapper.columnCountrySpecialAndAvgData(aIndex,special);
    };
    //查询某一指标所有国家某一年和对应的平均值
    @Override
    public List<String> columnCountryYearAndAvgData(String aIndex,String yearJc) {
        return baseMapper.columnCountryYearAndAvgData(aIndex,yearJc);
    };
    //查询某一指标所有国家某一物种某一年和对应的平均值
    @Override
    public List<String> columnCountrySpecialYearAndAvgData(String aIndex,String special,String yearJc) {
        return baseMapper.columnCountrySpecialYearAndAvgData(aIndex,special,yearJc);
    };


    /**
     * 查询某一指标某一国家  所有省份对应的平均值
     */
    @Override
    public List<String> columnProvinceAndAvgData(String aIndex,String countryEn) {
        return baseMapper.columnProvinceAndAvgData(aIndex,countryEn);
    };
    //查询某一指标某一国家某一物种和对应的平均值-
    @Override
    public List<String> columnProvinceSpecialAndAvgData(String aIndex,String countryEn,String special) {
        return baseMapper.columnProvinceSpecialAndAvgData(aIndex,countryEn,special);
    };
    //查询某一指标某一国家某一年和对应的平均值
    @Override
    public List<String> columnProvinceYearAndAvgData(String aIndex,String countryEn,String yearJc) {
        return baseMapper.columnProvinceYearAndAvgData(aIndex,countryEn,yearJc);
    };
    //查询某一指标某一国家某一物种某一年和对应的平均值
    @Override
    public List<String> columnProvinceSpecialYearAndAvgData(String aIndex,String countryEn,String special,String yearJc) {
        return baseMapper.columnProvinceSpecialYearAndAvgData(aIndex,countryEn,special,yearJc);
    };


    /**
     * 查询某一指标某一省和对应的平均值
     */
    @Override
    public List<String> columnCityAndAvgData(String aIndex,String provinceEn ) {
        return baseMapper.columnCityAndAvgData(aIndex,provinceEn);
    };
    //查询某一指标所有国家某一物种和对应的平均值-
    @Override
    public List<String> columnCitySpecialAndAvgData(String aIndex,String provinceEn,String special) {
        return baseMapper.columnCitySpecialAndAvgData(aIndex,provinceEn,special);
    };
    //查询某一指标所有国家某一年和对应的平均值
    @Override
    public List<String> columnCityYearAndAvgData(String aIndex,String provinceEn,String yearJc) {
        return baseMapper.columnCityYearAndAvgData(aIndex,provinceEn,yearJc);
    };
    //查询某一指标所有国家某一物种某一年和对应的平均值
    @Override
    public List<String> columnCitySpecialYearAndAvgData(String aIndex,String provinceEn,String special,String yearJc) {
        return baseMapper.columnCitySpecialYearAndAvgData(aIndex,provinceEn,special,yearJc);
    };



}