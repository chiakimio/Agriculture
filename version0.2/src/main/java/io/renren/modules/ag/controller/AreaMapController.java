package io.renren.modules.ag.controller;

import io.renren.common.utils.R;
import io.renren.modules.ag.service.AreaMapService;
import io.renren.modules.ag.service.DataMangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


/**
 * 分布图
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
@RestController
@RequestMapping("ag/areaMap")
@Api(tags = "分布图")

public class AreaMapController {
    @Autowired
    private AreaMapService areaMapService;
    @Autowired
    private DataMangeService dataMangeService;

    //分布图
    /**
     * 分布图-传入指标名称 - 查询-dataMangeService
     *
     */
    @GetMapping("/columnCountryAndAvgData")
    @ApiOperation("分布图-传入指标名称 - 查询-国家和对应的平均值")
    @RequiresPermissions("ag:areaMap:columnCountryAndAvgData")
    public R columnCountryAndAvgData(String  aIndex) throws IOException, ClassNotFoundException {
        //地图的数据
        List countryAndAvgData = areaMapService.columnCountryAndAvgData(aIndex);
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();


        return R.ok().put("data", countryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标名称+物种 - 查询-国家和对应的数据平均值
     *
     */
    @GetMapping("/columnSpecialCountryAndAvgData")
    @ApiOperation("分布图-传入指标名称+物种 - 查询-国家和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnSpecialCountryAndAvgData")
    public R columnSpecialCountryAndAvgData(String  aIndex,String special){

        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnSpecialCountryAndAvgData = areaMapService.columnCountrySpecialAndAvgData(aIndex,special);
        return R.ok().put("data", columnSpecialCountryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+年份 -查询-国家和对应的数量
     *
     */
    @GetMapping("/columnYearCountryAndAvgData")
    @ApiOperation("分布图-传入指标+年份 - 查询-国家和对应的数量")
    @RequiresPermissions("ag:areaMap:columnYearCountryAndAvgData")
    public R columnYearCountryAndAvgData(String  aIndex,String year){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnYearCountryAndAvgData = areaMapService.columnCountryYearAndAvgData(aIndex,year);
        return R.ok().put("data", columnYearCountryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+物种+年份 -查询-国家和对应的数量
     *
     */
    @GetMapping("/columnSpecialYearCountryAndAvgData")
    @ApiOperation("分布图-传入指标+物种+年份 -查询-国家和对应的数量")
    @RequiresPermissions("ag:areaMap:columnSpecialYearCountryAndAvgData")
    public R columnSpecialYearCountryAndAvgData(String  aIndex,String special,String year){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnSpecialYearCountryAndAvgData = areaMapService.columnCountrySpecialYearAndAvgData(aIndex,special,year);
        return R.ok().put("data", columnSpecialYearCountryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标-国家名称 - 查询-省份和对应的数量
     *
     */
    @GetMapping("/columnProvinceAndAvgData")
    @ApiOperation("分布图-传入指标-国家名称 - 查询-省份和对应的数量")
    @RequiresPermissions("ag:areaMap:columnProvinceAndAvgData")
    public R columnProvinceAndAvgData(String  aIndex,String countryEn){
        //地图的数据
        List columnProvinceAndAvgData = areaMapService.columnProvinceAndAvgData(aIndex,countryEn);
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        return R.ok().put("data", columnProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标名称+国家+物种 - 查询-省份和对应的数据平均值
     *
     */
    @GetMapping("/columnSpecialProvinceAndAvgData")
    @ApiOperation("分布图-传入指标名称+国家+物种 - 查询-省份和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnSpecialProvinceAndAvgData")
    public R columnSpecialProvinceAndAvgData(String  aIndex,String countryEn,String special){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnSpecialProvinceAndAvgData = areaMapService.columnProvinceSpecialAndAvgData(aIndex,countryEn,special);
        return R.ok().put("data", columnSpecialProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标名称+国家+物种+年份 - 查询-省份和对应的数据平均值
     *
     */
    @GetMapping("/columnYearProvinceAndAvgData")
    @ApiOperation("分布图-传入指标名称+国家+物种+年份 - 查询-省份和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnYearProvinceAndAvgData")
    public R columnYearProvinceAndAvgData(String  aIndex,String countryEn,String year){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnYearProvinceAndAvgData = areaMapService.columnProvinceYearAndAvgData(aIndex,countryEn,year);
        return R.ok().put("data", columnYearProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+物种+年份 -查询-省份和对应的数量
     *
     */
    @GetMapping("/columnSpecialYearProvinceAndAvgData")
    @ApiOperation("分布图-传入指标+物种+年份 -查询-省份和对应的数量")
    @RequiresPermissions("ag:areaMap:columnSpecialYearProvinceAndAvgData")
    public R columnSpecialYearProvinceAndAvgData(String  aIndex,String countryEn,String special,String year){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnSpecialYearProvinceAndAvgData = areaMapService.columnProvinceSpecialYearAndAvgData(aIndex,countryEn,special,year);
        return R.ok().put("data", columnSpecialYearProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标-省份名称 - 查询-城市和对应的数量
     *
     */
    @GetMapping("/columnCityAndAvgData")
    @ApiOperation("分布图-传入指标-省份名称 - 查询-城市和对应的数量")
    @RequiresPermissions("ag:areaMap:columnCityAndAvgData")
    public R columnCityAndAvgData(String  aIndex,String provinceEn){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List CityAndAvgData = areaMapService.columnCityAndAvgData(aIndex,provinceEn);
        return R.ok().put("data", CityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标名称+省份+物种 - 查询-城市和对应的数据平均值
     *
     */
    @GetMapping("/columnSpecialCityAndAvgData")
    @ApiOperation("分布图-传入指标名称+省份+物种 - 查询-城市和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnSpecialCityAndAvgData")
    public R columnSpecialCityAndAvgData(String  aIndex,String provinceEn,String special){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnSpecialCityAndAvgData = areaMapService.columnCitySpecialAndAvgData(aIndex,provinceEn,special);
        return R.ok().put("data", columnSpecialCityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+省份+年份 -查询-城市和对应的数量
     *
     */
    @GetMapping("/columnYearCityAndAvgData")
    @ApiOperation("分布图-传入指标+省份+年份 -查询-城市和对应的数量")
    @RequiresPermissions("ag:areaMap:columnYearCityAndAvgData")
    public R columnYearCityAndAvgData(String  aIndex,String provinceEn,String year){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnYearCityAndAvgData = areaMapService.columnCityYearAndAvgData(aIndex,provinceEn,year);
        return R.ok().put("data", columnYearCityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+省份+物种+年份 -查询-城市和对应的数量
     *
     */
    @GetMapping("/columnSpecialYearCityAndAvgData")
    @ApiOperation("分布图-传入指标+省份+物种+年份 -查询-城市和对应的数量")
    @RequiresPermissions("ag:areaMap:columnSpecialYearCityAndAvgData")
    public R columnSpecialYearCityAndAvgData(String  aIndex,String provinceEn,String special,String year){
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();
        //地图的数据
        List columnSpecialYearCityAndAvgData = areaMapService.columnCitySpecialYearAndAvgData(aIndex,provinceEn,special,year);
        return R.ok().put("data", columnSpecialYearCityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
}
