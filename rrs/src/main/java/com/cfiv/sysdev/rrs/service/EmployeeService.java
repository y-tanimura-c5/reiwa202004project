package com.cfiv.sysdev.rrs.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.dto.EmployeeRequest;
import com.cfiv.sysdev.rrs.entity.Employee;
import com.cfiv.sysdev.rrs.repository.EmployeeRepository;

/**
 * �]�ƈ���� Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EmployeeService {

    /**
     * �]�ƈ���� Repository
     */
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * �]�ƈ���� �S����
     * @return ��������
     */
    public List<Employee> searchAll() {
        return employeeRepository.findAll();
    }

    /**
     * ��ƃR�[�h�ł̏]�ƈ���񌟍�
     * @return ��������
     */
    public List<Employee> searchFromCompanyID(Long id) {
        return employeeRepository.findAll();
    }

    /**
     * �Ј��ԍ��ł̏]�ƈ���񌟍�
     * @return ��������
     */
    public List<Employee> searchFromEmployeeID(String id) {
        return employeeRepository.findAll();
    }

    /**
     * ��Ə��V�K�o�^
     * @param company ��Ə��
     */
    public void create(Employee employee) {
        Date now = new Date();

        employee.setDeleted(false);
        employee.setRegistTime(now);
        employee.setRegistUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateUser("user");

        employeeRepository.save(employee);
    }

    public void create(EmployeeRequest req) {
        Date now = new Date();
        Employee employee = new Employee();

        try {
            employee.setCompanyID(Long.parseLong(req.getCompanyID()));
        }
        catch (NumberFormatException e) {
            employee.setCompanyID(0L);
        }

        employee.setEmployeeID(req.getEmployeeID());
        employee.setEmployeeFName(req.getEmployeeFName());
        employee.setHireYM(req.getHireYM());
        employee.setAdoptCodeFromString(req.getAdoptCode());
        employee.setSupportCodeFromString(req.getSupportCode());
        employee.setEmployCodeFromString(req.getEmployCode());
        employee.setDeleted(false);
        employee.setRegistUser("user");
        employee.setRegistTime(now);
        employee.setUpdateUser("user");
        employee.setUpdateTime(now);

        employeeRepository.save(employee);
    }

    public Employee findOne(Long id) {
        Optional<Employee> opt = employeeRepository.findById(id);

        try {
            return opt.get();
        }
        catch (Exception e) {
            return new Employee();
        }
    }

    public Employee save(Long id, EmployeeRequest req) {
        Date now = new Date();
        Employee employee = findOne(id);

        try {
            employee.setCompanyID(Long.parseLong(req.getCompanyID()));
        }
        catch (NumberFormatException e) {
            employee.setCompanyID(0L);
        }

        employee.setEmployeeID(req.getEmployeeID());
        employee.setEmployeeFName(req.getEmployeeFName());
        employee.setHireYM(req.getHireYM());
        employee.setAdoptCodeFromString(req.getAdoptCode());
        employee.setSupportCodeFromString(req.getSupportCode());
        employee.setEmployCodeFromString(req.getEmployCode());
        employee.setUpdateUser("user");
        employee.setUpdateTime(now);

        return employeeRepository.save(employee);
    }

    public Employee save(Employee employee) {
        Date now = new Date();

        employee.setUpdateUser("user");
        employee.setUpdateTime(now);

        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
