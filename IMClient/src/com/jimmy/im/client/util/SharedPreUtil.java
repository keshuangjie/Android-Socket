package com.jimmy.im.client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jimmy.im.client.MyApplication;

/**
 * @author keshuangjie
 * @date 2014-12-2 下午1:33:35
 * @package com.jimmy.im.client.util
 * @version 1.0
 * TODO
 */
public class SharedPreUtil {
	
	private static final String FILE_KEY = "sever";
	
	private SharedPreferences sp;
	
	private static SharedPreUtil mInstance;
	
	static{
		mInstance = new SharedPreUtil();
	}
	
	private SharedPreUtil(){
		sp = MyApplication.getInstance().getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
	}
	
	public static SharedPreUtil getInstance(){
		return mInstance;
	}
	
	public void putString(String key, String value){
		Editor edit = getEditor();
		edit.putString(key, value);
		edit.commit();
	}
	
	public String getString(String key){
		return sp.getString(key, "");
	}
	
	public void putInt(String key, int value){
		Editor edit = getEditor();
		edit.putInt(key, value);
		edit.commit();
	}
	
	public boolean getBoolean(String key){
		return getBoolean(key, false);
	}
	
	public boolean getBoolean(String key, boolean defaultValue){
		return sp.getBoolean(key, defaultValue);
	}
	
	public void putBoolean(String key, boolean value){
		Editor edit = getEditor();
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	public int getInt(String key){
		return sp.getInt(key, -1);
	}
	
	public Editor getEditor(){
		return sp.edit();
	}

}
