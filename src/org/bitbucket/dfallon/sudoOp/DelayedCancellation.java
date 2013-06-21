package org.bitbucket.dfallon.sudoOp;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;

public class DelayedCancellation extends BukkitRunnable {
	private SudoOp plugin;
	private String approvalCode;
	private OperatorCommand opcmd;
	public DelayedCancellation(SudoOp p, String approvalCode, OperatorCommand o){
		opcmd = o;
		this.approvalCode = approvalCode;
		plugin = p;
	}
	@Override
	public void run() {
		// cancel command
		opcmd.cancel();
		//log action
		try {
			plugin.sudolog.addLine(opcmd.toString());
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Could not add line to log file, canceling command");
			OperatorCommand opcmd = plugin.openCommands.get(approvalCode);
			opcmd.cancel();
			String message = "Could not create log of command, cancelling command\n"+opcmd.toString();
			opcmd.getSender().sendMessage(message);
			opcmd.getSender().sendMessage(message);
			plugin.openCommands.remove(approvalCode);
			return;
		}
		// notify user
		opcmd.getSender().sendMessage("Your command has been cancelled because the noone has responded to you request:\n"
								+ "\"/" + opcmd.getCommand() + "\"");
		plugin.openCommands.remove(approvalCode);
		
	}

}
