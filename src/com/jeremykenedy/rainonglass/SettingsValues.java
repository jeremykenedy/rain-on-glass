package com.jeremykenedy.rainonglass;

public final class SettingsValues {
    private SettingsValues() {}

    public static boolean isSupported(String key, String value) {
        if (key == null || value == null) return false;
        if ("background".equals(key)) return oneOf(value, "night", "overcast", "random");
        if ("density".equals(key)) return oneOf(value, "few", "handful", "many", "heavy", "downpour", "random");
        if ("motion".equals(key)) return oneOf(value, "slow", "steady", "fast", "random");
        if ("city_glow".equals(key)) return oneOf(value, "off", "on", "random");
        if ("focus".equals(key)) return oneOf(value, "soft", "crisp", "random");
        if ("randomize_all".equals(key)) return oneOf(value, "true", "false");
        return false;
    }

    private static boolean oneOf(String value, String... allowed) {
        for (String option : allowed) if (option.equals(value)) return true;
        return false;
    }
}
