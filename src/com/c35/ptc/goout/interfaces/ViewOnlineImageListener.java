package com.c35.ptc.goout.interfaces;

public interface ViewOnlineImageListener {

	public void OnImageLoadStart(String imageUrl);
	public void OnImageLoadSuccess(String imageUrl);
	public void OnImageLoadFail(String imageUrl);
	
}
