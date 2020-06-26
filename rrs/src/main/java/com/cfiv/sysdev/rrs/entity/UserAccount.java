package com.cfiv.sysdev.rrs.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAccount implements UserDetails {
    private Account account;

    /**
     * �R���X�g���N�^
     * (�󃆁[�U�[�͍쐬�s��)
     */
    protected UserAccount(){}

    public UserAccount(Account a){
        account = a;
    }

    /**
     * ���[�U�[�������X�g
     * @return ���[�U�[�������X�g
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (account.getUserrole() == 0) {
            return AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_CLIENTADMIN", "ROLE_USER");
        }
        else if (account.getUserrole() == 1) {
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
        return account.isEnabled();
    }
}
