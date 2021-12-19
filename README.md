<h1 align="center">EASY-TEST</h1>

# 0、前言

EASY-TEST一个基于注解的快速(HTTP)测试框架。HTTP实现使用OKHTTP。

# 1、模块介绍

## 1.1 HTTP模块

### 1.1.1 Requester

Requester就像是一个单独的用户，理论上来说一个用户只能有一个用户信息。不过为了方便使用，还是开放支持在header中指定用户信息。

- 当理解为用户时，建议通过构造函数注入鉴权信息。如果需要其他用户，则重新实例化一个新的对象
- 当理解为客户端时，可以通过header修改鉴权信息。不过不建议这么做，因为每次修改header数据时容易造成数据混乱，导致用例失败。

#### 1.1.1.1 创建实例

作为用户时

```java
Requester requester = Requester.create(map);
```

或者

```java
Requester requester = Requester.create(header);
```

以上两种方式都不需要在请求的header中再次指定鉴权信息

作为客户端时

```java
Requester requester = Requester.create();
```

#### 1.1.1.2 构建参数

可进行url、http method、url parameter、form body、json body设置

```java
Requester requester = Requester.create(map);
requester.metadata().setUrl();
requester.metadata().setHttpMethod();
requester.metadata().setParameters();
requester.metadata().setForms();
requester.metadata().setJson();
```

#### 1.1.1.3 发送请求

```java
Requester requester = Requester.create(map);
//构建参数
requester.metadata().setUrl();
requester.metadata().setHttpMethod();
requester.metadata().setParameters();
requester.metadata().setForms();
requester.metadata().setJson();
//发送请求
requester.sync();
```

发送请求共有sync和async两种方式。sync会阻塞进程，而async则不会阻塞进程。async一般不用。

### 1.1.2 Respondor

重新包装后的响应信息对象。添加断言功能（该功能也添加到requester中）。通常情况我们不会直接new该对象。都是通过requester.getResponse()来获取。

```java
Requester requester = Requester.create(map);
requester.sync();
requester.getResponse();
```

### 1.1.3 Metadata

http元数据，包含url，httpmethod，url参数，form，json，header

用法：

```java
Requester requester = Requester.create();
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
Requester requester = Requester.create(map);
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
        Requester requester = Requester.create();
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

参考okhttp的client builder。

### 1.1.5 Asserts

断言工具

```java
@Component
public class MyStep implements Step {
    @Autowired
    HelloController controller;

    @Capture(isThrow = false)
    public Responder test1(){
        Requester requester = Requester.create();
        Headers headers = new Headers();
        headers.write(new Header("cookie", "cookie"));
        requester.metadata().setHeaders(headers);
        return controller.testRestMetadata(requester);
    }

