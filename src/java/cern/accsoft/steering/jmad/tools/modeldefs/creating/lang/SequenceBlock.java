/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinitionImpl;

public class SequenceBlock {

    private final SequenceDefinitionImpl sequenceDefinition;

    public SequenceBlock(SequenceDefinitionImpl sequenceDefinition) {
        this.sequenceDefinition = requireNonNull(sequenceDefinition, "sequenceDefinition must not be null");
    }

    public OngoingBeam beam() {
        AssertUtil.requireNull(sequenceDefinition.getBeam(), "beam is already defined. Cannot define twice.");
        Beam beam = new Beam();
        sequenceDefinition.setBeam(beam);
        return new OngoingBeam(beam);
    }

    public OngoingRange range(String rangeName) {
        return new OngoingRange(rangeName, sequenceDefinition);
    }
}
