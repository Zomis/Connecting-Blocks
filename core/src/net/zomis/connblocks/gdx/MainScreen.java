package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockType;
import net.zomis.connblocks.ConnectingGame;
import net.zomis.connblocks.levels.TutorialLevels;

/**
 * Created by Zomis on 2014-11-12.
 */
public class MainScreen implements Screen {

    private final BlockMap map;
    private final BlockMapRenderer renderer;
    private final ConnectingGame game;

    public MainScreen(ConnectingGame game) {
        this.map = game.helper.loadLevel(new TutorialLevels().getLevel(0));
        // map = new BlockMap(16, 16);
        this.game = game;
        renderer = new BlockMapRenderer(map);


/*
        map.addConnection(map.pos(3, 3), map.pos(3, 4)).setConnectGroups(3);
        map.addConnection(map.pos(7, 6), map.pos(7, 7)).setConnectGroups(1);
        map.addConnection(map.pos(5, 8), map.pos(5, 9)).setConnectGroups(2);

        map.pos(7, 1).setType(BlockType.IMPASSABLE);
        map.pos(3, 2).setType(BlockType.IMPASSABLE);
        map.pos(2, 1).setType(BlockType.IMPASSABLE);
        map.pos(4, 4).setType(BlockType.IMPASSABLE);
        map.pos(6, 6).setType(BlockType.IMPASSABLE);
        map.pos(8, 8).setType(BlockType.IMPASSABLE);
        map.pos(10, 10).setType(BlockType.IMPASSABLE);
        map.pos(12, 12).setType(BlockType.IMPASSABLE);
        map.pos(14, 14).setType(BlockType.IMPASSABLE);
        map.pos(15, 15).setType(BlockType.IMPASSABLE);*/
//        new Mesh()
//        new VertexAttribute()
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
