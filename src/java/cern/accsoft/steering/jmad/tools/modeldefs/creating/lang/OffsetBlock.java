/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.file.ModelPathOffsetsImpl;

public class OffsetBlock {

    private final ModelPathOffsetsImpl modelPathOffsetsImpl;

    public OffsetBlock(ModelPathOffsetsImpl modelPathOffsetsImpl) {
        this.modelPathOffsetsImpl = requireNonNull(modelPathOffsetsImpl, "modelPathOffsets must not be null");
    }

    public void resource(String offset) {
        AssertUtil.requireNull(modelPathOffsetsImpl.getResourceOffset(),
                "resourceOffset was already set! Cannot be set twice!");
        modelPathOffsetsImpl.setResourceOffset(offset);
    }

    public void repository(String offset) {
        AssertUtil.requireNull(modelPathOffsetsImpl.getRepositoryOffset(),
                "repositoryOffset was already set! Cannot be set twice!");
        modelPathOffsetsImpl.setRepositoryOffset(offset);
    }

}
