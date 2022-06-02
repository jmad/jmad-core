package cern.accsoft.steering.jmad.modeldefs.io.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import cern.accsoft.steering.jmad.domain.beam.Beam;
import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.domain.twiss.TwissInitialConditionsImpl;
import cern.accsoft.steering.jmad.kernel.AbstractJMadExecutable;
import cern.accsoft.steering.jmad.kernel.cmd.BeamCommand;
import cern.accsoft.steering.jmad.kernel.cmd.Command;
import cern.accsoft.steering.jmad.kernel.cmd.TwissCommand;
import cern.accsoft.steering.jmad.kernel.cmd.UseCommand;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.OpticsDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.JMadModelDefinitionExportRequest;
import cern.accsoft.steering.jmad.modeldefs.io.ModelDefinitionPersistenceService;
import cern.accsoft.steering.jmad.modeldefs.io.ModelFileFinderManager;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;

public class MadxScriptModelDefinitionPersistenceService implements ModelDefinitionPersistenceService {

    private ModelFileFinderManager fileFinderManager;

    @Override
    public File save(JMadModelDefinitionExportRequest exportRequest, File file) throws PersistenceServiceException {
        try {
            save(exportRequest, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new PersistenceServiceException(e);
        }
        return file;
    }

    @Override
    public void save(JMadModelDefinitionExportRequest exportRequest, OutputStream outStream) {
        JMadModelDefinition model = exportRequest.getModelDefinition();
        MadxScriptCreationContext script = new MadxScriptCreationContext(
                fileFinderManager.getModelFileFinder(model)::getArchivePath, model, outStream);
        script.comment("JMad export of model " + model.getName());
        script.space();

        script.comment(" -------- initialization -------- ");
        model.getInitFiles().forEach(script::call);
        script.space();

        for (OpticsDefinition opticsDefinition : exportRequest.getOpticsToExport()) {
            script.setCommented(!model.getDefaultOpticsDefinition().equals(opticsDefinition));
            script.comment(format(" -------- optics: %s -------- ", opticsDefinition.getName()));
            opticsDefinition.getInitFiles().forEach(script::call);
            script.setCommented(false);
        }
        script.space();

        for (SequenceDefinition sequenceDefinition : exportRequest.getSequencesToExport()) {
            boolean isActiveSequence = model.getDefaultSequenceDefinition().equals(sequenceDefinition);
            script.setCommented(!isActiveSequence);

            script.comment(format(" -------- sequence: %s -------- ", sequenceDefinition.getName()));
            Beam beam = sequenceDefinition.getBeam();
            if (beam != null) {
                script.command(new BeamCommand(beam));
            }
            List<RangeDefinition> rangesToExport = sequenceDefinition.getRangeDefinitions().stream() //
                    .filter(exportRequest.getRangesToExport()::contains) //
                    .collect(toList());

            for (RangeDefinition rangeDefinition : rangesToExport) {
                boolean isActiveRange = sequenceDefinition.getDefaultRangeDefinition().equals(rangeDefinition);
                script.setCommented((!isActiveSequence) | (!isActiveRange));
                script.comment(format(" range: %s ", rangeDefinition.getName()));
                script.command(new UseCommand(sequenceDefinition.getName(), rangeDefinition.getMadxRange()));
                rangeDefinition.getPostUseFiles().forEach(script::call);
                TwissInitialConditionsImpl initialConditions = rangeDefinition.getTwiss();
                if (initialConditions != null) {
                    TwissCommand twissCommand = new TwissCommand(initialConditions);
                    twissCommand.setOutputFile(mockFile("twiss.tfs"));
                    script.command(twissCommand);
                }
                script.setCommented(!isActiveSequence);
            }
            script.setCommented(false);
        }
        script.space();

        script.flush();
    }

    public void saveOpticScriptDirectory(JMadModelDefinitionExportRequest exportRequest, File directory) {
        saveOpticScriptDirectory(exportRequest, directory,
                fileFinderManager.getModelFileFinder(exportRequest.getModelDefinition())::getArchivePath);
    }

    public void saveOpticScriptDirectory(JMadModelDefinitionExportRequest exportRequest, File directory,
            Function<ModelFile, String> modelFilePathResolver) {
        checkArgument(directory.isDirectory(), directory.getAbsolutePath() + " is not a directory!");
        JMadModelDefinition model = exportRequest.getModelDefinition();
        for (OpticsDefinition opticsDefinition : exportRequest.getOpticsToExport()) {
            MadxScriptCreationContext script = new MadxScriptCreationContext(modelFilePathResolver, model,
                    openOpticFile(directory, opticsDefinition));
            script.comment("JMad export of model " + model.getName() + " optic " + opticsDefinition.getName());
            script.comment("Generated: " + Instant.now().toString());
            script.space();
            opticsDefinition.getInitFiles().forEach(script::call);
            script.close();
        }
    }

    private static FileOutputStream openOpticFile(File directory, OpticsDefinition opticsDefinition) {
        String filePath = directory.getAbsolutePath() + "/" + opticsDefinition.getName() + ".madx";
        try {
            return new FileOutputStream(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Can not open file " + filePath, e);
        }
    }

    /**
     * Creates a "fake" {@link File}, which points to a RELATIVE path when
     * getAbsolutePath() is called. This is meant to be passed to a {@link Command}
     * (an {@link AbstractJMadExecutable}) as an argument to write to a relative
     * path (instead of to an absolute one during JMad Kernel Execution)
     *
     * @param fileName the relative path or file name
     * @return a "fake" {@link File} pointing to that path
     */
    private static File mockFile(String fileName) {
        return new File(fileName) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getAbsolutePath() {
                return this.getName();
            }
        };
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
}
