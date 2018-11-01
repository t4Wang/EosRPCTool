package com.runze.eos.rpctool.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.runze.eos.rpctool.domain.*;
import com.runze.eos.rpctool.util.GsonEosTypeAdapterFactory;
import com.runze.eos.rpctool.util.HttpUtils;
import com.runze.eos.rpctool.util.Property;
import com.runze.eos.rpctool.util.cypto.ec.EosPrivateKey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EosService {
    public static final String EOS_URL = Property.getProperty("EOS_URL");

    public static ChainInfo getChainInfo() throws IOException {
        String url = EOS_URL + "/v1/chain/get_info";
        ChainInfo chainInfo = JSON.parseObject(HttpUtils.post(url), new TypeReference<ChainInfo>() {});
        return chainInfo;
    }

    public static Block getBlock(String blockNumOrId) throws IOException {
        String url = EOS_URL + "/v1/chain/get_block";
        JSONObject jo = new JSONObject();
        jo.put("block_num_or_id", blockNumOrId);
        Block block = JSON.parseObject(HttpUtils.post(url, jo.toString()), new TypeReference<Block>() {});
        return block;
    }

    public static AbiJsonToBin abiJsonToBin(String accountName, String actionName, Map<String, Object> args) throws IOException {
        String url = EOS_URL + "/v1/chain/abi_json_to_bin";
        JSONObject jo = new JSONObject();
        jo.put("code", accountName);
        jo.put("action", actionName);
        jo.fluentPut("args", args);
        AbiJsonToBin abiJsonToBin = JSON.parseObject(HttpUtils.post(url, jo.toString()), new TypeReference<AbiJsonToBin>() {});
        return abiJsonToBin;
    }

    public static String pushTransaction(String actor, String accountName, String actionName, String privateKey, Map<String, Object> args) throws IOException {
        String url = EOS_URL + "/v1/chain/push_transaction";

        /* Create the json array of arguments */
        AbiJsonToBin data = EosService.abiJsonToBin(accountName, actionName, args);

        /* Get the head block */
        ChainInfo chainInfo = EosService.getChainInfo();

        /* Create Transaction Action Authorization */
        PermissionLevel authorization = new PermissionLevel(actor, "active");
        List<PermissionLevel> authorizations = new ArrayList<>();
        authorizations.add(authorization);

        /* Create Transaction Action */
        Action action = new Action(accountName, actionName);
        action.setData(data.getBinargs());
        action.setAuthorization(authorizations);

        /* Sign the Transaction */
        SignedTransaction signatures = new SignedTransaction();
        // chainInfo.getTimeAfterHeadBlockTime(30000)
        // "2023-05-10T18:38:19"
        signatures.setExpiration(chainInfo.getTimeAfterHeadBlockTime(600000));
        signatures.setReferenceBlock(chainInfo.getHeadBlockId());
        signatures.addAction(action);
        signatures.sign(new EosPrivateKey(privateKey), chainInfo.getChainId());

        PackedTransaction transaction = new PackedTransaction(signatures);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
                .excludeFieldsWithoutExposeAnnotation().create();
        String s = gson.toJson(transaction);
        System.out.println(s);

        JSONObject post = JSON.parseObject(HttpUtils.post(url, s));
        String transaction_id = (String)post.get("transaction_id");
        return transaction_id;
    }

    public static String getTransaction(String transaction_id) throws IOException {
        String url = EOS_URL + "/v1/history/get_transaction";
        JSONObject jo = new JSONObject();
        jo.put("id", transaction_id);
        JSONObject post = JSON.parseObject(HttpUtils.post(url, jo.toString()));
        System.out.println(post);
        return post.toString();
    }


}
