package com.cfiv.sysdev.rrs.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.cfiv.sysdev.rrs.dto.UserAddRequest;
import com.cfiv.sysdev.rrs.dto.UserEditRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.repository.AccountRepository;
import com.cfiv.sysdev.rrs.security.UserAccount;

/**
 * ユーザー情報 Service
 */
@Service
public class UserAccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * ユーザー情報 全検索
     * @return 検索結果
     */
    public List<Account> searchAll() {
        return accountRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmptyOrWhitespace(username)) {
            throw new UsernameNotFoundException("Username is empty");
        }

        Account ac = accountRepository.findByUsername(username);
        if (ac == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (!ac.isEnabled()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        UserAccount user = new UserAccount(ac);

        return user;
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account findOne(Long id) {
        Optional<Account> opt = accountRepository.findById(id);
        return opt.get();
    }

    /**
     * ユーザー情報新規登録
     * @param req ユーザー情報
     */
    @Transactional
    public void create(UserAddRequest req) {
        Date now = new Date();
        Account account = new Account();

        account.setUsername(req.getUsername());
        account.setPassword(passwordEncoder.encode(req.getPassword()));
        account.setDisplayName(req.getDisplayName());
        account.setUserRoleFromString(req.getUserRole());
        account.setCompanyIDFromName(req.getCompany());
        account.setEnabledFromString(req.getEnabled());
        account.setDeleted(false);
        account.setRegistUser("user");
        account.setRegistTime(now);
        account.setUpdateUser("user");
        account.setUpdateTime(now);

        accountRepository.save(account);
    }

    /**
     * ユーザー情報更新
     * @param id ID
     * @param req ユーザー情報
     */
    @Transactional
    public Account save(Long id, UserEditRequest req) {
        Date now = new Date();
        Account account = findOne(id);

        if (!req.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        account.setDisplayName(req.getDisplayName());
        account.setUserRoleFromString(req.getUserRole());
        account.setCompanyIDFromName(req.getCompany());
        account.setEnabledFromString(req.getEnabled());
        account.setUpdateTime(now);
        account.setUpdateUser("user");

        return accountRepository.save(account);
    }
}
