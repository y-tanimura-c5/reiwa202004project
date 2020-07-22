package com.cfiv.sysdev.rrs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
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
    /**
     * データベースエンティティ管理
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 従業員情報 Service
     */
    @Autowired
    EmployeeService employeeService;

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
     * IDでの面談結果検索
     * @param id ID
     * @return 検索結果(InterviewResult)
     */
    public InterviewResult findOne(Long id) {
        Optional<InterviewResult> opt = interviewResultRepository.findById(id);

        try {
            return opt.get();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * 企業コード、社員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeID 社員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewRequest> search() {
        List<InterviewRequest> req_list = new ArrayList<InterviewRequest>();

//        String sql = "FROM InterviewResult r";
        String sql = "SELECT DISTINCT r FROM InterviewResult r LEFT JOIN r.interviewContents ORDER BY r.interviewDateTime DESC";
        Query query = entityManager.createQuery(sql);
        List<InterviewResult> result_list = (List<InterviewResult>) query.getResultList();

        LogUtils.info("result_list.size = " + result_list.size());

        for (InterviewResult result : result_list) {
            InterviewRequest req = new InterviewRequest();

            req.setIdFromNumeric(result.getId(), 1);
            req.setCompanyIDFromNumeric(result.getCompanyID(), 4);
            req.setEmployeeID(result.getEmployeeID());

            List<EmployeeRequest> emp_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeID());
            req.setEmployeeFName(emp_list.get(0).getEmployeeFName());

            req.setInterviewDateFromDate(result.getInterviewDateTime());
            req.setInterviewTimeCode(result.getInterviewTimeCode());
            req.setDiscloseCode(result.getDiscloseCode());
            req.setInterviewerComment(result.getInterviewerComment());
            req.setAdminComment(result.getAdminComment());
            req.setAttachedFilename(result.getFilename());

            List<Integer> job_checked = new ArrayList<Integer>();
            List<String> job_memos = new ArrayList<String>();
            List<String> pri_memos = new ArrayList<String>();
            for (InterviewContent content : result.getInterviewContents()) {
                if (content.getContentKind() == 0) {
                    job_checked.add(content.getContentCode());
                    job_memos.add(content.getContentComment());
                }
                else {
                    pri_memos.add(content.getContentComment());
                }
            }
            req.setContentJobCheckedList(job_checked);
            req.setContentJobMemos(job_memos);
            req.setContentPrivateMemos(pri_memos);

            req_list.add(req);
        }

        return req_list;
    }

    /**
     * 企業コード、社員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeID 社員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewRequest> searchFromID(Long companyID, String employeeID, int limit) {
        List<InterviewRequest> req_list = new ArrayList<InterviewRequest>();

        String sql = "FROM InterviewResult r WHERE r.companyID = :companyID AND r.employeeID = :employeeID ORDER BY r.interviewDateTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("companyID", companyID);
        query.setParameter("employeeID", employeeID);

        if (limit > 0) {
            query.setMaxResults(limit);
        }

        List<InterviewResult> result_list = (List<InterviewResult>) query.getResultList();
        for (InterviewResult result : result_list) {
            InterviewRequest req = new InterviewRequest();

            req.setIdFromNumeric(result.getId(), 1);
            req.setCompanyIDFromNumeric(result.getCompanyID(), 4);
            req.setEmployeeID(result.getEmployeeID());

            List<EmployeeRequest> emp_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeID());
            req.setEmployeeFName(emp_list.get(0).getEmployeeFName());

            req.setInterviewDateFromDate(result.getInterviewDateTime());
            req.setInterviewTimeCode(result.getInterviewTimeCode());
            req.setDiscloseCode(result.getDiscloseCode());
            req.setInterviewerComment(result.getInterviewerComment());
            req.setAdminComment(result.getAdminComment());
            req.setAttachedFilename(result.getFilename());

            List<InterviewContent> content_list = interviewContentRepository.findByResultID(result.getId());
            List<Integer> job_checked = new ArrayList<Integer>();
            List<String> job_memos = new ArrayList<String>();
            List<String> pri_memos = new ArrayList<String>();
            for (InterviewContent content : content_list) {
                if (content.getContentKind() == 0) {
                    job_checked.add(content.getContentCode());
                    job_memos.add(content.getContentComment());
                }
                else {
                    pri_memos.add(content.getContentComment());
                }
            }
            req.setContentJobCheckedList(job_checked);
            req.setContentJobMemos(job_memos);
            req.setContentPrivateMemos(pri_memos);

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
        for (int i = 0; i < req.getContentJobCheckItems().size(); i ++) {
            if (req.containsContentJobChecked(i) || !req.getContentJobMemos().get(i).isEmpty()) {
                contentJobItems.put(i, req.getContentJobMemos().get(i));
            }
        }

        Map<Integer, String> contentPrivateItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < req.getContentPrivateCheckItems().size(); i ++) {
            if (!req.getContentPrivateMemos().get(i).isEmpty()) {
                contentPrivateItems.put(i, req.getContentPrivateMemos().get(i));
            }
        }

        LogUtils.info("contentJobItems:");
        for (int key : contentJobItems.keySet()) {
            LogUtils.info("key = " + key + ", value = " + contentJobItems.get(key));

            InterviewContent content = new InterviewContent();
            content.setResultID(result.getId());
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
            content.setResultID(result.getId());
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
