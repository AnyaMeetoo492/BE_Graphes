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
      double cout_jusqua_destination = this.getsommet_node().getPoint().distanceTo(this.data.getDestination().getPoint());
      return cout_depuis_origine + cout_jusqua_destination;
    }
  }