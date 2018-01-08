import gnu.io.CommPortIdentifier;


import gnu.io.SerialPort;

import java.io.InputStream;

import java.io.OutputStream;
import java.util.Enumeration;
import com.dspread.pos.sdk.PosSdkConstants;
import com.dspread.pos.sdk.PosSdkJni;
import com.dspread.pos.sdk.Util;


public class Main {
	
	public static void transmit(OutputStream output,InputStream input,byte[] packat) throws Exception {
		output.write(packat);
		byte[] tmp = new byte[1024];
		int len  = 0;
		byte[] response;
		System.out.println("Write To Com:" + Util.bytes2Hex(packat));
        do {
        	int ret = input.read();
        	if(ret == -1){
        		continue;
        	}
        	byte b = (byte) ret;                    	
        	PosSdkJni.onChar(b);
        	tmp[len ++] = b;
        	//System.out.println(Util.byte2Hex(b));
        	//System.out.println("Complete:" + PosSdkJni.isPackageRreceiveComplete());
		} while (PosSdkJni.isPackageRreceiveNotComplete());
        response = new byte[len];
        System.arraycopy(tmp, 0, response, 0, len);
        System.out.println("Read From Com:" + Util.bytes2Hex(response));
	}
	
	public static void printICCResult(){
		System.out.println("EncrptMode:" +  Util.bytes2Hex(PosSdkJni.getBykey("EncrptMode")));
		System.out.println("TradeResult:" +  Util.bytes2Hex(PosSdkJni.getBykey("TradeResult")));
		System.out.println("IccDataType:" +  Util.bytes2Hex(PosSdkJni.getBykey("IccDataType")));
		System.out.println("IssScriptRes:" +  Util.bytes2Hex(PosSdkJni.getBykey("IssScriptRes")));
		System.out.println("FroceTradeAccept:" +  Util.bytes2Hex(PosSdkJni.getBykey("FroceTradeAccept")));
		System.out.println("IccData:" +  Util.bytes2Hex(PosSdkJni.getBykey("IccData")));
		System.out.println("CardStatus:" + Util.bytes2Hex(PosSdkJni.getBykey("CardStatus")));
		System.out.println("CardResult;" + Util.bytes2Hex(PosSdkJni.getBykey("CardResult")));
		System.out.println("TrackBlock:" + Util.bytes2Hex(PosSdkJni.getBykey("TrackBlock")));
		System.out.println("PinBlock:" + Util.bytes2Hex(PosSdkJni.getBykey("PinBlock")));
		System.out.println("PsamId:" + Util.bytes2Hex(PosSdkJni.getBykey("PsamId")));
		System.out.println("PosId:" + Util.bytes2Hex(PosSdkJni.getBykey("PosId")));
		System.out.println("MacBlock:" + Util.bytes2Hex(PosSdkJni.getBykey("MacBlock")));
		System.out.println("FormatId:" + Util.bytes2Hex(PosSdkJni.getBykey("FormatId")));
		System.out.println("CardMask:" + Util.bytes2Hex(PosSdkJni.getBykey("CardMask")));
		System.out.println("CardExpire:" + Util.bytes2Hex(PosSdkJni.getBykey("CardExpire")));
		System.out.println("ServiceCode:" + Util.bytes2Hex(PosSdkJni.getBykey("ServiceCode")));
		System.out.println("CardHolder:" + Util.bytes2Hex(PosSdkJni.getBykey("CardHolder")));
		System.out.println("CardSeqNo:" + Util.bytes2Hex(PosSdkJni.getBykey("CardSeqNo")));
	}
	
