package cern.accsoft.steering.jmad.tools.response;

import java.util.List;

import cern.accsoft.steering.jmad.domain.types.enums.JMadPlane;

/**
 * this interface defines, what kickers shall be used for calculating the response matrix and at what monitors the
 * response-values shall be calculated.
 * 
 * @author kfuchsbe
 */
public interface ResponseRequest {

    /**
     * This method must return the names of the correctors which shall be used for calculating the response request.
     * 
     * @return The names of the correctors to use.
     */
    public abstract List<String> getCorrectorNames();

    /**
     * must return the planes in which the correctors shall be kicked.
     * 
     * @return the planes for the correctors.
     */
    public abstract List<JMadPlane> getCorrectorPlanes();

    /**
     * This method must return the strengthes which shall be used at the corrector for calculating the response at the
     * according correctors. The given strength will be applied once positive and once negative. The Response-matrix
     * element is then the difference of the two trajectories devided by twice the strength.
     * 
     * @return the strengths to use
     */
    public abstract List<Double> getStrengthValues();

    /**
     * The monitor names.
     * 
     * @return the names of the monitors at which positions the the response shall be calculated.
     */
    public abstract List<String> getMonitorNames();

    /**
     * must return a plane for each of monitors, at which the position shall be taken to calc the response matrix
     * element.
     * 
     * @return the monitor-planes
     */
    public abstract List<JMadPlane> getMonitorPlanes();

    /**
     * must return regular expressions representing the monitors. This makes the twiss faster then using all the names
     * seperately as filter.
     * 
     * @return a list of regular expressions.
     */
    public abstract List<String> getMonitorRegexps();

}