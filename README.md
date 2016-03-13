# Gabriel
存放一些日常写的代码:

hotswap : java 基于1.6 attach instrumenttation 的线上动态更新项目.

MemDbComponent:内存数据库存入物理数据库的缓存中间件

countAwareOrderedExecutor:对相同签名任务进行数量控制并使它们按顺序执行的线程池，可作为游戏服务器netty异步执行客户端传来数据的线程池中间件。(通过update整体更新方式);


AsynResourceAdder:提供游戏中的异步增加资源接口来应对如战斗这些低延迟的场景。
1.使用原子变量无锁增加资源
2.对执行线程池和Interrupted线程的干净清理。
3.通过模仿Netty哨兵,防止执行task被重复加入到遍历线程中。(在actor模式中经常吃用到)

Activity: 项目用于将游戏中活动相关的数据库对象转为相应实体对象在业务逻辑中进行使用
Class PlayerEvent : pid,type,param1,param2,......
通过注解反射,可以将数据库存储和实际对象进行转化使业务逻辑更清楚
class ChargeActivity:@paramName("param1") int chargeTime @paramName("param2") int chargeNum

NetFunctionInvocation：
项目名:网络接口注解IOC控制框架:
模仿struct框架对包中的接口层进行扫描。
原理:
1.标有@Action的类和它的继承者们是网络接口类,在其中标有@Command("player@getPlayerName")的代表调用方法,
@param("value")代表需要传的参数名.
2.当带有command和参数map的request从网络中传来即可经过拦截器调用指定函数。





