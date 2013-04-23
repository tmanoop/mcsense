package simset;

public class Head extends Linkage {
    public Head() { PRED = SUC = this; }
    public final Link first() { return suc(); }
    public final Link last() { return pred(); }
    public final boolean empty() { return SUC == this; }
    public final int cardinal() {
        int i = 0;
        for (Link ptr = first(); ptr != null; ptr = ptr.suc())
            i++;
        return i; 
    }
    
    public final void clear() {
        while (first() != null)
        	first().out();
    } 
}