package org.lustrouslib.UnitTests;

import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;
import org.lustrouslib.geometry.GeometricAlgorithms;
import org.lustrouslib.geometry.Polygon;
import org.lustrouslib.structure.QuadTree;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class QuadTreeCoordTest {
    final int TIMEOUT = 200;
    private QuadTree<Integer> tree;

    @Before
    public void setUp() {
        tree = new QuadTree<Integer>(new Vector(-100, 0, -100), new Vector(100, 0, 100), 2);
    }


    @Test(timeout = TIMEOUT)
    public void testSizeAndInitialization() {
        assertEquals(0, tree.getSize());
    }

    @Test(timeout = TIMEOUT)
    public void testBorderInit() {
        assertEquals("-100.0,0.0,-100.0", tree.getRoot().blCorner.toString());
        assertEquals("100.0,0.0,100.0", tree.getRoot().trCorner.toString());
    }

    @Test(timeout = TIMEOUT)
    public void addBasic() {

        Vector vec = new Vector(10, 0, -10);
        Vector v1 = new Vector(10, 0, 10);
        Vector v2 = new Vector(20, 0, 10);
        Vector v3 = new Vector(15, 0, 35);
        ArrayList<Vector> polygon = new ArrayList<Vector>();
        polygon.add(v1);
        polygon.add(v2);
        polygon.add(v3);
        tree.insert(new Polygon(polygon), 12);
        assertEquals(new Integer(12), tree.getRoot().dataInSquare.get(0).value);
    }

    @Test(timeout = TIMEOUT)
    public void addTwo() {

        Vector vec = new Vector(10, 0, 10);
        Vector v1 = new Vector(20, 0, 10);
        Vector v2 = new Vector(20, 0, 20);
        Vector v3 = new Vector(10, 0, 20);
        ArrayList<Vector> polygon = new ArrayList<Vector>();
        polygon.add(vec);
        polygon.add(v1);
        polygon.add(v2);
        polygon.add(v3);
        tree.insert(new Polygon(polygon), 12);
        assertEquals(new Integer(12), tree.getRoot().dataInSquare.get(0).value);


        Vector vec2 = new Vector(-1, 0, -1);
        Vector v21 = new Vector(-20, 0, -10);
        Vector v22 = new Vector(-20, 0, -20);
        Vector v23 = new Vector(-10, 0, -20);
        ArrayList<Vector> polygon2 = new ArrayList<Vector>();
        polygon2.add(vec2);
        polygon2.add(v21);
        polygon2.add(v22);
        polygon2.add(v23);
        tree.insert(new Polygon(polygon2), 1);
        assertEquals(4, tree.getQuadrants());

    }

    @Test(timeout = TIMEOUT)
    public void doubleDivide() {

        Vector vec = new Vector(10, 0, 10);
        Vector v1 = new Vector(20, 0, 10);
        Vector v2 = new Vector(20, 0, 20);
        Vector v3 = new Vector(10, 0, 20);
        ArrayList<Vector> polygon = new ArrayList<Vector>();
        polygon.add(vec);
        polygon.add(v1);
        polygon.add(v2);
        polygon.add(v3);
        tree.insert(new Polygon(polygon), 12);
        assertEquals(new Integer(12), tree.getRoot().dataInSquare.get(0).value);


        Vector vec2 = new Vector(60, 0, 60);
        Vector v21 = new Vector(70, 0, 60);
        Vector v22 = new Vector(70, 0, 70);
        Vector v23 = new Vector(60, 0, 70);
        ArrayList<Vector> polygon2 = new ArrayList<Vector>();
        polygon2.add(vec2);
        polygon2.add(v21);
        polygon2.add(v22);
        polygon2.add(v23);
        tree.insert(new Polygon(polygon2), 1);
        assertEquals(4, tree.getRoot().subsquares[2].subsquares.length);

    }


    @Test(timeout = TIMEOUT)
    public void DoubleAddTwoQuads() {
        boolean test = (new Vector(0,0,0).isInAABB(new Vector(100, 0, 100), new Vector(200, 0, 200)));
        Vector vec = new Vector(0, 0, 0);
        Vector v1 = new Vector(16, 0, 16);
        Vector v2 = new Vector(0, 0, 16);
        Vector v3 = new Vector(16, 0, 0);
        ArrayList<Vector> polygon = new ArrayList<Vector>();
        polygon.add(vec);
        polygon.add(v1);
        polygon.add(v2);
        polygon.add(v3);
        tree.insert(new Polygon(polygon), 12);
        assertEquals(new Integer(12), tree.getRoot().dataInSquare.get(0).value);


        Vector vec2 = new Vector(-90, 0, -90);
        Vector v21 = new Vector(-100, 0, -100);
        Vector v22 = new Vector(-90, 0, -100);
        Vector v23 = new Vector(-100, 0, -90);
        ArrayList<Vector> polygon2 = new ArrayList<Vector>();
        polygon2.add(vec2);
        polygon2.add(v21);
        polygon2.add(v22);
        polygon2.add(v23);
        tree.insert(new Polygon(polygon2), 144);
        assertEquals(4, tree.getRoot().subsquares[2].subsquares.length);

    }

    @Test(timeout = TIMEOUT)
    public void ClaimAtCenter() {

        Vector vec = new Vector(-5, 0, -5);
        Vector v1 = new Vector(5, 0, 5);
        Vector v2 = new Vector(-5, 0, 5);
        Vector v3 = new Vector(5, 0, -5);
        ArrayList<Vector> polygon = new ArrayList<Vector>();
        polygon.add(vec);
        polygon.add(v1);
        polygon.add(v2);
        polygon.add(v3);
        tree.insert(new Polygon(polygon), 12);
        assertEquals(new Integer(12), tree.getRoot().dataInSquare.get(0).value);

        Vector vec2 = new Vector(10, 0, 10);
        Vector v21 = new Vector(15, 0, 15);
        Vector v22 = new Vector(15, 0, 10);
        Vector v23 = new Vector(10, 0, 15);
        ArrayList<Vector> polygon2 = new ArrayList<Vector>();
        polygon2.add(vec2);
        polygon2.add(v21);
        polygon2.add(v22);
        polygon2.add(v23);
        tree.insert(new Polygon(polygon2), 1);
        assertEquals(4, tree.getRoot().subsquares[2].subsquares.length);
        assertEquals(new Integer(12), tree.query(0, 0).get(0).value);

    }

}
