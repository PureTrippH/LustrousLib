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
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof QuadTreeNode)) return false;
            QuadTreeNode<E> compNode = (QuadTreeNode<E>) o;
            if (compNode.polygon.equals(this.polygon)) return true;
            return false;
        }
    }

    /**
     * The Representation of a Square of data
     */
    public class QuadTreeSquare {
        public boolean hasOverflowed;
        public QuadTreeSquare[] subsquares;
        public List<QuadTreeNode<E>> dataInSquare;
        ArrayList<Vector> corners;
        public Vector blCorner;
        public Vector trCorner;
        public int width;
        public int height;
        QuadTreeSquare(Vector blCorner, Vector trCorner) {
            this.corners = new ArrayList<Vector>();
            corners.add(blCorner);
            corners.add(trCorner);
            this.blCorner = getMinimumVecor();
            this.trCorner = getMaximumVector();
            this.width = (int) (trCorner.getX() - blCorner.getX());
            this.height = (int) (trCorner.getZ() - blCorner.getZ());
            this.subsquares = (QuadTreeSquare[]) Array.newInstance(QuadTreeSquare.class, 4);
            this.dataInSquare = new ArrayList<QuadTreeNode<E>>(4);
        }

        public boolean isInSquare(Vector vec) {
            return vec.clone().isInAABB(this.getMinimumVecor().clone(), this.getMaximumVector().clone());
        }

        public Vector getMaximumVector() {
            if (corners.isEmpty()) {
                return null;
            }

            double maxX = Double.MIN_VALUE;
            double maxY = 0;
            double maxZ = Double.MIN_VALUE;

            for (Vector vector : corners) {
                maxX = Math.max(maxX, vector.getX());
                maxY = Math.max(maxY, vector.getY());
                maxZ = Math.max(maxZ, vector.getZ());
            }

            return new Vector(maxX, maxY, maxZ);
        }
        public Vector getMinimumVecor() {
            if (corners.isEmpty()) {
                return null;
            }

            double minX = Double.MAX_VALUE;
            double minY = 0;
            double minZ = Double.MAX_VALUE;

            for (Vector vector : corners) {
                minX = Math.min(minX, vector.getX());
                minY = Math.min(minY, vector.getY());
                minZ = Math.min(minZ, vector.getZ());
            }

            return new Vector(minX, minY, minZ);
        }
        @Override
        public String toString() {
            return ("Curr Square: (" + this.blCorner + ")  (" + this.trCorner + ") Capacity: " + capacity + " Size: " + this.dataInSquare.size());
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
     * @param trCorner Top Right Vector of the Boundary
     */
    public QuadTree(Vector blCorner, Vector trCorner, int capacity) {
        this.root = new QuadTreeSquare(blCorner, trCorner);
        this.capacity = capacity;
        this.size = 0;
        this.quadrants = 0;
    }


    /**
     * Inserts a new Polygonal region into the quad tree
     * @param polygon Polygon defining the addition region in th tree
     * @param data E data to pass in
     */
    public void insert(Polygon polygon, E data) {
        for (Vector vec : polygon.getVertices()) {
            if (!(this.root.isInSquare(vec))) {
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
        updateHelper(root, data, oldPolygon, newPolygon);
    }

    private void updateHelper(QuadTreeSquare curr, E data, Polygon oldPolygon, Polygon newPolygon) {
        if(curr == null) {
            return;
        }
        //Will not run if size == 0
        for (QuadTreeNode<E> currNode : curr.dataInSquare) {
            //If the Old polygon is in the square, then see if new still fits. If not, remove
            if (currNode.polygon.equals(oldPolygon)) {
                if(!checkCollision(curr, newPolygon)) {
                    removeWithPolyHelper(data, oldPolygon, curr);
                } else {
                    currNode.polygon = newPolygon;
                }
            }
        }
        if (checkCollision(curr, newPolygon) && curr.hasOverflowed == false) {
            //If new Polygon Belongs, then add it
            insertHelper(new QuadTreeNode<E>(newPolygon, data), curr);
        }
        for (QuadTreeSquare square : curr.subsquares) {
            //Go Find Portions of the Tree Where Old Polygon is to remove
            if (checkCollision(square, oldPolygon)) {
                updateHelper(square, data, oldPolygon, newPolygon);
                return;
            }
            //Find Portions with new to Add To Them
            if (checkCollision(square, newPolygon)) {
                updateHelper(square, data, oldPolygon, newPolygon);
                return;
            }
        }
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
        // If the current square has space and has not overflowed
        if (curr.dataInSquare.size() < capacity && !curr.hasOverflowed) {
            // Add the node to the current square
            if (!curr.dataInSquare.contains(node)) {
                curr.dataInSquare.add(node);
                size++;
            }
            // Check if the square needs to be subdivided
            if (curr.dataInSquare.size() == capacity) {
                subdivide(curr);
            }
            return;
        }

        // If the current square is full or has overflowed
        for (QuadTreeSquare subsquare : curr.subsquares) {
            if (subsquare != null && !subsquare.hasOverflowed && checkCollision(subsquare, node.polygon)) {
                // Recursively insert the node into the intersecting subsquare
                insertHelper(node, subsquare);
                return; // Only insert into one subsquare
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
                if (curr.subsquares[i].isInSquare(currPos)) {
                    return queryHelper(currPos, curr.subsquares[i]);
                }
            }
        }
        throw new NoSuchElementException("Element is not In the QuadTree!");
    }

    private void subdivide(QuadTreeSquare curr) {
        if (curr.dataInSquare.size() == capacity && !curr.hasOverflowed) {
            System.out.println(curr);
            List<QuadTreeNode<E>> savedNodes = new ArrayList<>(curr.dataInSquare);
            Vector center = curr.blCorner.clone().midpoint(curr.trCorner.clone());

            // Calculate the midpoints for x and z coordinates
            double midX = (curr.blCorner.getX() + curr.trCorner.getX()) / 2.0;
            double midZ = (curr.blCorner.getZ() + curr.trCorner.getZ()) / 2.0;

            // Quadrant 1
            curr.subsquares[0] = new QuadTreeSquare(center.clone(), curr.trCorner.clone());
            // Quadrant 2
            curr.subsquares[1] = new QuadTreeSquare(new Vector(midX, 0, curr.blCorner.getZ()), new Vector(curr.trCorner.getX(), 0, midZ));
            // Quadrant 3
            curr.subsquares[2] = new QuadTreeSquare(curr.blCorner.clone(), center.clone());
            // Quadrant 4
            curr.subsquares[3] = new QuadTreeSquare(new Vector(curr.blCorner.getX(), 0, midZ), new Vector(midX, 0, curr.trCorner.getZ()));
            quadrants += 4;

            curr.hasOverflowed = true;
            curr.dataInSquare.clear();

            // Insert saved nodes into the new subsquares
            for (QuadTreeNode<E> node : savedNodes) {
                for (int i = 0; i < curr.subsquares.length; i++) {
                    if (checkCollision(curr.subsquares[i], node.polygon)) {
                        insertHelper(node, curr.subsquares[i]);
                    }
                }
            }
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
        for (Vector v : polygon.getVertices()) {
            if (curr.isInSquare(v) && !v.equals(new Vector(0, 0, 0))) return true;
        }
        //Create the bounds of the new QuadTreeRectangle
        for (int i = 0; i < polygon.getVertices().size(); i++) {
            Vector aVec = polygon.getVertices().get(i % polygon.getVertices().size());
            Vector bVec = polygon.getVertices().get((i + 1) % polygon.getVertices().size()).clone();
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
        Vector newTR = root.trCorner.clone().add(new Vector(100,0,100));
        QuadTree<E> newTree = new QuadTree<E>(newBL, newTR, this.capacity);
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
                " Bottom Left: " + root.corners.get(0).toString() +
                " Top Right: " + root.corners.get(1).toString();
    }

    public int getQuadrants() {
        return quadrants;
    }
}