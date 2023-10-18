package net.zomis.connblocks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Zomis on 2014-11-12.
 */
public class Base64Tool {

    public List<Integer> doubleUp(LinkedList<Integer> values) {
        List<Integer> doubles = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            doubles.add(values.get(i) * 2);
        }
        return doubles;
    }

    int findBiggestDifference(int[] numbers) {
        int biggestDifference = 0;
        for (int a : numbers) {
            for (int b : numbers) {
                int currentDifference = a - b;
                if (currentDifference > biggestDifference) {
                    biggestDifference = currentDifference;
                }
            }
        }
        return biggestDifference;
    }

    public static void main(String[] args) {
        Base64Tool a = new Base64Tool();
        List<Integer> b = a.doubleUp(new LinkedList<>(Arrays.asList(1, 3, 2)));
        int c = a.findBiggestDifference(new int[]{ -10, -20, 30, 15 });

        System.out.println(b);
        System.out.println(c);
    }


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
            return new String(Base64.getDecoder().decode(level), "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not found. That's strange.");
        }
    }

}
