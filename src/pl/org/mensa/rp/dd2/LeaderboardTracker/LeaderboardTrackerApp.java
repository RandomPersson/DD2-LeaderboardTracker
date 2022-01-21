package pl.org.mensa.rp.dd2.LeaderboardTracker;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class LeaderboardTrackerApp implements Runnable {
	static final DateFormat date_formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	Config config;
	
	private Map<String, ConnectionHandler> connection_handlers;
	private Map<String, LeaderboardTracker> trackers;
	
	public LeaderboardTrackerApp() throws IOException {
		loadConfig("config.txt");
	}
	
	@Override
	public void run() {
		for (Map.Entry<String, ConnectionHandler> entry : connection_handlers.entrySet()) {
			System.out.print(prefix() + "Downloading data from site " + entry.getKey() + ": ");
			if (!entry.getValue().update()) {
				System.out.println(prefix() + "All downloads aborted");
				break;
			}
		}
		System.out.println();
		for (Map.Entry<String, LeaderboardTracker> entry : trackers.entrySet()) {
			System.out.print(prefix() + "Saving data from tracker " + entry.getKey() + ": ");
			entry.getValue().saveData();
		}
		System.out.println();
	}
	
	private void loadConfig(String config_file_name) throws IOException {
		File config_file = new File("./"+config_file_name);
		config = new Config(config_file);
		
		System.out.println(prefix() + "Loading connection handlers..");
		connection_handlers = config.loadConnectionHandlers();
		System.out.println();
		System.out.println(prefix() + "Loading trackers..");
		trackers = config.loadLeaderboardTrackers(connection_handlers);
		System.out.println();
	}
	
	private String prefix() {
		return ("[" + date_formatter.format(Calendar.getInstance().getTime()) + "] ");
	}
}
