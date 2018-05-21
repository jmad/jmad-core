/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.file.AbstractModelFile;
import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.file.ModelFile.ModelFileLocation;
import cern.accsoft.steering.jmad.domain.file.TableModelFileImpl;

public class TableModelFileBuilder implements ModelFileBuilder {

    private final String path;
    private ModelFileLocation location = AbstractModelFile.DEFAULT_MODEL_FILE_LOCATION;
    private String tableName;

    public TableModelFileBuilder(String path) {
        this.path = requireNonNull(path, "filePath must not be null");
    }

    @Override
    public ModelFile build() {
        if (tableName == null) {
            throw new IllegalStateException("tableName is not set. This is not allowed.");
        }
        return new TableModelFileImpl(path, location, tableName);
    }

    public void setTableName(String tableName) {
        this.tableName = requireNonNull(tableName, "tableName must not be null");
    }

    public void setLocation(ModelFileLocation location) {
        this.location = requireNonNull(location, "location must not be null");
    }

}
