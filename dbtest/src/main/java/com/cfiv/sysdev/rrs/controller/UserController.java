package com.cfiv.sysdev.rrs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.User;
import com.cfiv.sysdev.rrs.service.UserService;

/**
 * ���[�U�[��� Controller
 */
@Controller
public class UserController {

    /**
     * ���[�U�[��� Service
     */
    @Autowired
    UserService userService;

    /**
     * ���[�U�[���ꗗ��ʂ�\��
     * @param model Model
     * @return ���[�U�[���ꗗ���
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String displayList(Model model) {
        List<User> userlist = userService.searchAll();
        model.addAttribute("userlist", userlist);
        return "user/list";
    }

    /**
     * ���[�U�[�V�K�o�^��ʂ�\��
     * @param model Model
     * @return ���[�U�[���ꗗ���
     */
    @RequestMapping(value = "/user/add", method = RequestMethod.GET)
    public String displayAdd(Model model) {
        model.addAttribute("userRequest", new UserRequest());
        return "user/add";
    }

    /**
     * ���[�U�[�V�K�o�^
     * @param userRequest ���N�G�X�g�f�[�^
     * @param model Model
     * @return ���[�U�[���ꗗ���
     */
    @RequestMapping(value="/user/create", method=RequestMethod.POST)
    public String create(@ModelAttribute UserRequest userRequest, Model model) {
        // ���[�U�[���̓o�^
        userService.create(userRequest);
        return "redirect:/user/list";
    }
}