package com.spring.boot.tomcat.exchange.rate;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 获取网页内容类</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/3 0003
 */
public class Spider {

    /**
     * GET请求获取网页内容
     *
     * @param url
     * @param charset
     * @return
     */
    public static String pickData(String url, Charset charset) {
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            closeableHttpClient = HttpClients.createDefault();
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String responseEntityStr;
            if (null == charset) {
                responseEntityStr = EntityUtils.toString(httpEntity);
            } else {
                responseEntityStr = EntityUtils.toString(httpEntity, charset);
            }

            return responseEntityStr;
        } catch (IOException e) {
            return null;
        } finally {
            if (closeableHttpResponse != null) {
                try {
                    closeableHttpResponse.close();
                } catch (IOException e) {
                    System.out.println("可关闭HTTP响应关闭异常！");
                }
            }

            if (closeableHttpClient != null) {
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    System.out.println("可关闭HTTP客户端关闭异常！");
                }
            }
        }
    }

    /**
     * POST请求提交表单获取中国银行外汇牌价网页内容
     *
     * @param bocCode
     * @return
     */
    public static String getFromBoc(String bocCode) {
        String url = "http://srh.bankofchina.com/search/whpj/search.jsp";

        HttpPost httpPost = new HttpPost(url);
        //设置请求内容类型为表单
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");

        //设置表单中键值
        List<NameValuePair> pairs = new ArrayList();
        //设置页面中牌价选择下拉框的值
        pairs.add(new BasicNameValuePair("pjname", bocCode));

        //UrlEncodedFormEntity: An entity composed of a list of url-encoded pairs. This is typically useful while sending an HTTP POST request.
        //UrlEncodedFormEntity: 由url编码的键值对列表组成的一个实体。通常用于发送HTTP POST请求。
        //使用指定编码中的参数列表创建一个url编码的表单实体
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs, Charset.forName("UTF-8"));
        //设置HTTP POST请求实体
        httpPost.setEntity(urlEncodedFormEntity);

        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse closeableHttpResponse = null;

        try {
            //创建可关闭的HTTP客户端
            closeableHttpClient = HttpClients.createDefault();
            //HTTP客户端执行HTTP POST请求，返回可关闭的HTTP响应
            closeableHttpResponse = closeableHttpClient.execute(httpPost);
            //从响应中获取响应HTTP实体
            HttpEntity httpEntity = closeableHttpResponse.getEntity();

            String responseEntityStr = EntityUtils.toString(httpEntity);

            return responseEntityStr;
        } catch (Exception e) {
            return null;
        } finally {
            if (closeableHttpResponse != null) {
                try {
                    closeableHttpResponse.close();
                } catch (IOException e) {
                    System.out.println("可关闭HTTP响应关闭异常！");
                }
            }

            if (closeableHttpClient != null) {
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    System.out.println("可关闭HTTP客户端关闭异常！");
                }
            }
        }
    }

    public static void main(String[] args) {
        String responseEntityStr;

//        responseEntityStr = getFromBoc("0");
//        System.out.println("人民币 响应实体字符串：" + responseEntityStr);
//
//        responseEntityStr = getFromBoc("1316");
//        System.out.println("美元 响应实体字符串：" + responseEntityStr);

        //获取汇率网－－中国人民银行人民币汇率中间价网页内容
        responseEntityStr = pickData("https://www.huilv.cc/renminyinhanghuilv.html", Charset.forName("GB2312"));
        System.out.println("中国人民银行人民币汇率中间价网页内容: " + responseEntityStr);
    }
}
