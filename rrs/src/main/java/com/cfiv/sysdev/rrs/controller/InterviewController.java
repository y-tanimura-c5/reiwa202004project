package com.cfiv.sysdev.rrs.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.InterviewCSV;
import com.cfiv.sysdev.rrs.dto.InterviewConditionRequest;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.entity.InterviewAttach;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.EmployeeService;
import com.cfiv.sysdev.rrs.service.InterviewService;
import com.cfiv.sysdev.rrs.service.UserService;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

/**
 * 面談結果 Controller
 */
@Controller
public class InterviewController {

    /**
     * 面談結果 Service
     */
    @Autowired
    InterviewService interviewService;

    /**
     * 従業員情報 Service
     */
    @Autowired
    EmployeeService employeeService;

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
     * セッション
     */
    @Autowired
    HttpSession session;

    /**
     * 検索条件セッション
     */
    private static final String SESSION_FORM_ID = "interviewSearchRequest";

    /**
     * 面談結果検索画面初期表示
     * @param model Model
     * @return 面談結果検索画面
     */
    @RequestMapping(value = "/interview/listinit", method = RequestMethod.GET)
    public String init(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        InterviewSearchRequest cond = new InterviewSearchRequest();
        Page<InterviewRequest> reqPage = new PageImpl<InterviewRequest>(new ArrayList<InterviewRequest>()
                , PageRequest.of(currentPage - 1, pageSize), 0);

        UserRequest uReq = userService.getLoginAccount();

        if (interviewService.isUseInitSearchCondition(uReq.getUsername())) {
            cond = interviewService.getSearchRequestFromCondition(uReq.getUsername());
            reqPage = interviewService.searchRequest(cond, uReq, PageRequest.of(currentPage - 1, pageSize));
        }

        if (uReq.getUserRoleCode() != Consts.USERROLECODE_ADMIN) {
            cond.setCompanyID(uReq.getCompanyID());
        }

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/interview/list");
        model.addAttribute("interview_search_request", cond);
        model.addAttribute("loginUser", uReq);

        return "interview/list";
    }

    /**
     * 面談結果検索画面
     * @param model Model
     * @return 面談結果検索画面
     */
    @RequestMapping(value = "/interview/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "interview/list";
    }

    /**
     * 面談結果検索結果表示(ページ)
     * @param model
     * @param page
     * @return 面談結果検索画面(POST結果)
     */
    @RequestMapping(value="/interview/pagenate", method = RequestMethod.GET)
    public String pagenate(Model model, @RequestParam("page") int page) {
        InterviewSearchRequest req = (InterviewSearchRequest) session.getAttribute(SESSION_FORM_ID);

        return search(model, req, Optional.of(page), Optional.of(Consts.PAGENATION_PAGESIZE));
    }

    /**
     * 面談結果検索結果表示
     * @param model モデル
     * @param req 検索条件
     * @param page ページ番号
     * @param size 1ページ表示件数
     * @return 面談結果検索画面
     */
    @RequestMapping(value = "/interview/search", method = RequestMethod.POST)
//    @RequestMapping(value = "/interview/search", params = "search", method = RequestMethod.POST)
    public String search(Model model
            , @ModelAttribute("interview_search_request") InterviewSearchRequest req
            , @RequestParam("page") Optional<Integer> page
            , @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(Consts.PAGENATION_PAGESIZE);
        UserRequest uReq = userService.getLoginAccount();

        Page<InterviewRequest> reqPage = interviewService.searchRequest(req, uReq, PageRequest.of(currentPage - 1, pageSize));

        session.setAttribute(SESSION_FORM_ID, req);

        model.addAttribute("page", reqPage);
        model.addAttribute("url", "/interview/pagenate");
        model.addAttribute("interview_search_request", req);
        model.addAttribute("searchDone", 1);
        model.addAttribute("loginUser", uReq);

        return "interview/list";
    }

