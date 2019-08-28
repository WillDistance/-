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
 * @描述: 百度统计工具类
 * @版权: Copyright (c) 2018
 * @公司: Thinkive
 * @作者: hejun
 * @创建日期: 2018年4月12日 下午6:36:43
 */

public class BaiDuCountUtils {
    static Logger logger = Logger.getLogger(BaiDuCountUtils.class);

    private BaiDuCountUtils() {

    }

    /** 百度登录用户名 **/
    private final static String BAIDU_LOGIN = Configuration.getString("thinkive_sj1_edu_baidu.login");

    /** 百度登录密码 **/
    private final static String BAIDU_PWD = Configuration.getString("thinkive_sj1_edu_baidu.pwd");

    /** 百度接口调用token **/
    private final static String BAIDU_TOKEN = Configuration.getString("thinkive_sj1_edu_baidu.token");

    /** 百度获取站点接口调用url **/
    private final static String GET_SITE_URL = Configuration.getString("thinkive_sj1_edu_baidu.getSiteUrl");

    /** 百度获取对应站点数据接口调用url **/
    private final static String GET_DATA_URL = Configuration.getString("thinkive_sj1_edu_baidu.getDataUrl");

    /** 默认站点ID **/
    private final static String SITEID = Configuration.getString("thinkive_sj1_edu_baidu.siteId");

    /** 默认method **/
    private final static String METHOD = Configuration.getString("thinkive_sj1_edu_baidu.method");

    /** 默认metrics **/
    private final static String METRICS = Configuration.getString("thinkive_sj1_edu_baidu.metrics");

    private static int IS_SUM = 0;

    /** 请求头（固定） **/
    private static HashMap<String, String> _HEADER = new HashMap<String, String>();
    static {
        _HEADER.put("account_type", "1");
        _HEADER.put("username", BAIDU_LOGIN);
        _HEADER.put("password", BAIDU_PWD);
        _HEADER.put("token", BAIDU_TOKEN);
    }

    /** 请求参数 **/
    private static HashMap<String, Object> _BODY = new HashMap<String, Object>();

    public static void main(String[] args) throws Exception {
        getData("2019/05/04", "2019/06/14");
    }

    /**
     * 
     * @描述: 获取当前用户下的站点和子目录列表以及对应参数信息，不包括权限站点和汇总网站
     * @作者: hejun
     * @创建日期: 2018年4月12日 下午6:40:33
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
     * @描述: 根据站点ID获取站点报告数据。
     * @作者: hejun
     * @创建日期: 2018年4月12日 下午7:13:35
     * @return
     * @throws Exception 
     */

    public static List<DataRow> getData() throws Exception {
        return getData(null, null, null, null, null, null, null, null, null);
    }

    /**
     * 
     * @描述: 根据站点ID获取站点报告数据。
     * @作者: hejun
     * @创建日期: 2018年4月12日 下午7:13:35
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @return
     * @throws Exception 
     */
    public static List<DataRow> getData(String startDate, String endDate) throws Exception {
        return getData(startDate, endDate, null, null, null, null, null, null, null);
    }

    /**
     * 
     * @描述: 根据站点ID获取站点报告数据。
     * @作者: hejun
     * @创建日期: 2018年4月12日 下午7:13:35
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @param comStartDate 对比开始时间
     * @param comEndDate 对比结束时间
     * @return
     * @throws Exception 
     */
    public static List<DataRow> getData(String startDate, String endDate, String comStartDate, String comEndDate)
        throws Exception {
        return getData(startDate, endDate, comStartDate, comEndDate, null, null, null, null, null);
    }

    /**
     * 
     * @描述: 根据站点ID获取站点报告数据。
     * @作者: hejun
     * @创建日期: 2018年4月12日 下午7:13:35
     * @param startDate 查询开始时间
     * @param endDate 查询结束时间
     * @param comStartDate 对比开始时间
     * @param comEndDate 对比结束时间
     * @param siteId 站点ID
     * @param method 对应要查询的报告
     * @param metrics 所要获取的指标
     * @param order 排序指标
     * @param sort 排序类型
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
     * @描述: 发送请求
     * @作者: hejun
     * @创建日期: 2018年4月12日 下午6:40:59
     * @param url 请求地址
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
     * @描述: 返回数据JSON转换
     * @作者: hejun
     * @创建日期: 2018年4月13日 下午3:24:10
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
            throw new Exception("请求结果为空");
        }
    }

    /**
     * 
     * @描述:数据解析
     * @作者: hejun
     * @创建日期: 2018年4月13日 下午4:00:24
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
     * @描述:数据解析
     * @作者: hejun
     * @创建日期: 2018年4月13日 下午4:00:24
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
     * @描述: 设置查询结果为 统计
     * @作者: hejun
     * @创建日期: 2018年4月18日 下午2:52:29
     */
    public static void setIsSum() {
        IS_SUM = 1;
    }
}
