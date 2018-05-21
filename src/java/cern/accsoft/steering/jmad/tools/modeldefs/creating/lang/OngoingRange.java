/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import java.util.function.Consumer;

import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinitionImpl;

public class OngoingRange {

    private final RangeDefinitionBuilder builder;
    private final SequenceDefinitionImpl sequenceDefinition;
    private boolean isDefault = false;

    public OngoingRange(String rangeName, SequenceDefinitionImpl sequenceDefinition) {
        this.sequenceDefinition = sequenceDefinition;
        this.builder = new RangeDefinitionBuilder(rangeName, sequenceDefinition);
    }

    public OngoingRange isDefault() {
        this.isDefault = true;
        return this;
    }

    public void isDefinedAs(Consumer<RangeBlock> block) {
        RangeBlock rangeBlock = new RangeBlock(builder);
        block.accept(rangeBlock);
        RangeDefinition rangeDefinition = builder.build();
        sequenceDefinition.addRangeDefinition(rangeDefinition);
        if (isDefault) {
            AssertUtil.requireNull(sequenceDefinition.getDefaultRangeDefinition(),
                    "Default range definition already set for sequence " + sequenceDefinition + ". Cannot set twice.");
            sequenceDefinition.setDefaultRangeDefinition(rangeDefinition);
        }
    }

}
