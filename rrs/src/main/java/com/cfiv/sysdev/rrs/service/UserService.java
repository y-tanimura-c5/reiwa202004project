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
 * ���[�U�[��� Service
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserService {

    /**
     * ���[�U�[��� Repository
     */
    @Autowired
    UserRepository userRepository;

    /**
     * ���[�U�[��� �S����
     * @return ��������
     */
    public List<User> searchAll() {
        return userRepository.findAll();
    }

    /**
     * ���[�U�[���V�K�o�^
     * @param user ���[�U�[���
     */
    public void create(UserRequest req) {
        userRepository.save(CreateUser(req));
    }

    /**
     * ���[�U�[TBL�G���e�B�e�B�̐���
     * @param userRequest ���[�U�[��񃊃N�G�X�g�f�[�^
     * @return ���[�U�[TBL�G���e�B�e�B
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