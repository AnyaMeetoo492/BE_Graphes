package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class LabelStar extends Label {

    private ShortestPathData data;

    // CONSTRUCTOR (same as Label class + addition of data of current label)
    public LabelStar(Node sommet_courant, boolean marque, double cout_realise, Node pere, ShortestPathData data){
        super(sommet_courant,marque,cout_realise,pere);
        this.data = data;
    }

    @Override
    public double getCost(){
      // On prédit un cout pour tout le chemin (origine - point courant - destination)
      // getcout_realise : cout de l'origine au node courant
      // + cout du point jusqu'à la destination

      double cout_depuis_origine = this.getcout_realise();
      return cout_depuis_origine;
    }

    @Override
    public int compareTo(Label l){
      // COMPARE betweeen 
      //cost from origin to l to destination and 
      //cost from origin to current label to destination
      
      double total_l = l.getCost() + this.getsommet_node().getPoint().distanceTo(this.data.getDestination().getPoint());
      double total_this = this.getCost() + this.getsommet_node().getPoint().distanceTo(this.data.getDestination().getPoint());
      if (total_l > total_this){
          return -1;
      }
      else if (total_l < total_this){
          return 1;
      }
      return 0;
  }
  }