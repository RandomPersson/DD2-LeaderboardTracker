package pl.org.mensa.rp.dd2.LeaderboardTracker.formatters;

import java.util.List;

public interface DataFormatter {
	public List<List<String>> formatData(String raw_data);
}
