package jp.hishidama.asakusafw_spi.dmdl.template.driver.beans;

public class TemplateBeanUtil {

	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder(s.length());

		boolean first = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '_') {
				first = true;
				continue;
			}
			if (first) {
				sb.append(Character.toUpperCase(c));
				first = false;
			} else {
				sb.append(Character.toLowerCase(c));
			}
		}

		return sb.toString();
	}
}
