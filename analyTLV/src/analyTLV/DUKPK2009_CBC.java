package analyTLV;


import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DUKPK2009_CBC {
	
	
	public static void main(String[] args) {
		
		String ksn = "09117121100165E00005";
		String bdk = "0123456789ABCDEFFEDCBA9876543210";
		byte [] byte_ksn = parseHexStr2Byte(ksn);
		byte [] byte_bdk = parseHexStr2Byte(bdk);
		
		byte[] ipek = GenerateIPEK(byte_ksn,byte_bdk);
		String ipekStr = parseByte2HexStr(ipek);
		System.out.println("ipekStr=" + ipekStr);
		
		byte[] dataKey = GetDataKeyVariant(byte_ksn,ipek);
		String dataKeyStr = parseByte2HexStr(dataKey);
		System.out.println("dataKeyStr=" + dataKeyStr);
		
		byte[] dataKey__ = GetDataKey(byte_ksn,ipek);
		String dataKeyStr__ = parseByte2HexStr(dataKey__);
		System.out.println("dataKeyStr__=" + dataKeyStr__);
		
//		3352DE66 4E684C2C B45873C4 B82F1E35 
		String datastr = "8551394E4D6129EDFA6A440928F44D042D2B3F2746EE95256AFF51D654A37FDCEABE91157D16BC7E2A7C29DF1215207708ED769B0E64DCBDC0A3E3D05CBFCEDF714A5F6A3E031337943EB57E9464D7E3E60586A3962BCF6EE6BF9AB0757FB17123478C07FFB1EF14468EAAB3E872574A412FB5A36431A17852384D511A7BF80D6A1A7F8F41D4C1CABC7A36A7E7ABB536F2660A94703B85CED03304C20442034C4D65DD1BD1D47B8FC7B8E74264F4C44CBF10CC7DF6DBD6E6923C9D852C0809CEA8AC09DDB7F290F4C0552E21F4BFF993";
		String deResultStr = parseByte2HexStr(TriDesDecryptionCBC(parseHexStr2Byte(dataKeyStr__), parseHexStr2Byte(datastr)));
		System.out.println("data: "+deResultStr);
		
		ksn = "00000332100300E00034";
		bdk = "0123456789ABCDEFFEDCBA9876543210";
		byte_ksn = parseHexStr2Byte(ksn);
		byte_bdk = parseHexStr2Byte(bdk);
		
		ipek = GenerateIPEK(byte_ksn,byte_bdk);
		ipekStr = parseByte2HexStr(ipek);
		System.out.println("ipekStr=" + ipekStr);
		
		byte[] pinKey = GetPinKeyVariant(byte_ksn,ipek);
		String pinKeyStr = parseByte2HexStr(pinKey);
		System.out.println("pinKeyStr=" + pinKeyStr);
		
		datastr = "2B601229057B9BE1";
		deResultStr = parseByte2HexStr(TriDesDecryption(parseHexStr2Byte(pinKeyStr), parseHexStr2Byte(datastr)));
		System.out.println("pin: "+deResultStr);
	}
	
    public static byte[] GenerateIPEK(byte[] ksn, byte[] bdk)
    {
    	byte[] result;
    	byte[] temp, temp2, keyTemp;

        result = new byte[16];
        temp = new byte[8];
        keyTemp = new byte[16];

        System.arraycopy(bdk, 0, keyTemp, 0, 16);   //Array.Copy(bdk, keyTemp, 16);
        System.arraycopy(ksn, 0, temp, 0, 8);    //Array.Copy(ksn, temp, 8);
        temp[7] &= 0xE0;
        temp2 = TriDesEncryption(keyTemp,temp);    //TDES_Enc(temp, keyTemp, out temp2);temp
        System.arraycopy(temp2, 0, result, 0, 8);   //Array.Copy(temp2, result, 8);
        keyTemp[0] ^= 0xC0;
        keyTemp[1] ^= 0xC0;
        keyTemp[2] ^= 0xC0;
        keyTemp[3] ^= 0xC0;
        keyTemp[8] ^= 0xC0;
        keyTemp[9] ^= 0xC0;
        keyTemp[10] ^= 0xC0;
        keyTemp[11] ^= 0xC0;
//        TDES_Enc(temp, keyTemp, out temp2);
        temp2 = TriDesEncryption(keyTemp,temp);    //TDES_Enc(temp, keyTemp, out temp2);
//        Array.Copy(temp2, 0, result, 8, 8);
        System.arraycopy(temp2, 0, result, 8, 8);   //Array.Copy(temp2, 0, result, 8, 8);
        return result;
    }
    
    public static byte[] GetDUKPTKey(byte[] ksn, byte[] ipek)
    {
    	byte[] key;
    	byte[] cnt;
    	byte[] temp;
//    	byte shift;
    	int shift;

        key = new byte[16];
//        Array.Copy(ipek, key, 16);   
        System.arraycopy(ipek, 0, key, 0, 16); 

        temp = new byte[8];
        cnt = new byte[3];
        cnt[0] = (byte)(ksn[7] & 0x1F);
        cnt[1] = ksn[8];
        cnt[2] = ksn[9];
//        Array.Copy(ksn, 2, temp, 0, 6);
        System.arraycopy(ksn, 2, temp, 0, 6); 
        temp[5] &= 0xE0;

        shift = 0x10;
        while (shift > 0)
        {
            if ((cnt[0] & shift) > 0)
            {
//            	System.out.println("**********");
                temp[5] |= shift;
                NRKGP(key, temp);
            }
            shift >>= 1;
        }
        shift = 0x80;
        while (shift > 0)
        {
            if ((cnt[1] & shift) > 0)
            {
//            	System.out.println("&&&&&&&&&&");
                temp[6] |= shift;
                NRKGP(key, temp);
            }
            shift >>= 1;
        }
        shift = 0x80;
        while (shift > 0)
        {
            if ((cnt[2] & shift) > 0)
            {
//            	System.out.println("^^^^^^^^^^");
                temp[7] |= shift;
                NRKGP(key, temp);
            }
            shift >>= 1;
        }

        return key;
    }
    
    private static void NRKGP(byte[] key, byte[] ksn)
    {

    	byte[] temp, key_l, key_r, key_temp;
        int i;

        temp = new byte[8];
        key_l = new byte[8];
        key_r = new byte[8];
        key_temp = new byte[8];

//        Console.Write("");

//        Array.Copy(key, key_temp, 8);
        System.arraycopy(key, 0, key_temp, 0, 8); 
        for (i = 0; i < 8; i++)
            temp[i] = (byte)(ksn[i] ^ key[8 + i]);
//        DES_Enc(temp, key_temp, out key_r);
        key_r = TriDesEncryption(key_temp,temp);
        for (i = 0; i < 8; i++)
            key_r[i] ^= key[8 + i];

        key_temp[0] ^= 0xC0;
        key_temp[1] ^= 0xC0;
        key_temp[2] ^= 0xC0;
        key_temp[3] ^= 0xC0;
        key[8] ^= 0xC0;
        key[9] ^= 0xC0;
        key[10] ^= 0xC0;
        key[11] ^= 0xC0;

        for (i = 0; i < 8; i++)
            temp[i] = (byte)(ksn[i] ^ key[8 + i]);
//        DES_Enc(temp, key_temp, out key_l);
        key_l = TriDesEncryption(key_temp,temp);
        for (i = 0; i < 8; i++)
            key[i] = (byte)(key_l[i] ^ key[8 + i]);
        System.arraycopy(key_r, 0, key, 8, 8); 
    }
    
    public static byte[] GetDataKeyVariant(byte[] ksn, byte[] ipek)
    {
    	byte[] key;

        key = GetDUKPTKey(ksn, ipek);
        key[5] ^= 0xFF;
        key[13] ^= 0xFF;

        return key;
    }

    public static byte[] GetPinKeyVariant(byte[] ksn, byte[] ipek)
    {
    	byte[] key;

        key = GetDUKPTKey(ksn, ipek);
        key[7] ^= 0xFF;
        key[15] ^= 0xFF;

        return key;
    }
    
    public static byte[] GetMacKeyVariant(byte[] ksn, byte[] ipek)
    {
    	byte[] key;

        key = GetDUKPTKey(ksn, ipek);
        key[6] ^= 0xFF;
        key[14] ^= 0xFF;

        return key;
    }
	
    public static byte[] GetDataKey(byte[] ksn, byte[] ipek)
    {
    	byte[] temp1 = GetDataKeyVariant(ksn, ipek);
    	byte[] temp2 = temp1;
    	
    	byte[] key = TriDesEncryption(temp2,temp1);
 
        return key;
    }
	
	// 3DES
	public static byte[] TriDesEncryption(byte[] byteKey, byte[] dec) {

		try {
			byte[] en_key = new byte[24];
			if (byteKey.length == 16) {
				System.arraycopy(byteKey, 0, en_key, 0, 16);
				System.arraycopy(byteKey, 0, en_key, 16, 8);
			} else if (byteKey.length == 8) {
				System.arraycopy(byteKey, 0, en_key, 0, 8);
				System.arraycopy(byteKey, 0, en_key, 8, 8);
				System.arraycopy(byteKey, 0, en_key, 16, 8);
			} else {
				en_key = byteKey;
			}
			SecretKeySpec key = new SecretKeySpec(en_key, "DESede");

			Cipher ecipher = Cipher.getInstance("DESede/ECB/NoPadding");
			ecipher.init(Cipher.ENCRYPT_MODE, key);

			// Encrypt
			byte[] en_b = ecipher.doFinal(dec);

			// String en_txt = parseByte2HexStr(en_b);
			// String en_txt =byte2hex(en_b);
			return en_b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 3DES CBC
	public static byte[] TriDesDecryptionCBC(byte[] byteKey, byte[] dec) {
		byte[] en_key = new byte[24];
		if (byteKey.length == 16) {
			System.arraycopy(byteKey, 0, en_key, 0, 16);
			System.arraycopy(byteKey, 0, en_key, 16, 8);
		} else if (byteKey.length == 8) {
			System.arraycopy(byteKey, 0, en_key, 0, 8);
			System.arraycopy(byteKey, 0, en_key, 8, 8);
			System.arraycopy(byteKey, 0, en_key, 16, 8);
		} else {
			en_key = byteKey;
		}

		try {
			Key deskey = null;
			byte[] keyiv=new byte[8];
	        DESedeKeySpec spec = new DESedeKeySpec(en_key);
	        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
	        deskey = keyfactory.generateSecret(spec);

	        Cipher cipher = Cipher.getInstance("desede" + "/CBC/NoPadding");
	        IvParameterSpec ips = new IvParameterSpec(keyiv);

	        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

			byte[] de_b = cipher.doFinal(dec);

			return de_b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	// 3DES
	public static byte[] TriDesDecryption(byte[] byteKey, byte[] dec) {
		// private String TriDesDecryption(String dnc_key, byte[] dec){
		byte[] en_key = new byte[24];
		if (byteKey.length == 16) {
			System.arraycopy(byteKey, 0, en_key, 0, 16);
			System.arraycopy(byteKey, 0, en_key, 16, 8);
		} else if (byteKey.length == 8) {
			System.arraycopy(byteKey, 0, en_key, 0, 8);
			System.arraycopy(byteKey, 0, en_key, 8, 8);
			System.arraycopy(byteKey, 0, en_key, 16, 8);
		} else {
			en_key = byteKey;
		}
		SecretKey key = null;

		try {
			key = new SecretKeySpec(en_key, "DESede");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			Cipher dcipher = Cipher.getInstance("DESede/ECB/NoPadding");
			dcipher.init(Cipher.DECRYPT_MODE, key);

			byte[] de_b = dcipher.doFinal(dec);

			// String de_txt = parseByte2HexStr(removePadding(de_b));
			return de_b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}
	
	public static String dataFill(String dataStr) {
		int len = dataStr.length();
		if (len%16 != 0) {
			dataStr += "80";
			len = dataStr.length();
		}
		while (len%16 != 0) {
			dataStr += "0";
			len ++;
			System.out.println(dataStr);
		}
		return dataStr;
	}

}
