import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner=new Scanner(new File("graph.in"));
        PrintStream output=new PrintStream(new File("bexpr.out"));
        Graph graph=new Graph(scanner);

        if(graph.quickExclude()) {
            output.println(Graph.x(1, 1) + "&~" + Graph.x(1, 1));
            return;
        }

        output.print(graph.Edges()+"&");
        output.print(graph.Paths()+"&");
        output.print(graph.Conexity()+"&");
        output.print(graph.Follows()+"&");
        output.println(graph.Perms());
    }
}

