package com.dspread.pos.sdk;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import junit.framework.TestCase;

public class TestPosSdkJni extends TestCase{
	private String updateFirmwareStr="";
	private OutputStream output = null;
	private InputStream input = null;
	private static int i=0;
	public void testSdk() throws Exception{
	  		//-Djava.library.path=
			//System.out.println(System.getProperty("java.library.path"));
			byte[] bytes = {0x4D,0x00,0x01,0x23, 0x6F};
			System.out.println(PosSdkJni.isPackageRreceiveComplete());			
			for (byte b : bytes) {
				PosSdkJni.onChar(b);
			}
			System.out.println(PosSdkJni.isPackageRreceiveComplete());
			
			PosSdkJni.packU8("TradeMode", (byte)0x0A);
			PosSdkJni.packBytes("TradeAmount", new byte[]{0x31,0x31,0x31});
			bytes = PosSdkJni.getDLPackage((byte)0x21, (byte)0x10, (byte)0x10, (byte)0x3C);
			System.out.println(Util.bytes2Hex(bytes));
			
			PosSdkJni.setTck(new byte[]{0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,});
			System.out.println(Util.bytes2Hex(PosSdkJni.getTck()));
			
			PosSdkJni.setTck(new byte[]{0x00,0x00,0x00,0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,});
			System.out.println(Util.bytes2Hex(PosSdkJni.calcMAC(new byte[]{0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,})));
			
			bytes = PosSdkJni.getDLPackage((byte)0x21, (byte)0x10, (byte)0x00, (byte)0x0A);
			bytes = new byte[]{0x4D, 0x00, 0x1F, 0x24, 0x10, 0x00, 0x00, 0x00, 0x19, 0x08, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x31, 0x0A, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7D};
			for (byte b : bytes) {
				PosSdkJni.onChar(b);
			}
			mytt();//open uart
			System.out.println(PosSdkJni.isPackageRreceiveComplete());
			System.out.println("PosId:" + Util.bytes2Hex(PosSdkJni.getBykey("PosId")));
			System.out.println("PsamId:" + Util.bytes2Hex(PosSdkJni.getBykey("PsamId")));
			System.out.println("CmdId:" + Util.bytes2Hex(PosSdkJni.getBykey("CmdID")));
//			setMasterKey("9B3A7B883A100F739B3A7B883A100F73","82E13665B4624DF5",0);
//			getPin(1, 0, 6, "Pls Input Pin", "00962B60AA556E65111", "", 30);
//			udpateWorkKey("DFEA613760EF4B8B7D538741B2C509E5","6C4E2C799D3B0EDF","CD0FE2DB34AE5C9C3BE9F3F6D83F1738","DF4F6D0A5A96FB82","CD0FE2DB34AE5C9C3BE9F3F6D83F1738","DF4F6D0A5A96FB82",0,20);
			powerOnNFC(30);
//			powerOffNFC(30);
			
			//set the lcd function
			/*String string="lcd set";
			byte[] paras = string.getBytes("GBK");
			lcdShowCustomDisplay(LcdModeAlign.LCD_MODE_ALIGNCENTER, Util.byteArray2Hex(paras), 6);*/
	}
	
	public void mytt(){
//		System.out.println(System.getProperty("java.library.path"));
    	Enumeration comList = null;
        CommPortIdentifier portId = null;
        SerialPort serialPort = null;
       
        comList = CommPortIdentifier.getPortIdentifiers();
        while (comList.hasMoreElements()) {
        	portId = (CommPortIdentifier) comList.nextElement();
        	if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        		System.out.println("portID:"+portId.getName());
        		 if (portId.getName().equals("COM4")) {
        			  try {
						serialPort = (SerialPort)portId.open("PcSDKJavaDemo", 2000);
						output = serialPort.getOutputStream();
						input = serialPort.getInputStream();
						serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                      System.out.println(serialPort);
        		 }
        	}
        }
	}
	
