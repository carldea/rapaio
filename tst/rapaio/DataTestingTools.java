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

package rapaio;

import rapaio.core.RandomSource;
import rapaio.core.distributions.Normal;
import rapaio.data.VarBinary;
import rapaio.data.VarDouble;
import rapaio.data.VarInt;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 10/25/19.
 */
public final class DataTestingTools {

    public static VarDouble generateRandomDoubleVariable(int len, double nonMissing) {
        Normal normal = Normal.std();
        return VarDouble.from(len, row -> {
            if(RandomSource.nextDouble() < nonMissing) {
                double value = normal.sampleNext();
                return value + Math.signum(value)*2;
            }
            return VarDouble.MISSING_VALUE;
        });
    }

    public static VarInt generateRandomIntVariable(int len, int from, int to, double nonMissing) {
        return VarInt.from(len, row -> RandomSource.nextDouble() < nonMissing ? RandomSource.nextInt(to - from) + from : VarInt.MISSING_VALUE);
    }

    public static VarBinary generateRandomBinaryVariable(int len, double nonMissing) {
        return VarBinary.from(len, row -> RandomSource.nextDouble() < nonMissing ? RandomSource.nextDouble() <= 0.5 : null);
    }
}
