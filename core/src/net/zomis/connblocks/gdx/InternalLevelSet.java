package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import net.zomis.connblocks.levels.BlockLevelSet;

/**
 * Created by Zomis on 2014-11-16.
 */
public class InternalLevelSet implements BlockLevelSet {
    private final String name;
    private final int count;

    public InternalLevelSet(String name) {
        this.name = name;
        FileHandle directory = Gdx.files.internal("levels/" + name);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException(name + " is not a valid internal levelset");
        }
        this.count = directory.list().length;
    }

    @Override
    public String getLevel(int i) {
        return "levels/" + name + "/" + name + "-" + i;
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
