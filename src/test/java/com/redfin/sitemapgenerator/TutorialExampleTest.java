package com.redfin.sitemapgenerator;

import java.io.File;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import com.redfin.sitemapgenerator.W3CDateFormat.Pattern;

public class TutorialExampleTest extends TestCase {
	
	File myDir;
	File myFile;
	
	public void setUp() throws Exception {
		myDir = File.createTempFile(TutorialExampleTest.class.getSimpleName(), "");
		myDir.delete();
		myDir.mkdir();
		myDir.deleteOnExit();
		myFile = new File(myDir, "sitemap_index.xml");
	}
	
	public void tearDown() {
		for (File file : myDir.listFiles()) {
			file.deleteOnExit();
			file.delete();
		}
		myDir.delete();
		myDir = null;
	}
	
	public void testGettingStarted() throws Exception {
		WebSitemapGenerator wsg = new WebSitemapGenerator("http://www.example.com", myDir);
		wsg.addUrl("http://www.example.com/index.html"); // repeat multiple times
		wsg.write();
	}

	public void testConfiguringWsgOptions() throws Exception {
		WebSitemapGenerator wsg = WebSitemapGenerator.builder("http://www.example.com", myDir)
			.gzip(true).build(); // enable gzipped output
		wsg.addUrl("http://www.example.com/index.html");
		wsg.write();
	}
	public void testConfiguringUrlOptions() throws Exception {
		WebSitemapGenerator wsg = new WebSitemapGenerator("http://www.example.com", myDir);
		WebSitemapUrl url = new WebSitemapUrl.Options("http://www.example.com/index.html")
			.lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.HOURLY).build();
		// this will configure the URL with lastmod=now, priority=1.0, changefreq=hourly 
		wsg.addUrl(url);
		wsg.write();
	}
	public void testConfiguringDateFormat() throws Exception {
		W3CDateFormat dateFormat = new W3CDateFormat(Pattern.DAY); // e.g. 2008-01-29
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // Use Greenwich Mean Time timezone
		WebSitemapGenerator wsg = WebSitemapGenerator.builder("http://www.example.com", myDir)
			.dateFormat(dateFormat).build(); // actually use the configured dateFormat
		wsg.addUrl("http://www.example.com/index.html");
		wsg.write();
	}
	public void testLotsOfUrlsWsg() throws Exception {
		WebSitemapGenerator wsg = new WebSitemapGenerator("http://www.example.com", myDir);
		for (int i = 0; i < 60000; i++) wsg.addUrl("http://www.example.com/index.html");
		wsg.write();
		wsg.writeSitemapsWithIndex(); // generate the sitemap_index.xml
	}
	public void testLotsOfUrlsSig() throws Exception {
		WebSitemapGenerator wsg;
		// generate foo sitemap
		wsg = WebSitemapGenerator.builder("http://www.example.com", myDir).fileNamePrefix("foo").build();
		for (int i = 0; i < 5; i++) wsg.addUrl("http://www.example.com/foo"+i+".html");
		wsg.write();
		// generate bar sitemap
		wsg = WebSitemapGenerator.builder("http://www.example.com", myDir).fileNamePrefix("bar").build();
		for (int i = 0; i < 5; i++) wsg.addUrl("http://www.example.com/bar"+i+".html");
		wsg.write();
		// generate sitemap index for foo + bar 
		SitemapIndexGenerator sig = new SitemapIndexGenerator("http://www.example.com", myFile);
		sig.addUrl("http://www.example.com/foo.html");
		sig.addUrl("http://www.example.com/bar.html");
		sig.write();
	}
	public void testAutoValidate() throws Exception {
		WebSitemapGenerator wsg = WebSitemapGenerator.builder("http://www.example.com", myDir)
			.autoValidate(true).build(); // validate the sitemap after writing
		wsg.addUrl("http://www.example.com/index.html");
		wsg.write();
	} 
}
