import java.io.*;
import java.util.*;
import java.util.NoSuchElementException; // Added for clarity when heap is empty

/**
 * Custom Heap (Min-Heap) implementation that supports efficient priority updates.
 * Used as a priority queue for Dijkstra's algorithm.
 * @param <T> The type of elements stored in the heap, must be Comparable.
 */
class Heap<T extends Comparable<T>>
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
     * @param item The item whose priority has been updated.
     */
    public void updatePriority(T item)
    {
        // Get the current index of the item from the map
        Integer index = map.get(item);
        if (index == null) {
            // Item not found in heap, handle this case if necessary (e.g., throw exception or log)
            System.err.println("Warning: Attempted to update priority for item not found in heap: " + item);
            return;
        }
        // Move the item up to restore heap property after its priority has decreased
        upheapify(index);
    }
}

/**
 * Represents a graph data structure to model a flight map.
 * It uses an adjacency list representation.
 */
public class Graph_M
{
    // Inner class representing a single airport (vertex) in the graph.
    public static class Vertex
    {
        // Stores neighbors of this vertex and the weight (distance/time) of the edge.
        // Key: Name of the neighboring airport (String)
        // Value: Weight of the edge (Integer)
        HashMap<String, Integer> nbrs = new HashMap<>();
    }

    // Static map storing all vertices (airports) in the graph.
    // Key: Name of the airport (String)
    // Value: The Vertex object for that airport
    static HashMap<String, Vertex> vtces;

    /**
     * Constructor for the Graph_M class.
     * Initializes the static HashMap for vertices.
     */
    public Graph_M()
    {
        vtces = new HashMap<>();
    }

    // --- Basic Graph Operations ---

    /**
     * Returns the total number of vertices (airports) in the graph.
     * @return The number of vertices.
     */
    public int numVertices()
    {
        return this.vtces.size();
    }

    /**
     * Checks if a vertex with the given name exists in the graph.
     * @param vname The name of the vertex to check.
     * @return True if the vertex exists, false otherwise.
     */
    public boolean containsVertex(String vname)
    {
        return this.vtces.containsKey(vname);
    }

    /**
     * Adds a new vertex (airport) to the graph.
     * @param vname The name of the vertex to add.
     */
    public void addVertex(String vname)
    {
        Vertex vtx = new Vertex();
        vtces.put(vname, vtx);
    }

    /**
     * Removes a vertex (airport) and all its incident edges from the graph.
     * @param vname The name of the vertex to remove.
     */
    public void removeVertex(String vname)
    {
        Vertex vtx = vtces.get(vname);
        if (vtx == null) {
            return; // Vertex not found
        }

        // Create a copy of keys to avoid ConcurrentModificationException
        ArrayList<String> keysToRemove = new ArrayList<>(vtx.nbrs.keySet());

        // For each neighbor of the vertex being removed, remove the edge pointing back to it.
        for (String key : keysToRemove) {
            Vertex nbrVtx = vtces.get(key);
            if (nbrVtx != null) {
                nbrVtx.nbrs.remove(vname);
            }
        }

        // Finally, remove the vertex itself from the main map.
        vtces.remove(vname);
    }

    /**
     * Returns the total number of edges (connections) in the graph.
     * Assumes an undirected graph, so each edge is counted once.
     * @return The number of edges.
     */
    public int numEdges()
    {
        int count = 0;
        for (String key : vtces.keySet()) {
            Vertex vtx = vtces.get(key);
            count = count + vtx.nbrs.size();
        }
        // Each edge is stored twice (once for each endpoint), so divide by 2.
        return count / 2;
    }

    /**
     * Checks if an edge exists between two specified vertices.
     * @param vname1 Name of the first vertex.
     * @param vname2 Name of the second vertex.
     * @return True if an edge exists, false otherwise.
     */
    public boolean containsEdge(String vname1, String vname2)
    {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        // An edge exists if both vertices are in the graph and vtx1's neighbors contain vname2.
        return vtx1 != null && vtx2 != null && vtx1.nbrs.containsKey(vname2);
    }

    /**
     * Adds an edge between two vertices with a specified weight.
     * Assumes an undirected graph, so edge is added in both directions.
     * @param vname1 Name of the first vertex.
     * @param vname2 Name of the second vertex.
     * @param value The weight of the edge (e.g., distance or base time unit).
     */
    public void addEdge(String vname1, String vname2, int value)
    {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        // Do not add edge if either vertex does not exist or if the edge already exists.
        if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
            return;
        }

