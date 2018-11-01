public abstract class Sign {

    public boolean isBound() { return false; }
    public boolean isMine()  { return false; }
    public boolean isEmpty() { return false; }

    public int getBound() {
        return Integer.MAX_VALUE;
    }
    public void setBound(int bound) {}

    public abstract char toChar();
    public abstract Sign copy();

    public boolean saturated() {
        return false;
    }

    public int decrement() {
        return Integer.MAX_VALUE;
    }

    public int increment() {
        return Integer.MAX_VALUE;
    }


}
