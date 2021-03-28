package io.renren.modules.ag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.ag.dao.ScatterMapDao;
import io.renren.modules.ag.entity.DataEntity;
import io.renren.modules.ag.service.ScatterMapService;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("scatterMapService")
public class ScatterMapServiceImpl extends ServiceImpl<ScatterMapDao, DataEntity> implements ScatterMapService {


    //地图可视化 - 散点图
    /**
     * 查询某一指标的 经纬度和值
     */
    @Override
    public List<String> indexLongDimenZhi(String aIndex){return baseMapper.indexLongDimenZhi(aIndex);};
    //查询某一指标所有国家某一物种和对应的平均值-
    @Override
    public List<String> indexSpeciesLongDimenZhi(String aIndex,String special){return baseMapper.indexSpeciesLongDimenZhi(aIndex,special);};
    //查询某一指标所有国家某一年和对应的平均值
    @Override
    public List<String> indexYearLongDimenZhi(String aIndex,String yearJc){return baseMapper.indexYearLongDimenZhi(aIndex,yearJc);};
    //查询某一指标所有国家某一物种某一年和对应的平均值
    @Override
    public List<String> indexSpeciesYearLongDimenZhi(String aIndex,String special,String yearJc){return baseMapper.indexSpeciesYearLongDimenZhi(aIndex,special,yearJc);};



}