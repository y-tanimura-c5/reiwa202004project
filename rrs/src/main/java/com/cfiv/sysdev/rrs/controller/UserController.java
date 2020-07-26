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

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.UserAccountService;

/**
 * ユーザー情報 Controller
 */
@Controller
public class UserController {

    /**
     * ユーザー情報 Service
     */
    @Autowired
    UserAccountService userAccountService;

    /**
     * 企業情報 Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * ユーザー情報一覧画面を表示
     * @param model Model
     * @return ユーザー情報一覧画面
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<UserRequest> req_list = new ArrayList<UserRequest>();
        List<Account> account_list = userAccountService.searchAll();

        for (Account account : account_list) {
            req_list.add(new UserRequest(account.getIdString(1), account.getUsername(), account.getPassword(),
                    account.getDisplayName(), account.getUserRole(), companyService.getCompanyName(account.getCompanyID()),
                    account.isEnabled()));
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
     * ユーザー新規登録画面を表示
     * @param model Model
     * @return ユーザー新規登録画面
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        Map<String, String> company_items = companyService.getAllCompanyNames();
        List<String> keys = new ArrayList<String>(company_items.keySet());

        model.addAttribute("user_request", new UserRequest("", "", "", "", Consts.USERROLE_REFINER_CODE, company_items.get(keys.get(0)), true));
        model.addAttribute("company_items", company_items);

        return "user/add";
    }

    /**
     * ユーザー情報新規登録
     * @param req リクエストデータ
     * @param result バリデーションチェック結果
     * @param model Model
     * @return ユーザー情報一覧画面(登録完了時)／新規登録画面(エラー発生時)
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("user_request") @Valid UserRequest req, BindingResult result, Model model) {
        if (result.hasErrors()) {
//            for(FieldError err: result.getFieldErrors()) {
//                LogUtils.info("error field  [" + err.getField() + "], error code = [" + err.getCode() + "]");
//            }

            model.addAttribute("company_items", companyService.getAllCompanyNames());
            return "/user/add";
        }

        userAccountService.create(req);
        return "redirect:/user/list";
    }

    /**
     * ユーザー情報更新画面を表示
     * @param id ID
     * @param model Model
     * @return ユーザー情報更新画面
     */
    @RequestMapping(value = "/user/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        Account account = userAccountService.findOne(id);
        Map<String, String> company_items = companyService.getAllCompanyNames();

        model.addAttribute("user_request", new UserRequest(account.getIdString(1), account.getUsername(), account.getPassword(),
                account.getDisplayName(), account.getUserRole(), companyService.getCompanyName(account.getCompanyID()),
                account.isEnabled()));
        model.addAttribute("company_items", company_items);

        String key = BindingResult.MODEL_KEY_PREFIX + "user_request";
        if (model.containsKey("errors")) {
            model.addAttribute(key, model.get("errors"));
        }

        return "user/edit";
    }

    /**
     * ユーザー情報更新
     * @param attributes リダイレクト先に渡す属性値
     * @param id ID
     * @param req リクエストデータ
     * @param result バリデーションチェック結果
     * @param model Model
     * @return ユーザー情報一覧画面(登録完了時)／新規登録画面(エラー発生時)
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public String update(RedirectAttributes attributes, @PathVariable Long id,
            @ModelAttribute("user_request") @Valid UserRequest req, BindingResult result, Model model) {
        boolean error = true;

        if (result.hasErrors()) {
            for(FieldError err: result.getFieldErrors()) {
//                LogUtils.info("error field = [" + err.getField() + "], error code = [" + err.getCode() + "]");

                // 空パスワードが来た場合は、前回設定パスワードで更新するためバリデーションチェック外とする
                if ((err.getField().equals("password") && err.getCode().equals("Size")) && req.getPassword().isEmpty() ||
                        (err.getField().equals("passwordCheck") && err.getCode().equals("Size") && req.getPasswordCheck().isEmpty()) ||
                        (err.getField().equals("password") && err.getCode().equals("NotBlank")) ||
                        (err.getField().equals("passwordCheck") && err.getCode().equals("NotBlank"))) {
                    error = false;
                }
                // 更新のためすでに存在するユーザーに対する更新はバリデーションチェック外とする
                else if (err.getField().equals("username") && err.getCode().equals("Unused")) {
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