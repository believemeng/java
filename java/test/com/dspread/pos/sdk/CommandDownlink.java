package com.dspread.pos.sdk;


public class CommandDownlink {
	private Packet p = null;

	public CommandDownlink(int cmdID,int cmdCode, int cmdSubCode, int delay, byte[] paras) {
		p = new Packet(paras.length);
		p.setCmdID((byte) cmdID);
		p.setCmdCode((byte) cmdCode);
		p.setCmdSubCode((byte) cmdSubCode);
		p.setTimeOut((byte) delay);
		p.setDataField(paras);
		p.buildCRC();
	}

	public byte[] getBytes(){
		return p.getBytes();
	}
	

}
