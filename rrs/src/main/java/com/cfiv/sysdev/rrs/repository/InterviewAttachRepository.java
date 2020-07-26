package com.cfiv.sysdev.rrs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.InterviewAttach;

/**
 * 面談添付ファイル Repository
 */
@Repository
public interface InterviewAttachRepository extends JpaRepository<InterviewAttach, Long> {
    public List<InterviewAttach> findByResultID(Long resultID);
}
