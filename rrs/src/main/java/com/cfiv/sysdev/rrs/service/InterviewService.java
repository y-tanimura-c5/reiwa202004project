package com.cfiv.sysdev.rrs.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.LogUtils;
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.dto.InterviewCSV;
import com.cfiv.sysdev.rrs.dto.InterviewConditionRequest;
import com.cfiv.sysdev.rrs.dto.InterviewRequest;
import com.cfiv.sysdev.rrs.dto.InterviewSearchRequest;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Employee;
import com.cfiv.sysdev.rrs.entity.InterviewAttach;
import com.cfiv.sysdev.rrs.entity.InterviewCondition;
import com.cfiv.sysdev.rrs.entity.InterviewContent;
import com.cfiv.sysdev.rrs.entity.InterviewResult;
import com.cfiv.sysdev.rrs.repository.InterviewAttachRepository;
import com.cfiv.sysdev.rrs.repository.InterviewConditionRepository;
import com.cfiv.sysdev.rrs.repository.InterviewContentRepository;
import com.cfiv.sysdev.rrs.repository.InterviewResultRepository;

/**
 * 面談結果 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class InterviewService {
    static final String WHERE = " WHERE ";
    static final String AND = " AND ";
    static final String OR = " OR ";

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
     * 企業情報 Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * ユーザー情報 Service
     */
    @Autowired
    UserService userService;

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
     * 面談添付ファイル Repository
     */
    @Autowired
    private InterviewAttachRepository interviewAttachRepository;

    /**
     * 面談結果検索条件 Repository
     */
    @Autowired
    private InterviewConditionRepository interviewConditionRepository;

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
     * IDでの従業員情報検索
     * @param id ID
     * @return 検索結果(Employee)
     */
    public InterviewRequest findOneRequest(Long id) {
        return resultToRequest(findOne(id));
    }

    /**
     * 企業コード、従業員番号での面談結果検索
     * @param sReq 検索条件
     * @param uReq ユーザー情報
     * @return 検索結果(InterviewRequest)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewResult> searchResult(InterviewSearchRequest sReq, UserRequest uReq) {
        List<InterviewResult> resultList = new ArrayList<InterviewResult>();

        List<Employee> employeeList = searchEmployees(sReq);

        if (employeeList == null || employeeList.isEmpty()) {
            return resultList;
        }

        for (int i = 0; i < sReq.getContentJobCheckItems().size(); i++) {
            if (!sReq.containsContentJobChecked(i) && !sReq.getContentJobMemos().isEmpty()
                    && !sReq.getContentJobMemos().get(i).isEmpty()) {
                sReq.getContentJobCheckedList().add(i);
            }
        }

        StringBuffer sql = new StringBuffer();
        String deletedTag = "deleted";
        String companyIDTag = "companyID";
        String employeeCodeTag = "employeeCode";
        String select = "SELECT DISTINCT r FROM InterviewResult r LEFT JOIN r.interviewContents c"
                + " WHERE r.deleted = :" + deletedTag
                + " AND r.companyID = :" + companyIDTag
                + " AND r.employeeCode = :" + employeeCodeTag;
        String orderBy = " ORDER BY r.interviewDate DESC";

        String refinerUserIDTag = "refinerUserID";
        String interviewDateStartTag = "interviewDateStart";
        String interviewDateEndTag = "interviewDateEnd";
        String interviewTimeCodeTag = "interviewTimeCode_";
        String discloseCodeTag = "discloseCode_";
        String contentJobCodeTag = "contentJobCode_";
        String contentJobMemoTag = "contentJobMemo_";
        String contentPriMemoTag = "contentPrivateMemo_";
        String interviewerCommentTag = "interviewerCommentMemo_";
        String adminCommentTag = "adminCommentMemo_";

        String refinerUserIDCond = "r.refinerUserID = :" + refinerUserIDTag;
        String interviewDateStartCond = "r.interviewDate >= :" + interviewDateStartTag;
        String interviewDateEndCond = "r.interviewDate < :" + interviewDateEndTag;
        String interviewTimeCodeCond = "r.interviewTimeCode = :" + interviewTimeCodeTag;
        String discloseCodeCond = "r.discloseCode = :" + discloseCodeTag;
        String contentJobCodeCond = "c.contentKind = " + Consts.CONTENTKIND_JOB + " AND c.contentCode = :"
                + contentJobCodeTag;
        String contentJobMemoCond = "c.contentComment LIKE :" + contentJobMemoTag;
        String contentPriMemoCond = "c.contentKind = " + Consts.CONTENTKIND_PRIVATE + " AND c.contentComment LIKE :"
                + contentPriMemoTag;
        String interviewerCommentCond = "r.interviewerComment LIKE :" + interviewerCommentTag;
        String adminCommentCond = "r.adminComment LIKE :" + adminCommentTag;

        sql.append(select);

        // ユーザーがリファイナーの場合、リファイナー条件文字列を追加する
        if (uReq.getUserRoleCode() == Consts.USERROLECODE_REFINER) {
            sql.append(createSingleCondition(AND, uReq.getUsername(), refinerUserIDCond));
        }

        // 面談日条件文字列を作成する
        Date startDate = null;
        Date endDate = null;
        if (sReq.getInterviewDateCode() == Consts.INTERVIEWDATECODE_REGION) {
            if (sReq.getInterviewDateStartDate() != null && sReq.getInterviewDateEndDate() != null) {
                if (sReq.getInterviewDateStartDate().compareTo(sReq.getInterviewDateEndDate()) < 0) {
                    startDate = sReq.getInterviewDateStartDate();
                    endDate = Utils.tommorow(sReq.getInterviewDateEndDate());
                }
                else {
                    startDate = sReq.getInterviewDateEndDate();
                    endDate = Utils.tommorow(sReq.getInterviewDateStartDate());
                }

                sql.append(AND + interviewDateStartCond + AND + interviewDateEndCond);
            }
        }
        else if (sReq.getInterviewDateCode() == Consts.INTERVIEWDATECODE_LAST) {
            if (sReq.getInterviewDateLastDate() != null) {
                endDate = Utils.tommorow(sReq.getInterviewDateLastDate());

                sql.append(AND + interviewDateEndCond);
            }
        }

        // 面談時間、情報開示条件文字列を作成する
        sql.append(createMultiCondition(AND, sReq.getInterviewTimeCheckedList().size(), interviewTimeCodeCond));
        sql.append(createMultiCondition(AND, sReq.getDiscloseCheckedList().size(), discloseCodeCond));

        // 面談内容(会社関連、プライベート)条件文字列を作成する
        sql.append(createContentJobCondition(AND, sReq.getContentJobCheckedList(), sReq.getContentJobMemos(),
                contentJobCodeCond, contentJobMemoCond));
        List<String> pMemos = Utils.trimMemos(sReq.getContentPrivateMemos());
        sql.append(createMultiCondition(AND, pMemos.size(), contentPriMemoCond));

        // 相談内容、管理者記入欄条件文字列を作成する
        List<String> iMemos = Utils.trimMemos(sReq.getInterviewerCommentMemos());
        sql.append(createMultiCondition(AND, iMemos.size(), interviewerCommentCond));
        List<String> aMemos = Utils.trimMemos(sReq.getAdminCommentMemos());
        sql.append(createMultiCondition(AND, aMemos.size(), adminCommentCond));

        sql.append(orderBy);
        LogUtils.debug("interviewSQL = " + sql);

        // 作成したSQLに対応するパラメータを設定する
        Query query = entityManager.createQuery(sql.toString());
        for (Employee employee : employeeList) {
            query.setParameter(deletedTag, Consts.EXIST);
            query.setParameter(companyIDTag, employee.getCompanyID());
            query.setParameter(employeeCodeTag, employee.getEmployeeCode());

            if (uReq.getUserRoleCode() == Consts.USERROLECODE_REFINER) {
                query.setParameter(refinerUserIDTag, uReq.getUsername());
            }

            if (startDate != null) {
                query.setParameter(interviewDateStartTag, startDate);
            }
            if (endDate != null) {
                query.setParameter(interviewDateEndTag, endDate);
            }

            for (int i = 0; i < sReq.getInterviewTimeCheckedList().size(); i++) {
                query.setParameter(interviewTimeCodeTag + i, sReq.getInterviewTimeCheckedList().get(i));
            }
            for (int i = 0; i < sReq.getDiscloseCheckedList().size(); i++) {
                query.setParameter(discloseCodeTag + i, sReq.getDiscloseCheckedList().get(i));
            }

            for (int i = 0; i < sReq.getContentJobCheckedList().size(); i++) {
                query.setParameter(contentJobCodeTag + i, sReq.getContentJobCheckedList().get(i));

                String jMemo = sReq.getContentJobMemos().get(sReq.getContentJobCheckedList().get(i));
                if (!jMemo.isEmpty()) {
                    query.setParameter(contentJobMemoTag + i, "%" + jMemo + "%");
                }
            }

            for (int i = 0; i < pMemos.size(); i++) {
                query.setParameter(contentPriMemoTag + i, "%" + pMemos.get(i) + "%");
            }

            for (int i = 0; i < iMemos.size(); i++) {
                query.setParameter(interviewerCommentTag + i, "%" + iMemos.get(i) + "%");
            }
            for (int i = 0; i < aMemos.size(); i++) {
                query.setParameter(adminCommentTag + i, "%" + aMemos.get(i) + "%");
            }

            resultList.addAll((List<InterviewResult>) query.getResultList());
        }

        return resultList;
    }

    public List<InterviewRequest> searchRequest(InterviewSearchRequest sReq, UserRequest uReq) {
        return resultToRequestList(searchResult(sReq, uReq));
    }

    /**
     * 企業コード、従業員番号での面談結果検索
     * @param sReq 検索条件
     * @param pageable ページング条件
     * @return 検索結果ページ(InterviewRequest)
     */
    public Page<InterviewRequest> searchRequest(InterviewSearchRequest sReq, UserRequest uReq, Pageable pageable) {
        List<InterviewRequest> reqList = searchRequest(sReq, uReq);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<InterviewRequest> pageList;

        if (reqList.size() < startItem) {
            pageList = Collections.emptyList();
        }
        else {
            int toIndex = Math.min(startItem + pageSize, reqList.size());
            pageList = reqList.subList(startItem, toIndex);
        }

        Page<InterviewRequest> reqPage = new PageImpl<InterviewRequest>(pageList, PageRequest.of(currentPage, pageSize),
                reqList.size());

        return reqPage;
    }

    /**
     * 企業コード、従業員番号での面談結果検索
     * @param sReq 検索条件
     * @param uReq ユーザー情報
     * @return 検索結果(InterviewCSV)
     */
    public List<InterviewCSV> searchCSV(InterviewSearchRequest sReq, UserRequest uReq) {
        List<InterviewRequest> reqList = searchRequest(sReq, uReq);
        List<InterviewCSV> csvList = new ArrayList<>();

        // 取得した面談結果を面談結果リクエストに変換する
        for (InterviewRequest req : reqList) {
            csvList.add(req.toCSV(uReq.getUserRoleCode()));
        }

        return csvList;
    }

    /**
     * 従業員情報検索条件での従業員情報検索
     * @param req 検索条件
     * @return 検索結果
     */
    @SuppressWarnings("unchecked")
    public List<Employee> searchEmployees(InterviewSearchRequest req) {

        StringBuffer sql = new StringBuffer();
        String deletedTag = "deleted";
        String select = "SELECT DISTINCT e FROM Employee e WHERE e.deleted = :" + deletedTag;
        String orderBy = " ORDER BY e.companyID, e.employeeCode";

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

        sql.append(select);

        // 企業コード、従業員番号条件文字列を作成する
        sql.append(createSingleCondition(AND, req.getCompanyID(), companyIDCond));
        sql.append(createSingleCondition(AND, req.getEmployeeCode(), employeeCodeCond));

        // 勤続年数範囲条件文字列を作成する
        Date hireYMMin = null;
        Date hireYMMax = null;
        if (req.getHireYMDateFromLengthStartCode() != null && req.getHireYMDateFromLengthEndCode() != null) {
            if (req.getHireYMDateFromLengthStartCode().compareTo(req.getHireYMDateFromLengthEndCode()) < 0) {
                hireYMMin = req.getHireYMDateFromLengthStartCode();
                hireYMMax = req.getHireYMDateFromLengthEndCode();
            }
            else {
                hireYMMin = req.getHireYMDateFromLengthEndCode();
                hireYMMax = req.getHireYMDateFromLengthStartCode();
            }

            sql.append(AND + hireYMMinCond + AND + hireYMMaxCond);
        }
        else if (req.getHireYMDateFromLengthStartCode() != null && req.getHireYMDateFromLengthEndCode() == null) {
            hireYMMax = req.getHireYMDateFromLengthStartCode();

            sql.append(AND + hireYMMaxCond);
        }
        else if (req.getHireYMDateFromLengthStartCode() == null && req.getHireYMDateFromLengthEndCode() != null) {
            hireYMMin = req.getHireYMDateFromLengthEndCode();

            sql.append(AND + hireYMMinCond);
        }

        // 採用種別、扶養有無、就業種別条件文字列を作成する
        sql.append(createMultiCondition(AND, req.getAdoptCheckedList().size(), adoptCodeCond));
        sql.append(createMultiCondition(AND, req.getSupportCheckedList().size(), supportCodeCond));
        sql.append(createMultiCondition(AND, req.getEmployCheckedList().size(), employCodeCond));
        sql.append(orderBy);
        LogUtils.debug("employeeSQL = " + sql);

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter(deletedTag, Consts.EXIST);
        if (!req.getCompanyID().isEmpty()) {
            query.setParameter(companyIDTag, req.getCompanyIDLong());
        }
        if (!req.getEmployeeCode().isEmpty()) {
            query.setParameter(employeeCodeTag, req.getEmployeeCode());
        }
        if (hireYMMin != null) {
            query.setParameter(hireYMMinTag, hireYMMin);
        }
        if (hireYMMax != null) {
            query.setParameter(hireYMMaxTag, hireYMMax);
        }
        for (int i = 0; i < req.getAdoptCheckedList().size(); i++) {
            query.setParameter(adoptCodeTag + i, req.getAdoptCheckedList().get(i));
        }
        for (int i = 0; i < req.getSupportCheckedList().size(); i++) {
            query.setParameter(supportCodeTag + i, req.getSupportCheckedList().get(i));
        }
        for (int i = 0; i < req.getEmployCheckedList().size(); i++) {
            query.setParameter(employCodeTag + i, req.getEmployCheckedList().get(i));
        }

        List<Employee> employeeList = (List<Employee>) query.getResultList();

        return employeeList;
    }

    /**
     * 企業コード、従業員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(InterviewResult)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewResult> searchResultFromKey(Long companyID, String employeeCode, int limit) {
        String deletedTag = "deleted";
        String companyIDTag = "companyID";
        String employeeCodeTag = "employeeCode";
        String sql = "SELECT DISTINCT r FROM InterviewResult r LEFT JOIN r.interviewContents"
                + " WHERE r.deleted = :" + deletedTag
                + " AND r.companyID = :" + companyIDTag
                + " AND r.employeeCode = :" + employeeCodeTag
                + " ORDER BY r.interviewDate DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter(deletedTag, Consts.EXIST);
        query.setParameter(companyIDTag, companyID);
        query.setParameter(employeeCodeTag, employeeCode);
        if (limit > 0) {
            query.setMaxResults(limit);
        }

        return (List<InterviewResult>) query.getResultList();
    }

    /**
     * 企業コードでの面談結果検索
     * @param companyID 企業コード
     * @return 検索結果(InterviewResult)
     */
    @SuppressWarnings("unchecked")
    public InterviewResult findOneResultFromCompanyID(Long companyID) {
        String deletedTag = "deleted";
        String companyIDTag = "companyID";
        String sql = "SELECT DISTINCT r FROM InterviewResult r LEFT JOIN r.interviewContents"
                + " WHERE r.deleted = :" + deletedTag
                + " AND r.companyID = :" + companyIDTag
                + " ORDER BY r.interviewDate DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter(deletedTag, Consts.EXIST);
        query.setParameter(companyIDTag, companyID);
        query.setMaxResults(1);

        List<InterviewResult> result = (List<InterviewResult>) query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * 企業コードでの最終面談日時検索
     * @param companyID 企業コード
     * @return 検索結果(InterviewResult)
     */
    public Date getLastInterviewDateFromCompanyID(Long companyID) {
        InterviewResult result = findOneResultFromCompanyID(companyID);

        if (result != null) {
            return result.getInterviewDate();
        }
        else {
            return null;
        }
    }

    /**
     * 企業コード、従業員番号での面談結果検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(InterviewRequest)
     */
    public List<InterviewRequest> searchRequestFromKey(Long companyID, String employeeCode, int limit) {
        return resultToRequestList(searchResultFromKey(companyID, employeeCode, limit));
    }

    /**
     * 面談結果一覧の型変換(InterviewResult→InterviewRequest)
     * @param list 面談結果一覧(InterviewResult)
     * @return 面談結果一覧(InterviewRequest)
     */
    public List<InterviewRequest> resultToRequestList(List<InterviewResult> list) {
        List<InterviewRequest> res = new ArrayList<InterviewRequest>();
        for (InterviewResult result : list) {
            res.add(resultToRequest(result));
        }

        return res;
    }

    /**
     * 面談結果の型変換(InterviewResult→InterviewRequest)
     * @param list 面談結果(InterviewResult)
     * @return 面談結果(InterviewRequest)
     */
    public InterviewRequest resultToRequest(InterviewResult result) {
        if (result != null) {
            EmployeeRequest eReq = employeeService.findOneRequestFromID(result.getCompanyID(),
                    result.getEmployeeCode());
            String cName = companyService.getCompanyName(result.getCompanyID());
            String rName = userService.getDisplayNameFromUsername(result.getRefinerUserID());
            return new InterviewRequest(result, eReq, cName, rName);
        }
        else {
            return null;
        }
    }

    /**
     * 面談結果ID、面談内容種別、面談内容コードでの面談内容検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public InterviewContent findOneContentFromKey(Long resultID, int contentKind, int contentCode) {
        String deletedTag = "deleted";
        String resultIDTag = "resultID";
        String contentKindTag = "contentKind";
        String contentCodeTag = "contentCode";
        String sql = "FROM InterviewContent c"
                + " WHERE c.deleted = :" + deletedTag
                + " AND c.resultID = :" + resultIDTag
                + " AND c.contentKind = :" + contentKindTag
                + " AND c.contentCode = :" + contentCodeTag;
        Query query = entityManager.createQuery(sql);
        query.setParameter(deletedTag, Consts.EXIST);
        query.setParameter(resultIDTag, resultID);
        query.setParameter(contentKindTag, contentKind);
        query.setParameter(contentCodeTag, contentCode);

        List<InterviewContent> list = (List<InterviewContent>) query.getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * 面談結果ID、添付ファイル名での面談添付ファイル検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public InterviewAttach findOneAttachFromKey(Long resultID, String filename) {
        String deletedTag = "deleted";
        String resultIDTag = "resultID";
        String filenameTag = "filename";
        String sql = "FROM InterviewAttach a"
                + " WHERE a.deleted = :" + deletedTag
                + " AND a.resultID = :" + resultIDTag
                + " AND a.filename = :" + filenameTag;
        Query query = entityManager.createQuery(sql);
        query.setParameter(deletedTag, Consts.EXIST);
        query.setParameter(resultIDTag, resultID);
        query.setParameter(filenameTag, filename);

        List<InterviewAttach> list = (List<InterviewAttach>) query.getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * 面談結果ID、添付ファイル名での面談内容検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewContent> searchContentFromResultID(Long resultID) {
        String deletedTag = "deleted";
        String resultIDTag = "resultID";
        String sql = "FROM InterviewContent c"
                + " WHERE c.deleted = :" + deletedTag
                + " AND c.resultID = :" + resultIDTag;
        Query query = entityManager.createQuery(sql);
        query.setParameter(deletedTag, Consts.EXIST);
        query.setParameter(resultIDTag, resultID);

        return (List<InterviewContent>) query.getResultList();
    }

    /**
     * 面談結果ID、添付ファイル名での面談添付ファイル検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<InterviewAttach> searchAttachFromResultID(Long resultID) {
        String deletedTag = "deleted";
        String resultIDTag = "resultID";
        String sql = "FROM InterviewAttach a"
                + " WHERE a.deleted = :" + deletedTag
                + " AND a.resultID = :" + resultIDTag;
        Query query = entityManager.createQuery(sql);
        query.setParameter(deletedTag, Consts.EXIST);
        query.setParameter(resultIDTag, resultID);

        return (List<InterviewAttach>) query.getResultList();
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
        result.setEmployeeCode(req.getEmployeeCode());
        result.setInterviewDate(req.getInterviewDateDate());
        result.setRefinerUserID(Utils.loginUsername());
        result.setInterviewTimeCode(req.getInterviewTimeCode());
        result.setInterviewerComment(req.getInterviewerComment());
        result.setDiscloseCode(req.getDiscloseCode());
        result.setAdminComment(req.getAdminComment());
        result.setDeleted(Consts.EXIST);
        result.setRegistUser(Utils.loginUsername());
        result.setRegistTime(now);
        result.setUpdateUser(Utils.loginUsername());
        result.setUpdateTime(now);

        result = interviewResultRepository.save(result);

        Map<Integer, String> contentJobItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < req.getContentJobCheckItems().size(); i++) {
            if (req.containsContentJobChecked(i) || !req.getContentJobMemos().get(i).isEmpty()) {
                contentJobItems.put(i, req.getContentJobMemos().get(i));
            }
        }

        Map<Integer, String> contentPrivateItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < req.getContentPrivateCheckItems().size(); i++) {
            if (!req.getContentPrivateMemos().get(i).isEmpty()) {
                contentPrivateItems.put(i, req.getContentPrivateMemos().get(i));
            }
        }

        saveContents(result.getId(), Consts.CONTENTKIND_JOB, contentJobItems);
        saveContents(result.getId(), Consts.CONTENTKIND_PRIVATE, contentPrivateItems);

        if (req.getAttachedFile() != null && !req.getAttachedFile().isEmpty()) {
            List<MultipartFile> attachList = new ArrayList<MultipartFile>();
            attachList.add(req.getAttachedFile());
            saveAttaches(result.getId(), attachList);
        }
    }

    /**
     * 面談結果更新
     * @param id 面談結果ID
     * @param req 面談結果
     */
    @Transactional
    public void save(Long id, InterviewRequest req) {
        Date now = new Date();

        InterviewResult result = findOne(id);

        if (result != null) {
            result.setInterviewDate(req.getInterviewDateDate());
            result.setInterviewTimeCode(req.getInterviewTimeCode());
            result.setInterviewerComment(req.getInterviewerComment());
            result.setDiscloseCode(req.getDiscloseCode());
            result.setAdminComment(req.getAdminComment());
            result.setUpdateUser(Utils.loginUsername());
            result.setUpdateTime(now);
            result.setUpdateCount(result.getUpdateCount() + 1);
        }
        else {
            LogUtils.error("面談結果更新対象の取得に失敗しました。");
            return;
        }

        result = interviewResultRepository.save(result);

        deleteAllContents(result.getId());
        Map<Integer, String> contentJobItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < req.getContentJobCheckItems().size(); i++) {
            if (req.containsContentJobChecked(i) || !req.getContentJobMemos().get(i).isEmpty()) {
                contentJobItems.put(i, req.getContentJobMemos().get(i));
            }
        }

        Map<Integer, String> contentPrivateItems = new LinkedHashMap<Integer, String>();
        for (int i = 0; i < req.getContentPrivateCheckItems().size(); i++) {
            if (!req.getContentPrivateMemos().get(i).isEmpty()) {
                contentPrivateItems.put(i, req.getContentPrivateMemos().get(i));
            }
        }

        saveContents(result.getId(), Consts.CONTENTKIND_JOB, contentJobItems);
        saveContents(result.getId(), Consts.CONTENTKIND_PRIVATE, contentPrivateItems);

        if (req.getAttachedFile() != null && !req.getAttachedFile().isEmpty()) {
            deleteAllAttaches(result.getId());
            List<MultipartFile> attachList = new ArrayList<MultipartFile>();
            attachList.add(req.getAttachedFile());
            saveAttaches(result.getId(), attachList);
        }
    }

    /**
     * 面談結果削除
     * @param id 面談結果ID
     * @param req 面談結果
     */
    @Transactional
    public void delete(Long id, InterviewRequest req) {
        Date now = new Date();

        InterviewResult result = findOne(id);

        if (result != null) {
            result.setDeleted(Consts.DELETED);
            result.setUpdateUser(Utils.loginUsername());
            result.setUpdateTime(now);
            result.setUpdateCount(result.getUpdateCount() + 1);
        }
        else {
            LogUtils.error("面談結果更新対象の取得に失敗しました。");
            return;
        }

        result = interviewResultRepository.save(result);
    }

    /**
     * 面談内容保存
     * @param resultID 面談結果ID
     * @param contentKind 面談内容種別
     * @param items 面談内容
     */
    @Transactional
    private void saveContents(Long resultID, int contentKind, Map<Integer, String> items) {
        Date now = new Date();

        for (int key : items.keySet()) {
            InterviewContent content = findOneContentFromKey(resultID, contentKind, key);

            if (content != null) {
                content.setContentComment(items.get(key));
                content.setUpdateUser(Utils.loginUsername());
                content.setUpdateTime(now);
            }
            else {
                content = new InterviewContent();
                content.setResultID(resultID);
                content.setContentKind(contentKind);
                content.setContentCode(key);
                content.setContentComment(items.get(key));
                content.setDeleted(Consts.EXIST);
                content.setRegistUser(Utils.loginUsername());
                content.setRegistTime(now);
                content.setUpdateUser(Utils.loginUsername());
                content.setUpdateTime(now);
            }

            interviewContentRepository.save(content);
        }
    }

    /**
     * 面談添付ファイル保存
     * @param resultID 面談結果ID
     * @param files 面談添付ファイル
     */
    @Transactional
    private void saveAttaches(Long resultID, List<MultipartFile> files) {
        Date now = new Date();

        for (MultipartFile file : files) {
            try {
                InterviewAttach attach = findOneAttachFromKey(resultID, file.getOriginalFilename());

                if (attach != null) {
                    attach.setFilename(file.getOriginalFilename());
                    if (file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                        attach.setFiledata(file.getBytes());
                    }
                    else {
                        attach.setFiledata(Utils.imageSizeConvert(file.getBytes()));

                    }
                    attach.setUpdateUser(Utils.loginUsername());
                    attach.setUpdateTime(now);
                }
                else {
                    attach = new InterviewAttach();
                    attach.setResultID(resultID);
                    attach.setFilename(file.getOriginalFilename());
                    if (file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                        attach.setFiledata(file.getBytes());
                    }
                    else {
                        attach.setFiledata(Utils.imageSizeConvert(file.getBytes()));

                    }
                    attach.setDeleted(Consts.EXIST);
                    attach.setRegistUser(Utils.loginUsername());
                    attach.setRegistTime(now);
                    attach.setUpdateUser(Utils.loginUsername());
                    attach.setUpdateTime(now);
                }

                interviewAttachRepository.save(attach);
            }
            catch (IOException e) {
                LogUtils.error("添付ファイルのアクセスに失敗しました。filename = " + file.getOriginalFilename());
            }
        }
    }

    /**
     * 面談内容全削除
     * @param resultID 面談結果ID
     */
    @Transactional
    private void deleteAllContents(Long resultID) {
        Date now = new Date();

        List<InterviewContent> contents = searchContentFromResultID(resultID);

        for (InterviewContent content : contents) {
            content.setDeleted(Consts.DELETED);
            content.setUpdateUser(Utils.loginUsername());
            content.setUpdateTime(now);
            content.setUpdateCount(content.getUpdateCount() + 1);
        }
        interviewContentRepository.saveAll(contents);
    }

    /**
     * 面談添付ファイル全削除
     * @param resultID 面談結果ID
     */
    @Transactional
    private void deleteAllAttaches(Long resultID) {
        List<InterviewAttach> attaches = searchAttachFromResultID(resultID);

        for (InterviewAttach attach : attaches) {
            interviewAttachRepository.delete(attach);
        }
    }

    /**
     * SQL条件文字列(単体パラメータ)
     * @param prefix 条件文字列の前に付与する文字列
     * @param parm パラメータ
     * @param condition 条件文字列
     * @return
     */
    private String createSingleCondition(String prefix, String parm, String condition) {
        if (!parm.isEmpty()) {
            return prefix + condition;
        }
        else {
            return "";
        }
    }

    /**
     * SQL条件文字列(複数パラメータ)
     * @param prefix 条件文字列の前に付与する文字列
     * @param paramNum パラメータ数
     * @param condition 条件文字列
     * @return
     */
    private String createMultiCondition(String prefix, int paramNum, String condition) {
        StringBuffer result = new StringBuffer();

        if (paramNum == 1) {
            result.append(condition + 0);
        }
        else if (paramNum > 1) {
            result.append("(");

            for (int i = 0; i < paramNum; i++) {
                result.append("(" + condition + i + ")");

                if (i != paramNum - 1) {
                    result.append(OR);
                }
            }

            result.append(")");
        }

        if (result.length() != 0) {
            return prefix + result.toString();
        }
        else {
            return "";
        }
    }

    /**
     * SQL条件文字列(面談内容・会社関連)
     * @param prefix 条件文字列の前に付与する文字列
     * @param checkedList チェック済アイテムリスト
     * @param memos メモリスト
     * @param codeCond チェック済アイテム条件文字列
     * @param memoCond メモ条件文字列
     * @return
     */
    private String createContentJobCondition(String prefix, List<Integer> checkedList, List<String> memos,
            String codeCond, String memoCond) {
        StringBuffer result = new StringBuffer();

        if (checkedList.size() == 1) {
            result.append(codeCond + 0);

            if (!memos.get(checkedList.get(0)).isEmpty()) {
                result.append(AND + memoCond + 0);
            }
        }
        else if (checkedList.size() > 1) {
            result.append("(");

            for (int i = 0; i < checkedList.size(); i++) {
                result.append("(");
                result.append(codeCond + i);

                if (!memos.get(checkedList.get(i)).isEmpty()) {
                    result.append(AND + memoCond + i);
                }
                result.append(")");

                if (i != checkedList.size() - 1) {
                    result.append(OR);
                }
            }

            result.append(")");
        }

        if (result.length() != 0) {
            return prefix + result.toString();
        }
        else {
            return "";
        }
    }

    /**
     * 面談結果検索条件保存
     * @param username ユーザーID
     */
    public InterviewSearchRequest getSearchRequestFromCondition(String username) {
        InterviewSearchRequest req = new InterviewSearchRequest();
        InterviewConditionRequest cond = getCondition(username);

        req.setInterviewDateStart(cond.getInterviewDateStart());
        req.setInterviewDateEnd(cond.getInterviewDateEnd());
        req.setInterviewDateLastCode(cond.getInterviewDateLastCode());
        req.setInterviewDateCode(cond.getInterviewDateCode());
        req.setInterviewTimeCheckedList(cond.getInterviewTimeCheckedList());
        req.setDiscloseCheckedList(cond.getDiscloseCheckedList());
        req.setContentJobCheckedList(cond.getContentJobCheckedList());
        req.setContentJobMemos(cond.getContentJobMemos());
        req.setContentPrivateCheckedList(cond.getContentPrivateCheckedList());
        req.setContentPrivateMemos(cond.getContentPrivateMemos());
        req.setInterviewerCommentMemos(cond.getInterviewerCommentMemos());
        req.setAdminCommentMemos(cond.getAdminCommentMemos());
        req.setHireLengthStartCode(cond.getHireLengthStartCode());
        req.setHireLengthEndCode(cond.getHireLengthEndCode());
        req.setAdoptCheckedList(cond.getAdoptCheckedList());
        req.setSupportCheckedList(cond.getSupportCheckedList());
        req.setEmployCheckedList(cond.getEmployCheckedList());

        return req;
    }

    /**
     * 面談結果検索条件
     * @param username ユーザーID
     */
    public InterviewConditionRequest getCondition(String username) {
        InterviewConditionRequest req = new InterviewConditionRequest();
        List<InterviewCondition> list;
        List<Integer> iList;
        List<String> sList;

        list = interviewConditionRepository.findByUsernameAndConditionKind(username, Consts.CONDITIONKIND_USESEARCH);
        if (!list.isEmpty()) {
            req.setUseInterviewSearch(list.get(0).getConditionString());
        }

        list = interviewConditionRepository.findByUsernameAndConditionKind(username,
                Consts.CONDITIONKIND_INTERVIEWDATECODE);
        if (!list.isEmpty()) {
            req.setInterviewDateCode(list.get(0).getConditionCode());
        }

        list = interviewConditionRepository.findByUsernameAndConditionKind(username,
                Consts.CONDITIONKIND_INTERVIEWDATEREGION);
        if (list.size() >= 2) {
            req.setInterviewDateMin(list.get(0).getConditionCode());
            req.setInterviewDateMax(list.get(1).getConditionCode());
        }

        list = interviewConditionRepository.findByUsernameAndConditionKind(username,
                Consts.CONDITIONKIND_INTERVIEWDATELAST);
        if (!list.isEmpty()) {
            req.setInterviewDateLastCode(list.get(0).getConditionCode());
        }

        req.setInterviewTimeCheckedList(getConditionCheckedList(username, Consts.CONDITIONKIND_INTERVIEWTIME));
        req.setDiscloseCheckedList(getConditionCheckedList(username, Consts.CONDITIONKIND_DISCLOSE));

        list = interviewConditionRepository.findByUsernameAndConditionKind(username, Consts.CONDITIONKIND_CONTENTJOB);
        sList = new ArrayList<String>();
        for (int i = 0; i < req.getContentJobCheckItems().size(); i++) {
            sList.add("");
        }
        iList = new ArrayList<Integer>();
        for (InterviewCondition cond : list) {
            iList.add(cond.getConditionCode());
            sList.set(cond.getConditionCode(), cond.getConditionString());
        }
        req.setContentJobCheckedList(iList);
        req.setContentJobMemos(sList);

        req.setContentPrivateMemos(getConditionMemos(username, Consts.CONDITIONKIND_CONTENTPRIVATE,
                req.getContentPrivateCheckItems().size()));
        req.setInterviewerCommentMemos(getConditionMemos(username, Consts.CONDITIONKIND_INTERVIEWERCOMMENT,
                Consts.INTERVIEWERCOMMENT_CONDNUM));
        req.setAdminCommentMemos(
                getConditionMemos(username, Consts.CONDITIONKIND_ADMINCOMMENT, Consts.ADMINCOMMENT_CONDNUM));

        list = interviewConditionRepository.findByUsernameAndConditionKind(username, Consts.CONDITIONKIND_HIREREGION);
        if (list.size() >= 2) {
            req.setHireLengthStartCode(list.get(0).getConditionCode());
            req.setHireLengthEndCode(list.get(1).getConditionCode());
        }
        req.setAdoptCheckedList(getConditionCheckedList(username, Consts.CONDITIONKIND_ADOPT));
        req.setSupportCheckedList(getConditionCheckedList(username, Consts.CONDITIONKIND_SUPPORT));
        req.setEmployCheckedList(getConditionCheckedList(username, Consts.CONDITIONKIND_EMPLOY));

        return req;
    }

    /**
     * 面談結果検索条件
     * @param username ユーザーID
     */
    public boolean isUseInitSearchCondition(String username) {
        List<InterviewCondition> list = interviewConditionRepository.findByUsernameAndConditionKind(username,
                Consts.CONDITIONKIND_USESEARCH);
        if (!list.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * ユーザーID、検索条件コードからのチェック結果リスト
     * @param username ユーザーID
     * @param code 検索条件コード
     * @return チェック結果リスト
     */
    private List<Integer> getConditionCheckedList(String username, int code) {
        List<InterviewCondition> list = interviewConditionRepository.findByUsernameAndConditionKind(username, code);
        List<Integer> iList = new ArrayList<Integer>();
        for (InterviewCondition cond : list) {
            iList.add(cond.getConditionCode());
        }

        return iList;
    }

    /**
     * ユーザーID、検索条件コードからの面談内容メモリスト
     * @param username ユーザーID
     * @param code 検索条件コード
     * @param size 面談内容最大サイズ
     * @return 面談内容メモリスト
     */
    private List<String> getConditionMemos(String username, int code, int size) {
        List<InterviewCondition> list = interviewConditionRepository.findByUsernameAndConditionKind(username, code);
        List<String> sList = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            sList.add("");
        }
        for (InterviewCondition cond : list) {
            sList.set(cond.getConditionCode(), cond.getConditionString());
        }

        return sList;
    }

    /**
     * 面談結果検索条件保存
     * @param username ユーザーID
     * @param req 面談結果検索条件
     */
    @Transactional
    public void saveCondition(String username, InterviewConditionRequest req) {
        List<InterviewCondition> list = interviewConditionRepository.findByUsername(username);
        interviewConditionRepository.deleteAll(list);

        list.clear();

        if (req.getUseInterviewSearch() != null) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_USESEARCH, 0, req.getUseInterviewSearch()));
        }
        list.add(new InterviewCondition(username, Consts.CONDITIONKIND_INTERVIEWDATECODE, req.getInterviewDateCode(),
                ""));
        list.add(new InterviewCondition(username, Consts.CONDITIONKIND_INTERVIEWDATEREGION, req.getInterviewDateMin(),
                ""));
        list.add(new InterviewCondition(username, Consts.CONDITIONKIND_INTERVIEWDATEREGION, req.getInterviewDateMax(),
                ""));
        list.add(new InterviewCondition(username, Consts.CONDITIONKIND_INTERVIEWDATELAST,
                req.getInterviewDateLastCode(), ""));
        for (int i : req.getInterviewTimeCheckedList()) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_INTERVIEWTIME, i, ""));
        }
        for (int i : req.getDiscloseCheckedList()) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_DISCLOSE, i, ""));
        }

        for (int i = 0; i < req.getContentJobCheckItems().size(); i++) {
            if (!req.containsContentJobChecked(i) && !req.getContentJobMemos().isEmpty()
                    && !req.getContentJobMemos().get(i).isEmpty()) {
                req.getContentJobCheckedList().add(i);
            }
        }
        for (int i = 0; i < req.getContentJobCheckedList().size(); i++) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_CONTENTJOB,
                    req.getContentJobCheckedList().get(i),
                    req.getContentJobMemos().get(req.getContentJobCheckedList().get(i))));
        }
        for (int i = 0; i < Utils.trimMemos(req.getContentPrivateMemos()).size(); i++) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_CONTENTPRIVATE, i,
                    req.getContentPrivateMemos().get(i)));
        }

        for (int i = 0; i < Utils.trimMemos(req.getInterviewerCommentMemos()).size(); i++) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_INTERVIEWERCOMMENT, i,
                    req.getInterviewerCommentMemos().get(i)));
        }
        for (int i = 0; i < Utils.trimMemos(req.getAdminCommentMemos()).size(); i++) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_ADMINCOMMENT, i,
                    req.getAdminCommentMemos().get(i)));
        }

        list.add(new InterviewCondition(username, Consts.CONDITIONKIND_HIREREGION, req.getHireLengthStartCode(), ""));
        list.add(new InterviewCondition(username, Consts.CONDITIONKIND_HIREREGION, req.getHireLengthEndCode(), ""));

        for (int i : req.getAdoptCheckedList()) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_ADOPT, i, ""));
        }
        for (int i : req.getSupportCheckedList()) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_SUPPORT, i, ""));
        }
        for (int i : req.getEmployCheckedList()) {
            list.add(new InterviewCondition(username, Consts.CONDITIONKIND_EMPLOY, i, ""));
        }

        interviewConditionRepository.saveAll(list);
    }
}
