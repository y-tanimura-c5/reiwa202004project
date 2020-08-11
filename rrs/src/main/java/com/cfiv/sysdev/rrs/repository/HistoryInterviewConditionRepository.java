package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.HistoryInterviewCondition;

/**
 * 面談結果検索条件履歴 Repository
 */
@Repository
public interface HistoryInterviewConditionRepository extends JpaRepository<HistoryInterviewCondition, Long> {

}
