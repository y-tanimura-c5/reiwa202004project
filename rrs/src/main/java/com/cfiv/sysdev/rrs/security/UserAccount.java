package com.cfiv.sysdev.rrs.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.cfiv.sysdev.rrs.Consts;
import com.cfiv.sysdev.rrs.entity.Account;

public class UserAccount implements UserDetails {
    private Account account;

    /**
     * コンストラクタ
     * (空ユーザーは作成不可)
     */
    protected UserAccount(){}

    public UserAccount(Account a){
        account = a;
    }

    /**
     * ユーザー権限リスト
     * @return ユーザー権限リスト
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (account.getUserRole() == 0) {
            return AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_CLIENTADMIN", "ROLE_USER");
        }
        else if (account.getUserRole() == 1) {
            return AuthorityUtils.createAuthorityList("ROLE_CLIENTADMIN", "ROLE_USER");
        }
        else {
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.getEnabled() == Consts.ENABLED ? true : false;
    }
}
