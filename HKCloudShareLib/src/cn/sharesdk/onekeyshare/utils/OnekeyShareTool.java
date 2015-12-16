package cn.sharesdk.onekeyshare.utils;

import java.util.Random;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class OnekeyShareTool {
  public static final int SHARE_SUCCESS = 1;
  public static final int SHARE_FAIL = 2;
  public static final int SHARE_CANCEL = 3;
  public static final String SHARE_SUCCESS_BOAST_STR = "cn.sharesdk.onekeyshare.utils.sharesuccess";

  /**
   * @param title 标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
   * @param titleUrl 标题的链接
   * @param shareWord 分享的内容
   * @param shareWorldUrlQ 分享的内容的链接，仅在 qq中使用
   * @param imageUrl 分享的图片链接
   * @param webSiteNameQ 网站名称
   * @param commentContent 评论，没有评论就不填
   * @param silent 是否安静的去分享  一般填true
   * @param platform 平台名称 qq还是qqzone还是微信等等
   * @param mHandler 处理的Handler，一般填null
   * @parem titleWXMoments 因为微信朋友圈分享的文字只显示 title,shareWord不会显示，导致文本内容太少。建议 titleWXMoments =
   * shareWord;
   * @description：
   * @author zhangjianlin (990996641)
   * @date 2015年6月13日 上午10:52:51
   */
  public static void initAndShowShare(Context context, String title, String titleWXMoments,
      String titleUrl, String shareWord, String shareWorldUrlQ, String imageUrl,
      String webSiteNameQ, String commentContent, boolean silent, String platform,
      Handler mHandler) {
    OnekeyShare oks = new OnekeyShare();
    if (!WechatMoments.NAME.equals(platform)) {
      oks.setTitle(title);
    } else {
      oks.setTitle(titleWXMoments);
    }
    /** sina微博  只支持140个汉字，要自己处理*/
    if (SinaWeibo.NAME.equals(platform)) {
      if (shareWord.length() > 135) {
        shareWord = shareWord.substring(0, 135);
      }
    }

    if (Wechat.NAME.equals(platform) || WechatMoments.NAME.equals(platform)) {
      oks.setText(shareWord); // context.getString(R.string.share_content_huixin_navigation)
    } else if (SinaWeibo.NAME.equals(platform)) {
      /** sina微博  只支持140个汉字，要自己处理*/
      if (shareWorldUrlQ.length() < 120) {//防止连接过长
        if (shareWord.length() > (135 - shareWorldUrlQ.length())) {
          shareWord = shareWord.substring(0, 135 - shareWorldUrlQ.length());
        }
        oks.setText(shareWord
            + shareWorldUrlQ); // context.getString(R.string.share_content_huixin_navigation)
      } else {
        oks.setText(shareWord);
      }
    } else {
      if (shareWorldUrlQ.equals(shareWord)) {
        oks.setText(shareWorldUrlQ);
      } else {
        oks.setText(
            shareWord);//oks.setText(shareWord + "\n" + shareWorldUrlQ); // context.getString(R.string.share_content_huixin_navigation)
      }
    }

    oks.setTitleUrl(titleUrl);
    // siteUrl是分享此文本的网站地址，仅在QQ空间使用
    oks.setSiteUrl(shareWorldUrlQ);
    oks.setUrl(shareWorldUrlQ);
    // site是分享此内容的网站名称，仅在QQ空间使用
    oks.setSite(webSiteNameQ);

    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    // oks.setImagePath(imageUrl);//setImagePath必需是本地图片
    // url仅在微信（包括好友和朋友圈）中使用
    if (QZone.NAME.equals(platform)) {
      oks.setImageUrl(
          imageUrl + "?" + System.currentTimeMillis() + new Random().nextInt(1000000) % 1000
              + 1); // NET_IMAGE_URI
    } else {
      oks.setImageUrl(imageUrl);
    }
    if (!TextUtils.isEmpty(commentContent)) {
      oks.setComment(commentContent);
    }
    if (SinaWeibo.NAME.equals(platform)) {
      oks.setSilent(false);
    } else {
      oks.setSilent(silent);
    }

    if (platform != null) {
      oks.setPlatform(platform);
    }
    if (null != mHandler) {
      oks.setCallback(new OneKeyShareCallback(context, mHandler));
    } else {
      oks.setCallback(new OneKeyShareCallback(context));
    }
    oks.setShareContentCustomizeCallback(new ShareContentCustomize(context));
    // 启动分享GUI
    // 关闭sso授权
    oks.disableSSOWhenAuthorize();
    oks.show(context);
  }

  public static String getTimeMill() {
    return "?" + System.currentTimeMillis() + new Random().nextInt(1000000) % 1000 + 1;
  }
}
