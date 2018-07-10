package com.ahyx.wechat.communicationplant.service;


import net.sf.json.JSONObject;

import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/4 17:13
 * @Description:菜单
 */
public interface MenuService {
     JSONObject getMenu(String accessToken);
     int createMenu(JSONObject menu , String accessToken);
     int deleteMenu(String accessToken);
}
