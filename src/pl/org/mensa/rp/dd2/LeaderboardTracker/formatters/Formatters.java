package pl.org.mensa.rp.dd2.LeaderboardTracker.formatters;

import java.util.HashMap;
import java.util.Map;

public class Formatters {
	private static Map<String, DataFormatter> formatters;
	static {
		formatters = new HashMap<String, DataFormatter>(1);
		formatters.put("html_table", new HTMLTableFormatter());
	}
	
	public static DataFormatter getFormatterByName(String name) {
		return formatters.get(name);
	}
}
