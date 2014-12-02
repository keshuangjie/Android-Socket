package com.jimmy.im.server.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimmy.im.server.R;
import com.jimmy.im.server.data.MsgEntity;
import com.jimmy.im.server.data.TextMsgEntity;
import com.jimmy.im.server.data.VoiceMsgEntity;
import com.jimmy.im.server.media.MediaPlay;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:44:30
 * @package com.jimmy.im.server.adapter
 * @version 1.0
 * 聊天list适配器
 */
public class ChatMsgViewAdapter extends BaseAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private List<MsgEntity> mMsgEntitys;

	private LayoutInflater mInflater;
	
	public ChatMsgViewAdapter(Context context) {
		this.mMsgEntitys = new ArrayList<MsgEntity>();
		mInflater = LayoutInflater.from(context);
	}

	public ChatMsgViewAdapter(Context context, List<MsgEntity> entitys) {
		this.mMsgEntitys = entitys;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return mMsgEntitys.size();
	}

	public Object getItem(int position) {
		return mMsgEntitys.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		MsgEntity entity = mMsgEntitys.get(position);

		if (entity.isSelf) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		return 2;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		final MsgEntity entity = mMsgEntitys.get(position);
		boolean isSelf = entity.isSelf;

		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (isSelf) {
				convertView = mInflater.inflate(
						R.layout.list_item_right, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.list_item_left, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView
					.findViewById(R.id.tv_chatcontent);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.isSelf = isSelf;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.tvSendTime.setText(entity.date);
		
		if (entity instanceof VoiceMsgEntity) {
			VoiceMsgEntity voiceMsgEntity = (VoiceMsgEntity) entity;
			viewHolder.tvContent.setText("");
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chatto_voice_playing, 0);
			viewHolder.tvTime.setText(voiceMsgEntity.time + "\"");
		} else {
			TextMsgEntity textMsgEntity = (TextMsgEntity) entity;
			viewHolder.tvContent.setText(textMsgEntity.msgContent);			
			viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			viewHolder.tvTime.setText("");
		}
		viewHolder.tvContent.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (entity != null && entity instanceof VoiceMsgEntity) {
					MediaPlay.getInstance().startPlay(((VoiceMsgEntity)entity).fileName);
				}
			}
		});
		viewHolder.tvUserName.setText(entity.userName);
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public TextView tvTime;
		public boolean isSelf;
	}

}
