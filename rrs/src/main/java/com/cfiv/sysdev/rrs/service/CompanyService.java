package com.cfiv.sysdev.rrs.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.dto.CompanyRequest;
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
     * 企業情報 全検索
     * @return 検索結果
     */
    public List<Company> searchAll() {
        return companyRepository.findAll();
    }

    /**
     * 企業情報新規登録
     * @param company 企業情報
     */
    public void create(Company company) {
        Date now = new Date();

        company.setEnabled(true);
        company.setDeleted(false);
        company.setRegistTime(now);
        company.setRegistUser("user");
        company.setUpdateTime(now);
        company.setUpdateUser("user");

        companyRepository.save(company);
    }

    public void create(CompanyRequest req) {
        Date now = new Date();
        Company company = new Company();

        company.setName(req.getName());
        company.setEnabledFromString(req.getEnabled());
        company.setDeleted(false);
        company.setRegistTime(now);
        company.setRegistUser("user");
        company.setUpdateTime(now);
        company.setUpdateUser("user");

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

        company.setName(req.getName());
        company.setEnabledFromString(req.getEnabled());
        company.setDeleted(false);
        company.setRegistTime(now);
        company.setRegistUser("user");
        company.setUpdateTime(now);
        company.setUpdateUser("user");

        return companyRepository.save(company);
    }

    public Company save(Company company) {
        Date now = new Date();

        company.setDeleted(false);
        company.setRegistTime(now);
        company.setRegistUser("user");
        company.setUpdateTime(now);
        company.setUpdateUser("user");

        return companyRepository.save(company);
    }

    public void delete(Long id) {
        companyRepository.deleteById(id);
    }

    /**
     * リスト表示用企業情報(全件)
     * @return 検索結果
     */
    public Map<String, String> getAllCompanyNames() {
        List<Company> company_list = searchAll();
        Map<String, String> result = new LinkedHashMap<String, String>();

        for (Company company : company_list) {
            if (company.isEnabled()) {
                result.put(company.getIdString(4), company.getIdString(4) + ":" + company.getName());
            }
        }

        return result;
    }

    /**
     * リスト表示用企業情報(id指定)
     * @return 検索結果
     */
    public String getCompanyName(Long id) {
        Company company = findOne(id);

        return company.getIdString(4) + ":" + company.getName();
    }
}
