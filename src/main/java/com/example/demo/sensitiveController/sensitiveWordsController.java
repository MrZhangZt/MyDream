package com.example.demo.sensitiveController;

import cn.xsshome.taip.vision.TAipVision;
import com.example.demo.util.AISDK;
import com.example.demo.util.HttpUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sensitive")
public class sensitiveWordsController {
    private String appId="2125636236";
    private String appKey="xjVevquA7DFNwW1R";

    @GetMapping("/videoSen")
    public String videoSensitive(@RequestParam("url") String url) throws IOException{
        Map params=new HashMap<String,String>();
        String result="";
        String nonce_str= UUID.randomUUID().toString().replace("-","");
        params.put("app_id",appId);
        params.put("app_key",appKey);
        String time_stamp=String.valueOf(AISDK.DateToTimestamp(new Date()));
        params.put("time_stamp",time_stamp);
        params.put("nonce_str",nonce_str);
        params.put("speech_id","aitestone");
        String urlEncord= URLEncoder.encode(url,"UTF-8");
        params.put("speech_url",urlEncord);
        params.put("porn_detect","1");
        params.put("keyword_detect","1");
        params.put("sign", AISDK.genSignString(params));
        System.out.println(params);
        // 打开和URL之间的连接
        try {
            result= HttpUtils.URLGet("https://api.ai.qq.com/fcgi-bin/aai/aai_evilaudio",params,"UTF-8");
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @GetMapping("/videoSenTaip")
    public String videoSensitiveTaip(@RequestParam("url") String url) throws Exception{
        TAipVision aipVision = new TAipVision(appId, appKey);
        String speech_url=url;
        String speech_id = UUID.randomUUID().toString().replace("-", "");
        String result = aipVision.aaiEvilAudio(speech_id,speech_url);//智能鉴黄
        return result;
    }

    @PostMapping("/imageIdentify")
    public String imageIdentify(@RequestParam("file")MultipartFile file ,@RequestParam("url") String url) throws Exception{
        String restult ="";
        return restult;
    }
}
