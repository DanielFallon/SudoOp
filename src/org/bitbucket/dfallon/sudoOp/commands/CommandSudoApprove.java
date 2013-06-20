package org.bitbucket.dfallon.sudoOp.commands;

import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import org.bitbucket.dfallon.sudoOp.OperatorCommand;
import org.bitbucket.dfallon.sudoOp.SudoOp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandSudoApprove extends SafeCommandExcecutor {

	public CommandSudoApprove(SudoOp plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length < 2)
			return false;
		if(plugin.openCommands.containsKey(args[1])
				&& plugin.openCommands.get(args[1]).getSender().getName().equalsIgnoreCase(args[0])){
			OperatorCommand opcmd = plugin.openCommands.get(args[1]);
			opcmd.approve(sender);
			try {
				plugin.sudolog.addLine(opcmd.toString());
			} catch (IOException e) {
				errorCancelCommand(sender, args[1]);
				return true;
			}
			//notify players
			opcmd.getSender().sendMessage(
					opcmd.getOverseer().getName()+" approved your command:\n"+
					"\"/"+opcmd.getCommand()+"\"");
			opcmd.getOverseer().sendMessage(
					"you approved "+opcmd.getSender().getName()+"'s command:\n" +
					"\"/"+opcmd.getCommand()+"\"");
			//notify players
			excecuteCommand(opcmd);
			plugin.openCommands.remove(args[1]);
			return true;
		}
		return false;
	}

	private void excecuteCommand(OperatorCommand opcmd) {
		//add player to config
		@SuppressWarnings("unchecked")
		List<String> madeOp = (List<String>) plugin.getConfig().getList("madeOp");
		if(madeOp == null)
			madeOp = new LinkedList<String>();
		madeOp.add(opcmd.getSender().getName());
		plugin.getConfig().set("madeOp", madeOp);
		plugin.saveConfig();
		//make them op
		opcmd.getSender().setOp(true);
		//execute command and notify player
		opcmd.getSender().sendMessage("Excecuting...\n\"/"+opcmd.getCommand()+"\"");
		plugin.getServer().dispatchCommand( opcmd.getSender(), opcmd.getCommand());
		//make them not op
		opcmd.getSender().setOp(false);
		//remove player from config
		madeOp.remove(opcmd.getSender().getName());
		plugin.getConfig().set("madeOp", madeOp);
		plugin.saveConfig();		
	}
}
