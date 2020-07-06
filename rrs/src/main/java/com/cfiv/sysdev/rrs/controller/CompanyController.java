package com.cfiv.sysdev.rrs.controller;

import java.util.ArrayList;
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

/**
 * ��Ə�� Controller
 */
@Controller
public class CompanyController {

    /**
     * ��Ə�� Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * ��Ə��ꗗ��ʂ�\��
     * @param model Model
     * @return ��Ə��ꗗ���
     */
    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        List<CompanyRequest> req_list = new ArrayList<CompanyRequest>();

        List<Company> company_list = companyService.searchAll();

        for (Company company : company_list) {
            req_list.add(new CompanyRequest(company.getIdString(4), company.getName(), company.getEnabledString()));
        }

        model.addAttribute("company_request_list", req_list);

        return "company/list";
    }

    /**
     * ��ƐV�K�o�^��ʂ�\��
     * @param model Model
     * @return ��Ə��ꗗ���
     */
    @RequestMapping(value = "/company/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("company_request", new CompanyRequest("", "", "�L��"));
        return "company/add";
    }

    @RequestMapping(value = "/company/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Company company = companyService.findOne(id);
        model.addAttribute("company_request", new CompanyRequest(company.getIdString(4), company.getName(), company.getEnabledString()));
        return "company/edit";
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute CompanyRequest req) {
        companyService.save(id, req);
        return "redirect:/company/list";
    }

    /**
     * ��Ə��V�K�o�^
     * @param req ���N�G�X�g�f�[�^
     * @param model Model
     * @return ��Ə��ꗗ���
     */
    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public String create(@ModelAttribute CompanyRequest req, Model model) {
        // ��Ə��̓o�^
        companyService.create(req);
        return "redirect:/company/list";
    }
}
