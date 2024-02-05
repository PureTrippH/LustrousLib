package org.lustrouslib.structure;

import org.bukkit.util.Vector;
import org.lustrouslib.geometry.GeometricAlgorithms;
import org.lustrouslib.geometry.Polygon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

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
     * Returns the amount of data in the quadtree
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the root of the Quadtree, or the universe
     * @return QuadTreeSquare containing bounds and data
     */
    public QuadTreeSquare getRoot() {
        return root;
    }

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

    public QuadTree(Vector blCorner, Vector trCorner, int capacity) {
        this(blCorner, blCorner.clone().add(new Vector(trCorner.getX(), 0, 0)),
                trCorner, blCorner.clone().add(new Vector(0, 0, trCorner.getZ())), capacity);
    }

    /**
     * Inserts a new Polygonal region into the quad tree
     * @param polygon Polygon defining the addition region in th tree
     * @param data E data to pass in
     */
    public void insert(Polygon polygon, E data) {
        for (Vector vec : polygon.getVertices()) {
            if (!(vec.clone().isInAABB(root.blCorner, root.trCorner))) {
                resize();
                insert(polygon, data);
                return;
            }
        }
        insertHelper(new QuadTreeNode<E>(polygon, data), root);
    }

    /**
     * Returns a rough in-order like traversal of the quad tree
     * @return List of all the data
     */
    public List<QuadTreeNode<E>> inOrder() {
        List<QuadTreeNode<E>> list = new LinkedList<QuadTreeNode<E>>();
        inOrderHelper(list, root);
        return list;
    }


    /**
     * Remove a Node only give a piece of data. Runs in O(n) traversal
     * @param data The old piece of data
     */
    public void remove(E data) {
        removeHelper(data, root);
    }

    /**
     * Remove a Node given a piece of data nad the old polygon. Runs in O(logn) traversal
     * @param data
     */
    public void remove(E data, Polygon poly) {
        removeWithPolyHelper(data, poly, root);
    }

    /**
     * Updates a region of a known node
     * @param data
     * @param oldPolygon
     * @param newPolygon
     */
    public void update(E data, Polygon oldPolygon, Polygon newPolygon) {

    }

    private void updatePolygon(QuadTreeSquare curr, E data, Polygon oldPolygon, Polygon newPolygon) {

    }

    private QuadTreeSquare removeWithPolyHelper(E data, Polygon poly, QuadTreeSquare curr) {
        if (curr == null) {
            return null;
        }
        for (QuadTreeNode<E> node : curr.dataInSquare) {
            if (node.value.equals(data)) {
                curr.dataInSquare.remove(node);
                curr.hasOverflowed = false;
                return curr;
            }
        }
        for (int i = 0; i < curr.subsquares.length; i++) {
            //Could be Possible to intersect all four squares. If so,
            if(checkCollision(curr.subsquares[i], poly)) {
                removeWithPolyHelper(data, poly, curr.subsquares[i]);
            }
        }
        return curr;
    }

    private QuadTreeSquare removeHelper(E data, QuadTreeSquare curr) {
        if (curr == null) {
            return null;
        }
        for (QuadTreeNode<E> node : curr.dataInSquare) {
          if (node.value.equals(data)) {
              curr.dataInSquare.remove(node);
              curr.hasOverflowed = false;
              return curr;
          }
        }
        for (int i = 0; i < curr.subsquares.length; i++) {
            //Could be Possible to intersect all four squares. If so,
            curr.subsquares[i] = removeHelper(data, curr.subsquares[i]);
        }
        return curr;
    }

    public void inOrderHelper(List<QuadTreeNode<E>> list, QuadTreeSquare curr) {
        if (curr == null) {
            return;
        }
        for (QuadTreeSquare square : curr.subsquares) {
            inOrderHelper(list, square);
        }
        for (QuadTreeNode<E> data : curr.dataInSquare) {
            list.add(data);
        }
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
     * @param z double Y of input Point
     */
    public List<QuadTreeNode<E>> query(double x, double z) {
        List<QuadTreeNode<E>> results = queryHelper(new Vector(x, 0, z), root);
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
        throw new NoSuchElementException("Element is not In the QuadTree!");
    }

    private void subdivide(QuadTreeSquare curr) {
        if(curr.dataInSquare.size() == capacity) {
            double width = curr.corners.get(0).getX() + curr.corners.get(1).getX();
            double height = curr.corners.get(0).getZ() + curr.corners.get(3).getZ();
            Vector squareCenter = new Vector(width/2,
                    0, (height)/2);
            List<QuadTreeNode<E>> savedNodes = curr.dataInSquare;
            curr.subsquares[0] = new QuadTreeSquare(squareCenter, squareCenter.clone().add(new Vector(width/2, 0, 0)),
                    curr.corners.get(2), squareCenter.clone().add(new Vector(0, 0, height/2)));
            curr.subsquares[1] = new QuadTreeSquare(squareCenter.clone().add(new Vector(-width/2, 0, 0)),
                    squareCenter, squareCenter.clone().add(new Vector(0, 0, height/2)),curr.corners.get(3));
            curr.subsquares[2] = new QuadTreeSquare(curr.corners.get(0),
                    squareCenter.clone().add(new Vector(0, 0, -height/2)), squareCenter,
                    squareCenter.clone().add(new Vector(-width/2, 0, 0)));
            curr.subsquares[3] = new QuadTreeSquare(squareCenter.clone().add(new Vector(0, 0, -height/2)),
                    curr.corners.get(1), squareCenter.clone().add(new Vector(width/2, 0, 0)), squareCenter);
            quadrants+=4;
            for (QuadTreeNode<E> data : curr.dataInSquare) {
                insertHelper(data, curr);
            }
            curr.dataInSquare.clear();
            curr.hasOverflowed = true;
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
                if (GeometricAlgorithms.ccw(aVec, bVec, cVec).crossProduct(GeometricAlgorithms.ccw(aVec, bVec, cVec)).getY() < 0
                        && GeometricAlgorithms.ccw(cVec, dVec, aVec).crossProduct(GeometricAlgorithms.ccw(cVec, dVec, bVec)).getY() < 0) {
                    return true;
                }
            }
        }
        return false;
    }


    private void resize() {
        //Why? MC standards.
        Vector newBL = root.blCorner.clone().add(new Vector(-100,0,-100));
        Vector newBR = root.brCorner.clone().add(new Vector(100,0,-100));
        Vector newTL = root.tlCorner.clone().add(new Vector(-100,0,100));
        Vector newTR = root.trCorner.clone().add(new Vector(100,0,100));
        QuadTree<E> newTree = new QuadTree<E>(newBL, newBR, newTR, newTL, this.capacity);
        List<QuadTreeNode<E>> resizeList = inOrder();
        for (QuadTreeNode<E> node : resizeList) {
            newTree.insert(node.polygon, node.value);
        }

        this.root = newTree.getRoot();
    }


    @Override
    public String toString() {
        return "QuadTree: " +
                "capacity: " + this.capacity +
                " Bottom Left: " + root.blCorner.toString() +
                " Bottom Right: " + root.brCorner.toString() +
                " Top Right: " + root.trCorner.toString() +
                " Top Left: " + root.tlCorner.toString();
    }

    public int getQuadrants() {
        return quadrants;
    }
}