	public static void main(String[] args) throws Exception {
		//-Djava.library.path=
		System.out.println(System.getProperty("java.library.path"));
    	Enumeration comList = null;
        CommPortIdentifier portId = null;
        SerialPort serialPort = null;
        OutputStream output = null;
        InputStream input = null;

        comList = CommPortIdentifier.getPortIdentifiers();
        //serialPort = (SerialPort)portId.open("PcSDKJavaDemo", 2000);
        
       
        while (comList.hasMoreElements()) {
            portId = (CommPortIdentifier) comList.nextElement();
            //System.out.println(portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            	System.err.println("portID:"+portId.getName());
                if (portId.getName().equals("COM4")) {
                    serialPort = (SerialPort)portId.open("PcSDKJavaDemo", 2000);
                    System.out.println(serialPort);
                    output = serialPort.getOutputStream();
                    input = serialPort.getInputStream();
                    serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
//                    System.out.println(1111);
                    PosSdkJni.packU8("TradeMode",PosSdkConstants.TRADE_MODE_SWIPE_AND_IC);
                    PosSdkJni.packBytes("TradeAmount",new byte[]{0x31,0x31,0x31});
                    PosSdkJni.packBytes("TradeRandom",new byte[]{0x33,0x34,0x35});
        			PosSdkJni.packBytes("TradeExtra",new byte[]{0x34,0x35,0x36});
        			byte[] packat = PosSdkJni.getDLPackage(PosSdkConstants.POS_NEW_COMMAND,(byte)0x16, (byte)0x20, (byte)0x3C);
                    transmit(output, input, packat);
//        			PosSdkJni.
//                    System.out.println(2222);
                    while (PosSdkJni.getBykey("CmdID")[0] != PosSdkConstants.POS_RESULT){
                    	 packat = PosSdkJni.getDLPackage(PosSdkConstants.POS_QUERY_COMMAND,(byte)0x16, (byte)0x20, (byte)0x0A);
                         transmit(output, input, packat);
                         Thread.sleep( 2 * 1000);
//                         System.out.println(3333);
                    }
                    
                    if(PosSdkJni.getBykey("CardStatus")[0] == PosSdkConstants.TRADE_RESULT_IC_INSERTED){  //ic card inserted
                    	PosSdkJni.packU8("IcTradeMode",PosSdkConstants.IC_TRADE_MODE_NORMAL);
        				PosSdkJni.packU8("IcTradeType",PosSdkConstants.IC_TRADE_TYPE_GOODS);
        				PosSdkJni.packBytes("TradeTime",new byte[]{0x20,0x14,0x08,0x16,0x13,0x11,0x33,(byte)0xFF});
        				PosSdkJni.packBytes("ICTradeAmount",new byte[]{(byte)0xFF,(byte)0xFF,(byte)0xF1,0x11});
        				PosSdkJni.packBytes("TradeCurrencyCode",new byte[]{0x03,(byte)0x84});
        				packat = PosSdkJni.getDLPackage(PosSdkConstants.POS_NEW_COMMAND,(byte)0x16, (byte)0x30, (byte)0x3C);
        				transmit(output, input, packat);
//        				System.out.println(4444);
        				while (PosSdkJni.getBykey("CmdID")[0] != PosSdkConstants.POS_RESULT){
                       	 packat = PosSdkJni.getDLPackage(PosSdkConstants.POS_QUERY_COMMAND,(byte)0x16, (byte)0x30, (byte)0x0A);
                            transmit(output, input, packat);
                            Thread.sleep( 2 * 1000);
                        }
//        				System.out.println(5555);
        				printICCResult();        				
        				PosSdkJni.packBytes("IcTradeResponse",new byte[]{(byte)0x8A,0x02,0x30,0x30});
        				packat = PosSdkJni.getDLPackage(PosSdkConstants.POS_NEW_COMMAND,(byte)0x16, (byte)0x40, 0x3C);
        				transmit(output, input, packat);
        				while (PosSdkJni.getBykey("CmdID")[0] != PosSdkConstants.POS_RESULT){
                          	 packat = PosSdkJni.getDLPackage(PosSdkConstants.POS_QUERY_COMMAND,(byte)0x16, (byte)0x40, (byte)0x0A);
                               transmit(output, input, packat);
                               Thread.sleep( 2 * 1000);
//                               System.out.println(6666);
                          }
        				printICCResult();
                        
                    }else{//swipe
                    	System.out.println("Response CmdId:" +  Util.bytes2Hex(PosSdkJni.getBykey("CmdID")));
        				System.out.println("TrackBlock:" +  Util.bytes2Hex(PosSdkJni.getBykey("TrackBlock")));
        				System.out.println("Track1:" +  Util.bytes2Hex(PosSdkJni.getBykey("Magic1")));
        				System.out.println("Track2:" +  Util.bytes2Hex(PosSdkJni.getBykey("Magic2")));
        				System.out.println("Track3:" +  Util.bytes2Hex(PosSdkJni.getBykey("Magic3")));
        				System.out.println("PinBlock:" +  Util.bytes2Hex(PosSdkJni.getBykey("PinBlock")));
        				System.out.println("PsamId:" +  Util.bytes2Hex(PosSdkJni.getBykey("PsamId")));
        				System.out.println("PosId:" +  Util.bytes2Hex(PosSdkJni.getBykey("PosId")));
        				System.out.println("MacBlock:" +  Util.bytes2Hex(PosSdkJni.getBykey("MacBlock")));
        				System.out.println("FormatId:" +  new String(PosSdkJni.getBykey("FormatId")));
        				System.out.println("CardMask:" +  Util.bytes2Hex(PosSdkJni.getBykey("CardMask")));
        				System.out.println("CardExpire:" +  new String(PosSdkJni.getBykey("CardExpire")));
        				System.out.println("ServiceCode:" +  new String(PosSdkJni.getBykey("ServiceCode")));
        				System.out.println("CardHolder:" +  Util.bytes2Hex(PosSdkJni.getBykey("CardHolder")));
        				System.out.println("ActivateCode:" +  Util.bytes2Hex(PosSdkJni.getBykey("ActivateCode")));
        				
                    }
                }
            }
        }		
	}
}

