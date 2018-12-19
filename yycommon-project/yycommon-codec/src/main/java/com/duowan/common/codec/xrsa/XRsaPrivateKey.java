package com.duowan.common.codec.xrsa;

import com.duowan.common.codec.exception.CodecException;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/30 15:39
 */
public class XRsaPrivateKey {

    public static final String RSA_ALGORITHM = "RSA";

    private RSAPrivateKey privateKey;

    private String privateKeyText;

    public XRsaPrivateKey(String privateKey) {
        try {

            this.privateKeyText = privateKey;
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new CodecException("不支持的密钥", e);
        }
    }

    public RSAPrivateKey getKey() {
        return privateKey;
    }

    public String getKeyText() {
        return privateKeyText;
    }
}
