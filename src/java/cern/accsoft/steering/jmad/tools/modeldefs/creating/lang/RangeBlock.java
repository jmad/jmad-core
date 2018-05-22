/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RangeBlock {

    private final RangeDefinitionBuilder builder;
    private boolean initialConditionsSpecified = false;
    private boolean filtersSpecified = false;
    private boolean postUseSpecified = false;

    public RangeBlock(RangeDefinitionBuilder builder) {
        this.builder = requireNonNull(builder, "builder must not be null");
    }

    public void start(String elementName) {
        AssertUtil.requireNull(builder.getStartElementName(), "start element already set. Cannot define twice.");
        requireNonNull("elementName must not be null");
        this.builder.setStartElementName(elementName);
    }

    public void end(String elementName) {
        AssertUtil.requireNull(builder.getEndElementName(), "end element already set. Cannot define twice.");
        requireNonNull("elementName must not be null");
        this.builder.setEndElementName(elementName);
    }

    public void rotateToStartAt(String elementName) {
        AssertUtil.requireNull(builder.getRotateStartElementName(),
                "The start element to rotate was already set. Cannot set twice.");
        requireNonNull("elementName must not be null");
        this.builder.setRotateStartElementName(elementName);
    }

    public void invert(Consumer<ElementFilterBlock> block) {
        AssertUtil.requireFalse(filtersSpecified, "Invert filters were already specified. Cannot specfy twice.");
        ElementFilterBlock filterBlock = new ElementFilterBlock(builder);
        block.accept(filterBlock);
        filtersSpecified = true;
    }

    public void onPostUse(Consumer<FileBlock> block) {
        AssertUtil.requireFalse(postUseSpecified, "postUse filse already specified. Cannot specfy twice.");
        List<ModelFileBuilder> modelFileBuilders = new ArrayList<>();
        block.accept(new FileBlock(modelFileBuilders));
        modelFileBuilders.stream().map(ModelFileBuilder::build).forEachOrdered(builder::addPostUseFile);
        postUseSpecified = true;
    }

    public void twiss(Consumer<InitialConditionsBlock> block) {
        AssertUtil.requireFalse(initialConditionsSpecified,
                "twiss initial conditions already set. Cannot define twice.");
        initialConditionsSpecified = true;
        block.accept(new InitialConditionsBlock(builder.getTwissInitialConditions()));
    }

}
