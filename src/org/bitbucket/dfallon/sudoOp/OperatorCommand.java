package org.bitbucket.dfallon.sudoOp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OperatorCommand {
	private final Player sender;
	private CommandSender overseer = null;
	private final String command;
	private Date completionTime = null;
	private int status; //0 = unapproved 1 = approved 2 = canceled 3 = denied
	
	public OperatorCommand(Player sender, String command){
		this.sender = sender;
		this.command = command;
		this.status = 0;
	}
	
	public CommandSender getOverseer() {
		return overseer;
	}

	public void approve(CommandSender approver) {
		if(!approver.equals(sender)){
			this.overseer = approver;
			status = 1;
			completionTime = new Date();
		}
	}
	
	public void deny(CommandSender denier){
		this.overseer = denier;
		status = 3;
		completionTime = new Date();
		
	}
	public void cancel(){
		status = 2;
		completionTime = new Date();
	}
	
	

	public Date getCompletionTime() {
		return completionTime;
	}

	public Player getSender() {
		return sender;
	}

	public String getCommand() {
		return command;
	}

	public boolean isCompleted(){
		if(completionTime==null)
			return false;
		else
			return true;
	}
	
	public boolean isApproved(){
		if(overseer == null)
			return false;
		else
			return true;
	}
	@Override
	public String toString(){
		switch(status){
		case 0:
			return "["+formatDate(new Date())+"]"
					+"[Unapproved]"+getSender().getName()
					+": /"+getCommand();
		case 1:
			if(getOverseer() != null && getCompletionTime() != null)
				return "["+formatDate(getCompletionTime())+"]"
					+"[Approved: "+getOverseer().getName()+"]"
					+getSender().getName()+": /"+getCommand();
		case 2:
			if(getCompletionTime() != null)
				return "["+formatDate(getCompletionTime())+"]"
					+"[Cancelled]"+getSender().getName()
					+": "+getCommand();
		case 3:
			if(getOverseer() != null && getCompletionTime() != null)
				return "["+formatDate(getCompletionTime())+"]"
					+"[Denied: "+getOverseer().getName()+"]"
					+getSender().getName()+": /"+getCommand();
		}
		return "malformed command";
	}
	
	private String formatDate(Date d){	
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(d);
	}
	
}	
