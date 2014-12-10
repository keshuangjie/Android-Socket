package com.jimmy.im.server.media;

import java.io.File;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Environment;
import android.util.Log;

import com.jimmy.im.server.util.CommonUtil;
import com.jimmy.im.server.util.ToastUtil;

/**
 * @author keshuangjie
 * @date 2014-11-26 上午11:52:29
 * @version 1.0 录制音频文件
 */
public class MediaRecord{
	private static final String TAG = MediaRecord.class
			.getSimpleName();

//	public static final String PATH_ROOT = "MyIm";
//	public static final String PATH_VOICE = "voice";
//	public static final String PATH_SEPARATOR = File.separator;
//	public static final String FILE_SUFFIX = ".amr";

	private int SAMPLE_RATE_IN_HZ = 8000;
	/** 音量振幅的最大级别 */
	private int MAX_LEVEL_SIZE = 5;

	private boolean mIsRecoding = false;

	private MediaRecorder recorder;
	/** 录音失败回调 事件 */
	private OnRecordErrorListener mOnRecordErrorListener;
	/** 录音文件保存的路径 */
	private String mPath;

	private Thread mRecorderThread;

	public MediaRecord() {
		recorder = new MediaRecorder();
	}

	public MediaRecord(OnRecordErrorListener listener) {
		this();
		this.mOnRecordErrorListener = listener;
	}

	/**
	 * 参数初始化
	 */
	private void init() throws Exception {
		recorder.reset();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(mPath);
		recorder.setOnErrorListener(new OnErrorListener() {
			
			public void onError(MediaRecorder mr, int what, int extra) {
				MediaRecord.this.onError("录音异常");
			}
		});
	}

	/**
	 * 开始录音
	 * 
	 * @param name
	 *            保存路径相关
	 */
	public void start() {
		if (!CommonUtil.isSdcardMounted()) {
			//提示没有sdcard
			ToastUtil.getInstance().toast("没有sdcard");
			return;
		}
		mRecorderThread = new Thread(new RecordTask());
		mRecorderThread.start();
	}

	class RecordTask implements Runnable {

		public void run() {
			try {
				if (mRecorderThread.getId() == Thread.currentThread().getId()) {

					if (!newOutputPath()) {
						onError("创建目录失败");
						return;
					}
					
					init();
					
					recorder.prepare();
					recorder.start();
					
					mIsRecoding = true;
					
					Log.i(TAG, "RecordTask -> run() -> mIsRecoding: " + mIsRecoding);
				}
			} catch (Exception e) {
				if (mOnRecordErrorListener != null) {
					mOnRecordErrorListener.onError("录音异常");
				}
			}
		}

	}

	protected boolean isRecording() {
		return mIsRecoding;
	}

	public interface OnRecordErrorListener {
		void onError(String msg);
	}

	public void setOnErrorListener(OnRecordErrorListener listener) {
		this.mOnRecordErrorListener = listener;
	}

	/**
	 * 发生异常处理
	 * @param msg
	 */
	private void onError(String msg) {
		
		if (mOnRecordErrorListener != null) {
			mOnRecordErrorListener.onError(msg);
		}
		
		mPath = null;
		
		mIsRecoding = false;
	}

	/**
	 * 生成output路径
	 * @return
	 */
	private boolean newOutputPath() {
		this.mPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ CommonUtil.PATH_SEPARATOR + CommonUtil.PATH_ROOT 
				+ CommonUtil.PATH_SEPARATOR + CommonUtil.PATH_VOICE
				+ CommonUtil.PATH_SEPARATOR + System.currentTimeMillis() 
				+ CommonUtil.FILE_SUFFIX;
		File directory = new File(mPath).getParentFile();
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				ToastUtil.getInstance().toast("目录创建失败,请重试");
				return false;
			}
		}
		return true;
	}
	
	protected String getOutputPath(){
		return this.mPath;
	}

	/**
	 * 获取音量振幅级别
	 * 
	 * @return
	 */
	protected int getAmplitudeLevel() {
		return (int) (MAX_LEVEL_SIZE * getAmplitude() / 32768);
	}

	private double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else
			return 0;
	}

	protected void stop() {
		if (recorder != null && mIsRecoding) {
			mIsRecoding = false;
			recorder.stop();
		}
	}

	protected void release() {
		if (recorder != null) {
			mIsRecoding = false;
			recorder.reset();
			recorder.release();
			recorder = null;
		}
	}

}