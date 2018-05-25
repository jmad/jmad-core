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
package cern.accsoft.steering.jmad.modeldefs.io.impl;

import static com.google.common.base.Predicates.not;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinitionImpl;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinitionImpl;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExportRequest;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExporter;
import cern.accsoft.steering.jmad.modeldefs.io.ModelDefinitionPersistenceService;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinder;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinderManager;
import cern.accsoft.steering.jmad.util.FileUtil;
import cern.accsoft.steering.jmad.util.StreamUtil;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;

/**
 * This is the default implementation of the {@link JMadModelDefinitionExporter}
 * 
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class JMadModelDefinitionExporterImpl implements JMadModelDefinitionExporter {
	/**
	 * The persistence service to use to write model definition to xml. (injected by
	 * spring)
	 */
	private List<ModelDefinitionPersistenceService> persistenceServices = new ArrayList<ModelDefinitionPersistenceService>();

	/**
	 * The class which keeps track of file finders for the model definitions.
	 */
	private ModelFileFinderManager fileFinderManager;

	/** The logger for the class */
	private static final Logger LOGGER = LoggerFactory.getLogger(JMadModelDefinitionExporterImpl.class);

	@Override
	public File export(JMadModelDefinitionExportRequest exportRequest, File exportPath) {
		if (exportRequest == null) {
			LOGGER.error("No model definition given to export.");
			return null;
		}
		if (exportPath == null) {
			LOGGER.error("No file given. Cannot export model definition.");
			return null;
		}

		ModelDefinitionPersistenceService persistenceService = findPersistenceService(exportPath.getName());

		if (persistenceService != null || exportPath.isDirectory()) {
			return exportAsFiles(exportRequest, exportPath);
		} else {
			/* per default we export as zip */
			return exportAsZip(exportRequest, exportPath);
		}
	}

	@Override
	public File exportAsFiles(JMadModelDefinitionExportRequest exportRequest, File exportPath) {
		if (exportRequest == null) {
			LOGGER.error("No model definition given to export.");
			return null;
		}
		if (exportPath == null) {
			LOGGER.error("No destination dir given. Cannot export model definition.");
			return null;
		}
		JMadModelDefinition modelDefinition = tailorModelDefinition(exportRequest);

		File xmlFile;
		File destDir;
		if (exportPath.isDirectory()) {
			destDir = exportPath;
			/* per default we save as xml */
			xmlFile = new File(destDir.getAbsolutePath() + File.separator + getFileName(modelDefinition));
		} else {
			destDir = exportPath.getAbsoluteFile().getParentFile();
			xmlFile = exportPath;
		}
		FileUtil.createDir(destDir, false);

		/*
		 * first we save the model-def file. If it matches one of the extensions of the
		 * persisters, then we use that one, otherwise we use the default.
		 */
		ModelDefinitionPersistenceService persistenceService = findPersistenceService(xmlFile.getAbsolutePath());
		if (persistenceService == null) {
			xmlFile = new File(xmlFile.getAbsolutePath() + ModelDefinitionUtil.getDefaultFileExtension());
			persistenceService = findPersistenceService(xmlFile.getAbsolutePath());
		}
		if (persistenceService == null) {
			LOGGER.error("Cannot find appropriate persistence service for file '" + xmlFile.getAbsolutePath() + "'.");
		}

		try {

			persistenceService.save(modelDefinition, xmlFile);

			/*
			 * then we loop through all model files and copy all the files.
			 */
			ModelFileFinder fileFinder = getFileFinderManager().getModelFileFinder(modelDefinition);
			for (ModelFile modelFile : getRequiredFiles(modelDefinition)) {
				/*
				 * We use the archive path here. So the file structure is the same as inside the
				 * zip archive.
				 */
				String archivePath = fileFinder.getArchivePath(modelFile);
				File file = new File(destDir.getAbsolutePath() + File.separator + archivePath);

				/*
				 * ensure that the parent dir exists
				 */
				FileUtil.createDir(file.getAbsoluteFile().getParentFile(), false);

				InputStream inStream = fileFinder.getStream(modelFile);
				if (!StreamUtil.toFile(inStream, file)) {
					LOGGER.error("Could not write file '" + file.getAbsolutePath() + "'");
					return null;
				}
			}
			return xmlFile;
		} catch (PersistenceServiceException e) {
			LOGGER.error("Could not save model definition to file '" + xmlFile.getAbsolutePath() + "'.", e);
		}
		return null;
	}

	private ModelDefinitionPersistenceService findPersistenceService(String fileName) {
		for (ModelDefinitionPersistenceService persistenceService : getPersistenceServices()) {
			if (persistenceService.isCorrectFileName(fileName)) {
				return persistenceService;
			}
		}
		return null;
	}

	@Override
	public File exportAsZip(JMadModelDefinitionExportRequest exportRequest, File file) {
		if (exportRequest == null) {
			LOGGER.error("No model definition given to export.");
			return null;
		}
		if (file == null) {
			LOGGER.error("No file given. Cannot export model definition.");
			return null;
		}
		JMadModelDefinition modelDefinition = tailorModelDefinition(exportRequest);

		File zipFile = ModelDefinitionUtil.ensureZipFileExtension(file);

		try {
			/* Create the output stream */
			ZipOutputStream outStream;
			outStream = new ZipOutputStream(new FileOutputStream(zipFile));

			String baseName = ModelDefinitionUtil.getProposedIdStringFromName(modelDefinition);

			/* Add a zip entry to the output stream each persister we have */
			for (ModelDefinitionPersistenceService persistenceService : getPersistenceServices()) {
				outStream.putNextEntry(new ZipEntry(baseName + persistenceService.getFileExtension()));
				persistenceService.save(modelDefinition, outStream);
				outStream.closeEntry();
			}

			/*
			 * next we need the corresponding ModelFileFinder to find all the required files
			 * and put them in the archive.
			 */
			ModelFileFinder fileFinder = getFileFinderManager().getModelFileFinder(modelDefinition);

			/*
			 * now we are ready to copy all the files into the archive.
			 */
			for (ModelFile modelFile : getRequiredFiles(modelDefinition)) {
				outStream.putNextEntry(new ZipEntry(fileFinder.getArchivePath(modelFile)));
				InputStream inStream = fileFinder.getStream(modelFile);
				StreamUtil.copy(inStream, outStream);
				outStream.closeEntry();
				inStream.close();
			}

			outStream.close();
			return zipFile;
		} catch (IOException e) {
			LOGGER.error("Could not save model definition to zip file '" + zipFile.getAbsolutePath() + "'", e);
		} catch (PersistenceServiceException e) {
			LOGGER.error("Could not save model definition to zip file '" + zipFile.getAbsolutePath() + "'", e);
		}
		return null;
	}

	private JMadModelDefinition tailorModelDefinition(JMadModelDefinitionExportRequest request) {
		JMadModelDefinition modelDefinitionForExport = cloneModel(request.getModelDefinition());

		/* remove optics/sequences/ranges not selected */
		modelDefinitionForExport.getOpticsDefinitions().removeIf(not(request.getOpticsToExport()::contains));
		modelDefinitionForExport.getSequenceDefinitions().removeIf(not(request.getSequencesToExport()::contains));
		modelDefinitionForExport.getSequenceDefinitions().forEach(//
				seq -> seq.getRangeDefinitions().removeIf(not(request.getRangesToExport()::contains)));

		/* remove empty sequences (no ranges) */
		modelDefinitionForExport.getSequenceDefinitions().removeIf(s -> s.getRangeDefinitions().isEmpty());

		/* if we end up with an empty model, throw */
		if (modelDefinitionForExport.getOpticsDefinitions().isEmpty()) {
			throw new IllegalArgumentException("no optics definitions have been selected for export!");
		}
		if (modelDefinitionForExport.getSequenceDefinitions().isEmpty()) {
			throw new IllegalArgumentException("no sequence definitions have been selected for export!");
		}
		if (modelDefinitionForExport.getRangeDefinitions().isEmpty()) {
			throw new IllegalArgumentException("no ranges have been selected for export!");
		}

		/* fix defaults */
		if (!modelDefinitionForExport.getOpticsDefinitions()
				.contains(modelDefinitionForExport.getDefaultOpticsDefinition())) {
			((JMadModelDefinitionImpl) modelDefinitionForExport)
					.setDefaultOpticsDefinition(modelDefinitionForExport.getOpticsDefinitions().get(0));
		}

		if (!modelDefinitionForExport.getSequenceDefinitions()
				.contains(modelDefinitionForExport.getDefaultSequenceDefinition())) {
			((JMadModelDefinitionImpl) modelDefinitionForExport)
					.setDefaultSequenceDefinition(modelDefinitionForExport.getSequenceDefinitions().get(0));
		}

		for (SequenceDefinition sequence : modelDefinitionForExport.getSequenceDefinitions()) {
			if (!sequence.getRangeDefinitions().contains(sequence.getDefaultRangeDefinition())) {
				((SequenceDefinitionImpl) sequence).setDefaultRangeDefinition(sequence.getRangeDefinitions().get(0));
			}
		}

		return modelDefinitionForExport;
	}

	private JMadModelDefinition cloneModel(JMadModelDefinition model) {
		for (ModelDefinitionPersistenceService cloneService : persistenceServices) {
			try {
				return cloneService.clone(model);
			} catch (Exception e) {
				/* try next service */
			}
		}
		throw new IllegalStateException("no persistence service was able to clone the model definition");
	}

	private String getFileName(JMadModelDefinition modelDefinition) {
		if ((modelDefinition.getSourceInformation() != null)
				&& (modelDefinition.getSourceInformation().getFileName() != null)) {
			return modelDefinition.getSourceInformation().getFileName();
		}
		return ModelDefinitionUtil.getProposedDefaultFileName(modelDefinition);
	}

	/**
	 * collects all the required files for a model definition. it returns a
	 * collection which will contain all the model files with the same archive path
	 * only once.
	 * 
	 * @param modelDefinition
	 *            the model definition for which to collect the files
	 * @return all the files, with unique archive-path
	 */
	private Collection<ModelFile> getRequiredFiles(JMadModelDefinition modelDefinition) {
		ModelFileFinder fileFinder = getFileFinderManager().getModelFileFinder(modelDefinition);
		Map<String, ModelFile> modelFiles = new HashMap<String, ModelFile>();
		for (ModelFile modelFile : ModelDefinitionUtil.getRequiredModelFiles(modelDefinition)) {
			String archivePath = fileFinder.getArchivePath(modelFile);
			modelFiles.put(archivePath, modelFile);
		}
		return modelFiles.values();
	}

	public void setFileFinderManager(ModelFileFinderManager fileFinderManager) {
		this.fileFinderManager = fileFinderManager;
	}

	private ModelFileFinderManager getFileFinderManager() {
		return fileFinderManager;
	}

	public void setPersistenceServices(List<ModelDefinitionPersistenceService> persistenceServices) {
		this.persistenceServices = persistenceServices;
	}

	private List<ModelDefinitionPersistenceService> getPersistenceServices() {
		return persistenceServices;
	}

}
