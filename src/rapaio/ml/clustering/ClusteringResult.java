/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2013 - 2021 Aurelian Tutuianu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package rapaio.ml.clustering;

import rapaio.data.Frame;
import rapaio.data.VarInt;
import rapaio.printer.Printable;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 8/31/20.
 */
public class ClusteringResult implements Printable {

    protected final ClusteringModel<?, ?, ?> model;
    protected final Frame df;
    protected final VarInt assignment;

    public ClusteringResult(ClusteringModel<?, ?, ?> model, Frame df, VarInt assignment) {
        this.model = model;
        this.df = df;
        this.assignment = assignment;
    }

    public ClusteringModel<?, ?, ?> getModel() {
        return model;
    }

    public Frame getDf() {
        return df;
    }

    public VarInt getAssignment() {
        return assignment;
    }
}
