package com.huika.cloud.support.event;

import java.io.Serializable;

public class LoginEvent implements Serializable {
	private static final long serialVersionUID = -5582728308345869073L;
	public boolean isSuceess;
	public String msg;
	public LoginEvent(boolean flag,String message) {
         this.isSuceess = flag;
         this.msg = message;
	}
}
