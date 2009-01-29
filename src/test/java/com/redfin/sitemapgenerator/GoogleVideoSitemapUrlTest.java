package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.redfin.sitemapgenerator.GoogleVideoSitemapGenerator;
import com.redfin.sitemapgenerator.GoogleVideoSitemapUrl;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.GoogleVideoSitemapUrl.Options;

public class GoogleVideoSitemapUrlTest extends TestCase {
	
	private static final URL LANDING_URL = newURL("http://www.example.com/index.html");
	private static final URL CONTENT_URL = newURL("http://www.example.com/index.flv");
	File dir;
	GoogleVideoSitemapGenerator wsg;
	
	private static URL newURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {}
		return null;
	}
	
	public void setUp() throws Exception {
		dir = File.createTempFile(GoogleVideoSitemapUrlTest.class.getSimpleName(), "");
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
		wsg = new GoogleVideoSitemapGenerator("http://www.example.com", dir);
		GoogleVideoSitemapUrl url = new GoogleVideoSitemapUrl(LANDING_URL, CONTENT_URL);
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" + 
			"    <video:video>\n" + 
			"      <video:content_loc>http://www.example.com/index.flv</video:content_loc>\n" + 
			"    </video:video>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}

	public void testOptions() throws Exception {
		W3CDateFormat dateFormat = new W3CDateFormat();
		dateFormat.setTimeZone(W3CDateFormat.ZULU);
		wsg = GoogleVideoSitemapGenerator.builder("http://www.example.com", dir)
			.dateFormat(dateFormat).build();
		GoogleVideoSitemapUrl url = new Options(LANDING_URL, CONTENT_URL)
			.playerUrl(new URL("http://www.example.com/index.swf"), true)
			.thumbnailUrl(new URL("http://www.example.com/thumbnail.jpg"))
			.title("This is a video!").description("A great video about dinosaurs")
			.rating(5.0).viewCount(500000).publicationDate(new Date(0)).tags("dinosaurs", "example", "awesome")
			.category("example").familyFriendly(false).durationInSeconds(60*30)
			.build();
		wsg.addUrl(url);
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\" >\n" + 
			"  <url>\n" + 
			"    <loc>http://www.example.com/index.html</loc>\n" + 
			"    <video:video>\n" + 
			"      <video:content_loc>http://www.example.com/index.flv</video:content_loc>\n" + 
			"      <video:player_loc allow_embed=\"Yes\">http://www.example.com/index.swf</video:player_loc>\n" + 
			"      <video:thumbnail_loc>http://www.example.com/thumbnail.jpg</video:thumbnail_loc>\n" + 
			"      <video:title>This is a video!</video:title>\n" + 
			"      <video:description>A great video about dinosaurs</video:description>\n" + 
			"      <video:rating>5.0</video:rating>\n" + 
			"      <video:view_count>500000</video:view_count>\n" + 
			"      <video:publication_date>1970-01-01</video:publication_date>\n" + 
			"      <video:tag>dinosaurs</video:tag>\n" + 
			"      <video:tag>example</video:tag>\n" + 
			"      <video:tag>awesome</video:tag>\n" + 
			"      <video:category>example</video:category>\n" + 
			"      <video:family_friendly>No</video:family_friendly>\n" + 
			"      <video:duration>1800</video:duration>\n" + 
			"    </video:video>\n" + 
			"  </url>\n" + 
			"</urlset>";
		String sitemap = writeSingleSiteMap(wsg);
		assertEquals(expected, sitemap);
	}
	
	public void testLongTitle() {
		try {
			new Options(LANDING_URL, CONTENT_URL).title("Unfortunately, this title is far longer than 100 characters" +
					"by virtue of having a great deal to say but not much content.");
			fail("Long title inappropriately allowed");
		} catch (RuntimeException e) {}
	}
	public void testLongDescription() {
		StringBuilder sb = new StringBuilder(2049);
		for (int i = 0; i < 2049; i++) {
			sb.append('x');
		}
		try {
			new Options(LANDING_URL, CONTENT_URL).description(sb.toString());
			fail("Long description inappropriately allowed");
		} catch (RuntimeException e) {}
	}
	public void testWrongRating() {
		Options o = new Options(LANDING_URL, CONTENT_URL);
		try {
			o.rating(-1.0);
			fail("Negative rating allowed");
		} catch (RuntimeException e) {}
		
		try {
			o.rating(10.0);
			fail(">5 rating allowed");
		} catch (RuntimeException e) {}
	}
	public void testTooManyTags() {
		int maxTags = 32;
		String[] tags = new String[maxTags+1];
		for (int i = 0; i < maxTags+1; i++) {
			tags[i] = "tag" + i;
		}
		try {
			new Options(LANDING_URL, CONTENT_URL).tags(tags).build();
			fail("Too many tags allowed");
		} catch (RuntimeException e) {}
		
	}
	public void testLongCategory() {
		StringBuilder sb = new StringBuilder(257);
		for (int i = 0; i < 257; i++) {
			sb.append('x');
		}
		try {
			new Options(LANDING_URL, CONTENT_URL).category(sb.toString());
			fail("Long category inappropriately allowed");
		} catch (RuntimeException e) {}
	}
	
	public void testWrongDuration() {
		Options o = new Options(LANDING_URL, CONTENT_URL);
		try {
			o.durationInSeconds(-1);
			fail("Negative duration allowed");
		} catch (RuntimeException e) {}
		
		try {
			o.durationInSeconds(Integer.MAX_VALUE);
			fail(">8hr duration allowed");
		} catch (RuntimeException e) {}
	}
	
	private String writeSingleSiteMap(GoogleVideoSitemapGenerator wsg) {
		List<File> files = wsg.write();
		assertEquals("Too many files: " + files.toString(), 1, files.size());
		assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
		return TestUtil.slurpFileAndDelete(files.get(0));
	}
}
