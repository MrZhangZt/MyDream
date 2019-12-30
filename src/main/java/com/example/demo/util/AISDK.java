package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

public class AISDK {
    public static  String genSignString(Map<String,String> str){
        String uri_Str="";
        Map<String,String> map= new TreeMap<>();
        System.out.println(str);
        map.putAll(str);
        Set<String> keySet=map.keySet();
        Iterator<String> iterator=keySet.iterator();
        while(iterator.hasNext()){
            String key=iterator.next();
            if(key.equalsIgnoreCase("app_key"))
                continue;
            String encode="";
            try {
                String value=str.get(key);
                encode= URLEncoder.encode(value,"UTF-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            uri_Str=uri_Str+key+"="+encode+"&";
            System.out.println(uri_Str);
        }
        String sign_str=uri_Str+"app_key="+str.get("app_key");
        System.out.println(sign_str);
        sign_str=conVertTextToMD5(sign_str).toUpperCase();
        return sign_str;
    }

    public static String conVertTextToMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static Integer DateToTimestamp(Date time) {
        Timestamp ts = new Timestamp(time.getTime());
        return (int) ((ts.getTime()) / 1000);
    }
}
