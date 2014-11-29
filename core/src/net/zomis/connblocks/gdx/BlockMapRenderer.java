package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import net.zomis.connblocks.ConnBlocks;
import net.zomis.connblocks.IntPoint;
import net.zomis.connblocks.*;

/**
 * Created by Zomis on 2014-11-12.
 */
public class BlockMapRenderer {

    private final BlockMap game;
    public static final float size = 30;
    private static final float SIZE_SPECIAL = size * 2 / 3;
    private static final float OFFSET_SPECIAL = (size - SIZE_SPECIAL) / 2;
    private final ShapeRenderer shape = new ShapeRenderer();

    private final Texture specialBlock = new Texture("white.png");
    private final ConnectingGame connGame;
    private final ShaderProgram gradientShader;

    public BlockMapRenderer(ConnectingGame connGame, BlockMap game) {
        this.game = game;

        this.connGame = connGame;
        gradientShader = new ShaderProgram(Gdx.files.internal("shader.vert"), Gdx.files.internal("shader.frag"));
        if (!gradientShader.isCompiled()) {
            ConnBlocks.log("shader error: " + gradientShader.getLog());
        }
    }

    public void render(Batch batch, OrthographicCamera camera) {
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
    }
    private IntPoint po = new IntPoint();


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
