package com.papa.springMVC.context;

import com.papa.springMVC.XML.XMLParser;
import com.papa.springMVC.annotation.Autowired;
import com.papa.springMVC.annotation.Controller;
import com.papa.springMVC.annotation.Service;
import com.papa.springMVC.fileUtils.fileUtils;
import java.lang.reflect.Field;
import org.dom4j.DocumentException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class papaApplicationContext {
    private  static papaApplicationContext ioc=null;
    public papaApplicationContext(){}
    public static papaApplicationContext getInstance(){
        if(ioc==null){
            ioc=new papaApplicationContext();
        }
        return ioc;

    }

    //存放扫描包下的所有类的全路径
    private List<String> classFullPathList=new ArrayList<>();

    public ConcurrentMap<String, Object> singletonObjects=new ConcurrentHashMap<>();
    public void init(String xmlPath){
        try {
            xmlPath=xmlPath.split(":")[1];
            String pack=XMLParser.getBasePackage(xmlPath);//可能得到多个要扫描的包，并且用,隔开
            String [] packs=pack.split(",");
            for(String s:packs){
                scanPackage(s);
            }
            executeInstance();
            //将配置文件中的bean进行注入ioc
            Map<String,Object> map=XMLParser.scanAllBean(xmlPath);
            singletonObjects.putAll(map);
            executeAutowired();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void scanPackage(String pack){
        String path="/"+pack.replaceAll("\\.","/");
        URL url=this.getClass().getClassLoader().getResource(path);
        System.out.println("url = "+url);
        File f=new File(url.getFile());
        classFullPathList= fileUtils.getFileList(f);
        for(String s:classFullPathList){
            System.out.println(s);
        }
    }
    public void executeInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (String classFullPath:classFullPathList){
            Class<?> clazz = Class.forName(classFullPath);

            if(clazz.isAnnotationPresent(Controller.class)){
                String beanId=null;
                Controller annotation = clazz.getAnnotation(Controller.class);
                beanId=annotation.value();
                if(beanId.equals("")){
                    //默认使用类名第一个字母小写
                    String className=clazz.getSimpleName();
                    beanId=className.substring(0,1).toLowerCase()+className.substring(1);
                }
                singletonObjects.put(beanId,clazz.newInstance());
            }else if(clazz.isAnnotationPresent(Service.class)){
                String beanId=clazz.getAnnotation(Service.class).value();
                Object obj=clazz.newInstance();
                if(beanId.equals("")){
                    //使用类名/接口名来作为beanUd
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for(Class cla:interfaces){
                        String interfaceName=cla.getSimpleName();
                        singletonObjects.put(interfaceName.substring(0,1).toLowerCase()+interfaceName.substring(1),obj);
                    }
                    String className=clazz.getSimpleName();
                    singletonObjects.put(className.substring(0,1).toLowerCase()+className.substring(1),obj);
                }else{
                    singletonObjects.put(beanId,obj);
                }

            }
        }
    }
    public void executeAutowired() throws IllegalAccessException {
        if(singletonObjects.isEmpty()) return;
        for(Map.Entry<String,Object> entry:singletonObjects.entrySet()){
            Object obj=entry.getValue();
            Class clazz=obj.getClass();
            //扫描Field域，看有无@Autowired注解
            Field[] fields=clazz.getDeclaredFields();
            for(Field f:fields){
                if(f.isAnnotationPresent(Autowired.class)){
                    Autowired annotation=f.getAnnotation(Autowired.class);
                    String beanId=annotation.value();
                    if(beanId.equals("")){
                        String typeName=f.getType().getSimpleName();
                        beanId=typeName.substring(0,1).toLowerCase()+typeName.substring(1);
                    }
                    //获取要装配的对象
                    Object other_obj=singletonObjects.get(beanId);
                    if(other_obj==null) throw new RuntimeException("需要装配的bean不在容器中存在");
                    f.setAccessible(true);
                    f.set(obj,other_obj);
                }
            }
        }

    }
}
