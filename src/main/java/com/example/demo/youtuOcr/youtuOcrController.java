package com.example.demo.youtuOcr;

import com.example.demo.util.AISDK;
import com.example.demo.util.Base64Util;
import com.example.demo.util.HttpUtils;
import com.example.demo.youtuOcrModel.IdCardOcrObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ocr")
public class youtuOcrController {
    private String appId="2125636236";
    private String appKey="xjVevquA7DFNwW1R";

    //身份证OCR
    @RequestMapping(value = "/cardOcr",method = RequestMethod.POST)
    public String idcard(@RequestBody IdCardOcrObject idCardOcrObject) throws  IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        String data= Base64Util.encode(idCardOcrObject.getFile().getBytes());
        params.put("card_type",idCardOcrObject.getCard_type());
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_idcardocr",params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //车牌OCR
    @RequestMapping(value = "/plateOcr",method = RequestMethod.POST)
    public String plate(@RequestParam("file")MultipartFile file) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);

        String data = Base64Util.encode(file.getBytes());

        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_plateocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //驾驶证、行驶证OCR
    @RequestMapping(value = "/driverOcr",method = RequestMethod.POST)
    public String drivingLicense(@RequestParam("file") MultipartFile file,@RequestParam("type") String type) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);

        String data = Base64Util.encode(file.getBytes());
        params.put("type",type);
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_driverlicenseocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //通用OCR
    @RequestMapping(value = "/currencyOcr",method = RequestMethod.POST)
    public String currency(@RequestParam("file") MultipartFile file) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        String data = Base64Util.encode(file.getBytes());
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_generalocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    //营业执照OCR
    @RequestMapping(value = "/businessLicenseOcr",method = RequestMethod.POST)
    public String businessLicense(@RequestParam("file") MultipartFile file) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        String data = Base64Util.encode(file.getBytes());
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_bizlicenseocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //银行卡OCR
    @RequestMapping(value = "/bankCardOcr",method = RequestMethod.POST)
    public String bankCard(@RequestParam("file") MultipartFile file) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        String data = Base64Util.encode(file.getBytes());
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_creditcardocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //手写体OCR
    @RequestMapping(value = "/handwritingOcr",method = RequestMethod.POST)
    public String handwriting(@RequestParam("file") MultipartFile file) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        String data = Base64Util.encode(file.getBytes());
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_handwritingocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    //名片OCR
    @RequestMapping(value = "/businessCardOcr",method = RequestMethod.POST)
    public String businessCard(@RequestParam("file") MultipartFile file) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        System.out.println(time_stamp);
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        String data = Base64Util.encode(file.getBytes());
        params.put("image", data);
        params.put("sign", AISDK.genSignString(params));
        // 打开和URL之间的连接
        try {
            result= HttpUtils.httpPostWithForm("https://api.ai.qq.com/fcgi-bin/ocr/ocr_bcocr",params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
