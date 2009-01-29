package com.redfin.sitemapgenerator;

import java.io.IOException;
import java.io.OutputStreamWriter;

abstract class AbstractSitemapUrlRenderer<T extends WebSitemapUrl> implements ISitemapUrlRenderer<T> {

	public void render(WebSitemapUrl url, OutputStreamWriter out, W3CDateFormat dateFormat, String additionalData)
			throws IOException {
		out.write("  <url>\n");
		out.write("    <loc>");
		out.write(url.getUrl().toString());
		out.write("</loc>\n");
		if (url.getLastMod() != null) {
			out.write("    <lastmod>");
			out.write(dateFormat.format(url.getLastMod()));
			out.write("</lastmod>\n");
		}
		if (url.getChangeFreq() != null) {
			out.write("    <changefreq>");
			out.write(url.getChangeFreq().toString());
			out.write("</changefreq>\n");
		}
		if (url.getPriority() != null) {
			out.write("    <priority>");
			out.write(url.getPriority().toString());
			out.write("</priority>\n");
		}
		if (additionalData != null) out.write(additionalData);
		out.write("  </url>\n");
	}

	public void renderTag(StringBuilder sb, String namespace, String tagName, Object value) {
		if (value == null) return;
		sb.append("      <");
		sb.append(namespace);
		sb.append(':');
		sb.append(tagName);
		sb.append('>');
		sb.append(value);
		sb.append("</");
		sb.append(namespace);
		sb.append(':');
		sb.append(tagName);
		sb.append(">\n");
	}

}
