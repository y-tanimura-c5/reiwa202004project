package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.InterviewContent;

/**
 * 面談内容 Repository
 */
@Repository
public interface InterviewContentRepository extends JpaRepository<InterviewContent, Long> {}