package com.cfiv.sysdev.rrs.controller;

import java.io.IOException;
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

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeFileRequest;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.entity.Employee;
import com.cfiv.sysdev.rrs.entity.EmployeeCSV;
import com.cfiv.sysdev.rrs.service.EmployeeService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

/**
 * è]ã∆àıèÓïÒ Controller
 */
@Controller
public class EmployeeController {

    /**
     * è]ã∆àıèÓïÒ Service
     */
    @Autowired
    EmployeeService employeeService;

    /**
     * è]ã∆àıèÓïÒåüçıâÊñ Çï\é¶
     * @param model Model
     * @return è]ã∆àıèÓïÒåüçıâÊñ 
     */
    @RequestMapping(value = "/employee/bulkregist", method = RequestMethod.GET)
    public String bulkRegist(Model model) {
        model.addAttribute("employeeFileRequest", new EmployeeFileRequest());

        return "employee/bulkregist";
    }

    /**
     * è]ã∆àıèÓïÒåüçıâÊñ Çï\é¶
     * @param model Model
     * @return è]ã∆àıèÓïÒåüçıâÊñ 
     */
    @RequestMapping(value = "/employee/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        List<EmployeeRequest> req_list = new ArrayList<EmployeeRequest>();

        List<Employee> employee_list = employeeService.searchAll();

        for (Employee employee : employee_list) {
            req_list.add(new EmployeeRequest(employee.getIdString(1), employee.getCompanyIDString(1), employee.getEmployeeID(),
                    employee.getEmployeeFName(), employee.getHireYM(), employee.getAdoptCodeString(), employee.getSupportCodeString(),
                    employee.getEmployCodeString()));
        }

        model.addAttribute("employee_request_list", req_list);

        return "company/list";
    }

    /**
     * è]ã∆àıàÍäáìoò^âÊñ Çï\é¶
     * @param model Model
     * @return è]ã∆àıàÍäáìoò^âÊñ 
     */
    @RequestMapping(value = "/employee/upload", method = RequestMethod.POST)
    public String upload(@Valid EmployeeFileRequest employeeFileRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "employee/bulkregist";
        }

        List<EmployeeCSV> items = null;
        try {
            Reader reader = new StringReader(new String(employeeFileRequest.getEmployeeFile().getBytes()));
            CsvToBean<EmployeeCSV> csvToBean = new CsvToBeanBuilder<EmployeeCSV>(reader).withType(EmployeeCSV.class).build();
            items = csvToBean.parse();
        }
        catch (IllegalStateException | IOException e) {
            return "employee/bulkregist";
        }

        int row = 2;
        if (items != null) {
            LogUtils.info("items = ");
            for (EmployeeCSV item : items) {
                LogUtils.info("  " + item.toString());
                item.check();
                LogUtils.info("  " + item.isResult());
                if (!item.isResult()) {
                    LogUtils.info("  " + item.getReason());
                    model.addAttribute("check_result", "ÉGÉâÅ[Ç†ÇË");
                    model.addAttribute("error_row", row);
                    model.addAttribute("error_reazon", item.getReason());
                    break;
                }
                row ++;
            }
        }

        return "employee/bulkregist";
    }

    @RequestMapping(value = "/employee/confirm", method = RequestMethod.POST)
    public String confirm(@ModelAttribute EmployeeRequest req) {
        return "redirect:/";
    }

    @RequestMapping(value = "/employee/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findOne(id);
        model.addAttribute("employee_request", new EmployeeRequest(employee.getIdString(1), employee.getCompanyIDString(1),
                employee.getEmployeeID(), employee.getEmployeeFName(), employee.getHireYM(), employee.getAdoptCodeString(),
                employee.getSupportCodeString(), employee.getEmployCodeString()));

        return "employee/edit";
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute EmployeeRequest req) {
        employeeService.save(id, req);

        return "redirect:/employee/list";
    }
}