	public void getPin(int encryptType, int keyIndex, int maxLen, String typeFace, String cardNo, String data, int timeout) throws Exception{
		//预留两字节
		String str = "0000";
		//加密类型+密钥索引+输入密码最大长度
		str += (Util.byteArray2Hex(new byte[]{ (byte)encryptType }) + Util.byteArray2Hex(new byte[]{ (byte)keyIndex }) +Util.byteArray2Hex(new byte[]{ (byte)maxLen }));
		//显示字体长度+显示字体+卡号长度+卡号+附加数据长度+附加数据
		int typeFaceLen = 0;
		if(typeFace != null && !typeFace.equals("")){
			try {
				typeFaceLen = typeFace.getBytes().length + 1;
				str += Util.byteArray2Hex(new byte[] { (byte) typeFaceLen }) + Util.byteArray2Hex(typeFace.getBytes("gbk")) + "00";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			typeFaceLen = 0;
			typeFace = "";
			str += Util.byteArray2Hex(new byte[]{ (byte)typeFaceLen }) + typeFace;
		}
		int cardNoLen = 0;
		if(typeFace != null && !typeFace.equals("")){
			cardNoLen = cardNo.length();
			str += Util.byteArray2Hex(new byte[]{ (byte)cardNoLen }) + Util.byteArray2Hex(cardNo.getBytes());
		} else {
			cardNoLen = 0;
			cardNo = "";
			str += Util.byteArray2Hex(new byte[]{ (byte)cardNoLen }) + cardNo;
		}
		int dataLen = 0;
		if(typeFace != null && !typeFace.equals("")){
			dataLen = data.length()/2;
			str += Util.byteArray2Hex(new byte[]{ (byte)dataLen }) + data;
		} else {
			dataLen = 0;
			data = "";
			str += Util.byteArray2Hex(new byte[]{ (byte)dataLen }) + data;
		}
		CommandDownlink dc = new CommandDownlink(0x21,0x10, 0x71, timeout, Util.HexStringToByteArray(str));
		System.out.println("write getpin:"+Util.byteArray2Hex(dc.getBytes()));
		String response=transmit(output, input, dc.getBytes());
	}
	
	public void setMasterKey(String keyString, String checkValue,int keyIndex) throws Exception{
		String mastrkeyStr=keyString+checkValue+"0" + keyIndex;
		CommandDownlink dc = new CommandDownlink(0x21,0x10, 0xe2, 20, Util.HexStringToByteArray(mastrkeyStr));
		System.out.println("write masterKey:"+Util.byteArray2Hex(dc.getBytes()));
		String response=transmit(output, input, dc.getBytes());
		 if(response.substring(12,14).equals("08")){
			 System.out.println("update keys failed!");
		 }else if(response.substring(12,14).equals("00")){
			 System.out.println("update keys success!");
		 }
	}
	
	public void udpateWorkKey(String pik, String pikCheck, String trk,
			String trkCheck, String mak, String makCheck,int keyIndex,int timeout) {
		String str = "";
		int pikkLen = 0;
		if ((pik != null && !"".equals(pik))
				&& (pikCheck != null && !"".equals(pikCheck))) {
			pikkLen = pik.length() + pikCheck.length();
			pikkLen = pikkLen / 2;
		} else {
			pik = "";
			pikCheck = "";
		}
		str += Util.byteArray2Hex(new byte[] { (byte) pikkLen }) + pik
				+ pikCheck;

		int trkLen = 0;
		if ((trk != null && !"".equals(trk))
				&& (trkCheck != null && !"".equals(trkCheck))) {
			trkLen = trk.length() + trkCheck.length();
			trkLen = trkLen / 2;
		} else {
			trk = "";
			trkCheck = "";
		}
		str += Util.byteArray2Hex(new byte[] { (byte) trkLen }) + trk
				+ trkCheck;

		int makLen = 0;
		if ((mak != null && !"".equals(mak))
				&& (makCheck != null && !"".equals(makCheck))) {
			makLen = mak.length() + makCheck.length();
			makLen = makLen / 2;
		} else {
			mak = "";
			makCheck = "";
		}
		str += Util.byteArray2Hex(new byte[] { (byte) makLen }) + mak
				+ makCheck;
		updateFirmwareStr=str+ "0" + keyIndex;
		setUpdateKey(Util.HexStringToByteArray(updateFirmwareStr), timeout);
	}
	
	public void setUpdateKey(byte[] cmdBytes, int udpateWorkKey_timeout){
		CommandDownlink dc = new CommandDownlink(0x21,0x10, 0xf0, udpateWorkKey_timeout, cmdBytes);
		System.out.println("write updateKey:"+Util.byteArray2Hex(dc.getBytes()));
		try {
			String response=transmit(output, input, dc.getBytes());
			 if(response.substring(12,14).equals("08")){
        	System.out.println("update keys failed!");
	        }else if(response.substring(12,14).equals("00")){
	        	System.out.println("update keys success!");
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String transmit(OutputStream output,InputStream input,byte[] packat) throws Exception {
		output.write(packat);
		byte[] tmp = new byte[1024];
		int len  = 0;
		byte[] response;
		System.out.println("Write To Com:" + Util.bytes2Hex(packat));
        do {
        	int ret = input.read();
        	if(ret == -1){
        		i++;
        		if(i>=20){
        			break;
        		}else{
        			continue;
        		}
        	}
        	byte b = (byte) ret;                    	
        	PosSdkJni.onChar(b);
        	tmp[len ++] = b;
        	System.out.println("b+++:"+Util.byte2Hex(b));
        	System.out.println("Complete:" + PosSdkJni.isPackageRreceiveComplete());
		} while (PosSdkJni.isPackageRreceiveNotComplete());
        response = new byte[len];
        System.arraycopy(tmp, 0, response, 0, len);
        System.out.println("Read From Com:" + Util.byteArray2Hex(response));
        return Util.byteArray2Hex(response);
       
	}
	
	public void powerOnNFC(int timeout) throws Exception{
		CommandDownlink dc = null;
		dc = new CommandDownlink(0x21,0x17, 0x00, timeout,new byte[]{});
		System.out.println("power on NFC:"+Util.byteArray2Hex(dc.getBytes()));
		String response=transmit(output, input, dc.getBytes());
		if(response!=null&&response.length()>0){
			if(response.equals("4D0001236F")){
				dc=new CommandDownlink(0x22, 0, 0, 15,new byte[]{});
				i=0;
				Thread.sleep(2000);
				response=transmit(output, input, dc.getBytes());
				if(response!=null&&response.length()>0){
					String re=response.substring(18,20);
					if(re.equals("00")){
						return;
					}
					String ksn = response.substring(20,40);
					String atrLen=response.substring(40,42);
					String atrMaskLen=response.substring(42,44);
					int maskLen=(Integer.parseInt(atrMaskLen, 16))*2;
					String atr =response.substring(44,44+maskLen);
					System.out.println(atr);
//					sendApduByNFC("6A", 30);
				}
			}
		}
	}
	
	public void powerOffNFC(int timeout) throws Exception{
		CommandDownlink dc = null;
		dc = new CommandDownlink(0x21,0x17, 0x20, timeout,new byte[]{});
		System.out.println("power off NFC:"+Util.byteArray2Hex(dc.getBytes()));
		String reString=transmit(output, input, dc.getBytes());
		if(reString!=null&&reString.length()>0){
			System.out.println("power off success!");
		}else{
			System.out.println("power off failed!");
		}
	}

	public void sendApduByNFC(String apduString,int timeout) throws Exception{
		if(apduString==null||apduString.length()==0){
			return;
		}
		CommandDownlink dc = null;
		dc = new CommandDownlink(0x21,0x17, 0x10, timeout, Util.HexStringToByteArray(apduString));
		System.out.println("sendapdu:"+Util.byteArray2Hex(dc.getBytes()));
		String response=transmit(output, input, dc.getBytes());
		if(response!=null&&response.length()>0){
			String re=response.substring(18,20);
			if(re.equals("00")){
				return;
			}
			String apduLen=response.substring(20,22);
			String apduMaskLen=response.substring(22,24);
			int maskLen=(Integer.parseInt(apduMaskLen, 16));
			if (re.equals("11")) {
				apduLen = apduLen +255;
				maskLen= maskLen+255;
			}
			String apdu=response.substring(24,24+maskLen*2);
			System.out.println("apdu:"+apdu);
		}
	}
	
	public void lcdShowCustomDisplay(LcdModeAlign lcdModeAlign, String lcdFont,
			int timeout) throws Exception{
		String align = "00";
		if (lcdModeAlign == LcdModeAlign.LCD_MODE_ALIGNLEFT) {
			align = "00";
		} else if (lcdModeAlign == LcdModeAlign.LCD_MODE_ALIGNRIGHT) {
			align = "20";
		} else if (lcdModeAlign == LcdModeAlign.LCD_MODE_ALIGNCENTER) {
			align = "40";
		} else {
			align = "00";
		}
		String str = "";
		if (lcdFont != null && !"".equals(lcdFont)) {
			str = align + lcdFont + "00";
		}
		CommandDownlink dc = new CommandDownlink(0x21,0x41, 0x10, timeout,
				Util.HexStringToByteArray(str));
		System.out.println("lcd:"+Util.byteArray2Hex(dc.getBytes()));
		String response=transmit(output, input, dc.getBytes());
		if(response!=null||response.length()!=0){
			Thread.sleep(timeout*1000);
			System.out.println("lcd set success!");
		}else{
			System.out.println("lcd set fail!");
		}
	}
	
	public static enum LcdModeAlign {
		LCD_MODE_ALIGNLEFT, // (0x00)
		LCD_MODE_ALIGNRIGHT, // (0x20)
		LCD_MODE_ALIGNCENTER // (0x40)
	}
}

