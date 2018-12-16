/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinitionImpl;

public class SequenceBlock {

    private final SequenceDefinitionImpl sequenceDefinition;

    public SequenceBlock(SequenceDefinitionImpl sequenceDefinition) {
        this.sequenceDefinition = requireNonNull(sequenceDefinition, "sequenceDefinition must not be null");
    }

    public void beam(Consumer<BeamBlock> beamBlock) {
        AssertUtil.requireNull(sequenceDefinition.getBeam(), "beam is already defined. Cannot define twice.");
        Beam beam = new Beam();
        beamBlock.accept(new BeamBlock(beam));
        sequenceDefinition.setBeam(beam);
    }

    public OngoingRange range(String rangeName) {
        return new OngoingRange(rangeName, sequenceDefinition);
    }
}
