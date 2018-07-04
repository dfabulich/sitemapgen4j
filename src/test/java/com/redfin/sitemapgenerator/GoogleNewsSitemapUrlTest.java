package com.redfin.sitemapgenerator;

import com.redfin.sitemapgenerator.W3CDateFormat.Pattern;
import junit.framework.TestCase;

import java.io.File;
import java.util.Date;
import java.util.List;

public class GoogleNewsSitemapUrlTest extends TestCase {
	
	File dir;
	GoogleNewsSitemapGenerator wsg;
	
	public void setUp() throws Exception {
		dir = File.createTempFile(GoogleNewsSitemapUrlTest.class.getSimpleName(), "");
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
		W3CDateFormat dateFormat = new W3CDateFormat(Pattern.SECOND);
		dateFormat.setTimeZone(W3CDateFormat.ZULU);
		wsg = GoogleNewsSitemapGenerator.builder("http://www.example.com", dir)
			.dateFormat(dateFormat).build();
		GoogleNewsSitemapUrl url = new GoogleNewsSitemapUrl("http://www.example.com/index.html", new Date(0), "Example Title", "The Example Times", "en");
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" +
				"    <news:news>\n" +
				"      <news:publication>\n" +
				"        <news:name>The Example Times</news:name>\n" +
				"        <news:language>en</news:language>\n" +
				"      </news:publication>\n" +
				"      <news:publication_date>1970-01-01T00:00:00Z</news:publication_date>\n" +
				"      <news:title>Example Title</news:title>\n" +
			"    </news:news>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testKeywords() throws Exception {
		W3CDateFormat dateFormat = new W3CDateFormat(Pattern.SECOND);
		dateFormat.setTimeZone(W3CDateFormat.ZULU);
		wsg = GoogleNewsSitemapGenerator.builder("http://www.example.com", dir)
			.dateFormat(dateFormat).build();
		GoogleNewsSitemapUrl url = new GoogleNewsSitemapUrl.Options("http://www.example.com/index.html", new Date(0), "Example Title", "The Example Times", "en")
			.keywords("Klaatu", "Barrata", "Nicto")
			.build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" +
				"    <news:news>\n" +
				"      <news:publication>\n" +
				"        <news:name>The Example Times</news:name>\n" +
				"        <news:language>en</news:language>\n" +
				"      </news:publication>\n" +
				"      <news:publication_date>1970-01-01T00:00:00Z</news:publication_date>\n" +
				"      <news:title>Example Title</news:title>\n" +
			"      <news:keywords>Klaatu, Barrata, Nicto</news:keywords>\n" +
			"    </news:news>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}

	public void testGenres() throws Exception {
		W3CDateFormat dateFormat = new W3CDateFormat(Pattern.SECOND);
		dateFormat.setTimeZone(W3CDateFormat.ZULU);
		wsg = GoogleNewsSitemapGenerator.builder("http://www.example.com", dir)
				.dateFormat(dateFormat).build();
		GoogleNewsSitemapUrl url = new GoogleNewsSitemapUrl.Options("http://www.example.com/index.html", new Date(0), "Example Title", "The Example Times", "en")
				.genres("persbericht")
				.build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" >\n" +
				"  <url>\n" +
				"    <loc>http://www.example.com/index.html</loc>\n" +
				"    <news:news>\n" +
				"      <news:publication>\n" +
				"        <news:name>The Example Times</news:name>\n" +
				"        <news:language>en</news:language>\n" +
				"      </news:publication>\n" +
				"      <news:genres>persbericht</news:genres>\n" +
				"      <news:publication_date>1970-01-01T00:00:00Z</news:publication_date>\n" +
				"      <news:title>Example Title</news:title>\n" +
				"    </news:news>\n" +
				"  </url>\n" +
				"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	private String writeSingleSiteMap(GoogleNewsSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
