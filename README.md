# 在线版本地表反射率尺度转换工具代码说明

## 1.项目结构

项目整体结构以及各个**文件夹**中内容功能如下所示

├─在线版本ScaleConversion前端代码
│  ├─.idea // 编译器文件
│  ├─.vscode // 编译器文件
│  ├─dist // 前端打包文件
│  │  └─assets
│  ├─public // 前端公共文件
│  └─src // 前端源代码
│      └─images
├─在线版本ScaleConversion后端代码
│  ├─.idea // 编译器文件
│  │  └─inspectionProfiles
│  ├─.mvn // 编译器文件
│  │  └─wrapper
│  └─src 
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─qxc
│      │  │          └─ScaleConversiontools 
│      │  │              ├─Config // ScaleConversion后端配置
│      │  │              ├─Controller // ScaleConversion控制层代码
│      │  │              ├─Dao // ScaleConversion数据层代码
│      │  │              ├─Service // ScaleConversion服务层接口
│      │  │              │  └─impl // ScaleConversion服务层接口实现类
│      │  │              └─Utiles // ScaleConversion公共工具
│      │  └─resources
│      │      ├─static // ScaleConversion静态文件
│      │      └─templates // ScaleConversion模板
│      └─test
│          └─java
│              └─com
│                  └─qxc
│                      └─ScaleConversiontools // ScaleConversion测试类
└─限流算法后端代码
    ├─.idea // 编译器文件
    │  ├─dataSources
    │  │  └─5f790cd3-9ec4-4e93-ba13-92c2a52c6474
    │  │      └─storage_v2
    │  │          └─_src_
    │  │              └─schema
    │  └─inspectionProfiles
    ├─src
    │  ├─main
    │  │  ├─java
    │  │  │  └─com
    │  │  │      └─qxc
    │  │  │          ├─configuration
    │  │  │          ├─databasecentral // 限流数据中心
    │  │  │          │  ├─configuration
    │  │  │          │  ├─controller
    │  │  │          │  ├─dao
    │  │  │          │  ├─pojo
    │  │  │          │  └─server
    │  │  │          │      └─impl
    │  │  │          ├─initializerbean // 初始化bean对象接口实现
    │  │  │          ├─pluginunit // 限流插件
    │  │  │          │  └─construct
    │  │  │          ├─springbootstarter
    │  │  │          ├─threadexector // 限流算法自定义线程工厂
    │  │  │          │  ├─checkthreadexector // 限流算法自定义线程工厂默认线程工厂
    │  │  │          │  ├─configuration // 限流算法自定义线程工厂配置文件
    │  │  │          │  ├─construct // 限流算法自定义线程工厂构造
    │  │  │          │  ├─dirctoryautolisten // 限流算法自定义线程工厂监听实现
    │  │  │          │  ├─documentthreadexector // 限流算法自定义线程工厂限流
    │  │  │          │  ├─eventbus // 限流算法自定义线程工厂消息总线
    │  │  │          │  ├─registerthreadexector // 限流算法自定义线程工厂注册中心
    │  │  │          │  ├─registrationcentercore // 限流算法自定义线程工厂侏罗纪
    │  │  │          │  ├─serverthreadexector // 限流算法自定义线程工厂执行器
    │  │  │          │  └─threadfactorycore // 限流算法自定义线程工厂接口
    │  │  │          └─utiles // 限流算法自定义工具
    │  │  │              ├─containertools
    │  │  │              ├─fileencoderdecoder
    │  │  │              ├─filesystemtools
    │  │  │              ├─pluginunittools
    │  │  │              │  └─impl
    │  │  │              └─reflectancetools
    │  │  └─resources
    │  └─test
    │      └─java
    └─target

## 2.环境依赖

java11

nginx1.21.6及以上

python3.6及以上

## 3.demo访问地址

http://81.70.190.126/

![image-20240424001206031](D:%5Cdesktop%5C%E6%AF%95%E4%B8%9A%E8%AE%BA%E6%96%87%5C%E8%A1%A5%E5%85%85%E6%8F%90%E4%BA%A4%5C%E9%99%84%E4%BB%B6%5C%E5%9C%A8%E7%BA%BF%E7%89%88%E6%9C%AC%E5%9C%B0%E8%A1%A8%E5%8F%8D%E5%B0%84%E7%8E%87%E5%B0%BA%E5%BA%A6%E8%BD%AC%E6%8D%A2%E5%B7%A5%E5%85%B7%E6%BA%90%E4%BB%A3%E7%A0%81%5Cimage-20240424001206031.png)

## 4.开源

**项目代码开源在github代码仓库中地址如下：**

[qxcnwu/ScaleConversionToolsWeb (github.com)](https://github.com/qxcnwu/ScaleConversionToolsWeb)