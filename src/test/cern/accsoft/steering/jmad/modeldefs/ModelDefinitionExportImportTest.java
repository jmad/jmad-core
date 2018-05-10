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

package cern.accsoft.steering.jmad.modeldefs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cern.accsoft.steering.jmad.JMadTestCase;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResultRequestImpl;
import cern.accsoft.steering.jmad.model.JMadModel;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.SourceInformation;
import cern.accsoft.steering.jmad.modeldefs.domain.SourceInformation.SourceType;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExporter;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionImporter;
import cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil;
import cern.accsoft.steering.jmad.util.FileUtil;

public class ModelDefinitionExportImportTest extends JMadTestCase {

    public static final String TEST_FILE_NAME = "test.jmd.zip";
    private JMadModelDefinitionExporter exporter;
	private JMadModelDefinitionImporter importer;

	private File testZipFile;
	private File testDir;

	@Before
	public void setUp() throws Exception {
		exporter = getJMadService().getModelDefinitionExporter();
		importer = getJMadService().getModelDefinitionImporter();
		testDir = Files.createTempDirectory(ModelDefinitionExportImportTest.class.getSimpleName()).toFile();
		testZipFile = new File(testDir, TEST_FILE_NAME);
	}

	@After
	public void tearDown() throws IOException{
        Files.deleteIfExists(testZipFile.toPath());
        FileUtil.deleteDir(testDir);
	}

	@Test
	public void testExportAsZip() {
		JMadModelDefinition modelDefinition = findExampleModelDefinition();
		assertTrue("No test file must be available.", !testZipFile.exists());
		File file = exporter.exportAsZip(modelDefinition, testZipFile);
		assertNotNull("File must not be null if the export was successful.",
				file);
		assertTrue("Test file should exist", testZipFile.exists());
		assertEquals("Return file should be the same as the original one.",
				testZipFile.getAbsolutePath(), file.getAbsolutePath());

	}

	@Test
	public void testExportAsFilesWithExistingDir() {
		JMadModelDefinition modelDefinition = findExampleModelDefinition();
		File file = exporter.exportAsFiles(modelDefinition, testDir);
		assertNotNull("returned file must not be null", file);
		assertTrue("Test dir should exist", testDir.exists());
		assertEquals(
				"Returned parent dir should be the same as the original one.",
				testDir.getAbsolutePath(), file.getAbsoluteFile()
						.getParentFile().getAbsolutePath());
	}

	@Test
	public void testExportAsFilesWithFile() {
		JMadModelDefinition modelDefinition = findExampleModelDefinition();
		String exportFilePath = testDir.getAbsolutePath() + File.separator + "test";
		File file = exporter.exportAsFiles(modelDefinition, new File(exportFilePath));
		assertNotNull("returned file must not be null", file);
		assertTrue("Test dir should exist", testDir.exists());
		assertTrue("export file should exist", file.exists());

		assertEquals(
				"Returned parent dir should be the same as the original one.",
				exportFilePath + ".jmd.xml", file.getAbsolutePath());
	}

	@Test
	public void testExportAsFilesWithJsonFile() {
		JMadModelDefinition modelDefinition = findExampleModelDefinition();
		String exportFilePath = testDir.getAbsolutePath() + File.separator + "test.jmd.json";
		File file = exporter.exportAsFiles(modelDefinition, new File(exportFilePath));
		assertNotNull("returned file must not be null", file);
		assertTrue("Test dir should exist", testDir.exists());
		assertTrue("export file should exist", file.exists());

		assertEquals("Returned file should be the same as the original one.",
				exportFilePath, file.getAbsolutePath());
	}

	@Test
	public void testImportFromJsonFile() {
		JMadModelDefinition modelDefinition = findExampleModelDefinition();
		String exportFilePath = testDir.getAbsolutePath() + File.separator + "test.jmd.json";
		File file = exporter.exportAsFiles(modelDefinition, new File(exportFilePath));

		JMadModelDefinition importedModelDefinition = importer.importModelDefinition(file);
		assertNull("Json import is not implemented at the moment!", importedModelDefinition);
	}

	@Test
	public void testImportFromZip() throws JMadModelException, InterruptedException {
		/* To test this we first have to export the model definition as zip */
		JMadModelDefinition modelDefinition = findExampleModelDefinition();
		exporter.exportAsZip(modelDefinition, testZipFile);

		/* and then reimport the model definitions from the zip file */
		Collection<JMadModelDefinition> modelDefinitions = importer.importModelDefinitions(testZipFile);
		assertEquals("Zip file should contain exactly one model definition.",
				1, modelDefinitions.size());

		JMadModelDefinition importedDefinition = importer.importModelDefinition(testZipFile);
		assertNotNull("Model definition must not be null.", importedDefinition);
		assertEquals(modelDefinition.getName(), importedDefinition.getName());

		/* The new definition should contain the new source information */
		SourceInformation sourceInformation = importedDefinition.getSourceInformation();
		assertEquals(testZipFile.getAbsolutePath(), sourceInformation.getRootPath().getAbsolutePath());
		assertEquals(SourceType.ZIP, sourceInformation.getSourceType());
		assertEquals(
				ModelDefinitionUtil.getProposedXmlFileName(modelDefinition),
				sourceInformation.getFileName());

		/*
		 * finally we try to create a model with the newly imported definition.
		 */
		JMadModel model = getJMadService().createModel(importedDefinition);
		model.init();
		model.twiss(new TfsResultRequestImpl());
		model.cleanup();
        TimeUnit.SECONDS.sleep(5);
	}
}
