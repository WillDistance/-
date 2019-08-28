package com;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
 
 
public class WatchFile {
 
    private static String listenfilePath = "C:\\Users\\Thinkive\\Desktop\\��ʱ�ļ�";//��Ҫ�������ļ�·��
    
    private static long sleepTime = 5000L;
    
    private static Map<String,Long> fileObjMap = new HashMap<String,Long>(); //����ļ�����޸�ʱ��
    
    private static Set<File> changeFile = new HashSet<File>();//��ű���ɨ�跢���ı�(�仯��������)���ļ� 
    
    private static Set<String> deleteFile = new HashSet<String>();//���ɾ���˵��ļ�
    
    private static Set<String> record = new HashSet<String>();//���ÿ�μ����ļ�path
    
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
                    System.out.println("�����仯ɾ��"+deleteFile.toString());
                    flag = true;
                    deleteFile.clear();
                }
                
                if(changeFile.size()>0)
                {
                    System.out.println("�����仯"+changeFile.toString());
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
        if (null == fileArr || fileArr.length <= 0)//�ļ����󲻴���
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
                //��һ�γ�ʼ�����ļ������仯
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
     * ģ�����
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