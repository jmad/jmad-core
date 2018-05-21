/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import cern.accsoft.steering.jmad.domain.machine.SequenceDefinitionImpl;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinitionImpl;

public class OngoingSequence {

    private final String name;
    private final JMadModelDefinitionImpl modelDefinition;
    private boolean isDefault = false;

    public OngoingSequence(String name, JMadModelDefinitionImpl modelDefinition) {
        this.name = requireNonNull(name, "name must not be null");
        this.modelDefinition = requireNonNull(modelDefinition, "modelDefinition must not be null");
    }

    public OngoingSequence isDefault() {
        this.isDefault = true;
        return this;
    }

    public void isDefinedAs(Consumer<SequenceBlock> block) {
        SequenceDefinitionImpl sequenceDefinition = new SequenceDefinitionImpl(name);
        SequenceBlock sequenceBlock = new SequenceBlock(sequenceDefinition);
        block.accept(sequenceBlock);
        modelDefinition.addSequenceDefinition(sequenceDefinition);

        if (isDefault) {
            AssertUtil.requireNull(modelDefinition.getDefaultSequenceDefinition(),
                    "default sequence already defined. Cannot define twice.");
            modelDefinition.setDefaultSequenceDefinition(sequenceDefinition);
        }
    }
}
