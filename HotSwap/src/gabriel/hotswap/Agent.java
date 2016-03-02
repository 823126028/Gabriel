package gabriel.hotswap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Agent {
	
	public static void agentmain(String arg,Instrumentation instru){
		Set<String> set = new HashSet<String>();
		String path = parseParams(set, arg);
		List<Class<?>> classList = getVailuebleList(instru, set);
		if(classList.size() <= 0)
			return;
		
		for (Class<?> clazz : classList) {
			instru.addTransformer(new TransFormer(getFile(clazz.getName(), path),clazz.getName()), true);
		}
		try {
			instru.retransformClasses(classList.toArray(new Class<?>[classList.size()]));
		} catch (UnmodifiableClassException e) {
			e.printStackTrace();
		}
		System.out.println("load sucess");
		
	}
	
	/**
	 * 
	 * @param className 需要读的类名
	 * @param filePath 文件的URL
	 * @return
	 */
	public static byte[] getFile(String className,String filePath){
		String fileName = filePath + File.separator + className + ".class";
		System.out.println(fileName);
		File file = new File(fileName);
		ByteArrayOutputStream byteArrayOutputStream = null;
		InputStream inPutStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream(200);
			byte[] byteArray = new byte[2048]; 
			inPutStream = new FileInputStream(file);
			while(true){
				int c = inPutStream.read(byteArray);
				if(c <= 0)
					break;
				byteArrayOutputStream.write(byteArray,0,c);
			}
			return byteArrayOutputStream.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
				try {
					if(byteArrayOutputStream != null){
						byteArrayOutputStream.close();
					}
					if(inPutStream != null){
						inPutStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	/**
	 * 获得所有需要载入的类
	 * @param instru
	 * @param set
	 * @return
	 */
	public static List<Class<?>> getVailuebleList(Instrumentation instru,Set<String> set){
		Map<String,Class<?>> classesMap = getClassesMap(instru);
		List<Class<?>> classList =new ArrayList<Class<?>>();
		for (String s : set) {
			Class<?> clazz = classesMap.get(s);
			if(clazz != null){
				classList.add(clazz);
			}else{
				System.out.println("no this class:"+clazz);
			}
		}
		return classList;
	}
	
	/**
	 * @param instru
	 * @return
	 */
	public static Map<String,Class<?>> getClassesMap(Instrumentation instru){
		Class[] clazzArray = instru.getAllLoadedClasses();
		Map<String,Class<?>> map = new HashMap<String,Class<?>>();
		for (Class clazz : clazzArray) {
			map.put(clazz.getName(), clazz);
		}
		return map;
	}
	
	/**
	 * 解析出需要更换 -c class 二进制字节码   -path 地址 
	 * @param set
	 * @param arg
	 * @return
	 */
	public static String parseParams(Set<String> set,String arg){
		String[] params = arg.split(" ");
		boolean p = false;
		boolean c = false;
		String path = "";
		for (String param : params) {
			if(param.equals("-c")){
				p = false;
				c = true;
			}else if(param.equals("-p")){
				p = true;
				c = false;
			}else{
				if(!param.trim().equals("")){
					if(p){
						path = param.trim();
					}else if(c){
						set.add(param.trim());
					}
				}
			}
		}
		return path;
	}
	
	public static void premain(String arg,Instrumentation instru){
		agentmain(arg, instru);
	}
}
