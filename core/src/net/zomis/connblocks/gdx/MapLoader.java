package net.zomis.connblocks.gdx;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import net.zomis.ConnBlocks;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockType;

import java.util.Iterator;

/**
 * Created by Zomis on 2014-11-16.
 */
public class MapLoader {

    public MapLoader() {
    }

    public BlockMap load(String fileName) {
        TmxMapLoader loader = new MyTmxMapLoader();
        TiledMap tiled = loader.load(fileName + ".tmx");

        Iterator<String> it = tiled.getProperties().getKeys();
        while (it.hasNext()) {
            ConnBlocks.log(it.next());
        }
        checkValue(tiled.getProperties(), "tilewidth", "32");
        checkValue(tiled.getProperties(), "tileheight", "32");

        BlockMap map = new BlockMap(tiled.getProperties().get("width", Integer.class), tiled.getProperties().get("height", Integer.class));

        Iterator<MapLayer> layers = tiled.getLayers().iterator();
        while (layers.hasNext()) {
            loadLayer(tiled, map, layers.next());
        }


        map.pos(1, 1).setType(BlockType.GOAL);
        map.addConnection(map.pos(2, 2));
        tiled.dispose();
        return map;
    }

    private void loadLayer(TiledMap tiled, BlockMap result, MapLayer layer) {
        if (layer.getName().equals("map")) {
            loadMapLayer(tiled, result, (TiledMapTileLayer) layer);
        }
        else if (layer.getName().toLowerCase().startsWith("conn")) {

        }
        else if (layer.getName().toLowerCase().startsWith("spec")) {

        }
    }

    private void loadMapLayer(TiledMap tiled, BlockMap result, TiledMapTileLayer layer) {

    }

    private void checkValue(Object properties, String key, String expected) {
        if (properties instanceof MapProperties) {
            MapProperties prop = (MapProperties) properties;
            String actual = String.valueOf(prop.get(key));
            check(key, actual, expected);
        }



    }

    private void check(String key, String actual, String expected) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("Invalid value for '" + key + "': Expected " + expected + " but was " + actual);
        }
    }

}
