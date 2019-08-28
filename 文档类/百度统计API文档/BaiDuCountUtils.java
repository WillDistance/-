package com.thinkive.sj1.edu.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thinkive.base.config.Configuration;
import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.DateHelper;
import com.thinkive.base.util.StringHelper;

/**
 * 
 * @����: �ٶ�ͳ�ƹ�����
 * @��Ȩ: Copyright (c) 2018
 * @��˾: Thinkive
 * @����: hejun
 * @��������: 2018��4��12�� ����6:36:43
 */

public class BaiDuCountUtils {
    static Logger logger = Logger.getLogger(BaiDuCountUtils.class);

    private BaiDuCountUtils() {

    }

    /** �ٶȵ�¼�û��� **/
    private final static String BAIDU_LOGIN = Configuration.getString("thinkive_sj1_edu_baidu.login");

    /** �ٶȵ�¼���� **/
    private final static String BAIDU_PWD = Configuration.getString("thinkive_sj1_edu_baidu.pwd");

    /** �ٶȽӿڵ���token **/
    private final static String BAIDU_TOKEN = Configuration.getString("thinkive_sj1_edu_baidu.token");

    /** �ٶȻ�ȡվ��ӿڵ���url **/
    private final static String GET_SITE_URL = Configuration.getString("thinkive_sj1_edu_baidu.getSiteUrl");

    /** �ٶȻ�ȡ��Ӧվ�����ݽӿڵ���url **/
    private final static String GET_DATA_URL = Configuration.getString("thinkive_sj1_edu_baidu.getDataUrl");

    /** Ĭ��վ��ID **/
    private final static String SITEID = Configuration.getString("thinkive_sj1_edu_baidu.siteId");

    /** Ĭ��method **/
    private final static String METHOD = Configuration.getString("thinkive_sj1_edu_baidu.method");

    /** Ĭ��metrics **/
    private final static String METRICS = Configuration.getString("thinkive_sj1_edu_baidu.metrics");

    private static int IS_SUM = 0;

    /** ����ͷ���̶��� **/
    private static HashMap<String, String> _HEADER = new HashMap<String, String>();
    static {
        _HEADER.put("account_type", "1");
        _HEADER.put("username", BAIDU_LOGIN);
        _HEADER.put("password", BAIDU_PWD);
        _HEADER.put("token", BAIDU_TOKEN);
    }

    /** ������� **/
    private static HashMap<String, Object> _BODY = new HashMap<String, Object>();

    public static void main(String[] args) throws Exception {
        getData("2019/05/04", "2019/06/14");
    }

