// package org.insa.graphs.algorithm.shortestpath;
// import java.util.List;
// import java.util.ArrayList;
// //import java.util.Comparator;
// import java.util.Map;
// import org.insa.graphs.algorithm.AbstractSolution.Status;
// import org.insa.graphs.algorithm.utils.BinaryHeap;
// import org.insa.graphs.model.Arc;
// import org.insa.graphs.model.Graph;
// import org.insa.graphs.model.Node;
// import org.insa.graphs.model.Path;

// import java.util.HashMap;

// public class DijkstraAlgorithm extends ShortestPathAlgorithm {

//     private Map<Node, Label> maplabel; // MAP : linking node with label

//     // CONSTRUCTOR
//     public DijkstraAlgorithm(ShortestPathData data) {
//         super(data);
//     }

//     // LABELLING NODES
//     public Label ChangeLabel(Node sommet_courant, boolean marque, double cout_realise, Node pere) {
//         return new Label(sommet_courant, marque, cout_realise, pere);
//     }

//     // ALGORITHM TO FIND SHORTEST PATH
//     @Override
//     protected ShortestPathSolution doRun() {
//         final ShortestPathData data = getInputData();
//         ShortestPathSolution solution = null;
//         BinaryHeap<Label> pile = new BinaryHeap<>();

//         boolean reached = false; // if Shortest Path found

//         // Retrieve the graph.
//         Graph graph = data.getGraph();
//         maplabel = new HashMap<>(); // Create a map

//         // Notify observers about the first event (origin processed).
//         notifyOriginProcessed(data.getOrigin());

//         // association nodes a un label
//         Label originlabel = this.ChangeLabel(data.getOrigin(), false, 0, null); // first node
//         maplabel.put(data.getOrigin(), originlabel); // Insert in map first node

//         pile.insert(maplabel.get(data.getOrigin())); // Insert the origin in stack 

//         while (!pile.isEmpty() && !reached) { // until all nodes are processed or shorstest path found

//             Label currentLabel = pile.deleteMin(); // take mininum from the stack
//             currentLabel.setmarque(true); // marked as processed true

//             notifyNodeMarked(currentLabel.getsommet_node()); // notify node marked

//             if (currentLabel.getsommet_node() == data.getDestination()) { // if destination reached
//                 reached = true; // shortest path found true
//             }

//             for (Arc successorArc : currentLabel.getsommet_node().getSuccessors()) { // find all successors arcs of current node
//                 // NOTE : succ is an arc with (origin, destination) = (currentnode, successorsOfCurrentNode)
//                 Label currentSucc = maplabel.get(successorArc.getDestination()); // take a successor
//                 notifyNodeReached(successorArc.getDestination()); // notify current node reached

//                 if (maplabel.containsKey(successorArc.getDestination()) && currentSucc.getmarque()) { //if already processed = already in map and marked
//                     continue; // skip for this step of the for loop
//                 }

//                 double currentCost = Double.POSITIVE_INFINITY; 

//                 if (currentSucc != null) { // if successor exists, find cost
//                     currentCost = currentSucc.getCost();
//                 }

//                 double costArc = data.getCost(successorArc); // cost arc from current node to successor
//                 double newDistance = currentLabel.getCost() + costArc; // calculating new distance = costprevious + arc 

//                 if (newDistance < currentCost) { // if path is shortest to current node
                    
//                     Label newdest = this.ChangeLabel(successorArc.getDestination(), reached, newDistance, successorArc.getOrigin());

//                     maplabel.put(successorArc.getDestination(), newdest); // Add (node,label) to map
//                     if (currentSucc != null) { // if successor exists
//                         pile.remove(currentSucc); // remove from stack
//                     }
//                     pile.insert(newdest); // add new label to stack
//                 }
//             }
//         }
//         Label destlabel = maplabel.get(data.getDestination()); // get destination label

//         if (destlabel == null) { // if destination doesn't exist
//             solution = new ShortestPathSolution(data, Status.INFEASIBLE); // SOLUTION DOES NOT EXIST
//         } 
//         else { // if destination exists

//             notifyDestinationReached(data.getDestination()); // notify destination reached

//             ArrayList<Arc> arcsShortestPath = new ArrayList<>(); // List of new arcs for shortest path

//             Label currentlabel = destlabel; // start at destination

//             // Create the path in reverse direction (destination to origin)
//             while (currentlabel.getsommet_node() != data.getOrigin()) { // until orgin is reached
//                 Label label = currentlabel; // get new point 
//                 Node pere = currentlabel.getpere(); // find father of current point

//                 List<Arc> successorsOfPere = pere.getSuccessors(); // get successors of father

//                 // find link between one/more fathers and find the arc with minimum cost
//                 Arc arcfound = null;
//                 double mininumcost = Double.POSITIVE_INFINITY; 
//                 for (Arc successorArcPere : successorsOfPere){ // visit all successors of father
//                     if (successorArcPere.getDestination() == label.getsommet_node()){ // if equal to current node
//                         if (label.getCost()<mininumcost){
//                             mininumcost = label.getCost();
//                             arcfound = successorArcPere; // found the arc
//                         }
//                     }
//                 } 

//                 if (arcfound == null) { // if no arc is found
//                     throw new RuntimeException("no arc found");
//                 }

//                 ///////////////////////////////////////////
//                 // Arc arc = pere.getSuccessors().stream()
//                 //         .filter(successorArc -> successorArc.getDestination() == label.getsommet_node())
//                 //         .min(Comparator.comparingDouble(data::getCost)).orElse(null);

//                 arcsShortestPath.add(0, arcfound);
//                 currentlabel = maplabel.get(pere); // switch to father node now (next node)
//             }

//             // Create the final solution.
//             solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcsShortestPath));
//         }

//         return solution;
//     }

// }
