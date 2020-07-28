package com.cfiv.sysdev.rrs.controller;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.cfiv.sysdev.rrs.dto.EmployeeCSV;
import com.cfiv.sysdev.rrs.dto.EmployeeFileRequest;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.service.EmployeeService;
import com.cfiv.sysdev.rrs.service.UserService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

/**
 * 従業員情報 Controller
 */
@Controller
public class EmployeeController {

    /**
     * 従業員情報 Service
     */
    @Autowired
    EmployeeService employeeService;

    /**
     * ユーザー情報 Service
     */
    @Autowired
    UserService userService;

    /**
     * セッション
     */
    @Autowired
    HttpSession session;

    /**
     * 検索条件セッション
     */
    private static final String SESSION_FORM_ID = "employeeRequest";

    /**
     * 従業員情報検索画面初期表示
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/employee/list", method = RequestMethod.GET)
    public String list(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        EmployeeRequest cond = new EmployeeRequest();

        UserRequest uReq = userService.getLoginAccount();

        if (uReq.getUserRoleCode() != Consts.USERROLECODE_ADMIN) {
            cond.setCompanyID(uReq.getCompanyID());
        }

        model.addAttribute("page"
                , new PageImpl<EmployeeRequest>(new ArrayList<EmployeeRequest>()
                        , PageRequest.of(currentPage - 1, pageSize), 0));
        model.addAttribute("url", "/employee/list");
        model.addAttribute("employee_request", cond);
        model.addAttribute("loginUser", uReq);

        return "employee/list";
    }

    /**
     * 従業員情報検索結果表示(ページ)
     * @param model
     * @param page
     * @return 従業員情報検索画面(POST結果)
     */
    @RequestMapping(value="/employee/pagenate", method = RequestMethod.GET)
    public String pagenate(Model model, @RequestParam("page") int page) {
        EmployeeRequest req = (EmployeeRequest) session.getAttribute(SESSION_FORM_ID);

        return search(model, req, Optional.of(page), Optional.of(Consts.PAGENATION_PAGESIZE));
    }

    /**
     * 従業員情報検索画面検索結果表示
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/employee/search", method = RequestMethod.POST)
    public String search(Model model
            , @ModelAttribute("employee_request") EmployeeRequest req
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        Page<EmployeeRequest> reqPage = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeCode()
                , PageRequest.of(currentPage - 1, pageSize));

        session.setAttribute(SESSION_FORM_ID, req);

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/employee/pagenate");
        model.addAttribute("employee_request", req);
        model.addAttribute("searchDone", 1);
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "employee/list";
    }

    /**
     * 従業員一括登録画面表示
     * @param model Model
     * @return 従業員情報一括登録画面
     */
    @RequestMapping(value = "/employee/bulkregist", method = RequestMethod.GET)
    public String bulkRegist(Model model) {
        model.addAttribute("employeeFileRequest", new EmployeeFileRequest());
        model.addAttribute("regist_result", false);
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "employee/bulkregist";
    }

    /**
     * 従業員一括登録情報取り込み
     * @param model Model
     * @return 従業員一括登録画面
     */
    @RequestMapping(value = "/employee/upload", method = RequestMethod.POST)
    public String upload(@Valid EmployeeFileRequest employeeFileRequest, BindingResult result, Model model) {
        UserRequest uReq = userService.getLoginAccount();

        if (result.hasErrors()) {
            model.addAttribute("loginUser", uReq);
            return "employee/bulkregist";
        }
        else {
            try {
                String csvStr = new String(employeeFileRequest.getEmployeeFile().getBytes(), "MS932");
                Reader reader = new StringReader(csvStr);
                CsvToBean<EmployeeCSV> csvToBean = new CsvToBeanBuilder<EmployeeCSV>(reader).withType(EmployeeCSV.class).build();
                employeeService.saveCSV(csvToBean.parse());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("regist_result", true);
        model.addAttribute("loginUser", uReq);

        return "employee/bulkregist";
    }

    @RequestMapping(value = "/employee/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("employee_request", employeeService.findOneRequest(id));
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "employee/edit";
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.POST)
    public String update(RedirectAttributes attributes, @PathVariable Long id, @ModelAttribute("employee_request") EmployeeRequest req) {
        employeeService.save(id, req);
        attributes.addFlashAttribute("loginUser", userService.getLoginAccount());

        return "redirect:/employee/list";
    }
}
