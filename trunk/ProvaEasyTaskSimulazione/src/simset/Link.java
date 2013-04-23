package simset;

public class Link extends Linkage {
    public final void out() {
        if (SUC != null) {
            SUC.PRED = PRED;
            PRED.SUC = SUC;
            SUC = PRED = null;
        }
}
    public final void follow(Linkage ptr) {
        out();
        if (ptr != null && ptr.SUC != null) {
            PRED = ptr;
            SUC = ptr.SUC;
            SUC.PRED = ptr.SUC = this;
        }
}
    public final void precede(Linkage ptr) {
        out();
        if (ptr != null && ptr.SUC != null) {
            SUC = ptr;
            PRED = ptr.PRED;
            PRED.SUC = ptr.PRED = this;
        }
}
    public final void into(Head s) {
        precede(s);
    }
    
}