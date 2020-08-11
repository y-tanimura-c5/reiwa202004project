package com.cfiv.sysdev.rrs.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.request.CompanyRequest;
import com.cfiv.sysdev.rrs.request.UserRequest;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.UserService;

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
     * ユーザー情報 Service
     */
    @Autowired
    UserService userService;

    /**
     * 企業情報一覧画面を表示
     * @param model Model
     * @param page ページ番号
     * @param size 1ページあたりの表示行数
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public String list(Model model
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        Page<CompanyRequest> reqPage = companyService.search(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/company/list");
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "company/list";
    }

    /**
     * 企業情報新規登録画面表示
     * @param model Model
     * @return 企業情報新規登録画面
     */
    @RequestMapping(value = "/company/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("company_request", new CompanyRequest("", "", Consts.ENABLED, null, null));
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "company/add";
    }

    /**
     * 企業情報新規登録
     * @param attributes リダイレクト用属性
     * @param req リクエストデータ
     * @param result バリデーションチェック結果
     * @param model モデル
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public String create(RedirectAttributes attributes
            , @ModelAttribute("company_request") @Valid CompanyRequest req
            , BindingResult result
            , Model model) {
        UserRequest lReq = userService.getLoginAccount();

        if (result.hasErrors()) {
            model.addAttribute("loginUser", lReq);
            model.addAttribute("company_request", req);

            return "company/add";
        }

        companyService.create(req);

        attributes.addFlashAttribute("loginUser", lReq);

        return "redirect:/company/list";
    }

    /**
     * 企業情報編集画面表示
     * @param id 企業情報ID
     * @param model モデル
     * @return 企業情報編集画面
     */
    @RequestMapping(value = "/company/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id
            , Model model) {
        Company company = companyService.findOne(id);

        model.addAttribute("company_request", company.toRequest());
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "company/edit";
    }

    /**
     * 企業情報更新
     * @param attributes リダイレクト用属性
     * @param id 企業情報ID
     * @param req リクエストデータ
     * @param result バリデーションチェック結果
     * @param model モデル
     * @return 企業情報一覧画面
     */
    @RequestMapping(value = "/company/{id}", method = RequestMethod.POST)
    public String update(RedirectAttributes attributes
            , @PathVariable Long id
            , @ModelAttribute("company_request") @Valid CompanyRequest req
            , BindingResult result
            , Model model) {
        UserRequest lReq = userService.getLoginAccount();

        if (result.hasErrors()) {
            model.addAttribute("loginUser", lReq);
            model.addAttribute("company_request", req);

            return "company/edit";
        }

        companyService.save(id, req);

        attributes.addFlashAttribute("loginUser", lReq);

        return "redirect:/company/list";
    }
}
