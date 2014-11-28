package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import net.zomis.ConnBlocks;
import net.zomis.connblocks.gdx.mapload.TMXLevelSet;
import net.zomis.connblocks.levels.BlockLevelSet;
import net.zomis.connblocks.levels.RealPack;
import net.zomis.connblocks.levels.TestPack;
import net.zomis.connblocks.levels.TutorialLevels;

/**
 * Created by Zomis on 2014-11-17.
 */
public class LevelsetPickScreen implements Screen {

    private final ConnectingGame game;
    private final Table table = new Table();
    private int levelsets = 0;

    public LevelsetPickScreen(ConnectingGame game) {
        this.game = game;

        table.setFillParent(true);
        addLevelset(new TutorialLevels());
        addLevelset(new TestPack());
        addLevelset(new RealPack());

        FileHandle levels = Gdx.files.internal("levels/");
        scanLevels(levels);
        if (Gdx.files.isExternalStorageAvailable()) {
            FileHandle external = Gdx.files.external("connBlocks/levels/");
            scanLevels(external);
            external.mkdirs();
        }
    }

    private void scanLevels(FileHandle directory) {
        ConnBlocks.log("Scanning directory " + directory);
        if (!directory.exists()) {
            ConnBlocks.log(directory.file().getAbsolutePath() + " does not exist.");
            return;
        }

        for (FileHandle handle : directory.list()) {
            if (handle.isDirectory()) {
                addLevelset(new TMXLevelSet(handle));
            }
        }
    }

    private void addLevelset(final BlockLevelSet levelSet) {
        levelsets++;
        TextButton button = new TextButton(levelSet.getLevelSetName(), game.skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadLevelset(levelSet);
            }
        });
        table.add(button).expand().fill();

        if (levelsets % 3 == 0) {
            table.row();
        }
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        game.hudStage.addActor(table);
    }

    @Override
    public void hide() {
        table.remove();
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
