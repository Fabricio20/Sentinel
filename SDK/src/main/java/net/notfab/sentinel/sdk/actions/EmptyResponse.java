package net.notfab.sentinel.sdk.actions;

import java.util.ArrayList;
import java.util.List;

public class EmptyResponse extends ActionResponse {

    @Override
    public <T> T get() {
        return null;
    }

    @Override
    public <T> List<T> getList() {
        return new ArrayList<>();
    }

}
