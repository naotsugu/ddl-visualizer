package com.mammb.diagram.parser;

import java.util.List;
import java.util.Objects;

public class Reference {

    private final Table table;
    private final List<TableElement> elements;

    public Reference(Table table, List<TableElement> elements) {
        this.table = Objects.requireNonNull(table);
        this.elements = Objects.requireNonNull(elements);
    }

    public static Reference of(Table table, List<TableElement> elements) {
        return new Reference(table, elements);
    }

    public Table getTable() {
        return table;
    }

    public List<TableElement> getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reference reference = (Reference) o;
        return table.equals(reference.table) &&
                elements.equals(reference.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, elements);
    }
}
