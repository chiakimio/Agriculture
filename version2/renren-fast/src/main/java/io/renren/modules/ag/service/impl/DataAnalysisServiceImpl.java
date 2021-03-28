package io.renren.modules.ag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ag.dao.DataAnalysisDao;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.DataAnalysisService;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("dataAnalysisService")
public class DataAnalysisServiceImpl extends ServiceImpl<DataAnalysisDao, DataEntity> implements DataAnalysisService {

    /**
     * 数据来源-物种-年份 三层递进关系
     */
    @Override
    public List dataFromSpecialYear(){
        return baseMapper.dataFromSpecialYear();
    };

    public List dataFromSpecialYear1(){

        List<Object> dataRelation = new LinkedList<>();

        List dataRelation1 = baseMapper.dataFromSpecialYear();
        for(int i=0;i<dataRelation1.size();i++){
            //得到一条数据
            HashMap d = (HashMap) dataRelation1.get(i);
            if(d==null||d.equals("")){
                continue;
            }else{
                d.forEach((key, value) -> {
                    dataRelation.add(value);
                    System.out.println("key"+key);
                    System.out.println("value"+value);
                });
            }
        }
        return dataRelation;
    };

    @Override
    public List oneIndexBox(String province,String aIndex){

        List<Object> boxData = new LinkedList<>();
        List boxData1 = baseMapper.oneIndexBox(province,aIndex);
        for(int i=0;i<boxData1.size();i++){
            HashMap oneBoxData = (HashMap) boxData1.get(i);
            if(oneBoxData==null||oneBoxData.equals("")){
                continue;
            }else{
                oneBoxData.forEach((key, value) -> {
                    boxData.add(value);
                });
            }
        }
        return boxData;
    };
    /**
     * 查询 国家-一个属性的误差范围图
     * @return
     */
    @Override
    public List<Double> oneIndexErrorBox(String provinceCn, String aIndex){

        List<Double> errorData = new LinkedList<>();
        List boxData1 = baseMapper.oneIndexErrorBox(provinceCn,aIndex);
            for(int i=0;i<boxData1.size();i++){
            HashMap oneBoxData = (HashMap) boxData1.get(i);
            if(oneBoxData==null||oneBoxData.equals("")){
                continue;
            }else{
                oneBoxData.forEach((key, value) -> {
                    errorData.add((Double) value);
                });
            }
        }
        Collections.sort(errorData);
        return errorData ;
};


    /**
     * 查询 国家-一个属性的散点
     * @return
     */
    @Override
    public List<Double> oneIndexScatterPlot(String countryCn, String aIndex){

        List<Double> ScatterData = new LinkedList<>();
        List ScatterData1 = baseMapper.oneIndexScatterPlot(countryCn,aIndex);
        for(int i=0;i<ScatterData1.size();i++){
            HashMap oneBoxData = (HashMap) ScatterData1.get(i);
            if(oneBoxData==null||oneBoxData.equals("")){
                continue;
            }else{
                oneBoxData.forEach((key, value) -> {
                    ScatterData.add((Double) value);
                });
            }
        }
        Collections.sort(ScatterData);
        return ScatterData ;
    };

    /**
     * 查询 国家-一个属性的雷达
     * @return
     */
    @Override
    public List<Double> oneIndexRadarPlot(String countryCn, String aIndex){
        return baseMapper.oneIndexRadarPlot(countryCn,aIndex);
    }

    @Override
    public List<Map<String, Object>> queryAllData(int page, String countryCn, String species, int[] unusedIds) {
        int limitLeft, limitRight;
        if (page == -1) {
            limitLeft = limitRight = -1;
        }
        else {
            limitLeft = page * 200;
            limitRight = 200;
        }
        List<Map<String,Object>> dataList = baseMapper.queryAllData(countryCn, species, limitLeft, limitRight);
        LinkedList<Map<String, Object>> retList = new LinkedList<>();

        if (unusedIds == null) {
            retList.addAll(dataList);
        }
        else {
            int unusedPt = 0;
            for (int i = 0; i < dataList.size(); i++) {
                if (unusedPt < unusedIds.length && i + 1 == unusedIds[unusedPt]) {
                    unusedPt++;
                    continue;
                }
                retList.addLast(dataList.get(i));
            }
        }

        return retList;
    }

    @Override
    public List<List<Double>> getTwoTargetData(String target1, String target2) {
        QueryWrapper<DataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(target1, target2);
        queryWrapper.isNotNull(target1).isNotNull(target2);

        List<Map<String, Object>> originData = this.listMaps(queryWrapper);

        List<List<Double>> dataList = new LinkedList<>();
        for (Map<String, Object> oneData : originData) {
            double x = Double.parseDouble(String.valueOf(oneData.get(target1)));
            double y = Double.parseDouble(String.valueOf(oneData.get(target2)));
            List<Double> pair = new LinkedList<>();
            pair.add(x);
            pair.add(y);
            dataList.add(pair);
        }
        originData.clear();
        return dataList;
    }

    @Override
    public List<List<List<Double>>> getMoreTargetData(String[] targetList) {
        if (targetList == null || (targetList.length & 1) == 1 || targetList.length > 8) return null;
        List<List<List<Double>>> dataList = new LinkedList<>();
        for (int i = 0; i < targetList.length; i += 2) {
            List<List<Double>> twoTargetData = getTwoTargetData(targetList[i], targetList[i + 1]);
            dataList.add(twoTargetData);
        }
        return dataList;
    }
}