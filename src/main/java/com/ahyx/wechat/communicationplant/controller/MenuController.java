package com.ahyx.wechat.communicationplant.controller;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.MenuService;
import com.ahyx.wechat.communicationplant.utils.TokenUtil;
import com.ahyx.wechat.communicationplant.utils.WechatUtil;
import com.ahyx.wechat.communicationplant.vo.AccessToken;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: daimengying
 * @Date: 2018/7/5 09:56
 * @Description:对订阅号的菜单的操作
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
//    @Resource
//    private TokenUtil tokenUtil;

    /**
     * 查询菜单
     * @return
     */
    @GetMapping("/get")
    public String getMenu() {
        AccessToken accessToken=TokenUtil.getInstance();
        if(accessToken!=null){
            JSONObject menuObj = menuService.getMenu(accessToken.getToken());
            return menuObj.toString();
        }
        return "";
    }

    /**
     * 创建菜单
     * @return
     */
    @PostMapping("/create")
    public int createMenu() {
        AccessToken accessToken=TokenUtil.getInstance();
        int createResult=-1;
        if(accessToken!=null){
            createResult=menuService.createMenu(getMenuList(),accessToken.getToken());
        }
        return createResult;
    }

    @PostMapping("/delete")
    public int deleteMenu() {
        AccessToken accessToken=TokenUtil.getInstance();
        int deleteResult=-1;
        if(accessToken!=null){
            deleteResult=menuService.deleteMenu(accessToken.getToken());
        }
        return deleteResult;
    }

    /**
     * 菜单数据
     * @return
     */
    public JSONObject getMenuList(){
        JSONObject resultObj=new JSONObject();
        JSONArray button=new JSONArray();
        //菜单1
        JSONObject menuObj1=new JSONObject();
        menuObj1.put("type", WeChatContant.MENU_CLICK);
        menuObj1.put("name","今日歌曲");
        menuObj1.put("key","1");
        //菜单2，含子菜单
        JSONObject menuObj2=new JSONObject();
        menuObj2.put("name","菜单");
        JSONArray subButton=new JSONArray();
        JSONObject subMenu1=new JSONObject();
        subMenu1.put("type",WeChatContant.MENU_VIEW);
        subMenu1.put("name","搜索");
        subMenu1.put("url","http://www.soso.com/");
        JSONObject subMenu2=new JSONObject();
        subMenu2.put("type",WeChatContant.MENU_SCANCODE_WAITMSG);
        subMenu2.put("name","扫码");
        subMenu2.put("key","22");
        subMenu2.put("sub_button",new JSONArray());
        subButton.add(subMenu1);
        subButton.add(subMenu2);
        menuObj2.put("sub_button",subButton);

        button.add(menuObj1);
        button.add(menuObj2);
        resultObj.put("button",button);
        return resultObj;
    }
}