        // Add edge in both directions for an undirected graph.
        vtx1.nbrs.put(vname2, value);
        vtx2.nbrs.put(vname1, value);
    }

    /**
     * Removes an edge between two specified vertices.
     * Assumes an undirected graph, so edge is removed in both directions.
     * @param vname1 Name of the first vertex.
     * @param vname2 Name of the second vertex.
     */
    public void removeEdge(String vname1, String vname2)
    {
        Vertex vtx1 = vtces.get(vname1);
        Vertex vtx2 = vtces.get(vname2);

        // Check if both vertices and the edge exist before attempting removal.
        if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
            return;
        }

        // Remove edge from both directions.
        vtx1.nbrs.remove(vname2);
        vtx2.nbrs.remove(vname1);
    }

    /**
     * Displays a formatted representation of the graph, showing each airport and its connections.
     */
    public void display_Map()
    {
        System.out.println("\n\t Flight Routes Map");
        System.out.println("\t------------------");
        System.out.println("----------------------------------------------------\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());

        for (String key : keys) {
            StringBuilder str = new StringBuilder(key).append(" =>\n");
            Vertex vtx = vtces.get(key);
            ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());

            for (String nbr : vtxnbrs) {
                str.append("\t").append(nbr).append("\t");
                // Dynamic formatting for better alignment
                if (nbr.length() < 16) str.append("\t");
                if (nbr.length() < 8) str.append("\t");
                str.append(vtx.nbrs.get(nbr)).append("\n");
            }
            System.out.println(str);
        }
        System.out.println("\t------------------");
        System.out.println("---------------------------------------------------\n");
    }

    /**
     * Displays a numbered list of all airports in the graph.
     */
    public void display_Stations()
    {
        System.out.println("\n***********************************************************************\n");
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        int i = 1;
        for (String key : keys) {
            System.out.println(i + ". " + key);
            i++;
        }
        System.out.println("\n***********************************************************************\n");
    }

    // --- Pathfinding Algorithms ---

    /**
     * Checks if a path exists between two airports using Depth-First Search (DFS).
     * @param vname1 The starting airport name.
     * @param vname2 The destination airport name.
     * @param processed A HashMap to keep track of visited vertices to avoid cycles.
     * @return True if a path exists, false otherwise.
     */
    public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed)
    {
        // Base case: If source and destination are the same, a path exists.
        if (vname1.equals(vname2)) {
            return true;
        }

        // Mark the current vertex as processed to avoid re-visiting in the current path.
        processed.put(vname1, true);

        Vertex vtx = vtces.get(vname1);
        // If the vertex doesn't exist (should be handled by prior checks), no path from it.
        if (vtx == null) {
            return false;
        }
        ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

        // Recursively explore all unprocessed neighbors.
        for (String nbr : nbrs) {
            if (!processed.containsKey(nbr)) { // Only visit if not already processed in this path
                if (hasPath(nbr, vname2, processed)) {
                    return true; // Path found through a neighbor
                }
            }
        }
        return false; // No path found from this vertex
    }

    /**
     * Helper class for Dijkstra's algorithm. Represents a vertex along with
     * the path taken to reach it and the accumulated cost.
     * Implements Comparable for use in a PriorityQueue (or custom Heap).
     */
    private static class DijkstraPair implements Comparable<DijkstraPair>
    {
        String vname; // Vertex name
        String psf;   // Path so far (string representation of stations visited)
        int cost;     // Accumulated cost from source to this vertex

        /**
         * Compares two DijkstraPair objects based on their cost.
         * Returns a negative integer, zero, or a positive integer as this object
         * is less than, equal to, or greater than the specified object.
         * This defines the min-heap behavior: smaller cost means higher priority.
         */
        @Override
        public int compareTo(DijkstraPair o) {
            return this.cost - o.cost;
        }
    }

    /**
     * Represents the result of a pathfinding operation.
     * Contains the path string, total cost, and number of interchanges.
     */
    private static class PathResult {
        String path;         // Raw or formatted path string (e.g., "A B C" or "Start A -> B End")
        int totalCost;       // Total cost (distance in KM or time in seconds)
        int interchangeCount; // Number of interchanges

        public PathResult(String path, int totalCost, int interchangeCount) {
            this.path = path;
            this.totalCost = totalCost;
            this.interchangeCount = interchangeCount;
        }
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest path (cost and raw path string).
     *
     * @param src        Source airport full name (e.g., "DEL~Indira Gandhi International Airport, Delhi")
     * @param des        Destination airport full name
     * @param isTimeCost If true, calculates time cost (in seconds); if false, calculates distance cost (in KM).
     * @return A PathResult object containing the raw path string and total cost, or error indicators.
     */
    public PathResult dijkstra(String src, String des, boolean isTimeCost) {
        // Basic validation: Check if source or destination exist in the graph.
        if (!containsVertex(src) || !containsVertex(des)) {
            return new PathResult("INVALID_STATIONS", -1, 0);
        }

        // Map to store the best DijkstraPair found so far for each vertex.
        // Used for efficient lookup and to check if a vertex is "processed" (removed from map means processed).
        HashMap<String, DijkstraPair> map = new HashMap<>();
        // Priority queue (using custom Heap) to always extract the vertex with the minimum cost.
        Heap<DijkstraPair> pq = new Heap<>();

        // Initialize all vertices: set cost to infinity, path to empty.
        // Source vertex's cost is 0, and its path starts with itself.
        for (String key : vtces.keySet()) {
            DijkstraPair np = new DijkstraPair();
            np.vname = key;
            np.psf = ""; // Path will be built
            np.cost = Integer.MAX_VALUE;

            if (key.equals(src)) {
                np.cost = 0;
                np.psf = key; // Path starts with the source airport
            }

            pq.add(np); // Add to the heap
            map.put(key, np); // Store in the map
        }

        // Dijkstra's main loop: continues as long as there are unvisited vertices in the heap.
        while (!pq.isEmpty()) {
            DijkstraPair rp = pq.remove(); // Extract the vertex with the minimum current cost.

            // If the extracted vertex is the destination, we've found the shortest path.
            if (rp.vname.equals(des)) {
                return new PathResult(rp.psf, rp.cost, 0); // Return raw path and cost.
            }

            // If this vertex has already been "processed" (i.e., removed from the map)
            // it means we found a shorter path to it previously and handled it.
            // This prevents processing stale entries if `updatePriority` adds new items.
            if (!map.containsKey(rp.vname)) {
                continue;
            }

            // Mark this vertex as processed by removing it from the map.
            map.remove(rp.vname);

            // Get the actual Vertex object for the removed pair.
            Vertex v = vtces.get(rp.vname);
            if (v == null) { // Defensive check, should not happen if graph is well-formed.
                continue;
            }

            // Explore all neighbors of the current vertex.
            for (String nbr : v.nbrs.keySet()) {
                // Only consider neighbors that are still in the 'map' (i.e., not yet processed).
                if (map.containsKey(nbr)) {
                    int oldCost = map.get(nbr).cost; // Current known cost to neighbor.
                    int newCost;
                    int edgeWeight = v.nbrs.get(nbr); // Weight of the edge to this neighbor (e.g., distance in KM).

                    if (isTimeCost) {
                        // Calculate new time cost: accumulated time + fixed overhead + travel time based on distance.
                        // Assuming edgeWeight (from addEdge) is 'distance units'.
                        // 120 (seconds) fixed time for transfer/waiting + 40 (seconds per distance unit) * edgeWeight.
                        newCost = rp.cost + (120 + 40 * edgeWeight);
                    } else {
                        // Calculate new distance cost: accumulated distance + edge distance.
                        newCost = rp.cost + edgeWeight;
                    }

                    // If a shorter path to the neighbor is found (relaxation step).
                    if (newCost < oldCost) {
                        DijkstraPair gp = map.get(nbr); // Get the DijkstraPair for the neighbor.
                        // CRITICAL FIX: Use a unique delimiter for path segments
                        gp.psf = rp.psf + "###" + nbr;    // Update neighbor's path.
                        gp.cost = newCost;             // Update neighbor's cost.

                        // Use the custom Heap's updatePriority to re-position the neighbor in the heap.
                        pq.updatePriority(gp);
                    }
                }
            }
        }
        // If the loop finishes and the destination was not reached, it means it's unreachable.
        return new PathResult("NO_PATH", -1, 0);
    }

    /**
     * Processes the raw path string (space-separated full airport names) from Dijkstra's,
     * calculates the number of interchanges, and formats the path for user display.
     * Includes robust checks for airport name format.
     *
     * @param rawPathResult A PathResult object containing the raw space-separated airport names and total cost.
     * @return A new PathResult object with a neatly formatted path string and interchange count.
     */
    public PathResult processPathForDisplay(PathResult rawPathResult) {
        // Handle error cases passed from Dijkstra.
        if (rawPathResult == null || rawPathResult.path == null || rawPathResult.path.isEmpty() ||
            rawPathResult.path.equals("NO_PATH") || rawPathResult.path.equals("INVALID_STATIONS")) {
            return rawPathResult; // Return the error state directly.
        }

        // CRITICAL FIX: Split the raw path string using the new unique delimiter
        String[] stations = rawPathResult.path.split("###");

        if (stations.length == 0) {
            return new PathResult("NO_PATH", -1, 0); // Should not happen with valid paths.
        }

        ArrayList<String> formattedPathSegments = new ArrayList<>();
        formattedPathSegments.add(stations[0]); // Add the starting airport as the first segment.

        int interchanges = 0;
        // Iterate through the path to find interchanges.
        for (int i = 1; i < stations.length; i++) {
            String currentStation = stations[i];
            String prevStation = stations[i - 1];

            // Ensure that both current and previous airport names contain the '~' separator
            // before attempting to use indexOf('~') and substring.
            int prevTildeIndex = prevStation.indexOf('~');
            int currentTildeIndex = currentStation.indexOf('~');

            if (prevTildeIndex == -1 || currentTildeIndex == -1) {
                // This indicates a severe issue with the airport data format within the path.
                System.err.println("Error: Malformed airport name detected in path for display. Missing '~' separator.");
                System.err.println("Problematic segment: Previous Airport: '" + prevStation + "', Current Airport: '" + currentStation + "'");
                return new PathResult("PATH_FORMAT_ERROR", -1, 0); // Return an error state.
            }

            // Extract line codes (the part before '~').
            String currentLineCode = currentStation.substring(0, currentTildeIndex);
            String prevLineCode = prevStation.substring(0, prevTildeIndex);

            // If line codes are different, it's an interchange.
            if (!currentLineCode.equals(prevLineCode)) {
                interchanges++;
                // Add an interchange instruction to the formatted path.
                formattedPathSegments.add("CHANGE FROM " + prevLineCode + " LINE TO " + currentLineCode + " LINE AT " + prevStation);
            }
            formattedPathSegments.add(currentStation); // Add the current airport.
        }

        // Build the final, human-readable path string.
        StringBuilder displayPath = new StringBuilder();
        displayPath.append("START ==> ").append(formattedPathSegments.get(0));

        for (int i = 1; i < formattedPathSegments.size(); i++) {
            displayPath.append("\n    ==> ").append(formattedPathSegments.get(i)); // Indented for clarity
        }
        displayPath.append("\n    ==> END"); // Final segment

        // Return a new PathResult with the formatted path, original cost, and calculated interchanges.
        return new PathResult(displayPath.toString(), rawPathResult.totalCost, interchanges);
    }


    // --- Flight Map Creation (Focused on India) ---

    /**
     * Populates the graph with predefined flight routes (airports as stations).
     * This version focuses primarily on major Indian domestic routes, with a few international connections.
     * Ensures consistent "CODE~FULL_NAME" format for all airport names.
     */
    public static void Create_Metro_Map(Graph_M g) {
        // Adding Vertices (Airports) - Indian Airports
        g.addVertex("DEL~Indira Gandhi International Airport, Delhi");
        g.addVertex("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai");
        g.addVertex("BLR~Kempegowda International Airport, Bengaluru");
        g.addVertex("MAA~Chennai International Airport, Chennai");
        g.addVertex("CCU~Netaji Subhas Chandra Bose International Airport, Kolkata");
        g.addVertex("HYD~Rajiv Gandhi International Airport, Hyderabad");
        g.addVertex("AMD~Sardar Vallabhbhai Patel International Airport, Ahmedabad");
        g.addVertex("PNQ~Pune Airport, Pune");
        g.addVertex("GOI~Goa International Airport (Dabolim), Goa");
        g.addVertex("COK~Cochin International Airport, Kochi");
        g.addVertex("JAI~Jaipur International Airport, Jaipur");
        g.addVertex("LKO~Chaudhary Charan Singh International Airport, Lucknow");
        g.addVertex("ATQ~Sri Guru Ram Dass Jee International Airport, Amritsar");

        // Adding a few essential International Hubs (connected from India)
        g.addVertex("LHR~Heathrow Airport, London");
        g.addVertex("DXB~Dubai International Airport, Dubai");
        g.addVertex("SIN~Changi Airport, Singapore");
        g.addVertex("JFK~John F. Kennedy International Airport, New York");
        g.addVertex("CDG~Charles de Gaulle Airport, Paris");
        g.addVertex("AUH~Abu Dhabi International Airport, Abu Dhabi"); // Added for better ME connectivity
        g.addVertex("HKG~Hong Kong International Airport, Hong Kong"); // Added for better Asia connectivity


        // --- Domestic Indian Flight Routes (approximate durations in minutes, converted to distance units) ---
        // Assuming 10 minutes ~ 1 distance unit (approximate, for demonstration)
        // Adjust these values as needed for more realistic distances/times.
        // For simplicity, using "distance units" as the edge weight.

        // Delhi (DEL) connections
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", 120); // ~2 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "BLR~Kempegowda International Airport, Bengaluru", 150); // ~2.5 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "MAA~Chennai International Airport, Chennai", 160); // ~2.7 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "CCU~Netaji Subhas Chandra Bose International Airport, Kolkata", 120); // ~2 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "HYD~Rajiv Gandhi International Airport, Hyderabad", 100); // ~1.7 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "AMD~Sardar Vallabhbhai Patel International Airport, Ahmedabad", 90); // ~1.5 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "JAI~Jaipur International Airport, Jaipur", 60); // ~1 hr
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "LKO~Chaudhary Charan Singh International Airport, Lucknow", 70); // ~1.2 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "ATQ~Sri Guru Ram Dass Jee International Airport, Amritsar", 70); // ~1.2 hrs


        // Mumbai (BOM) connections
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "BLR~Kempegowda International Airport, Bengaluru", 90); // ~1.5 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "MAA~Chennai International Airport, Chennai", 100); // ~1.7 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "HYD~Rajiv Gandhi International Airport, Hyderabad", 70); // ~1.2 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "PNQ~Pune Airport, Pune", 30); // ~0.5 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "GOI~Goa International Airport (Dabolim), Goa", 70); // ~1.2 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "AMD~Sardar Vallabhbhai Patel International Airport, Ahmedabad", 70); // ~1.2 hrs


        // Bengaluru (BLR) connections
        g.addEdge("BLR~Kempegowda International Airport, Bengaluru", "MAA~Chennai International Airport, Chennai", 60); // ~1 hr
        g.addEdge("BLR~Kempegowda International Airport, Bengaluru", "HYD~Rajiv Gandhi International Airport, Hyderabad", 60); // ~1 hr
        g.addEdge("BLR~Kempegowda International Airport, Bengaluru", "COK~Cochin International Airport, Kochi", 70); // ~1.2 hrs
        g.addEdge("BLR~Kempegowda International Airport, Bengaluru", "GOI~Goa International Airport (Dabolim), Goa", 60); // ~1 hr


        // Chennai (MAA) connections
        g.addEdge("MAA~Chennai International Airport, Chennai", "HYD~Rajiv Gandhi International Airport, Hyderabad", 75); // ~1.25 hrs
        g.addEdge("MAA~Chennai International Airport, Chennai", "COK~Cochin International Airport, Kochi", 80); // ~1.3 hrs


        // Kolkata (CCU) connections
        g.addEdge("CCU~Netaji Subhas Chandra Bose International Airport, Kolkata", "HYD~Rajiv Gandhi International Airport, Hyderabad", 120); // ~2 hrs
        g.addEdge("CCU~Netaji Subhas Chandra Bose International Airport, Kolkata", "BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", 150); // ~2.5 hrs


        // Other domestic connections
        g.addEdge("HYD~Rajiv Gandhi International Airport, Hyderabad", "GOI~Goa International Airport (Dabolim), Goa", 80); // ~1.3 hrs
        g.addEdge("PNQ~Pune Airport, Pune", "GOI~Goa International Airport (Dabolim), Goa", 60); // ~1 hr


        // --- Key International Connections (from major Indian cities) ---
        // Weights here are in 'distance units' (or effective minutes for pathfinding)
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "LHR~Heathrow Airport, London", 600); // ~10 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "DXB~Dubai International Airport, Dubai", 200); // ~3.3 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "SIN~Changi Airport, Singapore", 380); // ~6.3 hrs
        g.addEdge("BLR~Kempegowda International Airport, Bengaluru", "SIN~Changi Airport, Singapore", 300); // ~5 hrs
        g.addEdge("DEL~Indira Gandhi International Airport, Delhi", "JFK~John F. Kennedy International Airport, New York", 800); // ~13.3 hrs
        g.addEdge("BOM~Chhatrapati Shivaji Maharaj International Airport, Mumbai", "CDG~Charles de Gaulle Airport, Paris", 650); // ~10.8 hrs
        g.addEdge("HYD~Rajiv Gandhi International Airport, Hyderabad", "AUH~Abu Dhabi International Airport, Abu Dhabi", 250); // ~4.2 hrs
        g.addEdge("CCU~Netaji Subhas Chandra Bose International Airport, Kolkata", "HKG~Hong Kong International Airport, Hong Kong", 320); // ~5.3 hrs
    }

    /**
     * Extracts and returns an array of short codes for all airports.
     * It assumes airport names are in "CODE~FULL_NAME" format.
     * Includes robust error handling for missing '~'.
     * @return An array of airport codes (e.g., "DEL", "LHR").
     */
    public static String[] getStationCodes() {
        ArrayList<String> keys = new ArrayList<>(vtces.keySet());
        String[] codes = new String[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            int tildeIndex = key.indexOf('~');
            if (tildeIndex != -1) {
                codes[i] = key.substring(0, tildeIndex).toUpperCase();
            } else {
                // Fallback for names without '~', use first 3 chars or full name as code
                System.err.println("Warning: Airport name '" + key + "' is missing '~' separator. Using first few characters as code.");
                codes[i] = key.length() >= 3 ? key.substring(0, 3).toUpperCase() : key.toUpperCase();
            }
        }
        return codes;
    }

    /**
     * Prints a formatted list of all airports along with their generated short codes.
     * @param keys The list of all airport full names.
     * @param codes The array of airport short codes.
     */
    public static void printCodeList(ArrayList<String> keys, String[] codes) {
        System.out.println("\nList of airports along with their codes:\n");
        int maxLenSerial = String.valueOf(keys.size()).length(); // For dynamic spacing
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String code = codes[i];
            // Using printf for better formatted output
            System.out.printf("%" + maxLenSerial + "d. %s\t\t%s\n", (i + 1), key, code);
        }
        System.out.println();
    }


    // --- Main Application Logic ---

    /**
     * Helper method to get source and destination airport inputs from the user.
     * Provides options for input by serial number, code, or full name.
     * Includes robust input validation and error messages.
     *
     * @param inp   BufferedReader for reading user input.
     * @param keys  List of all airport full names (used for serial number input).
     * @param codes Array of airport codes (used for code input).
     * @return A String array containing [sourceAirportFullName, destinationAirportFullName], or null if input is invalid.
     * @throws IOException If an I/O error occurs during input reading.
     */
    public static String[] getSourceAndDestination(BufferedReader inp, ArrayList<String> keys, String[] codes) throws IOException {
        System.out.println("\nSelect input method:");
        System.out.println("1. Enter Serial No. of Airports");
        System.out.println("2. Enter Code of Airports");
        System.out.println("3. Enter Full Name of Airports (e.g., DEL~Indira Gandhi International Airport, Delhi)");
        System.out.print("ENTER YOUR CHOICE: ");

        int ch;
        try {
            ch = Integer.parseInt(inp.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
            return null;
        }

        String st1 = "", st2 = "";
        boolean found1 = false, found2 = false;

        if (ch == 1) { // Input by serial number
            printCodeList(keys, codes); // Display the list for user to pick serial numbers
            System.out.print("Enter source airport serial number: ");
            int index1;
            try {
                index1 = Integer.parseInt(inp.readLine()) - 1; // Adjust for 0-based index
                if (index1 >= 0 && index1 < keys.size()) {
                    st1 = keys.get(index1);
                    found1 = true;
                } else {
                    System.out.println("Invalid source serial number. Please choose from the list.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number for source serial.");
            }

            if (found1) { // Only ask for destination if source was valid
                System.out.print("Enter destination airport serial number: ");
                int index2;
                try {
                    index2 = Integer.parseInt(inp.readLine()) - 1; // Adjust for 0-based index
                    if (index2 >= 0 && index2 < keys.size()) {
                        st2 = keys.get(index2);
                        found2 = true;
                    } else {
                        System.out.println("Invalid destination serial number. Please choose from the list.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number for destination serial.");
                }
            }

        } else if (ch == 2) { // Input by code
            printCodeList(keys, codes); // Display the list for user to pick codes
            System.out.print("Enter source airport code: ");
            String a = (inp.readLine()).trim().toUpperCase();
            for (int j = 0; j < codes.length; j++) {
                if (a.equals(codes[j])) {
                    st1 = keys.get(j);
                    found1 = true;
                    break;
                }
            }
            if (!found1) {
                System.out.println("Invalid source airport code.");
            }

            if (found1) { // Only ask for destination if source was valid
                System.out.print("Enter destination airport code: ");
                String b = (inp.readLine()).trim().toUpperCase();
                for (int j = 0; j < codes.length; j++) {
                    if (b.equals(codes[j])) {
                        st2 = keys.get(j);
                        found2 = true;
                        break;
                    }
                }
                if (!found2) {
                    System.out.println("Invalid destination airport code.");
                }
            }

        } else if (ch == 3) { // Input by full name
            System.out.print("Enter source airport full name (e.g., DEL~Indira Gandhi International Airport, Delhi): ");
            st1 = inp.readLine().trim();
            if (vtces.containsKey(st1)) {
                found1 = true;
            } else {
                System.out.println("Invalid source airport full name.");
            }

            if (found1) { // Only ask for destination if source was valid
                System.out.print("Enter destination airport full name (e.g., LHR~Heathrow Airport, London): ");
                st2 = inp.readLine().trim();
                if (vtces.containsKey(st2)) {
                    found2 = true;
                } else {
                    System.out.println("Invalid destination airport full name.");
                }
            }
        } else {
            System.out.println("Invalid choice. Please choose 1, 2, or 3.");
            return null;
        }

        // Final check if both airports were successfully found/validated
        if (!found1 || !found2) {
            return null; // Return null if any airport input was invalid
        }

        return new String[]{st1, st2};
    }


    public static void main(String[] args) throws IOException {
        Graph_M g = new Graph_M();
        Create_Metro_Map(g); // Populate the graph with airports and connections

        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("\n\t\t\t****WELCOME TO THE GLOBAL FLIGHT APP*****\n");

        while (true) {
            System.out.println("\n\t\t\t\t~~LIST OF ACTIONS~~\n");
            System.out.println("1. LIST ALL THE AIRPORTS IN THE MAP");
            System.out.println("2. SHOW THE FLIGHT ROUTES MAP");
            System.out.println("3. GET SHORTEST DISTANCE (NUMERIC VALUE) FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("4. GET SHORTEST TIME (NUMERIC VALUE) TO REACH FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("5. GET SHORTEST PATH (DISTANCE WISE) TO REACH FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("6. GET SHORTEST PATH (TIME WISE) TO REACH FROM A 'SOURCE' TO 'DESTINATION'");
            System.out.println("7. EXIT THE MENU");
            System.out.print("\nENTER YOUR CHOICE FROM THE ABOVE LIST (1 to 7) : ");

            int choice = -1;
            try {
                choice = Integer.parseInt(inp.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number (1-7).");
                continue; // Continue to the next loop iteration
            }
            System.out.print("\n***********************************************************\n");

            if (choice == 7) {
                System.out.println("Thank you for using the Global Flight App!");
                System.exit(0);
            }

            switch (choice) {
                case 1:
                    g.display_Stations();
                    break;

                case 2:
                    g.display_Map();
                    break;

                case 3: // Get shortest distance (value only)
                case 4: // Get shortest time (value only)
                case 5: // Get shortest path (distance wise)
                case 6: // Get shortest path (time wise)
                    ArrayList<String> allAirports = new ArrayList<>(vtces.keySet());
                    String[] airportCodes = getStationCodes(); // Get codes (with robustness)

                    // Use the helper method to get validated source and destination airports
                    String[] selectedAirports = getSourceAndDestination(inp, allAirports, airportCodes);
                    if (selectedAirports == null) {
                        // Error message already printed by getSourceAndDestination
                        break;
                    }
                    String source = selectedAirports[0];
                    String destination = selectedAirports[1];

                    // Perform a path existence check only if the airports themselves were valid inputs.
                    // This is a quick check before running Dijkstra on potentially disconnected subgraphs.
                    HashMap<String, Boolean> processedForPathCheck = new HashMap<>();
                    if (!g.hasPath(source, destination, processedForPathCheck)) {
                        System.out.println("NO PATH EXISTS BETWEEN " + source + " AND " + destination + ".");
                        break;
                    }

                    if (choice == 3) {
                        PathResult result = g.dijkstra(source, destination, false); // Calculate distance
                        if (result.totalCost != -1 && !result.path.equals("NO_PATH")) {
                            System.out.println("SHORTEST DISTANCE FROM " + source + " TO " + destination + " IS " + result.totalCost + " KM\n");
                        } else {
                            System.out.println("Could not calculate shortest distance. Error: " + result.path);
                        }
                    } else if (choice == 4) {
                        PathResult result = g.dijkstra(source, destination, true); // Calculate time
                        if (result.totalCost != -1 && !result.path.equals("NO_PATH")) {
                            // Dijkstra's time cost is in seconds, convert to minutes for display
                            double shortestTimeInMinutes = Math.ceil((double) result.totalCost / 60);
                            System.out.println("SHORTEST TIME FROM " + source + " TO " + destination + " IS " + shortestTimeInMinutes + " MINUTES\n");
                        } else {
                            System.out.println("Could not calculate shortest time. Error: " + result.path);
                        }
                    } else if (choice == 5) {
                        PathResult rawResult = g.dijkstra(source, destination, false); // Get raw distance path
                        PathResult finalResult = g.processPathForDisplay(rawResult); // Process for display and interchanges

                        if (finalResult.path.equals("NO_PATH") || finalResult.path.equals("INVALID_STATIONS") || finalResult.path.equals("PATH_FORMAT_ERROR")) {
                            System.out.println("Could not find a shortest path (distance wise). Error: " + finalResult.path);
                        } else {
                            System.out.println("SOURCE AIRPORT: " + source);
                            System.out.println("DESTINATION AIRPORT: " + destination);
                            System.out.println("TOTAL DISTANCE: " + finalResult.totalCost + " KM");
                            System.out.println("NUMBER OF INTERCHANGES: " + finalResult.interchangeCount);
                            System.out.println("~~~~~~~~~~~~~");
                            System.out.println(finalResult.path); // Prints the fully formatted path
                            System.out.println("~~~~~~~~~~~~~");
                        }
                    } else if (choice == 6) {
                        PathResult rawResult = g.dijkstra(source, destination, true); // Get raw time path
                        PathResult finalResult = g.processPathForDisplay(rawResult); // Process for display and interchanges

                        if (finalResult.path.equals("NO_PATH") || finalResult.path.equals("INVALID_STATIONS") || finalResult.path.equals("PATH_FORMAT_ERROR")) {
                            System.out.println("Could not find a shortest path (time wise). Error: " + finalResult.path);
                        } else {
                            // Convert total cost (in seconds) to minutes for display
                            double timeInMinutes = Math.ceil((double) finalResult.totalCost / 60);
                            System.out.println("SOURCE AIRPORT: " + source);
                            System.out.println("DESTINATION AIRPORT: " + destination);
                            System.out.println("TOTAL TIME: " + timeInMinutes + " MINUTES");
                            System.out.println("NUMBER OF INTERCHANGES: " + finalResult.interchangeCount);
                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                            System.out.println(finalResult.path); // Prints the fully formatted path
                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        }
                    }
                    break; // End of combined case for pathfinding

                default:
                    System.out.println("Please enter a valid option! The options you can choose are from 1 to 7.");
            }
        }
    }
}
