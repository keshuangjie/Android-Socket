package com.jimmy.im.client.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.jimmy.im.client.config.Config;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:40:29
 * @package com.jimmy.im.client.socket
 * @version 1.0
 * 连接管理类
 */
public class SocketManager{

	private static final SocketManager sINSTANCE = new SocketManager();

	private SocketManager() {};

	public static SocketManager getInstance() {
		return sINSTANCE;
	}
	
	/**
	 * 启动一个socket长连接
	 * @param key
	 */
	public void startSocket(String key){
		new Launcher().start();
	}
	
	@Deprecated
	public void startTextHandlerSocket(){
		new TextHandlerSocket().start();
	}

	@Deprecated
	public void startFileSendSocket() {
		new FileHandlerSocket(FileHandlerSocket.TYPE_SEND).start();
	}
	
	@Deprecated
	public void startFileReceivelerSocket() {
		new FileHandlerSocket(FileHandlerSocket.TYPE_RECEIVE).start();
	}
	
	class Launcher extends Thread{
		
		public Launcher(){
		}
		
		@Override
		public void run() {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(Config.CONNET_IP,
						Config.CONNET_PORT), 5000);
				socket.setKeepAlive(true);
				
				new MsgReceiveHandler(socket).start();
				new MsgSendHandler(socket).start();
				
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
	}

}