    /**
     * 面談結果検索結果CSVダウンロード
     * @param model モデル
     * @param req 検索条件
     * @param page ページ番号
     * @param size 1ページ表示件数
     * @return 面談結果検索画面
     */
    @RequestMapping(value = "/interview/search", params = "download", method = RequestMethod.POST)
    public ResponseEntity<byte[]> download(Model model
            , @ModelAttribute("interview_search_request") InterviewSearchRequest req
            , HttpServletResponse response) throws IOException {
        UserRequest uReq = userService.getLoginAccount();
        List<InterviewCSV> csvList = interviewService.searchCSV(req, uReq);

        StringWriter writer = new StringWriter();
        try {
            StatefulBeanToCsv<InterviewCSV> beanToCsv = new StatefulBeanToCsvBuilder<InterviewCSV>(writer).build();
            beanToCsv.write(csvList);
        } catch (CsvDataTypeMismatchException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "text/csv; charset=shift_jis");
        String filename = Utils.getYYYYMMDDHHmmssFromDate(new Date()) + ".csv";
        header.setContentDispositionFormData("filename", filename);
        StringBuffer csv = writer.getBuffer();
        csv.insert(0, Utils.interviewCSVHeaderString());
        return new ResponseEntity<>(csv.toString().getBytes("MS932"), header, HttpStatus.OK);
    }


    /**
     * 従業員情報、過去面談結果表示
     * @param model モデル
     * @param req 面談結果
     * @param result バリデーションチェック結果
     * @return 面談結果新規登録画面
     */
    @RequestMapping(value = "/interview/submit", params = "info", method = RequestMethod.POST)
    public String info(Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        if (!result.hasErrors()) {
            EmployeeRequest employee = employeeService.findOneRequestFromID(req.getCompanyID(), req.getEmployeeCode());
            Company company = companyService.findOne(req.getCompanyIDLong());
            List<InterviewRequest> past_list = interviewService.searchRequestFromKey(req.getCompanyIDLong(), req.getEmployeeCode(), Consts.PASTINTERVIEW_NUM);

            req.setCompanyName(company.getName());
            req.setEmployee(employee);
            req.setPastInterviews(past_list);
        }

        model.addAttribute("interview_request", req);
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "interview/add";
    }


    /**
     * 面談結果新規登録画面表示
     * @param model Model
     * @return 面談結果新規登録画面
     */
    @RequestMapping(value = "/interview/add", method = RequestMethod.GET)
    public String add(Model model) {
        InterviewRequest req = new InterviewRequest();
        UserRequest uReq = userService.getLoginAccount();

        if (uReq.getUserRoleCode() != Consts.USERROLECODE_ADMIN) {
            req.setCompanyID(uReq.getCompanyID());
            Company company = companyService.findOne(req.getCompanyIDLong());
            req.setCompanyName(company.getName());
        }

        model.addAttribute("interview_request", req);
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "interview/add";
    }

    /**
     * 面談結果新規登録
     * @param model モデル
     * @param req 面談結果
     * @param result バリデーションチェック結果
     * @return トップ画面(更新成功)／面談結果新規登録画面(更新失敗)
     */
    @RequestMapping(value = "/interview/submit", params = "create", method = RequestMethod.POST)
    public String create(RedirectAttributes attributes, Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        UserRequest uReq = userService.getLoginAccount();

        if (!result.hasErrors()) {
            interviewService.create(req);
            attributes.addFlashAttribute("loginUser", uReq);

            return "redirect:/";
        }
        else {
            EmployeeRequest employee = employeeService.findOneRequestFromID(req.getCompanyID(), req.getEmployeeCode());
            Company company = companyService.findOne(req.getCompanyIDLong());
            List<InterviewRequest> past_list = interviewService.searchRequestFromKey(req.getCompanyIDLong(), req.getEmployeeCode(), Consts.PASTINTERVIEW_NUM);

            req.setCompanyName(company.getName());
            req.setEmployee(employee);
            req.setPastInterviews(past_list);

            model.addAttribute("interview_request", req);
            model.addAttribute("loginUser", uReq);

            return "interview/add";
        }
    }

