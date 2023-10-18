<div align="center">

#  simpleIAST  ![1.1.beta (shields.io)](https://img.shields.io/badge/1.1.beta-brightgreen.svg)

</div>


<p align="center">
simpleIAST是一种交互式应用程序安全测试工具。
</p>

# 目录
- [快速开始](#快速开始)
- [兼容](#兼容)
- [开始运行](#开始运行)
- [二次开发](#二次开发)




## 快速开始

- **下载并自行打包**

```shell
# clone安装包
wget https://github.com/keven1z/simpleIAST/archive/refs/heads/master.zip

```

```shell
mvn clean package
```
- **运行**
>将iast-agent.jar和iast-engine.jar 放在同一目录

1. 跟随应用启动运行
```shell
java -javaagent:iast-agent.jar -jar [app.jar] # 
```

2. 应用启动后attach方式运行
```shell
java -jar iast-engine.jar -p [PID] # attach方式运行

```
## 兼容
### 支持中间件

* Tomcat
* Springboot

### 支持JDK
* jdk 1.8
* jdk 11

## 支持漏洞
* SQL注入
* 反序列化漏洞
* SSRF
* URL跳转漏洞
* XXE
* 命令注入
* 文件上传
* XSS
* Spring EL表达式注入

## 开始运行
### 启动页面
启动成功默认显示以下banner
```text
 __ _                 _         _____  _    __  _____ 
/ _(_)_ __ ___  _ __ | | ___    \_   \/_\  / _\/__   \
\ \| | '_ ` _ \| '_ \| |/ _ \    / /\//_\\ \ \   / /\/
_\ \ | | | | | | |_) | |  __/ /\/ /_/  _  \_\ \ / /   
\__/_|_| |_| |_| .__/|_|\___| \____/\_/ \_/\__/ \/    
               |_|                                    
```
### 启动模式
>配置路径：src/main/java/com/keven1z/Agent.java 修改START_MODE
> 默认以下两种模式:
>   START_MODE_OFFLINE:离线模式. 
>   START_MODE_SERVER:服务器模式.
#### 离线模式（默认）
漏洞结果默认打印到控制台

#### 服务器模式
Config.java中增加服务器地址，默认漏洞上报api如下：
```java
    /**
     * 服务注册
     */
    public static final String AGENT_REGISTER_URL = Config.IAST_SERVER + "/agent/register";
    /**
     * 服务器解绑
     */
    public static final String AGENT_DEREGISTER_URL = Config.IAST_SERVER + "/agent/deregister";

    /**
     * 发送报告的url
     */
    public static final String SEND_REPORT_URL = Config.IAST_SERVER + "/report/receive";
    /**
     * 获取服务端指令url
     */
    public static final String INSTRUCTION_GET_URL = Config.IAST_SERVER + "/instruction/get";
```

## 二次开发
参考[二次开发](./二次开发.md)