package com.jimmy.im.client;

import com.jimmy.im.client.config.Config;
import com.jimmy.im.client.socket.SocketManager;
import com.jimmy.im.client.util.ToastUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class SettingActivity extends Activity{
	
	private EditText et_ip;
	private EditText et_port;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting);
		
		et_ip = (EditText) findViewById(R.id.et_ip);
		et_port = (EditText) findViewById(R.id.et_port);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		et_ip.setText(Config.CONNET_IP);
		et_port.setText(Config.CONNET_PORT + "");
	}
	
	public void onclickHandler(View view){
		if(commit()){
			finish();
		}
	}
	
	public boolean commit(){
		String ip = et_ip.getText().toString();
		String temp_port = et_port.getText().toString();
		if(TextUtils.isEmpty(ip)){
			ToastUtil.getInstance().toast("server ip address is null");
			return false;
		}
		
		
		if(TextUtils.isEmpty(temp_port)){
			ToastUtil.getInstance().toast("server port is null");
			return false;
		}
		
		int port;
		
		try {
			port = Integer.parseInt(temp_port);
		} catch (NumberFormatException e) {
			ToastUtil.getInstance().toast("server port must be number");
			return false;
		}
		
		Config.resetConncet(ip, port);
		
		return true;
	}
	
	@Override
	public void onBackPressed() {
		if(commit()){
			super.onBackPressed();
		}
	}

}
