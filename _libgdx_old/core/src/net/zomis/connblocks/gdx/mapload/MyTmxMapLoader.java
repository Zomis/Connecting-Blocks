package net.zomis.connblocks.gdx.mapload;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;

/**
 * Created by Zomis on 2014-11-16.
 */
public class MyTmxMapLoader extends TmxMapLoader {

    private final FileHandle handle;
    private final FileHandle file;

    public MyTmxMapLoader(FileHandle fileHandle, FileHandle tilesFile) {
        this.handle = tilesFile;
        this.file = fileHandle;
    }

    @Override
    public FileHandle resolve(String fileName) {
        return file;
    }

    @Override
    protected Array<FileHandle> loadTilesets(XmlReader.Element root, FileHandle tmxFile) throws IOException {
        return new Array<FileHandle>(new FileHandle[] { handle });
    }

    @Override
    protected TiledMap loadTilemap(XmlReader.Element root, FileHandle tmxFile, ImageResolver imageResolver) {
        return super.loadTilemap(root, tmxFile, new MyImageResolver());
    }

    private class MyImageResolver implements ImageResolver {

        @Override
        public TextureRegion getImage(String name) {
            return new TextureRegion(new Texture(handle));
        }
    }
}
