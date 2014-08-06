package com.redfin.sitemapgenerator;

public class GoogleImagesSitemapImage {

	private final String loc;
	private final String caption;
	private final String geoLocation;
	private final String title;
	private final String license;
	
	public GoogleImagesSitemapImage(String loc, String caption,
			String geoLocation, String title, String license) {
		this.loc = loc;
		this.caption = caption;
		this.geoLocation = geoLocation;
		this.title = title;
		this.license = license;
	}

	public String getLoc() {
		return loc;
	}

	public String getCaption() {
		return caption;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public String getTitle() {
		return title;
	}

	public String getLicense() {
		return license;
	}	
}
