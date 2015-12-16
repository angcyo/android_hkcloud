1、把ShareSDK.xml复制到主工程的assets目下,更改你工程中的key跟appid
2、复制lib的AndroidManifest.xml内容到主工程的AndroidManifest.xml,更改主工程的AndroidManifest.xml，
<data android:scheme="tencentYOUappId" />
3、用法参考  ShareSimple.java

4、微信平台注意点
微信开放平台上签名用的是 md5 去掉分隔符，再全部转为小写

5、使用了setImagePath导致微信朋友圈分享无图片，setImagePath必需是本地图片
oks.setImagePath(imageUrl);// path 或 imageurI; logo  //setImagePath必需是本地图片

6、腾讯微博的图片必需是外网的图片，内网测试的时候要使用外网图片测试

7、新浪微博的 审核注意点 

一、应用介绍图片：必需有张图片有 体现使用了  "分享新浪微博"
二、应用地址：必需是官网(单单一个下载网页可能会拒审原因:利用app自助工厂或类似网站创建的应用下载页面)，其他什么www.baidu.com无关的地址通不过
三、如果要分享图片；要在应用信息中填入  申请高级写入权限
四、{"error":"applications over the unaudited use restrictions!","error_code":21321,"request":"/2/statuses/update.json"}
是因为 在测试期 也就是 没有 提交审核前 ，新浪验证比较严格 只允许 当前申请APPKEY 用户 测试， 其他用户登录测试都会报这个错误.
所以登录测试时 只能用 你申请APPKEY 的用户. 或者 在申请的应用中添加测试账号，现在一个应用可以添加15个测试账号。当应用完成以后就可以提交审核，然后大家就可以用了。


五、如果您集成了微信或者易信，还需要添加下面两个Activity 
 <activity
 android:name=".wxapi.WXEntryActivity"
 android:theme="@android:style/Theme.Translucent.NoTitleBar"
 android:configChanges="keyboardHidden|orientation|screenSize"
 android:exported="true"
 android:screenOrientation="portrait" /> 
 
<!--易信分享回调 -->
 <activity
 android:name=".yxapi.YXEntryActivity"
 android:theme="@android:style/Theme.Translucent.NoTitleBar"
 android:configChanges="keyboardHidden|orientation|screenSize"
 android:exported="true"
 android:screenOrientation="portrait" />
 
 六、权限
 uses-permission android:name="android.permission.GET_TASKS" />
 <uses-permission android:name="android.permission.INTERNET" />
 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 <uses-permission android:name="android.permission.MANAGE_ACCOUNTS"/>
 <uses-permission android:name="android.permission.GET_ACCOUNTS"/>
 <!-- 蓝牙分享所需的权限 -->
  <uses-permission android:name="android.permission.BLUETOOTH" />
  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
  
  
  您好！惠卡app需要分享图片来给用户展示信息，需要这个权限。感谢在百忙中抽取时间给予审核，谢谢！