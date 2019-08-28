package com;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
 
 
public class WatchFile {
 
    private static String listenfilePath = "C:\\Users\\Thinkive\\Desktop\\临时文件";//需要监听的文件路径
    
    private static long sleepTime = 5000L;
    
    private static Map<String,Long> fileObjMap = new HashMap<String,Long>(); //存放文件最后修改时间
    
    private static Set<File> changeFile = new HashSet<File>();//存放本次扫描发生改变(变化或者新增)的文件 
    
    private static Set<String> deleteFile = new HashSet<String>();//存放删除了的文件
    
    private static Set<String> record = new HashSet<String>();//存放每次检查的文件path
    
    private static boolean flag = false;
    public static void watch() {
        File listenfile = new File(listenfilePath);
        if(listenfile.exists()&&listenfile.listFiles().length>0)
        {
            while(true)
            {
                record.clear();
                
                getAllFileInfo(listenfile.listFiles());
                deleteFile.addAll(fileObjMap.keySet());
                deleteFile.removeAll(record);
                if(deleteFile.size()>0)
                {
                    System.out.println("发生变化删除"+deleteFile.toString());
                    flag = true;
                    deleteFile.clear();
                }
                
                if(changeFile.size()>0)
                {
                    System.out.println("发生变化"+changeFile.toString());
                    flag = true;
                    changeFile.clear();
                }
                
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            
        }
        
    }
    
    private static void getAllFileInfo(File[] fileArr)
    {
        if (null == fileArr || fileArr.length <= 0)//文件对象不存在
        {
            return;
        }
        for (File file : fileArr)
        {
            String filePath = file.getPath();
            long lastModified = file.lastModified();
            record.add(filePath);
            if((fileObjMap.get(filePath) == null?0:fileObjMap.get(filePath)) - lastModified != 0)
            {
                //第一次初始化或文件发生变化
                changeFile.add(file);
            }
            fileObjMap.put(filePath, file.lastModified());
            if (file.isDirectory())
            {
                getAllFileInfo(file.listFiles());
            }
        }
    }
    
    /**
     * 模拟测试
     */
    public static void main(String[] args) throws Exception {
        watch();
        /*fileObjMap.put("a", 1L);
        fileObjMap.put("b", 2L);
        fileObjMap.put("c", 3L);
        System.out.println(fileObjMap.size());
        //deleteFile.addAll(fileObjMap.keySet());
        deleteFile=fileObjMap.keySet();
        deleteFile.clear();
        deleteFile.add("a");
        System.out.println(fileObjMap.size());*/
        
    }
 
}