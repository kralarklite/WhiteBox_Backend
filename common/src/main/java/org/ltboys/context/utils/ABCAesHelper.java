package org.ltboys.context.utils;

import java.nio.charset.StandardCharsets;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.AudioFormat.Encoding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ABCAesHelper {
	static{
		Security.addProvider(new BouncyCastleProvider());
	}
	
	private String aesKey;

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}
	
	public String encodeWithAES(String param) throws Exception {
		byte[] keyBin = aesKey.getBytes("UTF-8");
		SecretKeySpec skeySpec = new SecretKeySpec(keyBin, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding","BC");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] data = cipher.doFinal(param.getBytes("UTF-8"));
		return byteArrToString(data);
	}
	
	public String decodeWithAES(String param) throws Exception {
		byte[] keyBin = aesKey.getBytes("UTF-8");
		SecretKeySpec skeySpec = new SecretKeySpec(keyBin, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding","BC");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] data = cipher.doFinal(StringToByteArray(param));
		return  new String(data, StandardCharsets.UTF_8);
	}
	
	
	private String byteArrToString(byte[] bcd){
		StringBuffer s = new StringBuffer();
		for(int i=0;i<bcd.length;i++){
			int char1 = ((bcd[i]>>4)&0xF);
			char char1c = Character.forDigit(char1, 16);
			int char2 = ((bcd[i]) & 0xF);
			char char2c = Character.forDigit(char2, 16);
			s.append(char1c);
			s.append(char2c);
			
		}
		return s.toString();
	}
	
	private static byte[] StringToByteArray(String hex)
    {
		//return Hex.decodeHex(hex);
		byte[] bytes = new byte[hex.length()/2];
		
        for(int i=0;i<hex.length()/2;i++) {
        	char char1c = hex.charAt(i*2);
        	char char2c = hex.charAt(i*2+1);
        	
        	int b1 = Character.digit(char1c, 16);
        	int b2 = Character.digit(char2c, 16);
        	
        	bytes[i]= (byte)(b1*16+b2);
        }
        return bytes;
    }
}
