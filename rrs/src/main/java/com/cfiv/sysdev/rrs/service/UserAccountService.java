package com.cfiv.sysdev.rrs.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.cfiv.sysdev.rrs.entity.Account;
import com.cfiv.sysdev.rrs.entity.UserAccount;
import com.cfiv.sysdev.rrs.repository.AccountRepository;

/**
 * ÉÜÅ[ÉUÅ[èÓïÒ Service
 */
@Service
public class UserAccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Transactional
    public void registerUser(String username, String password, String displayname, int userrole,
            long company_id, boolean enabled, String createuser) {
        Account user = new Account(username, passwordEncoder.encode(password), displayname,
                userrole, company_id, enabled, createuser);

        accountRepository.save(user);
    }
}
