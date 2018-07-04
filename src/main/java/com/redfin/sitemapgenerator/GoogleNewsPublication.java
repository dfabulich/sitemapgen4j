package com.redfin.sitemapgenerator;

/**
 * @author Tom
 * @since 23/05/2017
 */
public class GoogleNewsPublication {
    private String name;
    private String language;

    public GoogleNewsPublication(String name, String language) {
        this.name = name;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
