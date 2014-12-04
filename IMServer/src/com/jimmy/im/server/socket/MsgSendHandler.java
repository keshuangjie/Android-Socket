package com.jimmy.im.server.socket;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import android.text.TextUtils;
import android.util.Log;

import com.jimmy.im.server.data.MsgEntity;
import com.jimmy.im.server.data.MsgQueueManager;
import com.jimmy.im.server.data.TextMsgEntity;
import com.jimmy.im.server.data.VoiceMsgEntity;

public class MsgSendHandler extends Thread {

	private static final String TAG = MsgSendHandler.class.getSimpleName();

	private Socket mSocket;
	OutputStream mOutputString = null;
	DataOutputStream mDataOutputStream = null;
	
	/** 会话唯一标识 */
//	private String mKey;
	
	public MsgSendHandler(Socket socket) {
		this.mSocket = socket;
	}

	public void run() {

		try {
			mOutputString = mSocket.getOutputStream();
			mDataOutputStream = new DataOutputStream(new BufferedOutputStream(
					mOutputString));

			while (true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				MsgEntity entity = MsgQueueManager.getInstance().poll();
				
				if(entity == null){
					continue;
				}
				
				if(entity instanceof VoiceMsgEntity){
					//发送语音消息
					doFileSend((VoiceMsgEntity) entity);
				}else if(entity instanceof TextMsgEntity){
					//发送文字消息
					doTextSend((TextMsgEntity) entity);
				}
			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 发送文件处理
	 * 
	 * @param dos
	 */
	private void doFileSend(VoiceMsgEntity entity) {
		if (mDataOutputStream == null || entity == null) {
			return;
		}

		String name = entity.fileName;
		Log.i(TAG, "doSend() -> new file name: " + name);

		File file = new File(entity.filePath);
		if (!file.exists()) {
			return;
		}

		FileInputStream reader = null;
		byte[] buf = null;

		try {
			
			//写入类型：语音
			mDataOutputStream.writeInt(MsgEntity.TYPE_VOICE);
			mDataOutputStream.flush();
			
			// 1. 读取文件输入流
			reader = new FileInputStream(file);
			// 2. 将文件内容写到Socket的输出流中
			// out.writeInt(UPLOAD);
			Log.i(TAG, "doSend() -> before writeUTF()");
			mDataOutputStream.writeUTF(name);
			Log.i(TAG, "doSend() -> after writeUTF()");
			mDataOutputStream.flush();
			mDataOutputStream.writeLong(file.length());
			mDataOutputStream.flush();
			mDataOutputStream.writeInt(entity.time);
			mDataOutputStream.flush();

			int bufferSize = 20480; // 20K
			buf = new byte[bufferSize];
			int read = 0;
			// 将文件输入流 循环 读入 Socket的输出流中
			while ((read = reader.read(buf, 0, buf.length)) != -1) {
				mDataOutputStream.write(buf, 0, read);
			}
			Log.i(TAG, "socket执行完成");
			mDataOutputStream.flush();
			// mSocket.shutdownOutput();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 发送文本消息
	 * @param entity
	 */
	private void doTextSend(TextMsgEntity entity) {
		if (mDataOutputStream == null || entity == null || TextUtils.isEmpty(entity.msgContent)) {
			return;
		}
		
		Log.i(TAG, "send to client: " + entity.msgContent);
		try {
			
			byte[] buffer = entity.msgContent.getBytes();
			
			//写入类型：文本
			mDataOutputStream.writeInt(MsgEntity.TYPE_TEXT);
			mDataOutputStream.flush();
			
			mDataOutputStream.writeInt(buffer.length);
			mDataOutputStream.flush();
			
			mDataOutputStream.write(buffer);
			mDataOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
