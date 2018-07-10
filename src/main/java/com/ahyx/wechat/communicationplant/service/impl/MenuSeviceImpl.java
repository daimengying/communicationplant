package com.ahyx.wechat.communicationplant.service.impl;

import com.ahyx.wechat.communicationplant.contants.WeChatContant;
import com.ahyx.wechat.communicationplant.service.MenuService;
import com.ahyx.wechat.communicationplant.utils.HttpsClient;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: daimengying
 * @Date: 2018/7/4 17:14
 * @Description:
 */
@Service
public class MenuSeviceImpl implements MenuService {
    private Logger _logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询菜单
     *
     * @param accessToken 有效的access_token
     * @return
     */
    @Override
    public JSONObject getMenu(String accessToken) {
        // 拼装创建菜单的url
        String url = WeChatContant.MENU_GET_URL.replace("ACCESS_TOKEN", accessToken);
        // 调用接口查询菜单
        JSONObject jsonObject = HttpsClient.httpRequest(url, "GET", null);

        return jsonObject;
    }

    /**
     * 创建菜单(替换旧菜单)
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int createMenu(JSONObject menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = WeChatContant.MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        String jsonMenu = menu.toString();
        // 调用接口创建菜单
        JSONObject jsonObject = HttpsClient.httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                _logger.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }

    /**
     * 删除菜单
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    @Override
    public int deleteMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = WeChatContant.MENU_DELETE_URL.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = HttpsClient.httpRequest(url, "POST", null);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                _logger.error("删除菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return result;
    }
}
