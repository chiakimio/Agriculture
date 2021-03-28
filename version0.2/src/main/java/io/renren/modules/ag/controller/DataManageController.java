package io.renren.modules.ag.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import io.renren.common.utils.R;
import io.renren.common.utils.StringUtil;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.DataMangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import static io.renren.common.utils.StringUtil.deepCopy;


/**
 * 农业数据表
 *
 * @author Guoping
 *
 * @date 2021-02-03 19:48:29
 */
@RestController
@RequestMapping("ag/dataManage")
@Api(tags = "数据管理")

public class DataManageController {
    @Autowired
    private DataMangeService dataMangeService;
    
    @Value("${file-save-path}")
    private String filePath;
    //@Value("${serverInfo.uploadpath}")

    private StringUtil stringUtil;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ApiOperation(value = "上传EXCEL", notes = "添加文件")
    @PostMapping("/uploadExcel")
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
    @GetMapping("/downloadExcel")
    @ApiOperation("excel模板下载")
    @RequiresPermissions("ag:dataManage:downloadExcel")
    public R excelDownload(HttpServletResponse response, HttpServletRequest request)throws Exception{

        List allExcelHeaders = dataMangeService.allExcelHeaders();

        List<List<String>> rows = CollUtil.newArrayList(allExcelHeaders);

        ExcelWriter writer = ExcelUtil.getWriter();
        //跳过当前行，既第一行，非必须，在此演示用
        //writer.passCurrentRow();

        StyleSet styleSet = writer.getStyleSet(); //样式集
        CellStyle headCellStyle = styleSet.getHeadCellStyle();//标题样式
        //一次性写出内容，强制输出标题
        writer.write(rows);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=formwork.xls");
        response.setHeader("Access-Control-Expose-Headers","formwork.xls");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out);
        writer.close();
        IoUtil.close(out);
        return R.ok();
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("获得列表信息.")
    @RequiresPermissions("ag:dataManage:list")
    public R list(){

        List allColumn = dataMangeService.allColumn();
        List allData = dataMangeService.allData();

        return R.ok().put("allColumn", allColumn).put("data", allData);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据id查询.")
    @RequiresPermissions("ag:dataManage:info")
    public R info(@PathVariable("id") Long id){
		DataEntity data = dataMangeService.getById(id);

        return R.ok().put("data", data);
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation("新增信息.")
    @RequiresPermissions("ag:dataManage:save")
    public R save(@RequestBody DataEntity data){
		dataMangeService.save(data);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("更新信息.")
    @RequiresPermissions("ag:dataManage:update")
    public R update(@RequestBody DataEntity data){
		dataMangeService.updateById(data);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除.")
    @RequiresPermissions("ag:dataManage:delete")
    public R delete(@RequestBody Long[] ids){
		dataMangeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 数据管理-数据关系展示
     */
    @GetMapping("/dataRelation")
    @ApiOperation("数据管理-数据关系展示.")
    @RequiresPermissions("ag:dataManage:dataRelation")
    public R dataRelation(){

        List dataRelation = dataMangeService.dataRelation();

        return R.ok().put("dataRelation", dataRelation);
    }

    /**
     * 数据管理-查询国家-年份-数量的图及下方的点击选项
     */
    @GetMapping("/countryYearNum")
    @ApiOperation("数据管理-查询国家-年份-数量的图及下方的点击选项.")
    @RequiresPermissions("ag:dataManage:countryYearNum")
    public R countryYearNum(){
        //给前端传 所有国家-所有年份 的数量  有些可能为0 ，数据库查不到，所以需要我来手动 匹配循环
        //下方选项
        //初始化呀！！！
        List<Object> allCountry = dataMangeService.allCountry();
        List<Object> allYear = dataMangeService.allYear();
        List<Object> allSpecial = dataMangeService.allSpecial();
        List<Object> allProvince = dataMangeService.allProvince();
        List<Object> allCity = dataMangeService.allCity();
        List allOrganization = dataMangeService.allOrganization();
        List allIndex = dataMangeService.allIndex();


        //柱状图的数据
        List countryYearNum = dataMangeService.countryYearNum();
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
    @GetMapping("/provinceYearNum")
    @ApiOperation("数据管理-传入国家名称-查询省份-年份-数量的图及下方的点击选项.")
    @RequiresPermissions("ag:dataManage:provinceYearNum")
    public R provinceYearNum(String country){
        //给前端传 某一国家所有省份-所有年份 的数量  有些可能为0 ，数据库查不到，所以需要我来手动 匹配循环

        //下方选项(查询国家的选项)
        List allProvince = dataMangeService.countryProvince(country);
        List allCity = dataMangeService.countryCity(country);
        List allYear = dataMangeService.countryYear(country);
        List allSpecial = dataMangeService.countrySpecial(country);
        //图的数据
        List provinceYearNum = dataMangeService.provinceYearNum(country);
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
    @RequiresPermissions("ag:cityYearNum")
    public R cityYearNum(String countryCn,String provinceCn){

        //下方选项(查询国家的选项)
        List allCity = dataMangeService.countryProvinceCity(countryCn,provinceCn);
        List allYear = dataMangeService.countryProvinceYear(countryCn,provinceCn);
        List allSpecial = dataMangeService.countryProvinceSpecial(countryCn,provinceCn);
        //图的数据
        List cityYearNum = dataMangeService.cityYearNum(countryCn,provinceCn);
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
    @GetMapping("/specialYearNum")
    @ApiOperation("数据管理-传入国家-省份-地级市-查询物种-年份-数量的图及下方的点击选项")
    @RequiresPermissions("ag:dataManage:specialYearNum")
    public R specialYearNum(String countryCn,String provinceCn,String cityCn){

        //下方选项
        List allYear = dataMangeService.countryProvinceCityYear(countryCn,provinceCn,cityCn);
        List allSpecial = dataMangeService.countryProvinceCitySpecial(countryCn,provinceCn,cityCn);
        //图的数据
        List specialYearNum = dataMangeService.specialYearCityNum(countryCn,provinceCn,cityCn);
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
    @GetMapping("/specialCountryYearNum")
    @ApiOperation("数据管理-传入物种名称-查询-所有国家的所有年份的数量 及下面的选项")
    @RequiresPermissions("ag:dataManage:specialCountryYearNum")
    public R specialCountryYearNum(String special){

        List allCountry = dataMangeService.specialCountry(special);
        List allYear = dataMangeService.specialYear(special);
        List allProvince = dataMangeService.specialProvince(special);
        List allCity = dataMangeService.specialCity(special);
        List<Object> allSpecial = dataMangeService.allSpecial();
        //图的数据
        List specialCountryYearNum = dataMangeService.specialCountryYearNum(special);
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



}
