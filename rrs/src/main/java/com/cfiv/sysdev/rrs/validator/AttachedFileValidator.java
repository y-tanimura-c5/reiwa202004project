package com.cfiv.sysdev.rrs.validator;

import java.io.IOException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.annotation.AttachedFile;

public class AttachedFileValidator implements ConstraintValidator<AttachedFile, MultipartFile> {
    @Override
    public void initialize(AttachedFile constraint) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String message = "";

        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            // 拡張子チェック
            if (!multipartFile.getOriginalFilename().endsWith(".jpg") &&
                    !multipartFile.getOriginalFilename().endsWith(".jpeg") &&
                    !multipartFile.getOriginalFilename().endsWith(".jpe") &&
                    !multipartFile.getOriginalFilename().endsWith(".png") &&
                    !multipartFile.getOriginalFilename().endsWith(".pdf")) {
                message = "登録可能な添付ファイルはJPGファイル、PNGファイル、PDFファイルのみです。";
           }

            try {
                byte[] filedata = multipartFile.getBytes();
                if (filedata.length > 1024 * 1024) {
                    message = "1Mバイトを超えるサイズの添付ファイルは登録できません。";
                }
            }
            catch (IOException e) {
                message = "添付ファイルのアクセスに失敗しました。";
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
