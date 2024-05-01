package com.aladin.springbootstudy.service.encrypt;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Service
public class EncryptService {
    /** 대칭키 */
    @Value("#{encrypt.kakao-kakao_encrpy_key-key}")
    String kakao_encrpy_key;

    public String aesBytesEncryptor(String value) {
        AesBytesEncryptor aesBytesEncryptor = new AesBytesEncryptor(kakao_encrpy_key, getSalt());
        byte[] bytes= aesBytesEncryptor.encrypt(value.getBytes(StandardCharsets.UTF_8));

        return byteArrayToString(bytes);
    }

    public String getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[20];

        sr.nextBytes(salt);

        //byte To String
        StringBuffer sb = new StringBuffer();
        for(byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // byte -> String
    public String byteArrayToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte abyte : bytes) {
            sb.append(abyte);
            sb.append(" ");
        }
        return sb.toString();
    }
}
