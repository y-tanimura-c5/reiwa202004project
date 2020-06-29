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

import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.entity.UserAccount;
import com.cfiv.sysdev.rrs.repository.AccountRepository;

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

    /**
     * ユーザー情報新規登録
     * @param user ユーザー情報
     */
    @Transactional
    public void create(UserRequest req) {
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

    public Account findOne(Long id) {
        Optional<Account> opt = accountRepository.findById(id);
        return opt.get();
    }

    @Transactional
    public Account save(Long id, UserRequest req) {
        Date now = new Date();
        Account account = findOne(id);

        account.setUsername(req.getUsername());
        account.setPassword(passwordEncoder.encode(req.getPassword()));
        account.setDisplayName(req.getDisplayName());
        account.setUserRoleFromString(req.getUserRole());
        account.setCompanyIDFromName(req.getCompany());
        account.setEnabledFromString(req.getEnabled());
        account.setDeleted(false);
        account.setRegistTime(now);
        account.setRegistUser("user");
        account.setUpdateTime(now);
        account.setUpdateUser("user");

        return accountRepository.save(account);
    }

    @Transactional
    public Account save(Account account) {
        Date now = new Date();

        account.setDeleted(false);
        account.setRegistTime(now);
        account.setRegistUser("user");
        account.setUpdateTime(now);
        account.setUpdateUser("user");

        return accountRepository.save(account);
    }
}
