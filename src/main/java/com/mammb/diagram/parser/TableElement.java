package com.mammb.diagram.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TableElement {

    private final String name;
    private final String typeName;
    private final Set<ElementProperty> props;

    protected TableElement(String name, String typeName, Set<ElementProperty> props) {
        this.name = Objects.requireNonNull(name);
        this.typeName = Objects.requireNonNull(typeName);
        this.props = Objects.isNull(props) ? new HashSet<>() : props;
    }

    public static TableElement of(String name, String typeName) {
        return new TableElement(name, typeName, new HashSet<>());
    }

    public void markAs(ElementProperty... props) {
        this.props.addAll(Arrays.asList(props));
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public Set<ElementProperty> getProps() {
        return props;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableElement element = (TableElement) o;
        return name.equals(element.name) &&
                typeName.equals(element.typeName) &&
                props.equals(element.props);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeName, props);
    }
}
