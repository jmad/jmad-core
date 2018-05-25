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

/**
 * 
 */
package cern.accsoft.steering.jmad.modeldefs.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinder;

/**
 * The default implementation of the Optics definition. to operate correctly it needs a {@link ModelFileFinder} and some
 * filenames.
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
@XStreamAlias("optic")
public class OpticsDefinitionImpl implements OpticsDefinition, Cloneable {

    /** The files */
    @XStreamAlias("init-files")
    private List<ModelFile> initFiles = new ArrayList<ModelFile>();

    @XStreamAlias("post-ptc-files")
    private List<ModelFile> postPtcUniverseFiles = new ArrayList<ModelFile>();

    /** The name of this optics */
    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name = null;

    /** per default we only have full optics */
    @XStreamAlias("overlay")
    @XStreamAsAttribute
    private boolean overlay = false;

    /**
     * no-args constructor for XStream
     */
    public OpticsDefinitionImpl() {
        super();
    }

    public OpticsDefinitionImpl(String name, ModelFile... modelFiles) {
        this.name = name;
        for (ModelFile modelFile : modelFiles) {
            this.initFiles.add(modelFile);
        }
    }

    public OpticsDefinitionImpl(String name, boolean overlay, ModelFile... modelFiles) {
        this(name, modelFiles);
        this.overlay = overlay;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public final String[] getOpticFileNames() {
        String[] paths = new String[this.initFiles.size()];
        int fileNumber = 0;
        for (ModelFile mFile : this.initFiles) {
            paths[fileNumber++] = mFile.getName();
        }

        return paths;
    }

    @Override
    public final Collection<ModelFile> getRequiredFiles() {
        Set<ModelFile> files = new HashSet<ModelFile>(getInitFiles());
        files.addAll(getPostPtcUniverseFiles());
        return files;
    }

    @Override
    public List<ModelFile> getInitFiles() {
        return this.initFiles;
    }

    @Override
    public boolean isOverlay() {
        return this.overlay;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public List<ModelFile> getPostPtcUniverseFiles() {
        return this.postPtcUniverseFiles;
    }

    public void addPostPtcUniverseFile(ModelFile modelFile) {
        this.postPtcUniverseFiles.add(modelFile);
    }

    private Object writeReplace() {
        OpticsDefinitionImpl writtenObj;
        try {
            writtenObj = clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }

        if (writtenObj.getInitFiles().isEmpty()) {
            writtenObj.initFiles = null;
        }
        if (writtenObj.getPostPtcUniverseFiles().isEmpty()) {
            writtenObj.postPtcUniverseFiles = null;
        }

        return writtenObj;
    }

    /**
     * is called after creating this object from xml. Some additional initialization is done here, since no emplty lists
     * are stored to xml.
     * 
     * @return the this object, fully configured.
     */
    private Object readResolve() {

        if (this.initFiles == null) {
            this.initFiles = new ArrayList<ModelFile>();
        }
        if (this.postPtcUniverseFiles == null) {
            this.postPtcUniverseFiles = new ArrayList<ModelFile>();
        }
        return this;
    }

    public OpticsDefinitionImpl clone() throws CloneNotSupportedException {
        OpticsDefinitionImpl object = (OpticsDefinitionImpl) super.clone();
        object.overlay = this.overlay;
        object.name = this.name;
        object.initFiles = new ArrayList<ModelFile>(this.initFiles);
        object.postPtcUniverseFiles = new ArrayList<ModelFile>(this.postPtcUniverseFiles);
        return object;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof OpticsDefinitionImpl)) {
			return false;
		}
		OpticsDefinitionImpl other = (OpticsDefinitionImpl) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
