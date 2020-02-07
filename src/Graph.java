import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph {
    int n;
    int[][] matrix;
    int[] degrees;

    Graph(Scanner input) {
        n=input.nextInt();
        matrix=new int[n+1][n+1];
        degrees=new int[n+1];
        while(true) {
            int s=input.nextInt();
            if(s==-1)
                break;
            int d=input.nextInt();
            matrix[s][d]=matrix[d][s]=1;
            degrees[s]++;
            degrees[d]++;
        }
    }

    private int next(int node,int curr) {
        curr++;
        while(curr < n && matrix[node][curr]==0 )
            curr++;
        return curr;
    }

    private String AtLeastTwoNeighbours(int node) {
        StringBuilder sb=new StringBuilder();
        String prefix2="";
        for(int i=next(node,0);i<n;i=next(node,i))
        {
            if(matrix[i][node]==1)
            {
                for(int j=next(node,i);j<=n;j=next(node,j))
                {
                    if(matrix[j][node]==1)
                    {
                            sb.append(prefix2).append("(");
                            String prefix="";
                            for(int k=next(node,0);k<=n;k=next(node,k))
                            {
                                if(matrix[node][k]!=1) continue;
                    	           sb.append(prefix);
                                if(k!=i&&k!=j)
                                sb.append("~");
                                sb.append(x(node, k));
                                prefix="&";
                            }
                        sb.append(")");
                        prefix2="|";
                    }
                }
            }
        }
        return sb.toString();
    }

    public boolean quickExclude() {
        boolean exclude=false;
        for(int i=1;i<=n;i++)
        {
            if(degrees[i]==0||degrees[i]==1) {
                return true;
            }
        }
        return exclude;
    }

    public static String x(int n1,int n2) {
        return "x"+n1+"-"+n2;
    }

    public static String a(int n1,int n2) {
        return "a"+n1+"-"+n2;
    }

    public Clause Paths(int node) {
        Clause clause=new Clause();
        clause.add("(");
        String prefix="";
        for(int i=1;i<=n/2+1;i++) {
            clause.add(prefix + a(i,node));
            prefix="|";
        }
        clause.add(")");
        return clause;
    }

    public Clause Edges(int node) {
        Clause clause=new Clause();
        clause.add("(");
        if(degrees[node]==2)
            {
            	clause.add("(");
                int i=next(node,0);
                clause.add(x(node,i)+"&");
                i=next(node,i);
                clause.add(x(node,i));
                clause.add(")");
            }
        else
            clause.add(AtLeastTwoNeighbours(node));
        clause.add(")");
        return  clause;
    }

    public Clause Edges() {
        Clause clause=Edges(1);
        for(int i=2;i<=n;i++)
        {
            clause.merge("&",Edges(i));
        }
        return clause;
    }

    public Clause Paths() {
        Clause clause=Paths(2);
        for(int i=3;i<=n;i++)
        {
            clause.merge("&",Paths(i) );
        }
        return clause;
    }

    public Clause Conexity() {
        Clause clause=new Clause();
        String prefix="";
        for(int i=1;i<=n;i++)
            for(int j=next(i,i);j<=n;j=next(i,j)){
            	if(matrix[i][j]!=1)
            		continue;
                clause.merge(prefix, Clause.Equivalence(x(i, j), x(j, i)));
                prefix="&";
            }
        return clause;
    }

    public Clause Follows() {
        Clause clause=new Clause();
        String prefix="";

        for(int i=1;i<=n;i++) {
            if(matrix[1][i]==1)
                clause.merge(prefix , Clause.Equivalence(x(1,i),a(1,i)));
            else
                clause.add(prefix+"~" + a(1, i));
            prefix="&";
        }

        return clause;
    }

    @Override
    public String toString() {
       StringBuilder sb=new StringBuilder();
       sb.append(n+"\n");
       for(int i=1;i<=n;i++) {
           for (int j = 1; j <= n; j++)
               sb.append(matrix[i][j]);
            sb.append("\n");
       }
       for(int i=1;i<=n;i++)
           sb.append(degrees[i]+" ");
       return sb.toString();
    }

    public Clause notPrc(int pos,int node) {
        Clause clause = new Clause();
        clause.add("(");
        String prefix = "";
        for(int i = 1; i < pos; i++) {
            clause.add(prefix + a(i, node));
            prefix = "|";
        }
        clause.add(")");
        return clause;
    }

    public Clause SemiPerm(int pos,int node) {
        Clause clause=new Clause();
        clause.add("(");
        String prefix="";
        for(int i=next(node,1) ; i<=n ; i=next(node,i)) {
        	if(matrix[i][node]!=1) continue;
            clause.add(prefix+"("+a(pos-1,i)+"&"+x(i,node)+")");
            prefix = "|";
        }
        clause.merge("&~",notPrc(pos, node));
        clause.add(")");
        return clause;
    }

    public Clause Perm(int pos,int node) {
        Clause clause=new Clause();

        clause.add( Clause.Equivalence( a(pos,node), SemiPerm(pos,node).toString() ).toString() );

        return clause;
    }

    public Clause Perms()
    {
        Clause clause=new Clause();

        String prefix="";

        for(int i=2;i<=n/2+1;i++)
            for(int j=2;j<=n;j++) {
                clause.merge(prefix,Perm(i, j));
                prefix="&";
            }

        return clause;
    }

}
