package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockType;
import net.zomis.connblocks.PinchZoomer;
import net.zomis.connblocks.gdx.mapload.MapLoadingException;
import net.zomis.connblocks.gdx.mapload.TMXLevelSet;
import net.zomis.connblocks.levels.BlockLevelSet;

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

    private MainScreen mainScreen;
    private BlockLevelSet levelset;
    private CheckBox panMode;
    private Texture bg;
    private ConnectionMover connectionMover;
    private final DesktopKeyboard desktopKeyboard = new DesktopKeyboard(this);
    private Label levelInfoLabel;
    private Label authorLabel;
    private Label levelNameLabel;

    public ConnectingGame(GameHelper helper) {
        this.helper = helper;
    }

    @Override
	public void create () {
        levelset = new TMXLevelSet(Gdx.files.internal("levels/tutorial"));

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        camera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        camera.setToOrtho(true, STAGE_WIDTH, STAGE_HEIGHT);

        hudCamera = new OrthographicCamera(STAGE_WIDTH, STAGE_HEIGHT);
        hudCamera.setToOrtho(false, STAGE_WIDTH, STAGE_HEIGHT);

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        stage = new Stage(new FitViewport(STAGE_WIDTH, STAGE_HEIGHT, camera), batch);
        hudStage = new Stage(new StretchViewport(STAGE_WIDTH, STAGE_HEIGHT, hudCamera), hudBatch);
        bg = new Texture("bg.png");

        inputHandler.addProcessor(hudStage);
        inputHandler.addProcessor(stage);
        inputHandler.addProcessor(new GestureDetector(new PinchZoomer(camera)));
        final CameraPanner cameraPanner = new CameraPanner(camera);
        cameraPanner.setEnabled(false);
        inputHandler.addProcessor(new GestureDetector(cameraPanner));
        connectionMover = new ConnectionMover(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new Dialog("Map Finished", skin) {
                    @Override
                    protected void result(Object object) {
                        nextLevel();
                    }
                };
                dialog.key(Input.Keys.ENTER, true);
                dialog.button("OK", true);
                dialog.show(hudStage);
            }
        });
        inputHandler.addProcessor(new GestureDetector(connectionMover));
        inputHandler.addProcessor(desktopKeyboard);
        Gdx.input.setInputProcessor(inputHandler);

        setScreen(new LevelsetPickScreen(this));

        Table levelInfoTable = new Table();
        levelInfoTable.setFillParent(true);
        levelInfoTable.top().left();
        levelInfoLabel = new Label("level", skin);
        levelInfoTable.add(levelInfoLabel).left().row();
        authorLabel = new Label("Author", skin);
        levelInfoTable.add(authorLabel).left().row();
        levelNameLabel = new Label("Level Name", skin);
        levelInfoTable.add(levelNameLabel).left().row();

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();

        TextButton reset = new TextButton("Reset", skin);
        table.add(reset).expandX().left().bottom();
        reset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetMap();
            }
        });

        TextButton nextLevel = new TextButton("NEXT!", skin);
        table.add(nextLevel).expandX().left().bottom();
        nextLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextLevel();
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

        hudStage.addActor(levelInfoTable);
        hudStage.addActor(table);
    }

    public void backToMenu() {
        mainScreen = null;
        setScreen(new LevelsetPickScreen(this));
    }

    public void resetMap() {
        mainScreen.setMap(loadLevel(levelset, level));
    }

    public void nextLevel() {
        level++;
        if (levelset.getLevelCount() > level) {
            mainScreen.setMap(loadLevel(levelset, level));
        }
    }

    public void previousLevel() {
        if (level <= 0) {
            return;
        }
        level--;
        if (levelset.getLevelCount() > level) {
            mainScreen.setMap(loadLevel(levelset, level));
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
        desktopKeyboard.checkInputs();
        camera.update();
        hudCamera.update();
        batch.setProjectionMatrix(camera.combined);
        hudBatch.setProjectionMatrix(hudCamera.combined);
        hudBatch.begin();
        hudBatch.draw(bg, 0, 0, STAGE_WIDTH, STAGE_HEIGHT);
        hudBatch.end();
        super.render();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        hudStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        hudStage.draw();
    }

    public BlockMap loadLevel(BlockLevelSet levelSet, int level) {
        levelInfoLabel.setText(levelSet.getLevelSetName() + " - Level " + (level + 1));
        try {
            BlockMap map = levelset.getLevel(level);
            if (map == null) {
                map = helper.loadLevel(levelSet.getLevelData(level));
                if (map == null) {
                    throw new RuntimeException("Incompatible Levelset and platform");
                }
            }
            return map;
        }
        catch (RuntimeException ex) {
            Dialog dialog = new Dialog("Error loading map", skin);
            dialog.text(ex.getMessage());
            dialog.button("OK", true);
            dialog.key(Input.Keys.ENTER, true);
            dialog.show(hudStage);
            if (mainScreen != null) {
                return mainScreen.getMap();
            }
            BlockMap errorMap = new BlockMap(10, 4);
            errorMap.addConnection(errorMap.pos(1, 1));
            errorMap.pos(8, 1).setType(BlockType.GOAL);
            errorMap.pos(8, 2).setType(BlockType.GOAL);
            errorMap.pos(7, 1).setType(BlockType.IMPASSABLE);
            errorMap.pos(7, 2).setType(BlockType.IMPASSABLE);
            return errorMap;
        }
    }

    public void loadLevelset(BlockLevelSet levelSet) {
        levelset = levelSet;
        mainScreen = new MainScreen(this, connectionMover, levelSet);
        setScreen(mainScreen);
    }

    public ConnectionMover getConnectionMover() {
        return connectionMover;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }
}
