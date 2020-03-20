package com.mammb.diagram.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Table {

    private final QualifiedName name;
    private final List<TableElement> elements;
    private final List<Reference> refs;

    protected Table(QualifiedName name, List<TableElement> elements, List<Reference> refs) {
        this.name = Objects.requireNonNull(name);
        this.elements = Objects.requireNonNull(elements);
        this.refs = Objects.requireNonNull(refs);
    }

    public static Table of(String name) {
        return new Table(QualifiedName.of(name), new ArrayList<>(), new ArrayList<>());
    }

    public void addElement(TableElement element) {
        elements.add(element);
    }

    public void addReference(Reference ref) {
        refs.add(ref);
    }

    public QualifiedName getName() {
        return name;
    }

    public Optional<TableElement> getElement(String elementName) {
        return elements.stream().filter(e -> e.getName().equals(elementName)).findFirst();
    }

    public List<TableElement> getElements() {
        return elements;
    }

    public List<Reference> getRefs() {
        return refs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return name.equals(table.name) &&
                elements.equals(table.elements) &&
                refs.equals(table.refs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, elements, refs);
    }
}
