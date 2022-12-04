package com.digitalbooks.user.dto;




public class BookContentResponse {
	
	private String title;
    private String content;
    private boolean isActive;
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public BookContentResponse(String title, String content, boolean isActive) {
		super();
		this.title = title;
		this.content = content;
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public BookContentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "BookContentResponse [title=" + title + ", content=" + content + ", isActive=" + isActive + "]";
	}
    
    
    
    
}