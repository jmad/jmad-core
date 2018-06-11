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

import static cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExportRequest.allFrom;

import java.io.File;
import java.io.OutputStream;

import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.util.xml.PersistenceService;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;

public interface ModelDefinitionPersistenceService extends PersistenceService<JMadModelDefinition> {
	File save(JMadModelDefinitionExportRequest exportRequest, File file) throws PersistenceServiceException;

	void save(JMadModelDefinitionExportRequest exportRequest, OutputStream outStream)
			throws PersistenceServiceException;

	default File save(JMadModelDefinition modelDefinition, File file) throws PersistenceServiceException {
		return save(allFrom(modelDefinition), file);
	}

	default void save(JMadModelDefinition modelDefinition, OutputStream outStream) throws PersistenceServiceException {
		save(allFrom(modelDefinition), outStream);
	}
}
