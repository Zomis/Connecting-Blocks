package net.zomis.connblocks.gdx;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.ConnectingBlocks;
import net.zomis.connblocks.ConnectingGame;

public class ConnectionActor extends Actor {

    private final ConnectingBlocks connection;
    private final Texture texture = new Texture("badlogic.jpg");
    private final TextureRegion region;
    private final ConnectingGame game;
    private Mesh mesh;

    public ConnectionActor(ConnectingGame game, ConnectingBlocks connection) {
        this.game = game;
        this.connection = connection;
        region = new TextureRegion(texture);

        float a = 100;
        float b = 100;
        mesh = new Mesh(true, 3, 3, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        createPolygonRegion(connection, mesh);
    }

    private void createPolygonRegion(ConnectingBlocks connection, Mesh mesh) {
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        for (Block block : connection.getBlocks()) {
            if (block.getX() < x) {
                x = block.getX();
            }
            if (block.getY() < y) {
                y = block.getY();
            }
        }
        final int size = 50;

        mesh.setVertices(new float[] {
                size*0, size*0, 0,
                size*1, size*1, 0,
                size*1, size*2, 0,
        });
        mesh.setIndices(new short[]{ 0, 1, 2 });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        mesh.render(game.shader, GL20.GL_TRIANGLES);
/*        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());*/
    }
}
