package com.jeremykenedy.rainonglass;

import java.util.Random;

public final class RainOptions {
    public final boolean light;
    public final int drops;
    public final float speed;
    public final boolean glow;
    public final boolean sharp;

    private RainOptions(boolean light, int drops, float speed, boolean glow, boolean sharp) {
        this.light = light;
        this.drops = drops;
        this.speed = speed;
        this.glow = glow;
        this.sharp = sharp;
    }

    public static RainOptions resolve(String background, String density, String motion, String cityGlow,
            String focus, boolean randomizeAll, Random random) {
        String selectedBackground = choose(background, randomizeAll, random, "night", "overcast");
        String selectedDensity = choose(density, randomizeAll, random, "few", "handful", "many", "heavy", "downpour");
        String selectedMotion = choose(motion, randomizeAll, random, "slow", "steady", "fast");
        String selectedGlow = choose(cityGlow, randomizeAll, random, "off", "on");
        String selectedFocus = choose(focus, randomizeAll, random, "soft", "crisp");
        return new RainOptions("overcast".equals(selectedBackground), dropsFor(selectedDensity),
                speedFor(selectedMotion), "on".equals(selectedGlow), "crisp".equals(selectedFocus));
    }

    private static String choose(String selected, boolean randomizeAll, Random random, String... values) {
        if (randomizeAll || "random".equals(selected)) return values[random.nextInt(values.length)];
        for (String value : values) if (value.equals(selected)) return value;
        return values[0];
    }

    static int dropsFor(String density) {
        if ("few".equals(density)) return 30;
        if ("many".equals(density)) return 100;
        if ("heavy".equals(density)) return 160;
        if ("downpour".equals(density)) return 220;
        return 65;
    }

    static float speedFor(String motion) {
        if ("slow".equals(motion)) return 0.55f;
        if ("fast".equals(motion)) return 1.65f;
        return 1f;
    }
}
