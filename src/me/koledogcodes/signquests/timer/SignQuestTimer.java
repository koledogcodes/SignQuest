package me.koledogcodes.signquests.timer;

import java.util.Timer;
import java.util.TimerTask;

import me.koledogcodes.signquests.SignQuest;

public class SignQuestTimer {
	
	public SignQuestTimer() {
	}
	
	public void registerNewRepeatingTimer(TimerTask task, long delay, long period){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, delay, period);
		SignQuest.activeTimers.add(timer);
		SignQuest.activeTasks.add(task);
	}
	
	public void registerNewNonRepeatingTimer(TimerTask task, long delay_unti_execute){
		Timer timer = new Timer();
		timer.schedule(task, delay_unti_execute);
		SignQuest.activeTimers.add(timer);
		SignQuest.activeTasks.add(task);
	}
	
	public void cancelAllTimers(boolean broadcast){
		if (broadcast){
			for (int i = 0; i < SignQuest.activeTimers.size(); i++){
				SignQuest.activeTimers.get(i).cancel();
				System.out.println("[SignQuests] SignQuestTimer #" + i + " has been cancelled.");
			}
		}
		else {
			for (int i = 0; i < SignQuest.activeTimers.size(); i++){
				SignQuest.activeTimers.get(i).cancel();
				SignQuest.activeTasks.get(i).cancel();
			}
		}
	}
}
