package net.zomis.connblocks.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.gdx.ConnectingGame;
import net.zomis.connblocks.gdx.GameHelper;

public class HtmlLauncher extends GwtApplication implements GameHelper {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new ConnectingGame(this);
        }

    @Override
    public BlockMap loadLevel(String level) {
        return null;
    }
}