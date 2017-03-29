package com.redfin.sitemapgenerator;

import java.net.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * One configurable Google Link URL. To configure, use {@link Options}
 *
 * @author Sergio Vico
 * @see Options
 * @see <a href="https://support.google.com/webmasters/answer/2620865">Creating alternate language pages Sitemaps</a>
 */
public class GoogleLinkSitemapUrl extends WebSitemapUrl {

    /** Options to configure mobile URLs */
    public static class Options extends AbstractSitemapUrlOptions<GoogleLinkSitemapUrl, Options> {
        private final Map<Locale, URL> alternates;

        private static Map<Locale, URL> convertAlternates(final Map<String, String> alternates)
            throws MalformedURLException {

            final Map<Locale, URL> converted = new LinkedHashMap<Locale, URL>(alternates.size());
            for (final Entry<String, String> entry : alternates.entrySet()) {
                converted.put(Locale.forLanguageTag(entry.getKey()), new URL(entry.getValue()));
            }
            return converted;
        }

        /** Specifies the url */
        public Options(final String url, final Map<String, String> alternates) throws MalformedURLException {

            this(new URL(url), convertAlternates(alternates));
        }

        /** Specifies the url */
        public Options(final URL url, final Map<Locale, URL> alternates) {
            super(url, GoogleLinkSitemapUrl.class);
            this.alternates = new LinkedHashMap<Locale, URL>(alternates);
        }
    }

    private final Map<Locale, URL> alternates;

    /** Specifies configures url with options */
    public GoogleLinkSitemapUrl(final Options options) {
        super(options);
        alternates = options.alternates;
    }

    /** Specifies the url */
    public GoogleLinkSitemapUrl(final String url, final Map<String, String> alternates) throws MalformedURLException {
        this(new Options(url, alternates));
    }

    /** Specifies the url */
    public GoogleLinkSitemapUrl(final URL url, final Map<Locale, URL> alternates) {
        this(new Options(url, alternates));
    }

    public Map<Locale, URL> getAlternates() {

        return this.alternates;
    }
}
