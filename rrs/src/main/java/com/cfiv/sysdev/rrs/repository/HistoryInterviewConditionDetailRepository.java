package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.HistoryInterviewConditionDetail;

/**
 * 面談結果検索条件履歴詳細 Repository
 */
@Repository
public interface HistoryInterviewConditionDetailRepository extends JpaRepository<HistoryInterviewConditionDetail, Long> {

}
