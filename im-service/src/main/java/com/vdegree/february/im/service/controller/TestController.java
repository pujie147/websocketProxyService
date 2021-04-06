package com.vdegree.february.im.service.controller;

import com.google.gson.Gson;
import com.vdegree.february.im.api.rpc.PublicAppServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * TODO 用于模拟调试 rpc调用
 *
 * @author DELL
 * @version 1.0
 * @date 2021/3/30 11:47
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private PublicAppServiceApi publicAppServiceApi;

    @Autowired
    private Gson gson;


    @RequestMapping("sendGrabOrderApi")
    @ResponseBody
    public String sendGrabOrderApi(@RequestBody SendGrabOrderApiVo sendGrabOrderApiVo){
        publicAppServiceApi.sendGrabOrderApi(sendGrabOrderApiVo.getSendUserId(),sendGrabOrderApiVo.getInvitationUserIds(),sendGrabOrderApiVo.getRoomType());
        return "success";
    }

}
