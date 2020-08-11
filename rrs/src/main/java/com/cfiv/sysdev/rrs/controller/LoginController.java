package com.cfiv.sysdev.rrs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ログイン Controller
 */
@Controller
public class LoginController {

    /**
     * ログイン成功
     * @param model Model
     * @return ログイン画面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("iserror", false);

        return "login";
    }

    /**
     * ログイン失敗
     * @param model Model
     * @return ログイン画面
     */
    @RequestMapping(value = "/login-error", method = RequestMethod.GET)
    public String loginError(Model model) {
        model.addAttribute("iserror", true);

        return "login";
    }
}
