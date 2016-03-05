# Gabriel
存放一些日常写的代码:

hotswap : java 基于1.6 attach instrumenttation 的线上动态更新项目.

MemDbComponent:内存数据库存入物理数据库的缓存中间件

countAwareOrderedExecutor:对相同签名任务进行数量控制并使它们按顺序执行的线程池，可作为游戏服务器netty异步执行客户端传来数据的线程池中间件。(通过update整体更新方式);


AsynResourceAdder:提供游戏中的异步增加资源接口来应对如战斗这些低延迟的场景。
1.使用原子变量无锁增加资源
2.对执行线程池和Interrupted线程的干净清理。
3.通过模仿Netty哨兵,防止执行task被重复加入到遍历线程中。(在actor模式中经常吃用到)
