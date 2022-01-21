package pl.org.mensa.rp.dd2.LeaderboardTracker.formatters;

import java.util.ArrayList;
import java.util.List;

public class HTMLTableFormatter implements DataFormatter {
	@Override
	public List<List<String>> formatData(String raw_data) {
		if (raw_data == null) return null;
		
		List<List<String>> rows = new ArrayList<List<String>>(100);
		for (int row_start=raw_data.indexOf("<tr"), row_end=raw_data.indexOf("</tr>", row_start); row_start>0;) {
			List<String> cells = new ArrayList<String>(2);
			
			for (int cell_start=raw_data.indexOf("<td", row_start), cell_end=raw_data.indexOf("</td>", cell_start); cell_start>0 && cell_end<row_end;) {
				String cell = raw_data.substring(raw_data.indexOf(">", cell_start)+1, cell_end);
				
				cell = cell.substring(cell.lastIndexOf(">")+1);
				while (cell.startsWith(" ") || cell.startsWith("\"")) cell = cell.substring(1);
				while (cell.endsWith(" ") || cell.endsWith("\"")) cell = cell.substring(0, cell.length()-1);
				
				if (cell.length() > 0) cells.add(cell);
				
				cell_start = raw_data.indexOf("<td", cell_end);
				cell_end = raw_data.indexOf("</td>", cell_start);
			}
			
			rows.add(cells);
			
			row_start = raw_data.indexOf("<tr", row_end);
			row_end = raw_data.indexOf("</tr>", row_start);
		}
		
		return rows;
	}
}
