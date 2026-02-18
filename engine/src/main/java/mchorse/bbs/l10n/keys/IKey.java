package mchorse.bbs.l10n.keys;

import mchorse.bbs.BBS;

import java.util.List;

public interface IKey {
    IKey EMPTY = new StringKey("");

    static IKey lang(String key) {
        return BBS.getL10n().getKey(key);
    }

    static IKey lang(String key, String content, IKey reference) {
        LangKey langKey = BBS.getL10n().getKey(key, content);

        if (reference instanceof LangKey) {
            langKey.reference = (LangKey) reference;
        }

        return langKey;
    }

    /**
     * This method is used to create an IKey that contains raw string data.
     */
    static IKey raw(String string) {
        return new StringKey(string);
    }

    static IKey comp(List<IKey> keys) {
        return new CompoundKey(keys);
    }

    String get();

    default IKey format(Object... args) {
        return new FormatKey(this, args);
    }
}