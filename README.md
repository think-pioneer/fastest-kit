<h1 align="center">Fastest</h1>

# 0、前言

Fastest一个基于注解思想的高效(HTTP)测试框架。使用注解快速完成用例功能编写。框架的目标并不是完全摈弃代码，而是减少代码。

当前大多数接口框架都是基于数据驱动的，但是单纯通过配置数据无法覆盖所有场景。<font color=red>复杂业务场景</font>还是需要进行代码编写才能有效覆盖。并且基于数据驱动需要保证环境不能出问题否则用例就会执行失败。

框架优势：

- 通过注解可以去掉30%-40%的冗余代码。
- 自定义扩展注解，满足业务测试的定制化需求。
- 通过测试三层思想，优化用例，提升用例的稳定性和健壮性
- 让测试人员能够把精力集中到用例场景的优化的组织上

# 1、框架介绍

## 1.0 用例分层思想

框架对API测试定义三层：

Api层，即每一个http请求，为用例的最小单元。

Step层，将一个或多个api组装成一个step。

TestCase层，将一个或多个step组装成一个testcase。

以删除用户为例

手工用例：

1. 删除用户

转换为自动化用例：

1. 找到用户 ----> getUser
2. 删除用户 ----> deleteUser

但是上面用例明显健壮性不足，极易可能失败，并且容易漏测，优化一下

1. 找到用户 ----> getUser
2. 判断第一步是否有找到至少一个用户，如果没有创建 ----> createUser
3. 重新获取用户(不管第二步创建接口有没有返回用户信息，我们都重新请求查询接口) ----> getUser
4. 删除用户 ----> deleteUser
5. 重新获取用户(不管第四步删除时有没有返回用户信息，我们都重新请求查询接口) ----> getUser

这里我们共使用了三个接口（api层），分别是getUser（api1），createUser（api2），deleteUser（api3）。对应的用例步骤（step层）实际只有两个，分别是1、2、3找到用例（step1），4、5删除用户（step2）。最终将组装成了我的一条用例case1（case层）

 <center>
     <img style="border-radius: 0.3125em;
    box-shadow: 0 2px 4px 0 rgba(34,36,38,.12),0 2px 10px 0 rgba(34,36,38,.08);" 
    src="static/case_layer.png" width = "100%" hight = "100%" alt=""/>
</center>

每一层都是独立的，都可以单独给其他层调用（原则上是上层调用下层）

优点：

1. 用例组织结构更清晰
2. 基于第一点，能够明显提升用例的健壮性和稳定性
3. 通过独立出api层和step层，每一条用例都能单独执行，完全不依赖其他用例。甚至可以达到一次编写处处执行。

缺点：

1. 优点即缺点，那就是运行时间会特别长，原因也是也为独立出来的api层导致接口会被请求多次，导致执行时间特别长

整体来说优点是大于缺点的。用例的执行时间和健壮性/稳定性比起来，肯定健壮性/稳定性更重要

测试三层只是理想，大家也不用完全按照三层来做，框架也未限制必须按照测试三层来组织测试用例、测试代码。

## 1.1 HTTP模块

HTTP模块基于OkHttp。基本思想为将每个请求对象映射为一个客户端实体，即每个客户端的cookie在实例化时一经确认便不可修改。切换用户只需要切换客户端。

### 1.1.1 Requester

