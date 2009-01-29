package com.redfin.sitemapgenerator;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import com.redfin.sitemapgenerator.GoogleMobileSitemapGenerator;
import com.redfin.sitemapgenerator.GoogleMobileSitemapUrl;

public class GoogleMobileSitemapUrlTest extends TestCase {
	
	File dir;
	GoogleMobileSitemapGenerator wsg;
	
	public void setUp() throws Exception {
		dir = File.createTempFile(GoogleMobileSitemapUrlTest.class.getSimpleName(), "");
		dir.delete();
		dir.mkdir();
		dir.deleteOnExit();
	}
	
	public void tearDown() {
		wsg = null;
		for (File file : dir.listFiles()) {
			file.deleteOnExit();
			file.delete();
		}
		dir.delete();
		dir = null;
	}
	
	public void testSimpleUrl() throws Exception {
		wsg = new GoogleMobileSitemapGenerator("http://www.example.com", dir);
		GoogleMobileSitemapUrl url = new GoogleMobileSitemapUrl("http://www.example.com/index.html");
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" " +
			"xmlns:mobile=\"http://www.google.com/schemas/sitemap-mobile/1.0\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" +
			"    <mobile:mobile/>\n" +
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	private String writeSingleSiteMap(GoogleMobileSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
