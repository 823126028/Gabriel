package gabriel.hotswap;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
public class TransFormer implements ClassFileTransformer{
	private byte[] bytes;
	private String className;
	public TransFormer(byte[] bytes,String className){
		this.bytes = bytes;
		/**import steps*/
		this.className = className.replace(".", "/");
	}
	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> clazz,
			ProtectionDomain domian, byte[] bytes)
			throws IllegalClassFormatException {
		if(this.className.equals(className)){
			return this.bytes;
		}else{
			System.out.println("wrong class:" + className);
			return null;
		}

	}

}
