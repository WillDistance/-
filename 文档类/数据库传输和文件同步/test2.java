package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * 
 * @����: ��ԴĿ���ļ����µ����ݸ��Ƶ�Ŀ���ļ����¡�Ŀ���ļ��в����ڻᱻ����
 * @��Ȩ: Copyright (c) 2019 
 * @��˾: ˼�ϿƼ� 
 * @����: ����
 * @�汾: 1.0 
 * @��������: 2019��3��7�� 
 * @����ʱ��: ����2:44:49
 */
public class test2
{
    static String source_path = "C:"+File.separator+"Users"+File.separator+"Thinkive"+File.separator+"Desktop"+File.separator+"��ʱ�ļ�"+File.separator+"main";//Դ�ļ���·��
    static String target_path = "C:"+File.separator+"Users"+File.separator+"Thinkive"+File.separator+"Desktop"+File.separator+"��ʱ�ļ�"+File.separator+"aaaa";//Ŀ���ļ���·��
    
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
     * @����������һ��Ŀ¼������Ŀ¼���ļ�������һ��Ŀ¼
     * @���ߣ�����
     * @ʱ�䣺2019��3��7�� ����2:46:11
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
            //list()�����Ƿ���ĳ��Ŀ¼�µ������ļ���Ŀ¼���ļ��������ص���String����
            //listFiles()�����Ƿ���ĳ��Ŀ¼�������ļ���Ŀ¼�ľ���·�������ص���File����
            String files[] = source.list();
            for (String file : files)
            {
                File sourceFile = new File(source, file);//����sourceĿ¼�µ��ļ���Ϊfile�ļ�����
                File targetFile = new File(target, file);
                // �ݹ鸴��
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
