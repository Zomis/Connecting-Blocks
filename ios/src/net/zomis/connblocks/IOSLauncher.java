package net.zomis.connblocks;

import org.apache.commons.codec.binary.Base64;
import net.zomis.connblocks.gdx.GameHelper;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import net.zomis.connblocks.ConnectingGame;

import java.io.IOException;

public class IOSLauncher extends IOSApplication.Delegate implements GameHelper {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new ConnectingGame(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public BlockMap loadLevel(String level) {
        try {
            if (!level.startsWith("{")) {
                level = new String(Base64.decodeBase64(level), "UTF-8");
            }
            return BlockMap.mapper().readValue(level, BlockMap.class).onLoad();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}