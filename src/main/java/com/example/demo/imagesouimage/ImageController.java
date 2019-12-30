package com.example.demo.imagesouimage;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Image;
import com.example.demo.model.ImageSearchedByImageObject;
import com.example.demo.model.RegisterObject;
import com.example.demo.model.UnRegisterObject;
import com.example.demo.util.Base64Util;
import com.example.demo.util.HttpUtils;
import com.example.demo.util.MyRunnable1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@RestController
@PropertySource("classpath:imageH.properties")
public class ImageController {
    public String id="";
    public String corn1="0/10 * * * * ?";

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> future1;

    @GetMapping("/index")
    public ModelAndView imageIndex(){
        return new ModelAndView("/index.html");
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @ResponseBody
    @RequestMapping(value = "/souImage",method = RequestMethod.POST)
    public Map<String,Object> souImage(@RequestBody RegisterObject registerObject) {
        Map<String,Object> result=new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("imageH");
        String registerUrl=bundle.getString("Register");
        Map<String,Object> map=new HashMap<>();
        if(registerObject.getDeviceID()==null||registerObject.getDeviceID().equals("")){
            result.put("status",0);
            result.put("Msg","设备号不能为空");
            return result;
        }
        String register=registerObject.toString();
        map= HttpUtils.URLPost(registerUrl,register,"application/json","UTF-8");
        result.put("status",1);
        result.put("Msg","成功");
        result.put("data",map);
       /* String json= JSONObject.toJSONString(map);*/
        id=registerObject.getDeviceID();
        this.startCron1(id);
        return result;
    }
    @ResponseBody
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public Map<String,Object> test(@RequestParam("file") MultipartFile[] file) throws  IOException{
        Map<String,Object> result=new HashMap<>();
        ArrayList<Image> images=new ArrayList<>();
        ImageSearchedByImageObject imageSearchedByImageObject=new ImageSearchedByImageObject();
        if(file.length<1){
            result.put("status",0);
            result.put("Msg","图片不能为空");
            return result;
        }
        for(int i=0;i<file.length;i++){
           String name=file[i].getOriginalFilename();
           String FileFormat=name.substring(name.indexOf(".")+1, name.length());
           String data= Base64Util.encode(file[i].getBytes());
           Image image=new Image();
           image.setData(data);
           image.setFileFormat(FileFormat);
           images.add(image);
        }

        imageSearchedByImageObject.setImage(images);
        result.put("status",1);
        result.put("Msg","成功");
        result.put("data",imageSearchedByImageObject);
        return result;
    }
    @ResponseBody
    @PostMapping("/UnRegister")
    public Map<String,Object> UnRegister(@RequestBody UnRegisterObject unRegisterObject){
        Map<String,Object> result=new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("imageH");
        String unRegisterUrl=bundle.getString("UnRegister");
        if(unRegisterObject.getDeviceID()==null||unRegisterObject.getDeviceID().equals("")){
            result.put("status",0);
            result.put("Msg","设备号不能为空");
            return result;
        }
        String unRegister=unRegisterObject.toString();
        Map<String,Object> map=new HashMap<>();
        map= HttpUtils.URLPost(unRegisterUrl,unRegister,"application/json","UTF-8");
        result.put("status",1);
        result.put("Msg","成功");
        result.put("data",map);
        String json= JSONObject.toJSONString(map);
        this.stopCron1();
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/ImageSearchedByImagesSync",method = RequestMethod.POST)
    public Map<String,Object> ImageSearchedByImagesSync(@RequestBody ImageSearchedByImageObject imageSearchedByImageObject, @RequestParam("file") MultipartFile[] file) throws IOException{
        Map<String,Object> result=new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle("imageH");
        String imagesSyncUrl=bundle.getString("ImageSearchedByImagesSync");
        if(file.length<1){
            result.put("status",0);
            result.put("Msg", "请添加图片！");
            return result;
        }

        String imageSearchDate=imageSearchedByImageObject.toString();
        Map<String,Object> map=new HashMap<>();
        map= HttpUtils.URLPost(imagesSyncUrl,imageSearchDate,"application/json","UTF-8");
       // String json= JSONObject.toJSONString(map);
        result.put("status",1);
        result.put("Msg","成功");
        result.put("data",map);
        return result;
    }

    @ResponseBody
    @PostMapping("/startCron1")
    public void startCron1(@RequestParam("id") String id) {
        future1 = threadPoolTaskScheduler.schedule(new MyRunnable1(id),new Trigger(){
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext){
                return new CronTrigger(corn1).nextExecutionTime(triggerContext);
            }
        });

        System.out.println("DynamicTask.startCron1()");
    }

    @ResponseBody
    @PostMapping("/stopCron1")
    public String stopCron1() {
        if (future1 != null) {
            future1.cancel(true);
        }
        System.out.println("DynamicTask.stopCron1()");
        return "";
    }
}
