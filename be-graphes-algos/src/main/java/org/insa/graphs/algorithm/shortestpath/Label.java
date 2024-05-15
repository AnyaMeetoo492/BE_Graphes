package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {

    private Node sommet_courant;
    private boolean marque;
    private double cout_realise;
    private Node pere;

    public Label(Node sommet_courant, boolean marque, double cout_realise, Node pere){
        this.sommet_courant = sommet_courant;
        this.marque = marque;
        this.cout_realise = cout_realise;
        this.pere = pere;
    }

    public Node getsommet_node(){
        return this.sommet_courant;
    }

    public boolean getmarque(){
        return this.marque;
    }

    public void setmarque(boolean value){
        this.marque = value;
    }

    public double getcout_realise(){
        return this.cout_realise;
    }

    public Node getpere(){
        return this.pere;
    }

    public double getCost(){
        return this.cout_realise;
    }

    public int compareTo(Label l){
        if (l.getCost() > this.getCost()){
            return -1;
        }
        else if (l.getCost() < this.getCost()){
            return 1;
        }
        return 0;
    }
}
