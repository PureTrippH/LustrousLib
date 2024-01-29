package org.lustrouslib.UnitTests;

import org.bukkit.util.Vector;
import org.junit.Test;
import org.lustrouslib.geometry.GeometricAlgorithms;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class ConvexTest {
    final int TIMEOUT = 200;

    @Test(timeout = TIMEOUT)
    public void testBasicTriangle() {
        ArrayList<Vector> vecList = new ArrayList<Vector>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(0, 5, 0));
        vecList.add(new Vector(3, 5, 0));
        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);
        assertEquals(3, hull.size());
        assertEquals((int) 0, (int) hull.get(0).getX());
        assertEquals((int) 0, (int) hull.get(0).getY());

        assertEquals((int) 3, (int) hull.get(1).getX());
        assertEquals((int) 5, (int) hull.get(1).getY());

        assertEquals((int) 0, (int) hull.get(2).getX());
        assertEquals((int) 5, (int) hull.get(2).getY());
    }

    @Test(timeout = TIMEOUT)
    public void testTriangleCenterPoint() {
        ArrayList<Vector> vecList = new ArrayList<Vector>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(0, 5, 0));
        vecList.add(new Vector(3, 5, 0));
        vecList.add(new Vector(1, 4, 0));
        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);
        assertEquals(3, hull.size());
        assertEquals((int) 0, (int) hull.get(0).getX());
        assertEquals((int) 0, (int) hull.get(0).getY());

        assertEquals((int) 3, (int) hull.get(1).getX());
        assertEquals((int) 5, (int) hull.get(1).getY());

        assertEquals((int) 0, (int) hull.get(2).getX());
        assertEquals((int) 5, (int) hull.get(2).getY());
    }

    @Test(timeout = TIMEOUT)
    public void testSquareCenterDot() {
        ArrayList<Vector> vecList = new ArrayList<Vector>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(0, 5, 0));
        vecList.add(new Vector(5, 5, 0));
        vecList.add(new Vector(5, 0, 0));
        vecList.add(new Vector(3, 2, 0));
        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);
        assertEquals(4, hull.size());

        assertEquals((int) 0, (int) hull.get(0).getX());
        assertEquals((int) 0, (int) hull.get(0).getY());

        assertEquals((int) 5, (int) hull.get(1).getX());
        assertEquals((int) 0, (int) hull.get(1).getY());

        assertEquals((int) 5, (int) hull.get(2).getX());
        assertEquals((int) 5, (int) hull.get(2).getY());

        assertEquals((int) 0, (int) hull.get(3).getX());
        assertEquals((int) 5, (int) hull.get(3).getY());

    }

    @Test(timeout = TIMEOUT)
    public void testConvexHull() {
        // Test case with colinear points, duplicate points, and random points
        ArrayList<Vector> vecList = new ArrayList<>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(1, 1, 0));
        vecList.add(new Vector(2, 2, 0));
        vecList.add(new Vector(3, 3, 0));
        vecList.add(new Vector(4, 4, 0));
        vecList.add(new Vector(5, 5, 0));
        vecList.add(new Vector(6, 6, 0));
        vecList.add(new Vector(1, 6, 0));
        vecList.add(new Vector(7, 7, 0));
        vecList.add(new Vector(7, 6, 0));
        vecList.add(new Vector(8, 8, 0));
        vecList.add(new Vector(9, 9, 0));
        vecList.add(new Vector(1, 1, 0)); // Duplicate point
        vecList.add(new Vector(3, 3, 0)); // Duplicate point
        vecList.add(new Vector(5, 5, 0)); // Duplicate point
        vecList.add(new Vector(7, 7, 0)); // Duplicate point
        vecList.add(new Vector(9, 9, 0)); // Duplicate point
        vecList.add(new Vector(10, 0, 0));
        vecList.add(new Vector(0, 10, 0));

        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);

        assertEquals(4, hull.size());

        assertEquals(0, (int) hull.get(0).getX());
        assertEquals(0, (int) hull.get(0).getY());

        assertEquals(10, (int) hull.get(1).getX());
        assertEquals(0, (int) hull.get(1).getY());

        assertEquals(9, (int) hull.get(2).getX());
        assertEquals(9, (int) hull.get(2).getY());

        assertEquals(0, (int) hull.get(3).getX());
        assertEquals(10, (int) hull.get(3).getY());

    }
}
