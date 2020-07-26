package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cfiv.sysdev.rrs.entity.LoginTime;

public interface LoginTimeRepository extends JpaRepository<LoginTime, Long> {

}
