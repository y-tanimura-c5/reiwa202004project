package com.cfiv.sysdev.rrs.validator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.annotation.FileRequired;
import com.cfiv.sysdev.rrs.dto.EmployeeCSV;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class FileRequiredValidator implements ConstraintValidator<FileRequired, MultipartFile> {
    @Override
    public void initialize(FileRequired constraint) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String message = "";

        if (multipartFile == null || multipartFile.getOriginalFilename().isEmpty()) {
            message = "ファイルの取得に失敗しました。";
        }

        List<EmployeeCSV> items = null;
        try {
            Reader reader = new StringReader(new String(multipartFile.getBytes()));
            CsvToBean<EmployeeCSV> csvToBean = new CsvToBeanBuilder<EmployeeCSV>(reader).withType(EmployeeCSV.class).build();
            items = csvToBean.parse();
        }
        catch (IllegalStateException | IOException e) {
            message = "CSVファイルではない可能性があります。";
        }

        int row = 2;
        if (items != null) {
            for (EmployeeCSV item : items) {
                item.check();
                if (!item.isResult()) {
                    message = row + "行目："+ item.getReason();
                    break;
                }
                row ++;
            }
        }

        if (!message.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;

        }

        return true;
    }
}