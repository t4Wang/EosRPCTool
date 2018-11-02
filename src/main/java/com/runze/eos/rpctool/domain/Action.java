package com.runze.eos.rpctool.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import com.runze.eos.rpctool.util.cypto.util.HexUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by swapnibble on 2017-09-12.
 */

public class Action implements EosType.Packer {
    @Expose
    private AccountName account;

    @Expose
    private ActionName name;

    @Expose
    private List<PermissionLevel> authorization = null;

    @Expose
    private JsonElement data;

    public Action(String account, String name, PermissionLevel authorization, String data){
        this.account = new AccountName(account);
        this.name = new ActionName(name);
        this.authorization = new ArrayList<>();
        if ( null != authorization ) {
            this.authorization.add(authorization);
        }

        if ( null != data ) {
            this.data = new JsonPrimitive(data);
        }
    }

    public Action(String account, String name) {
        this (account, name, null, null);
    }

    public Action(){
        this ( null, null, null, null);
    }

    public String getAccount() {
        return account.toString();
    }

    public void setAccount(String account) {
        this.account = new AccountName(account);
    }

    public String getName() {
        return name.toString();
    }

    public void setName(String name) {
        this.name = new ActionName(name);
    }

    public List<PermissionLevel> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<PermissionLevel> authorization) {
        this.authorization = authorization;
    }

    public void setAuthorization(PermissionLevel[] authorization) {
        this.authorization.addAll( Arrays.asList( authorization) );
    }

    public void setAuthorization(String[] accountWithPermLevel) {
        if ( null == accountWithPermLevel){
            return;
        }

        for ( String permissionStr : accountWithPermLevel ) {
            String[] split = permissionStr.split("@", 2);
            authorization.add( new PermissionLevel(split[0], split[1]) );
        }
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(String data) {
        this.data = new JsonPrimitive(data);
    }

    @Override
    public void pack(EosType.Writer writer) {
        account.pack(writer);
        name.pack(writer);

        writer.putCollection( authorization );

        if ( null != data ) {
            byte[] dataAsBytes = HexUtils.toBytes( data.getAsString());
            writer.putVariableUInt(dataAsBytes.length);
            writer.putBytes( dataAsBytes );
        }
        else {
            writer.putVariableUInt(0);
        }
    }
}