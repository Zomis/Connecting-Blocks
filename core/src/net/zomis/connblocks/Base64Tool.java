package net.zomis.connblocks;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Zomis on 2014-11-12.
 */
public class Base64Tool {

    public static BlockMap loadLevel(String level) {
        try {
            if (!level.startsWith("{"))
                level = decode(level);
            return BlockMap.mapper().readValue(level, BlockMap.class).onLoad();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String decode(String level) {
        try {
            return new String(DatatypeConverter.parseBase64Binary(level), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not found. That's strange.");
        }
    }

}
