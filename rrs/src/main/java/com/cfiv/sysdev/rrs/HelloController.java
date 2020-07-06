package com.cfiv.sysdev.rrs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cfiv.sysdev.rrs.repository.EmployeeRepository;

@Controller
//@RequestMapping(value="/")
public class HelloController {
    @Autowired
    EmployeeRepository empRepository;

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index(Model model) {
//      List<Employee> emplist=empRepository.findAll();
//      model.addAttribute("employeelist", emplist);
//
//      return "index";
//    }

//    @RequestMapping(value="/", method=RequestMethod.GET)
//    public ModelAndView index(ModelAndView mav) {
//        mav.setViewName("index");
//        mav.addObject("msg", "input your name :");
//        return mav;
//    }

    @GetMapping
    public String index(Model model) {
        return "index";
    }

//    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("name")String name,
            ModelAndView mav) {
        mav.setViewName("index");
        mav.addObject("msg", "Hello " + name + " !");
        mav.addObject("value", name);
        return mav;
    }
}