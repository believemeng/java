package com.dspread.pos.sdk;

public class Packet {
	private byte[] bytes = new byte[0];
	private static final int HEADER_LEN = 4;   
	private static final int CRC_LEN = 1;  //1byte CRC

	private static final int OVERHEAD_LEN = HEADER_LEN + CRC_LEN; 
	
	private static final int DATA_FIELD_HEADER_LEN = 5;
	/**
	 * 
	 * @param len 
	 */
	protected Packet(int len){
		len = OVERHEAD_LEN + DATA_FIELD_HEADER_LEN + len;
		bytes = new byte[len];
		bytes[1] = 0;
		
		byte[] lens = Util.IntToHex(len - 4);
		
		if(lens.length==1){
			bytes[2] = lens[0];
		}else{
			bytes[1] = lens[0];
			bytes[2] = lens[1];
		}
		bytes[0] = 'M';
	}
	
	protected void setByte(int offset, byte dat){
		bytes[HEADER_LEN + offset] = dat;
	}
	
	protected byte getByte(int offset) {
		for(byte a:bytes){
		}
		if (offset+HEADER_LEN < bytes.length){
			return bytes[offset + HEADER_LEN];
		} else {		
			return 0x00;
		}
	}
	
	protected byte[] getBytes(){
		return bytes;
	}
	
	protected void setCmdID(byte cmdID){
		bytes[3] = cmdID;
	}
	
	protected byte getCmdID(){
		return bytes[3];
	}
	
	protected void setCmdCode(byte cmdCode){
		setByte(0, cmdCode);
	}
	
	protected void setCmdSubCode(byte cmdSubCode){
		setByte(1, cmdSubCode);
	}
	
	protected byte getCmdCode(){
		return getByte(0);
	}
	
	protected byte getCmdSubCode(){
		return getByte(1);
	}
	
	protected void setDataField(byte[] dataField){
		int len =  dataField.length;
		setByte(3, (byte)0);
		
		byte[] lens = Util.IntToHex(len);
		if(lens.length==1){
			setByte(4, lens[0]);
		}else{
			setByte(3, lens[0]);
			setByte(4, lens[1]);
		}
		System.arraycopy(dataField, 0, bytes, HEADER_LEN+5, dataField.length);
	}
	
	protected byte getDataFieldByte(int offset) {
		if (offset+HEADER_LEN + 5< bytes.length){
			return bytes[offset + HEADER_LEN + 5];
		} else {		
			return 0x00;
		}
	}
	
	protected void setTimeOut(byte timeOut){
		setByte(2, timeOut);
	}
	
	private byte calCRC() {
		byte crcbyte = bytes[0];
		for (int i = 1; i < bytes.length - 1; i++) {
			crcbyte = (byte) (crcbyte ^ bytes[i]);
		}
		return crcbyte;
	}
	
	protected void setCRC(byte crc) {
		bytes[bytes.length-1] = crc;
	}
	
	protected void buildCRC() {
		setCRC(calCRC());
	}

}
