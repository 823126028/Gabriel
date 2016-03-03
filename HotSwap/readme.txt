该项目用于热更线上运行的java程序，在保证方法签名一致的情况下可以修改方法的内部逻辑

在打包MANIFEST.MF的时候需要添加如下:
MANIFEST.MF 
Premain-Class: gabriel.hotswap.Agent
Agent-Class: gabriel.hotswap.Agent
Can-Retransform-Classes: true

使用tools.jar
操作:
	java -classpath   .../tools.jar;..../Attach pid -c ...class -p ../../
