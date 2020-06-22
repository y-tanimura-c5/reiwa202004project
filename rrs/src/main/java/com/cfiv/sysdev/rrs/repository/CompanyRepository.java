package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.Company;

/**
 * ��Ə�� Repository
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {}