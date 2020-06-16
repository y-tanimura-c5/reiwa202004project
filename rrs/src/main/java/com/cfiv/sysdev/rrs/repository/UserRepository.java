package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.User;

/**
 * ÉÜÅ[ÉUÅ[èÓïÒ Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {}