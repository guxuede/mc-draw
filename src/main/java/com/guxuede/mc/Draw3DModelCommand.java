package com.guxuede.mc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.guxuede.mc.loader.ModelObjectLoader;
import com.guxuede.mc.loader.NotSupportResource;
import com.guxuede.mc.loader.PaintPointBox;
import com.guxuede.mc.printer.CoordinateTransformPrinter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Draw3DModelCommand implements CommandExecutor {
	
	private JavaPlugin javaPlugin;
	
	public Draw3DModelCommand(JavaPlugin myPlugin) {
		this.javaPlugin = myPlugin;
	}
	
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player player = (Player) sender;
        if(split.length < 1){
            player.sendMessage("Error:Arg 0 : filePath is required.");
            return false;
        }
        final String filePath = split[0];
        final int material = split.length < 2?4:Integer.parseInt(split[1]);
        final int faceVertex = split.length < 3?0:Integer.parseInt(split[2]);
        final int drawClass = split.length < 4?0:Integer.parseInt(split[3]);
        final int rotate = split.length < 5?0:Integer.parseInt(split[4]);
        System.out.println(material+","+faceVertex+","+drawClass+","+rotate);
        player.sendMessage("Analysis...");
        new BukkitRunnable(){
            @Override
            public void run() {
                PaintPointBox paintPointBox = null;
                try {
                    paintPointBox = new ModelObjectLoader(faceVertex,material).load(new FileInputStream(new File(filePath)));
                } catch (NotSupportResource notSupportResource) {
                    notSupportResource.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CoordinateTransformPrinter printer = new CoordinateTransformPrinter(paintPointBox, javaPlugin, player);
                printer.setType(drawClass);
                printer.roate = rotate;
                printer.print();
            }
        }.runTaskAsynchronously(javaPlugin);
        return true;
    }


}