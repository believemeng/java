package com.dspread.pos.sdk;


public class PosSdkJni {
  public final static boolean isPackageRreceiveNotComplete(){
		return isPackageRreceiveComplete() == false; 
  }
  public final static native boolean isPackageRreceiveComplete();
  public final static native void onChar(byte b);
  public final static native void onPackage(byte[] bytes);  
  public final static native byte[] getBykey(String key);
  public final static native byte[] getDLPackage(byte comId, byte comCode, byte subComCode, int timeout);
  public final static native void packBytes(String key, byte[] bytes);
  public final static native void packU8(String key, byte b);
  public final static native void setTck(byte[] newKey);
  public final static native byte[] getTck();
  public final static native byte[] calcMAC(byte[] to_mac);
  static{
	  System.loadLibrary("pos_sdk");
  }
  
}
