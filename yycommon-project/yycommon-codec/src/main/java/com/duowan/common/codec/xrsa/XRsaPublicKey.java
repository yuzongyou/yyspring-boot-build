package com.duowan.common.codec.xrsa;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/30 15:39
 */
public class XRsaPublicKey {

    public static final String RSA_ALGORITHM = "RSA";

    private RSAPublicKey publicKey;
    private String publicKeyText;

    public XRsaPublicKey(String publicKey) {
        try {
            this.publicKeyText = publicKey;
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);

            //通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        } catch (Exception e) {
            throw new RuntimeException("不支持的密钥", e);
        }
    }

    public RSAPublicKey getKey() {
        return publicKey;
    }

    public String getKeyText() {
        return publicKeyText;
    }
}
