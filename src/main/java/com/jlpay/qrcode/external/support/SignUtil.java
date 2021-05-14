package com.jlpay.qrcode.external.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Slf4j
public class SignUtil {
    private static final String SIGN_KEY = "sign";
    private static final String SIGN_FILTER_KEYS = "|sign|";
    private static final String UTF8_ENCODING = "UTF-8";
    private static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    /**
     * 验签
     * @param content
     * @param sign
     * @param publicKey
     * @return
     */
    private static boolean doCheck(String content, String sign, String publicKey){
        return doCheck(content, sign, publicKey, "utf-8");
    }
    /**
     * 验签
     * @param content 原始数据
     * @param sign 签名
     * @param publicKey 公钥
     * @param charset
     * @return
     */
    private static boolean doCheck(String content, String sign, String publicKey,String charset){
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(publicKey.getBytes());
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            Signature signature = Signature
                    .getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update( content.getBytes(charset) );

            boolean bverify = signature.verify( Base64.decodeBase64(sign.getBytes()) );
            return bverify;
        } catch (Exception e){
            log.error("验证RSA签名异常", e);
        }
        return false;
    }
    /**
     * keyvalue按key的ascii码升序拼接
     *
     * @param params
     * @return
     */
    public static String getKeyValueStr(Map<String, Object> params) {

        StringBuilder sb = new StringBuilder();
        Set<String> keySets = params.keySet();
        List<String> keys = new ArrayList<String>(keySets);
        Collections.sort(keys);

        int n = 0;
        for (String key : keys) {
            if (key.equals("sign")) {
                continue;
            }
            if (null == params.get(key)) {
                continue;
            }
            String val = params.get(key).toString();
            if (StringUtils.isEmpty(val)) {
                continue;
            }
            if (SIGN_FILTER_KEYS.indexOf("|" + key + "|") >= 0) {
                continue;
            }
            if (n > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(val);
            n++;
        }
        return sb.toString();
    }

    /**
     * 256签名
     * @param data
     * @param privateKey
     * @return
     */
    public static String sign256(String data,String privateKey){
        try{
            PrivateKey pkey = restorePrivateKey(privateKey);
            byte[] signDatas = sign256(data, pkey);
            return new String(Base64.encodeBase64(signDatas));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原私钥
     *
     * @param
     * @return
     */
    public static PrivateKey restorePrivateKey(String privateKey) {
        byte[] encodedKey = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * SHA256WithRSA签名
     * @param data
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] sign256(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
            SignatureException, UnsupportedEncodingException {

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initSign(privateKey);

        signature.update(data.getBytes(UTF8_ENCODING));

        return signature.sign();
    }

    /**
     * 获取签名验签数据,根据ASCII码表对key进行排序，生成的数据为key=value&key=value
     * @param  sortedParams 数据对象
     * @param ignoresKeys 忽略字段
     * @return
     */
    public static String getSignContent(Map<String, ?> sortedParams,String...ignoresKeys ){
        List<String> ignoresKeyList = null;
        if(ignoresKeys!=null){
            ignoresKeyList = Arrays.asList(ignoresKeys);
        }else{
            ignoresKeyList = new ArrayList<>();
        }
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if(ignoresKeyList.contains(key)){
                continue;
            }
            String value=sortedParams.get(key).toString();
            if (areNotEmpty(key, value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    private static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    private static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * RSA私钥签名
     *
     * @param content
     * @param privateKey
     * @param privateKey
     */
    public static String rsaSign(String content, String privateKey){
        try {
            log.debug("signContext-->" + content);
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            if (StringUtils.isEmpty(UTF8_ENCODING)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(UTF8_ENCODING));
            }

            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception var6) {
            throw new RuntimeException("RSAcontent = " + content + "; charset = " + UTF8_ENCODING, var6);
        }
    }

    /**
     * 还原私钥
     *
     * @param algorithm
     * @param ins
     * @return PrivateKey
     */
    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins != null && !StringUtils.isEmpty(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            byte[] encodedKey = readText(ins).getBytes();
            encodedKey = Base64.decodeBase64(encodedKey);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static String readText(InputStream in) throws Exception{
        Reader reader = null;
        StringWriter stringWriter = new StringWriter();
        try {
            reader = new InputStreamReader(in);
            char[] buffer = new char[4096];
            int amount;
            while((amount = reader.read(buffer)) >= 0) {
                stringWriter.write(buffer, 0, amount);
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return stringWriter.toString();
    }
}
