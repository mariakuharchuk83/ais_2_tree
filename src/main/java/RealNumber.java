package main.java;

public class RealNumber implements Comparable<RealNumber> {
    private int num;
    private int denom;

    public RealNumber(int num, int denom) {
        if (denom == 0) {
            throw new ArithmeticException("denom = 0");
        }
        this.num = num;
        this.denom = denom;
    }

    public static RealNumber real(int n) {
        return new RealNumber(n, 1);
    }

    public boolean isPositive() {
        return (num > 0 && denom > 0) || (num < 0 && denom < 0);
    }

    @Override
    public int compareTo(RealNumber o) {
        return Double.compare(((double) num / denom), ((double) o.num / o.denom));
    }

    @Override
    public String toString() {
        return num + "/" + denom;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof RealNumber)) {
            return false;
        }

        RealNumber c = (RealNumber) o;

        return num == c.num
                && denom == c.denom;
    }
}
