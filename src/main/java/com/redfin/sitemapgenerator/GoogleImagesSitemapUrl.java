package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GoogleImagesSitemapUrl extends WebSitemapUrl {

    private Set<GoogleImagesSitemapImage> images = new HashSet<GoogleImagesSitemapImage>();

    public static class Options extends
            AbstractSitemapUrlOptions<GoogleImagesSitemapUrl, Options> {
        public Options(String url) throws MalformedURLException {
            super(url, GoogleImagesSitemapUrl.class);
        }

        public Options(URL url) {
            super(url, GoogleImagesSitemapUrl.class);
        }
    }

    public GoogleImagesSitemapUrl(URL url) {
        this(new Options(url));
    }

    public GoogleImagesSitemapUrl(String url) throws MalformedURLException {
        this(new Options(url));
    }

    public GoogleImagesSitemapUrl(Options options) {
        super(options);
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }

    public Iterator<GoogleImagesSitemapImage> getImages() {
        return images.iterator();
    }

    public GoogleImagesSitemapUrl addImage(GoogleImagesSitemapImage image) {
        images.add(image);
        return this;
    }

    public GoogleImagesSitemapUrl addImages(
            Collection<GoogleImagesSitemapImage> sitemapImages) {
        images.addAll(sitemapImages);
        return this;
    }
}
