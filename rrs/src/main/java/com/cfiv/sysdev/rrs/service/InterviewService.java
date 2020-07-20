package com.cfiv.sysdev.rrs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.PastInterviewRequest;
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
    /**
     * データベースエンティティ管理
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 面談結果 Repository
     */
    @Autowired
    private InterviewResultRepository interviewResultRepository;

    /**
     * 面談内容 Repository
     */
    @Autowired
    private InterviewContentRepository interviewContentRepository;

    /**
     * 企業コード、社員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeID 社員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<PastInterviewRequest> searchFromID(Long companyID, String employeeID) {
        List<PastInterviewRequest> req_list = new ArrayList<PastInterviewRequest>();

        String sql = "FROM InterviewResult r WHERE r.companyID = :companyID AND r.employeeID = :employeeID ORDER BY r.interviewDateTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("companyID", companyID);
        query.setParameter("employeeID", employeeID);
        query.setMaxResults(3);

        List<InterviewResult> result_list = (List<InterviewResult>) query.getResultList();
        for (InterviewResult result : result_list) {
            PastInterviewRequest req = new PastInterviewRequest();
            req.setInterviewDate(result.getInterviewDateTimeString());
            req.setInterviewTime(result.getInterviewTimeString());
            req.setDisclose(result.getDiscloseString());
            req.setInterviewerComment(result.getInterviewerComment());
            req.setAdminComment(result.getAdminComment());
            req.setAttachedFilename(result.getFilename());
            req_list.add(req);
        }

        return req_list;
    }

    /**
     * 面談結果新規登録
     * @param req 面談結果
     */
    @Transactional
    public void create(InterviewRequest req) {
        Date now = new Date();

        InterviewResult result = new InterviewResult();
        result.setCompanyID(req.getCompanyIDLong());
        result.setEmployeeID(req.getEmployeeID());
        result.setInterviewDateTime(req.getInterviewDateDate());
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

        try {
            if (req.getAttachedFile() != null && !req.getAttachedFile().isEmpty()) {
                result.setFilename(req.getAttachedFile().getOriginalFilename());
                result.setFiledata(req.getAttachedFile().getBytes());
            }
        }
        catch (IOException e) {
            LogUtils.error("添付ファイルのアクセスに失敗しました。");
        }

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
