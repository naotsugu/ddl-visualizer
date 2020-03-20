package com.mammb.diagram.parser;

public class Relation {

    private final Reference from;
    private final Reference to;

    public Relation(Reference from, Reference to) {
        this.from = from;
        this.to = to;
    }

    public static Relation of(Reference from, Reference to) {
        return new Relation(from, to);
    }

    public Reference getFrom() {
        return from;
    }

    public Reference getTo() {
        return to;
    }

}
