package com.colibri.toread.userdata;

import com.colibri.toread.ToReadBaseEntity;

public class BookStatus extends ToReadBaseEntity{
	private boolean isRead;
	
	//New unread book status
	public BookStatus() {
		markAsUnread();
	}
	
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
