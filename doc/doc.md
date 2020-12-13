## Flash-Framework使用说明

### 一、flash-framework-core

#### 1. Maven 依赖

```xml
<dependency>
     <groupId>com.flash.framework</groupId>
     <artifactId>flash-framework-core</artifactId>
     <version>1.0.0.RELEASE</version>
</dependency>
```

#### 2.功能说明

##### 2.1 Spring容器初始化监听

```java
InitListenerHandler
```

可用于实现在Spring容器初始化后，初始化一些业务操作，例如应用初始化后校验数据库和缓存中的数据一致性。

注意：InitListenerHandler执行失败不会影响SpringBoot应用正常启动

* 使用方式

实现InitListenerHandler接口，并标注@SpringBootInitHandler注解

```java
@SpringBootInitHandler(order = 1)
public class DemoInitListenerHandler implements InitListenerHandler {

    @Override
    public void handle(ApplicationContext applicationContext) {
        System.out.println("DemoInitListenerHandler do handle");
    }
}
```

* 异步执行

```java
@SpringBootInitHandler(order = 2, async = true)
public class AsyncDemoInitListenerHandler implements InitListenerHandler {

    @Override
    public void handle(ApplicationContext applicationContext) {
        System.out.println("AsyncDemoInitListenerHandler do handle");
    }
}
```

##### 2.2 Spring容器上下文

ApplicationContextHolder通过静态方法的方式，可以在非Spring Bean的类中，获取ApplicationContext及Spring中的Bean

```java
//基于class获取Spring中的bean
ApplicationContextHolder.getBeansOfType(InitListenerHandler.class);
//基于名称获取Spring中的bean
ApplicationContextHolder.getBean("asyncDemoInitListenerHandler");
```

##### 2.3 批处理

```java
BatchExecutor
```

* 使用方式

```java
BatchExecutor.builder()
    			//分页数据批处理
                .itemReader(PagingItemReader.builder()
                         //设置批处理上下文
                        .context(new DemoPagingContext())
                         //设置每页处理数量
                        .pageSize(1)
                         //设置分页处理逻辑   
                        .handler(((pageNo, pageSize, readerContext) -> {
                            //查询一页数据
                            int start = (int) ((pageNo - 1) * pageSize);
                            int end = start + (int) pageSize;
                            Paging<Demo> paging = new Paging<>();
                            paging.setPageSize(1);
                            paging.setTotal(datas.size());
                            paging.setRecords(datas.subList(start, end));
                            return paging;
                        }))
                        .build())
    			//设置批处理策略
                .processorStrategy(new SimpleBatchProcessorStrategy())
                .build()
    			//设置每页数据处理逻辑
                .process((datas) ->
                        System.out.println("batch process : " + JSON.toJSONString(datas))
                );
```

* 批处理策略

  * SimpleBatchProcessorStrategy

    每查询一页数据，处理一页数据，所有数据的查询、处理均在一个线程中执行，顺序执行。可以对一页数据一起处理，也可以一条一条处理。

  * PagingConcurrentBatchProcessorStrategy

    每页分配一个线程去执行，有几页就分配几个线程，每个线程执行查询和数据处理操作，并发执行。

    ```java
    BatchExecutor.builder()
                    .itemReader(PagingItemReader.builder()
                            .context(new DemoPagingContext())
                            .pageSize(1)
                            .handler(((pageNo, pageSize, readerContext) -> {
                                int start = (int) ((pageNo - 1) * pageSize);
                                int end = start + (int) pageSize;
                                Paging<Demo> paging = new Paging<>();
                                paging.setPageSize(1);
                                paging.setTotal(datas.size());
                                paging.setRecords(datas.subList(start, end));
                                return paging;
                            }))
                            .build())
     //PagingConcurrentBatchProcessorStrategy需要设置线程池               .processorStrategy(PagingConcurrentBatchProcessorStrategy.builder()
                            .executor(threadPoolTaskExecutor).build())
                    .build()
                    .process((datas) ->
                            System.out.println("batch process : " + JSON.toJSONString(datas))
                    );
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
    
            }
    ```

  * ProcessConcurrentBatchProcessorStrategy

    每查询一页数据，处理一页数据，分页查询在主线程中执行，每页数据的处理分配单独线程执行。

    ```java
    BatchExecutor.builder()
                    .itemReader(PagingItemReader.builder()
                            .context(new DemoPagingContext())
                            .pageSize(1)
                            .handler(((pageNo, pageSize, readerContext) -> {
                                int start = (int) ((pageNo - 1) * pageSize);
                                int end = start + (int) pageSize;
                                Paging<Demo> paging = new Paging<>();
                                paging.setPageSize(1);
                                paging.setTotal(datas.size());
                                paging.setRecords(datas.subList(start, end));
                                return paging;
                            }))
                            .build())
     //ProcessConcurrentBatchProcessorStrategy需要设置线程池               .processorStrategy(ProcessConcurrentBatchProcessorStrategy.builder()
                            .executor(threadPoolTaskExecutor).build())
                    .build()
                    .process((datas) ->
                            System.out.println("batch process : " + JSON.toJSONString(datas))
                    );
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
    
            }
    ```

