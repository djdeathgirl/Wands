package me.dylan.wands.spell.implementations.darkmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public enum DarkSpark implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    DarkSpark() {
        this.behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}