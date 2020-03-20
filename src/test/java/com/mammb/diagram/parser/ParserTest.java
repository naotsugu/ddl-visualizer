package com.mammb.diagram.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parse() {
        Parser p = new Parser();
        p.parse("create table employee (id bigint not null, name varchar(50), primary key (id));");
    }
}