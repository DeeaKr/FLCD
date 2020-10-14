public class Node {
    private Node next;
    private String symbolValue;



    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public String getSymbolValue() {
        return symbolValue;
    }

    public void setSymbolValue(String symbolValue) {
        this.symbolValue = symbolValue;
    }

    public Node(String symbol) {
        this.symbolValue = symbol;

        this.next=null;
    }


}