##### 2.4 事件处理

事件机制思想基本为观察者模式，主要用来解耦一些比较复杂的业务逻辑，也可以用来实现业务应用内的异步处理。

```
EventHandler
```

* 使用方式

```yaml
##开启事件机制，默认是关闭的
flash.event.enable: true
```

```java
//事件处理实现
@Component
public class DemoEventHandler implements EventHandler<DemoEvent> {

    @Override
    @EventListener(classes = DemoEvent.class)
    @Subscribe
    public void handler(DemoEvent event) {
        System.out.println("accept event : " + JSON.toJSONString(event));
    }
}

//通过EventService发布事件
//事件发布-Spring事件机制
DemoEvent event = new DemoEvent("demo", EventSource.SPRING);
event.setDemo("SpringDemo");
eventService.publishEvent(event);
//事件发布-EventBus事件机制
DemoEvent event = new DemoEvent("demo", EventSource.EVENT_BUS);
event.setDemo("EventBusDemo");
eventService.publishEvent(event);
```

事件的实现机制是基于Spring自身的事件机制（默认同步执行）和Guava EventBus（异步执行）实现，事件处理方依赖于Spring的@EventListener注解和Guava的@Subscribe注解。

* 日志记录

由于Spring的事件机制和Guava的EventBus事件机制均为内存实现，不会记录任何事件的执行轨迹，在服务异常、GC频繁、负载较高的场景下容易出现丢事件的情况（事件发布了，但是处理端没收到）。为了方便排查事件的执行情况，提供了事件日志记录机制，一但出现事件发布了，处理端没收到的情况，就会记录日志。并且提供日志的查询接口，可以配合定时任务进行补偿。

```yaml
##开启事件日志记录，默认是关闭的
flash.event.log.enable: true
```

日志记录接口为EventLogHandler，默认实现为RedisEventLogHandler，需要引入Redis配置，开发者可以根据自己的需求定制EventLogHandler。

##### 2.5 扩展点机制

扩展机制一般用于接口、抽象类在不同的业务场景下，有不同的实现，但是在业务调用的入口处，是按照接口或者抽象类的方式调用（面向接口编程），这样后续有新的业务场景扩展，只需要重新增加一个接口/抽象类的实现即可。

* 使用方式

  * 定义一个接口或者抽象类

    ```java
    /**
    * 文件存储接口
    */
    public interface StorageService {
    	
        void save(@BizCodeParam String bizCode);
    
        void save(@BizCodeParam(el = "#storage") OssBizParam param);
    }
    ```

  * 定义接口或者抽象类的实现类

    ```java
    //OSS存储实现
    @ExtensionProvider(group = "storage", bizCode = "oss")
    public class OssStorageService implements StorageService {
    
        @Override
        public void save(String bizCode) {
            System.out.println("oss save");
        }
    
        @Override
        public void save(OssBizParam param) {
            System.out.println("oss save");
        }
    }
    //其他存储实现
    @ExtensionProvider(group = "storage", bizCode = "other")
    public class OtherStorageService implements StorageService {
    
        @Override
        public void save(String bizCode) {
            System.out.println("other save");
        }
    
        @Override
        public void save(OssBizParam param) {
            System.out.println("other save");
        }
    }
    ```

  * 业务调用

    ```java
    @Service
    public class StorageBizService {
    
        @Extension("storage")
        private StorageService storageService;
    
        /**
         * 测试基于bizCode动态选择实现
         *
         * @param bizCode
         */
        public void save(String bizCode) {
            storageService.save(bizCode);
        }
    
        /**
         * 测试基于spel获取bizCode
         *
         * @param param
         */
        public void save(OssBizParam param) {
            storageService.save(param);
        }
    }
    ```

