package com.cfiv.sysdev.rrs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.dto.EmployeeCSV;
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
     * �f�[�^�x�[�X�G���e�B�e�B�Ǘ�
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * �]�ƈ���� Repository
     */
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * �]�ƈ����S����
     * @return ��������(Employee)
     */
    public List<Employee> searchAll() {
        return employeeRepository.findAll();
    }

    /**
     * �]�ƈ����S����
     * @param companyID ��ƃR�[�h
     * @param employeeID �Ј��ԍ�
     * @return ��������(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestAll() {
        return employeeToRequestList(searchAll());
    }

    /**
     * ��ƃR�[�h�A�Ј��ԍ��ł̏]�ƈ���񌟍�
     * @param companyID ��ƃR�[�h
     * @param employeeID �Ј��ԍ�
     * @return ��������(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<Employee> searchFromID(String companyID, String employeeID) {
        List<Employee> result = new ArrayList<Employee>();

        // ���ׂău�����N�������ꍇ�͑S����������
        if (companyID.isEmpty() && employeeID.isEmpty()){
            result = employeeRepository.findAll();
        }
        else {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM Employee e WHERE ");

            boolean isNeedAnd = false;

            Long companyIDLong = null;
            if (!companyID.isEmpty()) {
                try {
                    companyIDLong = Long.parseLong(companyID);
                    sql.append("e.companyID = :companyID");
                    isNeedAnd = true;
                }
                catch (NumberFormatException e) {
                    companyIDLong = null;
                }
            }

            if (!employeeID.isEmpty()) {
                if (isNeedAnd) {
                    sql.append(" AND ");
                }
                sql.append("e.employeeID = :employeeID");
            }

            Query query = entityManager.createQuery(sql.toString());

            if (companyIDLong != null) {
                query.setParameter("companyID", companyIDLong);
            }
            if (!employeeID.isEmpty()) {
                query.setParameter("employeeID", employeeID);
            }

            result = (List<Employee>) query.getResultList();
        }

        return result;
    }

    /**
     * ��ƃR�[�h�A�Ј��ԍ��ł̏]�ƈ���񌟍�
     * @param companyID ��ƃR�[�h
     * @param employeeID �Ј��ԍ�
     * @return ��������(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestFromID(String companyID, String employeeID) {
        return employeeToRequestList(searchFromID(companyID, employeeID));
    }

    /**
     * ID�ł̏]�ƈ���񌟍�
     * @param id ID
     * @return ��������(Employee)
     */
    public Employee findOne(Long id) {
        Optional<Employee> opt = employeeRepository.findById(id);

        try {
            return opt.get();
        }
        catch (Exception e) {
            return new Employee();
        }
    }

    /**
     * ID�ł̏]�ƈ���񌟍�
     * @param id ID
     * @return ��������(Employee)
     */
    public EmployeeRequest findOneRequest(Long id) {
        Employee employee = findOne(id);

        if (employee != null) {
            return employee.toRequest();
        }
        else {
            return null;
        }
    }

    /**
     * �]�ƈ����ꗗ�̌^�ϊ�(Employee��EmployeeRequest)
     * @param list �]�ƈ����ꗗ(Employee)
     * @return �]�ƈ����ꗗ(EmployeeRequest)
     */
    public List<EmployeeRequest> employeeToRequestList(List<Employee> list) {
        List<EmployeeRequest> result = new ArrayList<EmployeeRequest>();
        for (Employee employee : list) {
            result.add(employee.toRequest());
        }

        return result;
    }

    /**
     * �]�ƈ����CSV�o�^
     * @param list �]�ƈ����ꗗ(EmployeeCSV)
     */
    public void saveCSV(List<EmployeeCSV> list) {
        for (EmployeeCSV csv : list) {
            List<Employee> employees = searchFromID(csv.getCompanyID(), csv.getEmployeeID());

            if (employees.size() > 0) {
                save(employees.get(0).getId(), csv);
            }
            else {
                create(csv);
            }
        }
    }

    /**
     * �]�ƈ����V�K�o�^
     * @param employee �]�ƈ����(Employee)
     */
    public void create(Employee employee) {
        Date now = new Date();

        employee.setDeleted(false);
        employee.setRegistTime(now);
        employee.setRegistUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateUser("user");
        employee.setUpdateCount(0);

        employeeRepository.save(employee);
    }

    /**
     * �]�ƈ����V�K�o�^
     * @param employee �]�ƈ����(EmployeeRequest)
     */
    public void create(EmployeeRequest req) {
        Date now = new Date();
        Employee employee = req.toEmployee();

        employee.setDeleted(false);
        employee.setRegistUser("user");
        employee.setRegistTime(now);
        employee.setUpdateUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateCount(0);

        employeeRepository.save(employee);
    }

    /**
     * �]�ƈ����V�K�o�^
     * @param employee �]�ƈ����(EmployeeCSV)
     */
    public void create(EmployeeCSV req) {
        Date now = new Date();
        Employee employee = req.toEmployee();

        employee.setEmployCode(0);
        employee.setDeleted(false);
        employee.setRegistUser("user");
        employee.setRegistTime(now);
        employee.setUpdateUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateCount(0);

        employeeRepository.save(employee);
    }

    /**
     * �]�ƈ����ۑ�
     * @param req �]�ƈ����(Employee)
     * @return �ۑ�����(Employee)
     */
    public Employee save(Employee employee) {
        Date now = new Date();

        employee.setUpdateUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * �]�ƈ����ۑ�
     * @param id ID
     * @param req �]�ƈ����(EmployeeRequest)
     * @return �ۑ�����(Employee)
     */
    public Employee save(Long id, EmployeeRequest req) {
        Employee employee = findOne(id);

        if (employee == null) {
            return null;
        }

        Date now = new Date();
        employee.setCompanyIDFromString(req.getCompanyID());
        employee.setEmployeeID(req.getEmployeeID());
        employee.setEmployeeFName(req.getEmployeeFName());
        employee.setHireYM(req.getHireYM());
        employee.setAdoptCodeFromString(req.getAdoptCode());
        employee.setSupportCodeFromString(req.getSupportCode());
        employee.setEmployCodeFromString(req.getEmployCode());
        employee.setUpdateUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * �]�ƈ����ۑ�
     * @param id ID
     * @param req �]�ƈ����(EmployeeCSV)
     * @return �ۑ�����(Employee)
     */
    public Employee save(Long id, EmployeeCSV req) {
        Employee employee = findOne(id);

        if (employee == null) {
            return null;
        }

        Date now = new Date();
        employee.setCompanyIDFromString(req.getCompanyID());
        employee.setEmployeeID(req.getEmployeeID());
        employee.setEmployeeFName(req.getEmployeeFName());
        employee.setHireYM(req.getHireYM());
        employee.setAdoptCodeFromString(req.getAdoptCode());
        employee.setSupportCodeFromString(req.getSupportCode());
        employee.setUpdateUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * �]�ƈ����_���폜
     * @param id ID
     */
    public void deleteLogical(Long id) {
        Employee employee = findOne(id);

        if (employee == null) {
            return;
        }

        employee.setDeleted(true);
        employeeRepository.save(employee);
    }

    /**
     * �]�ƈ���񕨗��폜
     * @param id ID
     */
    public void deletePhisical(Long id) {
        employeeRepository.deleteById(id);
    }
}
