package com.dspread.pos.sdk;
public class Util {
	
	 static final String HEXES = "0123456789ABCDEF";
	  public static String byteArray2Hex( byte [] raw ) {
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	  }
	public static String bytes2Hex(byte[] data){
		StringBuffer result = new StringBuffer();
		if(data == null){return result.toString();}
		for (int i = 0; i < data.length; i++) {
			result.append(byte2Hex(data[i])).append(" ");
		}
		return bytes2Hex(data,data.length);
	}
	
	public static String bytes2Hex(byte[] data,int len){
		StringBuffer result = new StringBuffer();
		if(data == null){return result.toString();}
		for (int i = 0; i < len; i++) {
			result.append(byte2Hex(data[i])).append(" ");
		}
		return bytes2Hex(data,0,len);
	}
	
	public static String bytes2HexWithoutSpace(byte[] data){
		return removeAllSpace(bytes2Hex(data));
	}
	
	public static String bytes2HexWithoutSpace(byte[] data,int len){
		return removeAllSpace(bytes2Hex(data,len));
	}
	
	public static String bytes2HexWithoutSpace(byte[] data,int start,int len){
		return removeAllSpace(bytes2Hex(data,start,len));
	}
	
	public static String removeAllSpace(String value){
		return value.replace(" ", "");
	}	
	
	public static String bytes2Hex(byte[] data,int start,int len){
		StringBuffer result = new StringBuffer();
		if(data == null){return result.toString();}
		for (int i = start; i < start + len; i++) {
			result.append(byte2Hex(data[i])).append(" ");
		}
		return result.toString();
	}
	
	public static String byte2Hex(byte b) {
		return  String.format("%02X", b);
	}
	
	
	public static byte int2Bcd(int value){
		return (byte)(((value / 10)<<4) | ((value - value/10*10) & 0x0F));
	}
	
	public static byte[] int2Bcd2(int value){
		byte[] results = new byte[2];
		if(value >= 100){
			results[0] = 1;
		}
		value -= value/100*100;
		results[1] = int2Bcd(value);
		return results;
	}
	
	public static int bcdBytes2Int(byte b1,byte b2){
		return (b1>>4 & (byte)0x0F)*1000 + (b1 & 0x0F) * 100+ bcdByte2Int(b2);
	}
	
	public static int bcdByte2Int(byte b){
		return (b>>4 & (byte)0x0F)*10 + (b & 0x0F);
	}
	
	public static byte[] ecb(byte[] in) {
		
		byte[] a1 = new byte[8];
		
		for(int i = 0;i < (in.length / 8);i++){
			byte[] temp = new byte[8];
			System.arraycopy(in, i*8, temp, 0, temp.length);
			a1 = xor8(a1,temp);
		}
		if((in.length % 8 ) != 0){
			byte[] temp = new byte[8];
			System.arraycopy(in, (in.length/8)*8, temp, 0, in.length - (in.length/8)*8);
			a1 = xor8(a1,temp);
		}
		return bcd2asc(a1);
	}
	
	public static byte[] bcd2asc(byte[]src){
		byte[] results = new byte[src.length * 2];
		for(int i=0;i<src.length;i++){
		    //��Nibbleת��
		    if(((src[i] & 0xF0) >> 4) <= 9){
		    	results[2*i] = (byte)(((src[i] & 0xF0) >> 4) + 0x30);
	        }else{
	        	results[2*i]  = (byte)(((src[i] & 0xF0) >> 4) + 0x37);   //��дA~F
	        }    
	        //��Nibbleת��
	        if((src[i] & 0x0F) <= 9){
	        	results[2*i + 1] = (byte)((src[i] & 0x0F) + 0x30);
	        }else{
	        	results[2*i + 1] = (byte)((src[i] & 0x0F) + 0x37);   	//��дA~F
	        }    
	    }
		return results;
	}
	
	public static byte[] IntToHex(int i) {
		String string = null;
		if (i >= 0 && i < 10) {
			string = "0" + i;
		} else {
			string = Integer.toHexString(i);
		}
		return HexStringToByteArray(string);
	}
	
	 /**
	 * 16进制格式的字符串转成16进制byte 44 --> byte 0x44
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] HexStringToByteArray(String hexString) {//
		if (hexString == null || hexString.equals("")) {
			return new byte[]{};
		}
		if (hexString.length() == 1 || hexString.length() % 2 != 0) {
			hexString = "0" + hexString;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	public static byte[] asc2bcd(byte[]src){
		byte[] results = new byte[src.length / 2];
		
		
		for(int i=0,j = 0;i<src.length;i++,i++,j++){
			byte tmph = (byte) (src[i] - 30);
			byte tmpl = (byte) (src[ i + 1] - 30);
			results[j] = (byte) (tmph << 4 | tmpl); 
	    }
		return results;
	}
	
	public static byte[] xor8(byte[] src1, byte[] src2){
	    byte[] results = new byte[8];
	    for (int i = 0; i < results.length; i++){
	    	results[i] = (byte)(src1[i] ^ src2[i]);
	    }
	    return results;
	}
	
	public static byte[] bcdString2Bytes(String bcdString){
		return hexString2Bytes(bcdString);
	}
	
	public static byte[] hexString2Bytes(String hex){
		byte[] results = new byte[hex.replace(" ", "").length() / 2];
		for (int i = 0; i < results.length; i++) {
			results[i] = (byte)Integer.parseInt(hex.substring(i*2, i*2 + 2),16);
		}
		return results;
	}


	public static boolean equals(byte[] bytes1, byte[] bytes2) {
		if(bytes1.length != bytes2.length){
			return false;
		}
		
		for (int i = 0; i < bytes1.length; i++) {
			if(bytes1[i] != bytes2[i]){
				return false;
			}
		}
		return true;
	}


	
}
