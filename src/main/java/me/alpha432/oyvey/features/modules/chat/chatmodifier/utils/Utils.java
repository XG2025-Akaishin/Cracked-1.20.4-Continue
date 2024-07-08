package me.alpha432.oyvey.features.modules.chat.chatmodifier.utils;

import java.util.Random;

public class Utils {

    private static final Random random = new Random();

    public static int random(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static double random(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}
