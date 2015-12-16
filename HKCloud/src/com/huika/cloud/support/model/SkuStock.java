package com.huika.cloud.support.model;

import java.io.Serializable;

/**
 * @dec:sku库存价格
 * @author: samy(hongfeiliuxing@gmail.com)
 * @datetime: 2015/5/26 18:19
 */
public class SkuStock implements Serializable {
  /***/
  private static final long serialVersionUID = 6041274266574137599L;

  /**
   * SKU编码000238570001
   */
  public String sku;

  /**
   * 库存
   */
  public int stock;

  /**
   * 价格
   */
  public float price;

  @Override public String toString() {
    return "SkuStock [sku=" + sku + ", stock=" + stock + ", price=" + price + "]";
  }
}