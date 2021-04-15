package com.quchaoqun.usermanager.domain;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String password;
    private String email;
    private String sex;
    private int status;
    private String code;
    private int role;

}
