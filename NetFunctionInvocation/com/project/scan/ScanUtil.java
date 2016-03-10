package com.project.scan;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ScanUtil {
	/**
	 * 根据包名搜索出包内的所有class类
	 * @param packageName
	 * @return
	 */
	public static HashSet<Class<?>> getClasses(String packageName){
		String pathName = packageName.replace(".", "/");
		LinkedHashSet<Class<?>> hashSet = new LinkedHashSet<Class<?>>();
		Enumeration<URL> enumerations = null;
		try {
			enumerations = Thread.currentThread().getContextClassLoader().getResources(pathName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while(enumerations.hasMoreElements()){
			URL url = enumerations.nextElement();
			if(url.getProtocol().equals("file")){
				loadClassFromFile(packageName, url.getFile(), hashSet);
			}else if(url.getProtocol().equals("jar")){
				loadClassFromJar(url, packageName, hashSet);
			}else if(url.getProtocol().equals("class")){
				try {
					hashSet.add(Thread.currentThread().getContextClassLoader().loadClass(packageName));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return hashSet;
	}
	
	/**
	 * 从jar中扫出class文件
	 * @param url
	 * @param packageName
	 * @param hashSet
	 */
	private static void loadClassFromJar(URL url,String packageName,LinkedHashSet<Class<?>> hashSet){
		try {
			JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();
			Enumeration<JarEntry> enumeration = jarFile.entries();
			while (enumeration.hasMoreElements()) {
				JarEntry jarEntry = enumeration.nextElement();
				String name = jarEntry.getName();
				if(name.charAt(0) == '/'){
					name = name.substring(1);
				}
				String className = name.replace("/", ".");
				if(!name.startsWith(packageName))
					continue;
				
				if(name.endsWith("class")){
					String packageNameTemp = className.substring(0,className.lastIndexOf("."));
					className = className.substring(className.lastIndexOf("."),className.length() - 6);
					try {
						hashSet.add(Thread.currentThread().getContextClassLoader().loadClass(packageNameTemp  + className));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据文件过滤器，过滤出class和文件夹，再递归载入类文件
	 * @param packageName
	 * @param filePath
	 * @param hashSet
	 */
	private static void loadClassFromFile(String packageName,String filePath,LinkedHashSet<Class<?>> hashSet){
		File f = new File(filePath);
		File[] files =f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if(file.isDirectory() || file.getName().endsWith("class"))
					return true;
				return false;
			}
		});
		for (File file : files) {
			String fileName = file.getAbsolutePath();
			if(file.isDirectory()){
				loadClassFromFile(packageName + "." + file.getName(), fileName, hashSet);
			}else{
				try {
					hashSet.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
