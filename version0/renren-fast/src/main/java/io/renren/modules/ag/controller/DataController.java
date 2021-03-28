package io.renren.modules.ag.controller;

import java.io.*;
import java.util.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.renren.common.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.DataService;
import org.springframework.web.multipart.MultipartFile;
import static io.renren.common.utils.StringUtil.deepCopy;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 农业数据表
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
@RestController
@RequestMapping("ag")
@Api(tags = "农业数据表")

public class DataController {
    @Autowired
    private DataService dataService;
    @Value("${file-save-path}")
    private String filePath;
    //@Value("${serverInfo.uploadpath}")

    private StringUtil stringUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "上传EXCEL", notes = "添加文件")
    @PostMapping("/dataManage/uploadExcel")
    @RequiresPermissions("ag:dataManage:uploadExcel")
    public R uploadExcel(HttpServletRequest request, @RequestParam MultipartFile file) {

        //读取数据库目前的字段
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select CONCAT(COLUMN_NAME) from information_schema.COLUMNS where table_name = 'ag_data' and table_schema = 'agriculture';");
        List columnList = new ArrayList();
        for (Map<String, Object> map : maps
        ) {
            String value = (String) map.get("CONCAT(COLUMN_NAME)");
            //System.out.println(" "+value);
            columnList.add(value);
        }
        System.out.println();
        System.out.println("columnList:"+columnList);


        Map map = new HashMap();
        InputStream is = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String newFileName = file.getOriginalFilename();
            File newFile = new File(filePath + newFileName);
            //复制操作
            file.transferTo(newFile);
            //读取保存的文件
            is = new FileInputStream((filePath + newFileName));
            Workbook wb = null;
            if ((filePath + newFileName).endsWith(".xlsx")) {
                wb = new XSSFWorkbook(is);
            } else if ((filePath + newFileName).endsWith(".xls") || (filePath + newFileName).endsWith(".et")) {
                wb = new HSSFWorkbook(is);
            }
            /* 读EXCEL文字内容 */
            // 获取第一个sheet表，也可使用sheet表名获取
            Sheet sheet = wb.getSheetAt(0);

            List<Map<String, String>> sheetList = new ArrayList<Map<String, String>>();

            List<String> titles = new ArrayList<String>();
            int rowSize = sheet.getLastRowNum() + 1;

            System.out.println("文件行数  "+rowSize);

            for (int j = 0; j < rowSize; j++) {
                Row row = sheet.getRow(j);
                if (row == null) {
                    continue;
                }
                int cellSize = row.getLastCellNum();
                //表头
                if (j == 0) {
                    for (int k = 0; k < cellSize; k++) {
                        Cell cell = row.getCell(k);
                        titles.add(cell.toString());

                    }
                } else {
                    Map<String, String> rowMap = new HashMap<String, String>();
                    for (int k = 0; k < titles.size(); k++) {
                        Cell cell = row.getCell(k);
                        String key = titles.get(k);
                        String value = null;
                        if (cell != null) {
                            value = cell.toString();
                        }
                        rowMap.put(key, value);
                    }
                    sheetList.add(rowMap);
                    //rowMap.clear();
                }
            }
            wb.close();
            is.close();
            System.out.println("titles"+titles);
            /*
             * 现在已经从数据库字段和excel表头分别读到
             * columnList  数据库字段
             * titles      excel表头
             */
            List<String> columnListAfter = deepCopy(columnList);
            columnListAfter.remove("id");  //向列表中添加数据
            columnListAfter.remove("dataFrom");
            columnListAfter.remove("countryCn");
            columnListAfter.remove("countryEn");
            columnListAfter.remove("provinceCn");
            columnListAfter.remove("provinceEn");
            columnListAfter.remove("cityCn");
            columnListAfter.remove("cityEn");
            columnListAfter.remove("yearJc");
            columnListAfter.remove("species");
            columnListAfter.remove("longitude");
            columnListAfter.remove("dimension");
            System.out.println("columnListAfter   "+columnListAfter);

            List<String> titlesAfter = deepCopy(titles);
            //删除无关项
            titlesAfter.remove("数据来源");
            titlesAfter.remove("国家名称");
            titlesAfter.remove("Name of country");
            titlesAfter.remove("省份名称");
            titlesAfter.remove("Name of province");
            titlesAfter.remove("城市名称");
            titlesAfter.remove("Name of city");
            titlesAfter.remove("年份");
            titlesAfter.remove("物种");
            titlesAfter.remove("经度（°E）");
            titlesAfter.remove("纬度（°N）");

            System.out.println("titlesAfter   "+titlesAfter);
            System.out.println("titles   "+titles);

            List<String> newColumnList = new ArrayList<String>();
            //这两个list必然是一个包含一个，找出多余项只需要减去即可
            //使用大小判断不合适，因为excel的表头可能少于数据库字段，但是有新的，一定要找出新的
            for(String t:titlesAfter){
                if(columnList.contains(t)){
                    continue;
                }else {
                    //如果字段中没有这个元素
                    newColumnList.add(t);
                }
            }
            System.out.println("newColumnList"+newColumnList);
            for (String ncl : newColumnList) {
                String sql = "ALTER TABLE ag_data ADD " + ncl + " DOUBLE(255,15) DEFAULT NULL " ;
                jdbcTemplate.execute(sql);
            }
            System.out.println("执行完毕 "+newColumnList+" 字段插入成功");

            String[]  keys = new String[]{"key1","key2","key3","key4","key5","key6","key7","key8","key9","key10",
                    "key11","key12","key13","key14","key15","key16","key17","key18","key19","key20"};

            List<String> columnListChinese = deepCopy(columnList);
            Collections.replaceAll(columnListChinese, "dataFrom","数据来源");
            Collections.replaceAll(columnListChinese, "countryCn","国家名称");
            Collections.replaceAll(columnListChinese, "countryEn","Name of country");
            Collections.replaceAll(columnListChinese, "provinceCn","省份名称");
            Collections.replaceAll(columnListChinese, "provinceEn","Name of province");
            Collections.replaceAll(columnListChinese, "cityCn","城市名称");
            Collections.replaceAll(columnListChinese, "cityEn","Name of city");
            Collections.replaceAll(columnListChinese, "yearJc","年份");
            Collections.replaceAll(columnListChinese, "species","物种");
            Collections.replaceAll(columnListChinese, "longitude","经度（°E）");
            Collections.replaceAll(columnListChinese, "dimension","纬度（°N）");

            System.out.println("columnListChinese "+columnListChinese);

            //顺序应该一致，哎，没有更好的办法了，只能认为顺序一致
            //顺序不一致啊啊啊啊
            //怎么处理从里面获取数据，还能对应上呢？
            //params {年份=2018.0, δ13C=-27.7831247431398, δ2H=-45.1324284008629, 物种=枸杞, 纬度（°N）=30.2344, δ32W=23.3432, Name of province=Zhejiang, 经度（°E）=118.1223, δ32S=null, 国家名称=中国, Name of country=China, 省份名称=浙江, 数据来源=浙江农业科学院, δ15N=null, δ8X=12.6, δ18O=45.17295273723358}

            //直接把id去了
            columnList.remove("id");
            columnListChinese.remove("id");
            //数据库目前的字段
            System.out.println("columnList   "+columnList);
            System.out.println("columnListChinese "+columnListChinese);
            //sql语句，为之后的批量执行
            List<String> SQLlist = new ArrayList<String>();
            for (Map<String, String> params : sheetList) {

                //这里应该是数据库字段 的长度，这样才好对应插入数据
                //一次循环，一条数据
                String insertSQL="";
                String insertColumnSQL = "";
                String insertDataSQL = "";
                for(int i=0;i<columnList.size();i++){
                    keys[i] = columnListChinese.get(i);
                    if(params.get("数据来源")==null) {
                        continue;
                    }else{
                        //System.out.print(" " + columnList.get(i) + " " + params.get(keys[i])+" ");
                        if(i==0){
                            insertColumnSQL = insertColumnSQL+columnList.get(i);
                            insertDataSQL = insertDataSQL +"'"+params.get(keys[i])+"'";

                        }else if(params.get(keys[i])==null||params.get(keys[i]).equals("")){
                            //如果值为null，那么不需要加单引号
                            insertColumnSQL = insertColumnSQL+","+columnList.get(i);
                            insertDataSQL = insertDataSQL +",null";
                        }else if(columnList.get(i)=="yearJc"||columnList.get(i).equals("yearJc")){
                            String year  = params.get(keys[i]);
                            if(year.contains(".")) {
                                int indexOf = year.indexOf(".");
                                year = year.substring(0, indexOf);
                            }
                            insertColumnSQL = insertColumnSQL+","+columnList.get(i);
                            insertDataSQL = insertDataSQL+","+"'"+year+"'" ;
                        }else{
                            insertColumnSQL = insertColumnSQL+","+columnList.get(i);
                            insertDataSQL = insertDataSQL +","+"'"+params.get(keys[i])+"'";
                        }
                        //INSERT INTO ag_data (dataFrom,countryCn,countryEn,provinceCn,provinceEn,yearJc,species,longitude,dimension,a13c,a15n,a2h,a18o,a32s) VALUES (#{dataFrom},#{countryCn},#{countryEn},#{provinceCn},#{provinceEn},#{yearJc},#{species},#{longitude},#{dimension},#{a13c},#{a15n},#{a2h},#{a18o},#{a32s})
                    }
                }
                if(params.get("数据来源")!=null) {
                    insertSQL = "INSERT INTO ag_data ("+insertColumnSQL+") VALUES ( "+insertDataSQL+")";
                    SQLlist.add(insertSQL);
                    //System.out.println();
                }
            }
            //System.out.println("SQLlist "+SQLlist);

            for (String sqll : SQLlist) {
                jdbcTemplate.execute(sqll);
            }
            System.out.println("数据插入完成");

            //展示前十条
            List<Map<String, String>> sheetListPart = new ArrayList<Map<String, String>>();
            if(sheetList.size()>10){
                for(int i=0;i<10;i++){
                    System.out.println(sheetList.get(i));
                    sheetListPart.add(sheetList.get(i));
                }
            }else{
                for(int i=0;i<sheetList.size();i++){
                    System.out.println(sheetList.get(i));
                    sheetListPart.add(sheetList.get(i));
                }
            }
            return R.ok().put("titles", titles).put("sheetListPart", sheetListPart);


        } catch (Exception ex) {
            ex.printStackTrace();

            return R.error();
        }
    }

    /**
     * excel模板下载
     */
    @GetMapping("/dataManage/downloadExcel")
    @ApiOperation("excel模板下载")
    @RequiresPermissions("ag:dataManage:downloadExcel")
    public R excelDownload(HttpServletResponse response, HttpServletRequest request)throws Exception{
        List<String> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd");
        List<String> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1");
        List<String> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2");
        List<String> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3");
        List<String> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4");

        List<List<String>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);

        ExcelWriter writer = ExcelUtil.getWriter();
        //跳过当前行，既第一行，非必须，在此演示用
        writer.passCurrentRow();


        //一次性写出内容，强制输出标题
        writer.write(rows);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out);
        writer.close();
        IoUtil.close(out);
        return R.ok();
    }

    /**
     * 列表
     */
    @GetMapping("/dataManage/list")
    @ApiOperation("获得列表信息.")
    @RequiresPermissions("ag:dataManage:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = dataService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/dataManage/info/{id}")
    @ApiOperation("根据id查询.")
    @RequiresPermissions("ag:dataManage:info")
    public R info(@PathVariable("id") Long id){
		DataEntity data = dataService.getById(id);

        return R.ok().put("data", data);
    }

    /**
     * 新增
     */
    @PostMapping("/dataManage/save")
    @ApiOperation("新增信息.")
    @RequiresPermissions("ag:dataManage:save")
    public R save(@RequestBody DataEntity data){
		dataService.save(data);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/dataManage/update")
    @ApiOperation("更新信息.")
    @RequiresPermissions("ag:dataManage:update")
    public R update(@RequestBody DataEntity data){
		dataService.updateById(data);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/dataManage/delete")
    @ApiOperation("删除.")
    @RequiresPermissions("ag:dataManage:delete")
    public R delete(@RequestBody Long[] ids){
		dataService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 数据管理-数据关系展示
     */
    @GetMapping("/dataManage/dataRelation")
    @ApiOperation("数据管理-数据关系展示.")
    @RequiresPermissions("ag:dataManage:dataRelation")
    public R dataRelation(){

        List dataRelation = dataService.dataRelation();

        return R.ok().put("dataRelation", dataRelation);
    }

    /**
     * 数据管理-查询国家-年份-数量的图及下方的点击选项
     */
    @GetMapping("/dataManage/countryYearNum")
    @ApiOperation("数据管理-查询国家-年份-数量的图及下方的点击选项.")
    @RequiresPermissions("ag:dataManage:countryYearNum")
    public R countryYearNum(){
        //给前端传 所有国家-所有年份 的数量  有些可能为0 ，数据库查不到，所以需要我来手动 匹配循环
        //下方选项
        //初始化呀！！！
        List<Object> allCountry = dataService.allCountry();
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List<Object> allProvince = dataService.allProvince();
        List<Object> allCity = dataService.allCity();
        List allOrganization = dataService.allOrganization();
        List allIndex = dataService.allIndex();

        //柱状图的数据
        List countryYearNum = dataService.countryYearNum();
        //根据前端需要处理格式
        List<Object> countryYearNumData = new LinkedList<>();

        for(Object c : allCountry) {
            List<Object> result = new LinkedList<>();
            HashMap<String,String> country = new HashMap<>();
            country.put("country", (String) c);
            @SuppressWarnings("rawtypes")
            HashMap<Object, Map<Object, Object>> value = new HashMap<>();
            HashMap<Object, Object> data = new HashMap<>();

            for (int i = 0; i < countryYearNum.size(); i++) {
                HashMap cyn = (HashMap) countryYearNum.get(i);
                //除去非空
                if (cyn == null || cyn.equals("")) {
                    continue;
                } else {
                    Object cyountry =  cyn.get("countryCn");
                    if(c.equals(cyountry)){
                        data.put(cyn.get("yearJc"),cyn.get("num"));
                        value.put("value",data);
                    }
                }
            }
            result.add(country);
            result.add(value);
            countryYearNumData.add(result);
        }

        return R.ok().put("data", countryYearNumData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial).put("allYear", allYear).put("allIndex", allIndex).put("allOrganization", allOrganization);
    }

    /**
     * 数据管理-传入国家名称 - 查询省份-年份-数量的图及下方的点击选项
     */
    @GetMapping("/dataManage/provinceYearNum")
    @ApiOperation("数据管理-传入国家名称-查询省份-年份-数量的图及下方的点击选项.")
    @RequiresPermissions("ag:dataManage:provinceYearNum")
    public R provinceYearNum(String country){
        //给前端传 某一国家所有省份-所有年份 的数量  有些可能为0 ，数据库查不到，所以需要我来手动 匹配循环

        //下方选项(查询国家的选项)
        List allProvince = dataService.countryProvince(country);
        List allCity = dataService.countryCity(country);
        List allYear = dataService.countryYear(country);
        List allSpecial = dataService.countrySpecial(country);


        //图的数据
        List provinceYearNum = dataService.provinceYearNum(country);
        List<Object> provinceYearNumData = new LinkedList<>();

        for(Object p : allProvince) {
            List<Object> result2 = new LinkedList<>();
            HashMap<String,String> province2 = new HashMap<>();
            province2.put("province", (String) p);
            @SuppressWarnings("rawtypes")
            HashMap<Object, Map<Object, Object>> value = new HashMap<>();
            HashMap<Object, Object> data = new HashMap<>();

            for (int i = 0; i < provinceYearNum.size(); i++) {
                HashMap pyn = (HashMap) provinceYearNum.get(i);
                //除去空
                if (pyn == null || pyn.equals("")) {
                    continue;
                } else {
                    Object pyrovince =  pyn.get("provinceCn");

                    if(p.equals(pyrovince)){
                        data.put(pyn.get("yearJc"),pyn.get("num"));
                        value.put("value",data);
                    }
                }
            }
            result2.add(province2);
            result2.add(value);
            provinceYearNumData.add(result2);
        }
        return R.ok().put("data", provinceYearNumData).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial).put("allYear", allYear);
    }

    /**
     * 数据管理-传入国家-省份-查询地级市-年份-数量的图及下方的点击选项
     */
    @GetMapping("/dataManage/cityYearNum")
    @ApiOperation("数据管理-传入国家-省份-查询地级市-年份-数量的图及下方的点击选项")
    @RequiresPermissions("ag:dataManage:cityYearNum")
    public R cityYearNum(String countryCn,String provinceCn){

        //下方选项(查询国家的选项)
        List allCity = dataService.countryProvinceCity(countryCn,provinceCn);
        List allYear = dataService.countryProvinceYear(countryCn,provinceCn);
        List allSpecial = dataService.countryProvinceSpecial(countryCn,provinceCn);
        //图的数据

        List cityYearNum = dataService.cityYearNum(countryCn,provinceCn);
        List<Object> cityYearNumData = new LinkedList<>();

        for(Object c : allCity) {
            List<Object> result3 = new LinkedList<>();
            HashMap<String,String> city2 = new HashMap<>();
            city2.put("city", (String) c);
            HashMap<Object, Map<Object, Object>> value = new HashMap<>();
            HashMap<Object, Object> data2 = new HashMap<>();

            for (int i = 0; i < cityYearNum.size(); i++) {
                HashMap cyn = (HashMap) cityYearNum.get(i);
                System.out.println("cyn === "+cyn);
                //除去空
                if (cyn == null || cyn.equals("")) {
                    continue;
                } else {
                    Object city =  cyn.get("cityCn");
                    if(c.equals(city)){
                        data2.put(cyn.get("yearJc"),cyn.get("num"));
                        value.put("value",data2);
                    }
                }
            }
            result3.add(city2);
            result3.add(value);
            cityYearNumData.add(result3);
        }
        return R.ok().put("data", cityYearNumData).put("allCity",allCity).put("allSpecial", allSpecial).put("allYear", allYear);
    }

    /**
     * 数据管理-传入国家-省份-地级市-查询物种-年份-数量的图及下方的点击选项
     */
    @GetMapping("/dataManage/specialYearNum")
    @ApiOperation("数据管理-传入国家-省份-地级市-查询物种-年份-数量的图及下方的点击选项")
    @RequiresPermissions("ag:dataManage:specialYearNum")
    public R specialYearNum(String countryCn,String provinceCn,String cityCn){

        //下方选项
        List allYear = dataService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List allSpecial = dataService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        //图的数据
        List specialYearNum = dataService.specialYearCityNum(countryCn,provinceCn,cityCn);
        List<Object> specialYearNumData = new LinkedList<>();
        for(Object s : allSpecial) {
            List<Object> result3 = new LinkedList<>();
            HashMap<String,String> special2 = new HashMap<>();
            special2.put("special", (String) s);
            HashMap<Object, Map<Object, Object>> value = new HashMap<>();
            HashMap<Object, Object> data = new HashMap<>();

            for (int i = 0; i < specialYearNum.size(); i++) {
                HashMap syn = (HashMap) specialYearNum.get(i);
                //除去空
                if (syn == null || syn.equals("")) {
                    continue;
                } else {
                    Object sypecial =  syn.get("species");

                    if(s.equals(sypecial)){
                        data.put(syn.get("yearJc"),syn.get("num"));
                        value.put("value",data);
                    }
                }
            }
            result3.add(special2);
            result3.add(value);
            specialYearNumData.add(result3);
        }
        return R.ok().put("data", specialYearNumData).put("allSpecial", allSpecial).put("allYear", allYear);
    }

    /**
     * 数据管理-传入物种名称-查询-所有国家的所有年份的数量 及下面的选项
     *
     */
    @GetMapping("/dataManage/specialCountryYearNum")
    @ApiOperation("数据管理-传入物种名称-查询-所有国家的所有年份的数量 及下面的选项")
    @RequiresPermissions("ag:dataManage:specialCountryYearNum")
    public R specialCountryYearNum(String special){

        List allCountry = dataService.specialCountry(special);
        List allYear = dataService.specialYear(special);
        List allProvince = dataService.specialProvince(special);
        List allCity = dataService.specialCity(special);
        List<Object> allSpecial = dataService.allSpecial();
        //图的数据

        List specialCountryYearNum = dataService.specialCountryYearNum(special);
        List<Object> specialCountryYearNumData = new LinkedList<>();

        for(Object c : allCountry) {
            List<Object> result = new LinkedList<>();
            HashMap<String,String> country = new HashMap<>();
            country.put("country", (String) c);
            @SuppressWarnings("rawtypes")
            HashMap<Object, Map<Object, Object>> value = new HashMap<>();
            HashMap<Object, Object> data = new HashMap<>();

            for (int i = 0; i < specialCountryYearNum.size(); i++) {
                HashMap cyn = (HashMap) specialCountryYearNum.get(i);
                //除去非空
                if (cyn == null || cyn.equals("")) {
                    continue;
                } else {
                    Object cyountry =  cyn.get("countryCn");
                    if(c.equals(cyountry)){
                        data.put(cyn.get("yearJc"),cyn.get("num"));
                        value.put("value",data);
                    }
                }
            }
            result.add(country);
            result.add(value);
            specialCountryYearNumData.add(result);
        }
        return R.ok().put("data", specialCountryYearNumData).put("allSpecial", allSpecial).put("allSpecial", allSpecial).put("allYear", allYear).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity);
    }

    //分布图
    /**
     * 分布图-传入指标名称 - 查询-国家和平均值
     *
     */
    @GetMapping("/areaMap/columnCountryAndAvgData")
    @ApiOperation("分布图-传入指标名称 - 查询-国家和对应的平均值")
    @RequiresPermissions("ag:areaMap:columnCountryAndAvgData")
    public R columnCountryAndAvgData(String  aIndex) throws IOException, ClassNotFoundException {
        //地图的数据
        List countryAndAvgData = dataService.columnCountryAndAvgData(aIndex);

        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();


        return R.ok().put("data", countryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标名称+物种 - 查询-国家和对应的数据平均值
     *
     */
    @GetMapping("/areaMap/columnSpecialCountryAndAvgData")
    @ApiOperation("分布图-传入指标名称+物种 - 查询-国家和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnSpecialCountryAndAvgData")
    public R columnSpecialCountryAndAvgData(String  aIndex,String special){

        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnSpecialCountryAndAvgData = dataService.columnCountrySpecialAndAvgData(aIndex,special);
        return R.ok().put("data", columnSpecialCountryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+年份 -查询-国家和对应的数量
     *
     */
    @GetMapping("/areaMap/columnYearCountryAndAvgData")
    @ApiOperation("分布图-传入指标+年份 - 查询-国家和对应的数量")
    @RequiresPermissions("ag:areaMap:columnYearCountryAndAvgData")
    public R columnYearCountryAndAvgData(String  aIndex,String year){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnYearCountryAndAvgData = dataService.columnCountryYearAndAvgData(aIndex,year);
        return R.ok().put("data", columnYearCountryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+物种+年份 -查询-国家和对应的数量
     *
     */
    @GetMapping("/areaMap/columnSpecialYearCountryAndAvgData")
    @ApiOperation("分布图-传入指标+物种+年份 -查询-国家和对应的数量")
    @RequiresPermissions("ag:areaMap:columnSpecialYearCountryAndAvgData")
    public R columnSpecialYearCountryAndAvgData(String  aIndex,String special,String year){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnSpecialYearCountryAndAvgData = dataService.columnCountrySpecialYearAndAvgData(aIndex,special,year);
        return R.ok().put("data", columnSpecialYearCountryAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标-国家名称 - 查询-省份和对应的数量
     *
     */
    @GetMapping("/areaMap/columnProvinceAndAvgData")
    @ApiOperation("分布图-传入指标-国家名称 - 查询-省份和对应的数量")
    @RequiresPermissions("ag:areaMap:columnProvinceAndAvgData")
    public R columnProvinceAndAvgData(String  aIndex,String countryEn){
        //地图的数据
        List columnProvinceAndAvgData = dataService.columnProvinceAndAvgData(aIndex,countryEn);
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        return R.ok().put("data", columnProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标名称+国家+物种 - 查询-省份和对应的数据平均值
     *
     */
    @GetMapping("/areaMap/columnSpecialProvinceAndAvgData")
    @ApiOperation("分布图-传入指标名称+国家+物种 - 查询-省份和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnSpecialProvinceAndAvgData")
    public R columnSpecialProvinceAndAvgData(String  aIndex,String countryEn,String special){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnSpecialProvinceAndAvgData = dataService.columnProvinceSpecialAndAvgData(aIndex,countryEn,special);
        return R.ok().put("data", columnSpecialProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标名称+国家+物种+年份 - 查询-省份和对应的数据平均值
     *
     */
    @GetMapping("/areaMap/columnYearProvinceAndAvgData")
    @ApiOperation("分布图-传入指标名称+国家+物种+年份 - 查询-省份和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnYearProvinceAndAvgData")
    public R columnYearProvinceAndAvgData(String  aIndex,String countryEn,String year){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnYearProvinceAndAvgData = dataService.columnProvinceYearAndAvgData(aIndex,countryEn,year);
        return R.ok().put("data", columnYearProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+物种+年份 -查询-省份和对应的数量
     *
     */
    @GetMapping("/areaMap/columnSpecialYearProvinceAndAvgData")
    @ApiOperation("分布图-传入指标+物种+年份 -查询-省份和对应的数量")
    @RequiresPermissions("ag:areaMap:columnSpecialYearProvinceAndAvgData")
    public R columnSpecialYearProvinceAndAvgData(String  aIndex,String countryEn,String special,String year){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnSpecialYearProvinceAndAvgData = dataService.columnProvinceSpecialYearAndAvgData(aIndex,countryEn,special,year);
        return R.ok().put("data", columnSpecialYearProvinceAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标-省份名称 - 查询-城市和对应的数量
     *
     */
    @GetMapping("/areaMap/columnCityAndAvgData")
    @ApiOperation("分布图-传入指标-省份名称 - 查询-城市和对应的数量")
    @RequiresPermissions("ag:areaMap:CityAndAvgData")
    public R CityAndAvgData(String  aIndex,String provinceEn){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List CityAndAvgData = dataService.columnCityAndAvgData(aIndex,provinceEn);
        return R.ok().put("data", CityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 分布图-传入指标名称+省份+物种 - 查询-城市和对应的数据平均值
     *
     */
    @GetMapping("/areaMap/columnSpecialCityAndAvgData")
    @ApiOperation("分布图-传入指标名称+省份+物种 - 查询-城市和对应的数据平均值")
    @RequiresPermissions("ag:areaMap:columnSpecialCityAndAvgData")
    public R columnSpecialCityAndAvgData(String  aIndex,String provinceEn,String special){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnSpecialCityAndAvgData = dataService.columnCitySpecialAndAvgData(aIndex,provinceEn,special);
        return R.ok().put("data", columnSpecialCityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+省份+年份 -查询-城市和对应的数量
     *
     */
    @GetMapping("/areaMap/columnYearCityAndAvgData")
    @ApiOperation("分布图-传入指标+省份+年份 -查询-城市和对应的数量")
    @RequiresPermissions("ag:areaMap:columnYearCityAndAvgData")
    public R columnYearCityAndAvgData(String  aIndex,String provinceEn,String year){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnYearCityAndAvgData = dataService.columnCityYearAndAvgData(aIndex,provinceEn,year);
        return R.ok().put("data", columnYearCityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 分布图-传入指标+省份+物种+年份 -查询-城市和对应的数量
     *
     */
    @GetMapping("/areaMap/columnSpecialYearCityAndAvgData")
    @ApiOperation("分布图-传入指标+省份+物种+年份 -查询-城市和对应的数量")
    @RequiresPermissions("ag:areaMap:columnSpecialYearCityAndAvgData")
    public R columnSpecialYearCityAndAvgData(String  aIndex,String provinceEn,String special,String year){
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();
        //地图的数据
        List columnSpecialYearCityAndAvgData = dataService.columnCitySpecialYearAndAvgData(aIndex,provinceEn,special,year);
        return R.ok().put("data", columnSpecialYearCityAndAvgData).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }


    //散点图
    /**
     * 散点图-传入指标名称 - 查询-经纬度和数值
     */
    @GetMapping("/scatterMap/indexLongDimenData")
    @ApiOperation("散点图-传入指标名称 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexLongDimenData")
    public R indexLongDimenData(String  aIndex){
        //原始数据
        List indexLongDimenDatazz = dataService.indexLongDimenZhi(aIndex);

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
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();

        return R.ok().put("data", indexLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }
    /**
     * 散点图-传入指标-物种名称 - 查询-经纬度和数值
     */
    @GetMapping("/scatterMap/indexSpecialLongDimenData")
    @ApiOperation("散点图-传入指标-物种名称 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexSpecialLongDimenData")
    public R indexSpecialLongDimenData( String  aIndex,  String special ){
        //原始数据
        List indexLongDimenDatazz = dataService.indexSpeciesLongDimenZhi(aIndex,special);

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
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();

        return R.ok().put("data", indexSpeciesLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 散点图-传入指标-年份 - 查询-经纬度和数值
     */
    @GetMapping("/scatterMap/indexYearLongDimenData")
    @ApiOperation("散点图-传入指标-年份 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexYearLongDimenData")
    public R indexYearLongDimenData(String  aIndex,String year){
        //原始数据
        List indexLongDimenDatazz = dataService.indexYearLongDimenZhi(aIndex,year);

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
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();

        return R.ok().put("data", indexYearLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 散点图-传入指标-物种-年份 - 查询-经纬度和数值
     */
    @GetMapping("/scatterMap/indexSpecialYearLongDimenData")
    @ApiOperation("散点图-传入指标-物种-年份名称 - 查询-经纬度和数值")
    @RequiresPermissions("ag:scatterMap:indexSpecialYearLongDimenData")
    public R indexSpecialYearLongDimenData(String  aIndex,String special,String year){
        //原始数据
        List indexLongDimenDatazz = dataService.indexSpeciesYearLongDimenZhi(aIndex,special,year);

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
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List allIndex = dataService.allIndex();

        return R.ok().put("data", indexSpeciesYearLongDimenDataList).put("allYear",allYear).put("allSpecial",allSpecial).put("allIndex",allIndex);
    }

    /**
     * 误差棒图-全世界默认信息

     * @return
     */

    @GetMapping("/errorBar/AllIndex")
    @ApiOperation("误差棒图-全世界默认信息")
    @RequiresPermissions("ag:errorBar:AllIndex")
    public R AllIndex() throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object column : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.allIndexZhi((String) column);
            System.out.println(column);
            System.out.println(aIndexData);
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
        List<Object> allCountry = dataService.allCountry();
        List<Object> allYear = dataService.allYear();
        List<Object> allSpecial = dataService.allSpecial();
        List<Object> allProvince = dataService.allProvince();
        List<Object> allCity = dataService.allCity();

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-查询某一国家  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/errorBar/countryAllIndex")
    @ApiOperation("误差棒图-查询某一国家  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryAllIndex")
    public R countryAllIndex(String countryCn) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryAllIndexZhi((String) aIndex,countryCn);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryYear(countryCn);
        List<String> allSpecial = dataService.countrySpecial(countryCn);
        List<String> allProvince = dataService.countryProvince(countryCn);
        List<String> allCity = dataService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-查询某一国家某一物种  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/errorBar/countrySpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countrySpecialAllIndex")
    public R countrySpecialAllIndex(String countryCn,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countrySpeciesAllIndexZhi((String) aIndex,countryCn,special);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryYear(countryCn);
        List<String> allSpecial = dataService.countrySpecial(countryCn);
        List<String> allProvince = dataService.countryProvince(countryCn);
        List<String> allCity = dataService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一年份  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/errorBar/countryYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一年份  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryYearAllIndex")
    public R countryYearAllIndex(String countryCn,String year) throws IOException, ClassNotFoundException {
        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryYearAllIndexZhi((String) aIndex,countryCn,year);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryYear(countryCn);
        List<String> allSpecial = dataService.countrySpecial(countryCn);
        List<String> allProvince = dataService.countryProvince(countryCn);
        List<String> allCity = dataService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一年份某一物种  所有指标的数据
     * 传入国家的汉语名
     * @return
     */
    @GetMapping("/errorBar/countryYearSpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一年份某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryYearSpecialAllIndex")
    public R countryYearSpecialAllIndex(String countryCn,String year,String special) throws IOException, ClassNotFoundException {
        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryYearSpeciesAllIndexZhi((String) aIndex,countryCn,year,special);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryYear(countryCn);
        List<String> allSpecial = dataService.countrySpecial(countryCn);
        List<String> allProvince = dataService.countryProvince(countryCn);
        List<String> allCity = dataService.countryCity(countryCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allProvince", allProvince).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-某个国家-某个省份的所有属性
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceAllIndex")
    @ApiOperation("误差棒图-某个省的所有属性")
    @RequiresPermissions("ag:errorBar:countryProvinceAllIndex")
    public R countryProvinceAllIndex(String countryCn,String provinceCn) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceAllIndexZhi((String) aIndex,countryCn,provinceCn);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一物种  所有指标的数据
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceSpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceSpecialAllIndex")
    public R countryProvinceSpecialAllIndex(String countryCn,String provinceCn,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceSpeciesAllIndexZhi((String) aIndex,countryCn,provinceCn,special);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一年份  所有指标的数据
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一年份  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceYearAllIndex")
    public R countryProvinceYearAllIndex(String countryCn,String provinceCn,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceYearAllIndexZhi((String) aIndex,countryCn,provinceCn,year);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一年份某一物种  所有指标的数据
     * 传入国家-省份的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceYearSpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一年份某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceYearSpecialAllIndex")
    public R countryProvinceYearSpecialAllIndex(String countryCn,String provinceCn,String year,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceYearSpeciesAllIndexZhi((String) aIndex,countryCn,provinceCn,year,special);
            System.out.println(aIndex);
            System.out.println(aIndexData);
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
        List<String> allYear = dataService.countryProvinceYear(countryCn,provinceCn);
        List<String> allSpecial = dataService.countryProvinceSpecial(countryCn,provinceCn);
        List<String> allCity = dataService.countryProvinceCity(countryCn,provinceCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCity", allCity).put("allYear", allYear).put("allSpecial", allSpecial);
    }

    /**
     * 误差棒图-某个国家-某个省份-某个地级市 的所有属性
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceCityAllIndex")
    @ApiOperation("误差棒图-某个市的所有属性")
    @RequiresPermissions("ag:errorBar:countryProvinceCityAllIndex")
    public R countryProvinceCityAllIndex(String countryCn,String provinceCn,String cityCn) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceCityAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn);
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
        List<String> allYear = dataService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一地市某一年份  所有指标的数据
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceCityYearAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一地市某一年份  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceCityYearAllIndex")
    public R countryProvinceCityYearAllIndex(String countryCn,String provinceCn,String cityCn,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceCityYearAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn,year);
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
        List<String> allYear = dataService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一地市某一物种  所有指标的数据
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceCitySpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一地市某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceCitySpecialAllIndex")
    public R countryProvinceCitySpecialAllIndex(String countryCn,String provinceCn,String cityCn,String special) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceCitySpeciesAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn,special);
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
        List<String> allYear = dataService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-查询某一国家某一省份某一地市某一年某一物种所有指标的数据
     * 传入国家-省份-市的汉语名
     * @return
     */

    @GetMapping("/errorBar/countryProvinceCityYearSpecialAllIndex")
    @ApiOperation("误差棒图-查询某一国家某一省份某一地市某一年某一物种  所有指标的数据")
    @RequiresPermissions("ag:errorBar:countryProvinceCitySpecialYearAllIndex")
    public R countryProvinceCityYearSpecialAllIndex(String countryCn,String provinceCn,String cityCn,String special,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.countryProvinceCitySpeciesYearAllIndexZhi((String) aIndex,countryCn,provinceCn,cityCn,year,special);
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
        List<String> allYear = dataService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List<String> allSpecial = dataService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allYear", allYear).put("allSpecial", allSpecial);
    }
    /**
     * 误差棒图-某个物种 的所有属性
     * 传入物种的汉语名
     * @return
     */

    @GetMapping("/errorBar/specialAllIndex")
    @ApiOperation("误差棒图-某个物种的所有属性")
    @RequiresPermissions("ag:errorBar:specialAllIndex")
    public R specialAllIndex(String  special) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.speciesAllIndexZhi((String) aIndex,special);
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
        List<String> allCountry = dataService.specialCountry(special);
        List<String> allYear = dataService.specialYear(special);
        List<Object> allSpecial = dataService.allSpecial();
        List<String> allProvince = dataService.specialProvince(special);
        List<String> allCity = dataService.specialCity(special);

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial).put("allYear", allYear);
    }
    /**
     * 误差棒图-查询某一物种 某一年 所有指标的数据
     * 传入物种的汉语名
     * @return
     */

    @GetMapping("/errorBar/SpecialYearAllIndex")
    @ApiOperation("误差棒图-查询某一物种 某一年 所有指标的数据")
    @RequiresPermissions("ag:errorBar:SpecialYearAllIndex")
    public R SpecialYearAllIndex(String  special,String year) throws IOException, ClassNotFoundException {

        List allIndex = dataService.allIndex();
        //所有指标误差数据列表（指标，最大值，最小值）
        List<Object> errorDataList = new LinkedList<>();
        //平均值柱子
        List<Double> barData = new LinkedList<>();
        //遍历所有属性
        for(Object aIndex : allIndex) {
            //某个指标的检索数据
            List aIndexData = dataService.speciesYearAllIndexZhi((String) aIndex,special,year);
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
        List<String> allCountry = dataService.specialCountry(special);
        List<Object> allSpecial = dataService.allSpecial();
        List<String> allProvince = dataService.specialProvince(special);
        List<String> allCity = dataService.specialCity(special);

        return R.ok().put("allIndex", allIndex).put("errorDataList", errorDataList).put("barData", barData).put("allCountry", allCountry).put("allProvince", allProvince).put("allCity", allCity).put("allSpecial", allSpecial);
    }

}
