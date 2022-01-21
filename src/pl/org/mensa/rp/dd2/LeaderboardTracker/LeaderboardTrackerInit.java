package pl.org.mensa.rp.dd2.LeaderboardTracker;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeaderboardTrackerInit {
	public static void main(String args[]) throws IOException {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		try {
			LeaderboardTrackerApp app = new LeaderboardTrackerApp();
			
			scheduler.scheduleAtFixedRate(app, 0, 1, TimeUnit.HOURS);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
}
