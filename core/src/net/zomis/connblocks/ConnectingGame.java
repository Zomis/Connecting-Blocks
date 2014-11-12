package net.zomis.connblocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import net.zomis.ConnBlocks;
import net.zomis.Direction4;
import net.zomis.connblocks.gdx.GameHelper;
import net.zomis.connblocks.gdx.MainScreen;

public class ConnectingGame extends Game {
    private static final float STAGE_WIDTH = 800;
    private static final float STAGE_HEIGHT = 480;

    private float startingInitialDistance;
    private float startingZoom;

    public final GameHelper helper;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public Stage stage;
    public Skin skin;

    private BlockMap currentGame;

    public ConnectingGame(GameHelper helper) {
        this.helper = helper;
    }

    @Override
	public void create () {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        camera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        camera.setToOrtho(true, STAGE_WIDTH, STAGE_HEIGHT);

        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(STAGE_WIDTH, STAGE_HEIGHT, camera), batch);

        InputMultiplexer inputHandler = new InputMultiplexer();
        inputHandler.addProcessor(stage);
        inputHandler.addProcessor(new GestureDetector(new PinchZoomer(camera)));
        ConnectionMover connectionMover = new ConnectionMover(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new Dialog("Map Finished", skin);
                dialog.button("OK", true);
                dialog.show(stage);
            }
        });
        inputHandler.addProcessor(new GestureDetector(connectionMover));
//        inputHandler.addProcessor(new GestureDetector(new CameraPanner(camera)));
        Gdx.input.setInputProcessor(inputHandler);

        MainScreen mainScreen = new MainScreen(this, connectionMover);
        setScreen(mainScreen);
        currentGame = mainScreen.getMap();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.render();
		batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }
}
