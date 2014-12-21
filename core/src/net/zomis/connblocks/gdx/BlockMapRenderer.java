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
import net.zomis.connblocks.move.*;

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

        shape.begin();
        shape.setColor(Color.LIGHT_GRAY);
        shape.set(ShapeRenderer.ShapeType.Filled);
        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                BlockTile tile = game.pos(x, y);
                BlockType type = tile.getType();
                if (type == null) {
                    shape.rect(x * size, y * size, size, size);
                }
            }
        }

        shape.setColor(Color.CYAN);
        for (int x = 0; x < game.getMapWidth(); x++) {
            for (int y = 0; y < game.getMapHeight(); y++) {
                BlockTile tile = game.pos(x, y);
                if (tile.getMoveStrategyFrom() != null || tile.getMoveStrategyTo() != null) {
                    drawSpecial(tile);
                }
            }
        }
        shape.end();
    }

    private void drawSpecial(BlockTile tile) {
        int x = tile.getX();
        int y = tile.getY();

        MoveStrategy from = tile.getMoveStrategyFrom();
        MoveStrategy to = tile.getMoveStrategyTo();

        drawSpecial(x, y, from);
        drawSpecial(x, y, to);
    }

    private void drawSpecial(int x, int y, MoveStrategy strategy) {
        if (strategy == null) {
            return;
        }

        float xdraw = x * size + OFFSET_SPECIAL;
        float ydraw = y * size + OFFSET_SPECIAL;

        if (strategy instanceof CombinedMoveStrategy) {
            CombinedMoveStrategy strat = (CombinedMoveStrategy) strategy;
            drawSpecial(x, y, strat.getA());
            drawSpecial(x, y, strat.getB());
        }
        if (strategy instanceof BlockBreaker) {
            shape.setColor(Color.RED);
        }
        if (strategy instanceof BlockCreator) {
            shape.setColor(Color.GREEN);
        }
        if (strategy instanceof AutoMover) {
            shape.setColor(Color.CYAN);
        }
        if (strategy instanceof BlockLink) {
            shape.setColor(Color.BLUE);
        }
        if (strategy instanceof ConnectModifier) {
            shape.setColor(Color.PURPLE);
        }
        if (strategy instanceof DeniedColor) {
            shape.setColor(Color.CYAN);
        }
        if (strategy instanceof ExecuteArea) {
            shape.setColor(Color.CYAN);
        }
        if (strategy instanceof ForwardMover) {
            shape.setColor(Color.BLACK);
        }
        if (strategy instanceof LimitedDirections) {
            shape.setColor(Color.CYAN);
        }
        if (strategy instanceof LimitedUses) {
            shape.setColor(Color.TEAL);
            xdraw += 2;
            ydraw += 2;
        }
        if (strategy instanceof NotContinueForward) {
            shape.setColor(Color.CYAN);
        }
        if (strategy instanceof RequiredColor) {
            shape.setColor(Color.BLACK);
        }
        if (strategy instanceof RequiredConnection) {
            shape.setColor(Color.ORANGE);
        }

        shape.rect(xdraw, ydraw, SIZE_SPECIAL, SIZE_SPECIAL);

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
