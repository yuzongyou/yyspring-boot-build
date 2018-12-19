package com.duowan.common.codec.xrsa;

import com.duowan.common.utils.JsonUtil;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/31 9:54
 */
public class XRsaUtilTest {

    /*
     * 非对称加密算法   RSA过程 ： 以甲乙双方为例
     *      1、初始化密钥 构建密钥对,生成公钥、私钥保存到keymap中
     *              KeyPairGenerator --->    KeyPair     -->      RSAPublicKey、RSAPrivateKey
     *      2、甲方使用私钥加密, 加密后在用私钥对加密数据进行数据签名，然后发送给乙方
     *              RSACoder.encryptByPrivateKey(data, privateKey);
     *              RSACoder.sign(encodedData, privateKey);
     *      3、乙方则通过公钥验证签名的加密数据，如果验证正确则在通过公钥对加密数据进行解密
     *              RSACoder.verify(encodedData, publicKey, sign);
     *              RSACoder.decryptByPublicKey(encodedData, publicKey);
     *
     *      4、乙方在通过公钥加密发送给甲方
     *              RSACoder.encryptByPublicKey(decodedData, publicKey);
     *      5、甲方通过私钥解密该数据
     *              RSACoder.decryptPrivateKey(encodedData, privateKey);
     */
    @Test
    public void test() throws Exception {

//        String publicKeyText = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzgZxZtpcODmLINwSGOF8F9voDW5yWZB2JxObfTcyKFyUus4Efn3A441xjGdd4JFpugFpLkTViHXmKxWnfdSQD0MuPBBtR9VNhJxRf2f9CJ7V/hSbcMSYyOdsq5yFseSibBEBDoYQYUYavfSTrsUyqMJDz6ro/P0e+8iW4XchTVQIDAQAB";
//        String privateKeyText = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALOBnFm2lw4OYsg3BIY4XwX2+gNbnJZkHYnE5t9NzIoXJS6zgR+fcDjjXGMZ13gkWm6AWkuRNWIdeYrFad91JAPQy48EG1H1U2EnFF/Z/0IntX+FJtwxJjI52yrnIWx5KJsEQEOhhBhRhq99JOuxTKowkPPquj8/R77yJbhdyFNVAgMBAAECgYBv85C4Xuj8ijpPVgjLUNLqnGq2t2cEYHIPny7B6/p50Q+OfcM1HrHVuO7Am7hHJJ0Ft6UY5foeM/W+d3qdM6h+b7K6dBaMAtAMf6m4Y1Vr41NXAobIqkBJykFxMbn1DRav271jzEHEfX/JMneF9UGjfGpEGyR2T0J6jDOMRPsUQQJBAO9HX1/8a8VskMKiiNNnppCE4ZF/7I/znzXWKUDUjigB5UTcjoyR7vza/IkmgmNTbLZOTUk4KaxDJ2e8hrESVIUCQQDADOzzWfr0N92Q3nH/kn7bK53lb4kzC427ef1W4wuIEC/8X6ugXqsi01qZ8rJBMVX9523LjNXlo3Shimr8oOSRAkEAxWAugFML4zKGxsaj7x7U+ulh2E+Zp7TiY9pg9SVgjgfiQ0dcHwBSDynkno2xiJVMW6WDgx5c4cgTPTL6OB5SsQJBAJ8c1l/T58fYVRhGQ9qDa9h3rYy+OMRyyQ1PR2ZqG06yYp9MQCxQ4qaqxwCPA8JhdqYyzFN9LmXgY6vGu1bskhECQQDtmwNq2oQ46dIEaO/ydDViWxBDZ7IXVz2MFtQq0+1igst9/bH4or5Ug1E1btKeD14ixTspiDIWs2TYo6oW/oCH";

        String publicKeyText = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzgZxZtpcODmLINwSGOF8F9voDW5yWZB2JxObf" +
                "TcyKFyUus4Efn3A441xjGdd4JFpugFpLkTViHXmKxWnfdSQD0MuPBBtR9VNhJxRf2f9CJ7V/hSbc" +
                "MSYyOdsq5yFseSibBEBDoYQYUYavfSTrsUyqMJDz6ro/P0e+8iW4XchTVQIDAQAB";

        String privateKeyText = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALOBnFm2lw4OYsg3BIY4XwX2+gNb" +
                "nJZkHYnE5t9NzIoXJS6zgR+fcDjjXGMZ13gkWm6AWkuRNWIdeYrFad91JAPQy48EG1H1U2EnFF/Z" +
                "/0IntX+FJtwxJjI52yrnIWx5KJsEQEOhhBhRhq99JOuxTKowkPPquj8/R77yJbhdyFNVAgMBAAEC" +
                "gYBv85C4Xuj8ijpPVgjLUNLqnGq2t2cEYHIPny7B6/p50Q+OfcM1HrHVuO7Am7hHJJ0Ft6UY5foe" +
                "M/W+d3qdM6h+b7K6dBaMAtAMf6m4Y1Vr41NXAobIqkBJykFxMbn1DRav271jzEHEfX/JMneF9UGj" +
                "fGpEGyR2T0J6jDOMRPsUQQJBAO9HX1/8a8VskMKiiNNnppCE4ZF/7I/znzXWKUDUjigB5UTcjoyR" +
                "7vza/IkmgmNTbLZOTUk4KaxDJ2e8hrESVIUCQQDADOzzWfr0N92Q3nH/kn7bK53lb4kzC427ef1W" +
                "4wuIEC/8X6ugXqsi01qZ8rJBMVX9523LjNXlo3Shimr8oOSRAkEAxWAugFML4zKGxsaj7x7U+ulh" +
                "2E+Zp7TiY9pg9SVgjgfiQ0dcHwBSDynkno2xiJVMW6WDgx5c4cgTPTL6OB5SsQJBAJ8c1l/T58fY" +
                "VRhGQ9qDa9h3rYy+OMRyyQ1PR2ZqG06yYp9MQCxQ4qaqxwCPA8JhdqYyzFN9LmXgY6vGu1bskhEC" +
                "QQDtmwNq2oQ46dIEaO/ydDViWxBDZ7IXVz2MFtQq0+1igst9/bH4or5Ug1E1btKeD14ixTspiDIW" +
                "s2TYo6oW/oCH";


        XRsaKeyPair pair = XRsaUtil.generateKey();

        XRsaPublicKey publicKey = pair.getPublicKey();
        XRsaPrivateKey privateKey = pair.getPrivateKey();
//
//        publicKey = new XRsaPublicKey(publicKey.getKeyText());
//        privateKey = new XRsaPrivateKey(privateKey.getKeyText());

//        publicKey = new XRsaPublicKey(readString("publickey"));
//        privateKey = new XRsaPrivateKey(readString("privatekey"));

//        publicKey = new XRsaPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzgZxZtpcODmLINwSGOF8F9voDW5yWZB2JxObfTcyKFyUus4Efn3A441xjGdd4JFpugFpLkTViHXmKxWnfdSQD0MuPBBtR9VNhJxRf2f9CJ7V/hSbcMSYyOdsq5yFseSibBEBDoYQYUYavfSTrsUyqMJDz6ro/P0e+8iW4XchTVQIDAQAB");
//        privateKey = new XRsaPrivateKey("MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALOBnFm2lw4OYsg3BIY4XwX2+gNbnJZkHYnE5t9NzIoXJS6zgR+fcDjjXGMZ13gkWm6AWkuRNWIdeYrFad91JAPQy48EG1H1U2EnFF/Z/0IntX+FJtwxJjI52yrnIWx5KJsEQEOhhBhRhq99JOuxTKowkPPquj8/R77yJbhdyFNVAgMBAAECgYBv85C4Xuj8ijpPVgjLUNLqnGq2t2cEYHIPny7B6/p50Q+OfcM1HrHVuO7Am7hHJJ0Ft6UY5foeM/W+d3qdM6h+b7K6dBaMAtAMf6m4Y1Vr41NXAobIqkBJykFxMbn1DRav271jzEHEfX/JMneF9UGjfGpEGyR2T0J6jDOMRPsUQQJBAO9HX1/8a8VskMKiiNNnppCE4ZF/7I/znzXWKUDUjigB5UTcjoyR7vza/IkmgmNTbLZOTUk4KaxDJ2e8hrESVIUCQQDADOzzWfr0N92Q3nH/kn7bK53lb4kzC427ef1W4wuIEC/8X6ugXqsi01qZ8rJBMVX9523LjNXlo3Shimr8oOSRAkEAxWAugFML4zKGxsaj7x7U+ulh2E+Zp7TiY9pg9SVgjgfiQ0dcHwBSDynkno2xiJVMW6WDgx5c4cgTPTL6OB5SsQJBAJ8c1l/T58fYVRhGQ9qDa9h3rYy+OMRyyQ1PR2ZqG06yYp9MQCxQ4qaqxwCPA8JhdqYyzFN9LmXgY6vGu1bskhECQQDtmwNq2oQ46dIEaO/ydDViWxBDZ7IXVz2MFtQq0+1igst9/bH4or5Ug1E1btKeD14ixTspiDIWs2TYo6oW/oCH");

        publicKey = new XRsaPublicKey(publicKeyText);
        privateKey = new XRsaPrivateKey(privateKeyText);


//        write(publicKey.getKeyText(), "publickey");
//        write(privateKey.getKeyText(), "privatekey");

        System.out.println("公钥：");
        System.out.println(publicKey.getKeyText());
        System.out.println("xxxxxxxxxxxxxx");

        System.out.println("私钥：");
        System.out.println(privateKey.getKeyText());
        System.out.println("xxxxxxxxxx");

        Map<String, Object> map = new HashMap<>();
        map.put("name", "Arinv");
        map.put("account", "Arinv");
        map.put("http", "http://portal-apollo.yy.com/config.html?#/appid=videox");
        map.put("logo", "http://portal-apollo.yy.com/config.html?#/appid=videox");
        map.put("label", "hello world");
        map.put("sex", "MALE");
        map.put("age", "20");
        map.put("aopenId6", "122222222222222222222");

        String inputData = JsonUtil.toJson(map);

        long begTime = System.currentTimeMillis();
        String encryptData = XRsaUtil.encryptByPrivateKey(inputData, privateKey, publicKey);
        System.out.println("加密耗时1： " + (System.currentTimeMillis() - begTime) + " 毫秒");

        begTime = System.currentTimeMillis();
        encryptData = XRsaUtil.encryptByPrivateKey(inputData, privateKey, publicKey);
        System.out.println("加密耗时2： " + (System.currentTimeMillis() - begTime) + " 毫秒");

        begTime = System.currentTimeMillis();
        encryptData = XRsaUtil.encryptByPrivateKey(inputData, privateKey, publicKey);
        System.out.println("加密耗时3： " + (System.currentTimeMillis() - begTime) + " 毫秒");

        begTime = System.currentTimeMillis();
        encryptData = XRsaUtil.encryptByPrivateKey(inputData, privateKey, publicKey);
        System.out.println("加密耗时4： " + (System.currentTimeMillis() - begTime) + " 毫秒");

        System.out.println("EncryptData: " + encryptData);
        System.out.println("EncryptDataLen: " + encryptData.length());

        begTime = System.currentTimeMillis();
        String descryptData = XRsaUtil.decryptByPublicKey(encryptData, publicKey);
        System.out.println("解密耗时： " + (System.currentTimeMillis() - begTime) + " 毫秒");

        System.out.println("DescryptData: " + descryptData);

    }

    public static void write(String content, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)))) {

            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {

            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}