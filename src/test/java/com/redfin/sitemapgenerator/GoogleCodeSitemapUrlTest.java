package com.redfin.sitemapgenerator;

import java.io.File;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.GoogleCodeSitemapGenerator;
import com.redfin.sitemapgenerator.GoogleCodeSitemapUrl;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.GoogleCodeSitemapUrl.FileType;
import com.redfin.sitemapgenerator.GoogleCodeSitemapUrl.License;
import com.redfin.sitemapgenerator.GoogleCodeSitemapUrl.Options;

public class GoogleCodeSitemapUrlTest extends TestCase {
	
	File dir;
	GoogleCodeSitemapGenerator wsg;
	
	public void setUp() throws Exception {
		dir = File.createTempFile(GoogleCodeSitemapUrlTest.class.getSimpleName(), "");
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
		wsg = new GoogleCodeSitemapGenerator("http://www.example.com", dir);
		GoogleCodeSitemapUrl url = new GoogleCodeSitemapUrl("http://www.example.com/Foo.java", FileType.JAVA);
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" " +
			"xmlns:codesearch=\"http://www.google.com/codesearch/schemas/sitemap/1.0\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/Foo.java</loc>\n" +
			"    <codesearch:codesearch>\n" + 
			"      <codesearch:filetype>java</codesearch:filetype>\n" + 
			"    </codesearch:codesearch>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testOptions() throws Exception {
		W3CDateFormat dateFormat = new W3CDateFormat();
		dateFormat.setTimeZone(W3CDateFormat.ZULU);
		wsg = GoogleCodeSitemapGenerator.builder("http://www.example.com", dir)
			.dateFormat(dateFormat).build();
		GoogleCodeSitemapUrl url = new Options("http://www.example.com/foo/Foo.java", FileType.JAVA)
			.changeFreq(ChangeFreq.HOURLY).lastMod(new Date(0)).priority(0.5)
			.license(License.GPL).fileName("Foo.java").packageUrl("http://www.example.com/foo/")
			.build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:codesearch=\"http://www.google.com/codesearch/schemas/sitemap/1.0\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/foo/Foo.java</loc>\n" + 
			"    <lastmod>1970-01-01</lastmod>\n" + 
			"    <changefreq>hourly</changefreq>\n" + 
			"    <priority>0.5</priority>\n" + 
			"    <codesearch:codesearch>\n" + 
			"      <codesearch:filetype>java</codesearch:filetype>\n" + 
			"      <codesearch:license>gpl</codesearch:license>\n" + 
			"      <codesearch:filename>Foo.java</codesearch:filename>\n" + 
			"      <codesearch:packageurl>http://www.example.com/foo/</codesearch:packageurl>\n" + 
			"    </codesearch:codesearch>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testPackageOptions() throws Exception {
		wsg = new GoogleCodeSitemapGenerator("http://www.example.com", dir);
		GoogleCodeSitemapUrl url = new Options("http://www.example.com/foo/Foo.zip", FileType.ARCHIVE)
			.license(License.GPL).fileName("Foo.java").packageUrl("http://www.example.com/foo/")
			.packageMap("packagemap.xml").build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:codesearch=\"http://www.google.com/codesearch/schemas/sitemap/1.0\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/foo/Foo.zip</loc>\n" + 
			"    <codesearch:codesearch>\n" + 
			"      <codesearch:filetype>archive</codesearch:filetype>\n" + 
			"      <codesearch:license>gpl</codesearch:license>\n" + 
			"      <codesearch:filename>Foo.java</codesearch:filename>\n" + 
			"      <codesearch:packageurl>http://www.example.com/foo/</codesearch:packageurl>\n" + 
			"      <codesearch:packagemap>packagemap.xml</codesearch:packagemap>\n" + 
			"    </codesearch:codesearch>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testPackageMapNonArchive() throws Exception {
		Options options = new Options("http://www.example.com/foo/Foo.java", FileType.JAVA);
		try {
			options.packageMap("packagemap.xml");
			fail("I was allowed to set packagemap on non-archive");
		} catch (RuntimeException e) {}
	}
	
	private String writeSingleSiteMap(GoogleCodeSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
