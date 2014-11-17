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
import net.zomis.Direction4;
import net.zomis.connblocks.*;
import net.zomis.connblocks.move.*;

import java.util.*;

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
        Iterator<MapObject> it = layer.getObjects().iterator();
        int blockLink = -1;
        BlockTile blockLinkTile = null;
        while (it.hasNext()) {
            MapObject obj = it.next();
            Set<BlockTile> blocks = tilesForObject(result, obj);
            for (BlockTile tile : blocks) {
                int bl = setupSpecial(tile, obj, layer);
                if (bl != -1) {
                    if (blocks.size() != 1) {
                        throw new RuntimeException("Map Object `blockLink` with more than one block are not yet supported");
                    }
                    if (blockLink == -1) {
                        blockLink = bl;
                        blockLinkTile = tile;
                    }
                    else if (blockLink == bl) {
                        new BlockLink(blockLinkTile, tile);
                        blockLink = -1;
                        blockLinkTile = null;
                    }
                    else {
                        throw new RuntimeException("Too many `blockLink` on the same layer. Searched for " + blockLink +
                            " but found " + bl);
                    }
                }
            }
        }
        if (blockLink != -1) {
            throw new RuntimeException("`blockLink` not closed: " + blockLink);
        }
    }

    private int setupSpecial(BlockTile tile, MapObject obj, MapLayer layer) {
        List<MoveStrategy> strategies = new ArrayList<MoveStrategy>();
        Map<String, Object> combinedProperties = combinedProperties(obj.getProperties(), layer.getProperties());
        int limit = -1;
        int blockLink = -1;
        for (Map.Entry<String, Object> ee : combinedProperties.entrySet()) {
            String key = ee.getKey();
            if (key.equals("blockBreak")) {
                // to
                strategies.add(new BlockBreaker());
            }
            if (key.equals("blockCreator")) {
                // to
                strategies.add(new BlockCreator());
            }
            if (key.equals("blockLink")) {
                // correct strategies are setup in constructor
                blockLink = intValue(ee.getValue());
            }
            if (key.equals("modifier")) {
                // to
                strategies.add(new ConnectModifier());
            }
            if (key.equals("areaExecute")) {
                // to
                throw new UnsupportedOperationException(key + " not supported yet");
            }
            if (key.equals("forward")) {
                // to
                strategies.add(new ForwardMover());
            }
            if (key.equals("denyDirections")) {
                // to
                String value = (String) ee.getValue();
                List<Direction4> dirs = new ArrayList<Direction4>();
                if (value.contains("L")) dirs.add(Direction4.LEFT);
                if (value.contains("U")) dirs.add(Direction4.UP);
                if (value.contains("R")) dirs.add(Direction4.RIGHT);
                if (value.contains("D")) dirs.add(Direction4.DOWN);
                strategies.add(new LimitedDirections(dirs.toArray(new Direction4[dirs.size()])));
            }
            if (key.equals("usageCount")) {
                // to or from
                limit = intValue(ee.getValue());
            }
            if (key.equals("noForward")) {
                // to *and* from, setup in constructor
                new NotContinueForward(tile);
            }
            if (key.equals("minSize")) {
                // to
                int maxSize = intValue(combinedProperties.get("maxSize"));
                strategies.add(new RequiredConnection(intValue(ee.getValue()), maxSize));
            }
            if (key.equals("requiredColor")) {
                // to
                strategies.add(new RequiredColor(intValue(ee.getValue())));
            }
            if (key.equals("deniedColor")) {
                // to
                strategies.add(new DeniedColor(intValue(ee.getValue())));
            }
        }

        if (strategies.isEmpty()) {
            if (blockLink == -1) {
                throw new RuntimeException("Useless Map Object: " + tile);
            }
            return blockLink;
        }

        MoveStrategy strategy;
        if (strategies.size() > 1) {
            ConnBlocks.log(strategies.size() + " strategies on " + tile + ": " + strategies);
            Iterator<MoveStrategy> it = strategies.iterator();
            strategy = it.next();
            while (it.hasNext()) {
                strategy = new CombinedMoveStrategy(strategy, it.next());
            }
        }
        else {
            strategy = strategies.get(0);
        }

        if (limit > 0) {
            strategy = new LimitedUses(strategy, limit);
        }
        ConnBlocks.log("Strategy on " + tile + ": " + strategy);
        tile.setMoveStrategyTo(strategy);
        return blockLink;
    }

    private int intValue(Object value) {
        return Integer.parseInt(String.valueOf(value));
    }

    private Map<String, Object> combinedProperties(MapProperties... properties) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (MapProperties prop : properties) {
            Iterator<String> keys = prop.getKeys();
            while (keys.hasNext()) {
                String key = keys.next();
                result.put(key, prop.get(key));
            }
        }
        return result;
    }

    private void loadConnections(TiledMap tiled, BlockMap result, MapLayer layer) {
        Iterator<MapObject> it = layer.getObjects().iterator();
        final String CONNECT_GROUPS = "connectGroups";
        final String CONTROLLABLE = "controllable";
        final String FAR_AWAY = "farAway";
        final String PUSHABLE = "pushable";
        final String PUSHER = "pusher";
        String[] knownProperties = new String[]{ CONNECT_GROUPS, CONTROLLABLE, FAR_AWAY, PUSHABLE, PUSHER,
            "x", "y", "width", "height"};
        checkProperties(knownProperties, layer.getProperties());
        while (it.hasNext()) {
            MapObject obj = it.next();
            Set<BlockTile> blocks = tilesForObject(result, obj);
            ConnectingBlocks connection = result.addConnection(blocks.toArray(new BlockTile[blocks.size()]));
            connection.setConnectGroups(intValue(1, CONNECT_GROUPS, obj.getProperties(), layer.getProperties()));
            connection.setControllable(bool(false, CONTROLLABLE, obj.getProperties(), layer.getProperties()));
            connection.setFarAway(bool(false, FAR_AWAY, obj.getProperties(), layer.getProperties()));
            connection.setPushable(bool(false, PUSHABLE, obj.getProperties(), layer.getProperties()));
            connection.setPusher(bool(false, PUSHER, obj.getProperties(), layer.getProperties()));

            checkProperties(knownProperties, obj.getProperties());
        }
    }

    private void checkProperties(String[] knownProperties, MapProperties properties) {
        Iterator<String> it = properties.getKeys();
        outer:
        while (it.hasNext()) {
            String key = it.next();
            for (String knownKey : knownProperties) {
                if (knownKey.equals(key)) {
                    continue outer;
                }
            }
            throw new RuntimeException("Unknown property: " + key + " known is: " + Arrays.toString(knownProperties));
        }
    }

    private int intValue(int defaultValue, String key, MapProperties... properties) {
        for (MapProperties prop : properties) {
            Object value = prop.get(key);
            if (value != null) {
                return Integer.parseInt(String.valueOf(value));
            }
        }
        return defaultValue;
    }

    private boolean bool(boolean defaultValue, String key, MapProperties... properties) {
        return intValue(-1, key, properties) > 0;
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
