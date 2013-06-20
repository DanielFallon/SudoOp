package org.bitbucket.dfallon.sudoOp.commands;

import java.io.IOException;

import org.bitbucket.dfallon.sudoOp.OperatorCommand;
import org.bitbucket.dfallon.sudoOp.SudoOp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandSudoDeny extends SafeCommandExcecutor {

	public CommandSudoDeny(SudoOp plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2)
			return false;
		if (plugin.openCommands.containsKey(args[1])
				&& plugin.openCommands.get(args[1]).getSender().getName()
						.equalsIgnoreCase(args[0])) {
			OperatorCommand opcmd = plugin.openCommands.get(args[1]);
			opcmd.deny(sender);
			try {
				plugin.sudolog.addLine(opcmd.toString());
			} catch (IOException e) {
				errorCancelCommand(sender, args[1]);
				return true;
			}
			//notify players
			opcmd.getSender().sendMessage(
					opcmd.getOverseer().getName()+" denied your command:\n"+
					"\"/"+opcmd.getCommand()+"\"");
			opcmd.getOverseer().sendMessage(
					"you denied "+opcmd.getSender().getName()+"'s command:\n" +
					"\"/"+opcmd.getCommand()+"\"");
			return true;
		}

		return false;
	}

}
