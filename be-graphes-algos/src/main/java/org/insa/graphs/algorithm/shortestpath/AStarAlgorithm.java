package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        
    }

    @Override
    public Label ChangeLabel(Node sommet_courant, boolean marque, double cout_realise, Node pere){
        return new LabelStar(sommet_courant, marque, cout_realise, pere, this.getInputData());
    }

    

}
