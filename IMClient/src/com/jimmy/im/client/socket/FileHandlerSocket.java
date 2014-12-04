package com.jimmy.im.client.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.text.TextUtils;
import android.util.Log;

import com.jimmy.im.client.config.Config;
import com.jimmy.im.client.data.VoiceMsgEntity;
import com.jimmy.im.client.util.CommonUtil;

import de.greenrobot.event.EventBus;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:31:25
 * @package com.jimmy.im.client.socket
 * @version 1.0
 * 文件发送接收处理  只能单通道通信
 */
@Deprecated
public class FileHandlerSocket extends Thread {
	private static final String TAG = FileHandlerSocket.class.getSimpleName();
	
	public static final int TYPE_SEND = 1;
	public static final int TYPE_RECEIVE = 2;
	
	private Socket mSocket;

	private VoiceMsgEntity latestEntity;
	
	private int mType;

	public FileHandlerSocket(int type) {
		this.mType = type;
	}

	public void run() {

		InputStream mInputString = null;
		OutputStream mOutputString = null;
		DataInputStream mDataInputStream = null;
		DataOutputStream mDataOutputStream = null;

		try {
			mSocket = new Socket();
			mSocket.connect(new InetSocketAddress(Config.CONNET_IP,
					Config.CONNET_PORT), 5000);
			mSocket.setKeepAlive(true);
			mInputString = mSocket.getInputStream();
			mOutputString = mSocket.getOutputStream();
			// 1、访问Socket对象的getInputStream方法取得客户端发送过来的数据流
			mDataInputStream = new DataInputStream(new BufferedInputStream(
					mInputString));
			mDataOutputStream = new DataOutputStream(new BufferedOutputStream(
					mOutputString));

			while (true) {
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(mType == TYPE_SEND){
					// 发送文件
					doSend(mDataOutputStream);
				}else if(mType == TYPE_RECEIVE){
					// 接收文件
					doReceive(mDataInputStream);
				}
			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			close();
		}

	}
	
	/**
	 * 接收文件处理
	 * @param dis
	 */
	private void doReceive(DataInputStream dis) {
		if (dis == null) {
			return;
		}
		Log.i(TAG, "doReceive()");
		BufferedOutputStream fo = null;

		try {
			Log.i(TAG, "doReceive() -> before readUTF()");
			VoiceMsgEntity entity = new VoiceMsgEntity();
			// 文件名
			entity.fileName = dis.readUTF();
			
			Log.i(TAG, "doReceive() -> after readUTF()");

			// 存储路径
			if (TextUtils.isEmpty(entity.fileName)) {
//				fileName = System.currentTimeMillis() + "";
				Log.e(TAG, "run() -> name is null");
				return;
			}
			
			Log.i(TAG, "run() -> 文件名: " + entity.fileName);
			
			entity.size = dis.readLong();
			if(entity.size <= 0){
				Log.e(TAG, "run() -> 文件大小为0");
				return;
			}
			Log.i(TAG, "run() -> 文件大小: " + entity.size);
			
			entity.time = dis.readInt();
			
			entity.filePath = CommonUtil.getAmrFilePath(entity.fileName);

			Log.i(TAG, "run() -> 保存路径：" + entity.filePath);
			// 创建目录
			CreateDir(entity.filePath);

			// 2、将数据流写到文件中
			fo = new BufferedOutputStream(new FileOutputStream(new File(
					entity.filePath)));

			int bytesRead = 0;
			byte[] buffer = new byte[2048];
			int writeLens = 0;
			while ((bytesRead = dis.read(buffer, 0, buffer.length)) != -1) {
				writeLens += bytesRead;
				Log.i(TAG, "doReceive() -> 接收文件完成，文件长度：" + writeLens);
				fo.write(buffer, 0, bytesRead);
				//如果文件读取完，退出循环，避免阻塞
				if(writeLens >= entity.size){
					break;
				}
			}
			fo.flush();
			
			Log.i(TAG, "doReceive() -> 数据接收完毕");

			EventBus.getDefault().post(entity);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
//			e.printStackTrace();
			Log.i(TAG, "doReceive() -> IOException error");
		} finally {
			try {
				if (fo != null) {
					fo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发送文件处理
	 * @param dos
	 */
	private void doSend(DataOutputStream dos) {
		if (dos == null) {
			return;
		}

		if (newSendRequest()) {
			String name = latestEntity.fileName;
			Log.i(TAG, "doSend() -> new file name: " + name);
			
			File file = new File(latestEntity.filePath);
			if(!file.exists()){
				return;
			}

			FileInputStream reader = null;
			byte[] buf = null;
			
			try {
				// 1. 读取文件输入流
				reader = new FileInputStream(file);
				// 2. 将文件内容写到Socket的输出流中
				// out.writeInt(UPLOAD);
				Log.i(TAG, "doSend() -> before writeUTF()");
				dos.writeUTF(name);
				Log.i(TAG, "doSend() -> after writeUTF()");
				dos.flush();
				dos.writeLong(file.length());
				dos.flush();
				dos.writeInt(latestEntity.time);
				dos.flush();

				int bufferSize = 20480; // 20K
				buf = new byte[bufferSize];
				int read = 0;
				// 将文件输入流 循环 读入 Socket的输出流中
				while ((read = reader.read(buf, 0, buf.length)) != -1) {
					dos.write(buf, 0, read);
				}
//				dos.write(null);
				Log.i(TAG, "socket执行完成");
				dos.flush();
//				mSocket.shutdownOutput();
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

	}

	private boolean newSendRequest() {
		VoiceMsgEntity entity =  (VoiceMsgEntity) EventBus.getDefault().getStickyEvent(
				VoiceMsgEntity.class);
		if (entity == null) {
			return false;
		}
		
		File file = new File(entity.filePath);
		
		if(!file.exists()){
			return false;
		}

		if (latestEntity == entity) {
			return false;
		}

		latestEntity = entity;

		return true;
	}

	private void close() {
		try {
			if (mSocket != null) {
				mSocket.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "run() -> io error");
		}
	}

	// 创建目录（不存在则创建）
	public File CreateDir(String dir) {
		Log.i(TAG, "CreateDir()");
		File file = new File(dir);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}
}
