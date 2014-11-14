package net.zomis.connblocks.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import net.zomis.ConnBlocks;
import net.zomis.connblocks.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Zomis on 2014-11-12.
 */
public class BlockMapRenderer {

    private final BlockMap game;
    public static final float size = 30;
    private static final float SIZE_SPECIAL = size * 2 / 3;
    private static final float OFFSET_SPECIAL = (size - SIZE_SPECIAL) / 2;
    private final ShapeRenderer shape = new ShapeRenderer();

    private final Texture specialBlock = new Texture("marked.png");

    public BlockMapRenderer(BlockMap game) {
        this.game = game;
    }

    public void render(Batch batch, OrthographicCamera camera, ConnectingBlocks activeConnection) {
//        float mapHeight = (game.getMapHeight() - 2) * size;
        shape.setAutoShapeType(true);
        Matrix4 matrix = camera.combined;
        shape.setProjectionMatrix(matrix);
        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                BlockTile tile = game.pos(x, y);
                BlockType type = tile.getType();
                shape.setColor(colorFor(type));
                shape.begin();
                shape.set(ShapeRenderer.ShapeType.Filled);
                shape.rect(x * size, y * size, size, size);
                if (tile.getMoveStrategyFrom() != null || tile.getMoveStrategyTo() != null) {
                    float xdraw = x * size + OFFSET_SPECIAL;
                    float ydraw = y * size + OFFSET_SPECIAL;
//                    ConnBlocks.log("Special block for " + tile + " at " + xdraw + ", " + ydraw + " size " + SIZE_SPECIAL);
                    shape.setColor(Color.CYAN);
                    shape.rect(xdraw, ydraw, SIZE_SPECIAL, SIZE_SPECIAL);
//                    batch.draw(specialBlock, xdraw, ydraw, SIZE_SPECIAL, SIZE_SPECIAL);
                }
                shape.end();
            }
        }

        for (ConnectingBlocks connection : game.getConnections()) {
            Color color = getConnColors(connection)[0];
            if (connection == activeConnection) {
                color.a = 0.7f;
            }
            shape.setColor(color);

            for (Block block : connection.getBlocks()) {
                int x = block.getX();
                int y = block.getY();
                shape.begin();
                shape.set(ShapeRenderer.ShapeType.Filled);
//                shape.rect(x, y, origx, origy, width, height, scale, scale, degrees, col, col, col, col);
                shape.rect(x * size, y * size, size, size);
                shape.end();
            }
        }
    }

    private static final Color[] connectColors = new Color[] { Color.BLUE, Color.RED, Color.GREEN, new Color(0xff, 0xff, 0, 1),
            new Color(0, 0.9176f, 1, 1),
            intCol(0xffAA00FF),
//            0xffFF7F00, 0xffBFFF00, 0xff0095FF, 0xffFF00AA, 0xffFFD400, 0xffEDB9B9, 0xffEDB9B9, 0xffE7E9B9,
  //          0xffDCB9ED, 0xffB9EDE0, 0xff8F2323, 0xff23628F, 0xff8F6A23, 0xff6B238F, 0xff4F8F23, 0xff737373, 0xffCCCCCC,
    //        0xffff8000, 0xff7700dd, 0xff00ffff, 0xffffaec4
    };

    private static Color intCol(int i) {
        int r = 0x00ff0000 & i;
        int g = 0x0000ff00 & i;
        int b = 0x000000ff & i;

        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

    private static Color[] getConnColors(ConnectingBlocks conn) {
        if (conn.getConnectGroups() == 16777215)
            return new Color[]{ Color.WHITE };
        if (conn.getConnectGroups() == 0)
            return new Color[]{ Color.BLACK };

        int c = conn.getConnectGroups();
        List<Color> result = new LinkedList<Color>();
        int remain = c;
        for (int i = 0; i < 24 && remain > 0; i++) {
            int v = 1 << i;

            if ((c & v) == v) {
                result.add(connectColors[i]);
                remain -= v;
            }
        }

        Color[] ret = new Color[result.size()];
        ListIterator<Color> it = result.listIterator();
        while (it.hasNext()) {
            ret[it.nextIndex()] = it.next();
        }
        return ret;
    }

    private Color colorFor(BlockType type) {
        if (type == null) {
            return Color.LIGHT_GRAY;
        }
        else if (type == BlockType.GOAL) {
            return Color.GREEN;
        }
        else return Color.BLACK;

    }

    public void dispose() {
        specialBlock.dispose();
    }
}
