package net.notfab.sentinel.sdk;

public class Environment {

    public static <T> T get(String name, T fallback) {
        String env = System.getenv(name);
        if (env == null || env.isBlank()) {
            return fallback;
        }
        if (fallback instanceof Integer) {
            return (T) Integer.valueOf(env);
        } else if (fallback instanceof Boolean) {
            return (T) Boolean.valueOf(env);
        } else {
            return (T) env;
        }
    }

}