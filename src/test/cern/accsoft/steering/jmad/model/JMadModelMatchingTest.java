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

package cern.accsoft.steering.jmad.model;

import static org.assertj.core.data.Offset.offset;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cern.accsoft.steering.jmad.JMadTestCase;
import cern.accsoft.steering.jmad.domain.ex.JMadModelException;
import cern.accsoft.steering.jmad.domain.knob.strength.Strength;
import cern.accsoft.steering.jmad.domain.result.match.MatchResult;
import cern.accsoft.steering.jmad.domain.result.match.MatchResultRequestImpl;
import cern.accsoft.steering.jmad.domain.result.match.input.MadxVaryParameterImpl;
import cern.accsoft.steering.jmad.domain.result.match.input.MatchConstraintGlobal;
import cern.accsoft.steering.jmad.domain.result.match.output.MatchConstraintResult;
import cern.accsoft.steering.jmad.domain.var.enums.MadxGlobalVariable;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;

public class JMadModelMatchingTest extends JMadTestCase {

    private static JMadModelDefinition modelDefinition;
    private static JMadModel model;

    @BeforeClass
    public static void classSetUp() {
        modelDefinition = JMadTestCase.findExampleModelDefinition();
        model = getJMadService().createModel(modelDefinition);
    }

    @Before
    public void setUp() throws Exception {
        model.init();
    }

    @After
    public void tearDown() throws Exception {
        model.cleanup();
    }

    @Test
    public void initialTuneIsCorrect() throws JMadModelException {
        Double q1 = model.calcTwissSummary().getDoubleValue(MadxGlobalVariable.Q1);
        Assertions.assertThat(q1).isCloseTo(12.0299, offset(0.0001));
    }
    
    @Test
    public  void matchingTuneInTransferlineWorks() throws JMadModelException {
        MatchResultRequestImpl request = new MatchResultRequestImpl();
       
        Strength str = model.getStrengthVarManager().getStrengthVarSet().getStrength("kqif.20800");
        
        request.addMadxVaryParameter(new MadxVaryParameterImpl(str));
        
        MatchConstraintGlobal matchConstraint = new MatchConstraintGlobal();
        matchConstraint.setQ1(13.0);
        
        request.addMatchConstraint(matchConstraint);
        
       MatchResult result = model.match(request);

       MatchConstraintResult finalVal = result.getConstraintParameterResults().get(0);
       Assertions.assertThat(finalVal.getFinalValue()).isCloseTo(13.0, offset(0.0001));
       
    }

}
