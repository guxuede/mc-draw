package com.guxuede.mc.loader;

import java.io.IOException;
import java.io.InputStream;


public interface ObjectLoader {

	public PaintPointBox load(InputStream in) throws NotSupportResource,IOException; 
}
