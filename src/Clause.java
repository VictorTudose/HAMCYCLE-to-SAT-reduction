import java.util.ArrayList;

public class Clause {
    public ArrayList<String> stringArrayList;

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }

    public void setStringArrayList(ArrayList<String> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }

    public Clause() {
        this.stringArrayList = new ArrayList<>();
    }

    public void add(String s)
    {
        stringArrayList.add(s);
    }

    public void merge(String operation, Clause clause2) {
        stringArrayList.add(operation);
        stringArrayList.addAll(clause2.getStringArrayList());
        clause2=null;
    }

    public static Clause Equivalence(String s1,String s2) {
        Clause clause=new Clause();
        clause.add("(");
        clause.add("("+s1+"|~"+s2+")&");
        clause.add("(~"+s1+"|"+s2+")");
        clause.add(")");
        return clause;
    }

    @Override
    public String toString() {

        StringBuilder sb=new StringBuilder();
        for(String S: stringArrayList)
            sb.append(S);
        return sb.toString();
    }
}

