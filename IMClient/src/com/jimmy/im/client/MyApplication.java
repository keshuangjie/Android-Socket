package com.jimmy.im.client;

import com.jimmy.im.client.config.Config;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:38:15
 * @package com.jimmy.im.client
 * @version 1.0
 */
public class MyApplication extends Application{
	
	private static MyApplication mInstance;
	
	private Handler mHandler;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mInstance = this;
		
		mHandler = new Handler(Looper.getMainLooper());
		
		Config.initConnect();
	}
	
	public static MyApplication getInstance(){
		return mInstance;
	}
	
	public void post(Runnable runnable){
		if(runnable != null){
			mHandler.post(runnable);
		}
	}

}
