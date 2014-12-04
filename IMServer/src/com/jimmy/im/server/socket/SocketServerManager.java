package com.jimmy.im.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

import com.jimmy.im.server.util.ToastUtil;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:46:04
 * @package com.jimmy.im.server.socket
 * @version 1.0
 * socket连接处理
 */
public class SocketServerManager implements Runnable {
	private static final String TAG = SocketServerManager.class.getSimpleName();

	public static final int CONNET_PORT = 5013;
	
	ServerSocket serverSocket;

	private static final SocketServerManager sINSTANCE = new SocketServerManager();

	private SocketServerManager() {
	};

	public static SocketServerManager getInstance() {
		return sINSTANCE;
	}

	public void startConnect() {
		new Thread(this).start();
	}

	public void run() {
		try {
			Log.i(TAG, "1.创建ServerSocket");
			serverSocket = new ServerSocket(CONNET_PORT);
			
			int num = 0;//socket connect num

			while (true) {
				Socket client = serverSocket.accept();
				num++;
				log("run() -> craete client success "  + num);
				ToastUtil.getInstance().toast("client connet " + num);
				
				new MsgReceiveHandler(client).start();
				new MsgSendHandler(client).start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			close();
		}
	}
	
	public void close() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void log(String message) {
		Log.i(TAG, message);
	}

}
