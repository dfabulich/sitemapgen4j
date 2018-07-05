package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AlternatesSitemapGenerator extends SitemapGenerator<AlternatesSitemapUrl,AlternatesSitemapGenerator>{


    /** Configures a builder so you can specify sitemap generator options
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<AlternatesSitemapGenerator> builder(URL baseUrl, File baseDir) {
        SitemapGeneratorBuilder<AlternatesSitemapGenerator> builder =
                new SitemapGeneratorBuilder<AlternatesSitemapGenerator>(baseUrl, baseDir, AlternatesSitemapGenerator.class);
        return builder;
    }

    /** Configures a builder so you can specify sitemap generator options
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @return a builder; call .build() on it to make a sitemap generator
     */
    public static SitemapGeneratorBuilder<AlternatesSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
        SitemapGeneratorBuilder<AlternatesSitemapGenerator> builder =
                new SitemapGeneratorBuilder<AlternatesSitemapGenerator>(baseUrl, baseDir, AlternatesSitemapGenerator.class);
        return builder;
    }

    AlternatesSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
        super(options, new AlternatesSitemapGenerator.Renderer());
    }

    /** Configures the generator with a base URL and directory to write the sitemap files.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     * @throws MalformedURLException
     */
    public AlternatesSitemapGenerator(String baseUrl, File baseDir)
            throws MalformedURLException {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /** Configures the generator with a base URL and directory to write the sitemap files.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
     */
    public AlternatesSitemapGenerator(URL baseUrl, File baseDir) {
        this(new SitemapGeneratorOptions(baseUrl, baseDir));
    }

    /**Configures the generator with a base URL and a null directory. The object constructed
     * is not intended to be used to write to files. Rather, it is intended to be used to obtain
     * XML-formatted strings that represent sitemaps.
     *
     * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
     */
    public AlternatesSitemapGenerator(String baseUrl) throws MalformedURLException {
        this(new SitemapGeneratorOptions(new URL(baseUrl)));
    }


    private static class Renderer extends AbstractSitemapUrlRenderer<AlternatesSitemapUrl> implements ISitemapUrlRenderer<AlternatesSitemapUrl> {

        public Class<AlternatesSitemapUrl> getUrlClass() {
            return AlternatesSitemapUrl.class;
        }

        public String getXmlNamespaces() {
            return "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"";
        }

        public void render(AlternatesSitemapUrl url, StringBuilder sb, W3CDateFormat dateFormat) {
            String additionalData = buildAlternates(url.getAlternates());
            super.render(url, sb, dateFormat, additionalData);
        }

        private String buildAlternates(List<AlternatesSitemapUrl.Alternate> alternateList) {
            StringBuilder stringBuilder = new StringBuilder();
            for (AlternatesSitemapUrl.Alternate alternate : alternateList) {
                stringBuilder.append("    ");
                stringBuilder.append("<xhtml:link ");
                stringBuilder.append("rel=\"alternate\"");

                stringBuilder.append(" hreflang=");
                stringBuilder.append('"');
                stringBuilder.append(alternate.getHreflang());
                stringBuilder.append('"');

                stringBuilder.append(" href=");
                stringBuilder.append('"');
                stringBuilder.append(alternate.getHref());
                stringBuilder.append('"');

                stringBuilder.append(" />");
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }

    }
}
