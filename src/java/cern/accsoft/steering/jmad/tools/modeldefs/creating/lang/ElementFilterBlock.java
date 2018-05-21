/**
 * Copyright (c) 2018 European Organisation for Nuclear Research (CERN), All Rights Reserved.
 */

package cern.accsoft.steering.jmad.tools.modeldefs.creating.lang;

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;

import cern.accsoft.steering.jmad.domain.machine.filter.NameFilter;
import cern.accsoft.steering.jmad.domain.machine.filter.RegexNameFilter;
import cern.accsoft.steering.jmad.domain.types.enums.JMadPlane;

public class ElementFilterBlock {

    private final RangeDefinitionBuilder builder;

    public ElementFilterBlock(RangeDefinitionBuilder builder) {
        this.builder = requireNonNull(builder, "builder must not be null");
    }

    public OngoingRegexFilter monitorRegex(String regex) {
        requireNonNull(regex, "regex must not be null");
        return new OngoingRegexFilter(regex, builder::addMonitorInvertFilter);
    }

    public OngoingRegexFilter correctorRegex(String regex) {
        requireNonNull(regex, "regex must not be null");
        return new OngoingRegexFilter(regex, builder::addCorrectorInvertFilter);
    }

    public static final class OngoingRegexFilter {
        private final String regex;
        private final Consumer<NameFilter> consumer;

        public OngoingRegexFilter(String regex, Consumer<NameFilter> consumer) {
            this.regex = regex;
            this.consumer = consumer;
        }

        void inPlane(JMadPlane plane) {
            requireNonNull(plane, "plane must not be null");
            consumer.accept(new RegexNameFilter(regex, plane));
        }

    }

}
