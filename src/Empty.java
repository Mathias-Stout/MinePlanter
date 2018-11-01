public class Empty extends Sign {

    @Override
    public boolean isEmpty() { return true; }

    @Override
    public char toChar() {
        return '-';
    }

    @Override
    public Empty copy() {
        return new Empty();
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof Empty;
    }

}

