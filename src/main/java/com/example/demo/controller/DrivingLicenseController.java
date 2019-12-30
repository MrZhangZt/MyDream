package com.example.demo.controller;

import com.example.demo.util.AISDK;
import com.example.demo.util.Base64Util;
import com.example.demo.util.HttpUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class DrivingLicenseController {
    @RequestMapping(value = "/driver",method = RequestMethod.GET)
    public String drivingLicense(){
        AISDK aisdk=new AISDK();
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id","2125636236");
        params.put("app_key","xjVevquA7DFNwW1R");
        String time_stamp=String.valueOf(aisdk.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream("C:\\Users\\Administrator\\Desktop\\demo2\\src\\main\\resources\\templates\\img\\driver1.PNG");
           // in = new FileInputStream("C:\\Users\\Administrator\\Desktop\\demo2\\src\\main\\resources\\templates\\img\\driver.PNG");//行驶证
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64Util b64 = new Base64Util();
        String b64str = b64.encode(data);
        System.out.println(b64str);
        params.put("type","1");
       // params.put("type","0");//识别类型，0-行驶证识别，1-驾驶证识别
        params.put("image", b64str);
        params.put("sign", aisdk.genSignString(params));


        // 打开和URL之间的连接
        try {
            HttpUtils util = new HttpUtils();
            result= util.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_driverlicenseocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
