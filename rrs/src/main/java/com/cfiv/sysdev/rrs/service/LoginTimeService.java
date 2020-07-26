package com.cfiv.sysdev.rrs.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.entity.LoginTime;
import com.cfiv.sysdev.rrs.repository.LoginTimeRepository;

/**
 * ログイン日時情報 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class LoginTimeService {

    /**
     * データベースエンティティ管理
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * ログイン日時情報 Repository
     */
    @Autowired
    LoginTimeRepository loginTimeRepository;

    /**
     * ログイン日時情報保存
     * @param username ユーザーID
     * @param companyID 企業コード
     */
    public void save(String username, Long companyID) {
        Date now = new Date();
        LoginTime loginTime = findOneLoginTimeFromKey(username, companyID);

        if (loginTime == null) {
            loginTime = new LoginTime();
        }

        loginTime.setUsername(username);
        loginTime.setCompanyID(companyID);
        loginTime.setLoginTime(now);
        loginTime.setDeleted(false);
        loginTime.setRegistTime(now);
        loginTime.setRegistUser(username);
        loginTime.setUpdateTime(now);
        loginTime.setUpdateUser(username);

        loginTimeRepository.save(loginTime);
    }

    /**
     * ユーザーID、企業コードからのログイン日時情報検索
     * @param username ユーザーID
     * @param companyID 企業コード
     * @return ログイン日時情報
     */
    @SuppressWarnings("unchecked")
    public LoginTime findOneLoginTimeFromKey(String username, Long companyID) {
        String usernameTag = "username";
        String companyIDTag = "companyID";
        String sql = "FROM LoginTime l WHERE l.username = :" + usernameTag + " AND l.companyID = :" + companyIDTag
                + " ORDER BY l.loginTime DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter(usernameTag, username);
        query.setParameter(companyIDTag, companyID);
        query.setMaxResults(1);

        List<LoginTime> list = (List<LoginTime>) query.getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * ユーザーID、企業コードからの最終ログイン日時取得
     * @param username ユーザーID
     * @param companyID 企業コード
     * @return 最終ログイン日時
     */
    public Date getLastLoginTimeFromKey(String username, Long companyID) {
        LoginTime loginTime = findOneLoginTimeFromKey(username, companyID);

        if (loginTime != null) {
            return loginTime.getLoginTime();
        }
        else {
            return null;
        }
    }

    /**
     * 企業コードからの最終ログイン日時情報検索
     * @param companyID 企業コード
     * @return ログイン日時情報
     */
    @SuppressWarnings("unchecked")
    public LoginTime findOneLoginTimeFromCompanyID(Long companyID) {
        String companyIDTag = "companyID";
        String sql = "FROM LoginTime l WHERE l.companyID = :" + companyIDTag
                + " ORDER BY l.loginTime DESC";

        Query query = entityManager.createQuery(sql);
        query.setParameter(companyIDTag, companyID);
        query.setMaxResults(1);

        List<LoginTime> list = (List<LoginTime>) query.getResultList();

        if (!list.isEmpty()) {
            return list.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * 企業コードからの最終ログイン日時取得
     * @param companyID 企業コード
     * @return 最終ログイン日時
     */
    public Date getLastLoginTimeFromCompanyID(Long companyID) {
        LoginTime loginTime = findOneLoginTimeFromCompanyID(companyID);

        if (loginTime != null) {
            return loginTime.getLoginTime();
        }
        else {
            return null;
        }
    }
}
