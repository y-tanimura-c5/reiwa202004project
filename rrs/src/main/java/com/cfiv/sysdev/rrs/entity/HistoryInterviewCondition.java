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
 * 面談結果検索条件履歴 Entity
 */
@Entity
@Data
@Table(name = "H_INTERVIEW_CONDITION")
public class HistoryInterviewCondition implements Serializable {
    public HistoryInterviewCondition() {
        super();
    }

    public HistoryInterviewCondition(String u) {
        this();

        Date now = new Date();

        setUsername(u);
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
     * ユーザー名
     */
    @Column(name = "USERNAME", nullable = false)
    private String username;

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
