/*
 * $Id $
 * 
 * $Date$ $Revision$ $Author$
 * 
 * Copyright CERN ${year}, All Rights Reserved.
 */
package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import cern.accsoft.steering.jmad.domain.file.CallableModelFile.ParseType;
import cern.accsoft.steering.jmad.domain.file.CallableModelFileImpl;
import cern.accsoft.steering.jmad.domain.file.ModelFile;
import cern.accsoft.steering.jmad.domain.file.ModelFile.ModelFileLocation;

public class CallableModelFileBuilder implements ModelFileBuilder {

    private final String path;
    private ParseType parseType;
    private ModelFileLocation location;

    protected CallableModelFileBuilder(String filePath) {
        this.path = requireNonNull(filePath, "filePath must not be null");
    }

    public static CallableModelFileBuilder of(String filePath) {
        return new CallableModelFileBuilder(filePath);
    }

    public ModelFileBuilder parseAs(ParseType newParseType) {
        AssertUtil.requireNull(this.parseType, "parseType");
        this.parseType = newParseType;
        return this;
    }

    public ModelFileBuilder doNotParse() {
        return this.parseAs(ParseType.NONE);
    }

    public ModelFileBuilder from(ModelFileLocation newLocation) {
        AssertUtil.requireNull(this.location, "location");
        this.location = newLocation;
        return this;
    }

    @Override
    public ModelFile build() {
        if (parseType == null) {
            if (location == null) {
                return new CallableModelFileImpl(path);
            } else {
                return new CallableModelFileImpl(path, location);
            }
        }
        return new CallableModelFileImpl(this.path, this.location, this.parseType);
    }
}
