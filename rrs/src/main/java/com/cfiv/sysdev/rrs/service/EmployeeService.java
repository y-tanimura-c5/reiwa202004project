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
 * ]‹Æˆõî•ñ Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EmployeeService {
    /**
     * ƒf[ƒ^ƒx[ƒXƒGƒ“ƒeƒBƒeƒBŠÇ—
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * ]‹Æˆõî•ñ Repository
     */
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * ]‹Æˆõî•ñ‘SŒŸõ
     * @return ŒŸõŒ‹‰Ê(Employee)
     */
    public List<Employee> searchAll() {
        return employeeRepository.findAll();
    }

    /**
     * ]‹Æˆõî•ñ‘SŒŸõ
     * @param companyID Šé‹ÆƒR[ƒh
     * @param employeeID Ğˆõ”Ô†
     * @return ŒŸõŒ‹‰Ê(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestAll() {
        return employeeToRequestList(searchAll());
    }

    /**
     * Šé‹ÆƒR[ƒhAĞˆõ”Ô†‚Å‚Ì]‹Æˆõî•ñŒŸõ
     * @param companyID Šé‹ÆƒR[ƒh
     * @param employeeID Ğˆõ”Ô†
     * @return ŒŸõŒ‹‰Ê(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<Employee> searchFromID(String companyID, String employeeID) {
        List<Employee> result = new ArrayList<Employee>();

        // ‚·‚×‚Äƒuƒ‰ƒ“ƒN‚¾‚Á‚½ê‡‚Í‘SŒŒŸõ‚·‚é
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
     * Šé‹ÆƒR[ƒhAĞˆõ”Ô†‚Å‚Ì]‹Æˆõî•ñŒŸõ
     * @param companyID Šé‹ÆƒR[ƒh
     * @param employeeID Ğˆõ”Ô†
     * @return ŒŸõŒ‹‰Ê(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestFromID(String companyID, String employeeID) {
        return employeeToRequestList(searchFromID(companyID, employeeID));
    }

    /**
     * ID‚Å‚Ì]‹Æˆõî•ñŒŸõ
     * @param id ID
     * @return ŒŸõŒ‹‰Ê(Employee)
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
     * ID‚Å‚Ì]‹Æˆõî•ñŒŸõ
     * @param id ID
     * @return ŒŸõŒ‹‰Ê(Employee)
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
     * ]‹Æˆõî•ñˆê——‚ÌŒ^•ÏŠ·(Employee¨EmployeeRequest)
     * @param list ]‹Æˆõî•ñˆê——(Employee)
     * @return ]‹Æˆõî•ñˆê——(EmployeeRequest)
     */
    public List<EmployeeRequest> employeeToRequestList(List<Employee> list) {
        List<EmployeeRequest> result = new ArrayList<EmployeeRequest>();
        for (Employee employee : list) {
            result.add(employee.toRequest());
        }

        return result;
    }

    /**
     * ]‹Æˆõî•ñCSV“o˜^
     * @param list ]‹Æˆõî•ñˆê——(EmployeeCSV)
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
     * ]‹Æˆõî•ñV‹K“o˜^
     * @param employee ]‹Æˆõî•ñ(Employee)
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
     * ]‹Æˆõî•ñV‹K“o˜^
     * @param employee ]‹Æˆõî•ñ(EmployeeRequest)
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
     * ]‹Æˆõî•ñV‹K“o˜^
     * @param employee ]‹Æˆõî•ñ(EmployeeCSV)
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
     * ]‹Æˆõî•ñ•Û‘¶
     * @param req ]‹Æˆõî•ñ(Employee)
     * @return •Û‘¶Œ‹‰Ê(Employee)
     */
    public Employee save(Employee employee) {
        Date now = new Date();

        employee.setUpdateUser("user");
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * ]‹Æˆõî•ñ•Û‘¶
     * @param id ID
     * @param req ]‹Æˆõî•ñ(EmployeeRequest)
     * @return •Û‘¶Œ‹‰Ê(Employee)
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
     * ]‹Æˆõî•ñ•Û‘¶
     * @param id ID
     * @param req ]‹Æˆõî•ñ(EmployeeCSV)
     * @return •Û‘¶Œ‹‰Ê(Employee)
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
     * ]‹Æˆõî•ñ˜_—íœ
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
     * ]‹Æˆõî•ñ•¨—íœ
     * @param id ID
     */
    public void deletePhisical(Long id) {
        employeeRepository.deleteById(id);
    }
}
