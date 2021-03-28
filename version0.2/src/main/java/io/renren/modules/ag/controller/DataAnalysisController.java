package io.renren.modules.ag.controller;

import java.util.*;

import cn.hutool.http.HttpStatus;
import io.renren.common.utils.*;
import io.renren.modules.ag.service.DataAnalysisService;
import io.renren.modules.ag.service.DataMangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.*;

/**
 * 第三模块：数据处理与分析
 *
 * @author Guoping
 *
 * @date 2021-03-17 19:48:29
 */
@RestController
@RequestMapping("ag/dataAnalysis")
@Api(tags = "数据处理与分析")
public class DataAnalysisController {

    @Autowired
    private DataAnalysisService dataAnalysisService;
    @Autowired
    private DataMangeService dataMangeService;
    @Value("${file-save-path}")
    private String filePath;
    private StringUtil stringUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @ApiOperation(value = "根据特定参数查询数据")
    @GetMapping("/queryInformation")
    @RequiresPermissions("ag:dataAnalysis:queryInformation")
    public R queryInformation() {
        List<Object> allCountry = dataMangeService.allCountry();
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List<Object> allProvince = dataMangeService.allProvince();
        List<Object> allCity = dataMangeService.allCity();
        List allOrganization = dataMangeService.allOrganization();
        List allIndex = dataMangeService.allIndex();

        return R.ok().put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial).put("allYear", allYear).put("allIndex", allIndex).put("allOrganization", allOrganization);
    }
    @ApiOperation(value = "根据特定参数查询数据")
    @GetMapping("/queryData")
    @RequiresPermissions("ag:dataAnalysis:queryData")
    public R queryData(String dataFrom,String country,String province,String special,String year) {
        //目前的搭配是：
        // 单一属性：五种
        //两个属性：地理加物种或者地理加年，或者物种加年  两类地理信息
        //三个属性：数据来源+物种+年    国家+物种+年  省份+物种+年份
        //四个属性：国家+省份+物种+年
        //重载只能做到不同类型 的参数，但是我的参数类型高度类似，考虑只在个方法体中完成所有判断

        List allData = new ArrayList<>();

        if((dataFrom!=null&&!dataFrom.equals(""))&&(country==null||country.equals(""))&&(province==null||province.equals(""))&&(special==null||special.equals(""))&&(year==null||year.equals(""))){
            //只有数据来源
            System.out.println("只有数据来源");
            String dataFrom1 ="\""+dataFrom+"\"";
            String sql = "SELECT * from ag_data WHERE dataFrom = "+dataFrom1+";";
             allData = jdbcTemplate.queryForList(sql);
        }else if((country!=null&&!country.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(province==null||province.equals(""))&&(special==null||special.equals(""))&&(year==null||year.equals(""))) {
            //只有国家
            System.out.println("只有国家");
            String country1 ="\""+country+"\"";
            String sql = "SELECT * from ag_data WHERE countryCn = "+country1+";";
             allData = jdbcTemplate.queryForList(sql);
            

        }else if((province!=null&&!province.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(country==null||country.equals(""))&&(special==null||special.equals(""))&&(year==null||year.equals(""))) {
            //只有省份
            System.out.println("只有物种");
            String province1 ="\""+province+"\"";
            String sql = "SELECT * from ag_data WHERE provinceCn = "+province1+";";
             allData = jdbcTemplate.queryForList(sql);
            

        }else if((special!=null&&!special.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(province==null||province.equals(""))&&(country==null||country.equals(""))&&(year==null||year.equals(""))) {
            //只有物种
            System.out.println("只有物种");
            String special1 ="\""+special+"\"";
            String sql = "SELECT * from ag_data WHERE species = "+special1+";";
             allData = jdbcTemplate.queryForList(sql);
            

        }else if((country!=null&&!country.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(province==null||province.equals(""))&&(special==null||special.equals(""))&&(year==null||year.equals(""))) {
            //只有年份
            System.out.println("只有年份");
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE yearJc = "+year1+";";
             allData = jdbcTemplate.queryForList(sql);
            

        }else if((dataFrom!=null&&!dataFrom.equals(""))&&(special!=null&&!special.equals(""))&&(province==null||province.equals(""))&&(country==null||country.equals(""))&&(year==null||year.equals(""))) {
            //数据来源加物种
            System.out.println("数据来源加物种");
            String dataFrom1 ="\""+dataFrom+"\"";
            String special1 ="\""+special+"\"";
            String sql = "SELECT * from ag_data WHERE dataFrom = "+dataFrom1+" and species = "+special1+";";
             allData = jdbcTemplate.queryForList(sql);
            

        }else if((dataFrom!=null&&!dataFrom.equals(""))&&(year!=null&&!year.equals(""))&&(province==null||province.equals(""))&&(country==null||country.equals(""))&&(special==null||special.equals(""))) {
            //数据来源加年份
            System.out.println("数据来源加年份");
            String dataFrom1 ="\""+dataFrom+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE dataFrom = "+dataFrom1+" and yearJc = "+year1+";";
             allData = jdbcTemplate.queryForList(sql);
            

        }else if((country!=null&&!country.equals(""))&&(special!=null&&!special.equals(""))&&(province==null||province.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(year==null||year.equals(""))) {
            //国家加物种
            System.out.println("国家加物种");
            String country1 ="\""+country+"\"";
            String special1 ="\""+special+"\"";
            String sql = "SELECT * from ag_data WHERE countryCn = "+country1+" and species = "+special1+";";
            allData = jdbcTemplate.queryForList(sql);
            

        }else if((country!=null&&!country.equals(""))&&(year!=null&&!year.equals(""))&&(province==null||province.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(special==null||special.equals(""))) {
            //国家加年份
            System.out.println("国家加年份");
            String country1 ="\""+country+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE countryCn = "+country1+" and yearJc = "+year1+";";
            allData = jdbcTemplate.queryForList(sql);
            

        }else if((province!=null&&!province.equals(""))&&(special!=null&&!special.equals(""))&&(country==null||country.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(year==null||year.equals(""))) {
            //省份加物种
            System.out.println("省份加物种");
            String province1 ="\""+province+"\"";
            String special1 ="\""+special+"\"";
            String sql = "SELECT * from ag_data WHERE provinceCn = "+province1+" and species = "+special1+";";
            allData = jdbcTemplate.queryForList(sql);
        }else if((province!=null&&!province.equals(""))&&(year!=null&&!year.equals(""))&&(country==null||country.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(special==null||special.equals(""))) {
            //省份加年份
            System.out.println("省份加年份");
            String province1 ="\""+province+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE provinceCn = "+province1+" and yearJc = "+year1+";";
            allData = jdbcTemplate.queryForList(sql);
        }else if((dataFrom!=null&&!dataFrom.equals(""))&&(special!=null&&!special.equals(""))&&(year!=null&&!year.equals(""))&&(country==null||country.equals(""))&&(province==null||province.equals(""))) {
            //数据来源加物种+年份
            System.out.println("数据来源加物种+年份");
            String dataFrom1 ="\""+dataFrom+"\"";
            String special1 ="\""+special+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE dataFrom = "+dataFrom1+" and species = "+special1+" and yearJc = "+year1+";";
            allData = jdbcTemplate.queryForList(sql);
        }else if((country!=null&&!country.equals(""))&&(special!=null&&!special.equals(""))&&(year!=null&&!year.equals(""))&&(dataFrom==null||dataFrom.equals(""))&&(province==null||province.equals(""))) {
            //国家加物种+年份
            System.out.println("国家加物种+年份");
            String country1 ="\""+country+"\"";
            String special1 ="\""+special+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE countryCn = "+country1+" and species = "+special1+" and yearJc = "+year1+";";
            allData = jdbcTemplate.queryForList(sql);
        }else if((province!=null&&!province.equals(""))&&(special!=null&&!special.equals(""))&&(year!=null&&!year.equals(""))&&(country==null||country.equals(""))&&(dataFrom==null||dataFrom.equals(""))) {
            //省份加物种+年份
            System.out.println("省份加物种+年份");
            String province1 ="\""+province+"\"";
            String special1 ="\""+special+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE provinceCn = "+province1+" and species = "+special1+" and yearJc = "+year1+";";
            allData = jdbcTemplate.queryForList(sql);
        }else if((country!=null&&!country.equals(""))&&(province!=null&&!province.equals(""))&&(special!=null&&!special.equals(""))&&(year!=null&&!year.equals(""))&&(dataFrom==null||dataFrom.equals(""))) {
            //国家+省份+物种+年份
            System.out.println("国家+省份+物种+年份");
            String country1 ="\""+country+"\"";
            String province1 ="\""+province+"\"";
            String special1 ="\""+special+"\"";
            String year1 ="\""+year+"\"";
            String sql = "SELECT * from ag_data WHERE countryCn = "+country1+" and provinceCn = "+province1+" and species = "+special1+" and yearJc = "+year1+";";
            allData = jdbcTemplate.queryForList(sql);
        }else{
            allData = dataMangeService.allData();
            
        }

        List<Object> allCountry = dataMangeService.allCountry();
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List<Object> allProvince = dataMangeService.allProvince();
        List<Object> allCity = dataMangeService.allCity();
        List allIndex = dataMangeService.allIndex();
        List allOrganization = dataMangeService.allOrganization();

        List<Object> dataRelation = dataAnalysisService.dataFromSpecialYear();
        List<Object> dataRelation1 = dataAnalysisService.dataFromSpecialYear1();

        return R.ok().put("data", allData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial).put("allYear", allYear).put("allOrganization", allOrganization).put("allIndex", allIndex);
    }

    @ApiOperation(value = "传入国家名-1个属性的盒须图")
    @GetMapping("/oneIndexBox")
    @RequiresPermissions("ag:dataAnalysis:oneIndexBox")
    public R oneIndexBox(String country,String aIndex1) {
       //返回前端：下属的省份名+盒须图的五个数据  按照两个list

        List allData = new ArrayList<>();

        List<String> allProvince = dataMangeService.countryProvince(country);

        for(int i=0;i<allProvince.size();i++) {
            String province = allProvince.get(i);
            allData.add(dataAnalysisService.oneIndexBox(province,aIndex1));
        }
        return R.ok().put("data", allData).put("location", allProvince);
    }

    @ApiOperation(value = "传入国家名-2个属性的误差范围图")
    @GetMapping("/twoIndexBox")
    @RequiresPermissions("ag:dataAnalysis:twoIndexBox")
    public R twoIndexBox(String country,String aIndex1,String aIndex2) {
        //返回前端：下属的省份名+误差棒的六个数据  按照两个list
        List dimensions = new ArrayList<>();
        dimensions.add("province");
        dimensions.add(aIndex1+"_Min");
        dimensions.add(aIndex1);
        dimensions.add(aIndex1+"_Max");
        dimensions.add(aIndex2+"_Min");
        dimensions.add(aIndex2);
        dimensions.add(aIndex2+"_Max");

        List allData = new ArrayList<>();
        //待优化：可以少了查询省份的这一步，我们可以直接在数据库中查询省份
        List<String> allProvince = dataMangeService.countryProvince(country);

        for(int i=0;i<allProvince.size();i++) {
            String province = allProvince.get(i);
            List tempDataList =   new ArrayList<>();
            tempDataList.add(province);
            tempDataList.add(dataAnalysisService.oneIndexErrorBox(province,aIndex1).get(0));
            tempDataList.add(dataAnalysisService.oneIndexErrorBox(province,aIndex1).get(1));
            tempDataList.add(dataAnalysisService.oneIndexErrorBox(province,aIndex1).get(2));
            tempDataList.add(dataAnalysisService.oneIndexErrorBox(province,aIndex2).get(0));
            tempDataList.add(dataAnalysisService.oneIndexErrorBox(province,aIndex2).get(1));
            tempDataList.add(dataAnalysisService.oneIndexErrorBox(province,aIndex2).get(2));
            allData.add(tempDataList);

        }
        return R.ok().put("data", allData).put("dimensions", dimensions);
    }

    @ApiOperation(value = "传入国家名-三个属性的三维散点图")
    @GetMapping("/threeIndexScatterPlot")
    @RequiresPermissions("ag:dataAnalysis:threeIndexScatterPlot")
    public R threeIndexScatterPlot(String country,String aIndex1,String aIndex2,String aIndex3) {
        //返回前端：下属的省份名+误差棒的六个数据  按照两个list

        List allData = new LinkedList();
        List data1 = dataAnalysisService.oneIndexScatterPlot(country,aIndex1);
        System.out.println("data1"+data1);
        List data2 = dataAnalysisService.oneIndexScatterPlot(country,aIndex2);
        System.out.println("data2"+data2);
        List data3 = dataAnalysisService.oneIndexScatterPlot(country,aIndex3);
        //前端需要[指标一，指标二，指标三]这样的三个数据组成一个点
        int data1Len = data1.size();
        int data2Len = data2.size();
        int data3Len = data3.size();
        //求三个list的最小值
        int min = ((data1Len < data2Len) ? data1Len : data2Len)<data3Len?((data1Len < data2Len) ? data1Len : data2Len):data3Len;
        System.out.println("min:"+min);
        //按照最短的遍历
        for(int i=0;i<min;i++){
            List onePoint = new LinkedList();
            onePoint.add(data1.get(i));
            System.out.println(data1.get(i));
            onePoint.add(data2.get(i));
            onePoint.add(data3.get(i));
            allData.add(onePoint);
        }

        List indexList  = new LinkedList();
        indexList.add(aIndex1);
        indexList.add(aIndex2);
        indexList.add(aIndex3);

        return R.ok().put("data", allData).put("indexList", indexList);
    }



    @ApiOperation(value = "传入国家名-不定属性的雷达图")
    @GetMapping("/anyNumIndexRadarPlot")
    @RequiresPermissions("ag:dataAnalysis:anyNumIndexRadarPlot")
    public R anyNumIndexRadarPlot(String country,String[] aIndexs) {

        //最大值的效果不好

        String indexSQLList = "";
        for(String aIndex:aIndexs){
            //System.out.println("拼接"+aIndex);
            String indexSQL = String.format(" if(max(%s) is null ,0,max(%s)) max%s,if(min(%s) is null ,0,min(%s)) min%s ", aIndex, aIndex, aIndex, aIndex, aIndex, aIndex);
            indexSQLList = indexSQLList+indexSQL+",";
        }
        indexSQLList=indexSQLList.substring(0, indexSQLList.length()-1);
        String maxSql =  "select DISTINCT "+indexSQLList+"from ag_data " +
                "where  countryCn = " +  "\"" +country +  "\"" + ";";
        List maxData = jdbcTemplate.queryForList(maxSql);
        List<HashMap> indicators = new LinkedList();

        for(int i=0;i<maxData.size();i++){
            //每一个取出来的是LinkedCaseInsensitiveMap类型的，只能用map，不能用hashmap
            Map<String, Object> tempMap = (Map<String, Object>) maxData.get(i);
            tempMap.forEach((key, value) -> {
                HashMap indicator = new HashMap();
                if(key.contains("max")){
                    //如果键中包含最大值
                    indicator.put("name",key.replace("max",""));
                    if((Double) value>=0){
                        indicator.put("max",(Double) value*1.1);
                    }else{
                        indicator.put("max",(Double) value*0.9);
                    }
                }else{
                    //如果键中包含最大值
                    indicator.put("name",key.replace("min",""));
                    if((Double) value>=0){
                        indicator.put("min",(Double) value*0.5);
                    }else{
                        //负值
                        indicator.put("min",(Double) value*2);
                    }
                }
                indicators.add(indicator);
            });
        }

        List<HashMap> indis = new LinkedList();

        for(int i=0;i<indicators.size()-1;i++){
            HashMap indi = new HashMap();
            System.out.println(indicators.get(i));
            System.out.println(indicators.get(i).get("name"));

            if(indicators.get(i).get("name").equals(indicators.get(i+1).get("name"))){
                System.out.println(indicators.get(i).get("name"));
                //如果名字判断一样
                indi.put("name",indicators.get(i).get("name"));
                if(!indicators.get(i).get("max").equals(null)){
                    //如果最大值有东西
                    indi.put("max",indicators.get(i).get("max"));
                    System.out.println(indicators.get(i).get("max"));
                }else if(!indicators.get(i).get("min").equals(null)){
                    //如果最大值有东西
                    indi.put("min",indicators.get(i).get("min"));
                }else {
                    continue;
                }
                if(!indicators.get(i+1).get("min").equals(null)){
                    //如果最小值有东西
                    indi.put("min",indicators.get(i+1).get("min"));
                    System.out.println(indicators.get(i+1).get("min"));
                }else if(!indicators.get(i+1).get("max").equals(null)){
                    //如果后一个最小值有东西
                    indi.put("max",indicators.get(i+1).get("max"));
                }else{
                    continue;
                }

                indis.add(indi);
            }
        }

        String indexAvgSQLList = "";
        for(String aIndex:aIndexs){
            String indexAvgSQL = String.format(" if(avg(%s) is null ,0,avg(%s)) %s ", aIndex, aIndex, aIndex);
            indexAvgSQLList = indexAvgSQLList+indexAvgSQL+",";
        }
        indexAvgSQLList=indexAvgSQLList.substring(0, indexAvgSQLList.length()-1);

        String avgSql =  "select DISTINCT provinceCn,"+indexAvgSQLList+"from ag_data " +
               " WHERE provinceCn in(select distinct  provinceCn from ag_data ) and countryCn = " +  "\"" +country +  "\"" +
                "GROUP BY provinceCn;";
        List avgData = jdbcTemplate.queryForList(avgSql);
        List dataList = new LinkedList();
        for(int i=0;i<avgData.size();i++){
            //每一个取出来的是LinkedCaseInsensitiveMap类型的，只能用map，不能用hashmap
            Map<String, Object> tempMap = (Map<String, Object>) avgData.get(i);

            HashMap indicator = new HashMap();
            List avgDataList = new LinkedList();
            tempMap.forEach((key, value) -> {

                if(key.equals("provinceCn")){
                    indicator.put("name",value);
                }else{
                    avgDataList.add(value);
                }
            });
            indicator.put("value",avgDataList);
            dataList.add(indicator);
        }

        List allProvince = dataMangeService.countryProvince(country);

        return R.ok().put("indis",indis).put("data", dataList).put("allProvince", allProvince).put("aIndexs", aIndexs);
    }

    @ApiOperation(value = "传入参数查询数据")
    @GetMapping("/queryAllData")
    @RequiresPermissions("ag:dataAnalysis:queryAllData")
    public R queryAllData(String page, String countryCn, String species, int[] unusedIds) {
        int pageNumber;
        if (page == null) {
            pageNumber = -1;
        }
        else {
            try {
                pageNumber = Integer.parseInt(page);
                if (pageNumber < -1){
                    return R.error("page must be a non-negative integer");
                }
            }catch (Exception e) {
                return R.error("page must be a non-negative integer");
            }
        }

        List<Map<String, Object>> data = dataAnalysisService.queryAllData(pageNumber, countryCn, species, unusedIds);
        return R.ok().put("data", data);
    }

    @ApiOperation(value = "查询两个指标的数据")
    @GetMapping("/getTwoTargetData")
    @RequiresPermissions("ag:dataAnalysis:getTwoTargetData")
    public R getTwoTargetData(String target1, String target2) {
        if (target1 == null || target2 == null) return R.error("need two targets");
        List<List<Double>> data = dataAnalysisService.getTwoTargetData(target1, target2);
        return R.ok().put("data", data);
    }

    @ApiOperation(value = "查询多指标的数据")
    @GetMapping("/getMoreTargetData")
    @RequiresPermissions("ag:dataAnalysis:getMoreTargetData")
    public R getMoreTargetData(String[] targets) {
        if (targets == null || (targets.length & 1) == 1 || targets.length > 8) {
            return R.error("number of targets must be an even x, where 2 <= x <= 8");
        }
        List<List<List<Double>>> data = dataAnalysisService.getMoreTargetData(targets);
        return R.ok().put("data", data);
    }

}