package mchorse.bbs.settings.values;

import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.settings.values.base.BaseValueBasic;

public class ValueType extends BaseValueBasic<BaseType> {
    public ValueType(String id, BaseType value) {
        super(id, value);
    }

    @Override
    public BaseType toData() {
        return this.value;
    }

    @Override
    public void fromData(BaseType type) {
        this.set(type);
    }
}