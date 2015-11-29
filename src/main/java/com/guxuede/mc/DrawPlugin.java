package com.guxuede.mc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class DrawPlugin extends JavaPlugin {


	@Override
	public void onEnable() {
		// Register our events
		PluginManager pm = getServer().getPluginManager();
		// Register our commands
		getCommand("drawpic").setExecutor(new DrawPicCommand(this));
		getCommand("draw3d").setExecutor(new Draw3DModelCommand(this));
		// EXAMPLE: Custom code, here we just output some info so we can check
		// all is well
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(
				pdfFile.getName() + " version " + pdfFile.getVersion()
						+ " is enabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub
		return super.onCommand(sender, command, label, args);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
	}

	@Override
	public void onDisable() {
		// TODO: Place any custom disable code here

		// NOTE: All registered events are automatically unregistered when a
		// plugin is disabled

		// EXAMPLE: Custom code, here we just output some info so we can check
		// all is well
		getLogger().info("Goodbye world!");
	}
}