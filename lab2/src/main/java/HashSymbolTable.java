import java.util.ArrayList;
import java.util.List;

public class HashSymbolTable {
    private Node[] table;


    public HashSymbolTable(int size) {
        table=new Node[size];

    }


    public int getLength(){
        return table.length;
    }

    public void insertSymbol(String symbol)
    {
        if(!exists(symbol)) {

            int pos = myhash(symbol);
            Node nptr = new Node(symbol);
            if (table[pos] == null)
                table[pos] = nptr;
            else {
                nptr.setNext(table[pos]);
                table[pos] = nptr;
            }
        }
    }
    private int myhash(String symbol)
    {
        int l = symbol.length();
        int sum = 0;

        for (int i = 0; i < l; i++) {
                sum += symbol.charAt(i);
        }
        return sum % this.getLength();

    }
    public boolean exists(String symbol){

        int pos=myhash(symbol);
        if(table[pos]!=null){
            Node node=table[pos];
            while(node.getNext()!=null){
                if(node.getSymbolValue().equals(symbol)){
                    return true;
                }
                node=node.getNext();

            }
            return node.getSymbolValue().equals(symbol);

        }
        return false;

    }

    public List<Integer> findBySymbol(String symbol){
        int pos=myhash(symbol);
        int p=0;
        List<Integer> positions=new ArrayList<>();
        Node node=table[pos];
        if(exists(symbol)){
            while(!node.getSymbolValue().equals(symbol)){
                p+=1;
                node=node.getNext();
            }
            positions.add(pos);
            positions.add(p);
        }
        else
        {
            positions.add(pos);
            positions.add(-1);
        }
        return positions;

    }



    public StringBuilder printTable(){
        StringBuilder out= new StringBuilder();
        out.append("\n");
        for (int i = 0; i < table.length; i++)
        {
            out.append("ST_Position ").append(i).append(":  ");
            Node start = table[i];
            while(start != null)
            {
                out.append("symbol: ").append(start.getSymbolValue()).append(" ");
                start = start.getNext();
            }
            out.append("\n");
        }
        return out;
    }

}
