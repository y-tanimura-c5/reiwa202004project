package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.InterviewResult;

/**
 * �ʒk���� Repository
 */
@Repository
public interface InterviewResultRepository extends JpaRepository<InterviewResult, Long> {}