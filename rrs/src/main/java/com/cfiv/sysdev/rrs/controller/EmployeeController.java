package com.cfiv.sysdev.rrs.controller;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.dto.EmployeeCSV;
import com.cfiv.sysdev.rrs.dto.EmployeeFileRequest;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.service.EmployeeService;
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
     * 従業員情報検索画面初期表示
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/employee/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        model.addAttribute("employee_request_list", new ArrayList<EmployeeRequest>());
        model.addAttribute("employee_request", new EmployeeRequest());

        return "employee/list";
    }

    /**
     * 従業員情報検索画面検索結果表示
     * @param model Model
     * @return 従業員情報検索画面
     */
    @RequestMapping(value = "/employee/search", method = RequestMethod.POST)
    public String search(Model model, @ModelAttribute("employee_request") EmployeeRequest req) {
        List<EmployeeRequest> req_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeID());

        model.addAttribute("employee_request_list", req_list);
        model.addAttribute("employee_request", req);

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

        return "employee/bulkregist";
    }

    /**
     * 従業員一括登録情報取り込み
     * @param model Model
     * @return 従業員一括登録画面
     */
    @RequestMapping(value = "/employee/upload", method = RequestMethod.POST)
    public String upload(@Valid EmployeeFileRequest employeeFileRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
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
            }
        }

        model.addAttribute("regist_result", true);

        return "employee/bulkregist";
    }

    @RequestMapping(value = "/employee/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("employee_request", employeeService.findOneRequest(id));

        return "employee/edit";
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute EmployeeRequest req) {
        employeeService.save(id, req);

        return "redirect:/employee/list";
    }
}
