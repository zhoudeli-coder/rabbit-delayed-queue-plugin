package com.zdl.rabbit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhoudeli
 */
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 5386255055578359167L;
    private String name;
}
