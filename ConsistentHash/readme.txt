项目功能:解决分布式缓存宕机和加机器问题的一致性hash

VirtualNodeManager:
节点管理器增加或减少节点


Node 类,gabriel.consistent.hash.node.Node<T>
用以表示实际的节点,记录IP等内容,包括存储方案

VirtualNode:虚拟节点

缓存类: gabriel.consistent.hash.node.cache.INodeCache<T> 提供Node中
提供缓存实现方案的接口类

LocalNodeCache:提供本地的缓存类