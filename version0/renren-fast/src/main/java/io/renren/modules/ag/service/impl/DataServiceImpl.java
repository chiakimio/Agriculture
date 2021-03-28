package io.renren.modules.ag.service.impl;

import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ag.dao.DataDao;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.DataService;


@Service("dataService")
public class DataServiceImpl extends ServiceImpl<DataDao, DataEntity> implements DataService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DataEntity> page = this.page(
                new Query<DataEntity>().getPage(params),
                new QueryWrapper<DataEntity>()
        );

        return new PageUtils(page);
    }

    //逻辑代码
    /**
     * 查询所有国家
     */
    @Override
    public List allCountry() {

        List<Object> allCountry = new LinkedList<>();
        List allCountry1 =  baseMapper.allCountry();
        for(int i=0;i<allCountry1.size();i++){
            HashMap country = (HashMap) allCountry1.get(i);
            if(country==null||country.equals("")){
                continue;
            }else{
                country.forEach((key, value) -> {
                    allCountry.add(value);
                });
            }
        }
        return allCountry;
    }

    /**
     * 查询所有年份
     * @return
     */
    @Override
    public List<Object> allYear() {

        List<Object> allYear = new LinkedList<>();
        List allYear1 = baseMapper.allYear();
        for(int i=0;i<allYear1.size();i++){
            HashMap year = (HashMap) allYear1.get(i);
            if(year==null||year.equals("")){
                continue;
            }else{
                year.forEach((key, value) -> {
                    allYear.add(value);
                });
            }
        }
        return allYear;
    }

    /**
     * 查询所有物种
     * @return
     */
    @Override
    public List<Object> allSpecial() {
        List<Object> allSpecial = new LinkedList<>();
        List allSpecial1 = baseMapper.allSpecial();
        for(int i=0;i<allSpecial1.size();i++){
            HashMap special = (HashMap) allSpecial1.get(i);
            if(special==null||special.equals("")){
                continue;
            }else{
                special.forEach((key, value) -> {
                    allSpecial.add(value);
                });
            }
        }
        return allSpecial;
    }
    /**
     * 查询所有省份
     * @return
     */
    @Override
    public List<Object> allProvince() {
        List<Object> allProvince = new LinkedList<>();
        List allProvince1 = baseMapper.allProvince();
        for(int i=0;i<allProvince1.size();i++){
            HashMap pro = (HashMap) allProvince1.get(i);
            if(pro==null||pro.equals("")){
                continue;
            }else{
                pro.forEach((key, value) -> {
                    allProvince.add(value);
                });
            }
        }
        return allProvince;
    }
    /**
     * 查询所有地级市
     * @return
     */
    @Override
    public List<Object> allCity() {
        //所有地级市
        List<Object> allCity = new LinkedList<>();
        List allCity1 = baseMapper.allCity();
        for(int i=0;i<allCity1.size();i++){
            HashMap pro = (HashMap) allCity1.get(i);
            if(pro==null||pro.equals("")){
                continue;
            }else{
                pro.forEach((key, value) -> {
                    allCity.add(value);
                });
            }
        }
        return allCity;
    }
    /**
     * 查询所有指标
     * @return
     */
    @Override
    public List allIndex() {

        List<Object> allIndex = new LinkedList<>();
        List allIndex1 = baseMapper.allIndex();
        for(int i=0;i<allIndex1.size();i++){
            HashMap index = (HashMap) allIndex1.get(i);
            if(index==null||index.equals("")){
                continue;
            }else{
                index.forEach((key, value) -> {
                    allIndex.add(value);
                });
            }
        }
        //System.out.println(allIndex);
        List<String> noUseIndex  = new ArrayList<>();
        noUseIndex.add("id");
        noUseIndex.add("dataFrom");
        noUseIndex.add("countryCn");
        noUseIndex.add("countryEn");
        noUseIndex.add("cityCn");
        noUseIndex.add("cityEn");
        noUseIndex.add("provinceCn");
        noUseIndex.add("provinceEn");
        noUseIndex.add("yearJc");
        noUseIndex.add("species");
        noUseIndex.add("longitude");
        noUseIndex.add("dimension");
        allIndex.removeAll(noUseIndex);

        return allIndex;
    }

    /**
     * 查询所有机构
     * @return
     */
    @Override
    public List allOrganization() {

        List<Object> allOrganization = new LinkedList<>();
        List allOrganization1 = baseMapper.allOrganization();
        for(int i=0;i<allOrganization1.size();i++){
            HashMap organization = (HashMap) allOrganization1.get(i);
            if(organization==null||organization.equals("")){
                continue;
            }else{
                organization.forEach((key, value) -> {
                    allOrganization.add(value);
                });
            }
        }
        return allOrganization;
    }
    /**
     *  查询国家-省份
     */
    @Override
    public List countryProvince(String countryCn) {

        List<Object> allProvince = new LinkedList<>();
        List allProvince1 = baseMapper.countryProvince(countryCn);
        for(int i=0;i<allProvince1.size();i++){
            HashMap province = (HashMap) allProvince1.get(i);
            if(province==null||province.equals("")){
                continue;
            }else{
                province.forEach((key, value) -> {
                    allProvince.add(value);
                });
            }
        }
        return allProvince;
    };

    /**
     *  查询国家-下属地级市
     */
    @Override
    public List countryCity(String countryCn){
        //所有地级市
        List<Object> allCity = new LinkedList<>();
        List allCity1 = baseMapper.countryCity(countryCn);
        for(int i=0;i<allCity1.size();i++){
            HashMap pro = (HashMap) allCity1.get(i);
            if(pro==null||pro.equals("")){
                continue;
            }else{
                pro.forEach((key, value) -> {
                    allCity.add(value);
                });
            }
        }
        return allCity;
    };
    /**
     *  查询国家-年份
     */
    @Override
    public List countryYear(String countryCn) {
        List allYear = new LinkedList<>();
        List allYear1 = baseMapper.countryYear(countryCn);
        for(int i=0;i<allYear1.size();i++){
            HashMap year = (HashMap) allYear1.get(i);
            if(year==null||year.equals("")){
                continue;
            }else{
                year.forEach((key, value) -> {
                    allYear.add(value);
                });
            }
        }
        return allYear;
    };

    /**
     *  查询国家-物种
     */
    @Override
    public List countrySpecial(String countryCn) {
        List allSpecial = new LinkedList<>();
        List allSpecial1 = baseMapper.countrySpecial(countryCn);
        for(int i=0;i<allSpecial1.size();i++){
            HashMap special = (HashMap) allSpecial1.get(i);
            if(special==null||special.equals("")){
                continue;
            }else{
                special.forEach((key, value) -> {
                    allSpecial.add(value);
                });
            }
        }
        return allSpecial;
    };

    /**
     *  传入国家-省份-查询-地级市
     */
    @Override
    public List countryProvinceCity(String countryCn,String provinceCn) {
        //所有地级市
        List allCity = new LinkedList<>();
        List allCity1 = baseMapper.countryProvinceCity(countryCn,provinceCn);
        for(int i=0;i<allCity1.size();i++){
            HashMap city = (HashMap) allCity1.get(i);
            if(city==null||city.equals("")){
                continue;
            }else{
                city.forEach((key, value) -> {
                    allCity.add(value);
                });
            }
        }
        return allCity;
    };
    /**
     *  传入国家-省份-查询-年份
     */
    @Override
    public List countryProvinceYear(String countryCn,String provinceCn) {
        //所有年
        List allYear = new LinkedList<>();
        List allYear1 = baseMapper.countryProvinceYear(countryCn,provinceCn);
        for(int i=0;i<allYear1.size();i++){
            HashMap year = (HashMap) allYear1.get(i);
            if(year==null||year.equals("")){
                continue;
            }else{
                year.forEach((key, value) -> {
                    allYear.add(value);
                });
            }
        }
        return allYear;
    };

    /**
     *  传入国家-省份查询-物种
     */
    @Override
    public List countryProvinceSpecial(String countryCn,String provinceCn) {
        //所有物种
        List allSpecial = new LinkedList<>();
        List allSpecial1 = baseMapper.countryProvinceSpecial(countryCn,provinceCn);
        for(int i=0;i<allSpecial1.size();i++){
            HashMap special = (HashMap) allSpecial1.get(i);
            if(special==null||special.equals("")){
                continue;
            }else{
                special.forEach((key, value) -> {
                    allSpecial.add(value);
                });
            }
        }
        return allSpecial;
    }

    /**
     *  传入国家-省份-地级市-查询-年份
     */
    @Override
    public List countryProvinceCityYear(String countryCn, String provinceCn, String cityCn) {
        //所有年
        List allYear = new LinkedList<>();
        List allYear1 = baseMapper.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        for(int i=0;i<allYear1.size();i++){
            HashMap year = (HashMap) allYear1.get(i);
            if(year==null||year.equals("")){
                continue;
            }else{
                year.forEach((key, value) -> {
                    allYear.add(value);
                });
            }
        }
        return allYear;
    }
    /**
     *  传入国家-省份查询-物种
     */
    @Override
    public List countryProvinceCitySpecial(String countryCn, String provinceCn, String cityCn) {
        //物种
        List allSpecial = new LinkedList<>();
        List allSpecial1 = baseMapper.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        for(int i=0;i<allSpecial1.size();i++){
            HashMap special = (HashMap) allSpecial1.get(i);
            if(special==null||special.equals("")){
                continue;
            }else{
                special.forEach((key, value) -> {
                    allSpecial.add(value);
                });
            }
        }

        return allSpecial;
    }

    //物种
    /**
     *  传入物种查国家
     */
    @Override
    public List<String> specialCountry(String species) {
        List allCountry = new LinkedList<>();
        List allCountry1 = baseMapper.specialCountry(species);
        for(int i=0;i<allCountry1.size();i++){
            HashMap country = (HashMap) allCountry1.get(i);
            if(country==null||country.equals("")){
                continue;
            }else{
                country.forEach((key, value) -> {
                    allCountry.add(value);
                });
            }
        }
        return allCountry;
    };

    /**
     * 查询所有省份
     * @return
     */
    @Override
    public List specialProvince(String species) {
        List<Object> allProvince = new LinkedList<>();
        List allProvince1 = baseMapper.specialProvince(species);
        for(int i=0;i<allProvince1.size();i++){
            HashMap pro = (HashMap) allProvince1.get(i);
            if(pro==null||pro.equals("")){
                continue;
            }else{
                pro.forEach((key, value) -> {
                    allProvince.add(value);
                });
            }
        }
        return allProvince;
    }
    /**
     * 查询所有地级市
     * @return
     */
    @Override
    public List specialCity(String species) {
        //所有地级市
        List allCity = new LinkedList<>();
        List allCity1 = baseMapper.specialCity(species);
        for(int i=0;i<allCity1.size();i++){
            HashMap pro = (HashMap) allCity1.get(i);
            if(pro==null||pro.equals("")){
                continue;
            }else{
                pro.forEach((key, value) -> {
                    allCity.add(value);
                });
            }
        }
        return allCity;
    }


    /**
     *  传入物种查年份
     */
    @Override
    public List specialYear(String species) {

        List allYear = new LinkedList<>();
        List allYear1 = baseMapper.specialYear(species);
        for(int i=0;i<allYear1.size();i++){
            HashMap year = (HashMap) allYear1.get(i);
            if(year==null||year.equals("")){
                continue;
            }else{
                year.forEach((key, value) -> {
                    allYear.add(value);
                });
            }
        }
        return allYear;
    };




    ///业务代码

    /**
     * 查询数据管理的数据关系
     */

    @Override
    public List<String> dataRelation() {
        return baseMapper.dataRelation();
    }

    /**
     * 查询国家-年份-数量的关系
     */
    @Override
    public List<String> countryYearNum() {
        return baseMapper.countryYearNum();
    }

    /**
     * 传入国家-查询省份-年份-数量的关系
     */
    @Override
    public List<String> provinceYearNum(String countryCn) {
        return baseMapper.provinceYearNum(countryCn);
    };

    //第三个接口
    /**
     * 传入国家-省份-查询物种-年份-数量的关系
     */
    @Override
    public List<String> cityYearNum(String countryCn, String provinceCn) {
        return baseMapper.cityYearNum(countryCn,provinceCn);
    }


    //第四层
    /**
     *  传入国家-省份-地级市-查询-物种每年数量
     */

    @Override
    public List<String> specialYearCityNum(String countryCn, String provinceCn, String cityCn) {
        return baseMapper.specialYearCityNum(countryCn,provinceCn,cityCn);
    }

    //第五层
    /**
     *  传入物种查询-所有国家的所以年份的数量
     */
    @Override
    public List specialCountryYearNum(String species) {
        return  baseMapper.specialCountryYearNum(species);
    };


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



    //误差棒
    //误差棒
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


    //地图可视化 - 散点图
    /**
     * 查询某一指标的 经纬度和值
     */
    @Override
    public List<String> indexLongDimenZhi(String aIndex){return baseMapper.indexLongDimenZhi(aIndex);};
    //查询某一指标所有国家某一物种和对应的平均值-
    @Override
    public List<String> indexSpeciesLongDimenZhi(String aIndex,String special){return baseMapper.indexSpeciesLongDimenZhi(aIndex,special);};
    //查询某一指标所有国家某一年和对应的平均值
    @Override
    public List<String> indexYearLongDimenZhi(String aIndex,String yearJc){return baseMapper.indexYearLongDimenZhi(aIndex,yearJc);};
    //查询某一指标所有国家某一物种某一年和对应的平均值
    @Override
    public List<String> indexSpeciesYearLongDimenZhi(String aIndex,String special,String yearJc){return baseMapper.indexSpeciesYearLongDimenZhi(aIndex,special,yearJc);};



}