import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.runze.eos.rpctool.domain.*;
import com.runze.eos.rpctool.service.EosService;
import com.runze.eos.rpctool.util.GsonEosTypeAdapterFactory;
import com.runze.eos.rpctool.util.HttpUtils;
import com.runze.eos.rpctool.util.cypto.ec.EosPrivateKey;
import com.runze.eos.rpctool.util.cypto.ec.EosPublicKey;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EosrpcdemoTests {

    /* 生成创建账户的bin字符串
    api
    http://jungle.cryptolions.io:18888/v1/chain/abi_json_to_bin

    params
    {
      "code": "eosio",
      "action": "newaccount",
      "args": {
        "creator": "eosio",
        "name": "alice",
        "owner": {
          "threshold": 1,
          "keys": [
            {
              "key": "EOS7kHuRuK6FcXbrV7Li1F7epQJuVgpwoYVJahMCdJxGtk7jgWi3V",
              "weight": 1
            }
          ],
          "accounts": [],
          "waits": []
        },
        "active": {
          "threshold": 1,
          "keys": [
            {
              "key": "EOS7kHuRuK6FcXbrV7Li1F7epQJuVgpwoYVJahMCdJxGtk7jgWi3V",
              "weight": 1
            }
          ],
          "accounts": [],
          "waits": []
        }
      }
    }

    return
    {
        "binargs": "1042f03eab99b1ca2084f03eab99b1ca0100000001000341a9e89b10df5fb0b425e294e32c6208fc3c97becd61f111b351274cd30cd92801000000010000000100036b7a2e4feec43f2160927f34a046914d9aba7625a492597a0f888d2aaec225ba01000000"
    }
     */
    /* 生成购买内存的bin字符串
    api
    http://jungle.cryptolions.io:18888/v1/chain/abi_json_to_bin

    params
    {
       "code": "eosio",
       "action": "buyram",
       "args": {
            "payer": "eosio",
            "receiver": "alice",
            "quant": "50.0000 EOS",
        }
     }

    return
    {
        "binargs": "1042f03eab99b1ca2084f03eab99b1ca20a107000000000004454f5300000000"
    }

     */
    /* 生成购买抵押资源的bin字符串
    api
    http://jungle.cryptolions.io:18888/v1/chain/abi_json_to_bin

    params
    {
        "code": "eosio",
        "action": "delegatebw",
        "args": {
            "from": "eosio",
            "receiver": "alice",
            "stake_net_quantity": "20.0000 EOS",
            "stake_cpu_quantity": "20.0000 EOS",
            "transfer": 0,
        }
    }

    return
    {
        "binargs": "1042f03eab99b1ca2084f03eab99b1ca400d03000000000004454f5300000000400d03000000000004454f530000000000"
    }
     */
    @Test
    public void testNewAccount() throws IOException {
//        EosPrivateKey ownerKey = new EosPrivateKey("5JE3kB95aPcM1CthYythmXwg1H8RZjXbRdnuqCWujqqm56xWZPz");
//        EosPrivateKey activeKey = new EosPrivateKey("5JX4Q2N3heccMAhou3vT5g6tK8ZBxyQft6xGexeQ9oQYV9Jhtna");
        Key ownerKey = new Key();
        ownerKey.setKey("EOS7kHuRuK6FcXbrV7Li1F7epQJuVgpwoYVJahMCdJxGtk7jgWi3V");
        ownerKey.setWeight(1L);
        ArrayList<Key> ownerKeys = new ArrayList<>();
        ownerKeys.add(ownerKey);

        RequiredAuth owner = new RequiredAuth();
        owner.setThreshold("1");
        owner.setKeys(ownerKeys);

        Key activeKey = new Key();
        activeKey.setKey("EOS7TAfcm1gbEndtLxjHxgP4gZDUtKeDQyNHqTm1cKcVSg241MS6F");
        activeKey.setWeight(1L);
        ArrayList<Key> activeKeys = new ArrayList<>();
        activeKeys.add(activeKey);

        RequiredAuth active = new RequiredAuth();
        active.setThreshold("1");
        active.setKeys(activeKeys);

        Map<String, Object> argsNewaccount = new HashMap<>(5);
        argsNewaccount.put("creator", "eosio");
        argsNewaccount.put("name", "alice");
        argsNewaccount.put("owner", owner);
        argsNewaccount.put("active", active);
        argsNewaccount.put("recovery", "eosio");
        AbiJsonToBin newaccountData = EosService.abiJsonToBin("eosio", "newaccount", argsNewaccount);

        Map<String, Object> argsBuyram = new HashMap<>(3);
        argsBuyram.put("payer", "eosio");
        argsBuyram.put("receiver", "alice");
        argsBuyram.put("quant", "50.0000 EOS");
        AbiJsonToBin buyramData = EosService.abiJsonToBin("eosio", "buyram", argsBuyram);

        Map<String, Object> argsDelegatebw = new HashMap<>(5);
        argsDelegatebw.put("from", "eosio");
        argsDelegatebw.put("receiver", "alice");
        argsDelegatebw.put("stake_net_quantity", "20.0000 EOS");
        argsDelegatebw.put("stake_cpu_quantity", "20.0000 EOS");
        argsDelegatebw.put("transfer", 0);
        AbiJsonToBin delegatebwData = EosService.abiJsonToBin("eosio", "delegatebw", argsDelegatebw);

        ChainInfo chainInfo = EosService.getChainInfo();

        PermissionLevel authorization = new PermissionLevel("eosio", "active");
        List<PermissionLevel> authorizations = new ArrayList<>();
        authorizations.add(authorization);

        Action actionNewaccount = new Action("eosio", "newaccount");
        actionNewaccount.setData(newaccountData.getBinargs());
        actionNewaccount.setAuthorization(authorizations);

        Action actionBuyram = new Action("eosio", "buyram");
        actionBuyram.setData(buyramData.getBinargs());
        actionBuyram.setAuthorization(authorizations);

        Action actionDelegatebw = new Action("eosio", "delegatebw");
        actionDelegatebw.setData(delegatebwData.getBinargs());
        actionDelegatebw.setAuthorization(authorizations);

        SignedTransaction signatures = new SignedTransaction();
        signatures.setExpiration(chainInfo.getTimeAfterHeadBlockTime(600000));
        signatures.setReferenceBlock(chainInfo.getHeadBlockId());
        signatures.addAction(actionNewaccount);
        signatures.addAction(actionBuyram);
        signatures.addAction(actionDelegatebw);
        signatures.sign(new EosPrivateKey("5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3"), chainInfo.getChainId());

        PackedTransaction transaction = new PackedTransaction(signatures);
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
                .excludeFieldsWithoutExposeAnnotation().create();
        String s = gson.toJson(transaction);
        System.out.println(s);

        String url = EosService.EOS_URL + "/v1/chain/push_transaction";
        JSONObject post = JSON.parseObject(HttpUtils.post(url, s));
        String transaction_id = (String)post.get("transaction_id");
    }

    @Test
    public void testGetTransaction() throws IOException {
        String transaction = EosService.getTransaction("2f15ec4ebe635a2134852e1f2c7d796ed1d1f2a998b17cb4902cf90e341f0437");
    }

    /**
     * 转账
     * @throws IOException
     */
    @Test
    public void testPushTransaction() throws IOException {
        Map<String, Object> args = new HashMap<>(4);
        args.put("from", "eosio");
        args.put("to", "user1");
        args.put("quantity", "1.0000 SYS");
        args.put("memo", "My First Transaction");

        String s = EosService.pushTransaction("eosio", "eosio.token", "transfer", "5KQwrPbwdL6PhXujxW37FSSQZ1JiwsST4cqQzDeyXtP79zkvFD3", args);

    }

    @Test
    public void testAbiJsonToBin() throws IOException {
        Map<String, Object> args = new HashMap<>(4);
        args.put("from", "alice");
        args.put("to", "bob");
        args.put("quantity", "1.0000 EOS");
        args.put("memo", "My First Transaction");
        AbiJsonToBin abiJsonToBin = EosService.abiJsonToBin("eosio.token", "transfer", args);
        System.out.println(abiJsonToBin);
    }

    @Test
    public void testGetBlock() throws IOException {
        ChainInfo chainInfo = EosService.getChainInfo();
        Block block = EosService.getBlock(chainInfo.getHeadBlockId());
        System.out.println(block);
    }

    @Test
    public void testEosPrivateKey() {
        EosPrivateKey eosPrivateKey = new EosPrivateKey();
        System.out.println(eosPrivateKey);
        EosPublicKey publicKey = eosPrivateKey.getPublicKey();
        System.out.println(publicKey);
    }

}
