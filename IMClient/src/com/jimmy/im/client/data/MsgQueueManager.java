package com.jimmy.im.client.data;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author keshuangjie
 * @date 2014-12-4 下午2:02:03
 * @package com.jimmy.im.client.data
 * @version 1.0
 * 待发送消息队列
 */
public class MsgQueueManager {
	
	LinkedBlockingQueue<MsgEntity> mQueueList;
	
	private static final class Holder{
		public static final MsgQueueManager sINSTANCE = new MsgQueueManager();
	}
	
	private MsgQueueManager(){
		mQueueList = new LinkedBlockingQueue<MsgEntity>();
	}
	
	public static MsgQueueManager getInstance(){
		return Holder.sINSTANCE;
	}
	
	public MsgEntity poll(){
		MsgEntity entity = null;
		if(mQueueList != null){
			entity =  mQueueList.poll();
		}
		return entity;
	}
	
	public void push(MsgEntity entity){
		try {
			if(mQueueList != null){
				mQueueList = new LinkedBlockingQueue<MsgEntity>();
			}
			mQueueList.put(entity);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
