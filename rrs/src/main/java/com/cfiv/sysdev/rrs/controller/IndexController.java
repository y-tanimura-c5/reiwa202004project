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
import com.cfiv.sysdev.rrs.request.InterviewRequest;
import com.cfiv.sysdev.rrs.request.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.request.UserRequest;
import com.cfiv.sysdev.rrs.service.InterviewService;
import com.cfiv.sysdev.rrs.service.LoginTimeService;
import com.cfiv.sysdev.rrs.service.UserService;

/**
 * ホーム画面 Controller
 */
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

    /**
     * ホーム画面(GET)
     * @param model Model
     * @param page ページ番号
     * @param size 1ページあたりの表示行数
     * @return ホーム画面
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {
        UserRequest uReq = userService.getLoginAccount();
        InterviewSearchRequest cond = interviewService.getSearchRequestFromCondition(uReq.getUsername());

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);

        Page<InterviewRequest> reqPage = interviewService.searchRequest(cond, uReq, PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/");
        model.addAttribute("interview_search_request", cond);
        model.addAttribute("loginUser", uReq);

        return "index";
    }


    /**
     * ホーム画面(POST)
     * @param model Model
     * @param page ページ番号
     * @param size 1ページあたりの表示行数
     * @return ホーム画面
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String indexForward(Model model
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {
        UserRequest uReq = userService.getLoginAccount();
        loginTimeService.save(uReq.getUsername(), uReq.getCompanyIDLong());

        return index(model, page, size);
    }
}
