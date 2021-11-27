package com.gsd.global.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * AES256 방식 암호화 유틸 클래스
 * Created by Yohan lee
 * Created on 2021-02-16.
 **/
public class AES256Util {

    private static  String iv;
    private static Key keySpec;
    private static final String KEY = "anrndghkRhcdlvldjTtmqslek";
    private static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";

    /**
     * 비밀키 Spec 구축
     */
    private static void init() {
        //16자리의 키값으로 만들기위해 자름.
        iv = KEY.substring(0, 16);
        //바이트코드를 담기위한 객체
        byte[] keyBytes = new byte[16];
        //키값을 UTF-8 캐릭터셋으로 인코딩
        byte[] encodingByte = KEY.getBytes(StandardCharsets.UTF_8);
        int length = encodingByte.length;
        //16자리로 길이 고정.
        if(length > keyBytes.length) {
            length = keyBytes.length;
        }
        //encodingByte배열의 0번째 값을 keyByte배열의 0번째부터 16개 길이만큼 복사.
        System.arraycopy(encodingByte, 0, keyBytes, 0, length);
        //복사한 바이트배열로부터 비밀키 구축.
        keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 문자열을 암호화 한다.
     * @param str 암호화할 문자열
     * @return 암호화된 문자열
     */
    public static String encode(String str) throws GeneralSecurityException {
        init();

        //Ciper의 AES/CBC/PKCS5Padding형식 인스턴스 가져오기.
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        //인코딩을 위한 인스턴스 초기화(Ciper 모드설정, 구축한 비밀키, 키값파라미터
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        //암호화를 하여 바이트객체에 할당.
        byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
        //base64 방식으로 인코딩하여 리턴.
        return new String(Base64.encodeBase64URLSafe(encrypted));
    }

    /**
     * 암호화 된 문자열을 복호화 한다.
     * @param str 암호화된 문자열
     * @return 복호화된 문자열
     */
    public static String decode(String str) throws GeneralSecurityException {
        init();
        //Ciper의 AES/CBC/PKCS5Padding형식 인스턴스 가져오기. **ECB방식은 Exception 발생.
        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
        //디코딩을 위한 인스턴스 초기화(Ciper 모드 설정, 구축한 비밀키, 키값파라미터)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        //암호화 할때, Base64로 인코딩된 바이트 다시 디코딩.
        byte[] byteStr = Base64.decodeBase64URLSafe(str);
        //복호화 하여, UTF-8셋으로 변환하여 리턴.
        return new String(cipher.doFinal(byteStr), StandardCharsets.UTF_8);
    }
}
