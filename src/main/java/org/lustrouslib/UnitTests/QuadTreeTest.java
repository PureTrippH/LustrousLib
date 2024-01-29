package org.lustrouslib.UnitTests;

import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.lustrouslib.geometry.Polygon;
import org.lustrouslib.structure.QuadTree;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class QuadTreeTest {
    private static final int TIMEOUT = 200;
    private QuadTree<Integer> tree;

    @Before
    public void setUp() {
        tree = new QuadTree<>(new Vector(0, 0, 0), new Vector(100, 0, 0),
                new Vector(100, 0, 100), new Vector(0, 0, 100), 2);
    }

    @Test(timeout = TIMEOUT)
    public void testSizeAndInitialization() {
        assertEquals(0, tree.getSize());
    }

    @Test(timeout = TIMEOUT)
    public void testAdd() {
        Vector v1 = new Vector(10, 0, 10);
        Vector v2 = new Vector(20, 0, 10);
        Vector v3 = new Vector(15, 0, 35);
        ArrayList<Vector> polygon = new ArrayList<Vector>();
        polygon.add(v1);
        polygon.add(v2);
        polygon.add(v3);
        tree.insert(new Polygon(polygon), 12);
        assertEquals(12, (int) (tree.query(15, 10).get(0).value));
    }

    @Test(timeout = TIMEOUT)
    public void testBorderInit() {
        assertEquals("0.0,0.0,0.0", tree.getRoot().blCorner.toString());
        assertEquals("100.0,0.0,0.0", tree.getRoot().brCorner.toString());
        assertEquals("100.0,0.0,100.0", tree.getRoot().trCorner.toString());
        assertEquals("0.0,0.0,100.0", tree.getRoot().tlCorner.toString());
    }

    @Test(timeout = TIMEOUT)
    public void testMergedAdd() {
        Vector v1 = new Vector(10, 0, 10);
        Vector v3 = new Vector(60, 0, 10);
        Vector v2 = new Vector(60, 0, 60);
        Vector v4 = new Vector(10, 0, 60);
        ArrayList<Vector> polygon1 = new ArrayList<Vector>();
        polygon1.add(v1);
        polygon1.add(v2);
        polygon1.add(v3);
        polygon1.add(v4);
        tree.insert(new Polygon(polygon1), 12);
        Vector v1b = new Vector(90, 0, 90);
        Vector v2b = new Vector(90,0, 80);
        Vector v3b = new Vector(80,0, 80);
        Vector v4b = new Vector(80,0, 90);
        ArrayList<Vector> polygon2 = new ArrayList<Vector>();
        polygon2.add(v1b);
        polygon2.add(v2b);
        polygon2.add(v3b);
        polygon2.add(v4b);
        tree.insert(new Polygon(polygon2), 256);

        assertEquals(8, tree.getQuadrants());
        assertEquals(1, tree.getRoot().subsquares[3].dataInSquare.size());
        assertEquals(12, (int) tree.getRoot().subsquares[3].dataInSquare.get(0).value);
        assertEquals(1, (int) tree.getRoot().subsquares[0].subsquares[0].dataInSquare.size());
        assertEquals(256, (int) tree.getRoot().subsquares[0].subsquares[0].dataInSquare.get(0).value);
    }

    @Test(timeout = TIMEOUT)
    public void testQuery() {
        Vector v1 = new Vector(10, 0, 10);
        Vector v3 = new Vector(60, 0, 10);
        Vector v2 = new Vector(60, 0, 60);
        Vector v4 = new Vector(10, 0, 60);
        ArrayList<Vector> polygon1 = new ArrayList<Vector>();
        polygon1.add(v1);
        polygon1.add(v2);
        polygon1.add(v3);
        polygon1.add(v4);
        tree.insert(new Polygon(polygon1), 12);
        Vector v1b = new Vector(90, 0, 90);
        Vector v2b = new Vector(90,0, 80);
        Vector v3b = new Vector(80,0, 80);
        Vector v4b = new Vector(80,0, 90);
        ArrayList<Vector> polygon2 = new ArrayList<Vector>();
        polygon2.add(v1b);
        polygon2.add(v2b);
        polygon2.add(v3b);
        polygon2.add(v4b);
        tree.insert(new Polygon(polygon2), 256);
        assertEquals(256, (int) tree.query(85, 85).get(0).value);
        assertEquals(8, tree.getQuadrants());
    }
}