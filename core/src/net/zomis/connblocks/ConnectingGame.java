package net.zomis.connblocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import net.zomis.ConnBlocks;
import net.zomis.Direction4;
import net.zomis.connblocks.gdx.GameHelper;
import net.zomis.connblocks.gdx.MainScreen;
import net.zomis.connblocks.levels.BlockLevelSet;
import net.zomis.connblocks.levels.TutorialLevels;

public class ConnectingGame extends Game {
    private static final float STAGE_WIDTH = 800;
    private static final float STAGE_HEIGHT = 480;

    public final GameHelper helper;

    public OrthographicCamera camera;
    public OrthographicCamera hudCamera;
    public SpriteBatch batch;
    public SpriteBatch hudBatch;
    public Stage stage;
    public Stage hudStage;
    public Skin skin;
    public InputMultiplexer inputHandler = new InputMultiplexer();
    private int level = 0;

    private BlockMap currentGame;
    private MainScreen mainScreen;
    private BlockLevelSet levelset = new TutorialLevels();
    private CheckBox panMode;

    public ConnectingGame(GameHelper helper) {
        this.helper = helper;
    }

    @Override
	public void create () {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        camera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        camera.setToOrtho(false, STAGE_WIDTH, STAGE_HEIGHT);

        hudCamera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        hudCamera.setToOrtho(false, STAGE_WIDTH, STAGE_HEIGHT);

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        stage = new Stage(new FitViewport(STAGE_WIDTH, STAGE_HEIGHT, camera), batch);
        hudStage = new Stage(new StretchViewport(STAGE_WIDTH, STAGE_HEIGHT, hudCamera), hudBatch);

        inputHandler.addProcessor(hudStage);
        inputHandler.addProcessor(stage);
        inputHandler.addProcessor(new GestureDetector(new PinchZoomer(camera)));
        final CameraPanner cameraPanner = new CameraPanner(camera);
        cameraPanner.setEnabled(false);
        inputHandler.addProcessor(new GestureDetector(cameraPanner));
        ConnectionMover connectionMover = new ConnectionMover(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new Dialog("Map Finished", skin) {
                    @Override
                    protected void result(Object object) {
                        super.result(object);
                        nextLevel();
                    }
                };
                dialog.button("OK", true);
                dialog.show(stage);
            }
        });
        inputHandler.addProcessor(new GestureDetector(connectionMover));
        Gdx.input.setInputProcessor(inputHandler);

        mainScreen = new MainScreen(this, connectionMover, levelset);
        setScreen(mainScreen);
        currentGame = mainScreen.getMap();

        Table table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        table.bottom();

        TextButton reset = new TextButton("Reset", skin);
        table.add(reset).expandX().left().bottom();
        reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainScreen.setMap(helper.loadLevel(levelset.getLevel(level)));
            }
        });

        panMode = new CheckBox("Pan", skin);
        table.add(panMode).expandX().right().bottom();
        panMode.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cameraPanner.setEnabled(panMode.isChecked());
            }
        });

        hudStage.addActor(table);


    }

    private void nextLevel() {
        level++;
        if (levelset.getLevelCount() > level) {
            mainScreen.setMap(helper.loadLevel(levelset.getLevel(level)));
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        super.render();
		batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hudStage.draw();
    }
}
