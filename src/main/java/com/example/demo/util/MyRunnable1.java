package com.example.demo.util;

import com.example.demo.model.KeepaliveObject;

import java.util.ResourceBundle;

public class MyRunnable1 implements  Runnable {
    private String id;
    public MyRunnable1(String id ) {
        this.id=id;
    }
    @Override
    public void run() {
        KeepaliveObject keepaliveObject=new KeepaliveObject();
        if(id!=null&&id!=""){
           ResourceBundle bundle = ResourceBundle.getBundle("imageH");
           String keepaliveUrl=bundle.getString("Keepalive");
           keepaliveObject.setDeviceID(id);
           String keep=keepaliveObject.toString();
           HttpUtils.URLPost(keepaliveUrl,keep,"application/json","UTF-8");
        }else{
            ResourceBundle bundle = ResourceBundle.getBundle("imageH");
            String keepaliveUrl=bundle.getString("Keepalive");
        }
    }
}
