package com.jimmy.im.client.config;

import com.jimmy.im.client.util.SharedPreUtil;

public class Config {
	
	public static final String KEY_FIRST = "key_first";
	
	public static final String KEY_IP = "key_ip";
	public static final String KEY_PORT = "key_port";
	
	public static int CONNET_PORT = 5013;
	public static String CONNET_IP = "172.22.103.109";
	
	/**
	 * app启动时初始化服务器信息
	 */
	public static void initConnect(){
		SharedPreUtil sp = SharedPreUtil.getInstance();
		if(sp.getBoolean(KEY_FIRST, true)){
			sp.putBoolean(KEY_FIRST, false);
			sp.putString(KEY_IP, CONNET_IP);
			sp.putInt(KEY_PORT, CONNET_PORT);
		}else{
			Config.CONNET_IP = sp.getString(KEY_IP);
			Config.CONNET_PORT = sp.getInt(KEY_PORT);
		}
	}
	
	/**
	 * 重置服务器信息
	 * @param ip
	 * @param port
	 */
	public static void resetConncet(final String ip, final int port){
		Config.CONNET_IP = ip;
		Config.CONNET_PORT = port;
		SharedPreUtil sp = SharedPreUtil.getInstance();
		sp.putString(KEY_IP, CONNET_IP);
		sp.putInt(KEY_PORT, CONNET_PORT);
	}

}
