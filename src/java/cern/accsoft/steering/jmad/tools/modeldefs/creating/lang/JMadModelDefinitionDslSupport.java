/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import cern.accsoft.steering.jmad.domain.file.ModelPathOffsetsImpl;
import cern.accsoft.steering.jmad.modeldefs.ModelDefinitionFactory;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinitionImpl;

public abstract class JMadModelDefinitionDslSupport implements ModelDefinitionFactory {

    private final JMadModelDefinitionImpl modelDefinition = new JMadModelDefinitionImpl();

    private boolean initFilesInitialized = false;
    private boolean offsetsDefined = false;

    protected void name(String name) {
        AssertUtil.requireNull(modelDefinition.getName(), "name was already set! Cannot be set twice!");
        modelDefinition.setName(name);
    }

    protected void offsets(Consumer<OffsetBlock> offsetBlock) {
        AssertUtil.requireFalse(offsetsDefined, "Offsets already defined. Cannot define twice!");
        offsetBlock.accept(new OffsetBlock((ModelPathOffsetsImpl) modelDefinition.getModelPathOffsets()));
        offsetsDefined = true;
    }

    protected void init(Consumer<FileBlock> initBlock) {
        AssertUtil.requireFalse(initFilesInitialized, "Init block already defined. Cannot define twice!");
        requireNonNull(initBlock, "initBlock must not be null");

        List<ModelFileBuilder> initFileBuilders = new ArrayList<>();
        initBlock.accept(new FileBlock(initFileBuilders));
        initFileBuilders.stream().map(ModelFileBuilder::build).forEachOrdered(modelDefinition::addInitFile);
        initFilesInitialized = true;
    }

    protected OngoingOptics optics(String opticsName) {
        requireNonNull(opticsName, "opticsName must not be null");
        return new OngoingOptics(opticsName, this.modelDefinition);
    }

    protected OngoingSequence sequence(String sequenceName) {
        requireNonNull(sequenceName, "sequenceName must not be null");
        return new OngoingSequence(sequenceName, this.modelDefinition);
    }

    @Override
    public final JMadModelDefinition create() {
        return this.modelDefinition;
    }

}
