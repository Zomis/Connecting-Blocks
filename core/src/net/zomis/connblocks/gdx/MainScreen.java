package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Screen;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockType;
import net.zomis.connblocks.ConnectingGame;

/**
 * Created by Zomis on 2014-11-12.
 */
public class MainScreen implements Screen {

    private final BlockMap map;
    private final BlockMapRenderer renderer;
    private final ConnectingGame game;

    public MainScreen(ConnectingGame game) {
        this.map = new BlockMap(16, 16);
        this.game = game;
        renderer = new BlockMapRenderer(map);

        map.addConnection(map.pos(3, 3), map.pos(3, 4));

        map.pos(7, 1).setType(BlockType.IMPASSABLE);
        map.pos(3, 2).setType(BlockType.IMPASSABLE);
        map.pos(2, 1).setType(BlockType.IMPASSABLE);
        map.pos(4, 4).setType(BlockType.IMPASSABLE);
    }

    @Override
    public void render(float delta) {


        renderer.render(game.batch, game.camera.combined);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
