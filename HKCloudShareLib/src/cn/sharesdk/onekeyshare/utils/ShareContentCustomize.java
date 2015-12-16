package cn.sharesdk.onekeyshare.utils;

import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class ShareContentCustomize implements ShareContentCustomizeCallback {

  private Context mContext;

  public ShareContentCustomize(Context context) {
    this.mContext = context;
  }

  @Override public void onShare(Platform platform, ShareParams paramsToShare) {
    // 改写twitter分享内容中的text字段，否则会超长，
    // 因为twitter会将图片地址当作文本的一部分去计算长度
    //		if ("SinaWeibo".equals(platform.getName()) || "TencentWeibo".equals(platform.getName())) {
    //			String text = mContext.getString(R.string.share_content_huixin_navigation) + mContext.getString(R.string.free_download_address);
    //			paramsToShare.setTitleUrl(OnekeyShareTool.WEBSITE_ADDR_LOADURI);
    //			paramsToShare.setText(text);
    //		}
  }
}
