package io.renren.modules.ag.controller;

import io.renren.common.utils.R;
import io.renren.modules.ag.service.DataMangeService;
import io.renren.modules.ag.service.ScatterMapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 农业数据表
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
@RestController
@RequestMapping("ag/scatterMap")
@Api(tags = "散点图")

public class ScatterMapController {
    @Autowired
    private DataMangeService dataMangeService;
    @Autowired
    private ScatterMapService scatterMapService;


    //散点图
    /**
     * 散点图-传入指标名称 - 查询-经纬度和数值
     */
    @GetMapping("/indexLongDimenData")
    @ApiOperation("散点图-传入指标名称 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexLongDimenData")
    public R indexLongDimenData(String  aIndex){
        //原始数据
        List indexLongDimenDatazz = scatterMapService.indexLongDimenZhi(aIndex);

        List<Object> indexLongDimenDataList = new LinkedList<Object>();
        for (int i = 0; i < indexLongDimenDatazz.size(); i++) {
            //一条数据的list
            HashMap<Object, Object> indexLongDimenData = new HashMap<>();
            //经纬度数组
            List<Object> lonDenData = new LinkedList<Object>();
            //取出每一条原始数据
            HashMap indexLongDimenDatacc = (HashMap) indexLongDimenDatazz.get(i);
            //先把经纬度合成一个小数组
            lonDenData.add(indexLongDimenDatacc.get("longitude"));
            lonDenData.add(indexLongDimenDatacc.get("dimension"));
            //直接放入map
            indexLongDimenData.put("coordinates",lonDenData);
            indexLongDimenData.put("dataValue",indexLongDimenDatacc.get(aIndex));
            indexLongDimenDataList.add(indexLongDimenData);
        }

        //选项
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();

        return R.ok().put("data", indexLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 散点图-传入指标-物种名称 - 查询-经纬度和数值
     */
    @GetMapping("/indexSpecialLongDimenData")
    @ApiOperation("散点图-传入指标-物种名称 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexSpecialLongDimenData")
    public R indexSpecialLongDimenData( String  aIndex,  String special ){
        //原始数据
        List indexLongDimenDatazz = scatterMapService.indexSpeciesLongDimenZhi(aIndex,special);

        List<Object> indexSpeciesLongDimenDataList = new LinkedList<Object>();

        for (int i = 0; i < indexLongDimenDatazz.size(); i++) {
            //一条数据的list
            HashMap<Object, Object> indexLongDimenData = new HashMap<>();
            //经纬度数组
            List<Object> lonDenData = new LinkedList<Object>();
            //取出每一条原始数据
            HashMap indexLongDimenDatacc = (HashMap) indexLongDimenDatazz.get(i);
            //先把经纬度合成一个小数组
            lonDenData.add(indexLongDimenDatacc.get("longitude"));
            lonDenData.add(indexLongDimenDatacc.get("dimension"));
            //直接放入map
            indexLongDimenData.put("coordinates",lonDenData);
            indexLongDimenData.put("dataValue",indexLongDimenDatacc.get(aIndex));
            indexSpeciesLongDimenDataList.add(indexLongDimenData);
        }
        //选项
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();

        return R.ok().put("data", indexSpeciesLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 散点图-传入指标-年份 - 查询-经纬度和数值
     */
    @GetMapping("/indexYearLongDimenData")
    @ApiOperation("散点图-传入指标-年份 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexYearLongDimenData")
    public R indexYearLongDimenData(String  aIndex,String year){
        //原始数据
        List indexLongDimenDatazz = scatterMapService.indexYearLongDimenZhi(aIndex,year);

        List<Object> indexYearLongDimenDataList = new LinkedList<Object>();

        for (int i = 0; i < indexLongDimenDatazz.size(); i++) {
            //一条数据的list
            HashMap<Object, Object> indexLongDimenData = new HashMap<>();
            //经纬度数组
            List<Object> lonDenData = new LinkedList<Object>();
            //取出每一条原始数据
            HashMap indexLongDimenDatacc = (HashMap) indexLongDimenDatazz.get(i);
            //先把经纬度合成一个小数组
            lonDenData.add(indexLongDimenDatacc.get("longitude"));
            lonDenData.add(indexLongDimenDatacc.get("dimension"));
            //直接放入map
            indexLongDimenData.put("coordinates",lonDenData);
            indexLongDimenData.put("dataValue",indexLongDimenDatacc.get(aIndex));
            indexYearLongDimenDataList.add(indexLongDimenData);
        }
        //选项
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();

        return R.ok().put("data", indexYearLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 散点图-传入指标-物种-年份 - 查询-经纬度和数值
     */
    @GetMapping("/indexSpecialYearLongDimenData")
    @ApiOperation("散点图-传入指标-物种-年份名称 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexSpecialYearLongDimenData")
    public R indexSpecialYearLongDimenData(String  aIndex,String special,String year){
        //原始数据
        List indexLongDimenDatazz = scatterMapService.indexSpeciesYearLongDimenZhi(aIndex,special,year);

        List<Object> indexSpeciesYearLongDimenDataList = new LinkedList<Object>();

        for (int i = 0; i < indexLongDimenDatazz.size(); i++) {
            //一条数据的list
            HashMap<Object, Object> indexLongDimenData = new HashMap<>();
            //经纬度数组
            List<Object> lonDenData = new LinkedList<Object>();
            //取出每一条原始数据
            HashMap indexLongDimenDatacc = (HashMap) indexLongDimenDatazz.get(i);
            //先把经纬度合成一个小数组
            lonDenData.add(indexLongDimenDatacc.get("longitude"));
            lonDenData.add(indexLongDimenDatacc.get("dimension"));
            //直接放入map
            indexLongDimenData.put("coordinates",lonDenData);
            indexLongDimenData.put("dataValue",indexLongDimenDatacc.get(aIndex));
            indexSpeciesYearLongDimenDataList.add(indexLongDimenData);
        }
        //选项
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List allIndex = dataMangeService.allIndex();

        return R.ok().put("data", indexSpeciesYearLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

}
