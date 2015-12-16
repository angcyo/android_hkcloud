package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @dec:4.9.商品详情
 * @author: samy(hongfeiliuxing@gmail.com)
 * @datetime: 2015/5/26 17:27
 */
public class ProductDetailBean implements Serializable {
  /***/
  private static final long serialVersionUID = -702884568125692213L;
  //  商品ID
  public String productId;
  //商品名称
  public String productName;
  //专柜价格
  public double productPrice;
   
  // 佣金
  public double commissionPrice;  
  // 折扣
  public double discount;
  
  // 库存
  public String stock;
  public long salesVolume; // 销量
  public double logistics; // 配送方式 0：包邮其他县市运费
  
  public String introduction; // 商品详情介绍
  public String productparameter; // 商品规格参数
  public String salesservice; // 商品规格参数
 

  // 商品状态(0-正常，1已下架)
  public String statusType;
  // 配送方式给的是字符串多少钱和是否包邮
  public String postage;
  
//评论数目
 public int commentCount;

  //  商品图片列表
  public List<ProductImageArray> productImageArray = new ArrayList<ProductImageArray>();
  
  
  // 商品评论列表
  public List<ProductComment> productCommentArray = new ArrayList<ProductComment>();

  // 是否关注(0-收藏 1-未收藏)
  public String isFavorite;

  // sku属性 如果商品没有SKU码时，skuItems就为null,skuStock数组将只有一组数据
  public List<SkuPropertyUnit> skuItems = new ArrayList<SkuPropertyUnit>();

  // sku库存价格
  public List<SkuStock> skuStock = new ArrayList<SkuStock>();
  
  /** 商品小图 */
  public String productSmallImage;
  public String favourableTip;// 优惠提示语

}