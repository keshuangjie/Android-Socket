package com.jimmy.im.client.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import android.text.TextUtils;
import android.util.Log;

import com.jimmy.im.client.data.MsgEntity;
import com.jimmy.im.client.data.TextMsgEntity;
import com.jimmy.im.client.data.VoiceMsgEntity;
import com.jimmy.im.client.util.CommonUtil;

import de.greenrobot.event.EventBus;

/**
 * @author keshuangjie
 * @date 2014-12-4 下午4:30:41
 * @package com.jimmy.im.client.socket
 * @version 1.0
 * 双通道通信（接收和发送在不同的线程）
 */
public class MsgReceiveHandler extends Thread {

	private static final String TAG = MsgReceiveHandler.class.getSimpleName();

	InputStream mInputString = null;
	DataInputStream mDataInputStream = null;

	private Socket mSocket;

	public MsgReceiveHandler(Socket socket) {
		this.mSocket = socket;
	}

	public void run() {

		try {
			mInputString = mSocket.getInputStream();
			// 1、访问Socket对象的getInputStream方法取得客户端发送过来的数据流
			mDataInputStream = new DataInputStream(new BufferedInputStream(
					mInputString));

			while (true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (mSocket == null || mDataInputStream == null) {
					return;
				}
				
				Log.i(TAG, "before readInt");

				int type = mDataInputStream.readInt();
				
				Log.i(TAG, "run() -> type: " + type);
				
				Log.i(TAG, "after readInt");

				if (type == MsgEntity.TYPE_VOICE) {
					// 接收文件
					doFileReceive(mDataInputStream);
				} else if(type == MsgEntity.TYPE_TEXT){
					doTextReceive(mDataInputStream);
				}

			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 接收文本信息
	 * @param in
	 * @throws IOException
	 */
	private void doTextReceive(DataInputStream dis) throws IOException {
		String msg = null;
		int length = (int)dis.readInt();
		Log.i(TAG, "doTextReceive() -> length: " + length);
		byte[] buffer = new byte[length];
		dis.read(buffer, 0, buffer.length);
		msg = new String(buffer);
		Log.i(TAG, "doTextReceive() -> msg: " + msg);
		if (!TextUtils.isEmpty(msg.trim())) {
			TextMsgEntity entity = new TextMsgEntity();
			entity.msgContent = msg;
			EventBus.getDefault().post(entity);
		}
	}

	/**
	 * 接收文件处理
	 * 
	 * @param dis
	 */
	private void doFileReceive(DataInputStream dis){
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
				// fileName = System.currentTimeMillis() + "";
				Log.e(TAG, "run() -> name is null");
				return;
			}

			Log.i(TAG, "run() -> 文件名: " + entity.fileName);

			entity.size = dis.readLong();
			if (entity.size <= 0) {
				Log.e(TAG, "run() -> 文件大小为0");
				return;
			}
			Log.i(TAG, "run() -> 文件大小: " + entity.size);

			entity.time = dis.readInt();

			entity.filePath = CommonUtil.getAmrFilePath(entity.fileName);

			Log.i(TAG, "run() -> 保存路径：" + entity.filePath);
			// 创建目录
			if(!CommonUtil.CreateDir(entity.filePath)){
				Log.e(TAG, "create dir error " + entity.filePath);
				return;
			}

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
				// 如果文件读取完，退出循环，避免阻塞
				if (writeLens >= entity.size) {
					break;
				}
			}
			fo.flush();

			Log.i(TAG, "doReceive() -> 数据接收完毕");

			EventBus.getDefault().post(entity);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
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

}
