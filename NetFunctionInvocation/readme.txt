项目名:网络接口注解IOC控制框架:
模仿struct框架对包中的接口层进行扫描。
原理:
1.标有@Action的类和它的继承者们是网络接口类,在其中标有@Command("player@getPlayerName")的代表调用方法,
@param("value")代表需要传的参数名.
2.当带有command和参数map的request从网络中传来即可经过拦截器调用指定函数。

项目结构
	action :存放接口包的目录
	annotation:存放相关的注解
	InvocaionMananger 执行扫描注解，翻译参数的任务
	ParamInjector:所有参数解释器的父类
	scanUtils 在指定的包中扫描执行器。
