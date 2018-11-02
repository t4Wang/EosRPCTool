package com.runze.eos.rpctool.domain;

import com.google.gson.annotations.Expose;


/**
 * Created by swapnibble on 2017-09-12.
 */


public class PermissionLevel implements EosType.Packer {

    @Expose
    private AccountName actor;

    @Expose
    private PermissionName permission;

    public PermissionLevel(String accountName, String permissionName) {
        actor = new AccountName(accountName);
        permission = new PermissionName(permissionName);
    }

    public String getAccount(){
        return actor.toString();
    }

    public void setAccount(String accountName ){
        actor = new AccountName(accountName);
    }

    public String getPermission(){
        return permission.toString();
    }

    public void setPermission(String permissionName ){
        permission = new PermissionName(permissionName);
    }

    @Override
    public void pack(EosType.Writer writer) {

        actor.pack(writer);
        permission.pack(writer);
    }
}
