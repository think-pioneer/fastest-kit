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

1. 找到用例 ----> getUser
2. 判断第一步是否有找到至少一个用户，如果没有创建 ----> createUser
3. 重新获取用户(不管第二步创建接口有没有返回用户信息，我们都重新请求查询接口) ----> getUser
4. 删除用户 ----> deleteUser
5. 重新获取用户(不管第四步删除时有没有返回用户信息，我们都重新请求查询接口) ----> getUser

这里我们共使用了三个接口（api层），分别是getUser（api1），createUser（api2），deleteUser（api3）。对应的用例步骤（step层）实际只有两个，分别是1、2、3找到用例（step1），4、5删除用户（step2）。最终将组装成了我的一条用例case1（case层）

![testcase layer][testcase_layer]


每一层都是独立的，都可以单独给其他层调用（原则上是上层调用下层）

优点：

1. 用例组织结构更清晰
2. 基于第一点，能够明显提升用例的健壮性和稳定性
3. 通过独立出api层和step层，每一条用例都能单独执行，完全不依赖其他用例。甚至可以达到一次编写处处执行。

缺点：

1. 优点即缺点，那就是运行时间会特别长，原因也是也为独立出来的api层导致接口会被请求多次，导致执行时间特别长

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

重新包装后的响应信息对象。添加断言功能（该功能也添加到requester中）。通常情况我们不会直接new该对象。都是通过requester.getResponse()来获取。本质是一个接口，详细参考[Responder](#24-responder)

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

从okhttp3中抽取一些常用的配置做成settings，如果觉得配置不好够的可以自行创建HttpClient对象进行完整设置。

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
| client                   | HttpClient | HttpClient      | 如果需求复杂的设置，则需要自己设置好后传入client             |



### 1.1.5 Asserts

断言工具

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

该注解用来判断被注解类是否进行功能增强。

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

HelloController被增强后，可以使用[RestMetadata](#127-restmetadata)功能及类似功能。

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

待构造类

构造参数的参数类型及参数值需要放在ConstructorProperty中，每一个ConstructorProperty就是一个参数，添加ConstructorProperty时需要按照构造参数的顺序添加，否则会创建失败

```java
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
    @Autowired(targetClass = MyStep.class, method = "create")
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

| 参数        | 类型   | 默认值          | 说明                                                         |
| ----------- | ------ | --------------- | ------------------------------------------------------------ |
| targetClass | Class  | Autowired.class | 如果是有参构造，则需要指定获取构造参数的类，参考[Autowired-有参构造](#有参构造) |
| method      | String | ""              | 如果是有参构造，则需要指定获取构造参数的类的处理方法，参考[Autowired-有参构造](#有参构造) |
| isOrigin    | bool   | false           | 本次注入增强实例还是普通实例                                 |



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

自动注入FastestLogger对象到变量中。log类型为xyz.thinktest.fastest.logger.FastestLogger

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

配置文件的位置默认在apiconfig下，会读取该目录下(包括子目录)所有的配置文件，所以该目录请只放api配置文件(yaml格式)。如需自定义apiconf目录的路径，则在配置文件（yaml）中加入节点api.config.folder.path=YOURPATH

api配置文件模板可通过"ReadApiConfig.printTemplate();"查看，该方法也会返回该模板

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
| save   | boolean    | false  | 是否保存，默认不保存。如果设置全局参数rest.temp.save则忽略该参数的值。rest.temp.save可通过在properties文件中配置或者java启动参数中配置 |
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

```properties
name: luohongyao
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
@Pointcut(annotation = LogPrint.class)
public class LogPrintImpl<T> implements MethodAnnotationProcessable<T> {
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

| 参数       | 类型    | 默认值 | 说明                                                         |
| ---------- | ------- | ------ | ------------------------------------------------------------ |
| annotation | Class   | 必填   | 用来将实现类和注解绑定到一起。                               |
| index      | int     | 0      | 如果一个注解有多个实现类，则通过该参数指定执行顺序。         |
| before     | boolean | true   | 表示绑定的注解将在方法执行前执行，默认所有的注解都是在方法执行前生效。 |
| after      | boolean | false  | 表示绑定的注解将在方法执行后执行。                           |

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

当需要在测试前进行初始化操作时，可以继承Initialize实现preHook方法。

```java
public class MyInit implements Initialize{
    @Override
    public void preHook(){
        //do something
    }
}
```

方法说明

| 方法名  | 返回值 | 说明                     |
| ------- | ------ | ------------------------ |
| preHook | void   | 会在框架初始化完成后执行 |

## 2.2 Shutdown

当测试完成后退出系统前需要做一些收尾工作时，可以继承Shutdown实现postHook方法。

```java
public class MyShutdown implements Shutdown{
    @Override
    public void postHook(){
        //do something
    }
}
```

### 方法说明

| 方法名   | 返回值 | 说明             |
| -------- | ------ | ---------------- |
| postHook | void   | 测试结束时会执行 |

## 2.3 Requester

需要先说明一下，框架采用的http包为okhttp3，对于[requester](#23-requester)和[responder](#24-responder)的实现需要基于okhttp3。原因是设计之初并未考虑这部分的扩展，其实okhttp3已经够好了，不需要使用其他http框架，但是大家可以基于okhttp3自定义Requester(客户端)。

当框架提供的Requester实现无法满足需求时，可自己实现Requester，自己实现时需要在配置文件中指定实现类的完整类名

```java
public class MyRequester implements Requester{
    //do something
}
```

application.properties

```properties
fastest.api.http.requester=xxx.xxx.MyRequester
```

### 方法说明

| 方法名                      | 返回值    | 说明                                                         |
| --------------------------- | --------- | ------------------------------------------------------------ |
| metadata                    | Metadata  | 可以给requester添加metadata，此时会覆盖原来的metadata，也可获取requester的metadata，可以单独修改metadata的元素 |
| metadata(Metadata metadata) | Metadata  | 传入一个metadata，替换掉原来的metadata。                     |
| settings                    | Settings  | 框架提供的request设置项，包含简单的http所需的设置。          |
| getResponder                | Responder | 获取框架提供的http响应对象，参考[Responder](#24-responder)   |
| sync                        | void      | 同步请求                                                     |
| async                       | void      | 异步请求                                                     |
| asserts                     | Asserts   | 直接通过requester对http响应进行断言                          |

## 2.4 Responder

自定义实现Http响应解析。需要在配置文件指定实现类的完整类名

```java
public class MyResponder implements Responder{
    //do something
}
```

application.properties

```properties
fastest.api.http.responder=xxx.xxx.MyResponder
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



# 3. 项目使用方法

## 配置方法

pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>xyz.thinktest</groupId>
            <artifactId>fastest-api</artifactId>
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
java -cp fastest-test-1.0-SNAPSHOT.jar:fastest-test-1.0-SNAPSHOT-tests.jar org.testng.TestNG [testng xml path]
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
|   fastest.api.http.requester   | 否       |          DefaultRequester           |                   自定义Requester的实现类                    |
|   fastest.api.http.responder   | 否       |          DefaultResponder           |                   自定义Responder的实现类                    |
|   fastest.rest.print.request   | 否       |                true                 | 全局参数，控制打印http请求的log，单次请求可通过@HttpLog的showRequestLog覆盖该参数的值 |
|  fastest.rest.print.response   | 否       |                false                | 全局参数，控制打印http响应的log，单次请求可通过@HttpLog的showResponseLog覆盖该参数的值。鉴于某些响应body比较大，所以默认不打印 |
|  fastest.cache.expired.period  | 否       |                10000                |        缓存管理器定时清理过期数据的时间间隔，单位毫秒        |



<!-- end -->

[testcase_layer]:data:image/png;base64,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