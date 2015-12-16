package com.huika.cloud.support.event;

import java.io.Serializable;

public class MainPagerChangeEvent implements Serializable{
	  private static final long serialVersionUID = -5582728308345869073L;
	  public int changePageNo;

	  public MainPagerChangeEvent(int changePageNo) {
	    this.changePageNo = changePageNo;
	  }
}
