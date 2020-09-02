package com.cfiv.sysdev.rrs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cfiv.sysdev.rrs.entity.Account;

/**
 * ユーザー情報 Repository
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByUsername(String username);

    public List<Account> findByCompanyID(Long companyID);
}