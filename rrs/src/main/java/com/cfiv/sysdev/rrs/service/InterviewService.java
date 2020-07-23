package com.cfiv.sysdev.rrs.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.entity.Employee;
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
     * 企業コード、従業員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewRequest> search(InterviewSearchRequest req) {
        List<InterviewRequest> req_list = new ArrayList<InterviewRequest>();

        String where = " WHERE ";
        String and = " AND ";
        String or = " OR ";

        StringBuffer employeeSQL = new StringBuffer();
        String selectEmployee = "SELECT DISTINCT e FROM Employee e ";
        String employeeOrderBy = " ORDER BY e.companyID, e.employeeCode";

        String companyIDTag = "companyID";
        String employeeCodeTag = "employeeCode";
        String hireYMMinTag = "hireYMMin";
        String hireYMMaxTag = "hireYMMax";
        String adoptCodeTag = "adoptCode_";
        String supportCodeTag = "supportCode_";
        String employCodeTag = "employCode_";

        String companyIDCond = "e.companyID = :" + companyIDTag;
        String employeeCodeCond = "e.employeeCode = :" + employeeCodeTag;
        String hireYMMinCond = "e.hireYM >= :" + hireYMMinTag;
        String hireYMMaxCond = "e.hireYM <= :" + hireYMMaxTag;
        String adoptCodeCond = "e.adoptCode = :" + adoptCodeTag;
        String supportCodeCond = "e.supportCode = :" + supportCodeTag;
        String employCodeCond = "e.employCode = :" + employCodeTag;

        boolean isNeedWhere = true;
        boolean isNeedAnd = false;

        employeeSQL.append(selectEmployee);

        if (!req.getCompanyID().isEmpty()) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append(companyIDCond);
            isNeedAnd = true;
        }

        if (!req.getEmployeeCode().isEmpty()) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append(employeeCodeCond);
            isNeedAnd = true;
        }

        Date hireYMMin = null;
        Date hireYMMax = null;
        if (req.getHireYMDateFromLengthStartCode() != null && req.getHireYMDateFromLengthEndCode() != null) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            if (req.getHireYMDateFromLengthStartCode().compareTo(req.getHireYMDateFromLengthEndCode()) < 0) {
                hireYMMin = req.getHireYMDateFromLengthStartCode();
                hireYMMax = req.getHireYMDateFromLengthEndCode();
            }
            else {
                hireYMMin = req.getHireYMDateFromLengthEndCode();
                hireYMMax = req.getHireYMDateFromLengthStartCode();
            }

            employeeSQL.append(hireYMMinCond);
            employeeSQL.append(and);
            employeeSQL.append(hireYMMaxCond);
            isNeedAnd = true;
        }
        else if (req.getHireYMDateFromLengthStartCode() != null && req.getHireYMDateFromLengthEndCode() == null) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            hireYMMax = req.getHireYMDateFromLengthStartCode();

            employeeSQL.append(hireYMMaxCond);
            isNeedAnd = true;
        }
        else if (req.getHireYMDateFromLengthStartCode() == null && req.getHireYMDateFromLengthEndCode() != null) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            hireYMMin = req.getHireYMDateFromLengthEndCode();

            employeeSQL.append(hireYMMinCond);
            isNeedAnd = true;
        }

        if (req.getAdoptCheckedList().size() == 1) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append(adoptCodeCond + 0);
            isNeedAnd = true;
        }
        else if (req.getAdoptCheckedList().size() > 1) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append("(");

            for (int i = 0; i < req.getAdoptCheckedList().size(); i ++) {
                employeeSQL.append(adoptCodeCond + i);

                if (i != req.getAdoptCheckedList().size() - 1) {
                    employeeSQL.append(or);
                }
            }

            employeeSQL.append(")");
            isNeedAnd = true;
        }

        if (req.getSupportCheckedList().size() == 1) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append(supportCodeCond + 0);
            isNeedAnd = true;
        }
        else if (req.getSupportCheckedList().size() > 1) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append("(");

            for (int i = 0; i < req.getSupportCheckedList().size(); i ++) {
                employeeSQL.append(supportCodeCond + i);

                if (i != req.getSupportCheckedList().size() - 1) {
                    employeeSQL.append(or);
                }
            }

            employeeSQL.append(")");
            isNeedAnd = true;
        }

        if (req.getEmployCheckedList().size() == 1) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append(employCodeCond + 0);
            isNeedAnd = true;
        }
        else if (req.getEmployCheckedList().size() > 1) {
            if (isNeedWhere) {
                employeeSQL.append(where);
                isNeedWhere = false;
            }
            if (isNeedAnd) {
                employeeSQL.append(and);
            }

            employeeSQL.append("(");

            for (int i = 0; i < req.getEmployCheckedList().size(); i ++) {
                employeeSQL.append(employCodeCond + i);

                if (i != req.getEmployCheckedList().size() - 1) {
                    employeeSQL.append(or);
                }
            }

            employeeSQL.append(")");
            isNeedAnd = true;
        }

        employeeSQL.append(employeeOrderBy);

        Query employeeQuery = entityManager.createQuery(employeeSQL.toString());
        if (!req.getCompanyID().isEmpty()) {
            employeeQuery.setParameter(companyIDTag, req.getCompanyIDLong());
        }
        if (!req.getEmployeeCode().isEmpty()) {
            employeeQuery.setParameter(employeeCodeTag, req.getEmployeeCode());
        }
        if (hireYMMin != null) {
            employeeQuery.setParameter(hireYMMinTag, hireYMMin);
        }
        if (hireYMMax != null) {
            employeeQuery.setParameter(hireYMMaxTag, hireYMMax);
        }
        for (int i = 0; i < req.getAdoptCheckedList().size(); i ++) {
            employeeQuery.setParameter(adoptCodeTag + i, req.getAdoptCheckedList().get(i));
        }
        for (int i = 0; i < req.getSupportCheckedList().size(); i ++) {
            employeeQuery.setParameter(supportCodeTag + i, req.getSupportCheckedList().get(i));
        }
        for (int i = 0; i < req.getEmployCheckedList().size(); i ++) {
            employeeQuery.setParameter(employCodeTag + i, req.getEmployCheckedList().get(i));
        }

        List<Employee> employee_list = (List<Employee>) employeeQuery.getResultList();

        for (int i = 0; i < employee_list.size(); i ++) {
            LogUtils.info("[" + i + "]:CompanyID = " + employee_list.get(i).getCompanyID() + ", EmployeeCode = " + employee_list.get(i).getEmployeeCode());
        }


        LogUtils.info("employeeSQL = " + employeeSQL);
        if (hireYMMin != null) {
            LogUtils.info("hireYMMin = " + new SimpleDateFormat("yyyyMM").format(hireYMMin));
        }
        else {
            LogUtils.info("hireYMMin = null");
        }
        if (hireYMMax != null) {
            LogUtils.info("hireYMMax = " + new SimpleDateFormat("yyyyMM").format(hireYMMax));
        }
        else {
            LogUtils.info("hireYMMax = null");
        }

        String select = "SELECT DISTINCT r FROM InterviewResult r LEFT JOIN r.interviewContents ";
        String order_by = " ORDER BY r.interviewDateTime DESC";

        String interviewdate_region_cond = "r.interviewDateTime >= :interviewDateTimeStart AND r.interviewDateTime < :interviewDateTimeEnd";
        String interviewdate_last_cond = "r.interviewDateTime <= :interviewDateTimeLast";
        String interviewtime_cond = "r.interviewTimeCode = :interviewTimeCode_";
        String disclose_cond = "r.interviewTimeCode = :discloseCode_";
        String contentjob_checked_cond = "r.contentKind = 0 AND r.contentCode = :contentJobCode_";
        String contentjob_memo_cond = "r.contentComment LIKE :contentJobMemo_";
        String contentprivate_checked_cond = "r.contentKind = 1 AND r.contentCode = :contentPrivateCode_";
        String contentprivate_memo_cond = "r.contentComment LIKE :contentPrivateMemo_";
        String interviewer_comment_cond = "r.interviewerComment LIKE :interviewerCommentMemo_";
        String admin_comment_cond = "r.adminComment LIKE :adminCommentMemo_";

        Query query = entityManager.createQuery(select);
        List<InterviewResult> result_list = (List<InterviewResult>) query.getResultList();



