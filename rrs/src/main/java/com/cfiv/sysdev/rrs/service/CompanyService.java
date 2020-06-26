package com.cfiv.sysdev.rrs.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.dto.CompanyRequest;
import com.cfiv.sysdev.rrs.entity.Company;
import com.cfiv.sysdev.rrs.repository.CompanyRepository;

/**
 * ��Ə�� Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class CompanyService {

    /**
     * ��Ə�� Repository
     */
    @Autowired
    CompanyRepository companyRepository;

    /**
     * ��Ə�� �S����
     * @return ��������
     */
    public List<Company> searchAll() {
        return companyRepository.findAll();
    }

    /**
     * ��Ə��V�K�o�^
     * @param company ��Ə��
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
        return opt.get();
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
}
