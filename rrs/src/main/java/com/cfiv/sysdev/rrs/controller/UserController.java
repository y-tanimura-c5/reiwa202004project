package com.cfiv.sysdev.rrs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.UserAddRequest;
import com.cfiv.sysdev.rrs.dto.UserEditRequest;
import com.cfiv.sysdev.rrs.entity.Account;
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
        List<UserEditRequest> req_list = new ArrayList<UserEditRequest>();
        List<Account> account_list = userAccountService.searchAll();

        for (Account account : account_list) {
            req_list.add(new UserEditRequest(account.idToString(1), account.getUsername(), account.getPassword(),
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
     * @return ���[�U�[�V�K�o�^���
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        Map<String, String> company_items = companyService.getAllCompanyNames();
        List<String> keys = new ArrayList<String>(company_items.keySet());

        model.addAttribute("user_request", new UserAddRequest("", "", "", "", "���t�@�C�i�[", company_items.get(keys.get(0)), "�L��"));
        model.addAttribute("company_items", company_items);

        return "user/add";
    }

    /**
     * ���[�U�[���V�K�o�^
     * @param req ���N�G�X�g�f�[�^
     * @param result �o���f�[�V�����`�F�b�N����
     * @param model Model
     * @return ���[�U�[���ꗗ���(�o�^������)�^�V�K�o�^���(�G���[������)
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("user_request") @Valid UserAddRequest req, BindingResult result, Model model) {
        if (result.hasErrors()) {
            for(FieldError err: result.getFieldErrors()) {
                LogUtils.info("error field  [" + err.getField() + "], error code = [" + err.getCode() + "]");
            }

            model.addAttribute("company_items", companyService.getAllCompanyNames());
            return "/user/add";
        }

        userAccountService.create(req);
        return "redirect:/user/list";
    }

    /**
     * ���[�U�[���X�V��ʂ�\��
     * @param id ID
     * @param model Model
     * @return ���[�U�[���X�V���
     */
    @RequestMapping(value = "/user/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        Account account = userAccountService.findOne(id);
        Map<String, String> company_items = companyService.getAllCompanyNames();

        model.addAttribute("user_request", new UserEditRequest(account.idToString(1), account.getUsername(), account.getPassword(),
                account.getDisplayName(), account.getUserRoleString(), companyService.getCompanyName(account.getCompanyID()),
                account.getEnabledString()));
        model.addAttribute("company_items", company_items);

        String key = BindingResult.MODEL_KEY_PREFIX + "user_request";
        if (model.containsKey("errors")) {
            model.addAttribute(key, model.get("errors"));
        }

        return "user/edit";
    }

    /**
     * ���[�U�[���X�V
     * @param attributes ���_�C���N�g��ɓn�������l
     * @param id ID
     * @param req ���N�G�X�g�f�[�^
     * @param result �o���f�[�V�����`�F�b�N����
     * @param model Model
     * @return ���[�U�[���ꗗ���(�o�^������)�^�V�K�o�^���(�G���[������)
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public String update(RedirectAttributes attributes, @PathVariable Long id,
            @ModelAttribute("user_request") @Valid UserEditRequest req, BindingResult result, Model model) {
        boolean error = true;

        if (result.hasErrors()) {
            for(FieldError err: result.getFieldErrors()) {
                LogUtils.info("error field = [" + err.getField() + "], error code = [" + err.getCode() + "]");

                // ��p�X���[�h�������ꍇ�́A�O��ݒ�p�X���[�h�ōX�V���邽�߃G���[�ɂ��Ȃ�(�o���f�[�V�����`�F�b�N�O�Ƃ���)
                if ((err.getField().equals("password") && err.getCode().equals("Size")) && req.getPassword().isEmpty() ||
                        (err.getField().equals("passwordCheck") && err.getCode().equals("Size") && req.getPasswordCheck().isEmpty())) {
                    error = false;
                }
                else {
                    error = true;
                    break;
                }
            }

            if (error) {
                attributes.addFlashAttribute("errors", result);
                return "redirect:/user/{id}/edit";
            }
        }

        userAccountService.save(id, req);
        return "redirect:/user/list";
    }
}