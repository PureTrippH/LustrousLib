package org.lustrouslib.geometry;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Polygon {
    private List<Vector> vertices;

    public Polygon(List<Vector> vertices) {
        this.vertices = GeometricAlgorithms.convexHull(vertices);
    }

    public List<Vector> getVertices() {
        return vertices;
    }

    /**
     * Dynamically Removes a point from the polygon border
     * @param v Point to be removed
     * @return
     */
    public void removePoint(Vector v) {
        if (vertices.remove(v)) {
            this.vertices = GeometricAlgorithms.convexHull(vertices);
        }
    }

    /**
     *
     * @param v
     */
    public void addPoint(Vector v) {
        //Determine if point is inside the hull. If not, don't add
        if (!containsPoint(v)) {
            //TODO: Max and Min tangent
            vertices.add(v);
            this.vertices = GeometricAlgorithms.convexHull(vertices);
        }
    }

    public boolean containsPoint(Vector point) {
        int intersections = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vector vertex1 = vertices.get(i);
            Vector vertex2 = vertices.get(i + 1 < vertices.size() ? i + 1 : 0);
            double x0 = vertex1.getX();
            double y0 = vertex1.getY();
            double z0 = vertex1.getZ();
            double x1 = vertex2.getX();
            double y1 = vertex2.getY();
            double z1 = vertex2.getZ();

            Vector borderVector = vertex2.subtract(vertex1);
            double deltaZ = borderVector.getZ();
            double deltaX = borderVector.getX();

            if (deltaZ != 0) {
                double intersectionZ = point.getZ();
                double intersectionX = deltaX == 0 ? x0 : x0 + (intersectionZ - z0) / (deltaZ / deltaX);

                if (Math.abs(intersectionX - x0) < Vector.getEpsilon()) {
                    return false;
                }

                if (intersectionX > point.getX()
                        && intersectionX >= Math.min(x0, x1) && intersectionX <= Math.max(x0, x1)
                        && intersectionZ >= Math.min(z0, z1) && intersectionZ <= Math.max(z0, z1)) {
                    intersections++;
                }
            } else if (point.getZ() == z0) {
                return false;
            }
        }

        return (intersections % 2 == 1);
    }

}
