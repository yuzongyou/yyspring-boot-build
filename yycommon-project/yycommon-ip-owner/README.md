# IP归属查询
    支持 IP 归属省、市、县查询，查询结果如下：
    {
      "country" : "中国",                 // 国家名称，fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "province" : "广东",                // 归属省，中文，fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "city" : "广州",                    // 归属市，中文，fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "district" : "番禺区",               // 归属县，中文，fillDistrict = true 才有值
      "provider" : "联通",                 // 网络运营商, fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "longitude" : "23.125178",          // 经度, fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "latitude" : "113.280637",          // 纬度, fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "location" : "Asia/Shanghai",       // 国际、火星坐标, fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "timeZone" : "UTC+8",               // 时区、国际协调时间，fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "code" : "440100",                  // 省市行政区划代码, fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "countryCode" : "86",               // 国家代码，fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "internationalCode" : "CN",         // 国际代码，fillProvince,fillCity,fillDistrict 任意一个为true则有值
      "districtCode" : "440113",          // 区县行政区划代码，fillDistrict=true 则有值
      "districtRadius" : "9.4",           // 覆盖方位，IP使用区域半径，单位：千米，fillDistrict=true 则有值
      "districtLongitude" : "113.38397",  // 区县中心经度，fillDistrict=true 则有值
      "districtLatitude" : "22.93599"     // 区县中心纬度，fillDistrict=true 则有值
    }


    如果是可以引入 `applicationContext-ipowner.xml` 然后注入下面这个类使用：
    
    @Autowired
    private IpOwnerService ipOwnerService;
