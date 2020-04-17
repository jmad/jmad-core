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
package cern.accsoft.steering.jmad.tools.response;

import Jama.Matrix;
import cern.accsoft.steering.jmad.domain.elem.Element;
import cern.accsoft.steering.jmad.domain.elem.JMadElementType;
import cern.accsoft.steering.jmad.domain.elem.impl.Corrector;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.domain.machine.filter.NameFilter;
import cern.accsoft.steering.jmad.domain.result.Result;
import cern.accsoft.steering.jmad.domain.result.ResultType;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResultImpl;
import cern.accsoft.steering.jmad.domain.result.tfs.TfsResultRequestImpl;
import cern.accsoft.steering.jmad.domain.types.enums.JMadPlane;
import cern.accsoft.steering.jmad.domain.var.enums.MadxTwissVariable;
import cern.accsoft.steering.jmad.kernel.task.AddFieldErrors;
import cern.accsoft.steering.jmad.model.JMadModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import static java.util.Collections.newSetFromMap;
import static java.util.Collections.singletonList;

/**
 * This implementation of {@link ResponseMatrixTool} calculates the response matrix using the exact kick strengths given
 * in the request and calculates the response matrix by the use of two trajectories returned by the madx-model. It thus
 * includes all (even nonlinear) effects and coupling.
 *
 * @author Kajetan Fuchsberger (kajetan.fuchsberger at cern.ch)
 */
