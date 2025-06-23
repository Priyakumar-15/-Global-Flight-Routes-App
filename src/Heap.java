import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException; // Added for clarity when heap is empty

/**
 * Custom Heap (Min-Heap) implementation that supports efficient priority updates.
 * Used as a priority queue for Dijkstra's algorithm.
 * @param <T> The type of elements stored in the heap, must be Comparable.
 */
public class Heap<T extends Comparable<T>>
{
    // Stores the elements of the heap in an ArrayList (array representation)
    ArrayList<T> data = new ArrayList<>();
    // Maps each item to its current index in the 'data' ArrayList for O(1) lookup.
    // Crucial for the updatePriority method.
    HashMap<T, Integer> map = new HashMap<>();

    /**
     * Adds a new item to the heap and maintains the min-heap property.
     * @param item The item to add.
     */
    public void add(T item)
    {
        data.add(item); // Add the new item to the end
        map.put(item, this.data.size() - 1); // Store its initial index in the map
        upheapify(data.size() - 1); // Restore heap property by moving item up
    }

    /**
     * Restores the min-heap property by moving an item up the tree.
     * This is called after adding a new item or updating an item's priority.
     * @param ci The current index of the item to be moved up.
     */
    private void upheapify(int ci)
    {
        if (ci == 0) { // Base case: if it's the root, stop
            return;
        }
        int pi = (ci - 1) / 2; // Calculate parent index

        // For a MIN-HEAP: if the child (ci) is SMALLER than its parent (pi), swap them.
        // A.compareTo(B) returns negative if A < B.
        if (data.get(ci).compareTo(data.get(pi)) < 0)
        {
            swap(pi, ci); // Perform the swap
            upheapify(pi); // Recursively call upheapify for the parent's new position
        }
    }

    /**
     * Swaps two elements in the heap's underlying ArrayList and updates their indices in the map.
     * @param i Index of the first element.
     * @param j Index of the second element.
     */
    private void swap(int i, int j)
    {
        T ith = data.get(i); // Get the element at index i
        T jth = data.get(j); // Get the element at index j

        data.set(i, jth); // Place jth element at index i
        data.set(j, ith); // Place ith element at index j

        map.put(ith, j); // Update map: ith element is now at index j
        map.put(jth, i); // Update map: jth element is now at index i
    }

    /**
     * Prints the current state of the heap's underlying ArrayList.
     * (Note: This does not visualize the heap structure, just the array.)
     */
    public void display()
    {
        System.out.println(data);
    }

    /**
     * Returns the number of elements currently in the heap.
     * @return The size of the heap.
     */
    public int size()
    {
        return this.data.size();
    }

    /**
     * Checks if the heap is empty.
     * @return True if the heap contains no elements, false otherwise.
     */
    public boolean isEmpty()
    {
        return this.size() == 0;
    }

    /**
     * Removes and returns the minimum element from the heap (which is always at the root).
     * Maintains the min-heap property after removal.
     * @return The minimum element.
     * @throws NoSuchElementException if the heap is empty.
     */
    public T remove()
    {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty heap.");
        }
        // Swap the root (minimum) with the last element
        swap(0, this.data.size() - 1);
        // Remove the (old) root, which is now at the last position
        T rv = this.data.remove(this.data.size() - 1);
        // Restore heap property by moving the new root down
        downheapify(0);

        map.remove(rv); // Remove the returned item from the map as it's no longer in the heap
        return rv;
    }

    /**
     * Restores the min-heap property by moving an item down the tree.
     * This is called after removing the root or updating an item's priority to a higher value.
     * @param pi The current index of the item to be moved down.
     */
    private void downheapify(int pi)
    {
        int lci = 2 * pi + 1; // Left child index
        int rci = 2 * pi + 2; // Right child index
        int minIdx = pi;      // Assume parent is the minimum initially

        // Compare parent with left child: if left child is smaller, update minIdx
        if (lci < this.data.size() && this.data.get(lci).compareTo(this.data.get(minIdx)) < 0)
        {
            minIdx = lci;
        }

        // Compare current minimum (could be parent or left child) with right child:
        // if right child is smaller, update minIdx
        if (rci < this.data.size() && this.data.get(rci).compareTo(this.data.get(minIdx)) < 0)
        {
            minIdx = rci;
        }

        // If the minimum element is not the parent, swap and recurse
        if (minIdx != pi)
        {
            swap(minIdx, pi);      // Perform the swap
            downheapify(minIdx); // Recursively call downheapify for the child's new position
        }
    }

    /**
     * Returns the minimum element in the heap without removing it.
     * @return The minimum element.
     * @throws NoSuchElementException if the heap is empty.
     */
    public T peek() // Renamed get() to peek() for conventional naming in priority queues
    {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Cannot peek from empty heap.");
        }
        return this.data.get(0);
    }

    /**
     * Updates the priority of an existing item in the heap.
     * This method assumes the item's priority (its 'compareTo' value) has *decreased*,
     * which means it might need to bubble up the heap. For Dijkstra's, this is sufficient.
     * If priority could increase, you would also need to call downheapify from that index.
     * @param item The item whose priority has been updated.
     */
    public void updatePriority(T item)
    {
        // Get the current index of the item from the map
        Integer index = map.get(item);
        if (index == null) {
            // Item not found in heap, handle this case if necessary (e.g., throw exception or log)
            // For Dijkstra, this should generally not happen if map is kept consistent.
            return;
        }
        // Move the item up to restore heap property after its priority has decreased
        upheapify(index);
        // Note: For a general-purpose heap where priority could increase,
        // you might also need a downheapify(index) call here after upheapify,
        // or a more complex logic to check which way it should go.
        // But for Dijkstra, costs only decrease, so only upheapify is needed.
    }
}
