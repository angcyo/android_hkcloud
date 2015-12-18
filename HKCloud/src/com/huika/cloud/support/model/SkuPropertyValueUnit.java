package com.huika.cloud.support.model;

import java.io.Serializable;

/**
 * @author samy
 * @description：最详细的SKUItem中属性；
 * @date 2015年6月2日 下午3:48:02
 */
public class SkuPropertyValueUnit implements Serializable {
  /***/
  private static final long serialVersionUID = 681269121496456365L;
  /** 展示文字  白色、黑色 */
  public String valueStr;
  /** 属性值ID */
  public String valueId;
  /** 展示图片地址 */
  public String imgUrl;
  /** SKU编码,得手动解析成数组 */
//  public String sku; // 4025: 4028"//sku组合。4025 指attributeId ", sku=" + sku +

  @Override public String toString() {
    return "SkuPropertyValueUnit [valueStr=" + valueStr + ", valueId=" + valueId + ", imgUrl="
            + imgUrl + "]";
  }
}