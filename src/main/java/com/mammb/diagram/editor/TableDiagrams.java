package com.mammb.diagram.editor;

import com.mammb.diagram.diagram.BasicDiagram;
import com.mammb.diagram.diagram.Connector;
import com.mammb.diagram.parser.ElementProperty;
import com.mammb.diagram.parser.QualifiedName;
import com.mammb.diagram.parser.Table;
import com.mammb.diagram.parser.TableElement;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableDiagrams {

    private Map<QualifiedName, Pair> map;
    private ObservableList<Node> diagrams;

    public TableDiagrams(ObservableList<Node> diagrams) {
        this.map = new LinkedHashMap<>();
        this.diagrams = diagrams;
    }

    public void putAll(List<Table> tables) {
        initDiagrams(tables);
        initConnectors(tables);
    }

    private void initDiagrams(List<Table> tables) {

        Map<QualifiedName, Table> tableMap = tables.stream()
                .collect(Collectors.toMap(Table::getName, t -> t, (t1, t2) -> t1));
        List<Pair> removing = map.entrySet().stream()
                .filter(e -> !tableMap.containsKey(e.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        removing.forEach(p -> map.remove(p.table.getName()));
        removing.forEach(p -> diagrams.remove(p.diagram));

        tables.forEach(this::put);

    }

    private void put(Table table) {

        if (map.containsKey(table.getName())) {
            Pair pair = map.get(table.getName());
            if (!pair.table.equals(table)) {
                pair.table = table;
                initDiagramElement(pair);
            }

        } else {
            Pair pair = Pair.of(table, BasicDiagram.of(table.getName().getSuffix()));
            initDiagramElement(pair);
            map.put(table.getName(), pair);
            diagrams.add(pair.diagram);
        }
    }


    private void initDiagramElement(Pair pair) {
        pair.diagram.removeRows();
        for (TableElement element : pair.table.getElements()) {
            if (element.getProps().contains(ElementProperty.PK)) {
                pair.diagram.addPrimaryRow(element.getName(), element.getTypeName());
            } else {
                pair.diagram.addSecondaryRow(element.getName(), element.getTypeName());
            }
        }
    }

    private void initConnectors(List<Table> tables) {
        diagrams.removeIf(node -> node instanceof Connector);
        createConnectors(tables).forEach(diagrams::addAll);
    }

    private List<Connector> createConnectors(List<Table> tables) {
        return tables.stream()
                .map(this::createConnector)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    private List<Connector> createConnector(Table table) {
        return table.getRefs().stream()
                .filter(reference -> map.containsKey(table.getName()))
                .filter(reference -> map.containsKey(reference.getTable().getName()))
                .map(reference -> new Connector(
                        map.get(table.getName()).diagram,
                        map.get(reference.getTable().getName()).diagram))
                .collect(Collectors.toList());
    }


    static class Pair {
        private Table table;
        private BasicDiagram diagram;
        static Pair of(Table table, BasicDiagram diagram) {
            Pair pair = new Pair();
            pair.table = table;
            pair.diagram = diagram;
            return pair;
        }

    }
}
