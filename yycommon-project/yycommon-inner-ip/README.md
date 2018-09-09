# 办公网IP支持
    提供办公网的查询，基于公司运维统一维护的文件： http://webapi.sysop.duowan.com:62175/office/ip_desc.txt
    
## 运维提供的文件格式说明
    环球网校_北京威地大厦 61.148.205.165
    上海办公室 2018/4/20 116.236.170.6
    珠海唐家大楼2018/1/29 59.38.34.130
    佛山VPN 14.215.104.211
    广州羊城创意产业园3-08多玩业务部移动IP 2017/03/10 183.237.185.79
    广州番禺万达广场B1 联通1.5G IP 2017/4/24 58.248.229.128
    
    
    没有非常一致的格式，主要是由三列组成
    最后一列：   具体的IP
    倒数第二列:  如果是日期格式 yyyy/M/d 那么解析成添加时间
    剩下的就是IP地址的说明
    
## 办公网IP使用方法
    在你的 spring 配置文件中，定义：
    <bean id="innerIpService" class="com.duowan.common.innerip.impl.InnerIpServiceImpl" init-method="init"/>
    
    然后直接使用下面这个代码可引入：
        @Autowired
        private InnerIpService innerIpService;
     