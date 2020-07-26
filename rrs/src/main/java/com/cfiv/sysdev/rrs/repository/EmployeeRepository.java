package com.cfiv.sysdev.rrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cfiv.sysdev.rrs.entity.Employee;

/**
 * 従業員情報 Repository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}