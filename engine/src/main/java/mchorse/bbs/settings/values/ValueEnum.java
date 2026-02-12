package mchorse.bbs.settings.values;

import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.settings.values.base.BaseValueBasic;

public class ValueEnum <T extends Enum<T>> extends BaseValueBasic<T> {
    public ValueEnum(String id, T value) {
        super(id, value);
    }

    @Override
    public BaseType toData() {
        MapType map = new MapType();

        map.putString("name", this.value.name());
        map.putString("class", this.value.getClass().getName());

        return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fromData(BaseType type) {
        MapType map = type.asMap();

        try {
            Class<T> clazz = (Class<T>) Class.forName(map.getString("class"));

            this.set(Enum.valueOf(clazz, map.getString("name")));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}