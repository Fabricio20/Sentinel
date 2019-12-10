package net.notfab.sentinel.sdk.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tuple<K, V> {

    private K key;
    private V value;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tuple)) {
            return false;
        }
        Tuple tuple = (Tuple) object;
        return (Objects.equals(tuple.getKey(), this.getKey())) && (Objects.equals(tuple.getValue(), this.getValue()));
    }

    @Override
    public String toString() {
        return key + "/" + value;
    }

}
