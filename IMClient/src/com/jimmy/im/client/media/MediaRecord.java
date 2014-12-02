package com.jimmy.im.client.media;

import java.io.File;
import java.io.IOException;

import com.jimmy.im.client.util.CommonUtil;

import android.media.MediaRecorder;

/**
 * @author keshuangjie
 * @date 2014-11-26 上午11:52:29
 * @version 1.0
 * 录制音频文件
 */
public class MediaRecord {
	
	private static int SAMPLE_RATE_IN_HZ = 8000;

	private MediaRecorder recorder;
	private String path;

	public static final class MediaRecordHolder {
		public static final MediaRecord sINSTANCE = new MediaRecord();
	}

	private MediaRecord() {
	}

	public static MediaRecord getInstance() {
		return MediaRecordHolder.sINSTANCE;
	}

	private void initMediaRecoder() {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
		recorder.setOutputFile(path);
	}

	public void start(String name) {
		if (!CommonUtil.isSdcardMounted()) {
			return;
		}
		this.path = CommonUtil.getAmrFilePath(name);
		File directory = new File(path).getParentFile();
		if (!directory.exists()) {
			directory.mkdirs();
		}

		initMediaRecoder();

		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (recorder != null) {
			recorder.stop();
			recorder.release();
		}
	}

	public double getAmplitude() {
		if (recorder != null) {
			return (recorder.getMaxAmplitude());
		} else
			return 0;
	}
}