package com.cfiv.sysdev.rrs.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * ���[�U�[��� ���N�G�X�g�f�[�^
 */
@Data
public class UserRequest implements Serializable {

    /**
     * ���O
     */
    private String name;

    /**
     * �Z��
     */
    private String address;

    /**
     * �d�b�ԍ�
     */
    private String phone;
}