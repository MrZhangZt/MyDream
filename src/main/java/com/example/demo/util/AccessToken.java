package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Ticket;
import com.example.demo.model.Token;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.UUID;

@PropertySource("classpath:wxapplication.properties")
@Component
public class AccessToken {
    @Autowired
    private RedisUtil redisUtil;
    public  Token getAccessToken()  throws ClientProtocolException,IOException{
        ResourceBundle bundle = ResourceBundle.getBundle("wxapplication");
        Token token=new Token();
        String qiyeId=bundle.getString("qiyeId");
        String qiyesecret=bundle.getString("qiyesecret");
        String url=bundle.getString("getToken").replace("qiyeId",qiyeId).replace("qiyesecret",qiyesecret);
        JSONObject jsonObject=doGetStr(url);
        if(jsonObject!=null){
            token.setAccessToken(jsonObject.getString("access_token"));
            redisUtil.set("token",token,7200);
        }
        return token;
    }

    public  JSONObject doGetStr(String url) throws ClientProtocolException, IOException{
        CloseableHttpClient client= HttpClientBuilder.create().build();
        HttpGet httpGet=new HttpGet(url);
        JSONObject jsonObject=null;
        CloseableHttpResponse response = client.execute(httpGet);
        HttpEntity entity=response.getEntity();
        if(entity!=null){
            String result= EntityUtils.toString(entity,"UTF-8");
            jsonObject=JSONObject.parseObject(result);
        }
        httpGet.releaseConnection();
        return jsonObject;
    }
    public  String getTicket(String access_token) throws  IOException{
        ResourceBundle bundle = ResourceBundle.getBundle("wxapplication");
        String ticket="";
        String url=bundle.getString("getTicketUrl").replace("ACCESS_TOKEN",access_token);
        JSONObject jsonObject=doGetStr(url);
        if(jsonObject!=null){
            ticket=jsonObject.getString("ticket");
           /* ticket.setTicket(jsonObject.getString("ticket"));
            String nocestr= UUID.randomUUID().toString().replace("-","");
            String timestamp = createTimestamp();
            String dangqianyeurl= bundle.getString("dangqianyeUrl") ;
            String appId=bundle.getString("qiyeId");
            ticket.setNocestr(nocestr);
            ticket.setTimestamp(Integer.parseInt(timestamp));
            ticket.setUrl(dangqianyeurl);
            String signature=getUrl(ticket);
            ticket.setSignature(signature);
            ticket.setAppId(appId);*/
            redisUtil.set("ticket",ticket,7200);
        }
        return ticket;
    }
    public  Ticket getUrl(String tickets) throws IOException{
        Ticket ticket=new Ticket();
        ResourceBundle bundle = ResourceBundle.getBundle("wxapplication");
        String signature="";
        String string1="jsapi_ticket="+ticket.getTicket()
                +"&noncestr="+ticket.getNocestr()
                +"&timestamp="+ticket.getTimestamp()
                +"&url="+ ticket.getUrl();
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(string1.toString().getBytes());
            signature=byteToStr(digest);
            ticket.setTicket(tickets);
            String nocestr= UUID.randomUUID().toString().replace("-","");
            String timestamp = createTimestamp();
            String dangqianyeurl= bundle.getString("dangqianyeUrl") ;
            String appId=bundle.getString("qiyeId");
            ticket.setNocestr(nocestr);
            ticket.setTimestamp(Integer.parseInt(timestamp));
            ticket.setUrl(dangqianyeurl);
            ticket.setSignature(signature);
            ticket.setAppId(appId);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ticket;
    }
    public  String byteToStr(byte[] byteArray){
        String strDigest="";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest+=byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }
    public static String byteToHexStr(byte mByte){
        char[] Digit={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] tempArr = new char[2];
        tempArr[0]=Digit[(mByte>>>4)&0X0F];
        tempArr[1]=Digit[mByte&0X0F];
        String s=new String(tempArr);
        return s;
    }
    //生成时间戳
    private  String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
