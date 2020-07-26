package com.cfiv.sysdev.rrs.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.dto.CompanyRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.InterviewService;
import com.cfiv.sysdev.rrs.service.LoginTimeService;

/**
 * 企業情報 Controller
 */
@Controller
public class CompanyController {

    /**
     * 企業情報 Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * 面談情報 Service
     */
    @Autowired
    InterviewService interviewService;

    /**
     * ログイン日時情報 Service
     */
    @Autowired
    LoginTimeService loginTimeService;

    /**
     * 企業情報一覧画面を表示
     * @param model Model
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<CompanyRequest> req_list = new ArrayList<CompanyRequest>();

        List<Company> company_list = companyService.searchAll();

        for (Company company : company_list) {
            Date lastlogin = loginTimeService.getLastLoginTimeFromCompanyID(company.getId());
            Date lastInterview = interviewService.getLastInterviewDateFromCompanyID(company.getId());

            req_list.add(company.toRequest(lastlogin, lastInterview));
        }

        model.addAttribute("company_request_list", req_list);

        return "company/list";
    }

    /**
     * 企業情報新規登録画面表示
     * @param model Model
     * @return 企業情報新規登録画面
     */
    @RequestMapping(value = "/company/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("company_request", new CompanyRequest("", "", true, null, null));

        return "company/add";
    }

    /**
     * 企業情報編集画面表示
     * @param id 企業情報ID
     * @param model モデル
     * @return 企業情報編集画面
     */
    @RequestMapping(value = "/company/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Company company = companyService.findOne(id);

        model.addAttribute("company_request", company.toRequest());

        return "company/edit";
    }

    /**
     * 企業情報更新
     * @param id 企業情報ID
     * @param req リクエストデータ
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute CompanyRequest req) {
        companyService.save(id, req);

        return "redirect:/company/list";
    }

    /**
     * 企業情報新規登録
     * @param req リクエストデータ
     * @param model モデル
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public String create(@ModelAttribute CompanyRequest req, Model model) {
        companyService.create(req);

        return "redirect:/company/list";
    }
}
