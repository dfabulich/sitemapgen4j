package com.redfin.sitemapgenerator;

/**
 * @author James Brink
 * A simple Exception to specify that a URL is invalid for a sitemap.
 */
public class InvalidURLException extends Exception {
    public InvalidURLException(String message){
        super(message);
    }
}
