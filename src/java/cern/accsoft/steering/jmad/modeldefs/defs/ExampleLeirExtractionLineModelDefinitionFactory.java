/**
 * 
 */
package cern.accsoft.steering.jmad.modeldefs.defs;

import static cern.accsoft.steering.jmad.domain.file.CallableModelFile.ParseType.STRENGTHS;
import static cern.accsoft.steering.jmad.domain.file.ModelFile.ModelFileLocation.RESOURCE;

import cern.accsoft.steering.jmad.tools.modeldefs.creating.lang.JMadModelDefinitionDslSupport;

/**
 * This class is the actual model configuration for the leir extraction transfer line.
 */
public class ExampleLeirExtractionLineModelDefinitionFactory extends JMadModelDefinitionDslSupport {
    {
        name("Example LEIREXTR");

        offsets(o -> {
            o.resource("exampleleir");
        });

        init(i -> {
            i.call("lead.madx").from(RESOURCE);
            i.call("leir_extraction_fringe_field_2018.seq").from(RESOURCE);
        });

        optics("leir_extraction_2018") //
                .isDefault() //
                .isDefinedAs(o -> {
                    o.call("leir_extraction_2018.str").from(RESOURCE).parseAs(STRENGTHS);
                });

        sequence("eeetl").isDefault().isDefinedAs(s -> {
            s.range("ALL").isDefault().isDefinedAs(r -> {
                r.twiss(t -> {
                    t.betx(4.99920392);
                    t.dx(0.00159546);
                    t.bety(5.00038080);
                    t.calcAtCenter();
                });
            });
        });
    }

}
