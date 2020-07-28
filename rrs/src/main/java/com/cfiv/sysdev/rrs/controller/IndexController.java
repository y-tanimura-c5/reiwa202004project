package com.cfiv.sysdev.rrs.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.service.InterviewService;
import com.cfiv.sysdev.rrs.service.LoginTimeService;
import com.cfiv.sysdev.rrs.service.UserService;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * 面談結果 Service
     */
    @Autowired
    InterviewService interviewService;

    /**
     * ログイン日時情報 Service
     */
    @Autowired
    LoginTimeService loginTimeService;

    /**
     * ユーザー情報 Service
     */
    @Autowired
    UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        String username = Utils.loginUsername();
        InterviewSearchRequest cond = interviewService.getSearchRequestFromCondition(username);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        UserRequest uReq = userService.getLoginAccount();

        Page<InterviewRequest> reqPage = interviewService.search(cond, uReq, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/");
        model.addAttribute("interview_search_request", cond);
        model.addAttribute("loginUser", uReq);

        return "index";
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String indexForward(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        return index(model, page, size);
    }
}
