package com.example.demo.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class HttpUtils {
    //因为请求链接里需要一些特殊字符来拼接参数，这里将它们定义成变量，方便以后修改
    private static final String URL_PARAM_CONNECT_FLAG = "&";
    private static final String EMPTY = "";

    //声明一个多线程安全连接管理类变量，关于该类的简单介绍  https://blog.csdn.net/fairytall/article/details/7938692
//使用这个对象简单来说就是为了不去考虑多线程带来安全的问题
    private static MultiThreadedHttpConnectionManager connectionManager = null;
    //将参数提取成变量，方便以后修改
    private static int connectionTimeOut = 25000;

    private static int socketTimeOut = 25000;

    private static int maxConnectionPerHost = 20;

    private static int maxTotalConnections = 20;
    //声明client变量，用于执行请求的
    private static HttpClient client;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static Map<String, Object> URLPost(String url, Map<String, String> params, String enc) {
        //创建一个Map集合，用于存储返回结果
        Map<String, Object> result = new HashMap<>();
        String response = EMPTY;
        //创建一个请求方式对象
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //将请求参数放入postMethod中
            if (params != null) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String value = params.get(key);

                    postMethod.addParameter(key, value);
                }
            }
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {                //HttpStatus.SC_OK就是200，服务器正确处理了请求
                response = postMethod.getResponseBodyAsString(); //返回字符串格式的结果
                result.put("status", 200);                        //封装状态信息和结果
                result.put("data", response);
            } else {
                result.put("status", postMethod.getStatusCode());
                result.put("data", null);
            }
        } catch (HttpException e) {
            //log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            //log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {                           //这里一定要手动释放掉对象
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return result;
    }

    //这个方法与上面相同，只不过请求参数是json格式的
    public static Map<String, Object> URLPost(String url, String json,String contentType, String enc) {

        Map<String, Object> result = new HashMap<>();
        String response = EMPTY;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            RequestEntity se = new StringRequestEntity(json, contentType, enc);
            postMethod.setRequestEntity(se);
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = postMethod.getResponseBodyAsString();
                result.put("status", 200);
                result.put("data", response);
            } else {
                result.put("status", postMethod.getStatusCode());
                result.put("data", null);
            }
        } catch (HttpException e) {
            // log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            // log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return result;
    }

    /**
     * GET方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String URLGet(String url, Map<String, String> params, String enc) {

        String response = EMPTY;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(EMPTY);

        if (strtTotalURL.indexOf("?") == -1) {          //返回结果是-1代表没有找到该字符串，那么就在url后面拼接一个问号
            strtTotalURL.append(url).append("?").append(getUrl(params, enc));
        } else {
            strtTotalURL.append(url).append("&").append(getUrl(params, enc));
        }

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = getMethod.getResponseBodyAsString();
            } else {
                response = "";
            }
        } catch (HttpException e) {
            // log.error("发生致命的异常，可能是协议不对或者返回的内容有问题", e);
            e.printStackTrace();
        } catch (IOException e) {
            // log.error("发生网络异常", e);
            e.printStackTrace();
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * 据Map生成URL字符串
     *
     * @param map      Map
     * @param valueEnc URL编码
     * @return URL
     */
    private static String getUrl(Map<String, String> map, String valueEnc) {

        if (null == map || map.keySet().size() == 0) {
            return (EMPTY);
        }
        StringBuffer url = new StringBuffer();
        Set<String> keys = map.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            if (map.containsKey(key)) {
                String val = map.get(key).toString();
                String str = val != null ? val : EMPTY;
                try {
                    str = URLEncoder.encode(str, valueEnc);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
            }
        }
        System.out.println("url++++++++++++++"+url);
        String strURL = EMPTY;
        strURL = url.toString();
//这是为了保证  “&”符号后面一定有参数，如果“&”后面没有东西就把“&”去掉
        if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1))) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }

        return (strURL);
    }


    public static String httpPostWithForm(String url, Map<String, String> paramsMap) {
        // 用于接收返回的结果
        String resultData = "";
        try {
            HttpPost post = new HttpPost(url);
            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            // 迭代Map-->取出key,value放到BasicNameValuePair对象中-->添加到list中
            for (String key : paramsMap.keySet()) {
                pairList.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }
            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairList, "utf-8");
            post.setEntity(uefe);
            // 创建一个http客户端
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            // 发送post请求
            HttpResponse response = httpClient.execute(post);

            // 状态码为：200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回数据：
                resultData = EntityUtils.toString(response.getEntity(), "UTF-8");
            } else {
                throw new RuntimeException("接口连接失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("接口连接失败！");
        }
        return resultData;

    }
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    }