    /**
     * 
     * @����: ��ȡ��ǰ�û��µ�վ�����Ŀ¼�б��Լ���Ӧ������Ϣ��������Ȩ��վ��ͻ�����վ
     * @����: hejun
     * @��������: 2018��4��12�� ����6:40:33
     * @return
     * @throws Exception 
     */
    public static List<DataRow> getSiteList() throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("header", _HEADER);
        map.put("body", _BODY);
        String param = JSONObject.toJSONString(map);
        String data = sendPost(GET_SITE_URL, param);
        JSONObject jsonRow = JSONObject.parseObject(JsonToData(data));
        List<DataRow> listData = (List<DataRow>)jsonRow.get("list");
        logger.info(listData);
        return listData;
    }

    /**
     * 
     * @����: ����վ��ID��ȡվ�㱨�����ݡ�
     * @����: hejun
     * @��������: 2018��4��12�� ����7:13:35
     * @return
     * @throws Exception 
     */

    public static List<DataRow> getData() throws Exception {
        return getData(null, null, null, null, null, null, null, null, null);
    }

    /**
     * 
     * @����: ����վ��ID��ȡվ�㱨�����ݡ�
     * @����: hejun
     * @��������: 2018��4��12�� ����7:13:35
     * @param startDate ��ѯ��ʼʱ��
     * @param endDate ��ѯ����ʱ��
     * @return
     * @throws Exception 
     */
    public static List<DataRow> getData(String startDate, String endDate) throws Exception {
        return getData(startDate, endDate, null, null, null, null, null, null, null);
    }

    /**
     * 
     * @����: ����վ��ID��ȡվ�㱨�����ݡ�
     * @����: hejun
     * @��������: 2018��4��12�� ����7:13:35
     * @param startDate ��ѯ��ʼʱ��
     * @param endDate ��ѯ����ʱ��
     * @param comStartDate �Աȿ�ʼʱ��
     * @param comEndDate �ԱȽ���ʱ��
     * @return
     * @throws Exception 
     */
    public static List<DataRow> getData(String startDate, String endDate, String comStartDate, String comEndDate)
        throws Exception {
        return getData(startDate, endDate, comStartDate, comEndDate, null, null, null, null, null);
    }

    /**
     * 
     * @����: ����վ��ID��ȡվ�㱨�����ݡ�
     * @����: hejun
     * @��������: 2018��4��12�� ����7:13:35
     * @param startDate ��ѯ��ʼʱ��
     * @param endDate ��ѯ����ʱ��
     * @param comStartDate �Աȿ�ʼʱ��
     * @param comEndDate �ԱȽ���ʱ��
     * @param siteId վ��ID
     * @param method ��ӦҪ��ѯ�ı���
     * @param metrics ��Ҫ��ȡ��ָ��
     * @param order ����ָ��
     * @param sort ��������
     * @return
     * @throws Exception 
     */
    public static List<DataRow> getData(String startDate, String endDate, String comStartDate, String comEndDate,
        String siteId, String method, String metrics, String order, String sort) throws Exception {
        _BODY.put("site_id", StringHelper.isEmpty(siteId) ? SITEID : siteId);
        _BODY.put("method", StringHelper.isEmpty(method) ? METHOD : method);
        _BODY.put("metrics", StringHelper.isEmpty(metrics) ? METRICS : metrics);
        if (StringHelper.isEmpty(startDate)) {
            startDate = DateHelper.formatDate(new Date(), "yyyyMMdd");
        }
        if (StringHelper.isEmpty(endDate)) {
            endDate = DateHelper.formatDate(new Date(), "yyyyMMdd");
        }
        _BODY.put("start_date", startDate);
        _BODY.put("end_date", endDate);
        _BODY.put("start_date2", comStartDate);
        _BODY.put("end_date2", comEndDate);
        order = StringHelper.isEmpty(order) ? "pv_count" : order;
        sort = StringHelper.isEmpty(sort) ? "desc" : sort;
        _BODY.put("order", order + "," + sort);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("header", _HEADER);
        map.put("body", _BODY);
        String param = JSONObject.toJSONString(map);
        String data = sendPost(GET_DATA_URL, param);
        JSONObject jsonRow = JSONObject.parseObject(JsonToData(data));
        jsonRow = (JSONObject)jsonRow.get("result");
        if (IS_SUM == 0) {
            return DataFormate(jsonRow);
        } else {
            return DataFormateSum(jsonRow);
        }
    }

    /**
     * 
     * @����: ��������
     * @����: hejun
     * @��������: 2018��4��12�� ����6:40:59
     * @param url �����ַ
     * @param data
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, String data) throws IOException {
        String response = null;
        logger.info("url: " + url);
        logger.info("request: " + data);
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data, ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils.toString(httpresponse.getEntity());
                logger.info("response: " + response);
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 
     * @����: ��������JSONת��
     * @����: hejun
     * @��������: 2018��4��13�� ����3:24:10
     * @param data
     * @return
     * @throws Exception
     */
    public static String JsonToData(String data) throws Exception {
        try {
            JSONObject result = JSONObject.parseObject(data);
            result = (JSONObject)result.get("body");
            String liString = result.get("data").toString();
            liString = liString.substring(1, liString.length() - 1);
            logger.info(liString);
            return liString;
        } catch (Exception e) {
            throw new Exception("������Ϊ��");
        }
    }

    /**
     * 
     * @����:���ݽ���
     * @����: hejun
     * @��������: 2018��4��13�� ����4:00:24
     * @param jsonRow
     * @return
     */
    public static List<DataRow> DataFormate(JSONObject jsonRow) {
        List<DataRow> results = new ArrayList<DataRow>();
        List<DataRow> time = (List<DataRow>)jsonRow.get("timeSpan");
        int timeSize = time.size();
        List<DataRow> itemsList = (List<DataRow>)jsonRow.get("items");
        List<String> fieldsList = (List<String>)jsonRow.get("fields");
        List typelist = (List)itemsList.get(0);
        List ConverList = (List)itemsList.get(3);
        logger.info("typelist:------" + typelist);
        logger.info("ConverList:------" + ConverList);
        for (int n = 1; n <= timeSize; n++) {
            for (int i = 0; i < typelist.size(); i++) {
                DataRow dataRow = new DataRow();
                JSONArray jsonArray = (JSONArray)typelist.get(i);
                JSONObject data = (JSONObject)jsonArray.get(0);
                dataRow.set("name", data.getString("name"));
                dataRow.set("source", data.getString("source"));
                List timeDtalist = (List)itemsList.get(n);
                for (int k = 1; k < fieldsList.size(); k++) {
                    JSONArray timeData = (JSONArray)timeDtalist.get(i);
                    dataRow.set(fieldsList.get(k), timeData.get(k - 1));
                }
                if (ConverList.size() > 0) {
                    for (int k = 2; k < fieldsList.size(); k++) {
                        JSONArray converData = (JSONArray)ConverList.get(i);
                        dataRow.set(fieldsList.get(k) + "_conver", converData.get(k - 2));
                    }
                    logger.info("dataRow:------" + dataRow);
                } else {
                    dataRow.set("time", time.get(n - 1));
                }
                results.add(dataRow);
            }
        }
        logger.info("results:------" + results);
        return results;
    }

    /**
     * 
     * @����:���ݽ���
     * @����: hejun
     * @��������: 2018��4��13�� ����4:00:24
     * @param jsonRow
     * @return
     */
    public static List<DataRow> DataFormateSum(JSONObject jsonRow) {
        List<DataRow> results = new ArrayList<DataRow>();
        List<DataRow> time = (List<DataRow>)jsonRow.get("timeSpan");
        int timeSize = time.size();
        List<DataRow> itemsList = (List<DataRow>)jsonRow.get("pageSum");
        List<String> fieldsList = (List<String>)jsonRow.get("fields");
        List ConverList = (List)itemsList.get(2);
        logger.info("ConverList:------" + ConverList);
        for (int n = 1; n <= timeSize; n++) {
            DataRow dataRow = new DataRow();
            List timeDtalist = (List)itemsList.get(n - 1);
            for (int k = 1; k < fieldsList.size(); k++) {
                JSONArray timeData = (JSONArray)timeDtalist;
                dataRow.set(fieldsList.get(k), timeData.get(k - 1));
            }
            if (ConverList.size() > 0) {
                for (int k = 2; k < fieldsList.size(); k++) {
                    JSONArray converData = (JSONArray)ConverList;
                    dataRow.set(fieldsList.get(k) + "_conver", converData.get(k - 2));
                }
                logger.info("dataRow:------" + dataRow);
            } else {
                dataRow.set("time", time.get(n - 1));
            }
            results.add(dataRow);
        }
        logger.info("results:------" + results);
        return results;
    }

    /**
     * 
     * @����: ���ò�ѯ���Ϊ ͳ��
     * @����: hejun
     * @��������: 2018��4��18�� ����2:52:29
     */
    public static void setIsSum() {
        IS_SUM = 1;
    }
}