/*
        LogUtils.info("companyID = " + req.getCompanyID());
        LogUtils.info("employeeCode = " + req.getEmployeeCode());
        LogUtils.info("hireLengthStartCode = " + req.getHireLengthStartCode());
        LogUtils.info("hireLengthEndCode = " + req.getHireLengthEndCode());
        for (int i = 0; i < req.getAdoptCheckedList().size(); i ++) {
            LogUtils.info("adoptChecked[" + i + "] = " + req.getAdoptCheckedList().get(i));
        }
        for (int i = 0; i < req.getSupportCheckedList().size(); i ++) {
            LogUtils.info("supportChecked[" + i + "] = " + req.getSupportCheckedList().get(i));
        }
        for (int i = 0; i < req.getEmployCheckedList().size(); i ++) {
            LogUtils.info("employChecked[" + i + "] = " + req.getEmployCheckedList().get(i));
        }


        LogUtils.info("interviewDateStart = " + req.getInterviewDateStart());
        LogUtils.info("interviewDateEnd = " + req.getInterviewDateEnd());
        LogUtils.info("interviewDateLastCode = " + req.getInterviewDateLastCode());
        LogUtils.info("interviewDateCode = " + req.getInterviewDateCode());

        for (int i = 0; i < req.getInterviewTimeCheckedList().size(); i ++) {
            LogUtils.info("interviewTimeChecked[" + i + "] = " + req.getInterviewTimeCheckedList().get(i));
        }
        for (int i = 0; i < req.getDiscloseCheckedList().size(); i ++) {
            LogUtils.info("discloseChecked[" + i + "] = " + req.getDiscloseCheckedList().get(i));
        }

        for (int i = 0; i < req.getContentJobCheckedList().size(); i ++) {
            LogUtils.info("contentJobChecked[" + i + "] = " + req.getContentJobCheckedList().get(i));
        }
        for (int i = 0; i < req.getContentJobMemos().size(); i ++) {
            LogUtils.info("contentJobMemos[" + i + "] = " + req.getContentJobMemos().get(i));
        }
        for (int i = 0; i < req.getContentPrivateMemos().size(); i ++) {
            LogUtils.info("contentPrivateMemos[" + i + "] = " + req.getContentPrivateMemos().get(i));
        }
        for (int i = 0; i < req.getInterviewerCommentMemos().size(); i ++) {
            LogUtils.info("interviewerCommentMemos[" + i + "] = " + req.getInterviewerCommentMemos().get(i));
        }
        for (int i = 0; i < req.getAdminCommentMemos().size(); i ++) {
            LogUtils.info("adminCommentMemos[" + i + "] = " + req.getAdminCommentMemos().get(i));
        }


        try {
            DateFormat formatter = new SimpleDateFormat("yyyyMM");
            int d1 = Integer.parseInt(formatter.format(new SimpleDateFormat("yyyyMM").parse("197102")));
            int d2 = Integer.parseInt(formatter.format(new Date()));
            int age = (d2 - d1)/100;
            LogUtils.info("d1 = " + d1);
            LogUtils.info("d2 = " + d2);
            LogUtils.info("d2-d1 = " + (d2-d1));
            LogUtils.info("age = " + age);
        }
        catch (NumberFormatException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        catch (ParseException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }

        LogUtils.info("result_list.size = " + result_list.size());
*/

        for (InterviewResult result : result_list) {
            InterviewRequest ret = new InterviewRequest();

            ret.setIdFromNumeric(result.getId(), 1);
//            ret.setCompanyIDFromNumeric(result.getCompanyID(), 4);
//            ret.setEmployeeCode(result.getEmployeeCode());

            List<EmployeeRequest> emp_list = employeeService.searchRequestFromID(ret.getCompanyID(), ret.getEmployeeCode());
            ret.setEmployeeFName(emp_list.get(0).getEmployeeFName());

            ret.setInterviewDateFromDate(result.getInterviewDateTime());
            ret.setInterviewTimeCode(result.getInterviewTimeCode());
            ret.setDiscloseCode(result.getDiscloseCode());
            ret.setInterviewerComment(result.getInterviewerComment());
            ret.setAdminComment(result.getAdminComment());
            ret.setAttachedFilename(result.getFilename());

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
            ret.setContentJobCheckedList(job_checked);
            ret.setContentJobMemos(job_memos);
            ret.setContentPrivateMemos(pri_memos);

            req_list.add(ret);
        }

        return req_list;
    }


    /**
     * 企業コード、従業員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewRequest> searchFromID(Long companyID, String employeeCode, int limit) {
        List<InterviewRequest> req_list = new ArrayList<InterviewRequest>();

        String sql = "FROM InterviewResult r WHERE r.companyID = :companyID AND r.employeeCode = :employeeCode ORDER BY r.interviewDateTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("companyID", companyID);
        query.setParameter("employeeCode", employeeCode);

        if (limit > 0) {
            query.setMaxResults(limit);
        }

        List<InterviewResult> result_list = (List<InterviewResult>) query.getResultList();
        for (InterviewResult result : result_list) {
            InterviewRequest req = new InterviewRequest();

            req.setIdFromNumeric(result.getId(), 1);
//            req.setCompanyIDFromNumeric(result.getCompanyID(), 4);
//            req.setEmployeeCode(result.getEmployeeCode());

            List<EmployeeRequest> emp_list = employeeService.searchRequestFromID(req.getCompanyID(), req.getEmployeeCode());
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
//        result.setCompanyID(req.getCompanyIDLong());
//        result.setEmployeeCode(req.getEmployeeCode());
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
