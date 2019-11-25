package com.bestarmedia.pcmrecord;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.ByteOrder;

class RecordUtil {
	static float getVolume(byte [] buf1, short[] volumeBuffer, boolean isBigEndian){
		Bytes2Shorts(buf1, volumeBuffer, isBigEndian);
		int size = volumeBuffer.length;
/*		double sum = 0;
		for (short rawSample : volumeBuffer) {
	        double sample = rawSample / 32768.0;
	        sum += sample * sample;
	    }

		
		
		double rms = Math.sqrt(sum / size);
	    double db = 20 * Math.log10(rms);
		
	    return (float) -db;
		*/
		
		
		
		double v = 0;
		for(int i=0; i<size; i++){
			v += volumeBuffer[i] * volumeBuffer[i];
		}
		
//        double mean = v / (double) size;
//        double volume = 10 * Math.log10(v / (double) size);
        return (float) (10 * Math.log10(v / size));
	}
	
	static void Bytes2Shorts(byte[] buf, short [] s, boolean isBigEndian) {
        byte[] temp = new byte[2];
        for (int i = 0; i < s.length; i++) {
        	temp[0] = buf[i * 2];
        	temp[1] = buf[i * 2 + 1];
            s[i] = getShort(temp, isBigEndian);
        }
    }
    
	static short getShort(byte[] buf, boolean bBigEnding) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 2) {
            throw new IllegalArgumentException("byte array size > 2 !");
        }
        short r = 0;
        if (bBigEnding) {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        } else {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x00ff);
            }
        }
 
        return r;
    }
    
	static boolean testCPU() {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            return true;
        } else {
            return false;
        }
    }
    
	static String getProp(String key) {
		String ret = "";
		try {
			Class<?> SystemProperties = Class.forName("android.os.SystemProperties");
			Method get = SystemProperties.getMethod("get", new Class[]{String.class});
			Object obj = get.invoke(SystemProperties, new Object[]{key});
			if(obj != null) {
				ret = (String)obj;
			}
		} catch (Exception e) {
			ret = "";
			e.printStackTrace();
		}
		return ret;
	}
	
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    
 	public static String getTempPath(String rootPath){
 		File f = new File(rootPath);
 		try {
 			if(!f.exists()) {
 				if(!f.mkdir()){
 					UtilLog.e("getTempPath create path="+rootPath+"  is error");
 					return null;
 				}
 			}
 		} catch (Exception e){
 			e.printStackTrace();
 			return null;
 		}
 		
 		File dir = null;
 		String ret = null;
 		for(int i=0; i<1000; i++) {
 			dir = new File(f, "cache"+i);
 			try {
 				if(dir.exists() && dir.canRead() && dir.canWrite()) {
 					ret = getDir(dir);
 					if(ret != null) {
 						return ret;
 					}
 				} else {
 					if(dir.mkdir()) {
 						return dir.getAbsolutePath();
 					} else {
 						UtilLog.e("getTempPath dir="+dir.getAbsolutePath()+" is error");
 					}
 				}
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 		}
 		return null;
 	}
 	
 	private static String getDir(File dir){
 		try {
 			File []files = dir.listFiles();
 			if(files != null) {
 				for(File f : files) {
 					if(!f.delete()) {
 						return null;
 					}
 				}
 			}
 			return dir.getAbsolutePath();
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
 	}
 	
 	private static String getPath(File dir){
 		try {
 			File []files = dir.listFiles();
 			if(files != null) {
 				for(File f : files) {
 					f.delete();
 				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		File tempFile = new File(dir, "temp"+System.currentTimeMillis());
 		try {
 			if(tempFile.createNewFile()){
 				if(tempFile.delete()){
 					return tempFile.getAbsolutePath();
 				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
 	}
}
