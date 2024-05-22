package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import javax.management.RuntimeErrorException;
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
  private Map<Node, Label> maplabel;

  public DijkstraAlgorithm(ShortestPathData data) {
    super(data);
  }

  public Label ChangeLabel(Node sommet_courant, boolean marque, double cout_realise, Node pere) {
    return new Label(sommet_courant, marque, cout_realise, pere);
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
    maplabel = new HashMap<>();
    Label originlabel = this.ChangeLabel(data.getOrigin(), false, 0, null);
    maplabel.put(data.getOrigin(), originlabel);

    pile.insert(maplabel.get(data.getOrigin()));

    boolean reached = false;

    while (!pile.isEmpty() && !reached) {

      Label currentLabel = pile.deleteMin();
      currentLabel.setmarque(true);

      if (currentLabel.getsommet_node() == data.getDestination()) {
        reached = true;
      }

      notifyNodeMarked(currentLabel.getsommet_node());

      for (Arc succ : currentLabel.getsommet_node().getSuccessors()) {

        Label prochain = maplabel.get(succ.getDestination());
        if (maplabel.containsKey(succ.getDestination()) && prochain.getmarque()) {
          continue;
        }
        double d = Double.POSITIVE_INFINITY;
        if (prochain != null) {
          d = prochain.getCost();
        }
        double cost = data.getCost(succ);
        double newDistance = currentLabel.getCost() + cost;
        notifyNodeReached(succ.getDestination());
        if (newDistance < d) {
          Label newdest = this.ChangeLabel(succ.getDestination(), found, newDistance, succ.getOrigin());
          maplabel.put(succ.getDestination(), newdest);
          if (prochain != null) {
            pile.remove(prochain);
          }
          pile.insert(newdest);
        }
      }
    }
    Label destlabel = maplabel.get(data.getDestination());

    if (destlabel == null) {
      solution = new ShortestPathSolution(data, Status.INFEASIBLE);
    } else {
      notifyDestinationReached(data.getDestination());
      ArrayList<Arc> arcs = new ArrayList<>();
      Label currentlabel = destlabel;
      // Create the path from the array of predecessors...
      while (currentlabel.getsommet_node() != data.getOrigin()) {
        Label label = currentlabel;
        Node father = currentlabel.getpere();
        Arc arc = father.getSuccessors().stream().filter(succ -> succ.getDestination() == label.getsommet_node())
            .min(Comparator.comparingDouble(data::getCost)).orElse(null);
        if (arc == null) {
          throw new RuntimeException("no arc found");
        }
        arcs.add(0, arc);
        currentlabel = maplabel.get(father);
      }
      // Create the final solution.
      solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
    }
    return solution;
  }
}