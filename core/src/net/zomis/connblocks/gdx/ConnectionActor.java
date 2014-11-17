package net.zomis.connblocks.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.zomis.IntPoint;
import net.zomis.connblocks.Block;
import net.zomis.connblocks.ConnectingBlocks;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ConnectionActor extends Actor {

    private final ConnectingBlocks connection;
    private final Texture texture = new Texture("white.png");
    private final ConnectingGame game;
    private final ShaderProgram gradientShader;
    private Mesh mesh;

    public ConnectionActor(ConnectingGame game, ConnectingBlocks connection) {
        this.game = game;
        this.connection = connection;
        gradientShader = new ShaderProgram(Gdx.files.internal("shader.vert"), Gdx.files.internal("shader.frag"));
    }

    private void findTopLeft(ConnectingBlocks connection, IntPoint out) {
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
        out.set(x, y);

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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float size = BlockMapRenderer.size;
        Color color = getColor();

        for (Block block : connection.getBlocks()) {
            int x = block.getX();
            int y = block.getY();
            batch.draw(texture, x * size, y * size, size, size);
        }
        gradientShader.setUniformMatrix("u_projTrans", game.camera.combined);
//        findTopLeft(connection, po);
        Color[] colors = getConnColors(connection);
        if (colors.length == 1) {
            gradientShader.setUniformf("colorA", colors[0]);
            gradientShader.setUniformf("colorB", colors[0]);
//            ConnBlocks.log(connection + " is one colored: " + colors[0]);
            batch.setColor(colors[0]);
            batch.setShader(gradientShader);
        }
        else {
            Color theColor = getConnColors(connection)[0];
            gradientShader.setUniformf("colorA", colors[0]);
            gradientShader.setUniformf("colorB", colors[1]);
//            ConnBlocks.log(connection + " is multi colored " + Arrays.toString(colors));
            batch.setColor(theColor);
            batch.setShader(gradientShader);
        }

/*        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());*/
    }
}
