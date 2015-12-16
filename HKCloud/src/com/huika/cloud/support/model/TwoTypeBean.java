package com.huika.cloud.support.model;

import java.util.List;


public class TwoTypeBean {
	public String categoryName ;
	public String categoryId ;
	public String categoryImage ;
	public List<ThreeTypeBean> children;
	
	@Override
	public String toString() {
		return "TwoTypeBean [categoryName=" + categoryName + ", categoryId=" + categoryId + ", categoryImage=" + categoryImage + ", children=" + children + "]";
	}
	
}
