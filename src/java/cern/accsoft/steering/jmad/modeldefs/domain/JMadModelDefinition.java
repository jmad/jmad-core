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

import java.util.List;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.file.ModelFileDependant;
import cern.accsoft.steering.jmad.domain.file.ModelPathOffsets;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceImpl;

/**
 * This interface represents a general definition of a model-configuration for jmad. It can be implemented directly by a
 * class, which provides a configuration.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public interface JMadModelDefinition extends ModelFileDependant {

    /**
     * @return the name
     */
    String getName();

    /**
     * this method must return all the {@link ModelFile}s that shall be called during the initialization process of the
     * model.
     * 
     * @return the files to be called
     */
    List<ModelFile> getInitFiles();

    /**
     * @return the names of all {@link SequenceImpl}s defined for this machine
     */
    List<SequenceDefinition> getSequenceDefinitions();

    /**
     * @return the sequence definition, which shall be selected by default
     */
    SequenceDefinition getDefaultSequenceDefinition();

    /**
     * retrieves the sequence definition of the given name.
     * 
     * @param name the name of the sequence definition
     * @return the {@link SequenceDefinition} if found, null otherwise.
     */
    SequenceDefinition getSequenceDefinition(String name);

    /**
     * convenience method to return all range definitions which are contained in the sequence definitions.
     * 
     * @return all the range definitions
     */
    List<RangeDefinition> getRangeDefinitions();

    /**
     * convenience method to return the default range of the default sequence.
     * 
     * @return the default range
     */
    RangeDefinition getDefaultRangeDefinition();

    /**
     * @return all the possible optics definitions available for this model
     */
    List<OpticsDefinition> getOpticsDefinitions();

    /**
     * @return the default optics definition for this model
     */
    OpticsDefinition getDefaultOpticsDefinition();

    /**
     * retrieves the optics definition by its name.
     * 
     * @param name the name of the opticsDefinition
     * @return the opticsDefinition if found, null otherwise
     */
    OpticsDefinition getOpticsDefinition(String name);

    /**
     * @return the {@link ModelPathOffsets} for the {@link ModelFile}s
     */
    ModelPathOffsets getModelPathOffsets();

    /**
     * @return the {@link SourceInformation} for the model definition, which describes from which source the Model
     *         definition was loaded.
     */
    SourceInformation getSourceInformation();

    /**
     * @return the URI to the origin model pack of the model definition. This URI can be used with
     * jmad-modelpack-service to retrieve this definition at a later time. If this definition was not loaded from a
     * model pack, null is returned.
     *
     * Unlike {@link #getSourceInformation()}, this provides a more "high level" view on the origin location,
     * independent of the (possibly temporary) location in the file system.
     */
    String getModelPackUri();

}
