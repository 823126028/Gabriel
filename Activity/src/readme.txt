Activity: 项目用于将游戏中活动相关的数据库对象转为相应实体对象在业务逻辑中进行使用。
通常活动的数据库存储格式为
pid,
type,
param1,
param2,
.....

通过注解反射
ChargeActivity
@paramName("param1")
int chargeTime
@paramName("param2")
int chargeNum
可以将数据库存储和实际对象进行转化使业务逻辑更清楚

