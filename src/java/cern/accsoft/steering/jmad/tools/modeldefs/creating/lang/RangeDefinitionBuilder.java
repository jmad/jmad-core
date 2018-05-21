/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.machine.MadxRange;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinitionImpl;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.domain.machine.filter.NameFilter;
import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditionsImpl;

public class RangeDefinitionBuilder {

    private final SequenceDefinition sequenceDefinition;
    private final String rangeName;
    private TwissInitialConditionsImpl twissInitialConditions;
    private String startElementName;
    private String endElementName;

    private List<NameFilter> monitorInvertFilters = new ArrayList<>();
    private List<NameFilter> correctorInvertFilters = new ArrayList<>();
    private List<ModelFile> postUseFiles = new ArrayList<>();
    private String rotateStartElementName;

    public RangeDefinitionBuilder(String rangeName, SequenceDefinition sequenceDefinition) {
        this.rangeName = requireNonNull(rangeName, "rangeName must not be null");
        this.sequenceDefinition = requireNonNull(sequenceDefinition, "sequenceDefinition must not be null");
        this.twissInitialConditions = new TwissInitialConditionsImpl(
                sequenceDefinition.getName() + "-" + rangeName + "-twiss");
    }

    public RangeDefinition build() {
        if (twissInitialConditions == null) {
            throw new IllegalStateException(
                    "Twiss initial conditions not defined for range '" + rangeName + "'. This is not allowed!");
        }
        RangeDefinitionImpl rangeDefinition = new RangeDefinitionImpl(sequenceDefinition, rangeName, madxRange(),
                twissInitialConditions);
        monitorInvertFilters.stream().forEachOrdered(rangeDefinition::addMonitorInvertFilter);
        correctorInvertFilters.stream().forEachOrdered(rangeDefinition::addCorrectorInvertFilter);
        postUseFiles.stream().forEachOrdered(rangeDefinition::addPostUseFile);
        if (this.rotateStartElementName != null) {
            rangeDefinition.setStartElementName(rotateStartElementName);
        }
        return rangeDefinition;

    }

    private MadxRange madxRange() {
        MadxRange defaultRange = new MadxRange();
        String first = Optional.ofNullable(getStartElementName()).orElse(defaultRange.getFirstElementName());
        String last = Optional.ofNullable(getEndElementName()).orElse(defaultRange.getLastElementName());
        return new MadxRange(first, last);
    }

    public TwissInitialConditionsImpl getTwissInitialConditions() {
        return twissInitialConditions;
    }

    public void setTwissInitialConditions(TwissInitialConditionsImpl twissInitialConditions) {
        this.twissInitialConditions = twissInitialConditions;
    }

    public String getStartElementName() {
        return startElementName;
    }

    public void setStartElementName(String startElementName) {
        this.startElementName = startElementName;
    }

    public String getEndElementName() {
        return endElementName;
    }

    public void setEndElementName(String endElementName) {
        this.endElementName = endElementName;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void addMonitorInvertFilter(NameFilter filter) {
        this.monitorInvertFilters.add(filter);
    }

    public void addCorrectorInvertFilter(NameFilter filter) {
        this.correctorInvertFilters.add(filter);
    }

    public void addPostUseFile(ModelFile modelFile) {
        this.postUseFiles.add(modelFile);
    }

    public void setRotateStartElementName(String elementName) {
        this.rotateStartElementName = elementName;
    }

    public String getRotateStartElementName() {
        return this.rotateStartElementName;
    }
}
