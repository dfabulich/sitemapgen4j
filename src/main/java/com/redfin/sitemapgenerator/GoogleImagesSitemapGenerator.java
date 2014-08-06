package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Builds a Google Image Sitemaps
 * 
 * @author Victor Serrato
 * @see <a
 *      href="https://support.google.com/webmasters/answer/178636?hl=en">Image
 *      Sitemaps</a>
 */
public class GoogleImagesSitemapGenerator extends
        SitemapGenerator<GoogleImagesSitemapUrl, GoogleImagesSitemapGenerator> {

    private static final String IMAGE_NAMESPACE = "xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\"";

    /**
     * Configures a builder so you can specify sitemap generator options
     * 
     * @param baseUrl
     *            All URLs in the generated sitemap(s) should appear under this
     *            base URL
     * @param baseDir
     *            Sitemap files will be generated in this directory as either
     *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<GoogleImagesSitemapGenerator> builder(
            URL baseUrl, File baseDir) {
        return new SitemapGeneratorBuilder<GoogleImagesSitemapGenerator>(
                baseUrl, baseDir, GoogleImagesSitemapGenerator.class);
    }

    /**
     * Configures a builder so you can specify sitemap generator options
     * 
     * @param baseUrl
     *            All URLs in the generated sitemap(s) should appear under this
     *            base URL
     * @param baseDir
     *            Sitemap files will be generated in this directory as either
     *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<GoogleImagesSitemapGenerator> builder(
            String baseUrl, File baseDir) throws MalformedURLException {
        return new SitemapGeneratorBuilder<GoogleImagesSitemapGenerator>(
                baseUrl, baseDir, GoogleImagesSitemapGenerator.class);
    }

    GoogleImagesSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
        super(options, new Renderer());
    }

    /**
     * Configures the generator with a base URL and directory to write the
     * sitemap files.
     * 
     * @param baseUrl
     *            All URLs in the generated sitemap(s) should appear under this
     *            base URL
     * @param baseDir
     *            Sitemap files will be generated in this directory as either
     *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @throws MalformedURLException
     */
    public GoogleImagesSitemapGenerator(String baseUrl, File baseDir)
            throws MalformedURLException {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /**
     * Configures the generator with a base URL and directory to write the
     * sitemap files.
     * 
     * @param baseUrl
     *            All URLs in the generated sitemap(s) should appear under this
     *            base URL
     * @param baseDir
     *            Sitemap files will be generated in this directory as either
     *            "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     */
    public GoogleImagesSitemapGenerator(URL baseUrl, File baseDir) {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    private static class Renderer extends
            AbstractSitemapUrlRenderer<GoogleImagesSitemapUrl> implements
            ISitemapUrlRenderer<GoogleImagesSitemapUrl> {

        public Class<GoogleImagesSitemapUrl> getUrlClass() {
            return GoogleImagesSitemapUrl.class;
        }

        public void render(GoogleImagesSitemapUrl url, OutputStreamWriter out,
                W3CDateFormat dateFormat) throws IOException {              
            super.render(url, out, dateFormat, createAdditionalData(url));
        }
        
        private String createAdditionalData(GoogleImagesSitemapUrl url) {
            if (url.isEmpty()) {
                return null;
            }
            
            StringBuilder buffer = new StringBuilder();
            Iterator<GoogleImagesSitemapImage> images = url.getImages();
            
            while (images.hasNext()) {
                GoogleImagesSitemapImage image = images.next();
                buffer.append("    <image:image>\n");

                if (image.getLoc() != null) {
                    buffer.append("      <image:loc>").append(image.getLoc())
                            .append("</image:loc>\n");
                }                
                if (image.getCaption() != null) {
                    buffer.append("      <image:caption>")
                            .append(image.getCaption())
                            .append("</image:caption>\n");
                }                
                if (image.getGeoLocation() != null) {
                    buffer.append("      <image:geo_location>")
                            .append(image.getGeoLocation())
                            .append("</image:geo_location>\n");
                }
                
                if (image.getTitle() != null) {
                    buffer.append("      <image:title>")
                            .append(image.getTitle())
                            .append("</image:title>\n");
                }                
                if (image.getLicense() != null) {
                    buffer.append("      <image:license>")
                            .append(image.getLicense())
                            .append("</image:license>\n");
                }
                buffer.append("    </image:image>\n");
            }

            return buffer.toString();
        }

        public String getXmlNamespaces() {
            return IMAGE_NAMESPACE;
        }
    }
}