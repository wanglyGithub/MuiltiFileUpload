package com.zhy.http.okhttp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by wangly on 2016/7/22.
 *  添加ProgressDialog
 */
public class ProgressDialogHandler extends Handler{
	public static final int SHOW_PROGRESS_DIALOG = 1;
	public static final int DISMISS_PROGRESS_DIALOG = 2;

	private ProgressDialog dialog;

	private Context context;
	private boolean cancelable;

	public ProgressDialogHandler(Context context,boolean cancelable) {
		super();
		this.context = context;
		this.cancelable = cancelable;
	}

	private void initProgressDialog(String msg){
		if (dialog == null) {
			dialog = new ProgressDialog(context);
			dialog.setCancelable(cancelable);
			dialog.setMessage(msg);
			if (!dialog.isShowing()) {
				dialog.show();
			}
		}
	}
	

	private void dismissProgressDialog(){
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case SHOW_PROGRESS_DIALOG:
			initProgressDialog((String)msg.obj);
			break;
		case DISMISS_PROGRESS_DIALOG:
			dismissProgressDialog();
			break;
		}
	}


}
