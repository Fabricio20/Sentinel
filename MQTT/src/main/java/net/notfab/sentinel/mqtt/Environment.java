package net.notfab.sentinel.mqtt;

public class Environment {

    public static String get(String name) {
        return get(name, null);
    }

    public static String get(String name, String fallback) {
        String env = System.getenv(name);
        if (env == null) {
            return fallback;
        } else {
            return env;
        }
    }

}