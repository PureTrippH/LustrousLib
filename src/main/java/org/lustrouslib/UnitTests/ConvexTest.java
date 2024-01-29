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
        vecList.add(new Vector(0, 0, 5));
        vecList.add(new Vector(3, 0, 5));
        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);
        assertEquals(3, hull.size());
        assertEquals((int) 0, (int) hull.get(0).getX());
        assertEquals((int) 0, (int) hull.get(0).getZ());

        assertEquals((int) 3, (int) hull.get(1).getX());
        assertEquals((int) 5, (int) hull.get(1).getZ());

        assertEquals((int) 0, (int) hull.get(2).getX());
        assertEquals((int) 5, (int) hull.get(2).getZ());
    }

    @Test(timeout = TIMEOUT)
    public void testTriangleCenterPoint() {
        ArrayList<Vector> vecList = new ArrayList<Vector>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(0, 0, 5));
        vecList.add(new Vector(3, 0, 5));
        vecList.add(new Vector(1, 0, 4));
        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);
        assertEquals(3, hull.size());
        assertEquals((int) 0, (int) hull.get(0).getX());
        assertEquals((int) 0, (int) hull.get(0).getZ());

        assertEquals((int) 3, (int) hull.get(1).getX());
        assertEquals((int) 5, (int) hull.get(1).getZ());

        assertEquals((int) 0, (int) hull.get(2).getX());
        assertEquals((int) 5, (int) hull.get(2).getZ());
    }

    @Test(timeout = TIMEOUT)
    public void testSquareCenterDot() {
        ArrayList<Vector> vecList = new ArrayList<Vector>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(0, 0, 5));
        vecList.add(new Vector(5, 0, 5));
        vecList.add(new Vector(5, 0, 0));
        vecList.add(new Vector(3, 0, 2));
        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);
        assertEquals(4, hull.size());

        assertEquals((int) 0, (int) hull.get(0).getX());
        assertEquals((int) 0, (int) hull.get(0).getZ());

        assertEquals((int) 5, (int) hull.get(1).getX());
        assertEquals((int) 0, (int) hull.get(1).getZ());

        assertEquals((int) 5, (int) hull.get(2).getX());
        assertEquals((int) 5, (int) hull.get(2).getZ());

        assertEquals((int) 0, (int) hull.get(3).getX());
        assertEquals((int) 5, (int) hull.get(3).getZ());

    }

    @Test(timeout = TIMEOUT)
    public void testConvexHull() {
        // Test case with colinear points, duplicate points, and random points
        ArrayList<Vector> vecList = new ArrayList<>();
        vecList.add(new Vector(0, 0, 0));
        vecList.add(new Vector(1, 0, 1));
        vecList.add(new Vector(2, 0, 2));
        vecList.add(new Vector(3, 0, 3));
        vecList.add(new Vector(4, 0, 4));
        vecList.add(new Vector(5, 0, 5));
        vecList.add(new Vector(6, 0, 6));
        vecList.add(new Vector(1, 0, 6));
        vecList.add(new Vector(7, 0, 7));
        vecList.add(new Vector(7, 0, 6));
        vecList.add(new Vector(8, 0, 8));
        vecList.add(new Vector(9, 0, 9));
        vecList.add(new Vector(1, 0, 1)); // Duplicate point
        vecList.add(new Vector(3, 0, 3)); // Duplicate point
        vecList.add(new Vector(5, 0, 5)); // Duplicate point
        vecList.add(new Vector(7, 0, 7)); // Duplicate point
        vecList.add(new Vector(9, 0, 9)); // Duplicate point
        vecList.add(new Vector(10, 0, 0));
        vecList.add(new Vector(0, 0, 10));

        LinkedList<Vector> hull = GeometricAlgorithms.convexHull(vecList);

        assertEquals(4, hull.size());

        assertEquals(0, (int) hull.get(0).getX());
        assertEquals(0, (int) hull.get(0).getZ());

        assertEquals(10, (int) hull.get(1).getX());
        assertEquals(0, (int) hull.get(1).getZ());

        assertEquals(9, (int) hull.get(2).getX());
        assertEquals(9, (int) hull.get(2).getZ());

        assertEquals(0, (int) hull.get(3).getX());
        assertEquals(10, (int) hull.get(3).getZ());

    }
}
