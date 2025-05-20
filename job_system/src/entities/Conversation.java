package entities;

import java.util.Date;
import java.util.List;

public class Conversation {
	public Date startTime_;
	public String conversationStarter_;
	public List<String> channels_;	//Channels
	
	public Conversation(
			Date startTime,
			String conversationStarter,
			List<String> channels) {
		
		this.conversationStarter_ = conversationStarter;
		this.startTime_ = startTime;
		this.channels_ = channels;
	}
}
