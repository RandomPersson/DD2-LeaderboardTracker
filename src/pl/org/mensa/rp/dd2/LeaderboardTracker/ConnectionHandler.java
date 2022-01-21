package pl.org.mensa.rp.dd2.LeaderboardTracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pl.org.mensa.rp.dd2.LeaderboardTracker.formatters.DataFormatter;

public class ConnectionHandler {
	private String ip;
	DataFormatter data_formatter;
	
	List<String> downloaded_data;
	
	public ConnectionHandler(String ip, DataFormatter data_formatter) {
		this.ip = ip;
		this.data_formatter = data_formatter;
		
		downloaded_data = new ArrayList<String>(45);
	}
	
	public String getRawData() {
		if (downloaded_data == null) return null;
		
		String raw_data = "";
		
		for (String line : downloaded_data) {
			raw_data += line;
		}
		
		return raw_data;
	}
	public List<String> getData() {
		return this.downloaded_data;
	}
	public List<List<String>> getFormattedData() {
		return data_formatter.formatData(getRawData());
	}
	
	public boolean update() {
		downloaded_data.clear();
		
		try {
			URL url = new URL(ip);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			for (String line; (line = in.readLine()) != null;) {
				this.downloaded_data.add(line);
			}
			in.close();
			
			System.out.println("Success!");
			
			return true;
		}
		catch (Exception exc) {
			System.out.println("Connection error, could not obtain new data");
			downloaded_data.clear();
			
			return false;
		}
	}
}
