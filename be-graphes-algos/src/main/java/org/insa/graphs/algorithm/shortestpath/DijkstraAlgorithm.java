package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.Map;
import java.util.HashMap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    private Map <Node, Label> maplabel;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        BinaryHeap<Label> pile = new BinaryHeap<>();

        boolean found = false;

        // Retrieve the graph.
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        // association nodes a un label
        maplabel= new HashMap<>();
        Label originlabel = new Label(data.getOrigin(), false, 0, null);
        maplabel.put(data.getOrigin(), originlabel);

        pile.insert(maplabel.get(data.getOrigin()));

        while (!found && !pile.isEmpty()){

            Label currentLabel = pile.deleteMin();
            currentLabel.setmarque(true);

            double d = Double.POSITIVE_INFINITY;
            
            for(Arc succ : currentLabel.getsommet_node().getSuccessors()){
                Label prochain = new Label(succ.getDestination(), false, data.getCost(succ) + currentLabel.getCost(), currentLabel.getsommet_node());
                if (prochain.getsommet_node() == data.getDestination()){
                    found = true;
                    d = data.getCost(succ);
                }
                else{
                    if (!prochain.getmarque()){
                        pile.insert(maplabel.get(prochain.getsommet_node()));
                        if (data.getCost(succ)<d){
                            d = data.getCost(succ);
                        }
                    }
                }
                predecessorArcs[succ.getDestination().getId()] = succ;
                
            }
        }

        if (!found){
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else{
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        return solution;
    }

}
