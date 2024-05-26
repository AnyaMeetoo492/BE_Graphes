package org.insa.graphs.algorithm.shortestpath;

public class DijkstraAlgoritmTest extends ShortestPathAlgorithmTest {

  // Retrive Dijkstra Solution from data given
  @Override
  public ShortestPathAlgorithm createAlgorithm(ShortestPathData data){
    return new DijkstraAlgorithm(data);
  }
  
}
