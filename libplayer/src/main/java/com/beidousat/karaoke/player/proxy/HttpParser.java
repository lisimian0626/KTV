package com.beidousat.karaoke.player.proxy;

import com.beidousat.karaoke.player.proxy.Config.ProxyRequest;
import com.beidousat.karaoke.player.proxy.Config.ProxyResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Http报文处理类
 *
 * @author hellogv
 */
public class HttpParser {
    final static public String TAG = "HttpParser";
    final static private String RANGE_PARAMS = "Range: bytes=";
    final static private String RANGE_PARAMS_0 = "Range: bytes=0-";
    final static private String CONTENT_RANGE_PARAMS = "Content-Range: bytes ";
    final static private String CONTENT_LENGTH_PARAMS = "Content-Length: ";

    private static final int HEADER_BUFFER_LENGTH_MAX = 1024 * 50;
//	private byte[] headerBuffer = new byte[HEADER_BUFFER_LENGTH_MAX];
//	private int headerBufferLength=0;

    /**
     * 链接带的端口
     */
    private int remotePort = -1;
    /**
     * 远程服务器地址
     */
    private String remoteHost;
    /**
     * 代理服务器使用的端口
     */
    private int localPort;
    /**
     * 本地服务器地址
     */
    private String localHost;

    private boolean isEncrypt = false;

    public HttpParser(String rHost, int rPort, String lHost, int lPort, boolean isEncryt) {
        remoteHost = rHost;
        remotePort = rPort;
        localHost = lHost;
        localPort = lPort;
        this.isEncrypt = isEncryt;
    }

    public void clearHttpBody() {
//		headerBuffer = new byte[HEADER_BUFFER_LENGTH_MAX];
//		headerBufferLength=0;
    }

    /**
     * 获取Request报文
     *
     * @param source
     * @param length
     * @return
     */
    public byte[] getRequestBody(byte[] source, int length) {
        List<byte[]> httpRequest = getHttpBody(Config.HTTP_REQUEST_BEGIN, Config.HTTP_BODY_END, source, length);
        if (httpRequest.size() > 0) {
            return httpRequest.get(0);
        }
        return null;
    }

    public ProxyRequest getProxyRequest2(byte[] bodyBytes) {
        ProxyRequest result = new ProxyRequest();
        //获取Body
        result._body = new String(bodyBytes);
//		System.out.println(result._body+" result._body ");
        if (result._body.contains(RANGE_PARAMS) == false)
            result._rangePosition = 0;
        else {
            String rangePosition = Utils.getSubString(result._body, RANGE_PARAMS, "-");
            result._rangePosition = Integer.valueOf(rangePosition);
        }
        return result;
    }

    public String getServerBody(ProxyRequest result, int start) {
        String ret = result._body.replace(localHost, remoteHost);
        if (remotePort == -1)
            ret = ret.replace(":" + localPort, "");
        else
            ret = ret.replace(":" + localPort, ":" + remotePort);
        if (ret.contains(RANGE_PARAMS) == false) {
            ret = ret.replace(Config.HTTP_BODY_END,
                    "\r\n" + RANGE_PARAMS + start + "-" + Config.HTTP_BODY_END);
        } else {
            ret = ret.replace(RANGE_PARAMS + result._rangePosition + "-",
                    RANGE_PARAMS + start + "-");
        }
//        System.out.println("===body==" + ret);
        return ret;
    }

    /**
     * Request报文解析转换ProxyRequest
     *
     * @param bodyBytes
     * @return
     */
    public ProxyRequest getProxyRequest(byte[] bodyBytes) {
        ProxyRequest result = new ProxyRequest();
        //获取Body
        result._body = new String(bodyBytes);

        // 把request中的本地ip改为远程ip
        result._body = result._body.replace(localHost, remoteHost);
        // 把代理服务器端口改为原URL端口
        if (remotePort == -1)
            result._body = result._body.replace(":" + localPort, "");
        else
            result._body = result._body.replace(":" + localPort, ":" + remotePort);

        //不带Ranage则添加补上，方便后面处理
        if (result._body.contains(RANGE_PARAMS) == false)
            result._body = result._body.replace(Config.HTTP_BODY_END,
                    "\r\n" + RANGE_PARAMS_0 + Config.HTTP_BODY_END);


//		Log.i(TAG, "======\r\n"+result._body);
        //获取Ranage的位置
        String rangePosition = Utils.getSubString(result._body, RANGE_PARAMS, "-");
//		Log.i(TAG,"------->rangePosition:"+rangePosition);
        result._rangePosition = Integer.valueOf(rangePosition);
        if (isEncrypt) {
            if (result._rangePosition >= Config.ENCODE_LEN) {
                int startIndex = result._body.indexOf(RANGE_PARAMS) + RANGE_PARAMS.length();
                int endIndex = result._body.indexOf(Config.LINE_END, startIndex);
                String[] arr = result._body.substring(startIndex, endIndex).split("-");

                if (arr.length > 0) {
                    int currentPosition = Integer.valueOf(arr[0]);
                    if (currentPosition > 0) {
                        currentPosition = currentPosition + Config.HEAD_LEN;
                        int getlen = 0;
                        if (arr.length > 1) {
                            getlen = Integer.valueOf(arr[0]) + Config.HEAD_LEN;
                        }
                        String prev = result._body.substring(0, startIndex);
                        prev = prev + currentPosition + "-";
                        if (getlen > 0)
                            result._body = prev + getlen + result._body.substring(endIndex);
                        else
                            result._body = prev + result._body.substring(endIndex);
                    }
                }
            } else if (result._rangePosition > 0) {
                result._body = result._body.replace(RANGE_PARAMS + result._rangePosition + "-",
                        RANGE_PARAMS_0);
            }

        }
//		result._body = result._body.replace("Connection: close","Connection: Keep-Alive");
//		Log.i(TAG, "++++\r\n"+result._body);
        return result;
    }

