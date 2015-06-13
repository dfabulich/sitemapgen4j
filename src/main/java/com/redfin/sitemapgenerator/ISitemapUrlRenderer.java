package com.redfin.sitemapgenerator;

interface ISitemapUrlRenderer<T extends ISitemapUrl> {
	
	public Class<T> getUrlClass();
	public String getXmlNamespaces();
	public void render(T url, StringBuilder sb, W3CDateFormat dateFormat);
}
