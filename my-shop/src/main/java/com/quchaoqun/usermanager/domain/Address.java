package com.quchaoqun.usermanager.domain;

import lombok.Data;

@Data
public class Address {
    private int aid;
    private int uid;
    private String name;
    private String phone;
    private String detail;
    private int state;

}
