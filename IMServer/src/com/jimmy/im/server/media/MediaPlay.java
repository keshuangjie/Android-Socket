package com.jimmy.im.server.media;

import java.io.IOException;

import com.jimmy.im.server.util.CommonUtil;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
	
	private MediaPlayer mPlayer;
	
	private String source;
	
	public static final class AudioPlayHolder{
		public static MediaPlay sINSTANCE = new MediaPlay();
	}
	
	private MediaPlay(){
		mPlayer = new MediaPlayer();
	}
	
	public static MediaPlay getInstance(){
		return AudioPlayHolder.sINSTANCE;
	}
	
	public void startPlay(String name){
		this.source = CommonUtil.getAmrFilePath(name);;
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
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
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

}
