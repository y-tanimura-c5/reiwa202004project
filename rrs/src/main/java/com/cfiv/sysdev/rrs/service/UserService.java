package com.cfiv.sysdev.rrs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.Utils;
import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.repository.AccountRepository;
import com.cfiv.sysdev.rrs.security.UserAccount;

/**
 * ユーザー情報 Service
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 企業情報 Service
     */
    @Autowired
    CompanyService companyService;

    /**
     * ユーザー情報 全検索
     * @return 検索結果
     */
    public List<Account> searchAll() {
        return accountRepository.findAll();
    }

    /**
     * ユーザー情報 企業コード指定検索
     * @return 検索結果
     */
    public List<Account> searchFromCompanyID(Long companyID) {
        return accountRepository.findByCompanyID(companyID);
    }

    /**
     * 企業コード 全検索
     * @param pageable ページング条件
     * @return 検索結果ページ(UserRequest)
     */
    public Page<UserRequest> search(Pageable pageable, UserRequest lReq) {
        List<UserRequest> reqList = new ArrayList<UserRequest>();
        List<Account> list;

        if (lReq.getUserRoleCode() == Consts.USERROLECODE_ADMIN) {
            list = searchAll();
        }
        else {
            list = searchFromCompanyID(lReq.getCompanyIDLong());
        }

        for (Account account : list) {
            reqList.add(account.toRequest(companyService.getCompanyNameForDropdown(account.getCompanyID())));
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<UserRequest> pageList;

        if (list.size() < startItem) {
            pageList = Collections.emptyList();
        }
        else {
            int toIndex = Math.min(startItem + pageSize, list.size());
            pageList = reqList.subList(startItem, toIndex);
        }

        Page<UserRequest> reqPage = new PageImpl<UserRequest>(pageList, PageRequest.of(currentPage, pageSize), reqList.size());

        return reqPage;
    }

    /**
     * ユーザーIDからのユーザー情報(SpringSecurity用)
     * @param username ユーザーID
     * @return ユーザー情報(UserAccount)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmptyOrWhitespace(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }

        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (account.getEnabled() == Consts.UNABLED) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserAccount user = new UserAccount(account);

        return user;
    }

    /**
     * ログインユーザー情報
     * @param username ユーザーID
     * @return ユーザー情報(Account)
     */
    public UserRequest getLoginAccount() {
        Account account = accountRepository.findByUsername(Utils.loginUsername());
        return account.toRequest(companyService.getCompanyNameForDropdown(account.getCompanyID()));
    }

    /**
     * ユーザーIDからのユーザー情報
     * @param username ユーザーID
     * @return ユーザー情報(Account)
     */
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * ユーザーIDからのユーザー表示名
     * @param username ユーザーID
     * @return ユーザー表示名
     */
    public String getDisplayNameFromUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        return account.getDisplayName();
    }

    /**
     * IDからのユーザー情報(SpringSecurity用)
     * @param id ID
     * @return ユーザー情報(Account)
     */
    public Account findOne(Long id) {
        Optional<Account> opt = accountRepository.findById(id);
        return opt.get();
    }

    /**
     * ユーザー情報新規登録
     * @param req ユーザー情報
     */
    @Transactional
    public void create(UserRequest req) {
        UserRequest lReq = getLoginAccount();
        Date now = new Date();
        Account account = new Account();

        account.setUsername(req.getUsername());
        account.setPassword(passwordEncoder.encode(req.getPassword()));
        account.setDisplayName(req.getDisplayName());

        switch (lReq.getUserRoleCode()) {
        case Consts.USERROLECODE_ADMIN:
            if (req.getUserRoleCode() == Consts.USERROLECODE_ADMIN) {
                account.setCompanyID(Consts.COMPANYID_ADMIN);
            }
            else {
                account.setCompanyIDFromName(req.getCompanyName());
            }
            account.setUserRole(req.getUserRoleCode());
            account.setEnabled(req.getEnabled());
            break;

        case Consts.USERROLECODE_CLIENTADMIN:
            account.setCompanyIDFromName(lReq.getCompanyName());
            account.setUserRole(req.getUserRoleCode());
            account.setEnabled(req.getEnabled());
            break;
        }

        account.setDeleted(Consts.EXIST);
        account.setRegistUser(Utils.loginUsername());
        account.setRegistTime(now);
        account.setUpdateUser(Utils.loginUsername());
        account.setUpdateTime(now);

        accountRepository.save(account);
    }

    /**
     * ユーザー情報更新
     * @param id ID
     * @param req ユーザー情報
     */
    @Transactional
    public Account save(Long id, UserRequest req) {
        UserRequest lReq = getLoginAccount();
        Date now = new Date();
        Account account = findOne(id);

        if (!req.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        account.setDisplayName(req.getDisplayName());

        switch (lReq.getUserRoleCode()) {
        case Consts.USERROLECODE_ADMIN:
            if (req.getUserRoleCode() == Consts.USERROLECODE_ADMIN) {
                account.setCompanyID(Consts.COMPANYID_ADMIN);
            }
            else {
                account.setCompanyIDFromName(req.getCompanyName());
            }
            account.setUserRole(req.getUserRoleCode());
            account.setEnabled(req.getEnabled());
            break;

        case Consts.USERROLECODE_CLIENTADMIN:
            account.setUserRole(req.getUserRoleCode());
            account.setEnabled(req.getEnabled());
            break;
        }

        account.setUpdateTime(now);
        account.setUpdateUser(Utils.loginUsername());
        account.setUpdateCount(account.getUpdateCount() + 1);

        return accountRepository.save(account);
    }
}
