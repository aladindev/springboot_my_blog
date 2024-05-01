package com.aladin.springbootstudy.service.encrypt;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
public class EncryptService {
    /** 대칭키 */
    @Value("#{encrypt.kakao_encrpy_key}")
    String kakao_encrpy_key;

    public String aesCBCEncode(String plainText) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(kakao_encrpy_key.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(kakao_encrpy_key.substring(0,16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);

        byte[] encrpytionByte = c.doFinal(plainText.getBytes("UTF-8"));

        return Hex.encodeHexString(encrpytionByte);
    }


    public String aesCBCDecode(String encodeText) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(kakao_encrpy_key.getBytes("UTF-8"), "AES");
        IvParameterSpec IV = new IvParameterSpec(kakao_encrpy_key.substring(0,16).getBytes());

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.DECRYPT_MODE, secretKey, IV);

        byte[] decodeByte = Hex.decodeHex(encodeText.toCharArray());

        return new String(c.doFinal(decodeByte), "UTF-8");
    }
}
