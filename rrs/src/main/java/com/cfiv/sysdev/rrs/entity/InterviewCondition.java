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
 * 面談内容検索条件 Entity
 */
@Entity
@Data
@Table(name="M_INTERVIEW_CONDITION")
public class InterviewCondition implements Serializable {
    public InterviewCondition() {
        super();
    }

    public InterviewCondition(String u, int k, int c, String s) {
        this();

        Date now = new Date();

        setUsername(u);
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
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザー名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 検索条件種別
     */
    @Column(name="CONDITION_KIND")
    private int conditionKind;

    /**
     * 検索条件コード
     */
    @Column(name="CONDITION_CODE")
    private int conditionCode;

    /**
     * 検索条件文字列
     */
    @Column(name="CONDITION_STRING")
    private String conditionString;

    /**
     * 削除
     */
    @Column(name="DELETED")
    private int deleted;

    /**
     * 登録日
     */
    @Column(name="REGIST_TIME")
    private Date registTime;

    /**
     * 登録者
     */
    @Column(name="REGIST_USER")
    private String registUser;

    /**
     * 更新日
     */
    @Column(name="UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新者
     */
    @Column(name="UPDATE_USER")
    private String updateUser;

    /**
     * 更新回数
     */
    @Column(name="UPDATE_COUNT")
    private int updateCount;
}
