package io.renren.modules.ag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.ag.dao.DataManageDao;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.DataMangeService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static io.renren.common.utils.StringUtil.deepCopy;


@Service("dataManageService")
public class DataManageServiceImpl extends ServiceImpl<DataManageDao, DataEntity> implements DataMangeService {

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
     * 表中所有数据
     */
    @Override
    public List allData(){
        return baseMapper.allData();
    }
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
     * 查询所有字段
     * @return
     */
    @Override
    public List allColumn() {

        List<Object> allColumn = new LinkedList<>();
        List allIndex1 = baseMapper.allIndex();
        for(int i=0;i<allIndex1.size();i++){
            HashMap index = (HashMap) allIndex1.get(i);
            if(index==null||index.equals("")){
                continue;
            }else{
                index.forEach((key, value) -> {
                    allColumn.add(value);
                });
            }
        }
        return allColumn;
    }

    /**
     * 查询所有表头
     * @return
     */
    @Override
    public List allExcelHeaders() {

        List<Object> allColumn = new LinkedList<>();
        List allIndex1 = baseMapper.allIndex();
        for(int i=0;i<allIndex1.size();i++){
            HashMap index = (HashMap) allIndex1.get(i);
            if(index==null||index.equals("")){
                continue;
            }else{
                index.forEach((key, value) -> {
                    allColumn.add(value);
                });
            }
        }

        List<String> allExcelHeaders = null;
        try {
            allExcelHeaders = deepCopy(allColumn);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Collections.replaceAll(allExcelHeaders, "dataFrom","数据来源");
        Collections.replaceAll(allExcelHeaders, "countryCn","国家名称");
        Collections.replaceAll(allExcelHeaders, "countryEn","Name of country");
        Collections.replaceAll(allExcelHeaders, "provinceCn","省份名称");
        Collections.replaceAll(allExcelHeaders, "provinceEn","Name of province");
        Collections.replaceAll(allExcelHeaders, "cityCn","城市名称");
        Collections.replaceAll(allExcelHeaders, "cityEn","Name of city");
        Collections.replaceAll(allExcelHeaders, "yearJc","年份");
        Collections.replaceAll(allExcelHeaders, "species","物种");
        Collections.replaceAll(allExcelHeaders, "longitude","经度（°E）");
        Collections.replaceAll(allExcelHeaders, "dimension","纬度（°N）");

        System.out.println("allExcelHeaders "+allExcelHeaders);
        return allExcelHeaders;
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

    /**
     *  传入年份查国家
     */

    @Override
    public List<String> yearCountry(String year) {
        List allCountry = new LinkedList<>();
        List allCountry1 = baseMapper.yearCountry(year);
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
     *  传入年份查省份
     */
    @Override
    public List yearProvince(String year) {
        List<Object> allProvince = new LinkedList<>();
        List allProvince1 = baseMapper.yearProvince(year);
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
     *  传入年份查市
     */
    @Override
    public List yearCity(String year) {
        //所有地级市
        List allCity = new LinkedList<>();
        List allCity1 = baseMapper.yearCity(year);
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
     *  传入年份查市
     */
    @Override
    public List<String> yearSpecial(String year) {
        List allSpecial = new LinkedList<>();
        List allSpecial1 = baseMapper.yearSpecial(year);
        for(int i=0;i<allSpecial1.size();i++){
            HashMap country = (HashMap) allSpecial1.get(i);
            if(country==null||country.equals("")){
                continue;
            }else{
                country.forEach((key, value) -> {
                    allSpecial.add(value);
                });
            }
        }
        return allSpecial;
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




}