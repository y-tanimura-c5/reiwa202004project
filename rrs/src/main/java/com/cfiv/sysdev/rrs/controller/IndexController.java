package com.cfiv.sysdev.rrs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.service.InterviewService;
import com.cfiv.sysdev.rrs.service.LoginTimeService;
import com.cfiv.sysdev.rrs.service.UserAccountService;

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
    UserAccountService userAccountService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model) {
        InterviewSearchRequest req = new InterviewSearchRequest();
        List<InterviewRequest> req_list = interviewService.search(req);

        model.addAttribute("interview_search_request", req);
        model.addAttribute("interview_request_list", req_list);
        model.addAttribute("interview_request_size", req_list.size());

        return "index";
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String indexForward(Model model) {

        String username = Utils.loginUsername();

        Account account = userAccountService.findByUsername(username);
        loginTimeService.save(username, account.getCompanyID());

        InterviewSearchRequest req = new InterviewSearchRequest();
        List<InterviewRequest> req_list = interviewService.search(req);

        model.addAttribute("interview_search_request", req);
        model.addAttribute("interview_request_list", req_list);
        model.addAttribute("interview_request_size", req_list.size());

        return "index";
    }
}
