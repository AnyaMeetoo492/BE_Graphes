package org.insa.graphs.algorithm.shortestpath;

public class AStarAlgorithmTest extends ShortestPathAlgorithmTest{

  // Retrive AStar Algo from data given
  @Override
  public ShortestPathAlgorithm createAlgorithm(ShortestPathData data){
    return new AStarAlgorithm(data);
  }
  
}
