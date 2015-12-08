package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import junit.framework.TestCase;

public class SitemapGeneratorTest extends TestCase {
	
	
	private static final String SITEMAP_PLUS_ONE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/just-one-more</loc>\n" + 
		"  </url>\n" + 
		"</urlset>";
	private static final String SITEMAP1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/0</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/1</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/2</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/3</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/4</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/5</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/6</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/7</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/8</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/9</loc>\n" + 
		"  </url>\n" + 
		"</urlset>";
	private static final String SITEMAP2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/10</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/11</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/12</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/13</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/14</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/15</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/16</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/17</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/18</loc>\n" + 
		"  </url>\n" + 
		"  <url>\n" + 
		"    <loc>http://www.example.com/19</loc>\n" + 
		"  </url>\n" + 
		"</urlset>";
	File dir;
	WebSitemapGenerator wsg;
	
	public void setUp() throws Exception {
		dir = File.createTempFile(SitemapGeneratorTest.class.getSimpleName(), "");
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
		wsg = new WebSitemapGenerator("http://www.example.com", dir);
		wsg.addUrl("http://www.example.com/index.html");
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testTwoUrl() throws Exception {
		wsg = new WebSitemapGenerator("http://www.example.com", dir);
		wsg.addUrls("http://www.example.com/index.html", "http://www.example.com/index2.html");
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" + 
			"  </url>\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index2.html</loc>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testAllUrlOptions() throws Exception {
		W3CDateFormat df = new W3CDateFormat();
		df.setTimeZone(W3CDateFormat.ZULU);
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).dateFormat(df).autoValidate(true).build();
		WebSitemapUrl url = new WebSitemapUrl.Options("http://www.example.com/index.html")
			.changeFreq(ChangeFreq.DAILY).lastMod(new Date(0)).priority(1.0).build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"    <changefreq>daily</changefreq>\n" + 
			"    <priority>1.0</priority>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testBadUrl() throws Exception {
		wsg = new WebSitemapGenerator("http://www.example.com", dir);
		try {
			wsg.addUrl("http://example.com/index.html");
			fail("wrong domain allowed to be added");
		} catch (RuntimeException e) {}
	}

	public void testSameDomainDifferentSchemeOK() throws Exception {
		wsg = new WebSitemapGenerator("http://www.example.com", dir);
			
		wsg.addUrl("https://www.example.com/index.html");
		
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" + 
				"  <url>\n" + 
				"    <loc>https://www.example.com/index.html</loc>\n" + 
				"  </url>\n" + 
				"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);		
	}
	
	public void testDoubleWrite() throws Exception {
		testSimpleUrl();
		try {
			wsg.write();
			fail("Double-write is not allowed");
		} catch (RuntimeException e) {}
	}
	
	public void testEmptyWrite() throws Exception {
		try {
			wsg = new WebSitemapGenerator("http://www.example.com", dir);
			wsg.write();
			fail("Empty write is not allowed");
		} catch (RuntimeException e) {}
	}

