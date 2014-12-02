package com.jimmy.im.client.socket;

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
	
	public void startTextHandlerSocket(){
		new TextHandlerSocket().start();
	}

	public void startFileSendSocket() {
		new FileHandlerSocket(FileHandlerSocket.TYPE_SEND).start();
	}
	
	public void startFileReceivelerSocket() {
		new FileHandlerSocket(FileHandlerSocket.TYPE_RECEIVE).start();
	}

}
