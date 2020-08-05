package com.cfiv.sysdev.rrs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.dto.CompanyRequest;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.repository.CompanyRepository;

/**
 * 企業情報 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class CompanyService {

    /**
     * 企業情報 Repository
     */
    @Autowired
    CompanyRepository companyRepository;

    /**
     * ログイン日時情報 Service
     */
    @Autowired
    LoginTimeService loginTimeService;

    /**
     * 面談情報 Service
     */
    @Autowired
    InterviewService interviewService;

    /**
     * 企業情報 全検索
     * @return 検索結果
     */
    public List<Company> searchAll() {
        return companyRepository.findAll();
    }

    /**
     * 企業コード 全検索
     * @param pageable ページング条件
     * @return 検索結果ページ(CompanyRequest)
     */
    public Page<CompanyRequest> search(Pageable pageable) {
        List<CompanyRequest> reqList = new ArrayList<CompanyRequest>();
        List<Company> list = searchAll();
        for (Company company : list) {
            Date lastlogin = loginTimeService.getLastLoginTimeFromCompanyID(company.getId());
            Date lastInterview = interviewService.getLastInterviewDateFromCompanyID(company.getId());

            reqList.add(company.toRequest(lastlogin, lastInterview));
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<CompanyRequest> pageList;

        if (list.size() < startItem) {
            pageList = Collections.emptyList();
        }
        else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageList = reqList.subList(startItem, toIndex);
        }

        Page<CompanyRequest> reqPage = new PageImpl<CompanyRequest>(pageList, PageRequest.of(currentPage, pageSize), reqList.size());

        return reqPage;
    }

    /**
     * 企業情報新規登録
     * @param company 企業情報
     */
    public void create(Company company) {
        Date now = new Date();
        String loginUser = Utils.loginUsername();

        company.setEnabled(Consts.ENABLED);
        company.setDeleted(Consts.EXIST);
        company.setRegistTime(now);
        company.setRegistUser(loginUser);
        company.setUpdateTime(now);
        company.setUpdateUser(loginUser);

        companyRepository.save(company);
    }

    public void create(CompanyRequest req) {
        Date now = new Date();
        Company company = new Company();
        String loginUser = Utils.loginUsername();

        company.setName(req.getName());
        company.setEnabled(req.getEnabled());
        company.setDeleted(Consts.EXIST);
        company.setRegistTime(now);
        company.setRegistUser(loginUser);
        company.setUpdateTime(now);
        company.setUpdateUser(loginUser);

        companyRepository.save(company);
    }

    public Company findOne(Long id) {
        Optional<Company> opt = companyRepository.findById(id);

        try {
            return opt.get();
        }
        catch (Exception e) {
            return new Company();
        }
    }

    public Company save(Long id, CompanyRequest req) {
        Date now = new Date();
        Company company = findOne(id);
        String loginUser = Utils.loginUsername();

        company.setName(req.getName());
        company.setEnabled(req.getEnabled());
        company.setDeleted(Consts.EXIST);
        company.setRegistTime(now);
        company.setRegistUser(loginUser);
        company.setUpdateTime(now);
        company.setUpdateUser(loginUser);
        company.setUpdateCount(company.getUpdateCount() + 1);

        return companyRepository.save(company);
    }

    public Company save(Company company) {
        Date now = new Date();
        String loginUser = Utils.loginUsername();

        company.setDeleted(Consts.EXIST);
        company.setRegistTime(now);
        company.setRegistUser(loginUser);
        company.setUpdateTime(now);
        company.setUpdateUser(loginUser);
        company.setUpdateCount(company.getUpdateCount() + 1);

        return companyRepository.save(company);
    }

    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

    /**
     * リスト表示用企業情報(全件)
     * @return 検索結果
     */
    public Map<String, String> getAllCompanyNamesForDropdown(UserRequest loginUser) {
        Map<String, String> result = new LinkedHashMap<String, String>();

        if (loginUser.getUserRoleCode() == Consts.USERROLECODE_ADMIN) {
            List<Company> company_list = searchAll();

            for (Company company : company_list) {
                if (company.getEnabled() == Consts.ENABLED) {
                    result.put(company.getIdString(4), company.getIdString(4) + ":" + company.getName());
                }
            }
        }
        else {
            String companyName = getCompanyName(Utils.getLongFromString(loginUser.getCompanyID()));
            result.put(loginUser.getCompanyID(), loginUser.getCompanyID() + ":" + companyName);
        }

        return result;
    }

    /**
     * 企業名称
     * @return 検索結果
     */
    public String getCompanyName(Long id) {
        Company company = findOne(id);

        return company.getName();
    }

    /**
     * リスト表示用企業情報(id指定)
     * @return 検索結果
     */
    public String getCompanyNameForDropdown(Long id) {
        String result;

        if (id == Consts.COMPANYID_ADMIN) {
            result = "－";
        }
        else {
            Company company = findOne(id);
            result = company.getIdString(4) + ":" + company.getName();
        }

        return result;
    }
}
