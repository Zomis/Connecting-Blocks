package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockType;
import net.zomis.connblocks.ConnectingGame;
import net.zomis.connblocks.ConnectionMover;
import net.zomis.connblocks.levels.TutorialLevels;

/**
 * Created by Zomis on 2014-11-12.
 */
public class MainScreen implements Screen {

    private final BlockMap map;
    private final BlockMapRenderer renderer;
    private final ConnectingGame game;
    private final ConnectionMover mover;

    public MainScreen(ConnectingGame game, ConnectionMover connectionMover) {
        this.map = game.helper.loadLevel(new TutorialLevels().getLevel(0));
        // map = new BlockMap(16, 16);
        this.game = game;
        this.mover = connectionMover;
        renderer = new BlockMapRenderer(map);

        mover.setConnection(map.getConnections().iterator().next());
//        new Mesh()
//        new VertexAttribute()
    }

    @Override
    public void render(float delta) {


        renderer.render(game.batch, game.camera.combined, mover.getConnection());

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

    public BlockMap getMap() {
        return map;
    }
}
