package com.cfiv.sysdev.rrs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cfiv.sysdev.rrs.Consts;

import lombok.Data;

/**
 * 面談結果検索条件履歴詳細 Entity
 */
@Entity
@Data
@Table(name = "H_INTERVIEW_CONDITION_DETAIL")
public class HistoryInterviewConditionDetail implements Serializable {
    public HistoryInterviewConditionDetail() {
        super();
    }

    public HistoryInterviewConditionDetail(String u, Long h, int k, int c, String s) {
        this();

        Date now = new Date();

        setHistoryID(h);
        setConditionKind(k);
        setConditionCode(c);
        setConditionString(s);
        setDeleted(Consts.EXIST);
        setRegistUser(u);
        setRegistTime(now);
        setUpdateUser(u);
        setUpdateTime(now);
        setUpdateCount(0);
    }

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    /**
     * 検索条件履歴ID
     */
    @Column(name = "HISTORY_ID", nullable = false)
    private Long historyID;

    /**
     * 検索条件種別
     */
    @Column(name = "CONDITION_KIND", nullable = false)
    private int conditionKind;

    /**
     * 検索条件コード
     */
    @Column(name = "CONDITION_CODE", nullable = true)
    private int conditionCode;

    /**
     * 検索条件文字列
     */
    @Column(name = "CONDITION_STRING", nullable = true)
    private String conditionString;

    /**
     * 削除
     */
    @Column(name = "DELETED", nullable = false)
    private int deleted;

    /**
     * 登録日
     */
    @Column(name = "REGIST_TIME", nullable = false)
    private Date registTime;

    /**
     * 登録者
     */
    @Column(name = "REGIST_USER", nullable = false)
    private String registUser;

    /**
     * 更新日
     */
    @Column(name = "UPDATE_TIME", nullable = false)
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name = "UPDATE_USER", nullable = false)
    private String updateUser;

    /**
     * 更新回数
     */
    @Column(name = "UPDATE_COUNT", nullable = false)
    private int updateCount;

}
