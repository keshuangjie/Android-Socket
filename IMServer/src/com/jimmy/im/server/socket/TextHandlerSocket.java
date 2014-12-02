package com.jimmy.im.server.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import android.text.TextUtils;
import android.util.Log;

import com.jimmy.im.server.data.TextMsgEntity;

import de.greenrobot.event.EventBus;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:46:21
 * @package com.jimmy.im.server.socket
 * @version 1.0
 * 字符串传输接收处理
 */
public class TextHandlerSocket extends Thread {
	private static final String TAG = TextHandlerSocket.class.getSimpleName();

	private Socket mSocket;

	TextMsgEntity latestEntity;

	InputStream in = null;
	OutputStream out = null;

	public TextHandlerSocket(Socket socket) {
		mSocket = socket;
	}

	@Override
	public void run() {

		try {
			mSocket.setKeepAlive(true);
			in = mSocket.getInputStream();
			out = mSocket.getOutputStream();

			// 使用循环的方式，不停的与客户端交互会话
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (mSocket != null && mSocket.isConnected()) {
					// 发送数据回客户端
					doSend(out);
					// 处理客户端发来的数据
					doReceive(in);
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (mSocket != null) {
					mSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void doReceive(InputStream in) {
		String msg = null;
		try {
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			msg = new String(bytes);
			if (!TextUtils.isEmpty(msg.trim())) {
				TextMsgEntity entity = new TextMsgEntity();
				entity.msgContent = msg;
				EventBus.getDefault().post(entity);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doSend(OutputStream out) {
		TextMsgEntity entity = (TextMsgEntity) EventBus.getDefault()
				.getStickyEvent(TextMsgEntity.class);
		if (entity == null || TextUtils.isEmpty(entity.msgContent)) {
			return;
		}
		if (latestEntity == entity) {
			return;
		}
		latestEntity = entity;
		Log.i(TAG, "send to client: " + latestEntity.msgContent);
		try {
			out.write(latestEntity.msgContent.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
