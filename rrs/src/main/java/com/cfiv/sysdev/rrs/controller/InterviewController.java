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

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
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
    @RequestMapping(value = "/interview/list", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("interview_request_list", new ArrayList<InterviewRequest>());
        model.addAttribute("interview_request", new InterviewRequest());

        return "interview/list";
    }

    /**
     * 従業員情報検索画面検索結果表示
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/interview/search", method = RequestMethod.POST)
    public String search(Model model, @ModelAttribute("interview_request") InterviewRequest req) {
        List<InterviewRequest> req_list = interviewService.search();
//        List<InterviewRequest> req_list = interviewService.searchFromID(req.getCompanyIDLong(), req.getEmployeeID(), 0);

        model.addAttribute("interview_request_list", req_list);
        model.addAttribute("interview_request", req);

        return "/interview/list";
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
     * 従業員情報、過去面談結果表示
     * @param model Model
     * @return 面談結果新規登録画面
     */
    @RequestMapping(value = "/interview/submit", params = "info", method = RequestMethod.POST)
    public String info(Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        if (!result.hasErrors()) {
            List<EmployeeRequest> emp_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeID());
            Company company = companyService.findOne(req.getCompanyIDLong());
            List<InterviewRequest> past_list = interviewService.searchFromID(req.getCompanyIDLong(), req.getEmployeeID(), 3);

            if (emp_list.size() >= 1) {
                req.setCompanyName(company.getName());
                req.setEmployeeFName(emp_list.get(0).getEmployeeFName());
                req.setHireYM(emp_list.get(0).getHireYM());
                req.setAdoptCode(emp_list.get(0).getAdoptCode());
                req.setSupportCode(emp_list.get(0).getSupportCode());
                req.setEmployCode(emp_list.get(0).getEmployCode());
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
        }

        model.addAttribute("interview_request", req);

        return "/interview/add";
    }

    /**
     * 面談結果新規登録
     * @param model Model
     * @return 面談結果新規登録画面
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
}
