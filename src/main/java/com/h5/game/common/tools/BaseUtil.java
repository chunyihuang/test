package com.h5.game.common.tools;

import org.apache.commons.collections.FastHashMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本方法 <br>
 * create on 2016/8/9 <br>
 * <ul>
 *     <li>isEmpty  判断字符串是否为null或者空字符串方法</li>
 *     <li>isNum    判断是否为数字的方法</li>
 *     <li>copy     把两个相同对象的属性合并,可以设置过滤</li>
 *     <li>parseStringToDate    格式化字符串为Date类型对象</li>
 * </ul>
 * Created by 黄春怡 on 2017/4/1.
 */
public class BaseUtil {

    private static final Logger logger = Logger.getLogger(BaseUtil.class);

    public static String[] parsePatterns = {

            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH",

            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH",

            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH"
    };

    /**
     * 判断字符串是否NULL 或者 为空字符串
     * @param str 需要判断的字符串
     * @return <code>true</code> 为NUll 或者空字符串 <br> <code>false</code> 存在内容
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() <= 0;
    }

    /**
     * 测试字符串是否可以转换为数字
     * @param str 需要转换的字符串
     * @return true 可以 false 不可以
     */
    public static boolean isNum(String str){
        try{
            Integer.valueOf(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private static FastHashMap propertyDescriptorCache = new FastHashMap();

    private static PropertyDescriptor[] getPropertyDescriptor(Class<?> beanClass){
        String beanName = beanClass.getName();
        if(propertyDescriptorCache.containsKey(beanName)){
            return (PropertyDescriptor[]) propertyDescriptorCache.get(beanName);
        }else{
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                propertyDescriptorCache.put(beanName,propertyDescriptors);
                return propertyDescriptors;
            } catch (IntrospectionException e) {
                e.printStackTrace();
                return new PropertyDescriptor[0];
            }
        }
    }
    /**
     * 把source对象的属性复制到tag对象中
     * @param source 属性来源
     * @param tag    目标对象
     * @param filters 需要过滤的属性数组
     * @param filterClass 需要过滤的类型,该类型的所有子类型都会被过滤
     * @param <T>
     */
    public static <T> void copy(T source, T tag,String[] filters,Class<?> filterClass) {
        copy(source,tag,filters,filterClass,true);
    }

    public static <T> void copy(T source, T tag,String[] filters,Class<?> filterClass,boolean filterNull){
        Class<?> sClass = source.getClass();
        ArrayList<String> filterList = new ArrayList<String>();
        if(filters != null)
            Collections.addAll(filterList, filters);
        PropertyDescriptor[] pds = getPropertyDescriptor(sClass);
        for (PropertyDescriptor pd : pds) {
            if ("class".equals(pd.getName())) continue;
            try{
                if(pd.getReadMethod() == null)
                    throw new NoSuchMethodException("Class :" + tag.getClass().getName() + " Field :" + pd.getName() + " get or set method is not found");
                if(pd.getWriteMethod() == null)
                    throw new NoSuchMethodException("Class :" + tag.getClass().getName() + " Field :" + pd.getName() + " set method is not found");
            } catch (NoSuchMethodException e) {
                logger.warn(e.getMessage());
                continue;
            }
            if(filterList.indexOf(pd.getName()) > -1){
                continue;
            }
            if(filterClass != null){
                if(filterClass.isAssignableFrom(pd.getPropertyType())){ //判断filterClass是不是sField类型的父类
                    continue;
                }
            }
            copyValue(source,tag,pd,filterNull);
        }
    }


    private static <T>  void copyValue(T source,T tag,PropertyDescriptor propertyDescriptor,boolean filterNull){
        try {
            Object result = propertyDescriptor.getReadMethod().invoke(source);
            if (filterNull && result == null)
                return;       //如果为null则继续循环
            propertyDescriptor.getWriteMethod().invoke(tag,result);
        } catch (InvocationTargetException e) {
            //
        } catch (IllegalAccessException e) {
            //
        }
    }


    public static String toUpperCaseFirstOne(String str){
        return str.toUpperCase().charAt(0) + str.substring(1);
    }

    public static String toLowerCaseFirstOne(String str){
        return str.toLowerCase().charAt(0) + str.substring(1);
    }
    /**
     * 将字符串转换为Date类型对象
     * @param sDate 需要转换的字符串
     * @return 返回格式化后得到的Date对象
     * @exception ParseException 格式化错误时候会抛出异常;
     */
    public static Date parseStringToDate(String sDate) throws ParseException {
        return DateUtils.parseDate(sDate,parsePatterns);
    }

    public static String buildRandom(int count){
        int base = 9;
        int move = 1;
        for(int i = 1; i < count; i++){
            base = base * 10;
            move = move * 10;
        }
        return new Random().nextInt(base - 1) + move + "";
    }

    public static Set<String> getClassName(String packageName, boolean isRecursion) {
        Set<String> classNames = new HashSet<String>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");

        URL url = loader.getResource(packagePath);
        if (url != null) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
            } else if (protocol.equals("jar")) {
                JarFile jarFile = null;
                try{
                    jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                } catch(Exception e){
                    e.printStackTrace();
                }

                if(jarFile != null){
                    classNames = getClassNameFromJar(jarFile.entries(), packageName, isRecursion);
                }
            }
        } else {
			/*从所有的jar包中查找包名*/
            classNames = getClassNameFromJars(((URLClassLoader)loader).getURLs(), packageName, isRecursion);
        }

        return classNames;
    }

