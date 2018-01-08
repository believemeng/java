package analyTLV;

public class TLV {
	 String tag;
	    int len;
	    byte[] value;

	    public String getTag() {
	        return tag;
	    }
	    public void setTag(String tag) {
	        this.tag = tag;
	    }
	    public int getLen() {
	        return len;
	    }
	    public void setLen(int len) {
	        this.len = len;
	    }
	    public byte[] getValue() {
	        return value;
	    }
	    public void setValue(byte[] value) {
	        this.value = value;
	    }
}
