package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cfiv.sysdev.rrs.entity.LoginTime;

/**
 * ログイン時刻 Repository
 */
public interface LoginTimeRepository extends JpaRepository<LoginTime, Long> {

}
