package com.cfiv.sysdev.rrs.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.entity.InterviewContent;
import com.cfiv.sysdev.rrs.entity.InterviewResult;
import com.cfiv.sysdev.rrs.repository.InterviewContentRepository;
import com.cfiv.sysdev.rrs.repository.InterviewResultRepository;

/**
 * 面談結果 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class InterviewService {
    @Autowired
    private InterviewResultRepository interviewResultRepository;

    @Autowired
    private InterviewContentRepository interviewContentRepository;

    /**
     * 面談結果新規登録
     * @param req 面談結果
     */
    @Transactional
    public void create(InterviewRequest req) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        InterviewResult result = new InterviewResult();

        result.setCompanyID(req.getCompanyIDLong());
        result.setEmployeeID(req.getEmployeeID());
        try {
            result.setInterviewDateTime(dateFormat.parse(req.getInterviewDate()));
        }
        catch (ParseException e) {
            result.setInterviewDateTime(new Date(0));
        }
        result.setRefinerUserID("user");
        result.setInterviewTimeCode(req.getInterviewTimeCode());
        result.setInterviewerComment(req.getInterviewerComment());
        result.setDiscloseCode(req.getDiscloseCode());
        result.setAdminComment(req.getAdminComment());
        result.setDeleted(false);
        result.setRegistUser("user");
        result.setRegistTime(now);
        result.setUpdateUser("user");
        result.setUpdateTime(now);

        result = interviewResultRepository.save(result);

        Map<Integer, String> contentJobItems = new LinkedHashMap<Integer, String>();
        for (int key : req.getContentJobCheckItems().keySet()) {
            if (req.containsContentJobChecked(key) || !req.getContentJobMemos().get(key).isEmpty()) {
                contentJobItems.put(key, req.getContentJobMemos().get(key));
            }
        }

        Map<Integer, String> contentPrivateItems = new LinkedHashMap<Integer, String>();
        for (int key : req.getContentPrivateCheckItems().keySet()) {
            if (!req.getContentPrivateMemos().get(key).isEmpty()) {
                contentPrivateItems.put(key, req.getContentPrivateMemos().get(key));
            }
        }

        LogUtils.info("contentJobItems:");
        for (int key : contentJobItems.keySet()) {
            LogUtils.info("key = " + key + ", value = " + contentJobItems.get(key));

            InterviewContent content = new InterviewContent();
            content.setResult(result);
            content.setContentKind(0);
            content.setContentCode(key);
            content.setContentComment(contentJobItems.get(key));
            content.setDeleted(false);
            content.setRegistUser("user");
            content.setRegistTime(now);
            content.setUpdateUser("user");
            content.setUpdateTime(now);
            interviewContentRepository.save(content);
        }
        LogUtils.info("contentPrivateItems:");
        for (int key : contentPrivateItems.keySet()) {
            LogUtils.info("key = " + key + ", value = " + contentPrivateItems.get(key));

            InterviewContent content = new InterviewContent();
            content.setResult(result);
            content.setContentKind(1);
            content.setContentCode(key);
            content.setContentComment(contentPrivateItems.get(key));
            content.setDeleted(false);
            content.setRegistUser("user");
            content.setRegistTime(now);
            content.setUpdateUser("user");
            content.setUpdateTime(now);
            interviewContentRepository.save(content);
        }
    }

}
