package net.notfab.sentinel.gateway;

import java.util.List;
import java.util.function.Function;

public interface SentinelGateway {

    Function<Long, List<String>> getPrefixProvider();

}