public class FullResponseMatrixTool implements ResponseMatrixTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(FullResponseMatrixTool.class);
    /**
     * below this value we treat the kick as zero and leave the matrix values also at zero.
     */
    private static final double KICK_ZERO_LIMIT = 1e-10;
    private static final double BEND_TILT_TOLERANCE = 1e-5;

    private final Set<BiConsumer<ResponseRequest, Integer>> progressListeners =
            newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public Matrix calcResponseMatrix(JMadModel model, ResponseRequest request) throws JMadModelException {

        List<String> monitorNames = request.getMonitorNames();
        List<String> correctorNames = request.getCorrectorNames();
        List<JMadPlane> monitorPlanes = request.getMonitorPlanes();
        List<JMadPlane> correctorPlanes = request.getCorrectorPlanes();
        List<Double> strengthValues = request.getStrengthValues();

        Matrix matrix = new Matrix(monitorNames.size(), correctorNames.size());

        for (int i = 0; i < correctorNames.size(); i++) {
            String correctorName = correctorNames.get(i);
            JMadPlane correctorPlane = correctorPlanes.get(i);
            double strengthValue = strengthValues.get(i);
            LOGGER.info("Calculating for corrector {} ({} of {})", correctorName, i + 1, correctorNames.size());

            Element element = model.getActiveRange().getElement(correctorName);
            if (element == null) {
                throw new JMadModelException("Could not find element with name '" + correctorName
                        + "' in active range.");
            }

            double deltaKick = 2 * strengthValue;

            if (Math.abs(deltaKick) < KICK_ZERO_LIMIT) {
                LOGGER.info("  corrector {} skipped: no kick", correctorName);
                notifyProgressListeners(request, i);
                continue;
            }

            TfsResultImpl minus = calcResponse(model, element, correctorPlane, -strengthValue, monitorNames,
                    request.getMonitorRegexps());
            TfsResultImpl plus = calcResponse(model, element, correctorPlane, strengthValue, monitorNames,
                    request.getMonitorRegexps());

            List<Double> plusXData = plus.getDoubleData(MadxTwissVariable.X);
            List<Double> plusYData = plus.getDoubleData(MadxTwissVariable.Y);
            List<Double> minusXData = minus.getDoubleData(MadxTwissVariable.X);
            List<Double> minusYData = minus.getDoubleData(MadxTwissVariable.Y);

            for (int j = 0; j < monitorNames.size(); j++) {
                String monitorName = monitorNames.get(j);
                JMadPlane monitorPlane = monitorPlanes.get(j);

                Integer plusIndex = plus.getElementIndex(monitorName);
                if (plusIndex == null) {
                    throw new JMadModelException("No Data for monitor '" + monitorName + "' in plus-Result.");
                }
                Integer minusIndex = minus.getElementIndex(monitorName);
                if (minusIndex == null) {
                    throw new JMadModelException("No Data for monitor '" + monitorName + "' in minus-Result.");
                }

                double deltaPos;
                if (JMadPlane.H.equals(monitorPlane)) {
                    deltaPos = plusXData.get(plusIndex) - minusXData.get(minusIndex);
                } else if (JMadPlane.V.equals(monitorPlane)) {
                    deltaPos = plusYData.get(plusIndex) - minusYData.get(minusIndex);
                } else {
                    throw new JMadModelException("Unable to handle plane '" + monitorPlane + "'");
                }

                double value = deltaPos / deltaKick;

                /*
                 * inverting if it relates to a corrector that should be inverted by definition
                 */
                for (NameFilter filter : model.getActiveRange().getCorrectorInvertFilters()) {
                    if (filter.isConcerned(correctorName, correctorPlane)) {
                        value *= -1;
                        break;
                    }
                }

                /*
                 * inverting if it relates to a monitor that should be inverted by definition
                 */
                for (NameFilter filter : model.getActiveRange().getMonitorInvertFilters()) {
                    if (filter.isConcerned(monitorName, monitorPlane)) {
                        value *= -1;
                        break;
                    }
                }

                matrix.set(j, i, value);
            }
            notifyProgressListeners(request, i);
        }
        return matrix;
    }

    private void notifyProgressListeners(ResponseRequest request, int i) {
        progressListeners.forEach(p -> p.accept(request, i));
    }

    @Override
    public void addProgressListener(BiConsumer<ResponseRequest, Integer> listener) {
        progressListeners.add(listener);
    }

    @Override
    public void removeProgressListener(BiConsumer<ResponseRequest, Integer> listener) {
        progressListeners.remove(listener);
    }

    /**
     * calcs the response for one corrector
     *
     * @param model the model from which to calc the response
     * @param element the corrector for which to calc the response
     * @param plane the plane in where to kick
     * @param kick the value for the kick
     * @param monitorNames the monitorNames to be included in the result
     * @param monitorRegexps regexpressions which represent all monitors (using this makes the twiss faster)
     * @return the result of the twiss
     * @throws JMadModelException if something goes wrong
     */
    private TfsResultImpl calcResponse(JMadModel model, Element element, JMadPlane plane, double kick,
                                       List<String> monitorNames, List<String> monitorRegexps) throws JMadModelException {

        addKickToElement(model, element, plane, kick);

        TfsResultRequestImpl resultRequest = new TfsResultRequestImpl();
        if (monitorRegexps.isEmpty()) {
            /*
             * if none are defined, then we add just the monitorNames, but this seems to be slower for some reason.
             */
            for (String monitorName : monitorNames) {
                resultRequest.addElementFilter(monitorName);
            }
        } else {
            /*
             * if element-names for the response were defined for the range, then we use them
             */
            for (String regexp : monitorRegexps) {
                resultRequest.addElementFilter(regexp);
            }
        }
        resultRequest.addVariable(MadxTwissVariable.NAME);
        resultRequest.addVariable(MadxTwissVariable.X);
        resultRequest.addVariable(MadxTwissVariable.Y);
        resultRequest.addVariable(MadxTwissVariable.KEYWORD);

        TfsResultImpl tfsResult;
        try {
            Result result = model.twiss(resultRequest);
            if (ResultType.TFS_RESULT != result.getResultType()) {
                throw new JMadModelException("Twiss returned wrong type of result!");
            }
            tfsResult = (TfsResultImpl) result;
        } finally {
            /* reset strength to old Value */
            addKickToElement(model, element, plane, -kick);
        }
        return tfsResult;
    }

    private void addKickToElement(JMadModel model, Element element, JMadPlane plane, double kick) throws JMadModelException {
        if (JMadElementType.CORRECTOR.isTypeOf(element)) {
            Corrector corrector = (Corrector) element;
            double oldKick = corrector.getKick(plane);
            corrector.setKick(plane, oldKick + kick);
        } else if (JMadElementType.BEND.isTypeOf(element)) {
            assertBendCanKickInPlane(element, plane);
            AddFieldErrors fieldErrorsTask = new AddFieldErrors(element.getName(),
                    singletonList(-kick)); /* this will ADD to any existing field errors; - for orbit sign convention */
            model.execute(fieldErrorsTask.compose());
        } else {
            throw new JMadModelException("Element '" + element.getName()
                    + "' is not a corrector or bend! Cannot calc response for this element!");
        }
    }

    private static void assertBendCanKickInPlane(Element element, JMadPlane plane) throws JMadModelException {
        double tilt = Math.abs(Optional.ofNullable(element.getAttribute("tilt")).orElse(0.0));
        boolean hBend = Math.abs(tilt) < BEND_TILT_TOLERANCE || Math.abs(tilt - Math.PI) > BEND_TILT_TOLERANCE;
        boolean vBend = Math.abs(tilt - Math.PI / 2) < BEND_TILT_TOLERANCE;
        if (!(plane == JMadPlane.H && hBend) && !(plane == JMadPlane.V && vBend)) {
            String planeAngle = plane == JMadPlane.H ? "0 rad" : "pi/2 rad";
            throw new JMadModelException("Element '" + element.getName() + "' is a BEND with tilt=" + tilt
                    + " rad - can not kick in " + plane + " (= " + planeAngle + ", tol=" + BEND_TILT_TOLERANCE + ")");
        }
    }

}
