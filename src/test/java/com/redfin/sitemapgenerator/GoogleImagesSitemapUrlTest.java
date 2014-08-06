package com.redfin.sitemapgenerator;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

public class GoogleImagesSitemapUrlTest extends TestCase {

    File dir;
    GoogleImagesSitemapGenerator wsg;

    public void setUp() throws Exception {
        dir = File.createTempFile(
                GoogleImagesSitemapUrlTest.class.getSimpleName(), "");
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
        wsg = new GoogleImagesSitemapGenerator("http://www.example.com", dir);
        GoogleImagesSitemapUrl url = new GoogleImagesSitemapUrl(
                "http://www.example.com/index.html");
        GoogleImagesSitemapImage image = new GoogleImagesSitemapImage(
                "http://www.example.com/image.jpeg", "caption", "geoLocation",
                "title", "http://www.example.com/licence.html");
        url.addImage(image);
        wsg.addUrl(url);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" "
                + "xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n"
                + "  <url>\n"                 
                + "    <loc>http://www.example.com/index.html</loc>\n"
                + "    <image:image>\n" 
                + "      <image:loc>http://www.example.com/image.jpeg</image:loc>\n"
                + "      <image:caption>caption</image:caption>\n"
                + "      <image:geo_location>geoLocation</image:geo_location>\n"
                + "      <image:title>title</image:title>\n"
                + "      <image:license>http://www.example.com/licence.html</image:license>\n"
                + "    </image:image>\n"
                + "  </url>\n" + "</urlset>";
        String sitemap = writeSingleSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    private String writeSingleSiteMap(GoogleImagesSitemapGenerator wsg) {
        List<File> files = wsg.write();
        assertEquals("Too many files: " + files.toString(), 1, files.size());
        assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
        return TestUtil.slurpFileAndDelete(files.get(0));
    }
}
