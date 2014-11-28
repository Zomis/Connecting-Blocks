package net.zomis.connblocks.gdx.mapload;

/**
 * Created by Zomis on 2014-11-28.
 */
public class MapLoadingException extends RuntimeException {

    public MapLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapLoadingException(String message) {
        super(message);
    }
}
