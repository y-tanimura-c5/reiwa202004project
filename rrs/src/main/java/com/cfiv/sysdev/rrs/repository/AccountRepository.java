package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cfiv.sysdev.rrs.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByUsername(String username);

}