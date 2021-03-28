package io.renren.modules.ag.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 农业数据表
 * 
 * @author Guoping
 * @email 774647840@qq.com
 * @date 2021-02-03 19:48:29
 */
@Data
@TableName("ag_data")
public class DataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 数据id
	 */
	@TableId
	private Long id;
	/**
	 * 数据来源
	 */
	private String datafrom;
	/**
	 * 国家名称
	 */
	private String countrycn;
	/**
	 * 国家名称-英文
	 */
	private String countryen;
	/**
	 * 省份名称
	 */
	private String provincecn;
	/**
	 * 省份名称 英文
	 */
	private String provinceen;
	/**
	 * 年份
	 */
	private Integer yearjc;
	/**
	 * 物种
	 */
	private String species;
	/**
	 * 经度（°E）
	 */
	private Double longitude;
	/**
	 * 纬度（°N）
	 */
	private Double dimension;
	/**
	 * δ13C
	 */
	private Double δ13C;
	/**
	 * δ15N
	 */
	private Double δ15N;
	/**
	 * δ2H
	 */
	private Double δ2H;
	/**
	 * δ18O
	 */
	private Double δ18O;
	/**
	 * δ32S
	 */
	private Double δ32S;

}
