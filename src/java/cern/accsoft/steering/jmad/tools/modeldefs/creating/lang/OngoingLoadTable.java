/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.file.ModelFile.ModelFileLocation;

public class OngoingLoadTable {

    private final TableModelFileBuilder builder;

    public OngoingLoadTable(TableModelFileBuilder builder) {
        this.builder = requireNonNull(builder, "builder must not be null");
    }

    public OngoingLoadTable from(ModelFileLocation location) {
        this.builder.setLocation(location);
        return this;
    }

    public OngoingLoadTable asTable(String tableName) {
        this.builder.setTableName(tableName);
        return this;
    }

}
