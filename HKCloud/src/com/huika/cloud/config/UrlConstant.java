package com.huika.cloud.config;

// 接口常量
public class UrlConstant {

	/** 正式版本的环境 */
	public static final String RELEASE_SERVER_DOMAIN = Configuration.WEB_RELEASE_SERVER_DOMAIN;
	/** 预测试环境 */
	public static final String RELEASE_TEST_SERVER_DOMAIN = Configuration.WEB_RELEASE_TEST_SERVER_DOMAIN;
	/** 测试版本的环境 */
	public static final String TEST_SERVER_DOMAIN = Configuration.WEB_TEST_SERVER_DOMAIN;
	/**
	 * 获取订单列表
	 */
	// public static final String API_ORDER_LIST = HOST + "Order/GetOrderList";
	public static final String API_ORDER_LIST = "http://192.168.50.3:8083/hxlmpro-api/" + "Order/GetOrderList";
	public static String HOST = Configuration.BASE_WEB_SERVER + "/";
	/**************************** 个人用户的接口 start*************************/
	// 短信验证码请求1:注册、2：修改支付密码 3:提现 4:找回密码
	public static final String APP_GETSERVER_SMS = HOST + "GetSMSverification";
	// 注册借口
	public static final String USER_REGISTER = HOST + "User/Register";
	// 登录验证接口
	public static final String USER_LOGINVALIDATE = HOST + "User/LoginValidate";
	// 找回密码
	public static final String USER_FINDPASSWORD = HOST + "User/FindPassword";
	// 修改密码
	public static final String USER_CHANGEPASSWORD = HOST + "User/ChangePassword";
	// 实名认证
	public static final String USER_REALNAMEAUTHENTICATION = HOST + "User/RealNameAuthentication";
	/**
	 * 修改昵称
	 */
	public static final String UPDATE_USER_NIKE = HOST + "User/UserUpdate";
	/**
	 * 提现申请
	 */
	public static final String WITH_DRAW_APPLY = HOST + "User/Withdrawal";
	/**************************** 个人用户的接口 end*************************/
	/**
	 * 我的分销订单
	 */
	public static final String GET_DISTRIBUTION_ORDER = HOST+"User/GetMyInvite";
	/**************************** 商品的接口 start*************************/
	// 商品详情
	public static final String PRODUCT_GETPRODUCTDETAILS = HOST + "Product/GetProductDetails";
	// 商品sku详情
	public static final String PRODUCT_GETPRODUCTSKUDETAILS = HOST + "Product/GetProductSkuDetails";
	// 商品评价列表
	public static final String PRODUCT_GETPRODUCTCOMMENTLIST = HOST + "Product/GetProductCommentList";
	/**商品列表*/
	public static final String GET_PRODUCT_LIST = HOST + "Product/GetProductList";
	/**商品筛选*/
	public static final String GET_PRODUCT_FIRST_FILTER = HOST + "Product/GetProductScreenFirst";
	/**************************** 商品的接口 end*************************/
	/**商品分类*/
	public static final String GET_PRODUCT_CATEGORY_LIST = HOST + "Product/GetProductCategoryList";
	/**************************** 订单接口 start*************************/
	// 加入购物车
	public static final String ORDER_INTOCART = HOST + "Order/IntoCart";
	// 购物车列表的
	public static final String ORDER_GETCARTLIST = HOST + "Order/GetCartList";
	// 2.1. 下单中获取商品价格信息（确认商品价格准确性）
	public static final String ORDER_GETPRODUCTPRICEFORORDER = HOST + "Order/GetProductPriceForOrder";
	// 编辑购物车
	public static final String ORDER_EDITCART = HOST + "Order/EditCart";
	// 订单提交
	public static final String ORDER_SUBMITORDER = HOST + "Order/SubmitOrder";
	// 支付接口
	public static final String ORDER_PAYVALIDATE = HOST + "Order/PayValidate";
	/** 获取订单详情 */
	public static final String API_GET_OERDER_DETAIL = HOST + "Order/GetOrderDetails";
	/** 确认收货 */
	public static final String API_AFFIRM_RECEIVING = HOST + "Order/AffirmReceiving";
	/** 修改状态*/
	public static final String API_UPDATE_ORDER_STATUS = HOST + "Order/AffirmReceiving";
	/** 退换货申请*/
	public static final String API_RETURN_OR_CHANGE_APPLY = HOST + "Order/ReturnOrChangeApply";

	/**我的银行卡列表*/
	public static final String USER_GET_MY_CARD_LIST = HOST + "User/GetMyBankCardList";
	/**删除银行卡*/
	public static final String USER_DELETE_CARD = HOST + "User/DeleteBankCard";
	/**新增银行卡*/
	public static final String USER_ADD_CARD = HOST + "User/AddBankCard";
	/**全部银行列表*/
	public static final String USER_ALL_BANK = HOST + "User/GetBankList";
	/**用户收货地址列表*/
	public static final String USER_ADDRESS_LIST = HOST + "User/GetReceiverAddressList";
	/**查询单个收货地址*/
	public static final String USER_QUERY_SIMPLE_ADDRESS = HOST + "User/GetSimpleReceiverAddress";
	/**删除收货地址*/
	public static final String USER_DELETE_ADDRESS = HOST + "User/DeleteReceiverAddress";
	/**新增修改收货地址*/
	public static final String USER_ADD_OR_UPDATE_ADDRESS = HOST + "User/AddOrUpdateReceiverAddress";

	/**************************** 订单接口 end*************************/
	/**获取更新版本信息*********/
	public static final String SUPPORT_GETVERSIONINFO = HOST + "Support/GetVersionInfo";

	/*********************其他接口start******************************/

	/*********************其他接口end******************************/

}
