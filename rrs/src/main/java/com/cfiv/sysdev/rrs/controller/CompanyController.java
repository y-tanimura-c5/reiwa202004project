package com.cfiv.sysdev.rrs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.service.CompanyService;

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
     * 企業情報一覧画面を表示
     * @param model Model
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        List<Company> company_list = companyService.searchAll();
        model.addAttribute("company_list", company_list);
        return "company/list";
    }

    /**
     * 企業新規登録画面を表示
     * @param model Model
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("company", new Company());
        return "company/add";
    }

    @RequestMapping(value = "/company/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Company company = companyService.findOne(id);
        model.addAttribute("company", company);
        return "company/edit";
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute Company company) {
        company.setId(id);
        companyService.save(company);
        return "redirect:/company/list";
    }

    /**
     * 企業新規登録
     * @param company リクエストデータ
     * @param model Model
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public String create(@ModelAttribute Company company, Model model) {
        // 企業情報の登録
        companyService.create(company);
        return "redirect:/company/list";
    }

}
