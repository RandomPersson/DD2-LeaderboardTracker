package pl.org.mensa.rp.dd2.LeaderboardTracker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.org.mensa.rp.dd2.LeaderboardTracker.formatters.Formatters;

public class Config {
	File config_file;
	
	public Config(File config_file) {
		this.config_file = config_file;
	}
	
	public Map<String, ConnectionHandler> loadConnectionHandlers() throws IOException {
		Map<String, ConnectionHandler> connection_handlers = new HashMap<String, ConnectionHandler>(2);
		
		List<String> lines = Files.readAllLines(config_file.toPath());
		
		int i=0;
		while (i<lines.size()) {
			if (lines.get(i++).startsWith("sites")) break;
		}
		
		String name = null, ip = null, format = null;
		
		while (i<lines.size()) {
			String line = lines.get(i++);
			if (line.startsWith("//") || line.startsWith("#") || line.isEmpty()) continue;
			if (line.endsWith(":")) line = line.substring(0, line.length()-1);
			
			if (line.startsWith("  - ")) {
				line = line.substring(4);
				
				int colonIndex = line.indexOf(":");
				switch (colonIndex >= 0 ? line.substring(0, colonIndex) : line) {
					case "ip": {
						ip = line.substring(line.indexOf(": ")+2);
					} break;
					case "format": {
						format = line.substring(line.indexOf(": ")+2);
					} break;
					default: {}
				}
			}
			else if (line.startsWith("- ")) {
				line = line.substring(2);
				
				if (name != null) {
					System.out.println("  Creating connection handler: ('" + name + "', '" + ip + "', '" + format + "')");
					connection_handlers.put(name, new ConnectionHandler(ip, Formatters.getFormatterByName(format)));
				}
				
				name = line;
			}
			else {
				break;
			}
		}
		
		if (name != null) {
			System.out.println("  Creating connection handler: ('" + name + "', '" + ip + "', '" + format + "')");
			connection_handlers.put(name, new ConnectionHandler(ip, Formatters.getFormatterByName(format)));
		}
		
		return connection_handlers;
	}
	
	public Map<String, LeaderboardTracker> loadLeaderboardTrackers(Map<String, ConnectionHandler> connection_handlers) throws IOException {
		Map<String, LeaderboardTracker> trackers = new HashMap<String, LeaderboardTracker>(2);
		
		List<String> lines = Files.readAllLines(config_file.toPath());
		
		int i=0;
		while (i<lines.size()) {
			if (lines.get(i++).startsWith("trackers")) break;
		}
		
		String name = null, site = null, file_name = null;
		List<String> players = null;
		
		while (i<lines.size()) {
			String line = lines.get(i++);
			if (line.startsWith("//") || line.startsWith("#") || line.isEmpty()) continue;
			if (line.endsWith(":")) line = line.substring(0, line.length()-1);
			
			if (line.startsWith("    - ")) {
				line = line.substring(6);
				
				if (line.equals("*")) players = null;
				if (players != null) players.add(line);
			}
			else if (line.startsWith("  - ")) {
				line = line.substring(4);
				
				int colonIndex = line.indexOf(":");
				switch (colonIndex >= 0 ? line.substring(0, colonIndex) : line) {
					case "site": {
						site = line.substring(line.indexOf(": ")+2);
					} break;
					case "file": {
						file_name = line.substring(line.indexOf(": ")+2);
					} break;
					case "players": {
						players = new ArrayList<String>(2);
					} break;
					default: {}
				}
			}
			else if (line.startsWith("- ")) {
				line = line.substring(2);
				
				if (name != null) {
					String players_string = "";
					if (players != null) {
						for (String player : players) {
							players_string += "'" + player + "', ";
						}
						players_string = players_string.substring(0, players_string.length()-2);
					}
					else {
						players_string = "*";
					}
					
					System.out.println("  Creating tracker: ('" + name + "', '" + site + "', '" + file_name + "', {" + players_string + "})");
					trackers.put(name, new LeaderboardTracker(connection_handlers.get(site), players, new File("./"+file_name)));
				}
				
				name = line;
			}
			else {
				break;
			}
		}
		
		if (name != null) {
			String players_string = "";
			if (players != null) {
				for (String player : players) {
					players_string += "'" + player + "', ";
				}
				players_string = players_string.substring(0, players_string.length()-2);
			}
			else {
				players_string = "*";
			}
			
			System.out.println("  Creating tracker: ('" + name + "', '" + site + "', '" + file_name + "', {" + players_string + "})");
			trackers.put(name, new LeaderboardTracker(connection_handlers.get(site), players, new File("./"+file_name)));
		}
		
		return trackers;
	}
}
