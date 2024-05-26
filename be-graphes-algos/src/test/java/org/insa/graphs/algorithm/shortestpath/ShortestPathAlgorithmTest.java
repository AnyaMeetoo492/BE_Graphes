package org.insa.graphs.algorithm.shortestpath;

//import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspectorFactory;
//import org.insa.graphs.algorithm.utils.PriorityQueue;
//import org.insa.graphs.algorithm.utils.PriorityQueueTest;
import org.insa.graphs.model.Graph;
//import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public abstract class ShortestPathAlgorithmTest {

  // READ GRAPH
  private static Graph read(String mapName) throws IOException {
    // Create a graph reader.
    GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    return reader.read();
  }

  // GET DATA
  @Parameterized.Parameters
  public static Collection<Object> data() throws IOException{
    Collection<Object> data = new ArrayList<>();

    // LOAD GRAPH
    final Graph insa = read("/home/hindi/Bureau/3MIC/BE_Graphes/maps/insa.mapgr");
    final Graph belgium = read("/home/hindi/Bureau/3MIC/BE_Graphes/maps/belgium.mapgr");

    // load data
    //ShortestPathData(Graph graph, Node origin, Node destination, ArcInspector arcInspector)

    //valid path
    data.add(new ShortestPathData(insa, insa.get(479), insa.get(702), ArcInspectorFactory.getAllFilters().get(0))); // shortest
    data.add(new ShortestPathData(insa, insa.get(479), insa.get(702), ArcInspectorFactory.getAllFilters().get(2))); // fastest

    data.add(new ShortestPathData(belgium, belgium.get(60975), belgium.get(804592), ArcInspectorFactory.getAllFilters().get(0))); // shortest
    data.add(new ShortestPathData(belgium, belgium.get(60975), belgium.get(804592), ArcInspectorFactory.getAllFilters().get(2))); // fastest

    //invalid path
    data.add(new ShortestPathData(insa, insa.get(186), insa.get(864), ArcInspectorFactory.getAllFilters().get(0)));

    //origin to origin, path length null
    data.add(new ShortestPathData(insa, insa.get(479), insa.get(479), ArcInspectorFactory.getAllFilters().get(0)));
    data.add(new ShortestPathData(belgium, belgium.get(60975), belgium.get(60975), ArcInspectorFactory.getAllFilters().get(2))); // fastest

    return data;
  }

  // Retrive Algo from data given
  public abstract ShortestPathAlgorithm createAlgorithm(ShortestPathData data);

  @Before
  public void init(){
    this.algo = this.createAlgorithm(this.data);
    this.solution = this.algo.run();
  }

  public ShortestPathAlgorithm algo;
  private ShortestPathSolution solution;

  @Parameterized.Parameter
  public ShortestPathData data;

  //Verify that solution path is valid
  @Test
  public void VerifyValidity(){
    Assume.assumeTrue(this.solution.isFeasible());
    assertTrue(this.solution.getPath().isValid());
  }

  //Verify that origin solution is origin data given
  @Test
  public void VerifyOrigin(){
    Assume.assumeTrue(this.solution.isFeasible());
    assertEquals(this.data.getOrigin(), this.solution.getInputData().getOrigin());
  }

  //Verify that destination solution is destination data given
  @Test
  public void VerifyDestination(){
    Assume.assumeTrue(this.solution.isFeasible());
    assertEquals(this.data.getDestination(), this.solution.getInputData().getDestination());
  }

  //Verify that solution is equal to solution given by Bellman Ford Algorithm
  @Test
  public void VerifyEqualsBellmanFord(){
    Assume.assumeTrue(this.data.getGraph().getNodes().size() <= 5000);

    BellmanFordAlgorithm bellmanfordAlgo = new BellmanFordAlgorithm(this.data);
    ShortestPathSolution bellmanfordSolution = bellmanfordAlgo.run();

    assertSame(bellmanfordSolution.getStatus(), this.solution.getStatus());

    if (!this.solution.isFeasible()) return;
    
    //verify if solution has same length as Bellman Ford Solution
    assertSame(this.solution.getPath().getLength(), bellmanfordSolution.getPath().getLength());
    //verify both solution are feasible or not
    assertSame(this.solution.isFeasible(), bellmanfordSolution.isFeasible());

    if (!this.solution.isFeasible()) {
      return;
    }

    assertSame(this.solution.getPath().getArcs().size(), bellmanfordSolution.getPath().getArcs().size());

    //Verify that each arc of soltion is the same as arc of Bellman Ford Solution
    for (int i = 0; i < this.solution.getPath().getArcs().size(); i++) {
      assertSame(this.solution.getPath().getArcs().get(i).getDestination(), bellmanfordSolution.getPath().getArcs().get(i).getDestination());
    }

  }
  
}
