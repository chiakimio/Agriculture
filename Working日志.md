# DataAnalysisController

* 新增三个接口

### 接口1

```java
queryAllData(String page, String countryCn, String species, int[] unusedIds)
```

$url=/agriculture/ag/dataAnalysis/queryAllData?page=0\&countryCn=中国\&species=大米\&unusedIds=1,2,3$	

根据参数查询数据

参数说明：

1. page：

   分页查询，参数可为空，为空默认查询全部数据（查询较慢，不推荐使用）

   首页是第0页。每页200项。

2. countryCn:

   中文的国家名，为空则默认全部国家

3. species：

   物种名，为空默认所有物种

4. unusedIds:

   不需要的id的数组。

   id从1开始，这里的是当前页不需要的数据项，不是主键id。比如：

   原本返回数据data=[{1,1}, {2,2}, {4,4}, {8,8}]，传入unusedIds=[1, 3],则返回[{2,2}, {8, 8}]

返回示例：

![image-20210326105438467](E:\Working\Argriculture\Working日志.assets\image-20210326105438467.png)



### 接口2

```java
getTwoTargetData(String target1, String target2)
```

$ url= /agriculture/ag/dataAnalysis/getTwoTargetData?target1=δ32S{\&}target2=δ15N$

返回示例:

![image-20210326105529308](E:\Working\Argriculture\Working日志.assets\image-20210326105529308.png)

### 接口3

```java
getMoreTargetData(String[] targets)
```

$ url= /agriculture/ag/dataAnalysis/getTwoTargetData?targets=δ32S, δ15N, δ22N, δ15P$

这里传进来的必须是偶数个指标，且数量在2~8之间

返回示例：

![image-20210326110848133](E:\Working\Argriculture\Working日志.assets\image-20210326110848133.png)