package com.cfiv.sysdev.rrs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.service.CompanyService;
import com.cfiv.sysdev.rrs.service.EmployeeService;

/**
 * 面談結果 Controller
 */
@Controller
public class InterviewController {

    /**
     * 面談結果 Service
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
        model.addAttribute("interview_search_request", new InterviewSearchRequest());
        model.addAttribute("interview_request", new InterviewRequest());

        return "/interview/add";
    }

    /**
     * 従業員情報、過去面談結果表示
     * @param model Model
     * @return 面談結果新規登録画面
     */
    @RequestMapping(value = "/interview/search", method = RequestMethod.POST)
    public String search(Model model, @ModelAttribute("interview_search_request") InterviewSearchRequest req) {
        LogUtils.info("req.getCompanyID(), = " + req.getCompanyID() + ", req.getEmployeeID() = " + req.getEmployeeID());

        if (!req.getCompanyID().isEmpty() || !req.getEmployeeID().isEmpty()){
            List<EmployeeRequest> req_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeID());
            Company company = companyService.findOne(req.getCompanyIDLong());

            if (req_list.size() >= 1) {
                req.setCompanyName(company.getName());
                req.setEmployeeFName(req_list.get(0).getEmployeeFName());
                req.setHireYM(req_list.get(0).getHireYM());
                req.setAdoptCode(req_list.get(0).getAdoptCode());
                req.setSupportCode(req_list.get(0).getSupportCode());
                req.setEmployCode(req_list.get(0).getEmployCode());
            }
        }

        model.addAttribute("interview_search_request", req);
        model.addAttribute("interview_request", new InterviewRequest());

        return "/interview/add";
    }

    /**
     * 面談結果新規登録
     * @param model Model
     * @return 面談結果新規登録画面
     */
    @RequestMapping(value = "/interview/create", method = RequestMethod.POST)
    public String create(Model model, @ModelAttribute("interview_request") InterviewRequest ireq, @ModelAttribute("interview_search_request") InterviewSearchRequest sreq) {
        LogUtils.info("InterviewDate = " + ireq.getInterviewDate());
        LogUtils.info("InterviewTimeCode = " + ireq.getInterviewTimeCode());
        LogUtils.info("DiscloseCode = " + ireq.getDiscloseCode());
        LogUtils.info("InterviewerComment = " + ireq.getInterviewerComment());
        LogUtils.info("AdminComment = " + ireq.getAdminComment());
        LogUtils.info("Check/Memo1 = " + ireq.getInterviewContentCheck1() + ", "+ ireq.getInterviewContentMemo1());
        LogUtils.info("Check/Memo2 = " + ireq.getInterviewContentCheck2() + ", "+ ireq.getInterviewContentMemo2());
        LogUtils.info("Check/Memo3 = " + ireq.getInterviewContentCheck3() + ", "+ ireq.getInterviewContentMemo3());
        LogUtils.info("Check/Memo4 = " + ireq.getInterviewContentCheck4() + ", "+ ireq.getInterviewContentMemo4());
        LogUtils.info("Check/Memo5 = " + ireq.getInterviewContentCheck5() + ", "+ ireq.getInterviewContentMemo5());
        LogUtils.info("Check/Memo6 = " + ireq.getInterviewContentCheck6() + ", "+ ireq.getInterviewContentMemo6());
        LogUtils.info("Check/Memo7 = " + ireq.getInterviewContentCheck7() + ", "+ ireq.getInterviewContentMemo7());
        LogUtils.info("Check/Memo8 = " + ireq.getInterviewContentCheck8() + ", "+ ireq.getInterviewContentMemo8());
        LogUtils.info("Check/Memo9 = " + ireq.getInterviewContentCheck9() + ", "+ ireq.getInterviewContentMemo9());

        model.addAttribute("interview_request", ireq);
        model.addAttribute("interview_search_request", sreq);

        return "/interview/add";
    }

}
