public class Mine extends Sign {

    @Override
    public boolean isMine() { return true; }

    @Override
    public char toChar() {
        return '*';
    }

    @Override
    public Mine copy() {
        return new Mine();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Mine;
    }

}
