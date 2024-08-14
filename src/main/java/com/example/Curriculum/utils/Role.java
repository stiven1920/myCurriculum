package com.example.Curriculum.utils;

import java.util.Arrays;
import java.util.List;

public enum Role {

    CUSTOMER(Arrays.asList(
            Permission.SAVE_CUSTOMER,
            Permission.UPDATE_CUSTOMER,
            Permission.READ_CUSTOMER
    )),
    ADMINISTRATOR(Arrays.asList(
            Permission.SAVE_ADMINISTRATOR, 
            Permission.READ_ADMINISTRATOR,
            Permission.UPDATE_ADMINISTRATOR,
            Permission.DELETE_ADMINISTRATOR
    ));

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
