package cern.accsoft.steering.jmad.tools.modeldefs.creating;

import static java.util.Objects.requireNonNull;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.io.ModelDefinitionPersistenceService;
import cern.accsoft.steering.jmad.modeldefs.io.impl.ModelDefinitionUtil;
import cern.accsoft.steering.jmad.modeldefs.io.impl.XmlModelDefinitionPersistenceService;
import cern.accsoft.steering.jmad.util.xml.PersistenceServiceException;

public class ModelDefinitionWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelDefinitionWriter.class);

    private static final String DEFAULT_DESTINATION_PATH = "src/java/cern/accsoft/steering/jmad/modeldefs/defs";

    private final String destPath;
    private final ModelDefinitionPersistenceService service = new XmlModelDefinitionPersistenceService();

    public ModelDefinitionWriter(String destPath) {
        this.destPath = requireNonNull(destPath);
    }

    public static ModelDefinitionWriter create() {
        return toDestination(DEFAULT_DESTINATION_PATH);
    }

    public static ModelDefinitionWriter toDestination(String destPath) {
        return new ModelDefinitionWriter(destPath);
    }

    public void write(Iterable<JMadModelDefinition> modelDefinitions) {
        for (JMadModelDefinition modelDefinition : modelDefinitions) {
            File file = getFile(modelDefinition);
            LOGGER.info("Writing model definition '{}' to file {}.", modelDefinition, file);
            writeToXml(modelDefinition, file);
        }
    }

    private File getFile(JMadModelDefinition modelDefinition) {
        String fileName;
        fileName = ModelDefinitionUtil.getProposedXmlFileName(modelDefinition);
        String filePath;
        if (destPath != null) {
            filePath = destPath + File.separator + fileName;
        } else {
            filePath = fileName;
        }
        File file = new File(filePath);
        System.out.println("Writing file '" + file.getAbsolutePath() + "'.");
        return file;
    }

    private void writeToXml(JMadModelDefinition modelDefinition, File file) {
        try {
            service.save(modelDefinition, file);
        } catch (PersistenceServiceException e) {
            System.out.println("Could not save model definition to file '" + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    // public final static void main(String[] args) {
    //
    // ModelDefinitionWriter creator = null;
    // if (args.length > 0) {
    // creator = new ModelDefinitionWriter(args[0]);
    // } else {
    // creator = new ModelDefinitionWriter(DEFAULT_DESTINATION_PATH);
    // }
    //
    // creator.createThem();
    //
    // }

}
