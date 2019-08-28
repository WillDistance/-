package com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thinkive.base.jdbc.DBPage;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.JdbcTemplate;

public class test
{
    static int num = 500;//ÿ������ִ��������
    static String source_db = "source_db";//Դ���ݿ�����id
    static String target_db = "target_db";//Ŀ�����ݿ�����id
    static int max_sync_num = 1000;//�������ͬ����¼����С�ڵ�����ͬ��ȫ��
    @SuppressWarnings("unchecked")
    public static void main(String[] args)
    {
        JdbcTemplate template_source_db = new JdbcTemplate(source_db);
        JdbcTemplate template_target_db = new JdbcTemplate(target_db);
        List<DataRow> tables_source_db = template_source_db.query("select table_name from user_tables");
        List<DataRow> tables_target_db = template_target_db.query("select table_name from user_tables");
        /*
        tables = new ArrayList<DataRow>();
        DataRow d = new DataRow();
        d.set("table_name", "T_BIZ_VOICE_BROADCAST");
        tables.add(d);
        DataRow d1 = new DataRow();
        d1.set("table_name", "t_biz_voice_type");
        tables.add(d1);
        */
        System.out.println("/*******һ����Ҫִ��"+tables_source_db.size()+"��Ŀ�����"+tables_target_db.size()+"*******/");
        int count = 1;
        StringBuilder sbCorrect = new StringBuilder();
        StringBuilder sbError = new StringBuilder();
        
        Set<String> table_names = new HashSet<String>();
        try
        {
            for (DataRow dataRow : tables_target_db)
            {
                String table_name = dataRow.getString("table_name");
                table_names.add(table_name);
            }
            for (DataRow dataRow : tables_source_db)
            {
                String table_name = dataRow.getString("table_name");
                if(!table_names.contains(table_name))
                {
                    System.out.println("Ŀ�����ݿ��в����ڸñ�"+table_name);
                    continue;
                }
                System.out.println("/******��ǰִ�б�"+table_name+"*******/");
                long rows = template_source_db.queryMap("select count(1) as count_row  from "+table_name).getLong("count_row");
                if(max_sync_num > 0 && rows > max_sync_num)
                {
                    rows = max_sync_num;
                }
                if(rows>0)
                {
                    int curPage = 1;
                    do
                    {
                        DBPage page = template_source_db.queryPage("select * from "+table_name, curPage, num);
                        List<DataRow> resultList = page.getData();
                        System.out.println("��ѯ����"+table_name+"��"+resultList.size()+"������");
                        int size = resultList.size();
                        if(max_sync_num>0 && curPage*num > max_sync_num)
                        {
                            size =num + max_sync_num -(curPage*num);
                        }
                        Object[][] paramArray = new Object[size][];//�洢ÿ��sql�Ĳ���
                        //�洢sql
                        StringBuilder sqlBuffer = new StringBuilder();
                        //˳��洢�ֶ�
                        List<String> keyList = new ArrayList<String>();
                        
                        for (int j = 0; j < size ; j++)
                        {
                            //��ȡ����
                            DataRow data = resultList.get(j);
                            if(j == 0)
                            {
                                //��һ�Σ���key��������
                                keyList.addAll(data.keySet());
                                keyList.remove("rownum_");
                                Collections.sort(keyList);
                                sqlBuffer.append("insert into " + table_name + "(");
                            }
                            
                            String interrogationStr = "";
                            int i = 0;
                            List valueList = new ArrayList();
                            for (int k=0;k<keyList.size();k++)
                            {
                                i++;
                                String key = keyList.get(k);
                                valueList.add(data.get(key));//˳���Ų���
                                if(j==0)
                                {
                                    if (i < data.size()-1)
                                    {
                                        sqlBuffer.append(key + ",");
                                        interrogationStr += " ?,";
                                    }
                                    else
                                    {
                                        sqlBuffer.append(key);
                                        interrogationStr += "?";
                                    } 
                                }
                            }
                            if(j==0)
                            {
                                sqlBuffer.append(") values (");
                                sqlBuffer.append(interrogationStr);
                                sqlBuffer.append(") ");
                            }
                            paramArray[j] = valueList.toArray();
                        }
                        //����ִ��
                        try
                        {
                            template_target_db.batchUpdate(sqlBuffer.toString(),paramArray);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            sbError.append(table_name+",");
                            break;
                        }
                        
                    }
                    while((curPage++*num)<=rows);
                }
                sbCorrect.append(table_name+",");
                System.out.println("��"+table_name+"ִ����ɣ�ʣ���"+(tables_source_db.size()-count++));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("�Ѿ�����ִ����ɵı�"+sbCorrect.toString());
            System.out.println("�쳣ִ�еı�"+sbError.toString());
        }
        
    }
}
