package com.cfiv.sysdev.rrs.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.UserService;

/**
 * ユーザー情報 Controller
 */
@Controller
public class UserController {

    /**
     * ユーザー情報 Service
     */
    @Autowired
    UserService userService;

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
    public String list(Model model
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        Page<UserRequest> reqPage = userService.search(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/user/list");
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "user/list";
    }

    /**
     * ユーザー新規登録画面を表示
     * @param model Model
     * @return ユーザー新規登録画面
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        Map<String, String> companyItems = companyService.getAllCompanyNamesForDropdown();
        List<String> keys = new ArrayList<String>(companyItems.keySet());

        model.addAttribute("user_request", new UserRequest("", "", "", "", Consts.USERROLECODE_REFINER, "", companyItems.get(keys.get(0)), true));
        model.addAttribute("company_items", companyItems);
        model.addAttribute("loginUser", userService.getLoginAccount());

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
    public String create(RedirectAttributes attributes, @ModelAttribute("user_request") @Valid UserRequest req, BindingResult result, Model model) {
        UserRequest uReq = userService.getLoginAccount();

        if (result.hasErrors()) {
//            for(FieldError err: result.getFieldErrors()) {
//                LogUtils.info("error field  [" + err.getField() + "], error code = [" + err.getCode() + "]");
//            }

            model.addAttribute("company_items", companyService.getAllCompanyNamesForDropdown());
            model.addAttribute("loginUser", uReq);

            return "/user/add";
        }

        userService.create(req);

        attributes.addFlashAttribute("loginUser", uReq);

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
        Account account = userService.findOne(id);
        Map<String, String> companyItems = companyService.getAllCompanyNamesForDropdown();

        model.addAttribute("user_request", account.toRequest(companyService.getCompanyNameForDropdown(account.getCompanyID())));
        model.addAttribute("company_items", companyItems);

        String key = BindingResult.MODEL_KEY_PREFIX + "user_request";
        if (model.containsKey("errors")) {
            model.addAttribute(key, model.get("errors"));
        }

        model.addAttribute("loginUser", userService.getLoginAccount());

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

        userService.save(id, req);

        attributes.addFlashAttribute("loginUser", userService.getLoginAccount());

        return "redirect:/user/list";
    }
}