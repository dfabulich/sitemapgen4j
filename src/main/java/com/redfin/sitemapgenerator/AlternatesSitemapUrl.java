package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlternatesSitemapUrl extends WebSitemapUrl {

    private List<Alternate> alternates = new ArrayList<Alternate>();


    /** Options to configure mobile URLs */
    public static class Options extends AbstractSitemapUrlOptions<AlternatesSitemapUrl, Options> {

        /** Specifies the url */
        public Options(String url) throws MalformedURLException {
            this(new URL(url));
        }

        /** Specifies the url */
        public Options(URL url) {
            super(url, AlternatesSitemapUrl.class);
        }
    }

    public AlternatesSitemapUrl(String url) throws MalformedURLException {
        super(url);
    }

    /** Configures the URL with {@link GoogleGeoSitemapUrl.Options} */
    public AlternatesSitemapUrl(AlternatesSitemapUrl.Options options) {
        super(options);
    }


    public void addAlternate (Alternate alternate) {
        alternates.add(alternate);
    }

    public List<Alternate> getAlternates() {
        return alternates;
    }

    public void setAlternates(List<Alternate> alternates) {
        this.alternates = alternates;
    }

    public static class Alternate {

        private String hreflang;
        private String href;

        public Alternate(String hreflang, String href) {
            this.hreflang = hreflang;
            this.href = href;
        }

        public String getHreflang() {
            return hreflang;
        }

        public String getHref() {
            return href;
        }
    }
}
