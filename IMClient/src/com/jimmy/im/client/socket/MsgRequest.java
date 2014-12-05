package com.jimmy.im.client.socket;

/**
 * @author keshuangjie
 * @date 2014-12-4 下午7:41:14
 * @package com.jimmy.im.client.socket
 * @version 1.0
 * 消息发送请求
 */
public class MsgRequest {
	
	private MsgParam mParam;
	private SendCallback mSendCallBack;
	
	public MsgRequest(MsgParam param, SendCallback callBack){
		this.mParam = param;
		this.mSendCallBack = callBack;
	}
	
	public SendCallback getSendCallBack() {
		return mSendCallBack;
	}

	public void setSendCallBack(SendCallback mSendCallBack) {
		this.mSendCallBack = mSendCallBack;
	}

	public void setMsgParam(MsgParam param){
		this.mParam = param;
	}
	
	public MsgParam getMsgParam(){
		return mParam;
	}
	
	public interface SendCallback{
		void onFinish();
		
		void onError();
	}

}
