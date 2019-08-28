package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * 
 * @描述: 将源目标文件夹下的内容复制到目标文件夹下。目标文件夹不存在会被创建
 * @版权: Copyright (c) 2019 
 * @公司: 思迪科技 
 * @作者: 严磊
 * @版本: 1.0 
 * @创建日期: 2019年3月7日 
 * @创建时间: 下午2:44:49
 */
public class test2
{
    static String source_path = "C:"+File.separator+"Users"+File.separator+"Thinkive"+File.separator+"Desktop"+File.separator+"临时文件"+File.separator+"main";//源文件夹路径
    static String target_path = "C:"+File.separator+"Users"+File.separator+"Thinkive"+File.separator+"Desktop"+File.separator+"临时文件"+File.separator+"aaaa";//目标文件夹路径
    
    public static void main(String[] args) throws Exception
    {
        File source = new File(source_path);
        System.out.println(source.lastModified());
        System.out.println(source.getPath());
        File target = new File(target_path);
        copyFolder(source,target);
    }
    /**
     * 
     * @描述：复制一个目录及其子目录、文件到另外一个目录
     * @作者：严磊
     * @时间：2019年3月7日 下午2:46:11
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyFolder(File source, File target) throws Exception
    {
        if ( source.isDirectory() )
        {
            if ( !target.exists() )
            {
                target.mkdir();
            }
            //list()方法是返回某个目录下的所有文件和目录的文件名，返回的是String数组
            //listFiles()方法是返回某个目录下所有文件和目录的绝对路径，返回的是File数组
            String files[] = source.list();
            for (String file : files)
            {
                File sourceFile = new File(source, file);//建立source目录下的文件名为file文件对象
                File targetFile = new File(target, file);
                // 递归复制
                copyFolder(sourceFile, targetFile);
            }
        }
        else
        {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
}
