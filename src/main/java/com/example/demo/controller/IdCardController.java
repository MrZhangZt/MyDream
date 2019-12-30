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
public class IdCardController {
    @RequestMapping(value = "/idCard",method = RequestMethod.GET)
    public String idcard(){
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
        /*Base64ImgUtil imgutl = new Base64ImgUtil();
        String base64 = Base64ImgUtil.GetImageStr("C:\\Users\\Administrator\\Desktop\\shg.png");
*/
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            //in = new FileInputStream("C:\Users\Administrator\Desktop\demo2\src\main\resources\templates\img\shg.png");
            in = new FileInputStream("C:\\Users\\Administrator\\Desktop\\demo2\\src\\main\\resources\\templates\\img\\fanmian.PNG");
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Base64Util b64 = new Base64Util();
        String b64str = b64.encode(data);
        System.out.println(b64str);
//        params.put("card_type","0");
        params.put("card_type","1");
        params.put("image", b64str);
        params.put("sign", aisdk.genSignString(params));


        // 打开和URL之间的连接
        try {
            HttpUtils util = new HttpUtils();
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_idcardocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
