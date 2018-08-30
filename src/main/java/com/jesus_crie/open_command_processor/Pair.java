package com.jesus_crie.open_command_processor;

import java.util.Map;

public class Pair<L, R> implements Map.Entry<L, R> {

    public static <L, R> Pair<L, R> of(L l, R r) {
        return new Pair<>(l, r);
    }

    private final L left;
    private R right;

    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public L getKey() {
        return left;
    }

    @Override
    public R getValue() {
        return right;
    }

    @Override
    public R setValue(R value) {
        R t = right;
        right = value;
        return t;
    }
}
