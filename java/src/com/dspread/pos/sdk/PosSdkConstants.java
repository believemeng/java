
package com.dspread.pos.sdk;

public class PosSdkConstants{ 


	public static final byte POS_RESET=(byte)0x20;
	public static final byte POS_NEW_COMMAND=(byte)0x21;
	public static final byte POS_QUERY_COMMAND=(byte)0x22;
	public static final byte POS_COMMAND_EXCTUTEING=(byte)0x23;
	public static final byte POS_RESULT=(byte)0x24;
	public static final byte POS_TIMEOUT=(byte)0x25;
	public static final byte POS_DESTORY=(byte)0x26;
	public static final byte POS_CAC_ERROR=(byte)0x27;
	public static final byte POS_USER_CANCEL=(byte)0x28;
	public static final byte POS_MAC_ERROR=(byte)0x29;
	public static final byte POS_ICC_INIT_FAILED=(byte)0x30;
	public static final byte POS_ICC_POWER_ON_FAILED=(byte)0x31;
	public static final byte POS_ICC_TRANSMIT_FAILED=(byte)0x32;
	public static final byte POS_EMV_TRANS_OVER=(byte)0x33;
	public static final byte POS_EMV_DENIAL=(byte)0x34;
	public static final byte POS_INCALID_COMMAND=(byte)0x35;
	public static final byte POS_DATA_PART=(byte)0x36;
	public static final byte POS_EMV_APP_CONFIG_ERROR=(byte)0x37;
	public static final byte POS_EMC_CAPK_CONFIG_ERROR=(byte)0x38;
	public static final byte POS_DATA_RW_ERROR=(byte)0x39;
	public static final byte POS_WORK_KEY_UPGRADE_ERROR=(byte)0x40;
	public static final byte POS_RETURN_IC_CARD_NO=(byte)0x41;
	public static final byte POS_CHANGE_TO_IC_CARD_NO=(byte)0x42;
	public static final byte POS_REPORT_ICC_CARD_NO=(byte)0x43;
	public static final byte POS_IC_CARD_US_BAD_OR_NO_EMV_APP=(byte)0x44;
	public static final byte POS_SEKECT_APP_FAILED=(byte)0x45;
	public static final byte POS_CAPK_NOT_SUPPORTED=(byte)0x46;
	public static final byte POS_FALLBACKIC=(byte)0x47;
	public static final byte CMD_RESP_SUCCESS=(byte)0x00;
	public static final byte CMD_RESP_UPGRADE_FAILED=(byte)0x01;
	public static final byte CMD_RESP_SELECT_EMV_APP=(byte)0x02;
	public static final byte CMD_RESP_ENVELOPE_INVALID=(byte)0x03;
	public static final byte CMD_RESP_ENVELOPE_LEN_ERROR=(byte)0x04;
	public static final byte CMD_RESP_DESTORY=(byte)0x05;
	public static final byte CMD_RESP_CMD_ID_NOT_MATCHED=(byte)0x06;
	public static final byte CMD_RESP_IC_CARD_INSERTED=(byte)0x07;
	public static final byte CMD_RESP_UPDATE_WORK_KEY_ERROR=(byte)0x08;
	public static final byte CMD_RESP_TMK_ERROR=(byte)0x09;
	public static final byte CMD_RESP_NEED_PIN=(byte)0x0a;
	public static final byte CMD_RESP_HARDWARE_FAILED=(byte)0x0b;
	public static final byte CMD_RESP_RANDOM_LEN_ERROR=(byte)0x0c;
	public static final byte CMD_RESP_MODE_NOT_SUPPORT=(byte)0x0d;
	public static final byte CMD_RESP_CRC_ERROE=(byte)0x0f;
	public static final byte CMD_RESP_MGT_PUBLIC_KEY_WRITE_ERROR=(byte)0x10;
	public static final byte CMD_RESP_CONFIG_PUBLIC_KEY_WRITE_ERROR=(byte)0x11;
	public static final byte CMD_RESP_WRITE_READ_DATA_ERROR=(byte)0x20;
	public static final byte CMD_RESP_EMV_APP_CONFIG_ERROR=(byte)0x21;
	public static final byte CMD_RESP_EMV_CAPK_CONFIG_ERROR=(byte)0x22;
	public static final byte TRADE_MODE_SWIPE_AND_IC=(byte)0x0a;
	public static final byte IC_TRADE_MODE_NORMAL=(byte)0x00;
	public static final byte IC_TRADE_TYPE_GOODS=(byte)0x01;
	public static final byte TRADE_RESULT_IC_INSERTED=(byte)0x01;
	public static final byte EMV_TRANS_GOODS=(byte)0x01;
	public static final byte EMV_TRANS_SERVICES=(byte)0x02;
	public static final byte EMV_TRANS_CASH=(byte)0x03;
	public static final byte EMV_TRANS_CASHBACK=(byte)0x04;
	public static final byte EMV_TRANS_INQUIRY=(byte)0x05;
	public static final byte EMV_TRANS_TRANFER=(byte)0x06;
	public static final byte EMV_TRANS_ADMIN=(byte)0x07;
	public static final byte EMV_TRANS_CASHDEPOSIT=(byte)0x08;
	public static final byte EMV_TRANS_PAYMENT=(byte)0x09;
	public static final byte EMV_TRANS_PBOCLOG=(byte)0x0a;
	public static final byte EMV_TRANS_SALE=(byte)0x0b;
	public static final byte EMV_TRANS_PREAUTH=(byte)0x0c;

}
