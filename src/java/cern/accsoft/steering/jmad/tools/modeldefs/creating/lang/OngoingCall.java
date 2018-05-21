/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.file.CallableModelFile.ParseType;
import cern.accsoft.steering.jmad.domain.file.ModelFile.ModelFileLocation;

public class OngoingCall {

    private final CallableModelFileBuilder builder;

    public OngoingCall(CallableModelFileBuilder builder) {
        this.builder = requireNonNull(builder, "builder must not be null");
    }

    public OngoingCall from(ModelFileLocation location) {
        builder.from(location);
        return this;
    }

    public OngoingCall parseAs(ParseType parseType) {
        builder.parseAs(parseType);
        return this;
    }

}