* @ExtensionProvider

  扩展点实现注解，包含如下注解配置：

  | 注解字段  | 是否必填 | 描述                                                         |
  | --------- | -------- | ------------------------------------------------------------ |
  | group     | 是       | 扩展实现分组, 与@Extension 中的value对应；用于换分业务分组，例如订单业务、退款单业务 |
  | bizCode   | 是       | 全局唯一业务编码，支持多个业务编码配置；同一个group下的不同业务实现，例如订单业务下，自提订单和派送订单业务,又或者是对于一种数据的不同类型、不同状态有不同的处理（拆解大量的if elseif ... else）。 |
  | desc      | 否       | 扩展点实现描述                                               |
  | isDefault | 否       | 是否为默认实现，若当前执行bizCode的实现不存在，则走默认实现  |

  ```java
  @ExtensionProvider(group = "storage", bizCode = "oss")
  @ExtensionProvider(group = "storage", bizCode = "other")
  ```

* @BizCodeParam

  bizCode参数注解，用于在参数列表中标识bizCode字段，包含如下注解配置：

  | 注解字段 | 是否必填 | 描述     |
  | -------- | -------- | -------- |
  | el       | 否       | el表达式 |

  ```java
  void save(@BizCodeParam String bizCode);
  
  void save(@BizCodeParam(el = "#storage") OssBizParam param);
  ```

* @Extension

  扩展接口依赖注入注解，用于在Spring Bean中注入接口，作用类似于@Autowired，包含如下注解配置：

  | 注解字段 | 是否必填 | 描述                               |
  | -------- | -------- | ---------------------------------- |
  | value    | 是       | 与@ExtensionProvider 中的group对应 |

  ```java
  @Extension("storage")
  private StorageService storageService;
  ```

##### 2.6 业务流程扩展机制

业务流程扩展机制包括链式调用和图调用，主要用于复杂的业务拆分和扩展。以采供系统的合同创建为例，可以使用链式调用拆分为一下几个节点：

* 合同信息校验
* 构建合同对象，合同金额计算
* 持久化存储
* 提交审批

将业务流程细化拆分成节点，可以方便为后续新需求逻辑进行扩展，只需要调整流程配置文件，修改节点顺序或者增减节点，不需要动历史节点的逻辑即可实现扩展。另外，对于实现的节点可以方便的复用，一般情况下会对公用业务逻辑进行抽取作为通用节点。例如合同信息校验，对于合同主体信息校验是公用的可以抽象出一个校验节点，但是合同类型特别多的话，每个类型校验又不同，就可以针对每种类型的合同创建一个节点，通过条件项配置来控制不同类型的合同走不同类型的节点校验。

* 使用方式
  * 实现BizProcessor接口并标注@Processor，即可将一个类标注为一个流程节点
  * 实现BizProcessor中的execute方法和failback方法
  * 在Spring的resources目录下增加PROCESSORS文件夹，并创建*Processor.json文件
  * 通过BizProcessorManager.doProcessors方法来调用指定的业务流程
* BizProcessor接口方法说明
  * execute 为Processor节点正常业务执行方法
  * failback 为Processor节点execute方法执行失败后回滚或者补偿执行方法

  ```java
public interface BizProcessor<C> {

    /**
     * execute
     *
     * @param context 上下文
     */
    void execute(C context) throws Exception;

    /**
     * execute fallback
     *
     * @param context 上下文
     */
    void failback(C context) throws Exception;
}
  ```

* @Processor注解说明

| 注解字段  | 是否必填 | 描述                                         |
| --------- | -------- | -------------------------------------------- |
| name      | 否       | Processor节点名称                            |
| condition | 否       | 节点执行条件，支持el表达式，仅作用于链式调用 |

* Spring扩展配置

