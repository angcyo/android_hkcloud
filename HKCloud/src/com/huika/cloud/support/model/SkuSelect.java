package com.huika.cloud.support.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @desc：SKU
 * @author: samy (hongfeiliuxing@gmail.com)
 * @date: 2015年5月27日 下午5:23:12
 */
public class SkuSelect implements Serializable {
  // key 为 propId
  private HashMap<Integer, SkuPropertySelect> skuSelectMap;

  public SkuSelect() {
    this.skuSelectMap = new HashMap<Integer, SkuPropertySelect>();
  }

  /**
   * 用户是否选择了所有必须选择的SKU属性
   */
  public Boolean isSelectedAllSkus() {
    Boolean result = false;
    for (Map.Entry entry : skuSelectMap.entrySet()) {
      SkuPropertySelect skuPropertySelect = (SkuPropertySelect) entry.getValue();
      if (!skuPropertySelect.isSelected) {
        return result;
      }
    }
    return true;
  }

  /**
   * 获取没有选择的商品SKU属性名称的连接串
   */
  public String getPropNameString() {
    String result = "请您选择";
    for (Map.Entry entry : skuSelectMap.entrySet()) {
      SkuPropertySelect skuPropertySelect = (SkuPropertySelect) entry.getValue();
      if (!skuPropertySelect.isSelected) {
        result = result + "," + skuPropertySelect.getAttributeName();
      }
    }
    return result;
  }

  /**
   * 获取 用户选择的商品属性的属性名称的连接字符串
   */
  public String getUserSelectSkuPropNameString() {
    String result = "";
    for (Map.Entry entry : skuSelectMap.entrySet()) {
      SkuPropertySelect skuPropertySelect = (SkuPropertySelect) entry.getValue();
      result +=
          skuPropertySelect.getAttributeName() + ": " + skuPropertySelect.getValueStr() + "    ";
    }
    return result;
  }

  public void getAdvanceSeclectSkuId() {
    List<List<String>> allSkuList = new ArrayList<List<String>>();
    List<String> lastSkuIds = new ArrayList<String>();
    for (Map.Entry entry : skuSelectMap.entrySet()) {
      SkuPropertySelect skuPropertySelect = (SkuPropertySelect) entry.getValue();
      lastSkuIds.addAll(Arrays.asList(skuPropertySelect.getSku().split(",")));
      allSkuList.add(lastSkuIds);
    }
  }

  /**
   * @description：选择选中属性的SKU交集
   * @author samy
   * @date 2015年6月4日 下午3:51:02
   */
  public String getSeclectSkuId() {
    List<String> lastSkuIds = new ArrayList<String>();
    for (Map.Entry entry : skuSelectMap.entrySet()) {
      SkuPropertySelect skuPropertySelect = (SkuPropertySelect) entry.getValue();
      if (!skuPropertySelect.isUseSelectMap) {
        continue;
      }
      lastSkuIds.addAll(Arrays.asList(skuPropertySelect.getSku().split(",")));
    }
    for (String value : lastSkuIds) {
      if (getEqualCount(lastSkuIds, value) == skuSelectMap.size()) return value;
    }
    return lastSkuIds.get(0).toString();
  }

  private int getEqualCount(List<String> list, String compareStr) {
    int result = 0;
    for (String value : list) {
      if (value.equals(compareStr)) result++;
    }
    return result;
  }

  public static List findDuplicateWithOrder(List list) {
    Set set = new HashSet();
    List repeatList = new ArrayList();
    for (Iterator iter = list.iterator(); iter.hasNext(); ) {
      Object element = iter.next();
      if (!set.add(element)) {
        repeatList.add(element);
      }
    }
    //		如果Bean，只有一项是。默认的填充一个对象；保持repeatList始终有一个以上数据
    if (repeatList.isEmpty()) {
      repeatList.addAll(set);
    }
    return repeatList;
  }

