/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.Collection;

public class FileBlock {

    private final Collection<ModelFileBuilder> builders;

    public FileBlock(Collection<ModelFileBuilder> builders) {
        this.builders = requireNonNull(builders, "builder collection must not be null");
    }

    public OngoingCall call(String fileName) {
        CallableModelFileBuilder builder = CallableModelFileBuilder.of(fileName);
        builders.add(builder);
        return new OngoingCall(builder);
    }

    public OngoingLoadTable load(String fileName) {
        TableModelFileBuilder builder = new TableModelFileBuilder(fileName);
        builders.add(builder);
        return new OngoingLoadTable(builder);
    }

}
