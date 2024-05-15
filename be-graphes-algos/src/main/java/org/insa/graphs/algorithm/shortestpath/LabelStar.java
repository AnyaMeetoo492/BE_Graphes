package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private ShortestPathData data;

    public LabelStar(Node sommet_courant, boolean marque, double cout_realise, Node pere, ShortestPathData data){
        super(sommet_courant,marque,cout_realise,pere);
        this.data = data;
    }

    @Override
    public double getCost(){
      // Prend le cout realisé et l'ajoute à la distance du point courrant jusqu'à la destination
      return this.getcout_realise() + this.getsommet_node().getPoint().distanceTo(this.data.getDestination().getPoint());
    }
  }