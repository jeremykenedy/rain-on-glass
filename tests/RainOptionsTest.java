package com.jeremykenedy.rainonglass;

import java.util.Random;

public final class RainOptionsTest {
    public static void main(String[] args) {
        verifyDensityAndMotion();
        verifyExplicitChoices();
        verifyRandomChoices();
        verifySettingsValidation();
        System.out.println("Rain on Glass settings tests passed.");
    }

    private static void verifyDensityAndMotion() {
        check(RainOptions.dropsFor("few") == 30, "few rain density");
        check(RainOptions.dropsFor("handful") == 65, "default rain density");
        check(RainOptions.dropsFor("many") == 100, "many rain density");
        check(RainOptions.dropsFor("heavy") == 160, "heavy rain density");
        check(RainOptions.dropsFor("downpour") == 220, "downpour density");
        check(RainOptions.dropsFor("unknown") == 65, "density fallback");
        check(RainOptions.speedFor("slow") == 0.55f, "slow speed");
        check(RainOptions.speedFor("steady") == 1f, "steady speed");
        check(RainOptions.speedFor("fast") == 1.65f, "fast speed");
        check(RainOptions.speedFor("unknown") == 1f, "speed fallback");
    }

    private static void verifyExplicitChoices() {
        RainOptions night = RainOptions.resolve("night", "few", "slow", "off", "soft", false, new Random(1));
        check(!night.light && night.drops == 30 && night.speed == 0.55f && !night.glow && !night.sharp,
                "explicit night settings");
        RainOptions overcast = RainOptions.resolve("overcast", "downpour", "fast", "on", "crisp", false,
                new Random(2));
        check(overcast.light && overcast.drops == 220 && overcast.speed == 1.65f && overcast.glow && overcast.sharp,
                "explicit overcast settings");
        RainOptions fallback = RainOptions.resolve("invalid", "invalid", "invalid", "invalid", "invalid", false,
                new Random(3));
        check(!fallback.light && fallback.drops == 30 && fallback.speed == 0.55f && !fallback.glow && !fallback.sharp,
                "safe fallback settings");
    }

    private static void verifyRandomChoices() {
        RainOptions low = RainOptions.resolve("random", "random", "random", "random", "random", false,
                new FixedRandom(0));
        check(!low.light && low.drops == 30 && low.speed == 0.55f && !low.glow && !low.sharp,
                "random options can select first choice");
        RainOptions high = RainOptions.resolve("random", "random", "random", "random", "random", false,
                new FixedRandom(100));
        check(high.light && high.drops == 220 && high.speed == 1.65f && high.glow && high.sharp,
                "random options can select last choice");
        RainOptions all = RainOptions.resolve("night", "few", "slow", "off", "soft", true, new FixedRandom(100));
        check(all.light && all.drops == 220 && all.speed == 1.65f && all.glow && all.sharp,
                "randomize all overrides each stored choice");
    }

    private static void verifySettingsValidation() {
        check(!SettingsValues.isSupported(null, "night"), "null key rejected");
        check(!SettingsValues.isSupported("background", null), "null value rejected");
        for (String value : new String[] {"night", "overcast", "random"})
            check(SettingsValues.isSupported("background", value), "background value accepted");
        for (String value : new String[] {"few", "handful", "many", "heavy", "downpour", "random"})
            check(SettingsValues.isSupported("density", value), "density value accepted");
        for (String value : new String[] {"slow", "steady", "fast", "random"})
            check(SettingsValues.isSupported("motion", value), "motion value accepted");
        for (String value : new String[] {"off", "on", "random"})
            check(SettingsValues.isSupported("city_glow", value), "glow value accepted");
        for (String value : new String[] {"soft", "crisp", "random"})
            check(SettingsValues.isSupported("focus", value), "focus value accepted");
        check(SettingsValues.isSupported("randomize_all", "true"), "true random setting accepted");
        check(SettingsValues.isSupported("randomize_all", "false"), "false random setting accepted");
        check(!SettingsValues.isSupported("background", "day"), "unsupported background rejected");
        check(!SettingsValues.isSupported("density", "ocean"), "unsupported density rejected");
        check(!SettingsValues.isSupported("motion", "instant"), "unsupported motion rejected");
        check(!SettingsValues.isSupported("city_glow", "sometimes"), "unsupported glow rejected");
        check(!SettingsValues.isSupported("focus", "blurred"), "unsupported focus rejected");
        check(!SettingsValues.isSupported("randomize_all", "yes"), "unsupported random value rejected");
        check(!SettingsValues.isSupported("unknown", "value"), "unsupported key rejected");
    }

    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }

    private static final class FixedRandom extends Random {
        private final int value;
        FixedRandom(int value) { this.value = value; }
        @Override public int nextInt(int bound) { return Math.min(value, bound - 1); }
    }
}
