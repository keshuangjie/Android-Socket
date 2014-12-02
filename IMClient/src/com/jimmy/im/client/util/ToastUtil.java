package com.jimmy.im.client.util;

import com.jimmy.im.client.MyApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author keshuangjie
 * @date 2014-10-21 下午1:48:24
 * 单例Toast
 */
@SuppressLint("ShowToast")
public class ToastUtil {
	
	private Toast mToast;
	private Context mContext;
	
	private static ToastUtil mInstance;
	
	static{
		mInstance = new ToastUtil();
	}
	
	private ToastUtil(){
		mContext = MyApplication.getInstance().getApplicationContext();
	}
	
	public static ToastUtil getInstance(){
		return mInstance;
	}
	
	public void toast(String message){
		toast(message, Toast.LENGTH_SHORT);
	}
	
	public void toast(final String message, final int duration){
		if(isMainThread()){
			showToast(message, duration);
		}else{
			MyApplication.getInstance().post(new Runnable() {
				
				public void run() {
					showToast(message, duration);
				}
			});
		}
	}
	
	private void showToast(final String message, final int duration){
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
//		mToast.cancel();
		mToast.setText(message);
		mToast.setDuration(duration);
		mToast.show();
	}
	
	public void cancleToast(){
		mToast.cancel();
	}
	
	/**
	 * 判断当前是否是主线程
	 * @return
	 */
	private boolean isMainThread(){
		return Thread.currentThread() == Looper.getMainLooper().getThread();
	}
}
