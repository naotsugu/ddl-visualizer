package com.mammb.diagram.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.mammb.diagram.parser.sql.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Parser {

    public List<Table> parse(String source) {

        CharStream cs = CharStreams.fromString(source);
        SqlLexer lexer = new SqlLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SqlParser parser = new SqlParser(tokens);

        Listener listener = new Listener();
        ParseTreeWalker.DEFAULT.walk(listener, parser.root());

        return new ArrayList<>(listener.tables().values());
    }


    static class Listener extends SqlBaseListener {

        private Map<QualifiedName, Table> tables;

        Listener() {
            this.tables = new LinkedHashMap<>();
        }

        Map<QualifiedName, Table> tables() {
            return tables;
        }

        @Override
        public void enterCreateTable(SqlParser.CreateTableContext ctx) {

            Table table = Table.of(ctx.qualifiedTableName().getText());
            table = tables.getOrDefault(table.getName(), table);
            for (SqlParser.ColumnDefinitionContext cd : ctx.columnDefinition()) {
                table.addElement(TableElement.of(cd.columnName().getText(), cd.typeName().getText()));
            }

            for (SqlParser.TableConstraintContext tc : ctx.tableConstraint()) {
                if (tc.PRIMARY() != null) {
                    for (SqlParser.IndexedColumnContext ic : tc.indexedColumn()) {
                        table.getElement(ic.columnName().getText())
                                .ifPresent(e -> e.markAs(ElementProperty.PK));
                    }
                }
                if (tc.UNIQUE() != null) {
                    for (SqlParser.IndexedColumnContext ic : tc.indexedColumn()) {
                        table.getElement(ic.columnName().getText())
                                .ifPresent(e -> e.markAs(ElementProperty.UNIQUE));
                    }
                }
                if (tc.FOREIGN() != null) {
                    Optional<Reference> ref = foreignReference(tc.referenceDefinition());
                    ref.ifPresent(table::addReference);
                }
            }
            tables.put(table.getName(), table);
        }


        @Override
        public void enterAlterTable(SqlParser.AlterTableContext ctx) {

            Table table = tables.get(QualifiedName.of(ctx.qualifiedTableName().getText()));

            SqlParser.AlterSpecificationContext asc = ctx.alterSpecification();
            if (asc instanceof SqlParser.AlterByAddForeignKeyContext) {
                SqlParser.AlterByAddForeignKeyContext fkc = (SqlParser.AlterByAddForeignKeyContext) asc;
                foreignReference(fkc.referenceDefinition()).ifPresent(table::addReference);
            }
        }


        private Optional<Reference> foreignReference(SqlParser.ReferenceDefinitionContext ctx) {

            Table table = tables.get(QualifiedName.of(ctx.qualifiedTableName().getText()));
            if (Objects.isNull(table)) {
                Optional.empty();
            }

            List<TableElement> elements = ctx.columnName().stream()
                    .map(SqlParser.ColumnNameContext::getText)
                    .map(table::getElement)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            elements = elements.isEmpty()
                    ? table.getElements().stream()
                        .filter(e -> e.getProps().contains(ElementProperty.PK))
                        .collect(Collectors.toList())
                    : elements;

            if (elements.isEmpty()) {
                Optional.empty();
            }

            return Optional.of(Reference.of(table, elements));
        }

    }
}
