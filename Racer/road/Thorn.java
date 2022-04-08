package com.javarush.games.racer.road;

import com.javarush.engine.cell.*;

public class Thorn extends RoadObject {
    public Thorn(int x, int y) {
        super(RoadObjectType.THORN, x, y);
        super.speed = 0;
    }
}
