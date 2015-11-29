package com.guxuede.mc.loader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.bukkit.DyeColor;
import org.bukkit.Material;

import com.guxuede.math.Vector3;

public class ImageObjectLoader implements ObjectLoader {

	public static final Color EMPTY_TRANSLUCENT = new Color(0, 0, 0, 0);
	public static final List<Color> ALL_SUPPORT_COLOR = new ArrayList<Color>();
	public static final Map<Color, Pigment> colorBlockMap = new HashMap<Color, Pigment>();
	static {
		for (DyeColor dc : DyeColor.values()) {
			Pigment block = new Pigment(Material.WOOL, dc.getData());
			Color color = new Color(dc.getColor().asRGB());
			ALL_SUPPORT_COLOR.add(color);
			colorBlockMap.put(color, block);
		}
		Pigment blockAIR = new Pigment(Material.AIR, (byte) 0);
		ALL_SUPPORT_COLOR.add(EMPTY_TRANSLUCENT);
		colorBlockMap.put(EMPTY_TRANSLUCENT, blockAIR);
	}

	@Override
	public PaintPointBox load(InputStream in) throws NotSupportResource,
			IOException {
		PaintPointBox box = new PaintPointBox();
		BufferedImage image = ImageIO.read(in);
		int width = image.getWidth();
		int height = image.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color nearColor = getColorAtPoint(image, x, y);
				Pigment pen = colorBlockMap.get(nearColor);
				Vector3 loc = new Vector3( width-x, height-y, 0);
				PaintPoint point = new PaintPoint(pen, loc);
				box.addPoint((point));
			}
		}
		return box;
	}

	public static Color getColorAtPoint(BufferedImage bi, int x, int y) {
		ColorModel colorModel = bi.getColorModel();
		WritableRaster raster = bi.getRaster();
		Object o = raster.getDataElements(x, y, null);
		int r = colorModel.getRed(o);
		int g = colorModel.getGreen(o);
		int b = colorModel.getBlue(o);
		int a = colorModel.getAlpha(o);
		Color nearColor = getNearestColour(r, g, b, a);
		return nearColor;
	}

	public static Color getNearestColour(int r, int g, int b, int a) {
		if (a == 0) {
			return EMPTY_TRANSLUCENT;
		}
		Color color = null;
		if ((ALL_SUPPORT_COLOR != null) && (ALL_SUPPORT_COLOR.size() > 0)) {
			Color crtColor = null;
			int[] rgb = null;
			int diff = 0;
			int minDiff = 999;
			for (int i = 0; i < ALL_SUPPORT_COLOR.size(); i++) {
				crtColor = ALL_SUPPORT_COLOR.get(i);
				rgb = new int[3];
				rgb[0] = crtColor.getRed();
				rgb[1] = crtColor.getGreen();
				rgb[2] = crtColor.getBlue();
				diff = Math.abs(rgb[0] - r) + Math.abs(rgb[1] - g)
						+ Math.abs(rgb[2] - b);
				if (diff < minDiff) {
					minDiff = diff;
					color = crtColor;
				}
			}
		}
		if (color == null)
			color = EMPTY_TRANSLUCENT;
		return color;
	}
}