    /**
     * 面談結果添付ファイルプレビュー
     * @param id 面談結果ID
     * @param filename 添付ファイル名
     * @param model モデル
     * @return プレビュー画面
     */
    @RequestMapping(value = "/interview/{id}/{filename}", method = RequestMethod.GET)
    public String preview(@PathVariable Long id, @PathVariable String filename, Model model) {
        InterviewAttach attach = interviewService.findOneAttachFromKey(id, filename);

        StringBuffer data = new StringBuffer();
        String base64;
        try {
            base64 = new String(Base64.encodeBase64(attach.getFiledata()), "ASCII");
        }
        catch (UnsupportedEncodingException e) {
            base64 = "";
        }

        // 拡張子チェック
        String url;
        if (filename.endsWith(".pdf")) {
            data.append("data:application/pdf;base64,");
            url = "interview/previewpdf";
        }
        else {
            data.append("data:image/jpeg;base64,");
            url = "interview/previewimg";
        }

        data.append(base64);

        model.addAttribute("filename", filename);
        model.addAttribute("base64image", data.toString());
        model.addAttribute("loginUser", userService.getLoginAccount());

        return url;
    }

    /**
     * 面談結果編集画面表示
     * @param id 面談結果ID
     * @param model モデル
     * @return 面談結果編集画面
     */
    @RequestMapping(value = "/interview/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("interview_request", interviewService.findOneRequest(id));
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "interview/edit";
    }

    /**
     * 面談結果更新
     * @param model モデル
     * @param req 面談結果
     * @param result バリデーションチェック結果
     * @return 面談結果検索画面(更新成功)／面談結果編集画面(更新失敗)
     */
    @RequestMapping(value = "/interview/{id}", params = "confirm", method = RequestMethod.POST)
    public String update(RedirectAttributes attributes, @PathVariable Long id, Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {

        if (!result.hasErrors()) {
            interviewService.update(id, req);
            attributes.addFlashAttribute("interview_request", req);
            attributes.addFlashAttribute("loginUser", userService.getLoginAccount());

            return "redirect:/interview/listinit";
        }
        else {
            model.addAttribute("interview_request", req);
            model.addAttribute("loginUser", userService.getLoginAccount());

            return "interview/edit";
        }
    }

    /**
     * 面談結果削除
     * @param model モデル
     * @param req 面談結果
     * @param result バリデーションチェック結果
     * @return 面談結果検索画面(削除成功)／面談結果編集画面(削除失敗)
     */
    @RequestMapping(value = "/interview/{id}", params = "delete", method = RequestMethod.POST)
    public String delte(RedirectAttributes attributes, @PathVariable Long id, Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        if (!result.hasErrors()) {
            interviewService.delete(id, req);
            attributes.addFlashAttribute("interview_request", req);
            attributes.addFlashAttribute("loginUser", userService.getLoginAccount());

            return "redirect:/interview/listinit";
        }
        else {
            model.addAttribute("interview_request", req);
            model.addAttribute("loginUser", userService.getLoginAccount());

            return "interview/edit";
        }
    }

    /**
     * 初期表示設定画面初期表示
     * @param model Model
     * @return 初期表示設定画面
     */
    @RequestMapping(value = "/interview/condinit", method = RequestMethod.GET)
    public String initCondition(Model model) {
        model.addAttribute("interview_condition_request", interviewService.getCondition(Utils.loginUsername()));
        model.addAttribute("loginUser", userService.getLoginAccount());

        return "interview/condinit";
    }

    /**
     * 面談結果検索画面初期表示
     * @param model Model
     * @return 面談結果検索画面
     */
    @RequestMapping(value = "/interview/condconfirm", method = RequestMethod.POST)
    public String confirmCondition(RedirectAttributes attributes, Model model, @ModelAttribute("interview_condition_request") InterviewConditionRequest req) {
        interviewService.saveCondition(Utils.loginUsername(), req);
        attributes.addFlashAttribute("loginUser", userService.getLoginAccount());

        return "redirect:/";
    }
}
