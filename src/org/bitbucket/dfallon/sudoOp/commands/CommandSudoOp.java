package org.bitbucket.dfallon.sudoOp.commands;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bitbucket.dfallon.sudoOp.DelayedCancellation;
import org.bitbucket.dfallon.sudoOp.OperatorCommand;
import org.bitbucket.dfallon.sudoOp.SudoOp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;

public class CommandSudoOp extends SafeCommandExcecutor{

	public CommandSudoOp(SudoOp plugin) {
		super(plugin);
	}

	@Override
	boolean run(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = getPlayerOrNull(sender);
		if( player != null){;
			if(args.length>0){
				String command="";
				for(String arg:args){
					command+=arg+" ";
				};
				//generate random key
				Random random = new Random();
				String key;
				int intKey;
				
				do{
					 intKey = random.nextInt(10000);
					 key = String.format("%04d", intKey);
				}while(plugin.openCommands.containsKey(key));
				//add to hashmap
				plugin.openCommands.put(key, new OperatorCommand(player, command));
				OperatorCommand opcmd = plugin.openCommands.get(key);
				notifyOverseers(key, opcmd);
				setupCancellation(opcmd, key);
				
			}
			
		}
		return false;
	}

	private void setupCancellation(OperatorCommand opcmd, String approvalCode) {
		//get delay in seconds
		long delay = (((int)plugin.getConfig().get("cancellationDelay"))*1000);
		//schedule cancelation
		plugin.getServer().getScheduler()
			.scheduleSyncDelayedTask(
				plugin, new DelayedCancellation(plugin, approvalCode, opcmd), delay);
	}

	private void notifyOverseers(String key, OperatorCommand opcmd) {
		//get list of players
		PluginManager pm = plugin.getServer().getPluginManager();
		Set<Permissible> notifiedPlayers = new HashSet<Permissible>();
		Set<Permissible> approvers = pm.getPermissionSubscriptions("SudoOp.Approve");
		Set<Permissible> deniers = pm.getPermissionSubscriptions("SudoOp.Deny");
		notifiedPlayers.addAll(approvers);
		notifiedPlayers.addAll(deniers);
		//add command sending player
		notifiedPlayers.add(opcmd.getSender());
		
		int numPlayersNotified = 0;
		for(Permissible p: notifiedPlayers){
			if(p instanceof CommandSender){
				CommandSender s = (CommandSender) p;
				if (!(s.equals(opcmd.getSender()))) {
					numPlayersNotified++;
					String message = opcmd.getSender().getName() + " wants to use: "
							+"\n\"/" + opcmd.getCommand()+"\"";
					if (s.hasPermission("SudoOp.Approve"))
						message = message + "\n" + "Send \"/SudoApprove " +
								opcmd.getSender().getName()+ " " + key + "\" to approve.";
					if (s.hasPermission("SudoOp.Deny"))
						message = message + "\n" + "Send \"/SudoDeny " +
								opcmd.getSender().getName() + " " + key + "\" to deny.";
					s.sendMessage(message);
				}else if(s.hasPermission("SudoOp.Cancel")){
					String message = "Command requested: "+opcmd.getCommand()
							+"\nSend \"/SudoCancel "+key+"\" to cancel.";
					s.sendMessage(message);
				}
			}
		}
		if(numPlayersNotified == 0)
			opcmd.getSender().sendMessage("No one is available right now to approve your request," +
					"\nit will remain open for 5 minutes.");
	}
	
}
