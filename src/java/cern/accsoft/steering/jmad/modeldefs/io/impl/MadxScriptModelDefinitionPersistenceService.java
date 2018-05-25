package cern.accsoft.steering.jmad.modeldefs.io.impl;

import static java.lang.String.format;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.kernel.cmd.CallCommand;
import cern.accsoft.steering.jmad.kernel.cmd.Command;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.OpticsDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExportRequest;
import cern.accsoft.steering.jmad.modeldefs.io.ModelDefinitionPersistenceService;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinderManager;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;

public class MadxScriptModelDefinitionPersistenceService implements ModelDefinitionPersistenceService {

	private ModelFileFinderManager fileFinderManager;

	@Override
	public File save(JMadModelDefinition object, File file) throws PersistenceServiceException {
		try {
			save(object, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			throw new PersistenceServiceException(e);
		}
		return file;
	}

	@Override
	public void save(JMadModelDefinition model, OutputStream outStream) throws PersistenceServiceException {
		PrintWriter script = new PrintWriter(outStream);
		comment(script, " -------- initialization -------- ");
		for (ModelFile file : model.getInitFiles()) {
			command(script, new CallCommand(fileFinderManager.getModelFileFinder(model).getArchivePath(file)));
		}
		space(script);
		for (OpticsDefinition opticsDefinition : model.getOpticsDefinitions()) {
			boolean writeCommented = !model.getDefaultOpticsDefinition().equals(opticsDefinition);
			comment(script, format(" -------- optics %s -------- ", opticsDefinition.getName()));
			for (ModelFile file : opticsDefinition.getInitFiles()) {
				command(script, //
						new CallCommand(fileFinderManager.getModelFileFinder(model).getArchivePath(file)), //
						writeCommented);
			}
			// new CallCommand(opticsDefinition.getInitFiles().get(0).)
		}
		script.println("print, \"hello world\";");
		script.println("stop;");
		script.flush();
	}

	private void space(PrintWriter script) {
		script.println();
		script.println();
	}

	private void comment(PrintWriter script, String comment) {
		script.println("! " + comment.replaceAll("\n", "\n ! "));
	}

	private void command(PrintWriter script, Command madxCommand) {
		command(script, madxCommand, false);
	}

	private void command(PrintWriter script, Command madxCommand, boolean commented) {
		if (commented) {
			comment(script, madxCommand.compose());
		} else {
			script.println(madxCommand.compose());
		}
	}

	@Override
	public JMadModelDefinition load(File file) throws PersistenceServiceException {
		throw new PersistenceServiceException(new UnsupportedOperationException(
				"loading a model definition from a MAD-X script is not yet supported."));
	}

	@Override
	public JMadModelDefinition load(InputStream inputStream) throws PersistenceServiceException {
		throw new PersistenceServiceException(new UnsupportedOperationException(
				"loading a model definition from a MAD-X script is not yet supported."));
	}

	@Override
	public JMadModelDefinition clone(JMadModelDefinition object) throws PersistenceServiceException {
		throw new PersistenceServiceException(new UnsupportedOperationException(
				"cloning a model definition through a MAD-X script is not yet supported."));
	}

	@Override
	public String getFileExtension() {
		return ".madx";
	}

	@Override
	public boolean isCorrectFileName(String fileName) {
		return fileName.endsWith(".mad") || fileName.endsWith(".madx");
	}

	public void setFileFinderManager(ModelFileFinderManager fileFinderManager) {
		this.fileFinderManager = fileFinderManager;
	}

	@Override
	public File save(JMadModelDefinitionExportRequest exportRequest, File file) throws PersistenceServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(JMadModelDefinitionExportRequest exportRequest, OutputStream outStream)
			throws PersistenceServiceException {
		// TODO Auto-generated method stub
		
	}

}
