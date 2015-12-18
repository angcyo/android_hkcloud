package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/***
 * @desc：单个商品SKU的属性单元；如：尺码，颜色分类
 * @author: samy (hongfeiliuxing@gmail.com)
 * @date: 2015年5月27日 下午7:52:17
 */
public class SkuPropertyUnit implements Serializable {
  /***/
  private static final long serialVersionUID = 7298076100631999265L;
  /** 属性ID */
  public String attributeId;
  /** 属性名称 */
  public String attributeName;
  /** 图片或文字 true图片，false文字 */
  //此SKU属性是否关联了商品图片的变更
  public boolean isImage;
  /** 子单元的属性 */
  public List<SkuPropertyValueUnit> values = new ArrayList<SkuPropertyValueUnit>();

  @Override public String toString() {
    return "SkuPropertyUnit [attributeId=" + attributeId + ", attributeName=" + attributeName
            + ", isImage=" + isImage + ", values=" + values + "]";
  }
}
