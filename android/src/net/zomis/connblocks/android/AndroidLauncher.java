package net.zomis.connblocks.android;

import android.os.Bundle;

import android.util.Base64;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.gdx.ConnectingGame;
import net.zomis.connblocks.gdx.GameHelper;

import java.io.IOException;

public class AndroidLauncher extends AndroidApplication implements GameHelper {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new ConnectingGame(this), config);
	}

    @Override
    public BlockMap loadLevel(String level) {
        try {
            if (!level.startsWith("{")) {
                level = new String(Base64.decode(level, Base64.DEFAULT), "UTF-8");
            }
            return BlockMap.mapper().readValue(level, BlockMap.class).onLoad();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
