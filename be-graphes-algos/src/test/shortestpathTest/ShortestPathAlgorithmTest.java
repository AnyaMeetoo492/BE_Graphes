import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.beans.Transient;
import java.io.IOException;
import java.util.Collection;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;

@RunWith(Parameterized.class)
public abstract class ShortestPathAlgorithmTest {

  // READ GRAPH
  private static Graph read(String mapName) throws IOException {
    // Create a graph reader.
    GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    return reader.read();
  }

  // GET DATA
  @Parameterized.Parameter
  public static Collection<Object> data() throws IOException{
    Collection<Object> data = new ArrayList<>();

    // LOAD GRAPH
    final Graph insa = read("C:/Users/anya/Pictures/BE_Graphes/insa.mapgr");
    final Graph belgium = read("C:/Users/anya/Pictures/BE_Graphes/belgium.mapgr");

    // load data
    //ShortestPathData(Graph graph, Node origin, Node destination, ArcInspector arcInspector)

    //valid path
    data.add(new ShortestPathData(insa, insa.get(479), insa.get(702), ArcInspectorFactory.getAllFilters().get(0))); // shortest
    data.add(new ShortestPathData(insa, insa.get(479), insa.get(702), ArcInspectorFactory.getAllFilters().get(2))); // fastest

    data.add(new ShortestPathData(belgium, belgium.get(60975), belgium.get(804592), ArcInspectorFactory.getAllFilters().get(0))); // shortest
    data.add(new ShortestPathData(belgium, belgium.get(60975), belgium.get(804592), ArcInspectorFactory.getAllFilters().get(2))); // fastest

    //invalid path
    data.add(new ShortestPathData(insa, insa.get(186)), insa.get(864), ArcInspectorFactory.getAllFilters().get(0));

    //origin to origin, path length null
    data.add(new ShortestPathData(insa, insa.get(479), insa.get(479), ArcInspectorFactory.getAllFilters().get(0)));
    data.add(new ShortestPathData(belgium, belgium.get(60975), belgium.get(60975), ArcInspectorFactory.getAllFilters().get(2))); // fastest

    return data;
  }

  // Retrive Algo from data given
  public abstract ShortestPathAlgorithm createAlgorithm(ShortestPathData data);

  @Parameterized.Parameter
  private ShortestPathData data;
  
  private ShortestPathAlgorithm algo;
  private ShortestPathSolution solution;


  @BeforeClass
  public void init(){
    this.algo = this.createAlgorithm(this.data);
    this.solution = this.algo.run();
  }

  //Verify that solution path is valid
  @Test
  public void VerifyValidity(){
    assertTrue(this.solution.getPath().isValid());
  }

  //Verify that origin solution is origin data given
  @Test
  public void VerifyOrigin(){
    assertEquals(this.data.getOrigin(), this.solution.getInputData.getOrigin());
  }

  //Verify that destination solution is destination data given
  @Test
  public void VerifyDestination(){
    assertEquals(this.data.getDestination(), this.solution.getInputData.getDestination());
  }

  //Verify that solution is equal to solution given by Bellman Ford Algorithm
  @Test
  public void VerifyEqualsBellmanFord(){
    BellmanFordAlgorithm bellmanfordAlgo = new BellmanFordAlgorithm(this.data);
    ShortestPathSolution bellmanfordSolution = bellmanfordAlgo.run();

    //verify both solution are feasible or not
    assertSame(this.solution.isFeasible(), bellmanfordSolution.isFeasible());

    if (this.solution.isFeasible()){
      //verify if solution has same length as Bellman Ford Solution
      assertEquals(this.solution.getPath().getLength(), bellmanfordSolution.getPath().getLength());
      assertSame(this.solution.getPath().getArcs().size(), bellmanfordSolution.getPath().getArcs().size());

      //Verify that each arc of soltion is the same as arc of Bellman Ford Solution
      for (int i = 0; i < this.solution.getPath().getArcs().size(); i++) {
        assertSame(this.solution.getPath().getArcs().get(i).getDestination(), bellmanfordSolution.getPath().getArcs().get(i).getDestination());
      }
    }
    else{
      return; // solution unFeasible
    }

  }
  
}
