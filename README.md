# ✈️ Global Flight Routes 

## An Application for Optimizing Flight Paths using Graph Algorithms

---

## Table of Contents

* [About the Project](#about-the-project)
* [Features](#features)
* [Technologies Used](#technologies-used)
* [Algorithms & Data Structures](#algorithms--data-structures)
* [How to Run](#how-to-run)
    * [Live Web Application (Recommended)](#live-web-application-recommended)
    * [Java Console Version](#java-console-version)
* [Project Structure](#project-structure)
* [Future Enhancements](#future-enhancements)
* [Contributing](#contributing)
* [License](#license)

---

## About the Project

This project is a flight route optimization application designed to demonstrate fundamental computer science concepts in a practical scenario. It allows users to find the shortest flight paths (by both distance and estimated time) between various airports. The current data set focuses primarily on major Indian domestic routes and essential international connections.

Initially developed as a robust Java console application, the project has been significantly enhanced with a responsive, interactive web-based frontend. This frontend is built using HTML, CSS (Tailwind CSS), and pure JavaScript, showcasing the adaptability of the core graph and pathfinding logic across different platforms and providing a more intuitive user experience.

## Features

* **Airport Management:**
    * List all available airports in the network.
    * Display the complete flight map showing connections and their associated weights (distances/time units).
* **Shortest Path Calculation (Numeric Value):**
    * Calculate and display the **shortest distance** (in Kilometers) between any two selected airports.
    * Calculate and display the **shortest estimated time** (in Minutes) to travel between any two selected airports.
* **Shortest Path Details:**
    * Retrieve and display the **full shortest path (distance-wise)**, including all intermediate airports and the number of interchanges required.
    * Retrieve and display the **full shortest path (time-wise)**, including all intermediate airports and the number of interchanges required.
* **Intuitive User Interface:**
    * **Web Frontend:** Provides a clean, responsive layout with interactive dropdowns for airport selection and clear buttons for various flight queries.
    * **Console Frontend:** Offers a menu-driven interface, allowing airport selection by serial number, IATA code, or full airport name.
* **Robust Error Handling:** Includes comprehensive error checks for invalid inputs, non-existent airports, and cases where no path can be found between selected airports, providing clear user feedback.

## Technologies Used

* **Core Logic (Backend/Algorithms):** Java, JavaScript (for web frontend re-implementation)
* **Frontend (Web):**
    * HTML5 (Structure)
    * CSS3 (Styling, with [Tailwind CSS CDN](https://tailwindcss.com/docs/installation/play-cdn) for utility-first responsiveness)
    * Vanilla JavaScript (Interactive logic, re-implementation of graph algorithms)
* **Development & Deployment:**
    * Java Development Kit (JDK)
    * Git & GitHub Pages (for version control and web hosting)

## Algorithms & Data Structures

This project showcases a practical application of core computer science fundamentals:

* **Graphs:**
    * **Representation:** The flight network is efficiently modeled using an **Adjacency List** representation. Each airport is a vertex, and flight routes are weighted edges.
* **Dijkstra's Algorithm:**
    * The heart of the pathfinding functionality, implemented from scratch. It's used to determine the shortest path in a weighted graph, adaptable for both distance and estimated time metrics.
* **Heap (Min-Priority Queue):**
    * A custom-built **Min-Heap** data structure is crucial for optimizing Dijkstra's algorithm. It ensures efficient retrieval of the unvisited vertex with the smallest current cost. The implementation includes `add`, `remove`, `peek`, and `updatePriority` operations, leveraging a `HashMap`/`Map` for `O(1)` item lookup.
* **Depth-First Search (DFS):**
    * A basic DFS is employed to quickly check for the existence of a path between two airports, preventing unnecessary Dijkstra runs on disconnected graph components.

## How to Run

### Live Web Application (Recommended)

The interactive web frontend is hosted directly on GitHub Pages, providing instant access.

* **Access the Live Demo:**
    * Click here: **`https://priyakumar-15.github.io/-Global-Flight-Routes-App/`** (This is your live URL. Double-check this exact URL once GitHub Pages is fully deployed.)

### Java Console Version

The original Java application demonstrates the backend logic in a command-line environment.

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/priyakumar-15/-Global-Flight-Routes-App.git](https://github.com/priyakumar-15/-Global-Flight-Routes-App.git)
    cd -Global-Flight-Routes-App # Navigate into the cloned directory
    ```
2.  **Move to the `src` Directory (if needed):**
    * If your Java source files (`Graph_M.java`, `Heap.java`) are inside a `src` folder (as is common for Java projects), navigate into it:
        ```bash
        cd src
        ```
    * (If your `.java` files are directly in the repository root, you can skip this `cd` command.)
3.  **Compile the Java Code:**
    * Ensure you have a Java Development Kit (JDK) installed (JDK 8 or higher is generally recommended).
    * Compile both Java files:
        ```bash
        javac Graph_M.java Heap.java
        ```
        *(This will create `.class` files in the same directory.)*
4.  **Run the Application:**
    * Stay in the directory where your `.class` files were created (e.g., `src` folder or root).
    * Execute the main class:
        ```bash
        java Graph_M
        ```
    * The application will launch in your terminal, presenting a menu-driven interface.

## Project Structure

This repository contains the following key files:

* `Graph_M.java`: The main Java class for the flight graph, including graph operations and the Dijkstra's algorithm implementation.
* `Heap.java`: The custom Min-Heap data structure used by the Java version of the graph algorithms.
* `index.html`: The complete web-based frontend for the flight application, containing HTML, CSS (via Tailwind CDN), and JavaScript (including the re-implemented graph algorithms).
* `README.md`: This file provides an overview and instructions.

*(Note: Your Java files (`Graph_M.java`, `Heap.java`) should ideally be in a `src` subdirectory if you're following typical Java project conventions, but they need to be accessible for compilation.)*
## Future Enhancements (Ideas for further development)

* **Interactive Map Visualization:** Integrate a JavaScript mapping library (e.g., Leaflet, Google Maps API) to represent airports and flight paths on a world map visually.
* **Real-time Data Integration:** (Advanced) Explore connecting to real-time flight data APIs (e.g., FlightAware, FlightStats) for live updates on flight statuses, delays, and dynamic pricing.
* **User Accounts & Saved Routes:** Implement user authentication and enable users to save their favorite routes for quick access.
* **More Sophisticated Cost Metrics:** Incorporate additional factors into the path calculation, such as layover times, time zone changes, different aircraft types and speeds, or airline preferences.
* **Filter Options:** Add options to filter flight searches by airline, direct flights only, number of stops, etc.
* **Refined UI/UX:** Enhance the user interface with loading indicators, clearer success/error messages, and more visually appealing components.
* **Unit Testing:** Develop comprehensive unit tests for the Heap and Graph algorithms to ensure their correctness and robustness.

## Contributing

Feel free to fork this repository, explore the code, and suggest improvements or new features! All contributions are welcome.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature')
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request
