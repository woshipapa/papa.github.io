package com.papa.springMVC.fileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileUtils {

    private static List<String> fileList=new ArrayList<>();
    public static List<String> getFileList(File f){
        setFileList(f);
        return fileList;
    }
    public static void setFileList(File f){
        if(f.isDirectory()){
            for(File ff:f.listFiles()){
                setFileList(ff);
            }
        }else{
            String absolutePath=f.getAbsolutePath();
            String classFullPath=absolutePath.substring(absolutePath.indexOf("com"),absolutePath.lastIndexOf(".class"));
            classFullPath=classFullPath.replaceAll("\\\\","\\.");
            fileList.add(classFullPath);
        }
    }
}
