package org.bitbucket.dfallon.sudoOp.commands;

import java.util.logging.Level;

import org.bitbucket.dfallon.sudoOp.OperatorCommand;
import org.bitbucket.dfallon.sudoOp.SudoOp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

abstract class SafeCommandExcecutor implements CommandExecutor{
	protected SudoOp plugin;
	
	public SafeCommandExcecutor(SudoOp plugin){
		this.plugin = plugin;
	}
	abstract boolean run(CommandSender sender, Command cmd, String label, String[] args);
	@Override
	public boolean onCommand(CommandSender sender,
			Command cmd, String label, String[] args) {
		if(sender.hasPermission(cmd.getPermission())){
			return run(sender, cmd, label, args);
		}else{
			return false;
		}
	}
	
	protected Player getPlayerOrNull(CommandSender sender){
		if(sender instanceof Player){
			return (Player)sender;
		}else{
			return null;
		}
	}
	protected  void errorCancelCommand(CommandSender sender, String approvalCode){
		plugin.getLogger().log(Level.SEVERE, "Could not add line to log file, canceling command");
		OperatorCommand opcmd = plugin.openCommands.get(approvalCode);
		opcmd.cancel();
		String message = "Could not create log of command, cancelling command\n"+opcmd.toString();
		sender.sendMessage(message);
		opcmd.getSender().sendMessage(message);
		plugin.openCommands.remove(approvalCode);
		
	}
	

}
