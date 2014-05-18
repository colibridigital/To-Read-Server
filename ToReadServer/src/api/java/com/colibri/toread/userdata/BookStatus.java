package com.colibri.toread.userdata;

public class BookStatus {
	private boolean isRead;
	
	public boolean getIsRead() {
		return this.isRead;
	}
	
	public void markAsRead() {
		this.isRead = true;
	}
	
	public void markAsUnread() {
		this.isRead = false;
	}	
}
