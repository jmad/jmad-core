package cern.accsoft.steering.jmad.kernel.task;

import cern.accsoft.steering.jmad.kernel.cmd.Command;
import cern.accsoft.steering.jmad.kernel.cmd.EOptionCommand;
import cern.accsoft.steering.jmad.kernel.cmd.EfcompCommand;
import cern.accsoft.steering.jmad.kernel.cmd.SelectCommand;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static cern.accsoft.steering.jmad.kernel.cmd.SelectCommand.SELECT_FLAG_ERROR;

public class AddFieldErrors extends AbstractTask {

    private final String elementName;
    private final List<Double> absoluteErrors;
    private final List<Double> absoluteSkewErrors;

    public AddFieldErrors(String element, List<Double> absoluteErrors) {
        this.elementName = element;
        this.absoluteErrors = ImmutableList.copyOf(absoluteErrors);
        absoluteSkewErrors = null;
    }

    public AddFieldErrors(String element, List<Double> absoluteErrors, List<Double> absoluteSkewErrors) {
        this.elementName = element;
        this.absoluteErrors = ImmutableList.copyOf(absoluteErrors);
        this.absoluteSkewErrors = ImmutableList.copyOf(absoluteSkewErrors);
    }

    @Override
    protected List<Command> getCommands() {
        return ImmutableList.of(
                new EOptionCommand(null, true),
                clearErrorSelect(),
                selectErrorElement(elementName),
                efcomp()
        );
    }

    private static SelectCommand clearErrorSelect() {
        SelectCommand select = new SelectCommand();
        select.setFlag(SELECT_FLAG_ERROR);
        select.setClear(true);
        return select;
    }

    private static SelectCommand selectErrorElement(String elementName) {
        SelectCommand select = new SelectCommand();
        select.setFlag(SELECT_FLAG_ERROR);
        select.setPattern(elementName);
        return select;
    }

    private EfcompCommand efcomp() {
        EfcompCommand efcomp = new EfcompCommand();
        efcomp.setAbsoluteErrors(absoluteErrors);
        efcomp.setAbsoluteSkewErrors(absoluteSkewErrors);
        return efcomp;
    }
}
