package com.huika.cloud.support.model;

import java.io.Serializable;

/**
 * @author zc
 * @description：SKU类中的value实体
 * @date 2015年7月20日 下午4:24:06
 */
public class SKUItemValueBean implements Serializable {

  /**
   * @description：
   * @author zc
   * @date 2015年7月20日 下午4:24:02
   */
  private static final long serialVersionUID = -6177872790316037101L;
  private String valueStr;// 展示文字 白色、黑色
  private String valueId;// 属性值ID,
  private String imgUrl;// 展示图片地址

  public SKUItemValueBean(String valueStr, String valueId, String imgUrl) {
    super();
    this.valueStr = valueStr;
    this.valueId = valueId;
    this.imgUrl = imgUrl;
  }

  public String getValueStr() {
    return valueStr;
  }

  public void setValueStr(String valueStr) {
    this.valueStr = valueStr;
  }

  public String getValueId() {
    return valueId;
  }

  public void setValueId(String valueId) {
    this.valueId = valueId;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  @Override public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((imgUrl == null) ? 0 : imgUrl.hashCode());
    result = prime * result + ((valueId == null) ? 0 : valueId.hashCode());
    result = prime * result + ((valueStr == null) ? 0 : valueStr.hashCode());
    return result;
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    SKUItemValueBean other = (SKUItemValueBean) obj;
    if (imgUrl == null) {
      if (other.imgUrl != null) return false;
    } else if (!imgUrl.equals(other.imgUrl)) return false;
    if (valueId == null) {
      if (other.valueId != null) return false;
    } else if (!valueId.equals(other.valueId)) return false;
    if (valueStr == null) {
      if (other.valueStr != null) return false;
    } else if (!valueStr.equals(other.valueStr)) return false;
    return true;
  }
}
