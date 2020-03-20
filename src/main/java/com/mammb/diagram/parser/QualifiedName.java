package com.mammb.diagram.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class QualifiedName {

    private final List<String> parts;

    public QualifiedName(List<String> parts) {
        this.parts = parts;
    }

    public static QualifiedName of(String name) {
        return new QualifiedName(
                Arrays.stream(name.split("¥."))
                        .map(String::toLowerCase)
                        .collect(Collectors.toList()));
    }

    public Optional<QualifiedName> getPrefix() {
        if (parts.size() == 1) {
            return Optional.empty();
        }

        List<String> subList = parts.subList(0, parts.size() - 1);
        return Optional.of(new QualifiedName(subList));
    }

    public String getSuffix() {
        return parts.get(parts.size() - 1);
    }

    @Override
    public String toString() {
        return String.join(".", parts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifiedName that = (QualifiedName) o;
        return parts.equals(that.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parts);
    }
}
