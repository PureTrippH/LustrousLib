package org.lustrouslib.geometry;

import org.bukkit.util.Vector;

import java.util.List;

public class Polygon {
    private List<Vector> vertices;

    public Polygon(List<Vector> vertices) {
        this.vertices = vertices;
    }

    public List<Vector> getVertices() {
        return vertices;
    }

}
