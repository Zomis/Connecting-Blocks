package net.zomis.connblocks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.zomis.connblocks.Base64Tool;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.ConnectingGame;
import net.zomis.connblocks.gdx.GameHelper;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class DesktopLauncher implements GameHelper {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new ConnectingGame(new DesktopLauncher()), config);
	}

    @Override
    public BlockMap loadLevel(String level) {
        return Base64Tool.loadLevel(level);
    }

}
