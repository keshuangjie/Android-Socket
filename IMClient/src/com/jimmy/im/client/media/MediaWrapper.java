package com.jimmy.im.client.media;

import com.jimmy.im.client.media.MediaPlay.OnPlayCallbackListener;
import com.jimmy.im.client.media.MediaRecord.OnRecordErrorListener;

/**
 * @author keshuangjie
 * @date 2014-12-10 下午1:14:35
 * @version 1.0
 * 录制、播放音频封装
 */
public class MediaWrapper {
	
	private MediaRecord mRecorder;
	private MediaPlay mPlayer;
	
	public static final class Holder{
		public static final MediaWrapper sINSTANCE = new MediaWrapper();
	}
	
	private MediaWrapper(){};
	
	public static MediaWrapper getInstance(){
		return Holder.sINSTANCE;
	}
	
	/**
	 * 开始播放
	 * @param source 音频文件路径
	 */
	public void startPlay(String source){
		startPlay(source, null);
	}
	
	/**
	 * 开始播放
	 * @param source 音频文件路径
	 * @param listener 回调事件
	 */
	public void startPlay(String source, OnPlayCallbackListener listener){
		if(mPlayer == null){
			mPlayer = new MediaPlay();
			mPlayer.setOnPlayCallbackListener(listener);
		}
		
		mPlayer.start(source);
	}
	
	/**
	 * 开始录音
	 */
	public void startRecord(){
		startRecord(null);
	}
	
	/**
	 * 开始录音
	 * @param listener 发生异常的回调事件
	 */
	public void startRecord(OnRecordErrorListener listener){
		if(mRecorder == null){
			mRecorder = new MediaRecord();
			mRecorder.setOnErrorListener(listener);
		}
		mRecorder.start();
	}
	
	/**
	 * 获取音频文件路径
	 * @return
	 */
	public String getRecordFilePath(){
		if(mRecorder != null){
			return mRecorder.getOutputPath();
		}
		return null;
	}
	
	/**
	 * 获取录音时音量振幅级别
	 * 
	 * @return
	 */
	public int getAmplitudeLevel(){
		int level = 0;
		if(mRecorder != null){
			level = mRecorder.getAmplitudeLevel();
		}
		return level;
	}
	
	/**
	 * 停止播放，在activity/fragment onPause()方法中调用
	 */
	public void stopPlay(){
		if(mPlayer != null){
			mPlayer.stop();
		}
	}
	
	/**
	 * 停止录音
	 */
	public void stopRecord(){
		if(mRecorder != null){
			mRecorder.stop();
		}
	}
	
	/**
	 * 释放播放资源，在activity/fragment onStop()方法中调用
	 */
	public void releasePlay(){
		if(mPlayer != null){
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	/**
	 * 释放录音资源，在activity/fragment onStop()方法中调用
	 */
	public void releaseRecord(){
		if(mRecorder != null){
			mRecorder.release();
			mRecorder = null;
		}
	}

}