    public boolean getisEncrypt() {
        return isEncrypt;
    }

    /**
     * 获取ProxyResponse
     *
     * @param source
     * @param length
     */
    public ProxyResponse getProxyResponse(byte[] source, int length) {
        List<byte[]> httpResponse = getHttpBody(Config.HTTP_RESPONSE_BEGIN, Config.HTTP_BODY_END, source, length);
        if (httpResponse.size() == 0)
            return null;
        ProxyResponse result = new ProxyResponse();
        //获取Response正文
        result._body = httpResponse.get(0);
        String text = new String(result._body);
//		Log.i(TAG + "<---", text);
        //获取二进制数据
        if (httpResponse.size() == 2)
            result._other = httpResponse.get(1);
        //样例：Content-Range: bytes 2267097-257405191/257405192
        try {
            // 获取起始位置
            String currentPosition = Utils.getSubString(text, CONTENT_RANGE_PARAMS, "-");
            result._currentPosition = Integer.valueOf(currentPosition);
            // 获取最终位置
            String startStr = CONTENT_RANGE_PARAMS + currentPosition + "-";
            String duration = Utils.getSubString(text, startStr, "/");
            result._duration = Integer.valueOf(duration);
        } catch (Exception ex) {
//            Log.e(TAG, Utils.getExceptionMessage(ex));
        }
        return result;
    }

    /**
     * 替换Request报文中的Range位置,"Range: bytes=0-" -> "Range: bytes=XXX-"
     *
     * @param requestStr
     * @param position
     * @return
     */
    public String modifyRequestRange(String requestStr, long position) {
        String str = Utils.getSubString(requestStr, RANGE_PARAMS, "-");
        str = str + "-";
        String result = requestStr.replaceAll(str, position + "-");
        return result;
    }

    private List<byte[]> getHttpBody(String beginStr, String endStr, byte[] source, int length) {
        List<byte[]> result = new ArrayList<byte[]>();
        try {
//		if((headerBufferLength+length)>=headerBuffer.length){
//			clearHttpBody();
//		}
//		
//		System.arraycopy(source, 0, headerBuffer, headerBufferLength, length);
//		headerBufferLength+=length;
//
            String responseStr = new String(source);
            if (responseStr.contains(beginStr) && responseStr.contains(endStr)) {
                int startIndex = responseStr.indexOf(beginStr, 0);
                int endIndex = responseStr.indexOf(endStr, startIndex);
                endIndex += endStr.length();
//			responseStr = responseStr.replace("Connection: close","Connection: Keep-Alive");
                byte[] header = new byte[endIndex - startIndex];
                System.arraycopy(source, startIndex, header, 0, header.length);
                result.add(header);
//			System.out.println(header.length+" ===source============"+source.length+" == "+length);
                if (length > header.length) {//还有数据
                    byte[] other = new byte[length - header.length];
                    System.arraycopy(source, header.length, other, 0, other.length);
//				System.out.println("===headerBufferLength============"+other.length);

//				byte[] dec = Decoder.getinstance().decoder(other,other.length);
//				int len = dec.length;
//				if(other.length == len)
                    {
                        //isEncrypt = false;
                        result.add(other);
                    }
//				else  
//				{ 
////					byte[] other2 = new byte[len];  
////					System.arraycopy(other, 0, other2, 0,len);
//					result.add(dec);
//					isEncrypt = true;
//					
//				}
                }
                clearHttpBody();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public byte[] MPbody(byte[] body, long range) {
        if (!isEncrypt) return body;
        String bodystr = new String(body);
        int startIndex;
        int total = 0;
        int endIndex;
        String prev;
        try {
            // 获取起始位置
            startIndex = bodystr.indexOf(CONTENT_RANGE_PARAMS) + CONTENT_RANGE_PARAMS.length();
            endIndex = bodystr.indexOf("-", startIndex);
            int currentPosition = Integer.valueOf(bodystr.substring(startIndex, endIndex));
            if (currentPosition > 0)
                currentPosition = currentPosition - Config.HEAD_LEN;
            else if (range < Config.ENCODE_LEN) {
                currentPosition = (int) range;
            }
            // 获取最终位置
            int durindex = bodystr.indexOf("/", endIndex);
            int duration = Integer.valueOf(bodystr.substring(endIndex + 1, durindex));
            if (duration > 0)
                duration = duration - Config.HEAD_LEN;
            endIndex = bodystr.indexOf(Config.LINE_END, durindex);
            total = Integer.valueOf(bodystr.substring(durindex + 1, endIndex));
            prev = bodystr.substring(0, startIndex);
            total = total - Config.HEAD_LEN;
            bodystr = prev + currentPosition + "-" + duration + "/" + total + bodystr.substring(endIndex);
        } catch (Exception ex) {
//            Log.e(TAG, ex.getMessage());
        }
        startIndex = bodystr.indexOf(CONTENT_LENGTH_PARAMS);
        if (startIndex > 0) {
            startIndex = startIndex + CONTENT_LENGTH_PARAMS.length();
            endIndex = bodystr.indexOf(Config.LINE_END, startIndex);
            prev = bodystr.substring(0, startIndex);
            int len = Integer.valueOf(bodystr.substring(startIndex, endIndex));
            if (total < len) len = total;
            bodystr = prev + len + bodystr.substring(endIndex);
        }
//		System.out.println(bodystr);
        return bodystr.getBytes();

    }
}