    public boolean restorer() {
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

HelloController被增强后，可以使用[RestMetadata](#1.2.7 @RestMetadata)功能及类似功能。

### 12.2 @Autowired

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
| targetClass | Class  | Autowired.class | 如果是有参构造，则需要指定获取构造参数的类，参考[Autowired-有参构造](# 有参构造) |
| method      | String | ""              | 如果是有参构造，则需要指定获取构造参数的类的处理方法，参考[Autowired-有参构造](# 有参构造) |
| isOrigin    | bool   | false           | 本次注入增强实例还是普通实例                                 |



### 12.3 @Value

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

### 1.2.4 @Singleton

#### 用法说明

创建单例实例

```java
@Component
@Singleton
public class HelloController {
}
```

### 1.2.5 @Capture

#### 用法说明

在代码运行中可能出现unchecked exception，但是又不想写try{}catch{}。可以使用该注解做用到方法上。捕获代码运行中的异常，并输出到日志。默认会重新抛出异常

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

### 1.2.6 @LoggerSlf4j

#### 用法说明

自动注入Logger对象到变量中。log类型为org.slf4j.Logger

```java
public class CaseTest {
    @LoggerSlf4j
    static Logger logger;

    @Test
    public void case1() {
        logger.info("测试日志");
    }
}
```

#### 参数说明

| 参数  | 类型   | 默认值 | 说明                                             |
| ----- | ------ | ------ | ------------------------------------------------ |
| value | string | ""     | Logger名称，为空字符串时，则使用该字段所在的类名 |

### 1.2.7 @RestMetadata

#### 用法说明

自动装载api配置文件中的host、api，httpmethod信息

配置文件的位置默认在resources/apiconf下，会读取该目录下(包括子目录)所有的json文件，所以该目录请只放api配置文件.。如需自定义apiconf目录的路径，则在配置文件（yaml）中加入节点apiconfig.folderpath=YOURPATH

api配置文件模板可通过"ReadApiConfig.printTemplate();"查看，该方法也会返回该模板

api.json

```json
[
  {"serverName": "user_system", "desc": "用户系统服务", "host": "http://user.testdemo.com",
    "apis": [
      {"apiName":"get_userinfo_by_id","api": "/api/userinfo", "method": "get", "desc": "通过id获取用户信息"},
      {"apiName":"get_userinfo_by_id_restful","api": "/api/{id}/userinfo", "method": "get", "desc": "通过id获取用户信息(restful)"}
    ]
  },

  {"serverName": "manager_system", "desc": "管理员系统服务", "host": "http://manager.testdemo.com",
    "apis": [
      {"apiName":"get_manager_info_by_id","api": "/api/managerInfo", "method": "get", "desc": "通过id获取管理员信息"},
      {"apiName":"get_manager_info_by_id_restful","api": "/api/{id}/managerInfo", "method": "get", "desc": "通过id获取管理员信息(restful)"}
    ]
  }
]
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
Http Url:http://user.testdemo.com/api/userinfo
Http Method:GET
Http Header:[]
Http QueryParameters:{}
Http Forms:{}
Http Json:
Metadata{url=http://user.testdemo.com/api/userinfo, method=GET, headers=[], parameters={}, forms={}, json={}}
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

直接在注解中配置host，api，httpmethod等信息

```java
@Component
@Singleton
@RestServer("manager_system")
public class HelloController {

    @RestTemp(host = "http://test.com", api = "/usr/{id}", method = HttpMethod.GET)
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
| api    | string     | 必填   | 接口                                                         |
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

在方法之后执行，可以打印执行结果等一系列后置操作。用法参考[@PreMethod](# 1.2.10 @PreMethod)

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

### 1.2.13 @Before

#### 使用说明

类似aop的(before)切入点

***[自定义方法注解](##1.3 自定义注解)***时，指定该注解的功能在被注解方法之前执行。value参数指定该注解的实现类。

以@RestMatedate注解为例

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Before(RestMetadataProcess.class)
public @interface RestMetadata {
    String serverName() default "";
    String apiName();
    String desc() default "";
    boolean auto() default true;
    boolean sync() default true;
}
```

表示@RestMetadata在被注解方法执行之前执行。

#### 参数说明

| 参数  | 类型  | 默认值 | 说明             |
| ----- | ----- | ------ | ---------------- |
| value | Class | 必填   | 注解的功能实现类 |

### 1.2.14 @After

#### 使用说明

类似aop的(after)切入点

***[自定义方法注解](##1.3 自定义注解)***时，指定该注解的功能在被注解方法之后执行。value参数指定该注解的实现类。功能和@Before一样。

#### 参数说明

| 参数  | 类型  | 默认值 | 说明             |
| ----- | ----- | ------ | ---------------- |
| value | Class | 必填   | 注解的功能实现类 |

### 1.2.15 @MutexAnnotation

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

### 1.2.16 @Recovery

#### 使用说明

用于控制每次测试结束后执行Step.recovery方法，进行数据或场景恢复

#### 参数说明

| 参数     | 类型  | 默认值       | 说明             |
| -------- | ----- | ------------ | ---------------- |
| executor | Class | RecoveryStep | 恢复操作的执行者 |

## 1.3 自定义注解

实现用户自己的注解功能，目前只提供两种类型的自定义注解，字段注解和方法注解

### 1.3.1 字段注解

字段注解的功能实现类必须继承FieldAnnotationProcessable类

```java
public class MyCustomFieldAnnotation implements FieldAnnotationProcessable {
    @Override
    public void process(JoinPoint joinPoint) {
        //do something
    }
}
```

JoinPoint是被拦截字段的一些信息，包含注解对象、被拦截字段、被拦截字段所在的对象、以及当前实现类对象

### 1.3.2 方法注解

方法注解的功能实现类必须继承MethodAnnotationProcessable类

```java
public class MyCustomMethodAnnotation implements MethodAnnotationProcessable {

    @Override
    public void process(JoinPoint joinPoint) {
        //do something
    }
}
```

JoinPoint是被拦截方法的信息，包含注解对象、被拦截方法、被拦截方法的参数、被代理的对象、实现类的对象

# 2. 项目使用方法

## 配置方法

pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>org.easy</groupId>
            <artifactId>fastest</artifactId>
            <version>2.0-SNAPSHOT</version>
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
            <outputDirectory>resources</outputDirectory>
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
java -cp fastest-test-1.0-SNAPSHOT.jar:fastest-test-1.0-SNAPSHOT-tests.jar org.TestNG [testng xml path]
```

## 其他说明

1、默认从resources目录下读取文件，如果有其他路径的需要自行在assmebly.xml配置源路径和目标路径。如果使用resources目录作为各种文件的根目录，则向项目中可以使用FileUtil.RESOURCES_PATH，如果不适用resources目录，则需要使用FileUtil.PROJECT_ROOT拼接路径，或使用其他可识别的路径。

2、resources读取的是src/main/resources目录，这是为了照顾大家的使用习惯