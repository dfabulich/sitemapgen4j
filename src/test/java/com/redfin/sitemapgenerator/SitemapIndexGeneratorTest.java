package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;

import junit.framework.TestCase;

public class SitemapIndexGeneratorTest extends TestCase {

	private static final String INDEX = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap1.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap2.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap3.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap4.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap5.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap6.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap7.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap8.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap9.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"  <sitemap>\n" + 
			"    <loc>http://www.example.com/sitemap10.xml</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"  </sitemap>\n" + 
			"</sitemapindex>";
	
	private static final String EXAMPLE = "http://www.example.com/";
	private static final W3CDateFormat ZULU = new W3CDateFormat();
	File outFile;
	SitemapIndexGenerator sig;
	public void setUp() throws Exception {
		ZULU.setTimeZone(W3CDateFormat.ZULU);
		outFile = File.createTempFile(SitemapGeneratorTest.class.getSimpleName(), ".xml");
		outFile.deleteOnExit();
	}
	
	public void tearDown() {
		sig = null;
		outFile.delete();
		outFile = null;
	}

	public void testTooManyUrls() throws Exception {
		sig = new SitemapIndexGenerator.Options(EXAMPLE, outFile).maxUrls(10).autoValidate(true).build();
		for (int i = 0; i < 9; i++) {
			sig.addUrl(EXAMPLE+i);
		}
		sig.addUrl(EXAMPLE+"9");
		try {
			sig.addUrl("http://www.example.com/just-one-more");
			fail("too many URLs allowed");
		} catch (RuntimeException e) {}
	}
	public void testNoUrls() throws Exception {
		sig = new SitemapIndexGenerator(EXAMPLE, outFile);
		try {
			sig.write();
			fail("Allowed write with no URLs");
		} catch (RuntimeException e) {}
	}

	public void testNoUrlsEmptyIndexAllowed() throws Exception {
		sig = new SitemapIndexGenerator.Options(EXAMPLE, outFile).allowEmptyIndex(true).build();
		sig.write();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
				"</sitemapindex>";
		String actual = TestUtil.slurpFileAndDelete(outFile);
		assertEquals(expected, actual);
		assertEquals(expected, sig.writeAsString());
	}
	
	public void testMaxUrls() throws Exception {
		sig = new SitemapIndexGenerator.Options(EXAMPLE, outFile).autoValidate(true)
			.maxUrls(10).defaultLastMod(new Date(0)).dateFormat(ZULU).build();
		for (int i = 1; i <= 9; i++) {
			sig.addUrl(EXAMPLE+"sitemap"+i+".xml");
		}
		sig.addUrl(EXAMPLE+"sitemap10.xml");
		sig.write();
		String actual = TestUtil.slurpFileAndDelete(outFile);
		assertEquals(INDEX, actual);
		assertEquals(INDEX, sig.writeAsString());
	}
	
	public void testOneUrl() throws Exception {
		sig = new SitemapIndexGenerator.Options(EXAMPLE, outFile).dateFormat(ZULU).autoValidate(true).build();
		SitemapIndexUrl url = new SitemapIndexUrl(EXAMPLE+"index.html", new Date(0));
		sig.addUrl(url);
		sig.write();
		String actual = TestUtil.slurpFileAndDelete(outFile);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" + 
				"  <sitemap>\n" + 
				"    <loc>http://www.example.com/index.html</loc>\n" + 
				"    <lastmod>1970-01-01</lastmod>\n" + 
				"  </sitemap>\n" + 
				"</sitemapindex>";
		assertEquals(expected, actual);
		assertEquals(expected, sig.writeAsString());
	}
	
	public void testAddByPrefix() throws MalformedURLException {
		sig = new SitemapIndexGenerator.Options(EXAMPLE, outFile).autoValidate(true)
			.defaultLastMod(new Date(0)).dateFormat(ZULU).build();
		sig.addUrls("sitemap", ".xml", 10);
		sig.write();
		String actual = TestUtil.slurpFileAndDelete(outFile);
		assertEquals(INDEX, actual);
		assertEquals(INDEX, sig.writeAsString());
	}
	
}
