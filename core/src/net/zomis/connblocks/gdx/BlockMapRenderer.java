package net.zomis.connblocks.gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import net.zomis.connblocks.BlockMap;
import net.zomis.connblocks.BlockTile;
import net.zomis.connblocks.BlockType;

/**
 * Created by Zomis on 2014-11-12.
 */
public class BlockMapRenderer {

    private final BlockMap game;
    private float size = 32;
    private final ShapeRenderer shape = new ShapeRenderer();

    public BlockMapRenderer(BlockMap game) {
        this.game = game;
    }

    public void render(Batch batch, Matrix4 matrix) {
        shape.setAutoShapeType(true);
        shape.setProjectionMatrix(matrix);
        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                BlockTile tile = game.pos(x, y);
                BlockType type = tile.getType();
                shape.setColor(colorFor(type));
                shape.begin();
                shape.set(ShapeRenderer.ShapeType.Filled);
                shape.rect(x * size, y * size, size, size);
                shape.end();

            }
        }

    }

    private Color colorFor(BlockType type) {
        if (type == null) {
            return Color.WHITE;
        }
        else return Color.BLACK;

    }


}
