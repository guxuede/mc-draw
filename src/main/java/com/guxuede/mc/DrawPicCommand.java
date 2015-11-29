package com.guxuede.mc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.guxuede.mc.loader.ImageObjectLoader;
import com.guxuede.mc.loader.NotSupportResource;
import com.guxuede.mc.loader.PaintPointBox;
import com.guxuede.mc.printer.CoordinateTransformPrinter;


public class DrawPicCommand implements CommandExecutor {
	
	private JavaPlugin javaPlugin;
	
	public DrawPicCommand(JavaPlugin javaPlugin) {
		this.javaPlugin = javaPlugin;
	}
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        if(split.length < 1){
        	player.sendMessage("Error:Arg 0 : filePath is required.");
        	return false;
        }
        String filePath = split[0];
        int drawClass = (split == null || split.length < 2)?0:Integer.parseInt(split[1]);
        int rotate = (split == null || split.length < 3)?0:Integer.parseInt(split[2]);
        
        player.sendMessage("Drawing...");
        try(FileInputStream in = new FileInputStream(new File(filePath))) {
            ImageObjectLoader loader = new ImageObjectLoader();
            PaintPointBox paintPointBox = loader.load(in);
            CoordinateTransformPrinter printer = new CoordinateTransformPrinter(paintPointBox, javaPlugin, player);
            printer.setType(drawClass);
            printer.roate = rotate;
            printer.print();
		} catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("Error:" + e.getMessage());
		} catch (NotSupportResource e) {
            e.printStackTrace();
            player.sendMessage("Error:" + e.getMessage());
        }
        return true;
    }

}