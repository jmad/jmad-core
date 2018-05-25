// @formatter:off
 /*******************************************************************************
 *
 * This file is part of JMad.
 * 
 * Copyright (c) 2008-2011, CERN. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/
// @formatter:on

package cern.accsoft.steering.jmad.modeldefs.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.file.ModelPathOffsets;
import cern.accsoft.steering.jmad.domain.file.ModelPathOffsetsImpl;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.impl.converter.OpticsDefinitionListFilter;
import cern.accsoft.steering.jmad.modeldefs.io.impl.converter.SequenceDefinitionListFilter;
import cern.accsoft.steering.jmad.util.xml.converters.NameRefConverter;

@XStreamAlias("jmad-model-definition")
public class JMadModelDefinitionImpl extends AbstractModelDefinition {

    /** the logger for the class */
    private static final Logger LOGGER = LoggerFactory.getLogger(JMadModelDefinitionImpl.class);

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;

    @XStreamAlias("optics")
    @XStreamConverter(OpticsDefinitionListFilter.class)
    private final List<OpticsDefinition> opticsDefinitions = new ArrayList<OpticsDefinition>();

    @XStreamOmitField
    private OpticsDefinition defaultOpticsDefinition = null;

    @XStreamAlias("default-optic")
    @XStreamConverter(NameRefConverter.class)
    private String defaultOpticsDefinitionName = null;

    @XStreamAlias("sequences")
    @XStreamConverter(SequenceDefinitionListFilter.class)
    private final List<SequenceDefinition> sequenceDefinitions = new ArrayList<SequenceDefinition>();

    @XStreamOmitField
    private SequenceDefinition defaultSequenceDefinition = null;

    @XStreamAlias("default-sequence")
    @XStreamConverter(NameRefConverter.class)
    private String defaultSequenceDefinitionName = null;

    @XStreamAlias("init-files")
    private final List<ModelFile> initFiles = new ArrayList<ModelFile>();

    @XStreamAlias("path-offsets")
    private ModelPathOffsets modelPathOffsets = new ModelPathOffsetsImpl();

    @XStreamOmitField
    private SourceInformation sourceInformation = null;

    @XStreamAlias("svn-revision")
    private String svnRevision = "$" + "Revision" + "$";

    // ***********************************************
    // * Add and Remove SequenceDefinitions
    // ***********************************************

    @Override
    public SequenceDefinition getDefaultSequenceDefinition() {
        return this.defaultSequenceDefinition;
    }

    @Override
    public List<SequenceDefinition> getSequenceDefinitions() {
        return this.sequenceDefinitions;
    }

    public void setDefaultSequenceDefinition(SequenceDefinition sequenceDefinition) {
        if (sequenceDefinition == null) {
            return;
        }

        if (!this.sequenceDefinitions.contains(sequenceDefinition)) {
            addSequenceDefinition(sequenceDefinition);
        }

        /* If the addition worked */
        if (this.sequenceDefinitions.contains(sequenceDefinition)) {
            this.defaultSequenceDefinition = sequenceDefinition;
            this.defaultSequenceDefinitionName = sequenceDefinition.getName();
        }
    }

    public void addSequenceDefinition(SequenceDefinition sequenceDefinition) {
        String sequenceName = sequenceDefinition.getName();
        if (this.containsSequenceDefinition(sequenceName)) {
            LOGGER.error("ModelDefinition [" + this.name + "] already contains a Sequence Definition called ["
                    + sequenceName + "]\n--> rename the Sequence Definition to add!!");
            return;
        }
        this.sequenceDefinitions.add(sequenceDefinition);
    }

    public void removeSequenceDefinition(String name) {
        SequenceDefinition seqDefinition = findSequenceDefinition(name);
        if (seqDefinition != null) {
            this.sequenceDefinitions.remove(seqDefinition);
        }
    }

    // ***********************************************
    // * Add/remove Optics Definitions
    // ***********************************************

    @Override
    public List<OpticsDefinition> getOpticsDefinitions() {
        return this.opticsDefinitions;
    }

    @Override
    public OpticsDefinition getDefaultOpticsDefinition() {
        return this.defaultOpticsDefinition;
    }

    public void setDefaultOpticsDefinition(OpticsDefinition opticsDefinition) {
        Objects.requireNonNull(opticsDefinition, "opticsDefinition must not be null");
        if (!this.opticsDefinitions.contains(opticsDefinition)) {
            addOpticsDefinition(opticsDefinition);
        }

        if (this.opticsDefinitions.contains(opticsDefinition)) {
            this.defaultOpticsDefinition = opticsDefinition;
            this.defaultOpticsDefinitionName = opticsDefinition.getName();
        }
    }

    public void addOpticsDefinition(OpticsDefinition opticsDefinition) {
        String opticsName = opticsDefinition.getName();
        if (containsOpticsDefinition(opticsName)) {
            LOGGER.warn("ModelDefinition [" + this.name + "] already contains an Optics Definition called ["
                    + opticsName + "]\n--> rename the Optics Definition to add!!");
            return;
        }
        this.opticsDefinitions.add(opticsDefinition);
    }

    /**
     * Add an File which will be called when initializing the model
     * 
     * @param modelFile the {@link ModelFile} to add
     */
    public void addInitFile(ModelFile modelFile) {
        this.initFiles.add(modelFile);

    }

    public void removeInitFile(ModelFile modelFile) {
        this.initFiles.remove(modelFile);
    }

    // ***********************************************
    // * ModelDefinition Functions
    // ***********************************************

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<ModelFile> getInitFiles() {
        return this.initFiles;
    }

    @Override
    public ModelPathOffsets getModelPathOffsets() {
        return this.modelPathOffsets;
    }

    public void setModelPathOffsets(ModelPathOffsets modelPathOffsets) {
        this.modelPathOffsets = modelPathOffsets;
    }

    private Object readResolve() {
        if (this.defaultOpticsDefinitionName != null) {
            setDefaultOpticsDefinition(getOpticsDefinition(this.defaultOpticsDefinitionName));
        }
        if (this.defaultSequenceDefinitionName != null) {
            setDefaultSequenceDefinition(getSequenceDefinition(this.defaultSequenceDefinitionName));
        }
        return this;
    }

    @Override
    public SourceInformation getSourceInformation() {
        return this.sourceInformation;
    }

    public void setSourceInformation(SourceInformation sourceInformation) {
        this.sourceInformation = sourceInformation;
    }

    @Override
    public String getSvnRevision() {
        return this.svnRevision;
    }

}
