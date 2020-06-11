package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
    @Autowired
    EmployeeRepository empRepository;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
      List<Employee> emplist=empRepository.findAll();
      model.addAttribute("employeelist", emplist);
      return "index";
    }

//    @RequestMapping(value="/", method=RequestMethod.GET)
//    public ModelAndView index(ModelAndView mav) {
//        mav.setViewName("index");
//        mav.addObject("msg", "input your name :");    // 表示メッセージ
//        return mav;
//    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    public ModelAndView send(@RequestParam("name")String name,
            ModelAndView mav) {
        mav.setViewName("index");
        mav.addObject("msg", "Hello " + name + " !");    // 表示メッセージ
        mav.addObject("value", name);                    // 入力テキストに入力値を表示
        return mav;
    }
}