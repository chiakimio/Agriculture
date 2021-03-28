package io.renren.modules.ag.controller;

import io.renren.common.utils.R;
import io.renren.modules.ag.service.DataMangeService;
import io.renren.modules.ag.service.ErrorBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


/**
 * 农业数据表
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
@RestController
@RequestMapping("ag/errorBar")
@Api(tags = "误差棒")

public class ErrorBarController {
    @Autowired
    private DataMangeService dataMangeService;
    @Autowired
    private ErrorBarService errorBarService;

    /**
     * 误差棒图-全世界默认信息

     * @return
     */

    @GetMapping("/AllIndex")
    @ApiOperation("误差棒图-全世界默认信息")
    @RequiresPermissions("ag:errorBar:AllIndex")
    public R AllIndex() throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object column : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.allIndexZhi((String) column);
            HashMap aIndex = (HashMap) aIndexData.get(0);
            Object min = aIndex.get("min");
            Object max = aIndex.get("max");
            Object avg = aIndex.get("avg");
            //每个指标的数组
            List<Object> errorData = new LinkedList<Object>();
            errorData.add(column);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }

        //下方选项
        List<Object> allCountry = dataMangeService.allCountry();
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List<Object> allProvince = dataMangeService.allProvince();
        List<Object> allCity = dataMangeService.allCity();

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-查询某一国家  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/countryAllIndex")
    @ApiOperation("误差棒图-查询某一国家  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryAllIndex")
    public R countryAllIndex(String countryCn) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryAllIndexZhi((String) aIndex,countryCn);
            ////System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List<Object> errorData = new LinkedList<Object>();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryYear(countryCn);
        List<String> allSpecial = dataMangeService.countrySpecial(countryCn);
        List<String> allProvince = dataMangeService.countryProvince(countryCn);
        List<String> allCity = dataMangeService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-查询某一国家某一物种  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/countrySpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countrySpecialAllIndex")
    public R countrySpecialAllIndex(String countryCn,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countrySpeciesAllIndexZhi((String) aIndex,countryCn,special);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List<Object> errorData = new LinkedList<Object>();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryYear(countryCn);
        List<String> allSpecial = dataMangeService.countrySpecial(countryCn);
        List<String> allProvince = dataMangeService.countryProvince(countryCn);
        List<String> allCity = dataMangeService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一年份  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/countryYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一年份  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryYearAllIndex")
    public R countryYearAllIndex(String countryCn,String year) throws IOException, ClassNotFoundException {
        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryYearAllIndexZhi((String) aIndex,countryCn,year);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List<Object> errorData = new LinkedList<Object>();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryYear(countryCn);
        List<String> allSpecial = dataMangeService.countrySpecial(countryCn);
        List<String> allProvince = dataMangeService.countryProvince(countryCn);
        List<String> allCity = dataMangeService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一年份某一物种  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/countrySpecialYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一年份某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countrySpecialYearAllIndex")
    public R countrySpecialYearAllIndex(String countryCn,String special,String year) throws IOException, ClassNotFoundException {
        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryYearSpeciesAllIndexZhi((String) aIndex,countryCn,year,special);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryYear(countryCn);
        List<String> allSpecial = dataMangeService.countrySpecial(countryCn);
        List<String> allProvince = dataMangeService.countryProvince(countryCn);
        List<String> allCity = dataMangeService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-某个国家-某个省份的所有属性
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/countryProvinceAllIndex")
    @ApiOperation("误差棒图-某个省的所有属性")
    @RequiresPermissions("ag:errorBar:countryProvinceAllIndex")
    public R countryProvinceAllIndex(String countryCn,String provinceCn) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceAllIndexZhi((String) aIndex,countryCn,provinceCn);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataMangeService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataMangeService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一物种  所有指标的数据
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/countryProvinceSpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceSpecialAllIndex")
    public R countryProvinceSpecialAllIndex(String countryCn,String provinceCn,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceSpeciesAllIndexZhi((String) aIndex,countryCn,provinceCn,special);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataMangeService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataMangeService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一年份  所有指标的数据
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/countryProvinceYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一年份  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceYearAllIndex")
    public R countryProvinceYearAllIndex(String countryCn,String provinceCn,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceYearAllIndexZhi((String) aIndex,countryCn,provinceCn,year);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataMangeService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataMangeService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一年份某一物种  所有指标的数据
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/countryProvinceSpecialYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一年份某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceSpecialYearAllIndex")
    public R countryProvinceSpecialYearAllIndex(String countryCn,String provinceCn,String special,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceYearSpeciesAllIndexZhi((String) aIndex,countryCn,provinceCn,year,special);
            //System.out.println(aIndex);
            //System.out.println(aIndexData);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataMangeService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataMangeService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-某个国家-某个省份-某个地级市 的所有属性
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/countryProvinceCityAllIndex")
    @ApiOperation("误差棒图-某个市的所有属性")
    @RequiresPermissions("ag:errorBar:countryProvinceCityAllIndex")
    public R countryProvinceCityAllIndex(String countryCn,String provinceCn,String cityCn) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceCityAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataMangeService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一地市某一年份  所有指标的数据
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/countryProvinceCityYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一地市某一年份  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceCityYearAllIndex")
    public R countryProvinceCityYearAllIndex(String countryCn,String provinceCn,String cityCn,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceCityYearAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn,year);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataMangeService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一地市某一物种  所有指标的数据
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/countryProvinceCitySpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一地市某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceCitySpecialAllIndex")
    public R countryProvinceCitySpecialAllIndex(String countryCn,String provinceCn,String cityCn,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceCitySpeciesAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn,special);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataMangeService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一地市某一年某一物种所有指标的数据
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/countryProvinceCitySpecialYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一地市某一年某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceCitySpecialYearAllIndex")
    public R countryProvinceCitySpecialYearAllIndex(String countryCn,String provinceCn,String cityCn,String special,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.countryProvinceCitySpeciesYearAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn,year,special);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allYear = dataMangeService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataMangeService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-某个物种 的所有属性
     * 传入物种的汉语名
     * @return
     */

    @GetMapping("/SpecialAllIndex")
    @ApiOperation("误差棒图-某个物种的所有属性")
    @RequiresPermissions("ag:errorBar:specialAllIndex")
    public R specialAllIndex(String  special) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.speciesAllIndexZhi((String) aIndex,special);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        //下方选项
        List<String> allCountry = dataMangeService.specialCountry(special);
        List<String> allYear = dataMangeService.specialYear(special);
        List<Object> allSpecial = dataMangeService.allSpecial();
        List<String> allProvince = dataMangeService.specialProvince(special);
        List<String> allCity = dataMangeService.specialCity(special);

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial).put("allYear", allYear);
    }
    /**
     * 误差棒图-查询某一物种 某一年 所有指标的数据
     * 传入物种的汉语名
     * @return
     */

    @GetMapping("/SpecialYearAllIndex")
    @ApiOperation("误差棒图-查询某一物种 某一年 所有指标的数据")
    @RequiresPermissions("ag:errorBar:SpecialYearAllIndex")
    public R SpecialYearAllIndex(String  special,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.speciesYearAllIndexZhi((String) aIndex,special,year);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allCountry = dataMangeService.specialCountry(special);
        List<Object> allSpecial = dataMangeService.allSpecial();
        List<String> allProvince = dataMangeService.specialProvince(special);
        List<String> allCity = dataMangeService.specialCity(special);

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-查询某一物种 某一年 所有指标的数据
     * 传入物种的汉语名
     * @return
     */

    @GetMapping("/YearAllIndex")
    @ApiOperation("误差棒图-查询 某一年 所有指标的数据")
    @RequiresPermissions("ag:errorBar:YearAllIndex")
    public R YearAllIndex(String year) throws IOException, ClassNotFoundException {

        List allIndex = dataMangeService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = errorBarService.yearAllIndexZhi((String) aIndex,year);
            HashMap aData = (HashMap) aIndexData.get(0);
            Object min = aData.get("min");
            Object max = aData.get("max");
            Object avg = aData.get("avg");
            //每个指标的数组
            List errorData = new LinkedList();
            errorData.add(aIndex);
            errorData.add(max);
            errorData.add(min);
            errorDataList.add(errorData);
            barData.add((Double) avg);
        }
        //下方选项
        List<String> allCountry = dataMangeService.yearCountry(year);
        List allSpecial = dataMangeService.yearSpecial(year);
        List<String> allProvince = dataMangeService.yearProvince(year);
        List<String> allCity = dataMangeService.yearCity(year);
        List allYear = dataMangeService.allYear();

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial);
    }
}
