package com.cfiv.sysdev.rrs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.csv.EmployeeCSV;
import com.cfiv.sysdev.rrs.entity.Employee;
import com.cfiv.sysdev.rrs.repository.EmployeeRepository;
import com.cfiv.sysdev.rrs.request.EmployeeRequest;

/**
 * 従業員情報 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class EmployeeService {
    static final String AND = " AND ";

    /**
     * データベースエンティティ管理
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 従業員情報 Repository
     */
    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * 従業員情報全検索
     * @return 検索結果(Employee)
     */
    public List<Employee> searchAll() {
        return employeeRepository.findAll();
    }

    /**
     * 従業員情報全検索
     * @return 検索結果(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestAll() {
        return employeeToRequestList(searchAll());
    }

    /**
     * 企業コード、従業員番号での従業員情報検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(Employee)
     */
    @SuppressWarnings("unchecked")
    public List<Employee> searchFromID(String companyID, String employeeCode) {

        StringBuilder sql = new StringBuilder();
        String deletedTag = "deleted";
        String select = "SELECT DISTINCT e FROM Employee e WHERE e.deleted = :" + deletedTag;
        String orderBy = " ORDER BY e.companyID, e.employeeCode";

        String companyIDTag = "companyID";
        String employeeCodeTag = "employeeCode";
        String companyIDCond = "e.companyID = :" + companyIDTag;
        String employeeCodeCond = "e.employeeCode = :" + employeeCodeTag;

        sql.append(select);

        Long companyIDLong = null;
        if (!companyID.isEmpty()) {
            try {
                companyIDLong = Long.parseLong(companyID);
            }
            catch (NumberFormatException e) {
                return new ArrayList<Employee>();
            }
        }

        if (companyIDLong != null) {
            sql.append(AND + companyIDCond);
        }

        if (!employeeCode.isEmpty()) {
            sql.append(AND + employeeCodeCond);
        }
        sql.append(orderBy);

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter(deletedTag, Consts.EXIST);
        if (companyIDLong != null) {
            query.setParameter(companyIDTag, companyIDLong);
        }
        if (!employeeCode.isEmpty()) {
            query.setParameter(employeeCodeTag, employeeCode);
        }

        return (List<Employee>) query.getResultList();
    }

    /**
     * 企業コード、従業員番号での従業員情報取得
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(EmployeeRequest)
     */
    public EmployeeRequest findOneRequestFromID(Long companyID, String employeeCode) {
        return findOneRequestFromID(Long.toString(companyID), employeeCode);
    }

    /**
     * 企業コード、従業員番号での従業員情報取得
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(EmployeeRequest)
     */
    public EmployeeRequest findOneRequestFromID(String companyID, String employeeCode) {
        List<Employee> emp_list = searchFromID(companyID, employeeCode);
        if (!emp_list.isEmpty()) {
            return emp_list.get(0).toRequest();
        }
        else {
            return null;
        }
    }

    /**
     * 企業コード、従業員番号での従業員情報検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestFromID(Long companyID, String employeeCode) {
        return employeeToRequestList(searchFromID(Long.toString(companyID), employeeCode));
    }

    /**
     * 企業コード、従業員番号での従業員情報検索
     * @param companyID 企業コード
     * @param employeeCode 従業員番号
     * @return 検索結果(EmployeeRequest)
     */
    public List<EmployeeRequest> searchRequestFromID(String companyID, String employeeCode) {
        return employeeToRequestList(searchFromID(companyID, employeeCode));
    }

    /**
     * 企業コード、従業員番号での面談結果検索
     * @param req 検索条件
     * @param pageable ページング条件
     * @return 検索結果ページ(InterviewRequest)
     */
    public Page<EmployeeRequest> searchRequestFromID(String companyID, String employeeCode, Pageable pageable) {
        List<EmployeeRequest> reqList = searchRequestFromID(companyID, employeeCode);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<EmployeeRequest> pageList;

        if (reqList.size() < startItem) {
            pageList = Collections.emptyList();
        }
        else {
            int toIndex = Math.min(startItem + pageSize, reqList.size());
            pageList = reqList.subList(startItem, toIndex);
        }

        Page<EmployeeRequest> reqPage = new PageImpl<EmployeeRequest>(pageList, PageRequest.of(currentPage, pageSize), reqList.size());

        return reqPage;
    }

    /**
     * IDでの従業員情報検索
     * @param id ID
     * @return 検索結果(Employee)
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
     * IDでの従業員情報検索
     * @param id ID
     * @return 検索結果(Employee)
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
     * 従業員情報一覧の型変換(Employee→EmployeeRequest)
     * @param list 従業員情報一覧(Employee)
     * @return 従業員情報一覧(EmployeeRequest)
     */
    public List<EmployeeRequest> employeeToRequestList(List<Employee> list) {
        List<EmployeeRequest> result = new ArrayList<EmployeeRequest>();
        for (Employee employee : list) {
            result.add(employee.toRequest());
        }

        return result;
    }

    /**
     * 従業員情報CSV登録
     * @param list 従業員情報一覧(EmployeeCSV)
     */
    public void saveCSV(List<EmployeeCSV> list) {
        for (EmployeeCSV csv : list) {
            List<Employee> employees = searchFromID(csv.getCompanyID(), csv.getEmployeeCode());

            if (employees.size() > 0) {
                save(employees.get(0).getId(), csv);
            }
            else {
                create(csv);
            }
        }
    }

    /**
     * 従業員情報新規登録
     * @param employee 従業員情報(Employee)
     */
    public void create(Employee employee) {
        Date now = new Date();
        String loginUser = Utils.loginUsername();

        employee.setDeleted(Consts.EXIST);
        employee.setRegistTime(now);
        employee.setRegistUser(loginUser);
        employee.setUpdateTime(now);
        employee.setUpdateUser(loginUser);
        employee.setUpdateCount(0);

        employeeRepository.save(employee);
    }

    /**
     * 従業員情報新規登録
     * @param employee 従業員情報(EmployeeRequest)
     */
    public void create(EmployeeRequest req) {
        Date now = new Date();
        Employee employee = req.toEmployee();
        String loginUser = Utils.loginUsername();

        employee.setDeleted(Consts.EXIST);
        employee.setRegistUser(loginUser);
        employee.setRegistTime(now);
        employee.setUpdateUser(loginUser);
        employee.setUpdateTime(now);
        employee.setUpdateCount(0);

        employeeRepository.save(employee);
    }

    /**
     * 従業員情報新規登録
     * @param employee 従業員情報(EmployeeCSV)
     */
    public void create(EmployeeCSV req) {
        Date now = new Date();
        Employee employee = req.toEmployee();
        String loginUser = Utils.loginUsername();

        employee.setEmployCode(0);
        employee.setDeleted(Consts.EXIST);
        employee.setRegistUser(loginUser);
        employee.setRegistTime(now);
        employee.setUpdateUser(loginUser);
        employee.setUpdateTime(now);
        employee.setUpdateCount(0);

        employeeRepository.save(employee);
    }

    /**
     * 従業員情報保存
     * @param req 従業員情報(Employee)
     * @return 保存結果(Employee)
     */
    public Employee save(Employee employee) {
        Date now = new Date();
        String loginUser = Utils.loginUsername();

        employee.setUpdateUser(loginUser);
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * 従業員情報保存
     * @param id ID
     * @param req 従業員情報(EmployeeRequest)
     * @return 保存結果(Employee)
     */
    public Employee save(Long id, EmployeeRequest req) {
        Employee employee = findOne(id);

        if (employee == null) {
            return null;
        }

        Date now = new Date();
        String loginUser = Utils.loginUsername();

        employee.setEmployCode(req.getEmployCode());
        employee.setUpdateUser(loginUser);
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * 従業員情報保存
     * @param id ID
     * @param req 従業員情報(EmployeeCSV)
     * @return 保存結果(Employee)
     */
    public Employee save(Long id, EmployeeCSV req) {
        Employee employee = findOne(id);

        if (employee == null) {
            return null;
        }

        Date now = new Date();
        String loginUser = Utils.loginUsername();

        employee.setCompanyID(req.getCompanyIDLong());
        employee.setEmployeeCode(req.getEmployeeCode());
        employee.setEmployeeFName(req.getEmployeeFName());
        employee.setHireYM(req.getHireYMDate());
        employee.setAdoptCode(req.getAdoptCodeInteger());
        employee.setSupportCode(req.getSupportCodeInteger());
        employee.setUpdateUser(loginUser);
        employee.setUpdateTime(now);
        employee.setUpdateCount(employee.getUpdateCount() + 1);

        return employeeRepository.save(employee);
    }

    /**
     * 従業員情報論理削除
     * @param id ID
     */
    public void deleteLogical(Long id) {
        Employee employee = findOne(id);

        if (employee == null) {
            return;
        }

        employee.setDeleted(Consts.DELETED);
        employeeRepository.save(employee);
    }

    /**
     * 従業員情報物理削除
     * @param id ID
     */
    public void deletePhisical(Long id) {
        employeeRepository.deleteById(id);
    }
}
