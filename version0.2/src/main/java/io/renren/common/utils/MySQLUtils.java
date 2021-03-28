package io.renren.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import static io.renren.common.utils.StringUtil.deepCopy;

public class MySQLUtils {


    /**
     * 计算list的平均值
     */
    @Autowired
    private static JdbcTemplate jdbcTemplate;
    public  static List<String> getColumnList() throws IOException, ClassNotFoundException {
        //读取数据库目前的字段
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select CONCAT(COLUMN_NAME) from information_schema.COLUMNS where table_name = 'ag_data' and table_schema = 'agriculture';");
        List columnList = new ArrayList();
        for (Map<String, Object> map : maps) {
            String value = (String) map.get("CONCAT(COLUMN_NAME)");
            //System.out.println(" "+value);
            columnList.add(value);
        }
        //System.out.println();
       //System.out.println("columnList:" + columnList);

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
        //System.out.println("columnListAfter   " + columnListAfter);

        return columnListAfter;
    }
}
