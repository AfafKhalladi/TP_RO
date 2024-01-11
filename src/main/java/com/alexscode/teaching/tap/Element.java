package com.alexscode.teaching.tap;

public class Element implements Comparable<Element> {
    public int index;
    public double value;

    public Element(int index, double value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(Element other) {
        // For descending order, flip the sign of the comparison
        return Double.compare(other.value, this.value);
    }
}
