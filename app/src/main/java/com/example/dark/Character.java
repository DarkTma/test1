package com.example.dark;

import java.io.Serializable;

public class Character implements Serializable {
    private String name;
    private int strength;
    private int health;
    private int defense;
    private boolean owned = true;

    public Character(String name, int strength, int health, int defense) {
        this.name = name;
        this.strength = strength;
        this.health = health;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public int getHealth() {
        return health;
    }

    public int getDefense() {
        return defense;
    }
    public boolean getOwned(){
        return owned;
    }

    public int[] getStats() {
        int[] stats = new int[4];  // массив длиной 4
        stats[0] = strength;
        stats[1] = defense;
        stats[2] = health;
        int value = (strength + defense +  health) / 3;
        stats[3] = value;

        return stats;
    }
}