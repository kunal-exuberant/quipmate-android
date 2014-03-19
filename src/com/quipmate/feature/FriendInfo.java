package com.quipmate.feature;

public class FriendInfo {
	private String name;
	private String imageURL;
	public FriendInfo(String name, String imageURL) {
		super();
		this.name = name;
		this.imageURL = imageURL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	
}
