package com.redfin.sitemapgenerator;

import junit.framework.TestCase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoogleImageSitemapUrlTest extends TestCase {

    private static final URL LANDING_URL = newURL("http://www.example.com/index.html");
    private static final URL CONTENT_URL = newURL("http://www.example.com/index.flv");
    File dir;
    GoogleImageSitemapGenerator wsg;

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
        wsg = new GoogleImageSitemapGenerator("http://www.example.com", dir);
        GoogleImageSitemapUrl url = new GoogleImageSitemapUrl(LANDING_URL);
        url.addImage(new Image("http://cdn.example.com/image1.jpg"));
        url.addImage(new Image("http://cdn.example.com/image2.jpg"));
        wsg.addUrl(url);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n" +
                "  <url>\n" +
                "    <loc>http://www.example.com/index.html</loc>\n" +
                "    <image:image>\n" +
                "      <image:loc>http://cdn.example.com/image1.jpg</image:loc>\n" +
                "    </image:image>\n" +
                "    <image:image>\n" +
                "      <image:loc>http://cdn.example.com/image2.jpg</image:loc>\n" +
                "    </image:image>\n" +
                "  </url>\n" +
                "</urlset>";
        String sitemap = writeSingleSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    public void testBaseOptions() throws Exception {
        wsg = new GoogleImageSitemapGenerator("http://www.example.com", dir);
        GoogleImageSitemapUrl url = new GoogleImageSitemapUrl.Options(LANDING_URL)
                .images(new Image("http://cdn.example.com/image1.jpg"), new Image("http://cdn.example.com/image2.jpg"))
                .priority(0.5)
                .changeFreq(ChangeFreq.WEEKLY)
                .lastMod(new Date(0))
                .build();
        wsg.addUrl(url);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n" +
                "  <url>\n" +
                "    <loc>http://www.example.com/index.html</loc>\n" +
                "    <lastmod>1970-01-01T08:00+08:00</lastmod>\n" +
                "    <changefreq>weekly</changefreq>\n" +
                "    <priority>0.5</priority>\n" +
                "    <image:image>\n" +
                "      <image:loc>http://cdn.example.com/image1.jpg</image:loc>\n" +
                "    </image:image>\n" +
                "    <image:image>\n" +
                "      <image:loc>http://cdn.example.com/image2.jpg</image:loc>\n" +
                "    </image:image>\n" +
                "  </url>\n" +
                "</urlset>";

        String sitemap = writeSingleSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    public void testImageOptions() throws Exception {
        wsg = new GoogleImageSitemapGenerator("http://www.example.com", dir);
        GoogleImageSitemapUrl url = new GoogleImageSitemapUrl.Options(LANDING_URL)
                .images(new Image.ImageBuilder("http://cdn.example.com/image1.jpg")
                        .title("image1.jpg")
                        .caption("An image of the number 1")
                        .geoLocation("Pyongyang, North Korea")
                        .license("http://cdn.example.com/licenses/imagelicense.txt")
                        .build(),
                        new Image.ImageBuilder("http://cdn.example.com/image2.jpg")
                                .title("image2.jpg")
                                .caption("An image of the number 2")
                                .geoLocation("Pyongyang, North Korea")
                                .license("http://cdn.example.com/licenses/imagelicense.txt")
                                .build())
                .priority(0.5)
                .changeFreq(ChangeFreq.WEEKLY)
                .lastMod(new Date(0))
                .build();
        wsg.addUrl(url);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n" +
                "  <url>\n" +
                "    <loc>http://www.example.com/index.html</loc>\n" +
                "    <lastmod>1970-01-01T08:00+08:00</lastmod>\n" +
                "    <changefreq>weekly</changefreq>\n" +
                "    <priority>0.5</priority>\n" +
                "    <image:image>\n" +
                "      <image:loc>http://cdn.example.com/image1.jpg</image:loc>\n" +
                "      <image:caption>An image of the number 1</image:caption>\n" +
                "      <image:title>image1.jpg</image:title>\n" +
                "      <image:geo_location>Pyongyang, North Korea</image:geo_location>\n" +
                "      <image:license>http://cdn.example.com/licenses/imagelicense.txt</image:license>\n" +
                "    </image:image>\n" +
                "    <image:image>\n" +
                "      <image:loc>http://cdn.example.com/image2.jpg</image:loc>\n" +
                "      <image:caption>An image of the number 2</image:caption>\n" +
                "      <image:title>image2.jpg</image:title>\n" +
                "      <image:geo_location>Pyongyang, North Korea</image:geo_location>\n" +
                "      <image:license>http://cdn.example.com/licenses/imagelicense.txt</image:license>\n" +
                "    </image:image>\n" +
                "  </url>\n" +
                "</urlset>";

        String sitemap = writeSingleSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    public void testTooManyImages() throws Exception {
        wsg = new GoogleImageSitemapGenerator("http://www.example.com", dir);
        List<Image> images = new ArrayList<Image>();
        for(int i = 0; i <= 1000; i++) {
            images.add(new Image("http://cdn.example.com/image" + i + ".jpg"));
        }
        try {
            GoogleImageSitemapUrl url = new GoogleImageSitemapUrl.Options(LANDING_URL)
                    .images(images)
                    .priority(0.5)
                    .changeFreq(ChangeFreq.WEEKLY)
                    .lastMod(new Date(0))
                    .build();
            fail("Too many images allowed");
        } catch (RuntimeException r) {}
    }



    private String writeSingleSiteMap(GoogleImageSitemapGenerator wsg) {
        List<File> files = wsg.write();
        assertEquals("Too many files: " + files.toString(), 1, files.size());
        assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
        return TestUtil.slurpFileAndDelete(files.get(0));
    }

}
