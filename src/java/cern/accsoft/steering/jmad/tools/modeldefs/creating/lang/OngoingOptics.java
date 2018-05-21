/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinitionImpl;
import cern.accsoft.steering.jmad.modeldefs.domain.OpticsDefinitionImpl;

public class OngoingOptics {

    private final JMadModelDefinitionImpl modelDefinition;
    private final String name;
    private boolean isDefault = false;
    private Boolean overlay = null;

    public OngoingOptics(String name, JMadModelDefinitionImpl modelDefinition) {
        this.name = requireNonNull(name, "name must not be null");
        this.modelDefinition = requireNonNull(modelDefinition, "modelDefinition must not be null");
    }

    public OngoingOptics isDefault() {
        this.isDefault = true;
        return this;
    }

    public OngoingOptics isOverlay() {
        this.overlay = true;
        return this;
    }

    public void isDefinedAs(Consumer<FileBlock> fileBlock) {
        List<ModelFileBuilder> builders = new ArrayList<>();
        fileBlock.accept(new FileBlock(builders));

        List<ModelFile> modelFiles = builders.stream().map(ModelFileBuilder::build).collect(toList());
        OpticsDefinitionImpl opticsDefinition = opticsDefinition(modelFiles);
        modelDefinition.addOpticsDefinition(opticsDefinition);

        if (isDefault) {
            AssertUtil.requireNull(modelDefinition.getDefaultOpticsDefinition(),
                    "Default optics definition is already set. Cannot set twice!");
            modelDefinition.setDefaultOpticsDefinition(opticsDefinition);
        }
    }

    private OpticsDefinitionImpl opticsDefinition(List<ModelFile> modelFiles) {
        ModelFile[] fileArray = modelFiles.toArray(new ModelFile[modelFiles.size()]);
        if (overlay == null) {
            return new OpticsDefinitionImpl(name, fileArray);
        } else {
            return new OpticsDefinitionImpl(name, overlay, fileArray);
        }
    }

}
