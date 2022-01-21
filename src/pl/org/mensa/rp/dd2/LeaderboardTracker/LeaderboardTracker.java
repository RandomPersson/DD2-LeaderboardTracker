package pl.org.mensa.rp.dd2.LeaderboardTracker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LeaderboardTracker {
	static final DateFormat date_formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	ConnectionHandler connection_handler;
	List<String> trackedPlayers;
	File data_file;
	
	public LeaderboardTracker(ConnectionHandler connection_handler, List<String> trackedPlayers, File data_file) throws IOException {
		data_file.getParentFile().mkdirs();
		if (!data_file.exists()) data_file.createNewFile();
		
		this.connection_handler = connection_handler;
		this.trackedPlayers = trackedPlayers;
		this.data_file = data_file;
	}
	
	public void saveData() {
		List<List<String>> table = connection_handler.getFormattedData();
		
		if (table == null || table.isEmpty()) {
			System.out.println("No new data to save, aborting process");
			return;
		}
		
		String data = date_formatter.format(Calendar.getInstance().getTime()) + "\n";
		
		List<String> player_data = getData(table);
		for (String line : player_data) data += "  " + line + "\n";
		
		try {
			Files.write(data_file.toPath(), data.getBytes(), StandardOpenOption.APPEND);
			
			System.out.println("Success!");
		} catch (IOException e) {
			System.out.println("Error saving to file, data lost");
			e.printStackTrace();
		}
	}
	
	private List<String> getData(List<List<String>> table) {
		List<String> data = null;
		
		if (trackedPlayers == null) {
			data = new ArrayList<String>(100);
			
			for (List<String> row : table) {
				if (row.size() < 2) continue;
				
				data.add(row.get(0) + ": " + row.get(1));
			}
		}
		else {
			data = new ArrayList<String>(2);
			
			for (List<String> row : table) {
				if (row.size() < 2) continue;
				
				for (String player : trackedPlayers) {
					if (row.get(0).contains(player)) {
						data.add(row.get(0) + ": " + row.get(1));
						break;
					}
				}
			}
		}
		
		return data;
	}
}
