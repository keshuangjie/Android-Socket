package com.jimmy.im.client.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.text.TextUtils;
import android.util.Log;

import com.jimmy.im.client.config.Config;
import com.jimmy.im.client.data.TextMsgEntity;

import de.greenrobot.event.EventBus;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:41:03
 * @package com.jimmy.im.client.socket
 * @version 1.0
 * 文本发送接收处理
 */
public class TextHandlerSocket extends Thread {
	private static final String TAG = TextHandlerSocket.class.getSimpleName();

	private Socket mSocket;

	TextMsgEntity entity;

	InputStream in = null;
	OutputStream out = null;

	public TextHandlerSocket() {
	}

	@Override
	public void run() {

		try {
			mSocket = new Socket();
			mSocket.connect(new InetSocketAddress(Config.CONNET_IP,
					Config.CONNET_PORT), 5000);
			Log.i(TAG, "connect server success");
			mSocket.setKeepAlive(true);
			in = mSocket.getInputStream();
			out = mSocket.getOutputStream();

			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Log.i(TAG, "HandlerThread -> run()");
				if (mSocket != null && mSocket.isConnected()) {
					// 使用循环的方式，不停的与客户端交互会话
					// 发送数据回客户端
					doSend(out);
					// 处理客户端发来的数据
					doReceive(in);
				}
			}
		} catch (SocketException e1) {
			Log.i(TAG, "connect server error");
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
		TextMsgEntity entity1 = (TextMsgEntity) EventBus.getDefault()
				.getStickyEvent(TextMsgEntity.class);
		if (entity1 == null || TextUtils.isEmpty(entity1.msgContent)) {
			return;
		}
		if (entity == entity1) {
			return;
		}
		entity = entity1;
		Log.i(TAG, "send to client: " + entity.msgContent);
		try {
			out.write(entity.msgContent.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
