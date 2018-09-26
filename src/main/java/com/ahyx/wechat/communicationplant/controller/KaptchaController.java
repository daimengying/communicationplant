package com.ahyx.wechat.communicationplant.controller;

import org.springframework.stereotype.Controller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.impl.DefaultKaptcha;

/**
 * @Author: daimengying
 * @Date: 2018/9/20 17:25
 * @Description:谷歌验证码
 */
@Controller
public class KaptchaController {
    @Autowired
    private DefaultKaptcha captchaProducer;

    /**
     * 获取验证码 的 请求路径
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @RequestMapping("/getKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            httpServletRequest.getSession().setAttribute("vrifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


    /**
     * 验证的方法
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value="/imgvrifyKaptcha")
    @ResponseBody
    public Map<String,Object> imgvrifyKaptcha(HttpServletRequest httpServletRequest){
        Map<String,Object> result = new HashMap<>();
        String codeInSession = (String) httpServletRequest.getSession().getAttribute("vrifyCode");
        String codeInRequest = httpServletRequest.getParameter("vrifyCode");
        if(StringUtils.isEmpty(codeInSession)||StringUtils.isEmpty(codeInRequest)){
            result.put("success",false);
        }

        System.out.println("Session  vrifyCode "+codeInSession+" form vrifyCode "+codeInRequest);
        if (!codeInSession.equals(codeInRequest)) {
            result.put("success",false);
        } else {
            result.put("success",true);
        }
        return result;
    }
}
