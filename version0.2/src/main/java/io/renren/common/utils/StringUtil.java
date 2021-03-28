package io.renren.common.utils;

import java.io.*;
import java.util.List;

public class StringUtil {

    /**
     * 处理年份 读取出来之后变成2018.0 转不了整形的问题
     */
    public  static String removeToInt(String str){

        if(str.contains(".")) {
            int indexOf = str.indexOf(".");
            str = str.substring(0, indexOf);
        }
        return str;
    }

    /**
     * 深拷贝
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List deepCopy(List src) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);
        List dest = (List)in.readObject();
        return dest;
    }
}
