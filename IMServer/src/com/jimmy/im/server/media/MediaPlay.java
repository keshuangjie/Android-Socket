package com.jimmy.im.server.media;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author keshuangjie
 * @date 2014-11-26 上午11:51:48
 * @version 1.0
 * 播放音频文件
 */
public class MediaPlay {
	private static final String TAG = MediaPlay.class.getSimpleName();
	
	private OnPlayCallbackListener mOnPlayCallbackListener;
	private MediaPlayer mPlayer;
	
	public MediaPlay(){
		init();
	}
	
	public MediaPlay(OnPlayCallbackListener listener){
		this.mOnPlayCallbackListener = listener;
		init();
	}
	
	private void init(){
		mPlayer = new MediaPlayer();
		mPlayer.setOnErrorListener(new OnErrorListener() {
			
			public boolean onError(MediaPlayer mp, int what, int extra) {
				MediaPlay.this.onError(mp);
				return true;
			}
		});
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				MediaPlay.this.onComplete(mp);
			}
		});
	}
	
	protected void start(String source){
		if(TextUtils.isEmpty(source)){
			Log.i(TAG, "startPlay() -> source is null");
			return;
		}
		try {
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.reset();
			mPlayer.setDataSource(source);
//			mPlayer.prepare();
//			mPlayer.start();
			mPlayer.prepareAsync();
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 完成回调
	 * @param mp
	 */
	private void onComplete(MediaPlayer mp){
		if(mOnPlayCallbackListener != null){
			mOnPlayCallbackListener.onComplete(mp);
		}
	}
	
	/**
	 * 错误回调
	 * @param mp
	 */
	private void onError(MediaPlayer mp){
		if(mOnPlayCallbackListener != null){
			mOnPlayCallbackListener.onError(mp);
		}
	}
	
	public interface OnPlayCallbackListener{
		void onComplete(MediaPlayer mp);
		
		void onError(MediaPlayer mp);
	}
	
	protected void setOnPlayCallbackListener(OnPlayCallbackListener listener){
		this.mOnPlayCallbackListener = listener;
	}
	
	protected void pause(){
		if(mPlayer != null && mPlayer.isPlaying()){
			mPlayer.pause();
		}
	}
	
	/**
	 * 释放资源，推荐在Activity/Fragment onPause()方法中调用
	 */
	protected void stop(){
		if(mPlayer != null && mPlayer.isPlaying()){
			mPlayer.stop();
		}
	}
	
	/**
	 * 释放资源，推荐在Activity/Fragment onStop()方法中调用
	 */
	protected void release(){
		if(mPlayer != null){
			mPlayer.release();
			mPlayer = null;
		}
	}

}
