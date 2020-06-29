package com.cfiv.sysdev.rrs.form;

import javax.validation.constraints.NotBlank;

public class InputForm {

    @NotBlank(message = "text1‚ð“ü—Í‚µ‚Ä‚­‚¾‚³‚¢")
    String text1;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }
}