	public void testSuffixPresent() throws MalformedURLException {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).suffixStringPattern("01").build();
        wsg.addUrl("http://www.example.com/url1");
        wsg.addUrl("http://www.example.com/url2");
		List<File> files = wsg.write();
		assertEquals("Sitemap has a suffix now", "sitemap01.xml", files.get(0).getName());
	}

    public void testNullSuffixPassed() throws MalformedURLException {
        wsg = WebSitemapGenerator.builder("http://www.example.com", dir).suffixStringPattern("").build();
        wsg.addUrl("http://www.example.com/url1");
        wsg.addUrl("http://www.example.com/url2");
        List<File> files = wsg.write();
        assertEquals("Sitemap has a suffix now", "sitemap.xml", files.get(0).getName());
    }

	public void testTooManyUrls() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).allowMultipleSitemaps(false).build();
		for (int i = 0; i < SitemapGenerator.MAX_URLS_PER_SITEMAP; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		try {
			wsg.addUrl("http://www.example.com/just-one-more");
			fail("too many URLs allowed");
		} catch (RuntimeException e) {}
	}
	
	public void testMaxUrlsPlusOne() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).autoValidate(true).maxUrls(10).build();
		for (int i = 0; i < 9; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		wsg.addUrl("http://www.example.com/9");
		wsg.addUrl("http://www.example.com/just-one-more");
		String actual = TestUtil.slurpFileAndDelete(new File(dir, "sitemap1.xml"));
		assertEquals("sitemap1 didn't match", SITEMAP1, actual);
		List<File> files = wsg.write();
		assertEquals(2, files.size());
		assertEquals("First sitemap was misnamed", "sitemap1.xml", files.get(0).getName());
		assertEquals("Second sitemap was misnamed", "sitemap2.xml", files.get(1).getName());
		actual = TestUtil.slurpFileAndDelete(files.get(1));
		assertEquals("sitemap2 didn't match", SITEMAP_PLUS_ONE, actual);
	}
	
	public void testMaxUrls() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).autoValidate(true).maxUrls(10).build();
		for (int i = 0; i < 9; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		wsg.addUrl("http://www.example.com/9");
		String actual = writeSingleSiteMap(wsg);
		assertEquals("sitemap didn't match", SITEMAP1, actual);
	}
	
	public void testMaxUrlsTimesTwo() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).autoValidate(true).maxUrls(10).build();
		for (int i = 0; i < 19; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		wsg.addUrl("http://www.example.com/19");
		List<File> files = wsg.write();
		
		assertEquals(2, files.size());
		assertEquals("First sitemap was misnamed", "sitemap1.xml", files.get(0).getName());
		assertEquals("Second sitemap was misnamed", "sitemap2.xml", files.get(1).getName());
		
		String actual = TestUtil.slurpFileAndDelete(files.get(0));
		assertEquals("sitemap1 didn't match", SITEMAP1, actual);
		
		actual = TestUtil.slurpFileAndDelete(files.get(1));
		assertEquals("sitemap2 didn't match", SITEMAP2, actual);
	}
	
	public void testMaxUrlsTimesTwoPlusOne() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).autoValidate(true).maxUrls(10).build();
		for (int i = 0; i < 19; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		wsg.addUrl("http://www.example.com/19");
		wsg.addUrl("http://www.example.com/just-one-more");
		List<File> files = wsg.write();
		
		assertEquals(3, files.size());
		assertEquals("First sitemap was misnamed", "sitemap1.xml", files.get(0).getName());
		assertEquals("Second sitemap was misnamed", "sitemap2.xml", files.get(1).getName());
		assertEquals("Third sitemap was misnamed", "sitemap3.xml", files.get(2).getName());
		
		String expected = SITEMAP1;
		String actual = TestUtil.slurpFileAndDelete(files.get(0));
		assertEquals("sitemap1 didn't match", expected, actual);
		
		expected = SITEMAP2;
		actual = TestUtil.slurpFileAndDelete(files.get(1));
		assertEquals("sitemap2 didn't match", expected, actual);
		
		expected = SITEMAP_PLUS_ONE;
		actual = TestUtil.slurpFileAndDelete(files.get(2));
		assertEquals("sitemap3 didn't match", expected, actual);
	}
	
	public void testGzip() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir)
			.gzip(true).build();
		for (int i = 0; i < 9; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		wsg.addUrl("http://www.example.com/9");
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml.gz", files.get(0).getName());
		File file = files.get(0);
		file.deleteOnExit();
		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream fileStream = new FileInputStream(file);
			GZIPInputStream gzipStream = new GZIPInputStream(fileStream);
			InputStreamReader reader = new InputStreamReader(gzipStream);
			int c;
			while ((c = reader.read()) != -1) {
				sb.append((char)c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		file.delete();
		String actual = sb.toString();
		assertEquals("sitemap didn't match", SITEMAP1, actual);
	}
	
	public void testBaseDirIsNullThrowsNullPointerException() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", null).autoValidate(true).maxUrls(10).build();
		wsg.addUrl("http://www.example.com/index.html");
		Exception e = null;
		try {
			wsg.write();
		} catch (Exception ex) {
			e = ex;
		}
		assertTrue(e instanceof NullPointerException);
		assertEquals("Correct exception was not thrown", e.getMessage(), "To write to files, baseDir must not be null");
	}
	
	public void testWriteAsStringsMoreThanOneString() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", null).autoValidate(true).maxUrls(10).build();
		for (int i = 0; i < 9; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		wsg.addUrl("http://www.example.com/9");
		wsg.addUrl("http://www.example.com/just-one-more");
		List<String> siteMapsAsStrings = wsg.writeAsStrings();
		assertEquals("First string didn't match", SITEMAP1, siteMapsAsStrings.get(0));
		assertEquals("Second string didn't match", SITEMAP_PLUS_ONE, siteMapsAsStrings.get(1));
	}

	public void testWriteEmptySitemap() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).allowEmptySitemap(true).build();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" >\n" +
				"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}

	public void testMaxUrlsAllowingEmptyDoesNotWriteExtraSitemap() throws Exception {
		wsg = WebSitemapGenerator.builder("http://www.example.com", dir).allowEmptySitemap(true).maxUrls(10).build();
		for (int i = 0; i < 10; i++) {
			wsg.addUrl("http://www.example.com/"+i);
		}
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(SITEMAP1, sitemap);
	}
	
	private String writeSingleSiteMap(WebSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
