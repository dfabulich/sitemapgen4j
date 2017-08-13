package com.redfin.sitemapgenerator;

import java.net.URL;
import java.util.Date;

public interface ISitemapUrl {

	public abstract Date getLastMod();

	public abstract URL getUrl();

}