package net.zomis.connblocks.gdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.zomis.ConnBlocks;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.ConnectingBlocks;

public class ConnectionActor extends Actor {

    private final ConnectingBlocks connection;
    private final Texture texture = new Texture("badlogic.jpg");
    private final TextureRegion region;
    private final Camera camera;
    PolygonSprite poly;
    PolygonSpriteBatch polyBatch;

    public ConnectionActor(Camera camera, ConnectingBlocks connection) {
        this.camera = camera;
        this.connection = connection;
        region = new TextureRegion(texture);

        float a = 100;
        float b = 100;
        PolygonRegion polyReg = createPolygonRegion(connection);

        poly = new PolygonSprite(polyReg);
        poly.setOrigin(a, b);
        polyBatch = new PolygonSpriteBatch();
    }

    private PolygonRegion createPolygonRegion(ConnectingBlocks connection) {
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

        int a = 50;
        int b = 50;

        return new PolygonRegion(new TextureRegion(texture),
                new float[] {
                        a*0, b*0,
                        a*0, b*2,
                        a*3, b*2,
                        a*3, b*0,
                        a*2, b*0,
                        a*2, b*1,
                        a*1, b*1,
                        a*1, b*0,
                }, new short[]{ 0, 2, 3 });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        polyBatch.setProjectionMatrix(camera.combined);
        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();
/*        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());*/
    }
}
