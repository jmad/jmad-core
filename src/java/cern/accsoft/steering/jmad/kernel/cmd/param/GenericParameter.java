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

package cern.accsoft.steering.jmad.kernel.cmd.param;

import cern.accsoft.steering.jmad.domain.types.MadxValue;

import java.util.Collection;

import static java.util.stream.Collectors.joining;

public class GenericParameter<T> extends AbstractParameter {
    private String name;
    private T value;

    private boolean useValueQuotes = false;

    public GenericParameter(String name) {
        this(name, null, false);
    }

    public GenericParameter(String name, T value) {
        this(name, value, false);
    }

    public GenericParameter(String name, T value, boolean useValueQuotes) {
        this.name = name;
        this.value = value;
        this.useValueQuotes = useValueQuotes;
    }

    public boolean isSet() {
        if ((value != null) && (value.getClass().equals(Boolean.class))) {
            return (Boolean) value;
        } else {
            return (value != null);
        }
    }

    @Override
    public String compose() {
        if (value.getClass().equals(Boolean.class)) {
            if ((Boolean) value) {
                return name;
            } else {
                return "";
            }
        } else if (value instanceof Collection) {
            Collection<?> values = (Collection<?>) value;
            return name + "=" + values.stream().map(v -> valueToString(v, useValueQuotes))
                    .collect(joining(", ", "{", "}"));
        } else {
            return name + "=" + valueToString(value, useValueQuotes);
        }
    }

    private static String valueToString(Object value, boolean useValueQuotes) {
        if (value instanceof MadxValue) {
            return ((MadxValue) value).getMadxString();
        } else {
            if (useValueQuotes) {
                return "\"" + value.toString() + "\"";
            } else {
                return value.toString();
            }
        }
    }

}
