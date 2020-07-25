package com.cfiv.sysdev.rrs.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cfiv.sysdev.rrs.Const;
import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.entity.InterviewResult;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.EmployeeService;
import com.cfiv.sysdev.rrs.service.InterviewService;

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
     * 従業員情報検索画面初期表示
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/interview/listinit", method = RequestMethod.GET)
    public String init(Model model) {
        model.addAttribute("interview_request_list", new ArrayList<InterviewRequest>());
        model.addAttribute("interview_request_size", 0);
        model.addAttribute("interview_search_request", new InterviewSearchRequest());

        return "interview/list";
    }

    /**
     * 従業員情報検索画面
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/interview/list", method = RequestMethod.GET)
    public String list(Model model) {
        return "interview/list";
    }

    /**
     * 面談結果検索結果表示
     * @param redirectAttributes リダイレクト情報
     * @param model モデル
     * @param req 検索条件
     * @return 面談結果検索画面
     */
    @RequestMapping(value = "/interview/search", method = RequestMethod.POST)
    public String search(RedirectAttributes redirectAttributes, Model model, @ModelAttribute("interview_search_request") InterviewSearchRequest req) {
        List<InterviewRequest> req_list = interviewService.search(req);
//        List<InterviewRequest> req_list = interviewService.searchFromID(req.getCompanyIDLong(), req.getEmployeeCode(), 0);

/*
        LogUtils.info("companyID = " + req.getCompanyID());
        LogUtils.info("employeeCode = " + req.getEmployeeCode());
        LogUtils.info("interviewDateStart = " + req.getInterviewDateStart());
        LogUtils.info("interviewDateEnd = " + req.getInterviewDateEnd());
        LogUtils.info("interviewDateLastCode = " + req.getInterviewDateLastCode());
        LogUtils.info("interviewDateCode = " + req.getInterviewDateCode());

        for (int i = 0; i < req.getInterviewTimeCheckedList().size(); i ++) {
            LogUtils.info("interviewTimeChecked[" + i + "] = " + req.getInterviewTimeCheckedList().get(i));
        }
        for (int i = 0; i < req.getDiscloseCheckedList().size(); i ++) {
            LogUtils.info("discloseChecked[" + i + "] = " + req.getDiscloseCheckedList().get(i));
        }

        for (int i = 0; i < req.getContentJobCheckedList().size(); i ++) {
            LogUtils.info("contentJobChecked[" + i + "] = " + req.getContentJobCheckedList().get(i));
        }
        for (int i = 0; i < req.getContentJobMemos().size(); i ++) {
            LogUtils.info("contentJobMemos[" + i + "] = " + req.getContentJobMemos().get(i));
        }
        for (int i = 0; i < req.getContentPrivateMemos().size(); i ++) {
            LogUtils.info("contentPrivateMemos[" + i + "] = " + req.getContentPrivateMemos().get(i));
        }
        for (int i = 0; i < req.getInterviewerCommentMemos().size(); i ++) {
            LogUtils.info("interviewerCommentMemos[" + i + "] = " + req.getInterviewerCommentMemos().get(i));
        }
        for (int i = 0; i < req.getAdminCommentMemos().size(); i ++) {
            LogUtils.info("adminCommentMemos[" + i + "] = " + req.getAdminCommentMemos().get(i));
        }

        LogUtils.info("hireLengthStartCode = " + req.getHireLengthStartCode());
        LogUtils.info("hireLengthEndCode = " + req.getHireLengthEndCode());

        for (int i = 0; i < req.getAdoptCheckedList().size(); i ++) {
            LogUtils.info("adoptChecked[" + i + "] = " + req.getAdoptCheckedList().get(i));
        }
        for (int i = 0; i < req.getSupportCheckedList().size(); i ++) {
            LogUtils.info("supportChecked[" + i + "] = " + req.getSupportCheckedList().get(i));
        }
        for (int i = 0; i < req.getEmployCheckedList().size(); i ++) {
            LogUtils.info("employChecked[" + i + "] = " + req.getEmployCheckedList().get(i));
        }
*/

        redirectAttributes.addFlashAttribute("interview_search_request", req);
        redirectAttributes.addFlashAttribute("interview_request_list", req_list);
        redirectAttributes.addFlashAttribute("interview_request_size", req_list.size());

        return "redirect:/interview/list/#search_result";
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
            EmployeeRequest employee = employeeService.findOneFromID(req.getCompanyID(), req.getEmployeeCode());
            Company company = companyService.findOne(req.getCompanyIDLong());
            List<InterviewRequest> past_list = interviewService.searchRequestFromKey(req.getCompanyIDLong(), req.getEmployeeCode(), Const.PASTINTERVIEW_NUM);

            req.setCompanyName(company.getName());
            req.setEmployee(employee);
            req.setPastInterviews(past_list);

//                LogUtils.info("Count = " + past_list.size());
//                for (InterviewRequest past : past_list) {
//                    LogUtils.info("InterviewDate = " + past.getInterviewDate());
//                    LogUtils.info("InterviewTime = " + past.getInterviewTime());
//                    LogUtils.info("Disclose = " + past.getDisclose());
//                    LogUtils.info("InterviewerComment = " + past.getInterviewerComment());
//                    LogUtils.info("AdminComment = " + past.getAdminComment());
//                    LogUtils.info("AttachedFilename = " + past.getAttachedFilename());
//                }
        }

        model.addAttribute("interview_request", req);

        return "/interview/add";
    }


    /**
     * 面談結果新規登録画面表示
     * @param model Model
     * @return 面談結果新規登録画面
     */
    @RequestMapping(value = "/interview/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("interview_request", new InterviewRequest());

        return "/interview/add";
    }

    /**
     * 面談結果新規登録
     * @param model モデル
     * @param req 面談結果
     * @param result バリデーションチェック結果
     * @return トップ画面(更新成功)／面談結果新規登録画面(更新失敗)
     */
    @RequestMapping(value = "/interview/submit", params = "create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        model.addAttribute("interview_request", req);

        if (!result.hasErrors()) {
            interviewService.create(req);

            return "redirect:/";
        }
        else {
            return "/interview/add";
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
        LogUtils.info("id = " + id + ", filename = " + filename);

        InterviewResult result = interviewService.findOne(id);

        StringBuffer data = new StringBuffer();
        String base64;
        try {
            base64 = new String(Base64.encodeBase64(result.getFiledata()), "ASCII");
        }
        catch (UnsupportedEncodingException e) {
            base64 = "";
        }

        // 拡張子チェック
        String url;
        if (filename.endsWith(".pdf")) {
            data.append("data:application/pdf;base64,");
            url = "/interview/previewpdf";
        }
        else {
            data.append("data:image/jpeg;base64,");
            url = "/interview/previewimg";
        }

        data.append(base64);

        model.addAttribute("filename", filename);
        model.addAttribute("base64image", data.toString());

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

        return "interview/edit";
    }

    /**
     * 面談結果更新
     * @param model モデル
     * @param req 面談結果
     * @param result バリデーションチェック結果
     * @return 面談結果検索画面(更新成功)／面談結果編集画面(更新失敗)
     */
    @RequestMapping(value = "/interview/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        model.addAttribute("interview_request", req);

        if (!result.hasErrors()) {
            interviewService.update(id, req);

            return "redirect:/interview/listinit";
        }
        else {
            return "/interview/edit";
        }
    }
}
