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
    @Value("#{encrypt['kakao.encrypt_key']}")
    String kakao_encrpy_key;

    public String aesCBCEncode(String plainText) throws Exception {
        // AES 암호화를 위한 32바이트 키 사용
        SecretKeySpec secretKey = new SecretKeySpec(kakao_encrpy_key.getBytes("UTF-8"), "AES");
        // AES 블록 크기(16바이트)에 맞는 IV 생성
        IvParameterSpec IV = new IvParameterSpec(kakao_encrpy_key.substring(0,16).getBytes("UTF-8"));

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.ENCRYPT_MODE, secretKey, IV);

        byte[] encrpytionByte = c.doFinal(plainText.getBytes("UTF-8"));

        // Hex 문자열로 변환 (org.apache.commons.codec.binary.Hex 사용)
        return Hex.encodeHexString(encrpytionByte);
    }

    public String aesCBCDecode(String encodeText) throws Exception {
        // AES 암호화를 위한 32바이트 키 사용
        SecretKeySpec secretKey = new SecretKeySpec(kakao_encrpy_key.getBytes("UTF-8"), "AES");
        // AES 블록 크기(16바이트)에 맞는 IV 생성
        IvParameterSpec IV = new IvParameterSpec(kakao_encrpy_key.substring(0,16).getBytes("UTF-8"));

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");

        c.init(Cipher.DECRYPT_MODE, secretKey, IV);

        byte[] decodeByte = Hex.decodeHex(encodeText.toCharArray());

        return new String(c.doFinal(decodeByte), "UTF-8");
    }
}
