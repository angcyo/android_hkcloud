package cn.sharesdk.onekeyshare.utils;

import java.util.HashMap;

import com.hkcloud.share.R;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class OneKeyShareCallback implements PlatformActionListener {
  private Handler mHandler;
  private Context mContext;

  public OneKeyShareCallback(Context context, Handler mHandler) {
    super();
    this.mHandler = mHandler;
  }

  public OneKeyShareCallback(Context context) {
    super();
    mContext = context;
    mHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
        switch (msg.what) {
          case OnekeyShareTool.SHARE_SUCCESS:
            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_completed), 0)
                .show();
            Intent i = new Intent(OnekeyShareTool.SHARE_SUCCESS_BOAST_STR);
            mContext.sendBroadcast(i);
            break;
          case OnekeyShareTool.SHARE_FAIL:
            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_failed), 0)
                .show();
            break;
          case OnekeyShareTool.SHARE_CANCEL:
            Toast.makeText(mContext, mContext.getResources().getString(R.string.share_cancel), 0)
                .show();
            break;
        }
      }
    };
  }

  public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
    Message m = mHandler.obtainMessage();
    m.what = OnekeyShareTool.SHARE_SUCCESS;
    mHandler.sendMessage(m);
  }

  public void onError(Platform platform, int action, Throwable t) {
    Message msg = mHandler.obtainMessage();
    msg.what = OnekeyShareTool.SHARE_FAIL;
    msg.obj = t;
    msg.arg1 = action;
    mHandler.sendMessage(msg);
  }

  public void onCancel(Platform platform, int action) {
    Message m = mHandler.obtainMessage();
    m.what = OnekeyShareTool.SHARE_CANCEL;
    mHandler.sendMessage(m);
  }
}