| 配置                                 | 描述                  | 默认值      |
| ------------------------------------ | --------------------- | ----------- |
| flash.framework.bizProcessors.path   | Processor配置文件路径 | PROCESSORS  |
| flash.framework.bizProcessors.prefix | Processor配置文件前缀 | *Processors |
| flash.framework.bizProcessors.suffix | Processor配置文件后缀 | json        |

###### 1、链式流程

* 配置说明

```json
[
  {
    "bizScope": "业务域",
    "operation": "操作名称",
    "processorChain": [
      "Processor节点1 name",
      "Processor节点2 name"
    ]
  }
]
```

###### 2、DAG流程

```json
[
	{
    "bizScope": "业务域",
    "operation": "操作名称",
    "processorGraph": [
      {
        //前驱节点
        "predecessor": "Processor节点1 name",
        //后继节点
        "successor": "Processor节点2 name"
      },
      {
        "predecessor": "Processor节点1 name",
        "successor": "Processor节点3 name"
      },
      {
        "predecessor": "Processor节点2 name",
        "successor": "Processor节点4 name"
      },
      {
        "predecessor": "Processor节点3 name",
        "successor": "Processor节点4 name"
      }
    ]
  }
]
```

##### 2.6 状态机

状态机主要用于状态流转和事件触发，例如订单在待支付状态，触发支付操作，订单状态会流转为已支付。

* 使用方式
  * 配置StateMachineFactory
  * 在StateMachineFactory中构建对应业务场景的StateMachine.StateMachineBuilder
  * 通过StateMachineFactory.get().fire()来触发状态机流转

```java
@Bean
public StateMachineFactory demoStateMachineFactory() {
  return new StateMachineFactory() {
    @Override
    public void configure() {
      //构建订单业务状态流转
      StateMachine.StateMachineBuilder<String, String, Object> orderStateMachineBuilder = StateMachine.StateMachineBuilder.builder("order");
      orderStateMachineBuilder
        .addTransition("not_paid", "paid", "paid")
        .addTransition("paid", "delivery", "deliveryed")
        .addTransition("deliveryed", "confirm", "confirmed")
        .addTransition("not_paid", "cancel", "canceled", (event, to, context) -> {
          System.out.println("event : " + event);
          System.out.println("to : " + to);
          System.out.println("context : " + context);
        });
      //将订单状态添加到状态机
      addStateMachine(orderStateMachineBuilder.build());
    }
  };
}
```

```java
@Autowired
private StateMachineFactory stateMachineFactory;

@Test
public void test1() {
  System.out.println(stateMachineFactory.get("order").fire("not_paid", "paid", "付钱"));
  System.out.println(stateMachineFactory.get("order").fire("not_paid", "cancel", "取消"));
}
```

##### 2.7 重试

类似于Spring Retry的功能，提供注解的方式和代码嵌套的方式实现对应业务代码的异常重试。

* 使用方式

```java
@Retry(retry = 3, exception = ArithmeticException.class)
public Boolean demo() {
  int r = 1 / 0;
  return Boolean.TRUE;
}
```

```java
@Autowired
private RetryHelper retryHelper;

@Test
public void testByRetryHelper() throws Exception {
  retryHelper.retry(2, 1000, ArithmeticException.class, () -> {
    int r = 1 / 0;
    return Boolean.TRUE;
  });
}
```

* @Retry注解

| 注解字段  | 是否必填 | 描述                            |
| --------- | -------- | ------------------------------- |
| retry     | 否       | 重试次数，默认为1               |
| interval  | 否       | 重试间隔时间，默认为500ms       |
| exception | 否       | 触发重试的异常，默认为Exception |
| time      | 否       | 默认TimeUnit.MILLISECONDS       |

###  二、flash-framework-web

#### 1. Maven 依赖

```xml
<dependency>
     <groupId>com.flash.framework</groupId>
     <artifactId>flash-framework-web</artifactId>
     <version>1.0.0.RELEASE</version>
</dependency>
```

#### 2.功能说明

##### 2.1 统一Response结果

```json
{
  "code" : 200,
  "success" : true,
  "body" : "业务返回数据json体，可能是JSONObject，也可能是JSONArray",
  "message" : "异常信息，success=false是会出现"
}
```

注意：对于content-type为非application/json的请求或者Controller返回结果为ResponseEntity类型的，均会按原结果返回，不会封装。一般用于三方接口回调返回确认场景。

### 三、flash-framework-tools

