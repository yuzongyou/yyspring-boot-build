package com.duowan.common.codec.xrsa;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/30 15:58
 */
public class XRsaKeyPair {

    private final XRsaPublicKey publicKey;

    private final XRsaPrivateKey privateKey;

    public XRsaKeyPair(XRsaPublicKey publicKey, XRsaPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public XRsaPublicKey getPublicKey() {
        return publicKey;
    }

    public XRsaPrivateKey getPrivateKey() {
        return privateKey;
    }

}
