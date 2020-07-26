package com.cfiv.sysdev.rrs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.InterviewCondition;

/**
 * 面談結果検索条件 Repository
 */
@Repository
public interface InterviewConditionRepository extends JpaRepository<InterviewCondition, Long> {
    public List<InterviewCondition> findByUsername(String username);
    public List<InterviewCondition> findByUsernameAndConditionKind(String username, int conditionKind);
}
