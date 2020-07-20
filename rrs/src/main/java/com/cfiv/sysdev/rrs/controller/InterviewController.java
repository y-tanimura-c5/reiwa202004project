package com.cfiv.sysdev.rrs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.PastInterviewRequest;
import com.cfiv.sysdev.rrs.entity.Company;
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
    @RequestMapping(value = "/interview/submit", params = "search", method = RequestMethod.POST)
    public String search(Model model, @ModelAttribute("interview_request") @Valid InterviewRequest req, BindingResult result) {
        if (!result.hasErrors()) {
            List<EmployeeRequest> emp_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeID());
            Company company = companyService.findOne(req.getCompanyIDLong());
            List<PastInterviewRequest> past_list = interviewService.searchFromID(req.getCompanyIDLong(), req.getEmployeeID());

            if (emp_list.size() >= 1) {
                req.setCompanyName(company.getName());
                req.setEmployeeFName(emp_list.get(0).getEmployeeFName());
                req.setHireYM(emp_list.get(0).getHireYM());
                req.setAdoptCode(emp_list.get(0).getAdoptCode());
                req.setSupportCode(emp_list.get(0).getSupportCode());
                req.setEmployCode(emp_list.get(0).getEmployCode());
                req.setPastInterviews(past_list);

                LogUtils.info("Count = " + past_list.size());
                for (PastInterviewRequest past : past_list) {
                    LogUtils.info("InterviewDate = " + past.getInterviewDate());
                    LogUtils.info("InterviewTime = " + past.getInterviewTime());
                    LogUtils.info("Disclose = " + past.getDisclose());
                    LogUtils.info("InterviewerComment = " + past.getInterviewerComment());
                    LogUtils.info("AdminComment = " + past.getAdminComment());
                    LogUtils.info("AttachedFilename = " + past.getAttachedFilename());
                }
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
        if (!result.hasErrors()) {
            interviewService.create(req);
        }

        model.addAttribute("interview_request", req);

        return "/interview/add";
    }

}
