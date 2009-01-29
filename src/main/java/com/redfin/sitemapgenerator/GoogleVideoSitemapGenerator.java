package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Builds a sitemap for Google Video search.  To configure options, use {@link #builder(URL, File)}
 * @author Dan Fabulich
 * @see <a href="http://www.google.com/support/webmasters/bin/answer.py?answer=80472">Creating Video Sitemaps</a>
 */
public class GoogleVideoSitemapGenerator extends SitemapGenerator<GoogleVideoSitemapUrl,GoogleVideoSitemapGenerator> {

	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleVideoSitemapGenerator> builder(URL baseUrl, File baseDir) {
		return new SitemapGeneratorBuilder<GoogleVideoSitemapGenerator>(baseUrl, baseDir, GoogleVideoSitemapGenerator.class);
	}
	
	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleVideoSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
		return new SitemapGeneratorBuilder<GoogleVideoSitemapGenerator>(baseUrl, baseDir, GoogleVideoSitemapGenerator.class);
	}

	GoogleVideoSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
		super(options, new Renderer());
	}
	
	/**Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @throws MalformedURLException
	 */
	public GoogleVideoSitemapGenerator(String baseUrl, File baseDir)
			throws MalformedURLException {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/**Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 */
	public GoogleVideoSitemapGenerator(URL baseUrl, File baseDir) {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	private static class Renderer extends AbstractSitemapUrlRenderer<GoogleVideoSitemapUrl> implements ISitemapUrlRenderer<GoogleVideoSitemapUrl> {

		public Class<GoogleVideoSitemapUrl> getUrlClass() {
			return GoogleVideoSitemapUrl.class;
		}

		public void render(GoogleVideoSitemapUrl url, OutputStreamWriter out,
				W3CDateFormat dateFormat) throws IOException {
			StringBuilder sb = new StringBuilder();
			sb.append("    <video:video>\n");
			renderTag(sb, "video", "content_loc", url.getContentUrl());
			if (url.getPlayerUrl() != null) {
				sb.append("      <video:player_loc allow_embed=\"");
				sb.append(url.getAllowEmbed());
				sb.append("\">");
				sb.append(url.getPlayerUrl());
				sb.append("</video:player_loc>\n");
			}
			renderTag(sb, "video", "thumbnail_loc", url.getThumbnailUrl());
			renderTag(sb, "video", "title", url.getTitle());
			renderTag(sb, "video", "description", url.getDescription());
			renderTag(sb, "video", "rating", url.getRating());
			renderTag(sb, "video", "view_count", url.getViewCount());
			if (url.getPublicationDate() != null) {
				renderTag(sb, "video", "publication_date", dateFormat.format(url.getPublicationDate()));
			}
			if (url.getTags() != null) {
				for (String tag : url.getTags()) {
					renderTag(sb, "video", "tag", tag);
				}
			}
			renderTag(sb, "video", "category", url.getCategory());
			renderTag(sb, "video", "family_friendly", url.getFamilyFriendly());
			renderTag(sb, "video", "duration", url.getDurationInSeconds());
			sb.append("    </video:video>\n");
			super.render(url, out, dateFormat, sb.toString());
			
		}
		
		public String getXmlNamespaces() {
			return "xmlns:video=\"http://www.google.com/schemas/sitemap-video/1.1\"";
		}
		
	}
	
}
