package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Builds an extended sitemap with google support for google extensions. To configure options use {@link #builder(URL, File)}
 * @see <a href="https://support.google.com/webmasters/answer/183668">Manage your sitemaps</a>
 * */
public class GoogleExtensionSitemapGenerator extends SitemapGenerator<GoogleExtensionSitemapUrl, GoogleExtensionSitemapGenerator> {

    GoogleExtensionSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
        super(options, new GoogleExtensionSitemapGenerator.Renderer());
    }

    /** Configures the generator with a base URL and directory to write the sitemap files.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @throws MalformedURLException
     */
    public GoogleExtensionSitemapGenerator(String baseUrl, File baseDir)
            throws MalformedURLException {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /**Configures the generator with a base URL and directory to write the sitemap files.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     */
    public GoogleExtensionSitemapGenerator(URL baseUrl, File baseDir) {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /**Configures the generator with a base URL and a null directory. The object constructed
     * is not intended to be used to write to files. Rather, it is intended to be used to obtain
     * XML-formatted strings that represent sitemaps.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     */
    public GoogleExtensionSitemapGenerator(String baseUrl) throws MalformedURLException {
        this(new SitemapGeneratorOptions(new URL(baseUrl)));
    }

    /**Configures the generator with a base URL and a null directory. The object constructed
     * is not intended to be used to write to files. Rather, it is intended to be used to obtain
     * XML-formatted strings that represent sitemaps.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     */
    public GoogleExtensionSitemapGenerator(URL baseUrl) {
        this(new SitemapGeneratorOptions(baseUrl));
    }

    /** Configures a builder so you can specify sitemap generator options
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<GoogleExtensionSitemapGenerator> builder(URL baseUrl, File baseDir) {
        return new SitemapGeneratorBuilder<GoogleExtensionSitemapGenerator>(baseUrl, baseDir, GoogleExtensionSitemapGenerator.class);
    }

    /** Configures a builder so you can specify sitemap generator options
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     * @throws MalformedURLException
     */
    public static SitemapGeneratorBuilder<GoogleExtensionSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
        return new SitemapGeneratorBuilder<GoogleExtensionSitemapGenerator>(baseUrl, baseDir, GoogleExtensionSitemapGenerator.class);
    }

    private static class Renderer extends AbstractSitemapUrlRenderer<GoogleExtensionSitemapUrl> implements ISitemapUrlRenderer<GoogleExtensionSitemapUrl> {

        public Class<GoogleExtensionSitemapUrl> getUrlClass() {
            return GoogleExtensionSitemapUrl.class;
        }

        public String getXmlNamespaces() {
            return "xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\"";
        }

        public void render(GoogleExtensionSitemapUrl url, StringBuilder sb, W3CDateFormat dateFormat) {
            StringBuilder tagSb = new StringBuilder();

            for(Image image : url.getImages()) {
                tagSb.append("    <image:image>\n");
                renderTag(tagSb, "image", "loc", image.getUrl());
                renderTag(tagSb, "image", "caption", image.getCaption());
                renderTag(tagSb, "image", "title", image.getTitle());
                renderTag(tagSb, "image", "geo_location", image.getGeoLocation());
                renderTag(tagSb, "image", "license", image.getLicense());
                tagSb.append("    </image:image>\n");
            }
            super.render(url, sb, dateFormat, tagSb.toString());
        }
    }
}
