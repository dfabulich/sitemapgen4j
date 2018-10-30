package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Configurable sitemap url with support for Google extensions
 * @see <a href="https://support.google.com/webmasters/answer/183668">Manage your sitemaps</a>
 *
 * TODO Add support for video tags and news tags
 */
public class GoogleExtensionSitemapUrl extends WebSitemapUrl {

    private final List<Image> images;

    public GoogleExtensionSitemapUrl(String url) throws MalformedURLException {
        this(new Options(url));
    }

    public GoogleExtensionSitemapUrl(URL url) {
        this(new Options(url));
    }

    public GoogleExtensionSitemapUrl(Options options) {
        super(options);
        this.images = options.images;
    }

    public void addImage(Image image) {
        this.images.add(image);
        if(this.images.size() > 1000) {
            throw new RuntimeException("A URL cannot have more than 1000 image tags");
        }
    }

    /** Options to configure Google Extension URLs */
    public static class Options extends AbstractSitemapUrlOptions<GoogleExtensionSitemapUrl, GoogleExtensionSitemapUrl.Options> {
        private List<Image> images;


        public Options(URL url) {
            super(url, GoogleExtensionSitemapUrl.class);
            images = new ArrayList<Image>();
        }

        public Options(String url) throws MalformedURLException {
            super(url, GoogleExtensionSitemapUrl.class);
            images = new ArrayList<Image>();
        }

        public Options images(List<Image> images) {
            if(images != null && images.size() > 1000) {
                throw new RuntimeException("A URL cannot have more than 1000 image tags");
            }
            this.images = images;
            return this;
        }

        public Options images(Image...images) {
            if(images.length > 1000) {
                throw new RuntimeException("A URL cannot have more than 1000 image tags");
            }
            return images(Arrays.asList(images));

        }
    }

    /**Retrieves list of images*/
    public List<Image> getImages() {
        return this.images;
    }
}
