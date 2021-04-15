package com.quchaoqun.order.domain;

import lombok.Data;

@Data
public class Orders {
    private String oid;
    private int uid;
    private int aid;
    private String adetail;
    private String aphone;
    private String aname;
    private int ocount;
    private String time;
    private int state;

}
