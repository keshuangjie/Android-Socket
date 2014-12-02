package com.jimmy.im.server.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.os.Environment;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:46:59
 * @package com.jimmy.im.server.util
 * @version 1.0
 * 公用工具类
 */
public class CommonUtil {
	
	public static final String PATH_ROOT = "myVoice";
	public static final String PATH_SEPARATOR = File.separator;
	public static final String FILE_SUFFIX = ".amr";
	
	public static String getAmrFilePath(String name){
		String path = "";
		if(isSdcardMounted()){
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ PATH_SEPARATOR + PATH_ROOT + PATH_SEPARATOR + name
					+ FILE_SUFFIX;
		}
		return path;
	}
	
	public static boolean isSdcardMounted(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static String getLocalIP(){
    	String ip;
	    try {  
	         Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip = inetAddress.getHostAddress())) {
						return ip;
					}
	            }  
	        }  
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "获取失败";
    }
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

}
