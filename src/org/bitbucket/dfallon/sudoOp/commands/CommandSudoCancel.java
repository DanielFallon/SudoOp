package org.bitbucket.dfallon.sudoOp.commands;

import java.io.IOException;

import org.bitbucket.dfallon.sudoOp.OperatorCommand;
import org.bitbucket.dfallon.sudoOp.SudoOp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandSudoCancel extends SafeCommandExcecutor {

	public CommandSudoCancel(SudoOp plugin) {
		super(plugin);
	}

	@Override
	boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1)
			return false;
		if (plugin.openCommands.containsKey(args[0])) {
			OperatorCommand opcmd = plugin.openCommands.get(args[0]);
			opcmd.cancel();
			try {
				plugin.sudolog.addLine(opcmd.toString());
			} catch (IOException e) {
				errorCancelCommand(sender, args[1]);
				return true;
			}
			if (sender.equals(opcmd.getSender())) {
				sender.sendMessage("you successfully cancelled your command:\n"
						+ "\"/" + opcmd.getCommand() + "\"");
			}
			return true;
		}
		return false;
	}

}
