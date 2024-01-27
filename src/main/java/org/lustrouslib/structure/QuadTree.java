package org.lustrouslib.structure;

import org.bukkit.util.Vector;
import org.lustrouslib.geometry.Polygon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A QuadTree-RTree Hybrid Implementation for 2d Queries
 * @param <E> Data To Be Stored in each node
 */
public class QuadTree<E> {
    /**
     * A 2d Point That Holds data
     * @param <E>
     */
    public class QuadTreeNode<E> {
        Polygon polygon;
        public E value;

        QuadTreeNode(Polygon polygon, E value) {
            this.polygon = polygon;
            this.value = value;
        }
    }

    /**
     * The Representation of a Square of data
     */
     public class QuadTreeSquare {
        public Vector blCorner;
        public Vector brCorner;
        public Vector trCorner;
        public Vector tlCorner;
        public boolean hasOverflowed;
        public QuadTreeSquare[] subsquares;
        public List<QuadTreeNode<E>> dataInSquare;
        ArrayList<Vector> corners;
        QuadTreeSquare(Vector blCorner, Vector brCorner, Vector trCorner, Vector tlCorner) {
            this.corners = new ArrayList<>();
            //This is some fucking YandereDev Code. TODO: Clean the Italian Restaurant
            corners.add(blCorner);
            this.blCorner = blCorner;
            corners.add(brCorner);
            this.brCorner = brCorner;
            corners.add(trCorner);
            this.trCorner = trCorner;
            corners.add(tlCorner);
            this.tlCorner = tlCorner;
            this.subsquares = (QuadTreeSquare[]) Array.newInstance(QuadTreeSquare.class, 4);
            this.dataInSquare = new ArrayList<QuadTreeNode<E>>(4);
        }

        public Vector getVec() {
            return blCorner;
        }
    }

    private QuadTreeSquare root;
    private int capacity;
    private int size;
    private int quadrants;

    /**
     * Creates a Quadtree with a set boundary for the universe
     * @param blCorner Bottom left Vector of the Boundary
     * @param brCorner Bottom Right Vector of the Boundary
     * @param trCorner Top Right Vector of the Boundary
     * @param tlCorner Top Left Vector of the Boundary
     */
    public QuadTree(Vector blCorner, Vector brCorner, Vector trCorner, Vector tlCorner, int capacity) {
        this.root = new QuadTreeSquare(blCorner, brCorner, trCorner, tlCorner);
        this.capacity = capacity;
        this.size = 0;
        this.quadrants = 0;
    }

    public void insert(Polygon polygon, E data) {
        insertHelper(new QuadTreeNode<E>(polygon, data), root);
    }

    private void insertHelper(QuadTreeNode<E> node, QuadTreeSquare curr) {
        if (curr.dataInSquare.size() < capacity && !curr.hasOverflowed) {
            curr.dataInSquare.add(node);
            size++;
            subdivide(curr);
            return;
        } else {
            for (int i = 0; i < curr.subsquares.length; i++) {
                //Could be Possible to intersect all four squares. If so,
                if(checkCollision(curr.subsquares[i], node.polygon)) {
                    insertHelper(node, curr.subsquares[i]);
                }
            }
        }
    }

    /**
     * Given a point, find the data in that square
     * @param x double X of input Point
     * @param y double Y of input Point
     */
    public List<QuadTreeNode<E>> query(double x, double y) {
        List<QuadTreeNode<E>> results = queryHelper(new Vector(x, y, 0), root);
        return results;
    }

    private List<QuadTreeNode<E>> queryHelper(Vector currPos, QuadTreeSquare curr) {
        if (curr.dataInSquare.size() < capacity && !curr.hasOverflowed) {
            return curr.dataInSquare;
        } else {
            for (int i = 0; i < 4; i++) {
                if (currPos.clone().isInAABB(curr.subsquares[i].blCorner, curr.subsquares[i].trCorner)) {
                    return queryHelper(currPos, curr.subsquares[i]);
                }
            }
        }
        return null;
    }

    private void subdivide(QuadTreeSquare curr) {
        if(curr.dataInSquare.size() == capacity) {
            double width = curr.corners.get(0).getX() + curr.corners.get(1).getX();
            double height = curr.corners.get(0).getY() + curr.corners.get(3).getY();
            Vector squareCenter = new Vector(width/2,
                    (height)/2, 0);
            List<QuadTreeNode<E>> savedNodes = curr.dataInSquare;
            curr.subsquares[0] = new QuadTreeSquare(squareCenter, squareCenter.clone().add(new Vector(width/2, 0, 0)),
                    curr.corners.get(2), squareCenter.clone().add(new Vector(0, height/2, 0)));
            curr.subsquares[1] = new QuadTreeSquare(squareCenter.clone().add(new Vector(-width/2, 0, 0)),
                    squareCenter, squareCenter.clone().add(new Vector(0, height/2, 0)),curr.corners.get(3));
            curr.subsquares[2] = new QuadTreeSquare(curr.corners.get(0),
                    squareCenter.clone().add(new Vector(0, -height/2, 0)), squareCenter,
                    squareCenter.clone().add(new Vector(-width/2, 0, 0)));
            curr.subsquares[3] = new QuadTreeSquare(squareCenter.clone().add(new Vector(0, -height/2, 0)),
                    curr.corners.get(1), squareCenter.clone().add(new Vector(width/2, 0, 0)), squareCenter);
            quadrants += 4;
            curr.hasOverflowed = true;
            for (QuadTreeNode<E> data : curr.dataInSquare) {
                insertHelper(data, curr);
            }
            curr.dataInSquare.clear();
        }
    }

    /**
     * Strat: Check all 4 points. if a point is bounded within the box, then it has to be in the
     * box. If it isn't then it is still possible to lie in another box. To check this, use corners of the box.
     *
     * @param curr
     * @param polygon
     */
    private boolean checkCollision(QuadTreeSquare curr, Polygon polygon) {
        if (curr == null) {
            return false;
        }
        //Create the bounds of the new QuadTreeRectangle
        for (int i = 0; i < polygon.getVertices().size(); i++) {
            Vector aVec = polygon.getVertices().get(i % polygon.getVertices().size());
            //Check if a point is within a bound, if so, then
            if (aVec.isInAABB(curr.corners.get(0), curr.corners.get(2))) return true;
            Vector bVec = polygon.getVertices().get((i + 1) % polygon.getVertices().size());
            //loop to find every possible corner
            for (int cornerIndex = 0; cornerIndex < curr.corners.size(); cornerIndex++) {
                Vector cVec = curr.corners.get(cornerIndex % curr.corners.size());
                Vector dVec = curr.corners.get((cornerIndex + 1) % curr.corners.size());
                if (ccw(aVec, bVec, cVec).crossProduct(ccw(aVec, bVec, cVec)).getZ() < 0
                    && ccw(cVec, dVec, aVec).crossProduct(ccw(cVec, dVec, bVec)).getZ() < 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getSize() {
       return size;
    }

    private Vector ccw(Vector a, Vector b, Vector c) {
        Vector ba = b.clone().subtract(a.clone());
        Vector ca = c.clone().subtract(a.clone());
        return ba.crossProduct(ca);
    }

    public QuadTreeSquare getRoot() {
        return root;
    }

    public int getQuadrants() {
        return quadrants;
    }
}
