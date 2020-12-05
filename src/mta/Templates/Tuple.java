package mta.Templates;

public class Tuple<firstType, secondType> {
    public final firstType x;
    public final secondType y;
    public Tuple(firstType x, secondType y) {
        this.x = x;
        this.y = y;
    }
}
