package com.beidousat.karaoke.player.proxy;

/**
 * Config
 * @author hellogv
 *
 */
public class Config{

	final static public String LOCAL_IP_ADDRESS = "127.0.0.1";
	final static public int HTTP_PORT = 80;
	final static public String HTTP_BODY_END = "\r\n\r\n";
	final static public String HTTP_RESPONSE_BEGIN = "HTTP/";
	final static public String HTTP_REQUEST_BEGIN = "GET ";
	final static public String HTTP_REQUEST_LINE1_END = " HTTP/";
	public final static int HEAD_LEN = 8;
	public final static int ENCODE_LEN = 1024;
	public final static String ENCODESF = ".bd";
	public final static String LINE_END = "\r\n";
	public final static byte[] ENCODE_HEAD = new byte[]{'B','E','I','D','O','U',' ',' '};
	public final static int ENCODE_HEAD_LEN = ENCODE_LEN+HEAD_LEN;

	static public class ProxyRequest{
		/**Http Request 内容*/
		public String _body;
		/**Ranage的位置*/
		public long _rangePosition;
	}
	
	static public class ProxyResponse{
		public byte[] _body;
		public byte[] _other;
		public long _currentPosition;
		public long _duration;
	}
}