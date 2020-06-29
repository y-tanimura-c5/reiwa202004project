package com.cfiv.sysdev.rrs.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfiv.sysdev.rrs.dto.UserRequest;
import com.cfiv.sysdev.rrs.entity.User;
import com.cfiv.sysdev.rrs.repository.UserRepository;

/**
 * ユーザー情報 Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {

    /**
     * ユーザー情報 Repository
     */
    @Autowired
    UserRepository userRepository;

    /**
     * ユーザー情報 全検索
     * @return 検索結果
     */
    public List<User> searchAll() {
        return userRepository.findAll();
    }

    /**
     * ユーザー情報新規登録
     * @param user ユーザー情報
     */
    public void create(UserRequest req) {
        userRepository.save(CreateUser(req));
    }

    /**
     * ユーザーTBLエンティティの生成
     * @param userRequest ユーザー情報リクエストデータ
     * @return ユーザーTBLエンティティ
     */
    private User CreateUser(UserRequest userRequest) {
        Date now = new Date();

        User user = new User();
//        user.setName(userRequest.getName());
//        user.setAddress(userRequest.getAddress());
//        user.setPhone(userRequest.getPhone());
        user.setCreateDate(now);
        user.setUpdateDate(now);

        return user;
    }
}