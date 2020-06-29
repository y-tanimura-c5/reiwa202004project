package com.cfiv.sysdev.rrs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.form.InputForm;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.UserAccountService;

/**
 * ���[�U�[��� Controller
 */
@Controller
public class UserController {

    /**
     * ���[�U�[��� Service
     */
    @Autowired
    UserAccountService userAccountService;

    /**
     * ��Ə�� Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * ���[�U�[���ꗗ��ʂ�\��
     * @param model Model
     * @return ���[�U�[���ꗗ���
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        List<UserRequest> req_list = new ArrayList<UserRequest>();
        List<Account> account_list = userAccountService.searchAll();

        for (Account account : account_list) {
            req_list.add(new UserRequest(account.idToString(1), account.getUsername(), account.getPassword(),
                    account.getDisplayName(), account.getUserRoleString(), companyService.getCompanyName(account.getCompanyID()),
                    account.getEnabledString()));
        }

//        List<String> keys = new ArrayList<String>(company_items.keySet());
//        LogUtils.info("companyList");
//        for (String key : keys) {
//            LogUtils.info("company = " + key + " " + company_items.get(key));
//        }

        model.addAttribute("user_request_list", req_list);

        return "user/list";
    }

    /**
     * ���[�U�[�V�K�o�^��ʂ�\��
     * @param model Model
     * @return ���[�U�[���ꗗ���
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        Map<String, String> company_items = companyService.getAllCompanyNames();
        List<String> keys = new ArrayList<String>(company_items.keySet());

        model.addAttribute("user_request", new UserRequest("", "", "", "", "���t�@�C�i�[", company_items.get(keys.get(0)), "�L��"));
        model.addAttribute("company_items", company_items);

        return "user/add";
    }

    @RequestMapping(value = "/user/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Account account = userAccountService.findOne(id);
        Map<String, String> company_items = companyService.getAllCompanyNames();

        model.addAttribute("user_request", new UserRequest(account.idToString(1), account.getUsername(), account.getPassword(),
                account.getDisplayName(), account.getUserRoleString(), companyService.getCompanyName(account.getCompanyID()),
                account.getEnabledString()));
        model.addAttribute("company_items", company_items);

        return "user/edit";
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute UserRequest req, @Validated InputForm form, BindingResult result) {
        if (req.getPassword().isEmpty()) {
            LogUtils.info("password = " + req.getPassword());
            LogUtils.info("passwordConf = " + req.getPasswordConf());
        }

        Account account = userAccountService.findOne(id);
        req.setUsername(account.getUsername());
        userAccountService.save(id, req);
        return "redirect:/user/list";
    }

    /**
     * ���[�U�[���V�K�o�^
     * @param req ���N�G�X�g�f�[�^
     * @param model Model
     * @return ��Ə��ꗗ���
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(@ModelAttribute UserRequest req, Model model) {
        userAccountService.create(req);
        return "redirect:/user/list";
    }
}