package com.yanerwu.apple.sign.controller;

import com.alibaba.fastjson.JSON;
import com.yanerwu.apple.sign.utils.AppleLoginUtil;
import com.yanerwu.apple.sign.vo.IdTokenPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@RequestMapping(value = "/apple")
public class IndexController {

    @RequestMapping(value = "/index")
    public String index() {
        return "sign-in-apple-web";
    }

    @RequestMapping(value = {"/callback"})
    public String callback(ModelMap modelMap, String code) {
        try {
            IdTokenPayload idTokenPayload = AppleLoginUtil.appleAuth(code);
            modelMap.addAttribute("idTokenPayload", idTokenPayload);
            log.info(JSON.toJSONString(idTokenPayload));
        } catch (Exception e) {
            log.error("", e);
        }
        return "sign-in-apple-web-result";
    }

}
