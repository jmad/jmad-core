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

package cern.accsoft.steering.jmad.modeldefs.io;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.kernel.JMadKernel;
import cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil;

public interface ModelFileFinder {

    /**
     * returns an accessible file that is searched according to the given {@link ModelFile}, but (in case coming from an
     * archive) extracted in a way that it is unique for the given kerne. This avoids potential name conflicts between
     * files coming from different model packages used in kernels that are concurrently running.
     * 
     * @param modelFile the instance of {@link ModelFile} which describes where to find the file
     * @param kernel the jmad kernel relative to whose temp path files should be placed.
     * @return the file
     */
    File getFile(ModelFile modelFile, JMadKernel kernel);
    
    /**
     * returns the content of the model file as input stream. This is useful e.g. for copying the file or for packing it
     * into a zip archive.
     * 
     * @param modelFile the model file for which to get the stream
     * @return the input stream
     */
    InputStream getStream(ModelFile modelFile);
    
    /**
     * Returns an optional file that is the source of the given model file. This will return an empty optional in case
     * the source of this model file is not a plain file, but is supposed to be extracted from an archive. In other
     * words, the model definition for which this file finder is responsible is not coming from a file.
     * 
     * @param modelFile the model file for which to retrieve the source file
     * @return an optional of the source file for the given model file, or {@link Optional#empty()} if the source of the
     *         model definition does not come from a file
     */
    Optional<File> getLocalSourceFile(ModelFile modelFile);

    /**
     * puts together the path within the repository
     * 
     * @param modelFile the model file for which to put together the path
     * @return the absolute path to the file
     */
    String getRepositoryPath(ModelFile modelFile);

    /**
     * puts together the resource path for the file
     * 
     * @param modelFile the {@link ModelFile} for which to put together the resource path
     * @return the relative path used in jars below {@link ModelDefinitionUtil#PACKAGE_OFFSET} and in zip archives.
     */
    String getArchivePath(ModelFile modelFile);

    /**
     * set the priority mode for searching repository files.
     * 
     * @param priority the new priority
     */
    void setRepositoryFilePriority(RepositoryFilePriority priority);

    /**
     * @return the actual priority mode for searching repository files.
     */
    RepositoryFilePriority getRepositoryFilePriority();

    /**
     * This enum defines what shall take priority if dealing with repository files.
     * 
     * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
     */
    enum RepositoryFilePriority {
        /**
         * when the priority is set to Archive, then a repository file is first searched in the archive (jar, zip) and
         * only if it cannot be extracted then it is looked up in the real repository.
         */
        ARCHIVE,
        /**
         * when this is used, then a repository file is first looked up in the repository path. Only if it can not be
         * found there then it is extracted from the archive.
         */
        REPOSITORY;
    }

}
