package com.example.demo.controller;

import com.example.demo.model.Ticket;
import com.example.demo.model.Token;
import com.example.demo.util.AccessToken;
import com.example.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class QEWX {
    @Autowired
    private AccessToken accessToken;

    @Autowired
    private RedisUtil redisUtil;
    @ResponseBody
    @RequestMapping(value = "/getInfo",method = RequestMethod.GET)
    public Ticket getInfo() throws IOException{
        Token token=new Token();
        Ticket ticket=new Ticket();
        if(redisUtil.hasKey("token")){
            if(redisUtil.getExpire("token")!=0){
                token.setAccessToken(redisUtil.getString("token"));
                if(redisUtil.hasKey("ticket")&&redisUtil.getExpire("ticket")!=0){
                    String tickets=redisUtil.getString("ticket");
                    ticket=accessToken.getUrl(tickets);
                }else{
                   String tickets=accessToken.getTicket(token.getAccessToken());
                   ticket=accessToken.getUrl(tickets);
                }
            }else{
               token= accessToken.getAccessToken();
                String tickets=accessToken.getTicket(token.getAccessToken());
                ticket=accessToken.getUrl(tickets);
            }
        }else{
            token=accessToken.getAccessToken();
            String tickets=accessToken.getTicket(token.getAccessToken());
            ticket=accessToken.getUrl(tickets);
        }
        System.out.println("token"+token.getAccessToken());
        return ticket;
    }
}