#### 1. Maven 依赖

```xml
<dependency>
     <groupId>com.flash.framework</groupId>
     <artifactId>flash-framework-tools</artifactId>
     <version>1.0.0.RELEASE</version>
</dependency>
```

#### 2.功能说明

##### 2.1 推送封装

```java
package com.flash.framework.tools.push;

/**
 * 推送服务
 *
 * @author zhurg
 * @date 2019/6/13 - 下午4:50
 */
public interface PushService {


    /**
     * 推送给设备标识参数的用户(弹窗)
     *
     * @param registrationIds 设备ID
     * @param message         消息
     * @return
     */
    boolean sendNotificationToRegistrationId(PushMessage message, String... registrationIds);

    /**
     * 推送给所有Android用户(弹窗)
     *
     * @param message 消息
     * @return
     */
    boolean sendNotificationToAllAndroid(PushMessage message);

    /**
     * 推送给所有IOS用户(弹窗)
     *
     * @param message
     * @return
     */
    boolean sendNotificationToAllIos(PushMessage message);

    /**
     * 推送给指定tag的Android用户(推送)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendNotificationToTagAndroid(PushMessage message, String... tags);

    /**
     * 推送给指定tag的IOS用户(推送)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendNotificationToTagIos(PushMessage message, String... tags);

    /**
     * 推送给设备标识参数的用户(消息)
     *
     * @param registrationIds
     * @param message
     * @return
     */
    boolean sendMessageToRegistrationId(PushMessage message, String registrationIds);

    /**
     * 推送给全部Android用户(消息)
     *
     * @param pushMessage
     * @return
     */
    boolean sendMessageToAllAndroid(PushMessage pushMessage);


    /**
     * 推送给所有IOS用户(消息)
     *
     * @param message
     * @return
     */
    boolean sendMessageToAllIos(PushMessage message);

    /**
     * 推送给指定tag的Android用户(消息)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendMessageToTagAndroid(PushMessage message, String... tags);

    /**
     * 推送给指定tag的IOS用户(消息)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendMessageToTagIos(PushMessage message, String... tags);

    /**
     * 推送给全部用户(弹窗)
     *
     * @param pushMessage
     * @return
     */
    boolean sendNotificationToAll(PushMessage pushMessage);

    /**
     * 推送给全部用户(消息)
     *
     * @param pushMessage
     * @return
     */
    boolean sendMessageToAll(PushMessage pushMessage);
}
```

* JPUSH实现

```yaml
push:
  ## 需要开启push
  enable: true
	app-key: "ak"
	app-secret: "as"
	## JPUSH 配置
	jpush: 
		enable: true
```

##### 2.2 对象存储

```java
/**
 * 流媒体服务
 *
 * @author zhurg
 * @date 2019/6/6 - 下午3:33
 */
public interface StorageService {

    /**
     * 上传二进制数据流
     *
     * @param objectName 存储对象名称
     * @param bytes      二进制数据
     * @return
     * @throws Exception
     */
    ObjectStorageResponse upload(String objectName, byte[] bytes) throws Exception;

    /**
     * 上传输入流中的数据
     *
     * @param objectName  存储对象名称
     * @param inputStream 数据流对象
     * @return
     * @throws Exception
     */
    ObjectStorageResponse upload(String objectName, InputStream inputStream) throws Exception;

    /**
     * 上传文件
     *
     * @param objectName 存储对象名称
     * @param file       文件
     * @return
     * @throws Exception
     */
    ObjectStorageResponse upload(String objectName, File file) throws Exception;

    /**
     * 下载文件
     *
     * @param objectName 存储对象名称
     * @param localFile  本地文件路径
     * @throws Exception
     */
    void download(String objectName, String localFile) throws Exception;

    /**
     * 下载文件
     *
     * @param objectName 存储对象名称
     * @param file       本地文件
     * @throws Exception
     */
    void download(String objectName, File file) throws Exception;

    /**
     * 删除文件
     *
     * @param objectName 存储对象名称
     * @throws Exception
     */
    void delete(String objectName) throws Exception;
}
```

* 阿里云OSS实现

```yaml
storage:
	access-key-id: "ak"
	access-key-secret: "as"
	oss: 
		endpoint: qingdao
		domain: 
		bucket-name: "flash-framework"
```

