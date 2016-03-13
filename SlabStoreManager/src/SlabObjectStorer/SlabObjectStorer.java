package SlabObjectStorer;

import configuration.Configuration;
import SlabObj.SlabObject;
import SlabObj.NormalObj;

/**
 * Slab的链式存储器
 * @author Gabriel
 *
 */
public class SlabObjectStorer {
	private SlabObject header;
	private SlabObject tailer;
	private int totalNum;
	private int valuebleNum;
	
	/**
	 *遵循局部性原则， 用于记录上一个被搜寻的未被使用的object,该object的next也有未被使用的
	 */
	private SlabObject lastSearchObj;
	
	public SlabObjectStorer(){
		totalNum = 0;
		valuebleNum = 0;
		header = null;
		tailer = null;
		lastSearchObj = null;
	}
	
	/**
	 * 使存储器数量翻倍
	 */
	private void doubleSpace(){
		for(int i = 0; i < this.totalNum; i++){
			addTail(new NormalObj());
		}
	}
	
	/**
	 * 尝试着从当前链之后(不断循环)获得一个未使用的槽位,如果没有返回null
	 * @param searchObj
	 * @return
	 */
	private SlabObject tryGetValuebleOne(SlabObject searchObj){
		while (searchObj != null) {
			if(searchObj.getFlag() == 1){
				searchObj = searchObj.getNext();
				continue;
			}
			return searchObj;
		}
		return null;
	}
	
	
	/**
	 * 获得一个未被使用过的slabObject
	 * @return
	 */
	public synchronized SlabObject getOneClean(){
		if(lastSearchObj == null || lastSearchObj.getNext() == null){
			lastSearchObj = header;
		}
		if(header == null){
			return null;
		}
		
		// 如果可用数量小于 指定比例,对整个object进行扩容
		if(this.valuebleNum < (this.totalNum / Integer.parseInt(Configuration.getConfiguration(Configuration.ConfigElement.RELEASE_STRATEGY_DOUBLE_SIZE_THRESHOLD)))){
			doubleSpace();
		}
		SlabObject searchObj = lastSearchObj;
		for(int i = 0; i < 3; i++){
			searchObj = tryGetValuebleOne(searchObj);
			//如果从上次找寻到的位置继续向下找找不到，就从头开始找
			if(searchObj == null)
				searchObj = header;
			else
				this.valuebleNum -= 1;
				lastSearchObj = searchObj.getNext();
				searchObj.setFlag(1);
				return searchObj;
		}
		return null;
	}
	
	/**
	 * 从链的尾部添加
	 * @param obj
	 */
	public synchronized void addTail(SlabObject obj){
		obj.setNext(null);
		if(this.tailer == null){
			obj.setPre(null);
			this.header = obj;
			this.tailer = obj;
		}else{
			this.tailer.setNext(obj);
			obj.setPre(tailer);
			this.tailer = obj;
		}
		this.totalNum += 1;
		this.valuebleNum += 1;
	}
	
	/**
	 * 当可用slabObject达到一定数量的时候尝试缩减
	 * @param slabObject
	 * @return
	 */
	private boolean tryToShrink(SlabObject slabObject){
		if(this.valuebleNum > this.totalNum * Double.parseDouble((Configuration.getConfiguration(Configuration.ConfigElement.RELEASE_STRATEGY_REDUCE_SIZE_THRESHOLD)))){
			if(this.valuebleNum <= Integer.parseInt(Configuration.getConfiguration(Configuration.ConfigElement.RELEASE_STRATEGY_NUM_LOW))){
				return false;
			}else if(this.valuebleNum > Integer.parseInt(Configuration.getConfiguration(Configuration.ConfigElement.RELEASE_STRATEGY_NUM_LOW))
					&& this.valuebleNum <= Integer.parseInt(Configuration.getConfiguration(Configuration.ConfigElement.RELEASE_STRATEGY_NUM_HIGH)))
					tryToDestroy(slabObject);
				this.totalNum -= 1;
				return true;
			}else if(this.valuebleNum > Integer.parseInt(Configuration.getConfiguration(Configuration.ConfigElement.RELEASE_STRATEGY_NUM_HIGH))){
				SlabObject slabNext = slabObject.getNext();
				tryToDestroy(slabObject);
				this.totalNum -= 1;
				//当数量很多的时候尝试后面一个节点如果是脏链再删除一个
				if(slabNext == null || slabNext.getFlag() == 1){
					tryToDestroy(slabNext);
					this.totalNum -= 1;
				}
				return true;
		}
		return false;
	}
	
	/**
	 * 将脏链释放，(当数量过多时)有可能进行删除
	 * @param slabObject
	 */
	public synchronized void releaseSlabObject(SlabObject slabObject){
		if(slabObject == null || slabObject.getFlag() == 0){
			return;
		}	
		if(!tryToShrink(slabObject)){
			slabObject.resert();
			this.valuebleNum += 1;
		}
	}
	
	/**
	 * 直接从链上清除
	 * @param slabObject
	 */
	private void tryToDestroy(SlabObject slabObject){
		if(slabObject == header){
			header = slabObject.getNext();
			slabObject = null;
		}else if(slabObject == tailer){
			tailer = slabObject.getPre();
			slabObject = null;
		}else{
			slabObject.getNext().setPre(slabObject.getPre());
			slabObject.getPre().setNext(slabObject.getNext());
			slabObject = null;
		}
	}
	
	@Override
	public String toString(){
		return "total:" + this.totalNum + "valuble:" + this.valuebleNum;
	}
}