    /**
     * 从项目文件获取某包下所有类
     * @param filePath 文件路径
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
        Set<String> className = new HashSet<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                if (isRecursion) {
                    className.addAll(getClassNameFromDir(childFile.getPath(), packageName+"."+childFile.getName(), isRecursion));
                }
            } else {
                String fileName = childFile.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    className.add(packageName+ "." + fileName.replace(".class", ""));
                }
            }
        }

        return className;
    }


    /**
     * @param jarEntries
     * @param packageName
     * @param isRecursion
     * @return
     */
    private static Set<String> getClassNameFromJar(Enumeration<JarEntry> jarEntries, String packageName, boolean isRecursion){
        Set<String> classNames = new HashSet<String>();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            if(!jarEntry.isDirectory()){
                String entryName = jarEntry.getName().replace("/", ".");
                if (entryName.endsWith(".class") && !entryName.contains("$") && entryName.startsWith(packageName)) {
                    entryName = entryName.replace(".class", "");
                    if(isRecursion){
                        classNames.add(entryName);
                    } else if(!entryName.replace(packageName+".", "").contains(".")){
                        classNames.add(entryName);
                    }
                }
            }
        }

        return classNames;
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     * @param urls URL集合
     * @param packageName 包路径
     * @param isRecursion 是否遍历子包
     * @return 类的完整名称
     */
    private static Set<String> getClassNameFromJars(URL[] urls, String packageName, boolean isRecursion) {
        Set<String> classNames = new HashSet<String>();

        for (int i = 0; i < urls.length; i++) {
            String classPath = urls[i].getPath();

            //不必搜索classes文件夹
            if (classPath.endsWith("classes/")) {continue;}

            JarFile jarFile = null;
            try {
                jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (jarFile != null) {
                classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion));
            }
        }

        return classNames;
    }


    public static boolean doRegexMatch(String regex,String string){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    private static final String sourceStr = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    public static String getRandomToken(int count){
        int sourceLength = sourceStr.length();
        StringBuilder generateRandStr = new StringBuilder();
        Random rand = new Random();
        for(int i=0;i<count;i++)
        {
            int randNum = rand.nextInt(sourceLength);
            generateRandStr.append(sourceStr.substring(randNum,randNum+1));
        }
        return generateRandStr.toString();
    }
}
