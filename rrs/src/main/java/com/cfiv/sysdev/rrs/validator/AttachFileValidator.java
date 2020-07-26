package com.cfiv.sysdev.rrs.validator;

import java.io.IOException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.annotation.AttachedFile;

public class AttachFileValidator implements ConstraintValidator<AttachedFile, MultipartFile> {
    @Override
    public void initialize(AttachedFile constraint) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String message = "";

        if (multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
            // 拡張子チェック
            if (!multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpg") &&
                    !multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpeg") &&
                    !multipartFile.getOriginalFilename().toLowerCase().endsWith(".jpe") &&
                    !multipartFile.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                message = "登録可能な添付ファイルはJPGファイル、PDFファイルのみです。＜添付ファイル：" + multipartFile.getOriginalFilename() + "＞";
            }

            try {
                byte[] filedata = multipartFile.getBytes();
                if (multipartFile.getOriginalFilename().toLowerCase().endsWith(".pdf") && filedata.length > 1024 * 1024) {
                    message = "1Mバイトを超えるサイズのPDFファイルは登録できません。";
                }
            }
            catch (IOException e) {
                message = "添付ファイルのアクセスに失敗しました。";
            }
        }

        if (!message.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;

        }

        return true;
    }
}
