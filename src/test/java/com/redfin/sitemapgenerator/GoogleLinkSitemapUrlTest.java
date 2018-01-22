package com.redfin.sitemapgenerator;

import java.io.File;
import java.util.*;

import junit.framework.TestCase;

public class GoogleLinkSitemapUrlTest extends TestCase {

    File dir;
    GoogleLinkSitemapGenerator wsg;

    @Override
    public void setUp() throws Exception {

        dir = File.createTempFile(GoogleLinkSitemapUrlTest.class.getSimpleName(), "");
        dir.delete();
        dir.mkdir();
        dir.deleteOnExit();
    }

    @Override
    public void tearDown() {

        wsg = null;
        for (final File file : dir.listFiles()) {
            file.deleteOnExit();
            file.delete();
        }
        dir.delete();
        dir = null;
    }

    public void testSimpleUrlWithHrefLang() throws Exception {

        wsg = new GoogleLinkSitemapGenerator("http://www.example.com", dir);
        final Map<String, Map<String, String>> alternates = new LinkedHashMap<String, Map<String, String>>();
        alternates.put("http://www.example/en/index.html", Collections.singletonMap("hreflang", "en-GB"));
        alternates.put("http://www.example/fr/index.html", Collections.singletonMap("hreflang", "fr-FR"));
        alternates.put("http://www.example/es/index.html", Collections.singletonMap("hreflang", "es-ES"));

        final GoogleLinkSitemapUrl url = new GoogleLinkSitemapUrl("http://www.example.com/index.html", alternates);
        wsg.addUrl(url);
        //@formatter:off
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" "
            + "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n"
            + "  <url>\n"
            + "    <loc>http://www.example.com/index.html</loc>\n"
            + "    <xhtml:link\n"
            + "      rel=\"alternate\"\n"
            + "      hreflang=\"en-GB\"\n"
            + "      href=\"http://www.example/en/index.html\"\n"
            + "    />\n"
            + "    <xhtml:link\n"
            + "      rel=\"alternate\"\n"
            + "      hreflang=\"fr-FR\"\n"
            + "      href=\"http://www.example/fr/index.html\"\n"
            + "    />\n"
            + "    <xhtml:link\n"
            + "      rel=\"alternate\"\n"
            + "      hreflang=\"es-ES\"\n"
            + "      href=\"http://www.example/es/index.html\"\n"
            + "    />\n"
            + "  </url>\n"
            + "</urlset>";
        //@formatter:on
        final String sitemap = writeSingleSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    public void testSimpleUrlWithMedia() throws Exception {

        wsg = new GoogleLinkSitemapGenerator("http://www.example.com", dir);
        final Map<String, Map<String, String>> alternates = new LinkedHashMap<String, Map<String, String>>();
        alternates.put("http://www.example/en/index.html", Collections.singletonMap("media", "only screen and (max-width: 640px)"));
        alternates.put("http://www.example/fr/index.html", Collections.singletonMap("media", "only screen and (max-width: 640px)"));
        alternates.put("http://www.example/es/index.html", Collections.singletonMap("media", "only screen and (max-width: 640px)"));

        final GoogleLinkSitemapUrl url = new GoogleLinkSitemapUrl("http://www.example.com/index.html", alternates);
        wsg.addUrl(url);
        //@formatter:off
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" "
            + "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" >\n"
            + "  <url>\n"
            + "    <loc>http://www.example.com/index.html</loc>\n"
            + "    <xhtml:link\n"
            + "      rel=\"alternate\"\n"
            + "      media=\"only screen and (max-width: 640px)\"\n"
            + "      href=\"http://www.example/en/index.html\"\n"
            + "    />\n"
            + "    <xhtml:link\n"
            + "      rel=\"alternate\"\n"
            + "      media=\"only screen and (max-width: 640px)\"\n"
            + "      href=\"http://www.example/fr/index.html\"\n"
            + "    />\n"
            + "    <xhtml:link\n"
            + "      rel=\"alternate\"\n"
            + "      media=\"only screen and (max-width: 640px)\"\n"
            + "      href=\"http://www.example/es/index.html\"\n"
            + "    />\n"
            + "  </url>\n"
            + "</urlset>";
        //@formatter:on
        final String sitemap = writeSingleSiteMap(wsg);
        assertEquals(expected, sitemap);
    }

    private String writeSingleSiteMap(final GoogleLinkSitemapGenerator wsg) {

        final List<File> files = wsg.write();
        assertEquals("Too many files: " + files.toString(), 1, files.size());
        assertEquals("Sitemap misnamed", "sitemap.xml", files.get(0).getName());
        return TestUtil.slurpFileAndDelete(files.get(0));
    }
}
