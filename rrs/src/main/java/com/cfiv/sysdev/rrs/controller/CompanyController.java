package com.cfiv.sysdev.rrs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.dto.CompanyRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.service.CompanyService;

/**
 * Šé‹Æî•ñ Controller
 */
@Controller
public class CompanyController {

    /**
     * Šé‹Æî•ñ Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * Šé‹Æî•ñˆê——‰æ–Ê‚ğ•\¦
     * @param model Model
     * @return Šé‹Æî•ñˆê——‰æ–Ê
     */
    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        List<CompanyRequest> req_list = new ArrayList<CompanyRequest>();

        List<Company> company_list = companyService.searchAll();

        for (Company company : company_list) {
            req_list.add(new CompanyRequest(company.getIdString(4), company.getName(), company.getEnabledString()));
        }

        model.addAttribute("company_request_list", req_list);

        return "company/list";
    }

    /**
     * Šé‹ÆV‹K“o˜^‰æ–Ê‚ğ•\¦
     * @param model Model
     * @return Šé‹Æî•ñˆê——‰æ–Ê
     */
    @RequestMapping(value = "/company/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("company_request", new CompanyRequest("", "", "—LŒø"));
        return "company/add";
    }

    @RequestMapping(value = "/company/{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        Company company = companyService.findOne(id);
        model.addAttribute("company_request", new CompanyRequest(company.getIdString(4), company.getName(), company.getEnabledString()));
        return "company/edit";
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.POST)
    public String update(@PathVariable Long id, @ModelAttribute CompanyRequest req) {
        companyService.save(id, req);
        return "redirect:/company/list";
    }

    /**
     * Šé‹Æî•ñV‹K“o˜^
     * @param req ƒŠƒNƒGƒXƒgƒf[ƒ^
     * @param model Model
     * @return Šé‹Æî•ñˆê——‰æ–Ê
     */
    @RequestMapping(value = "/company/create", method = RequestMethod.POST)
    public String create(@ModelAttribute CompanyRequest req, Model model) {
        // Šé‹Æî•ñ‚Ì“o˜^
        companyService.create(req);
        return "redirect:/company/list";
    }
}
