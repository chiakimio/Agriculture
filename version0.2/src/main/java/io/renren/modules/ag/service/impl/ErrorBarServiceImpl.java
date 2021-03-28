package io.renren.modules.ag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.ag.dao.ErrorBarDao;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.ErrorBarService;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("errorBarService")
public class ErrorBarServiceImpl extends ServiceImpl<ErrorBarDao, DataEntity> implements ErrorBarService {
    
    //误差棒
    /**
     * 查询所有指标的数据
     */
    @Override
    public List<String> allIndexZhi(String aIndex) {return baseMapper.allIndexZhi(aIndex);};

    /**
     * 查询某一国家 所有指标的数据
     */
    @Override
    public List<String> countryAllIndexZhi(String aIndex,String countryCn){return baseMapper.countryAllIndexZhi(aIndex,countryCn);};
    /**
     * 查询某一国家某一物种  所有指标的数据
     */
    @Override
    public List<String> countrySpeciesAllIndexZhi(String aIndex,String countryCn,String species){return baseMapper.countrySpeciesAllIndexZhi(aIndex,countryCn,species);};


    /**
     *  查询某一国家某一年份  所有指标的数据
     */
    @Override
    public List<String> countryYearAllIndexZhi(String aIndex,String countryCn,String yearJc){return baseMapper.countryYearAllIndexZhi(aIndex,countryCn,yearJc);};
    /**
     * 查询某一国家某一年份某一物种  所有指标的数据
     */
    @Override
    public List<String> countryYearSpeciesAllIndexZhi(String aIndex,String countryCn,String yearJc,String species){return baseMapper.countryYearSpeciesAllIndexZhi(aIndex,countryCn,yearJc,species);};

    /**
     * 查询某一国家某一省份  所有指标的数据
     */
    @Override
    public List<String> countryProvinceAllIndexZhi(String aIndex,String countryCn,String provinceCn){return baseMapper.countryProvinceAllIndexZhi(aIndex,countryCn,provinceCn);};

    /**
     * 查询某一国家某一省份某一物种  所有指标的数据
     */
    @Override
    public List<String> countryProvinceSpeciesAllIndexZhi(String aIndex,String countryCn,String provinceCn,String species){return baseMapper.countryProvinceSpeciesAllIndexZhi(aIndex,countryCn,provinceCn,species);};
    /**
     * 查询某一国家某一省份某一年份  所有指标的数据
     */
    @Override
    public List<String> countryProvinceYearAllIndexZhi(String aIndex,String countryCn,String provinceCn,String yearJc){return baseMapper.countryProvinceYearAllIndexZhi(aIndex,countryCn,provinceCn,yearJc);};
    /**
     * 查询某一国家某一省份某一年份某一物种  所有指标的数据
     */
    @Override
    public List<String> countryProvinceYearSpeciesAllIndexZhi(String aIndex,String countryCn,String provinceCn,String yearJc,String species){return baseMapper.countryProvinceYearSpeciesAllIndexZhi(aIndex,countryCn,provinceCn,yearJc,species);};


    /**
     * 查询某一国家某一省份某一地市  所有指标的数据
     */
    @Override
    public List<String> countryProvinceCityAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn){return baseMapper.countryProvinceCityAllIndexZhi(aIndex,countryCn,provinceCn,cityCn);};
    /**
     * 查询某一国家某一省份某一地市某一年份  所有指标的数据
     */
    @Override
    public List<String> countryProvinceCityYearAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn,String yearJc){return baseMapper.countryProvinceCityYearAllIndexZhi(aIndex,countryCn,provinceCn,cityCn,yearJc);};;
    /**
     * 查询某一国家某一省份某一地市某一物种  所有指标的数据
     */
    @Override
    public List<String> countryProvinceCitySpeciesAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn,String species){return baseMapper.countryProvinceCitySpeciesAllIndexZhi(aIndex,countryCn,provinceCn,cityCn,species);};;
    /**
     * 查询某一国家某一省份某一地市某一年某一物种  所有指标的数据
     */
    @Override
    public List<String> countryProvinceCitySpeciesYearAllIndexZhi(String aIndex,String countryCn,String provinceCn,String cityCn,String yearJc,String species){return baseMapper.countryProvinceCitySpeciesYearAllIndexZhi(aIndex,countryCn,provinceCn,cityCn,yearJc,species);};;
    /**
     * 查询某一物种  所有指标的数据
     */
    @Override
    public List<String> speciesAllIndexZhi(String aIndex,String species){return baseMapper.speciesAllIndexZhi(aIndex,species);};

    /**
     * 查询某一物种 某一年 所有指标的数据
     */
    @Override
    public List<String> speciesYearAllIndexZhi(String aIndex,String species,String yearJc){return baseMapper.speciesYearAllIndexZhi(aIndex,species,yearJc);};
    /**
     * 查询 某一年 所有指标的数据
     */
    @Override
    public List<String> yearAllIndexZhi(String aIndex,String yearJc){return baseMapper.yearAllIndexZhi(aIndex,yearJc);};



}