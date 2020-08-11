package com.cfiv.sysdev.rrs.validator;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.annotation.FileRequired;
import com.cfiv.sysdev.rrs.csv.EmployeeCSV;
import com.cfiv.sysdev.rrs.request.UserRequest;
import com.cfiv.sysdev.rrs.service.UserService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class FileRequiredValidator implements ConstraintValidator<FileRequired, MultipartFile> {

    /**
     * ユーザー情報 Service
     */
    @Autowired
    UserService userService;

    @Override
    public void initialize(FileRequired constraint) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String message = "";
        UserRequest uReq = userService.getLoginAccount();

        if (multipartFile == null || multipartFile.getOriginalFilename().isEmpty()) {
            message = "ファイルの取得に失敗しました。";
        }
        else {
            List<EmployeeCSV> items = null;
            try {
                String csvStr = new String(multipartFile.getBytes(), "MS932");
                Reader reader = new StringReader(csvStr);
                CsvToBean<EmployeeCSV> csvToBean = new CsvToBeanBuilder<EmployeeCSV>(reader).withType(EmployeeCSV.class).build();
                items = csvToBean.parse();
            }
            catch (IllegalStateException | IOException e) {
                message = "CSVファイルではない可能性があります。";
            }
            catch (Exception e) {
                message = "CSVファイルのフォーマットまたは文字コードが異なります。";
            }

            int row = 2;
            if (items != null) {
                for (EmployeeCSV item : items) {
                    if (uReq.getUserRoleCode() == Consts.USERROLECODE_ADMIN) {
                        item.check(null);
                    }
                    else {
                        item.check(uReq.getCompanyID());
                    }

                    if (!item.isResult()) {
                        message = row + "行目："+ item.getReason();
                        break;
                    }
                    row ++;
                }
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