package com.redfin.sitemapgenerator;

import com.redfin.sitemapgenerator.W3CDateFormat.Pattern;
import junit.framework.TestCase;

import java.io.File;
import java.util.Date;
import java.util.List;

public class AlternateSitemapUrlTest extends TestCase {
	
	File dir;
	AlternatesSitemapGenerator wsg;
	
	public void setUp() throws Exception {
		dir = File.createTempFile(AlternateSitemapUrlTest.class.getSimpleName(), "");
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
		wsg = AlternatesSitemapGenerator.builder("http://www.example.be", dir).build();
		AlternatesSitemapUrl url = new AlternatesSitemapUrl.Options("http://www.example.be/index.html").lastMod(new Date()).build();
		url.addAlternate(new AlternatesSitemapUrl.Alternate("nl","http://www.example.nl/index.html"));
		url.addAlternate(new AlternatesSitemapUrl.Alternate("de","http://www.example.de/index.html"));
		url.addAlternate(new AlternatesSitemapUrl.Alternate("be","http://www.example.be/index.html"));
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns=\"http://www.w3.org/1999/xhtml\" >\n" +
				"  <url>\n" +
				"    <loc>http://www.example.be/index.html</loc>\n" +
				"      <xhtml:link rel=\"alternate\" hreflang=\"nl\" href=\"http://www.example.nl/index.html\" />\n" +
				"      <xhtml:link rel=\"alternate\" hreflang=\"de\" href=\"http://www.example.de/index.html\" />\n" +
				"      <xhtml:link rel=\"alternate\" hreflang=\"be\" href=\"http://www.example.be/index.html\" />\n" +
				"  </url>\n" +
				"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}

	
	private String writeSingleSiteMap(AlternatesSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
