package cern.accsoft.steering.jmad.modeldefs.io.impl;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.kernel.cmd.CallCommand;
import cern.accsoft.steering.jmad.kernel.cmd.Command;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinderManager;

/**
 * This class provides a "context" for writing Mad-X script files. It holds a
 * {@link JMadModelDefinition}, the {@link ModelFileFinderManager} and the
 * {@link OutputStream} to write to.
 * 
 * It allows the persistence service to write the file in a readable,
 * "procedural" way by calling the methods to write commands, comments, spaces,
 * call files etc.
 * 
 * This class is NOT thread-safe, and each instance is only to be used to write
 * a single MAD-X output file!
 * 
 * @author mihostet
 *
 */
class MadxScriptCreationContext {
	private final ModelFileFinderManager fileFinderManager;
	private final JMadModelDefinition model;
	private final PrintWriter script;

	private boolean writeCommented;

	/**
	 * Create a new MAD-X script creation context.
	 * 
	 * @param fileFinderManager
	 *            the {@link ModelFileFinderManager} (to be fetched from the
	 *            application context)
	 * @param modelDefinition
	 *            the {@link JMadModelDefinition} that will be saved
	 * @param outputStream
	 *            the stream to write to
	 */
	MadxScriptCreationContext(ModelFileFinderManager fileFinderManager, JMadModelDefinition modelDefinition,
			OutputStream outputStream) {
		this.fileFinderManager = fileFinderManager;
		this.model = modelDefinition;
		this.script = new PrintWriter(new OutputStreamWriter(outputStream));
	}

	/**
	 * Write a spacer (blank line)
	 */
	public void space() {
		script.println();
		script.println();
	}

	/**
	 * Write a single or multi line comment, prefixing each line accordingly
	 * 
	 * @param comment
	 *            the comment to write
	 */
	public void comment(String comment) {
		script.println("! " + comment.replaceAll("\n", "\n ! "));
	}

	/**
	 * Write a Mad-X command
	 * 
	 * The line will be commented if setCommented() has been set to true before
	 * 
	 * @param madxCommand
	 *            the command to write
	 */
	public void command(Command madxCommand) {
		if (writeCommented) {
			comment(madxCommand.compose());
		} else {
			script.println(madxCommand.compose());
		}
	}

	/**
	 * Write the Mad-X command to call a {@link ModelFile}
	 *
	 * The line will be commented if setCommented() has been set to true before
	 * 
	 * @param file
	 *            the file to call
	 */
	public void call(ModelFile file) {
		command(new CallCommand(fileFinderManager.getModelFileFinder(model).getArchivePath(file)));
	}

	/**
	 * Returns true if commands are currently written as comments
	 * 
	 * @return the comment status
	 */
	public boolean isCommented() {
		return writeCommented;
	}

	/**
	 * While set to true, all further commands/calls will be written to the MAD-X
	 * script as comments. When set back to false, commands/calls are written in
	 * their active form.
	 * 
	 * @param writeCommented
	 *            whether to write commands/calls commented
	 */
	public void setCommented(boolean writeCommented) {
		this.writeCommented = writeCommented;
	}

	/**
	 * Flush the output stream
	 */
	public void flush() {
		script.flush();
	}

}
