package org.bitbucket.dfallon.sudoOp;

import java.util.HashMap;
import java.util.List;

import org.bitbucket.dfallon.sudoOp.commands.CommandSudoApprove;
import org.bitbucket.dfallon.sudoOp.commands.CommandSudoCancel;
import org.bitbucket.dfallon.sudoOp.commands.CommandSudoDeny;
import org.bitbucket.dfallon.sudoOp.commands.CommandSudoOp;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SudoOp extends JavaPlugin implements Listener{
	
	public HashMap<String, OperatorCommand> openCommands = new HashMap<String, OperatorCommand>();
	public SudoLog sudolog;
	
	@Override
	public void onDisable() {
		super.onDisable();

	}

	@Override
	public void onEnable() {
		saveDefaultConfig();
		sudolog = new SudoLog(this, "log.txt");
		//deop leftover opped players
		@SuppressWarnings("unchecked")
		List<Object> madeOp = (List<Object>) getConfig().getList("madeOp");
		if(madeOp != null){
			for(Object o: madeOp){
				String p = (String) o;
				Player player = getServer().getPlayer(p);
				if(player != null){
					getLogger().info("Deopping "+player.getName());
					player.setOp(false);
				}
				
			}
		}
		//register command handlers
		getCommand("SudoOp").setExecutor(new CommandSudoOp(this));
		getCommand("SudoApprove").setExecutor(new CommandSudoApprove(this));
		getCommand("SudoCancel").setExecutor(new CommandSudoCancel(this));
		getCommand("SudoDeny").setExecutor(new CommandSudoDeny(this));
		
		//register event listener
		getServer().getPluginManager().registerEvents(this, this);
		super.onEnable();
	}
	
	public void onCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
		if(getConfig().getList("madeOp").contains(e.getPlayer().getName())){
			String msg = e.getMessage();
			String cmd = msg.substring(1, msg.indexOf(" "));
			if(!getConfig().getList("whiteListedCommands").contains(cmd)){
				e.setCancelled(true);
			}
		}
	}
}
