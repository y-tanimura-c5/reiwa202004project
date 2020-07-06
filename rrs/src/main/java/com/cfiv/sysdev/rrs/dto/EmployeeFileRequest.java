package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.annotation.FileRequired;

import lombok.Data;

@Data
public class EmployeeFileRequest implements Serializable {

    /**
     * �]�ƈ����t�@�C��
     */
    @FileRequired
    private MultipartFile employeeFile;
}