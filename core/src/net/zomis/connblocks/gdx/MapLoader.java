package net.zomis.connblocks.gdx;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import net.zomis.ConnBlocks;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.BlockType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Zomis on 2014-11-16.
 */
public class MapLoader {

    private static final int TILE_SIZE = 32;

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
        tiled.dispose();
        return map;
    }

    private void loadLayer(TiledMap tiled, BlockMap result, MapLayer layer) {
        if (layer.getName().equals("map")) {
            loadMapLayer(tiled, result, (TiledMapTileLayer) layer);
        }
        else if (layer.getName().toLowerCase().startsWith("conn")) {
            loadConnections(tiled, result, layer);
        }
        else if (layer.getName().toLowerCase().startsWith("spec")) {
            loadSpecials(tiled, result, layer);
        }
    }

    private void loadSpecials(TiledMap tiled, BlockMap result, MapLayer layer) {

    }

    private void loadConnections(TiledMap tiled, BlockMap result, MapLayer layer) {
        Iterator<MapObject> it = layer.getObjects().iterator();
        while (it.hasNext()) {
            MapObject obj = it.next();
            Set<BlockTile> blocks = tilesForObject(result, obj);
            result.addConnection(blocks.toArray(new BlockTile[blocks.size()]));
        }
    }

    private Set<BlockTile> tilesForObject(BlockMap map, MapObject obj) {
        Set<BlockTile> result = new HashSet<BlockTile>();
        if (obj instanceof PolygonMapObject) {
            PolygonMapObject poly = (PolygonMapObject) obj;
            Polygon polygon = poly.getPolygon();
            Rectangle bounds = polygon.getBoundingRectangle();
            checkBounds(obj.getName(), bounds);
            for (int xx = (int) bounds.x + 1; xx < bounds.x + bounds.width; xx += TILE_SIZE) {
                int x = xx / TILE_SIZE;
                for (int yy = (int) bounds.y + 1; yy < bounds.y + bounds.height; yy += TILE_SIZE) {
                    int y = map.getMapHeight() - 1 - yy / TILE_SIZE;
                    if (polygon.contains(xx, yy)) {
                        result.add(map.pos(x, y));
                    }
                }
            }
            return result;
        }
        if (obj instanceof RectangleMapObject) {
            RectangleMapObject rect = (RectangleMapObject) obj;
            Rectangle bounds = rect.getRectangle();
            checkBounds(obj.getName(), bounds);
            for (int xx = (int) bounds.x; xx < bounds.x + bounds.width; xx += TILE_SIZE) {
                int x = xx / TILE_SIZE;
                for (int yy = (int) bounds.y; yy < bounds.y + bounds.height; yy += TILE_SIZE) {
                    int y = map.getMapHeight() - 1 - yy / TILE_SIZE;
                    result.add(map.pos(x, y));
                }
            }

            return result;
        }
        throw new RuntimeException("Unsupported MapObject class: " + obj + " only Polygon and Rectangle are supported");
    }

    private void checkBounds(String name, Rectangle bounds) {
        checkMod(name, bounds.x, TILE_SIZE);
        checkMod(name, bounds.y, TILE_SIZE);
        checkMod(name, bounds.width, TILE_SIZE);
        checkMod(name, bounds.height, TILE_SIZE);
    }

    private void checkMod(String name, float value, int mod) {
        if (value % mod != 0) {
            throw new RuntimeException("Invalid value for '" + name + "': Expected divisibilty by " + mod + " but was " + value);
        }
    }

    private void loadMapLayer(TiledMap tiled, BlockMap result, TiledMapTileLayer layer) {
        BlockType[] blockTypes = BlockType.values();
        int height = result.getMapHeight();
        for (int x = 0; x < result.getMapWidth(); x++) {
            for (int y = 0; y < result.getMapHeight(); y++) {
                int blocksMapY = height - 1 - y;
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null || cell.getTile() == null) {
                    result.pos(x, blocksMapY).setType(null);
                    continue;
                }
                int id = cell.getTile().getId();
                if (id >= blockTypes.length) {
                    throw new RuntimeException("Tile id out of range: " + id + " at pos " + x + ", " + y);
                }
                result.pos(x, blocksMapY).setType(blockTypes[id]);
            }
        }
    }

    private void checkValue(Object properties, String key, String expected) {
        if (properties instanceof MapProperties) {
            MapProperties prop = (MapProperties) properties;
            String actual = String.valueOf(prop.get(key));
            check(key, actual, expected);
            return;
        }
        throw new UnsupportedOperationException("Unsupported properties: " + properties);
    }

    private void check(String key, String actual, String expected) {
        if (!expected.equals(actual)) {
            throw new RuntimeException("Invalid value for '" + key + "': Expected " + expected + " but was " + actual);
        }
    }

}
