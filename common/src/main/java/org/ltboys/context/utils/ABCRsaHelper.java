package org.ltboys.context.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class ABCRsaHelper {
	private String priKeyStr;
	private String pubKeyStr;

	public String getPriKeyStr() {
		return priKeyStr;
	}

	public void setPriKeyStr(String priKeyStr) {
		this.priKeyStr = priKeyStr;
	}

	public String getPubKeyStr() {
		return pubKeyStr;
	}

	public void setPubKeyStr(String pubKeyStr) {
		this.pubKeyStr = pubKeyStr;
	}

	public String createSignWithPriKeyBySHA256(String param) throws Exception {
		PKCS8EncodedKeySpec priKeyPCKS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(this.getPriKeyStr()));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey prikey = keyf.generatePrivate(priKeyPCKS8);
		Signature signet = Signature.getInstance("SHA256withRSA");
		signet.initSign(prikey);
		signet.update(param.getBytes("UTF-8"));
		return Base64.encodeBase64String(signet.sign());
	}
}
