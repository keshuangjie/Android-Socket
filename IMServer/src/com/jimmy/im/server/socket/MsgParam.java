package com.jimmy.im.server.socket;

import com.jimmy.im.server.data.MsgEntity;

public class MsgParam {
	
	private MsgEntity mMsgEntity;
	
	public void setMsgEntity(MsgEntity entity){
		this.mMsgEntity = entity;
	}
	
	public MsgEntity getMsgEntity(){
		return mMsgEntity;
	}

}
