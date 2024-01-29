package org.lustrouslib.geometry;

import org.bukkit.util.Vector;

import java.util.*;

public class GeometricAlgorithms {
    /**
     * Returns The Convex Hull (border) of an Aribtrary point cloud in R2
     * @param vertexSet
     * @return
     */
    public static LinkedList<Vector> convexHull(List<Vector> vertexSet) {
        if (vertexSet.size() < 3) throw new IllegalArgumentException("3 Points Are Needed for Convex Geometry!");
        LinkedList<Vector> hull = new LinkedList<Vector>();
        Vector minVert = vertexSet.get(0);
        for (Vector vert : vertexSet) {
            minVert = (vert.getZ() < minVert.getZ()) ? vert : minVert;
        }
        Stack<Vector> hullStack = new Stack<Vector>();
        Collections.sort(vertexSet, new PolarAngleComparator(minVert));

        hullStack.push(vertexSet.get(0));
        hullStack.push(vertexSet.get(1));
        for(int i = 2; i < vertexSet.size(); i++) {
            Vector candidate = vertexSet.get(i);
            Vector prev = hullStack.pop();
            while (hullStack.peek() != null && ccw(hullStack.peek(), prev, candidate).getY() >= 0) {
                prev = hullStack.pop();
            }
            hullStack.push(prev);
            hullStack.push(candidate);
        }
        return new LinkedList<>(hullStack);
    }

    public static Vector ccw(Vector a, Vector b, Vector c) {
        Vector ba = b.clone().subtract(a.clone());
        Vector ca = c.clone().subtract(a.clone());
        return ba.crossProduct(ca);
    }

    private static class PolarAngleComparator implements Comparator<Vector> {
        private Vector anchor;

        public PolarAngleComparator(Vector anchor) {
            this.anchor = anchor;
        }

        @Override
        public int compare(Vector v1, Vector v2) {
            double angle1 = Math.atan2(v1.getZ() - anchor.getZ(), v1.getX() - anchor.getX());
            double angle2 = Math.atan2(v2.getZ() - anchor.getZ(), v2.getX() - anchor.getX());
            if (angle1 < angle2) {
                return -1;
            } else if (angle1 > angle2) {
                return 1;
            } else {
                double dist1 = v1.distance(anchor);
                double dist2 = v2.distance(anchor);
                return (dist1 < dist2) ? -1 : 1;
            }
        }
    }
}
