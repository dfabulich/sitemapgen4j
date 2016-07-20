package com.redfin.sitemapgenerator;

abstract class AbstractSitemapUrlRenderer<T extends WebSitemapUrl> implements ISitemapUrlRenderer<T> {
	
	public void render(WebSitemapUrl url, StringBuilder sb, W3CDateFormat dateFormat, String additionalData) {
		sb.append("  <url>\n");
		sb.append("    <loc>");
		sb.append(UrlUtils.escapeXml(url.getUrl().toString()));
		sb.append("</loc>\n");
		if (url.getLastMod() != null) {
			sb.append("    <lastmod>");
			sb.append(dateFormat.format(url.getLastMod()));
			sb.append("</lastmod>\n");
		}
		if (url.getChangeFreq() != null) {
			sb.append("    <changefreq>");
			sb.append(url.getChangeFreq().toString());
			sb.append("</changefreq>\n");
		}
		if (url.getPriority() != null) {
			sb.append("    <priority>");
			sb.append(url.getPriority().toString());
			sb.append("</priority>\n");
		}
		if (additionalData != null) {
			sb.append(additionalData);
		}
		sb.append("  </url>\n");
	}

	public void renderTag(StringBuilder sb, String namespace, String tagName, Object value) {
		if (value == null) return;
		sb.append("      <");
		sb.append(namespace);
		sb.append(':');
		sb.append(tagName);
		sb.append('>');
		sb.append(UrlUtils.escapeXml(value.toString()));
		sb.append("</");
		sb.append(namespace);
		sb.append(':');
		sb.append(tagName);
		sb.append(">\n");
	}

}