  /**
   * 先根据 propId 进行排序
   */
  public String getPpath() {
    List<SkuPropertySelect> sortedSkuProperties = new ArrayList<SkuPropertySelect>();
    for (Map.Entry entry : skuSelectMap.entrySet()) {
      SkuPropertySelect skuPropertySelect = (SkuPropertySelect) entry.getValue();
      sortedSkuProperties.add(skuPropertySelect);
    }
    Collections.sort(sortedSkuProperties);
    StringBuilder builder = new StringBuilder();
    for (SkuPropertySelect sku : sortedSkuProperties) {
      builder.append(sku.getAttributeId());
      builder.append(":");
      builder.append(sku.getValueId());
      builder.append(";");
    }
    String result = builder.toString();
    if (result.endsWith(";")) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  public void put(int propId, String propName, boolean isSelect) {
    SkuPropertySelect skuPropertySelect = new SkuPropertySelect();
    skuPropertySelect.setAttributeId(propId);
    skuPropertySelect.setAttributeName(propName);
    skuPropertySelect.setSelected(isSelect);
    this.skuSelectMap.put(propId, skuPropertySelect);
  }

  public void remove(Long propId) {
    this.skuSelectMap.remove(propId.toString());
  }

  /**
   * @description：填充颜色和尺寸中的选择map数据
   * @author samy
   * @date 2015年6月4日 下午5:20:24
   */
  public void setSelectedSkuId(int propId, int valueId, String valueStr, String sku, String imgUrl,
      boolean isUseSelectMap) {
    this.skuSelectMap.get(propId).setValueId(valueId);
    this.skuSelectMap.get(propId).setValueStr(valueStr);
    this.skuSelectMap.get(propId).setSelected(Boolean.TRUE);
    this.skuSelectMap.get(propId).setSku(sku);
    this.skuSelectMap.get(propId).setImgUrl(imgUrl);
    this.skuSelectMap.get(propId).setUseSelectMap(isUseSelectMap);
  }

  /**
   * @author samy
   * @description：当前选择的SKU详细Item
   * @date 2015年6月1日 下午12:09:01
   */
  @SuppressWarnings("serial")
public class SkuPropertySelect implements Comparable<SkuPropertySelect>, Serializable {
    private boolean isSelected;

    private int attributeId;
    private String attributeName;
    private int valueId;
    private String valueStr;
    private String sku;
    private String imgUrl;

    private boolean isUseSelectMap;

    public boolean isUseSelectMap() {
      return isUseSelectMap;
    }

    public void setUseSelectMap(boolean isUseSelectMap) {
      this.isUseSelectMap = isUseSelectMap;
    }

    @Override public int compareTo(SkuPropertySelect skuPropertySelect) {
      return (this.getAttributeId() + "").compareTo(skuPropertySelect.getAttributeId() + "");
    }

    public boolean isSelected() {
      return isSelected;
    }

    public void setSelected(boolean isSelected) {
      this.isSelected = isSelected;
    }

    public int getAttributeId() {
      return attributeId;
    }

    public void setAttributeId(int attributeId) {
      this.attributeId = attributeId;
    }

    public String getAttributeName() {
      return attributeName;
    }

    public void setAttributeName(String attributeName) {
      this.attributeName = attributeName;
    }

    public int getValueId() {
      return valueId;
    }

    public void setValueId(int valueId) {
      this.valueId = valueId;
    }

    public String getValueStr() {
      return valueStr;
    }

    public void setValueStr(String valueStr) {
      this.valueStr = valueStr;
    }

    public String getSku() {
      return sku;
    }

    public void setSku(String sku) {
      this.sku = sku;
    }

    public String getImgUrl() {
      return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
      this.imgUrl = imgUrl;
    }

    @Override public String toString() {
      return "SkuPropertySelect [isSelected=" + isSelected + ", attributeId=" + attributeId
          + ", attributeName=" + attributeName + ", valueId=" + valueId + ", valueStr=" + valueStr
          + ", sku=" + sku + ", imgUrl=" + imgUrl + ", isUseSelectMap=" + isUseSelectMap + "]";
    }
  }

  public HashMap<Integer, SkuPropertySelect> getSkuSelectMap() {
    return skuSelectMap;
  }

  public void setSkuSelectMap(HashMap<Integer, SkuPropertySelect> skuSelectMap) {
    this.skuSelectMap = skuSelectMap;
  }
}