Requester就像是一个单独的用户，理论上来说一个用户只能有一个用户信息。不过为了方便使用，还是开放支持在header中指定用户信息。本质是一个接口，详细参考[Requester](#23-requester)

- 当理解为用户时，建议通过构造函数注入鉴权信息。如果需要其他用户，则重新实例化一个新的对象
- 当理解为客户端时，可以通过header修改鉴权信息。不过不建议这么做，因为每次修改header数据时容易造成数据混乱，导致用例失败。

#### 1.1.1.1 创建实例

框架提供RequesterFactory作为Requester的构造器，如果不使用RequesterFactory创建Requester对象，则需要创建时自己将鉴权信息到AuthManager中。

作为用户时

```java
Requester requester = RequesterFactory.create(auth);
```

或者

```java
Requester requester = RequesterFactory.create(auth1, auth2, auth3,...);
```

或者

```java
Requester requester = RequesterFactory.create(auths);
```

以上三种方式都不需要在请求的header中再次指定鉴权信息

作为客户端时

```java
Requester requester = RequesterFactory.create();//此时header中不带任何鉴权信息
```

#### 1.1.1.2 构建参数

可进行url、http method、url parameter、form body、json body设置

```java
Requester requester = RequesterFactory.create(auth);
requester.metadata().setUrl();
requester.metadata().setHttpMethod();
requester.metadata().setParameters();
requester.metadata().setForms();
requester.metadata().setJson();
```

#### 1.1.1.3 发送请求

```java
Requester requester = RequesterFactory.create(auth);
//构建参数
requester.metadata().setUrl();
requester.metadata().setHttpMethod();
requester.metadata().setParameters();
requester.metadata().setForms();
requester.metadata().setJson();
//发送请求
requester.sync();
```

发送请求共有sync和async两种方式。sync会阻塞进程，而async则不会阻塞进程。一般情况下都使用sync。

### 1.1.2 Responder

HTTP响应信息。添加断言功能（该功能也添加到requester中）。通常情况我们不会直接new该对象。都是通过requester.getResponse()来获取。本质是一个接口，详细参考[Responder](#24-responder)

```java
Requester requester = RequesterFactory.create(auth);
requester.sync();
requester.getResponse();
```

### 1.1.3 Metadata

http元数据，包含url，httpmethod，url参数，form，json，header

用法：

```java
Requester requester = RequesterFactory.create();
Metadata metadata = new Metadata();
metadata.setUrl();
metadata.setHttpMethod();
metadata.setHeaders();
metadata.setParameters();
metadata.setForms();
metadata.setJson();
requester.metadata(metadata);//如果之前对requester的metadate做过设置，则使用该方法会覆盖之前的值
```

等价于

```java
Requester requester = RequesterFactory.create(auth);
//构建参数
requester.metadata().setUrl();
requester.metadata().setHttpMethod();
requester.metadata().setHeaders();
requester.metadata().setParameters();
requester.metadata().setForms();
requester.metadata().setJson();
```

设置Header

```java
@Component
public class MyStep implements Step {
    @Autowired
    HelloController controller;

    @Capture(isThrow = false)
    public void test1(){
        Requester requester = RequesterFactory.create();
        Headers headers = new Headers();
        headers.write(new Header("cookie", "cookie"));
        requester.metadata().setHeaders(headers);
        controller.testRestMetadata(requester);
    }

    public boolean restorer() {
        return false;
    }
}
```

### 1.1.4 Settings

requester设置。

| 属性                     | 类型       | 默认值          | 说明                                                         |
| ------------------------ | ---------- | --------------- | ------------------------------------------------------------ |
| isCleanMetadata          | Boolean    | false           | 是否在每次请求完成后清空metadata                             |
| isCleanBody              | Boolean    | true            | 是否在每次请求完后清空body，和清空metadata相比，没有清空url和method |
| showRequestLog           | Boolean    | true            | 是否打印请求header、body信息                                 |
| showResponseLog          | Boolean    | false           | 是否打印响应header、body信息，因为某些body异常的大，所以默认不打印 |
| sslType                  | SSLType    | SSLType.DEFAULT | https的加密类型                                              |
| followRedirects          | Boolean    | true            | 是否自动重定向（http）                                       |
| followSslRedirects       | Boolean    | true            | 是否自动重定向（https）                                      |
| connectTimeout           | Long       | 60              | 连接超时时间，单位秒                                         |
| writeTimeout             | Long       | 60              | 读取超时时间，单位秒                                         |
| readTimeout              | Long       | 60              | 写入超时时间，单位秒                                         |
| callTimeout              | Long       | 120             | 全局的超时时间，单位秒。从DNS解析开始，到连接，到发请求体，到服务端响应，到服务端返回数据；以及可能出现的重定向，整个过程都需要在这个时间内完成，否则就算超时了，客户端会主动放弃或者断开连接。 |
| retryOnConnectionFailure | Boolean    | true            | 是否重试                                                     |
| client                   | HttpClient | HttpClient      | 客户端的更详细设置                                           |



### 1.1.5 Asserts

断言工具。将断言和responder绑定在一起，可进行高效的断言操作。

```java
@Component
public class MyStep implements Step {
    @Autowired
    HelloController controller;

    @Capture(isThrow = false)
    public Responder test1(){
        Requester requester = RequesterFactory.create();
        Headers headers = new Headers();
        headers.write(new Header("cookie", "cookie"));
        requester.metadata().setHeaders(headers);
        return controller.testRestMetadata(requester);
    }

    public boolean recovery() {
        return false;
    }
}
```

```java
public class CaseTest {
    @Autowired
    MyStep myStep;

    @Test
    public void case1() throws ClassNotFoundException {
        myStep.test1().asserts.assertEqual(2, "$.data.id");
    }
}
```



## 1.2 Enhance模块

框架提供的功能增强模块，缩减代码量，提高编码效率。提供可扩展的接口，方便实现自定义的功能增强。

### 1.2.1 @Component

#### 用法说明

该注解用来判断被注解类是否进行功能增强。所有后续注解都依赖于该注解。只有添加了该注解的类才会托管于框架。

```java
@Component
public class HelloController {

    @RestMetadata(serverName = "user_system", apiName = "get_userinfo_by_id")
    public Responder testRestMetadata(Requester requester, Restfuls restfuls){
        requester.sync();
        return requester.getResponse();
    }
}
```

例：HelloController被增强后，可以使用[RestMetadata](#127-restmetadata)功能及类似功能。

### 1.2.2 @Autowired

#### 用法说明

该注解用来自动创建实例。可创建普通实例或者增强实例。支持无参或有参构造

##### 无参构造

```java
@Component
public class MyStep implements Step {
    @Autowired //无参构造
    HelloController controller;
}
```

##### 有参构造

构造参数的参数类型及参数值需要放在ConstructorProperty中，每一个ConstructorProperty就是一个参数，添加ConstructorProperty时需要按照构造参数的顺序添加，否则会创建失败。

不建议进行有参构造。

```java
// 待构造类
@Component
public class MyStep implements Step {
    public MyStep(Auth auth){}

    //必须是静态方法；返回值必须是List<ConstructorProperty>
    public static List<ConstructorProperty> create(){
        Auth auth = new Auth("admin", "aIJy2UJJui");//auth为MyStep的构造参数
        List<ConstructorProperty> params = new ArrayList<>();//构造参数容器
        params.add(new ConstructorProperty(Auth.class, auth));//多个构造参数，请以此添加
        return params;
    }
    public boolean restorer() {
        return false;
    }
}
```

调用方

```java
@Component
public class CaseTest {
    @Autowired(constructor = "create")
    MyStep myStep;
}
```

##### 创建普通实例

设置isOrigin为true即可创建普通实例

```java
@Component
public class CaseTest {
    @Autowired(isOrigin = true)
    MyStep myStep;
}
```

#### 参数说明

| 参数        | 类型   | 默认值 | 说明                                                         |
| ----------- | ------ | ------ | ------------------------------------------------------------ |
| constructor | String | ""     | 如果是有参构造，则需要指定获取构造参数的类的处理方法，参考[Autowired-有参构造](#有参构造) |
| isOrigin    | bool   | false  | 本次注入增强实例还是普通实例                                 |



### 1.2.3 @Value

#### 用法说明

本框架默认使用yaml作为配置文件

从yml/yaml文件中读取配置文件的值，并赋值给被注解变量。如果多个配置文件有相同的变量名，则会取最后加载的变量(加载规则由jvm控制)

测试properties

```java
user: admin
number: 10
ids: 1,2,3,4
```

```java
@Component
public class CaseTest {
    @Value("user")
    String user;
}
```

如果指定变量的类型，则会自动转换为该类型，如果转换失败则抛ValueException异常

```java
@Component
public class CaseTest {
    @Value("number")
    Integer number;
}
```

如果需要将properties的值转换为数组。多个value间用分号隔开

```java
@Component
public class CaseTest {
    @Value("ids")
    Integer[] ids;
}
```

如之前所说，如果多个配置文件有相同的变量名，@Value支持指定文件名。***文件名需要指定为resources下的完整路径***，如果只指定文件名，则不同目录下则可能会有相同的文件名。建议指定文件名

```java
@Component
public class CaseTest {
    @Value(value = "user", file = "user.yaml")
    String user;
}
```

#### 参数说明

| 参数  | 类型   | 默认值 | 说明                                                         |
| ----- | ------ | ------ | ------------------------------------------------------------ |
| value | string | ""     | 配置文件中的key（和key的区别，不用输入字段名）               |
| key   | string | ""     | 配置文件中的key（和value的区别，需要输入字段名）             |
| file  | string | ""     | 指定配置文件名，需要从resources下的完整路径，不能只提供一个文件名，因为文件名也会重复 |

### 1.2.4 @MultipleInstance

#### 用法说明

创建多实例。框架默认在生成实例时是单例（模拟的单例），这是为了能减少性能开销。如果需要多实例，则只需要添加该注解，则会每次生成不同的实例

```java
@Component
@MultipleInstance
public class HelloController {
}
```

### 1.2.5 @Capture

#### 用法说明

在代码运行中可能出现unchecked exception，但是又不想写try{}catch{}。可以使用该注解做用到方法上。捕获代码运行中的异常，并输出到日志（使用[FastestLogger](#14-fastestlogger)）。默认会重新抛出异常

```java
@Component
public class MyStep implements Step {
    @Capture
    public void test1(){
        System.out.println(1/0); //异常
    }

    public boolean restorer() {
        return false;
    }
}
```

```log
2021-11-07 14:59:13,333 - [ ERROR ] - [ MyStep:68 ] 
org.easy.fasttest.exceptions.CaptureException: / by zero
```

设置isThrow = false。此时只会把异常保存到日志，不会抛出异常。

```java
@Component
public class MyStep implements Step {
    @Autowired
    HelloController controller;
    
    @Capture(isThrow = false)
    public void test1(){
        System.out.println(1/0);
    }

    public boolean restorer() {
        return false;
    }
}
```

#### 参数说明

| 参数      | 类型   | 默认值          | 说明                       |
| --------- | ------ | --------------- | -------------------------- |
| isThrow   | bool   | true            | 捕获异常后是否重新抛出异常 |
| message   | string | ""              | 重新抛出的异常信息         |
| exception | Class  | Throwable.class | 捕获该类及其子类异常       |

### 1.2.6 @LoggerJoin

#### 用法说明

自动注入FastestLogger对象到变量中。log类型为xyz.think.fastest.logger.FastestLogger

```java
public class CaseTest {
    @LoggerJoin
    static FastestLogger logger;

    @Test
    public void case1() {
        logger.info("测试日志");
    }
}
```

[FastestLogger参考](#14-Fastestlogger)

#### 参数说明

| 参数  | 类型   | 默认值 | 说明                                             |
| ----- | ------ | ------ | ------------------------------------------------ |
| value | string | ""     | Logger名称，为空字符串时，则使用该字段所在的类名 |

### 1.2.7 @RestMetadata

#### 用法说明

该注解为该模块定义的[测试三层](#10-用例分层思想)中的api层

自动装载api配置文件中的host、uri，httpmethod信息

配置文件的位置默认在apiconfig下，会读取该目录下(包括子目录)所有的配置文件，所以该目录请只放api配置文件(yaml格式)。如需自定义apiconf目录的路径，则在配置文件（yaml）中加入节点api.config.folder.path=YOUR_PATH

api配置文件模板可通过"ApiConfigTemplate.printTemplate();"查看，该方法也会返回该模板

uri.yaml

```yaml
- serverName: user_system
  desc: 用户系统服务
  host: 'http://user.testdemo.com'
  uris:
    - uriName: get_userinfo_by_id
      uri: /api/userinfo
      method: get
      desc: 通过id获取用户信息
    - uriName: get_userinfo_by_id_restful
      uri: '/api/{id}/userinfo'
      method: get
      desc: 通过id获取用户信息(restful)
```

```java
@Component
@Singleton
public class HelloController {

    @RestMetadata(serverName = "user_system", apiName = "get_userinfo_by_id") //请求方法从
    public Responder testRestMetadata(Requester requester, Restfuls restfuls){
        requester.sync();
        return requester.getResponse();
    }
}
```

执行请求后的 输出结果。可以看到url和method都已经自动注入完成

```
2021-11-07 15:57:41,687 - [ INFO ] - [ Sender:144 ] **********HTTP REQUEST**********
Http Url:http://user.testdemo.com/uri/userinfo
Http Method:GET
Http Header:[]
Http QueryParameters:{}
Http Forms:{}
Http Json:
Metadata{url=http://user.testdemo.com/uri/userinfo, method=GET, headers=[], parameters={}, forms={}, json={}}
```

#### 参数说明

| 参数       | 类型   | 默认值 | 说明                                                 |
| ---------- | ------ | ------ | ---------------------------------------------------- |
| serverName | string | ""     | 接口坐在的服务名，每个服务名为一个host域名           |
| apiName    | string | 必填   | 接口名称                                             |
| desc       | string | ""     | 接口说明                                             |
| auto       | bool   | true   | 是否自动发起请求，默认是                             |
| sync       | bool   | true   | 是否使用同步请求，默认是                             |
| file       | string | ""     | 执行接口所在的文件路径，从apiconfig.folderpath下读取 |

注：支持直接从swagger中读取配置，但是可能会出现兼容性问题，所以不建议

将swagger中的docs(json格式)，保存为.swagger文件即可。

### 1.2.8 @RestServer

#### 使用说明

如果同一个server下的api在同一个类里管理，则可以直接在类上使用该注解，并指定servername

```java
@Component
@Singleton
@RestServer("manager_system")
public class HelloController {

    @RestMetadata(apiName = "get_userinfo_by_id", desc = "未指定servername，使用RestServer的值")
    public Responder testRestServer(Requester requester, Restfuls restfuls){
        requester.sync();
        return requester.getResponse();
    }

}
```

#### 参数说明

| 参数  | 类型   | 默认值 | 说明                                                         |
| ----- | ------ | ------ | ------------------------------------------------------------ |
| value | string | ""     | api配置文件中的servername。如果@RestMetadata没有设置serverName属性，则对整个类生效 |
| file  | string | ""     | 执行接口所在的文件路径，从apiconfig.folderpath下读取。如果@RestMetadata没有设置file属性，则对整个类生效 |



### 1.2.9 @RestTemp

#### 使用说明

直接在注解中配置host，uri，httpmethod等信息

```java
@Component
@Singleton
@RestServer("manager_system")
public class HelloController {

    @RestTemp(host = "http://test.com", uri = "/usr/{id}", method = HttpMethod.GET)
    public Responder testRestTemp(Requester requester, Restfuls restfuls){
        requester.sync();
        return requester.getResponse();
    }
}
```

#### 参数说明

| 参数   | 类型       | 默认值 | 说明                                                         |
| ------ | ---------- | ------ | ------------------------------------------------------------ |
| name   | string     | ""     | 接口名                                                       |
| host   | string     | 必填   | 域名                                                         |
| uri    | string     | 必填   | 接口                                                         |
| method | HttpMethod | 必填   | 接口方法                                                     |
| desc   | string     | ""     | 接口描述                                                     |
| save   | boolean    | false  | 是否保存，默认不保存。全局参数（fastest.rest.temp.save）和该参数任意一个true都将保存。fastest.rest.temp.save可通过在properties文件中配置或者java启动参数中配置 |
| auto   | boolean    | true   | 是否自动发起请求，默认是                                     |
| sync   | boolean    | true   | 是否使用同步请求，默认是                                     |

### 1.2.10 @PreMethod

#### 用法说明

在方法之前执行，可以进行临时的参数注入、log打印等等一系列前置操作

pre method执行方法

```java
public class StepLog {
    public static void paramsLog(){
        System.out.println("prelog");
    }
    
    public static void paramsLog(String log){
        System.out.println(log);
    }
}
```

调用方（无参）

```java
@Component
public class MyStep implements Step {
    @Autowired
    HelloController controller;
    @PreMethod(targetClass = StepLog.class, method = "paramsLog")
    public Responder test1(String name){
        Requester requester = Requester.create();
        requester.metadata().setParameters("username", "luo").setParameters("password", "123");
        return controller.testRestTemp(requester);
    }
}

```

调用方（有参）

```java
@Component
public class MyStep implements Step {
    @Autowired
    HelloController controller;
    @PreMethod(targetClass = StepLog.class, method = "paramsLog", argsIndex = 0)
    public Responder test1(String name){
        Requester requester = Requester.create();
        requester.metadata().setParameters("username", "luo").setParameters("password", "123");
        return controller.testRestTemp(requester);
    }
}
```

参数说明

| 参数        | 类型   | 默认值 | 说明                                                         |
| ----------- | ------ | ------ | ------------------------------------------------------------ |
| targetClass | Class  | 必填   | 具体执行pre method的类                                       |
| method      | String | 必填   | 具体执行pre method的方法，如果有参数需要是被注解方法入参的类型中一个或全部 |
| argsIndex   | int[]  | {}     | 从被注解方法的入参中选取参数，元素是被注解方法入参的位置。例如：argsIndex = {1,1}，即连续选取被注解方法<br>的第一个参数，作为premethod的入参premethod(arg1, arg1)。 |



### 1.2.11 @PostMethod

在方法之后执行，可以打印执行结果等一系列后置操作。参考[@PreMethod](#1210-premethod)

### 1.2.12 @ValueEntity

#### 使用说明

用于将配置文件的内容直接赋值给实体对象

user.yaml

```yaml
user:
    name: Faker
    age: 30
    sex: man
```

UserEntity.java

```java
public class UserEntity {
    private String name;
    private Integer age;
    private String sex;
}
```

测试类

```java
@Component
public class CaseTest {
    @ValueEntity(key = "user", file = "user.yaml")
    UserEntity user;

    @Test
    public void case1() throws ClassNotFoundException {
        System.out.println(user.getName());//Faker
    }
}
```

#### 参数说明

| 参数 | 类型   | 默认值 | 说明                                                         |
| ---- | ------ | ------ | ------------------------------------------------------------ |
| key  | String | 必填   | 配置文件中的前缀，会将后面的值作为实体对象的key。例如：properties中配置user.name。<br>ValueEntity的key为user，则会在被注解的对象中寻找name字段，并赋值给name字段 |
| file | String | 必填   | yaml的文件名                                                 |



### 1.2.13 @MutexAnnotation

#### 使用说明

定义注解的互斥注解，即表示被注解的注解和哪些注解互斥

```java
@MutexAnnotation({B.class, C.class})
public @interface A{} //注解A和注解B、C互斥，不能同时存在

public @interface B{}

public @interface C{}
```

#### 参数说明

| 参数  | 类型  | 默认值 | 说明         |
| ----- | ----- | ------ | ------------ |
| value | Class | 必填   | 标明互斥注解 |

### 1.2.14 @Recovery

#### 使用说明

注解增加了用于控制每次测试结束后执行Step.recovery方法，进行数据或场景恢复

#### 参数说明

| 参数     | 类型    | 默认值       | 说明                                                         |
| -------- | ------- | ------------ | ------------------------------------------------------------ |
| recovery | boolean | true         | 是否对step进行恢复操作                                       |
| executor | Class   | RecoveryStep | 执行恢复操作的类                                             |
| stepType | Class[] | Step.class   | 需要进行恢复操作的step类，如果为Step.class则对测试类下的所有step进行恢复操作。通常用例下面会有很多step，可能在执行用例后不需要将所有step都执行恢复操作，可通过该参数指定需要恢复的step |

### 1.2.15 @Pointcut

自定义注解时，通过在注解实现类上使用该注解实现注解和实现类的绑定。

#### 使用说明

LogPrint.java

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogPrint {
}
```

LogPringImpl.java

```java
@Component
@Pointcut(annotation = LogPrint.class)
public class LogPrintImpl<T> implements MethodProcessable<T> {
    @Override
    public void process(JoinPoint joinPoint) {
        System.out.println("print log");
    }
}
```

Controller.java

```java
@Component
public interface Controller {

    @LogPrint
    default void hello(){
    }
}
```

#### 参数说明

| 参数       | 类型    | 默认值 | 说明                                                 |
| ---------- | ------- | ------ | ---------------------------------------------------- |
| annotation | Class   | 必填   | 用来将实现类和注解绑定到一起。                       |
| index      | int     | 0      | 如果一个注解有多个实现类，则通过该参数指定执行顺序。 |
| before     | boolean | false  | 表示绑定的注解将在方法执行前执行。                   |
| after      | boolean | false  | 表示绑定的注解将在方法执行后执行。                   |

## 1.3 自定义注解

实现用户自己的注解功能，目前只提供两种类型的自定义注解，字段注解和方法注解

### 1.3.1 字段注解

字段注解的功能实现类必须继承FieldProcessable类

JoinPoint是被拦截字段的一些信息，包含注解对象、被拦截字段、被拦截字段所在的对象、以及当前实现类对象

定义注解

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StringToBoolean {
}
```

注解处理

```java
@Pointcut(annotation = StringToBoolean.class)
public class MyCustomFieldAnnotation implements FieldProcessable {
    @Override
    public void process(JoinPoint joinPoint) {
        //do something
    }
}
```

### 1.3.2 方法注解

方法注解的功能实现类必须继承MethodProcessable类

JoinPoint是被拦截方法的信息，包含注解对象、被拦截方法、被拦截方法的参数、被代理的对象、实现类的对象、返回给被注解方法的返回值

定义注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before
public @interface LogPrint {
}
```

注解处理

```java

@Component
@Pointcut(annotation = LogPrint.class)
public class MyCustomMethodAnnotation implements MethodProcessable {

    @Override
    public void process(JoinPoint joinPoint) {
        System.out.println("MyCustomMethodAnnotation");
        System.out.println(joinPoint.getMethod().getName());
        System.out.println(Arrays.toString(joinPoint.getArgs()));
        joinPoint.setReturn("MyCustomMethodAnnotation");
    }
}
```

```java
	@LogPrint//MyCustomMethodAnnotation实现
    public Responder test1(MethodReturn methodReturn, String name){
        System.out.println("MethodReturn返回值:");
        System.out.println(methodReturn.getReturnValue());
        //输出
        //MethodReturn返回值:
	    //[MyCustomMethodAnnotation]，(多个)注解的(如果有)返回值为list
        return controller.testRestTemp(requester);
    }
```

## 1.4 Fastestlogger

框架内部使用的log模块，也是框架默认提供的log模块。***主要为解决部分新人不会使用和配置log4j、logback等框架，FastestLogger开箱即用无需配置***，可满足基本的log需求。如果有其他无法满足业务的log需求，可使用log4j、logback等框架。

### 用法

```java
public class CaseTest {
    FastestLogger logger = FastestLoggerFactory.getLogger(CaseTest.class);
    @Autowired
    MyStep myStep;

    @Test
    public void logTestCase(){
		logger.trace("测试日志trace");
        logger.debug("测试日志debug");
        logger.info("测试日志info");
        logger.warn("测试日志warn");
        logger.error("测试日志error")
    }
}
```

# 2. 扩展

## 2.1 Initialize

当需要在测试前进行初始化操作时，可以继承Initialize实现executor方法。

```java
@Component
public class MyInit implements Initialize{
    @Override
    public int order() {
        return 0;
    }
    
    @Override
    public void executor(){
        //do something
    }
}
```

方法说明

| 方法名   | 返回值 | 说明                                                     |
| -------- | ------ | -------------------------------------------------------- |
| order    | int    | 多个初始化操作的执行顺序，遇到相同的order则由jvm控制顺序 |
| executor | void   | 会在框架初始化完成后执行                                 |

## 2.2 Shutdown

当框架完成后退出系统前需要做一些收尾工作时，可以继承Shutdown实现executor方法。

```java
@Component
public class MyShutdown implements Shutdown{
    @Override
    public int order() {
        return 0;
    }
    
    @Override
    public void postHook(){
        //do something
    }
}
```

### 方法说明

| 方法名   | 返回值 | 说明                                                   |
| -------- | ------ | ------------------------------------------------------ |
| order    | int    | 多个关闭操作的执行顺序，遇到相同的order则由jvm控制顺序 |
| executor | void   | 框架结束时会执行                                       |

## 2.3 Requester

需要先说明一下，框架采用的http包为okhttp3，对于[requester](#23-requester)和[responder](#24-responder)的实现需要基于okhttp3。原因是设计之初并未考虑这部分的扩展，其实okhttp3已经够好了，不需要使用其他http框架，但是大家可以基于okhttp3自定义Requester(客户端)。

当框架提供的Requester实现无法满足需求时，可自己实现Requester，自己实现时需要在配置文件中指定实现类的完整类名

```java
@Component
public class MyRequester implements Requester{
    //do something
}
```

application.properties

```properties
fastest.http.requester=xxx.xxx.MyRequester
```

### 方法说明

| 方法名                      | 返回值    | 说明                                                         |
| --------------------------- | --------- | ------------------------------------------------------------ |
| metadata                    | Metadata  | 可以给requester添加metadata，此时会覆盖原来的metadata，也可获取requester的metadata，可以单独修改metadata的元素 |
| metadata(Metadata metadata) | Metadata  | 传入一个metadata，替换掉原来的metadata。                     |
| settings                    | Settings  | 框架提供的request设置项，包含简单的http所需的设置。          |
| settings(Settings settings) | Requester | 添加设置项                                                   |
| getResponder                | Responder | 获取框架提供的http响应对象，参考[Responder](#24-responder)   |
| send(Requester requester)   | void      | 发送请求，通过settings中的isSync控制时异步还是同步。该方法为静态方法，通过Requester.send(requester)调用。 |
| asserts                     | Asserts   | 直接通过requester对http响应进行断言                          |

## 2.4 Responder

自定义实现Http响应解析。需要在配置文件指定实现类的完整类名

```java
@Component
public class MyResponder implements Responder{
    //do something
}
```

application.properties

```properties
fastest.http.responder=xxx.xxx.MyResponder
```

### 方法说明

| 方法名                                                       | 返回值       | 说明                                         |
| ------------------------------------------------------------ | ------------ | -------------------------------------------- |
| stateCode                                                    | int          | http响应码，非业务响应码                     |
| body                                                         | ResponseBody | http响应body，okhttp的ResponseBody对象       |
| bodyToBytes                                                  | byte[]       | http响应body（二进制）                       |
| bodyToString                                                 | String       | HTTP响应body（字符串），字符类型为utf-8      |
| bodyToString(Charset charset)                                | String       | HTTP响应body（字符串），可以的指定字符类型   |
| bodyToJson                                                   | Json         | http响应body（框架提供的json对象）           |
| originalResponse                                             | Response     | 获取okhttp的响应对象                         |
| headers                                                      | Headers      | 获取框架提供的header对象                     |
| header(String key)                                           | String       | 获取header某个键的值                         |
| download(String file)                                        | void         | 下载文件,参数file为文件路径                  |
| asserts                                                      | Asserts      | 断言对象。方便拿到结果后直接断言。           |
| bodyToObject(JavaType type)                                  | T            | 将响应转换为Java 对象，默认使用jackson来转换 |
| bodyToObject(Class\<T> type)                                 | T            | 将响应转换为Java 对象，默认使用jackson来转换 |
| bodyToObject(TypeReference\<T> typeReference)                | T            | 将响应转换为Java 对象，默认使用jackson来转换 |
| bodyToObject(Class\<?> collectionClass, Class<?> ...elementClasses) | T            | 将响应转换为Java 对象，默认使用jackson来转换 |



## 2.5 Step

Step为该框架定义的[测试三层](#10-用例分层思想)中的step层，所有step需要继承并实现Step类。

继承Step类的目的是为了方便框架执行recovery操作，该继承并不是必须的操作。

```java
import org.testng.step.Step;

@Component
public class MyStep implements Step {
    
    public void getAllUsers(){
        //查找用户
        //首先从列表找，没有在创建
    }
    
    @Override
    public boolean recovery() {
        //删除用户列表的用户
        //全部删除返回true，否则返回false
        return false;
    }
}
```

### 方法说明

| 方法名   | 返回值  | 说明                                                         |
| -------- | ------- | ------------------------------------------------------------ |
| recovery | boolean | 执行用例后用来做一些“恢复操作”，比如：测试完添加订单功能后，删除订单的操作 |

## 2.6 RecoveryExecutor

### 方法说明

执行Step.recovery()方法的执行器

```
@Component
public class RecoveryExecutor1 implements RecoveryExecutor {

    @Override
    public boolean execute(long timeOut, boolean forceStop, Step... steps) {
		// for(Step step:steps){
		//  dosomething
		//}
        return true;
    }
}
```

| 方法名  | 参数                                                         | 返回值  | 说明                                                         |
| ------- | ------------------------------------------------------------ | ------- | ------------------------------------------------------------ |
| execute | long timeout:  执行step的超时时间<br>boolean forceStop:  如果step执行异常无法中断worker线程导致无法退出，将会尝试使用Thread.stop()停止线程。<br>Step[] steps:  测试步骤 | boolean | 测试类或者方法指定了@Recovery注解后，将会通过该方法执行所有的步骤中的recovery()方法。需要注意，recovery操作会在一个子线程中进行。 |



## 2.7 Filter

### 方法说明

在发送请求时执行的过滤器，用户可添加多个过滤器，通过[FilterConfig](#27-FilterConfig)的order属性控制执行顺序

Filter1.java

```java
public class Filter1 implements Filter {
    @Override
    public void doFilter(Requester requester, Responder responder, FilterChain filterChain) {
        System.out.println("before1");
        filterChain.doFilter(requester, responder);
        System.out.println("after1");
    }
}
```

Filter2.java

```java
public class Filter2 implements Filter {
    @Override
    public void doFilter(Requester requester, Responder responder, FilterChain filterChain) {
        System.out.println("before2");
        requester.metadata().setParameter("id", 3);
        filterChain.doFilter(requester, responder);
        System.out.println("after2");
        System.out.println(responder.bodyToString());
    }
}
```

TestFilter.java

```java
public static void main(String[] args) {
        Requester requester = RequesterFactory.create();
        requester.settings(Settings.create()
                        .setFilter(new FilterConfig(1, new Filter1()))
                        .setFilter(new FilterConfig(2, new Filter2())))
                .metadata(Metadata.create()
                        .setUrl("http://localhost:8080/hello")
                        .setHttpMethod(HttpMethod.GET)
                        .setParameter("id", 1)
                        .setParameter("id", 2)
                );
        System.out.println(requester.send().getResponder().bodyToString());
    }
```

执行结果

```
before1
before2
after2
{"id":[1,2,3],"OK":"200"}
after1
{"id":[1,2,3],"OK":"200"}
```

## 2.8 FilterConfig

### 方法说明

每个FilterConfig实例管理一个过滤器和该过滤器的序号，序号用来控制执行顺序

```java
Settings.create().setFilter(new FilterConfig(1, new Filter1()));
```



# 3. 项目使用方法

## 配置方法

pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>xyz.think</groupId>
            <artifactId>fastest-kit</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.5</version>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.4</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>
                                test-jar
                            </goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.3.0</version>
                <executions>
                    <execution>
                        <id>jar-with-dependencies</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <appendAssemblyId>false</appendAssemblyId>
                            <descriptorRefs>
                                <descriptorRef>jar-with-dependencies</descriptorRef>
                            </descriptorRefs>
                            <appendAssemblyId>false</appendAssemblyId>
                        </configuration>
                    </execution>
                    <execution>
                        <id>assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <descriptors>
                                <descriptor>src/main/assembly/assembly.xml</descriptor>
                            </descriptors>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

在src/main/assembly下添加assembly.xml（路径不存在则自行创建）

assembly.xml

```xml
<assembly
        xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0 http://maven.apache.org/xsd/assembly-1.1.0.xsd">
    <id>assembly</id>
    <formats>
        <format>zip</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>
    <fileSets>
        <fileSet>
            <directory>src/main/resources</directory>
            <outputDirectory>/</outputDirectory>
        </fileSet>
        <fileSet>
            <directory>target</directory>
            <includes>
                <include>*.jar</include>
            </includes>
            <outputDirectory>/</outputDirectory>
        </fileSet>
    </fileSets>
</assembly>
```

## 使用

打包命令

```shell
mvn clean package -DskipTests -Dmaven.skip.test.exec
```

打包后在target目录下会得到一个和项目名一样的压缩zip包，解压并进入到目录中，即可进行测试

执行测试

```shell
java -cp fastest-kit-1.0-SNAPSHOT.jar:fastest-test-1.0-SNAPSHOT-tests.jar org.testng.TestNG [testng xml path]
```

## 本地IDEA开发

Edit Configurations -> Edit Templates -> 选择TestNG -> Listeners -> 添加listener -> 搜索"org.testng.listener.TestRunListener"，选择并添加。

上面的步骤操作完成之后便可以直接运行testng用例

## 系统参数

框架提供了一些功能的控制参数，方便进行功能扩展。


|              参数              | 是否必须 |               默认值                |                             说明                             |
| :----------------------------: | :------- | :---------------------------------: | :----------------------------------------------------------: |
| fastest.api.config.folder.path | 否       |              apiconfig              | api配置文件的目录，会扫描该目录下所有的文件及子文件夹，文件类型为json、yaml、yml |
|   fastest.rest.temp.api.path   | 否       | apiconfig/apiconfig_custom/xxx.yaml |       保存RestTemp注解参数的目录及文件，文件类型为yaml       |
|     fastest.rest.temp.save     | 否       |                false                | 全局控制是否保存RestTemp注解的参数，当不想在每一个RestTemp注解上指定isSave参数时，可以使用该参数全局配置 |
|   fastest.http.requester   | 否       |          DefaultRequester           |       自定义Requester的实现类，不建议使用，后面会废弃        |
|   fastest.http.responder   | 否       |          DefaultResponder           |       自定义Responder的实现类，不建议使用，后面会废弃        |
|   fastest.rest.print.request   | 否       |                true                 | 全局参数，控制打印http请求的log，单次请求可通过@HttpLog的showRequestLog覆盖该参数的值 |
|  fastest.rest.print.response   | 否       |                false                | 全局参数，控制打印http响应的log，单次请求可通过@HttpLog的showResponseLog覆盖该参数的值。鉴于某些响应body比较大，所以默认不打印 |
|  fastest.cache.expired.period  | 否       |                10000                |        缓存管理器定时清理过期数据的时间间隔，单位毫秒        |
|    fasttest.poetry.disable     | 否       |                false                |       初始化阶段禁止打印诗词。默认值false，会打印诗词        |

