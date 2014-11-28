package net.zomis.connblocks.gdx.mapload;

import com.badlogic.gdx.files.FileHandle;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.gdx.mapload.MapLoader;
import net.zomis.connblocks.levels.BlockLevelSet;

/**
 * Created by Zomis on 2014-11-16.
 */
public class TMXLevelSet implements BlockLevelSet {
    private final String name;
    private final int count;
    private final FileHandle directory;

    public TMXLevelSet(FileHandle directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " is not a valid levelset directory");
        }
        this.name = directory.name();
        this.directory = directory;
        this.count = directory.list().length;
    }

    @Override
    public String getLevelData(int i) {
        return null;
    }

    @Override
    public BlockMap getLevel(int i) {
        return new MapLoader().load(directory.child(name + "-" + (i + 1) + ".tmx"));
    }

    @Override
    public String getLevelSetName() {
        return name;
    }

    @Override
    public int getLevelCount() {
        return count;
    }
}
