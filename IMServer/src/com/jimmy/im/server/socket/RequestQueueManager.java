package com.jimmy.im.server.socket;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author keshuangjie
 * @date 2014-12-4 下午2:02:03
 * @package com.jimmy.im.client.data
 * @version 1.0
 * 待发送消息队列
 */
public class RequestQueueManager {
	
	LinkedBlockingQueue<MsgRequest> mQueueList;
	
	private static final class Holder{
		public static final RequestQueueManager sINSTANCE = new RequestQueueManager();
	}
	
	private RequestQueueManager(){
		mQueueList = new LinkedBlockingQueue<MsgRequest>();
	}
	
	public static RequestQueueManager getInstance(){
		return Holder.sINSTANCE;
	}
	
	public MsgRequest poll(){
		MsgRequest entity = null;
		if(mQueueList != null){ 
			entity =  mQueueList.poll();
		}
		return entity;
	}
	
	public void push(MsgRequest entity){
		try {
			if(mQueueList != null){
				mQueueList = new LinkedBlockingQueue<MsgRequest>();
			}
			mQueueList.put(entity);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
