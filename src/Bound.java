public class Bound extends Sign {

    private int bound;

    public Bound(int bound) {
        this.bound = bound;
    }

    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public int getBound() {
        return bound;
    }

    @Override
    public void setBound(int bound) {
        this.bound = bound;
    }

    @Override
    public char toChar() {
        return (char)(bound + '0');
    }

    @Override
    public Bound copy() {
        return new Bound(bound);
    }

    @Override
    public boolean saturated() {
        return bound == 0;
    }

    @Override
    public int decrement() {
        return --bound;
    }

    @Override
    public int increment() {
        return ++bound;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Bound)) return false;
        Bound that = (Bound) o;
        return this.bound == that.bound;
    }

}
