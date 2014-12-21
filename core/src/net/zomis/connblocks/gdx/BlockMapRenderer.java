package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private final Texture tiles = new Texture("levels/tiles.png");
    private final TextureRegion wall = new TextureRegion(tiles, 0, 0, 32, 32);
    private final TextureRegion goal = new TextureRegion(tiles, 32, 0, 32, 32);
    private final TextureRegion notGoal = new TextureRegion(tiles, 64, 0, 32, 32);
    private final TextureRegion freezed = new TextureRegion(tiles, 96, 0, 32, 32);
    private final ConnectingGame connGame;
    private final ShaderProgram gradientShader;
    private final Color color = Color.WHITE;

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
        batch.setProjectionMatrix(matrix);
        batch.setColor(color);
        batch.begin();
        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                BlockTile tile = game.pos(x, y);
                BlockType type = tile.getType();

                TextureRegion texture = textureFor(type);
                if (texture != null) {
                    batch.draw(texture, x * size, y * size, size, size);
                }



            }
        }
        batch.end();

/*        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                BlockTile tile = game.pos(x, y);
                BlockType type = tile.getType();
                TextureRegion texture = textureFor(type);
                shape.begin();
                if (texture != null) {
                    batch.draw(texture, x * size, y * size, size, size);
                }
                else {
                    shape.setColor(colorFor(type));
                    shape.rect(x * size, y * size, size, size);
                }
                if (tile.getMoveStrategyFrom() != null || tile.getMoveStrategyTo() != null) {
                    shape.set(ShapeRenderer.ShapeType.Filled);
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
        batch.end();*/
    }

    private TextureRegion textureFor(BlockType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case IMPASSABLE: return wall;
            case GOAL: return goal;
            case NOT_GOAL: return notGoal;
            case FREEZED: return freezed;
            default: return null;
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
