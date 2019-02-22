package rapaio.data.group;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rapaio.core.*;
import rapaio.data.*;
import rapaio.datasets.*;
import rapaio.sys.*;

import static org.junit.Assert.assertEquals;
import static rapaio.data.group.Group.*;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 2/21/19.
 */
public class GroupTest {

    private Frame iris;
    private Frame play;
    private int textWidth=0;

    @Before
    public void setUp() {
        RandomSource.setSeed(1234);
        play = Datasets.loadPlay();
        iris = Datasets.loadIrisDataset();
        textWidth = WS.getPrinter().textWidth();
        WS.getPrinter().withTextWidth(100);
    }

    @After
    public void tearDown() {
        WS.getPrinter().withTextWidth(textWidth);
    }

    @Test
    public void testGroupStrings() {

        Group group1 = Group.from(play, "class", "outlook");
        assertEquals("GroupBy{keys:[class,outlook], group count:5, row count:14}", group1.toString());
        assertEquals("group by: class, outlook\n" +
                "group count: 5\n" +
                "\n" +
                "     class  outlook  row     temp humidity windy      class  outlook  row     temp humidity windy \n" +
                " [0] noplay rain     9  ->    71     80     true  [7] play   overcast 7  ->    64     65     true \n" +
                " [1] noplay rain     10  ->   65     70     true  [8] play   overcast 8  ->    81     75    false \n" +
                " [2] noplay sunny    1  ->    80     90     true  [9] play   rain     11  ->   75     80    false \n" +
                " [3] noplay sunny    2  ->    85     85    false [10] play   rain     12  ->   68     80    false \n" +
                " [4] noplay sunny    3  ->    72     95    false [11] play   rain     13  ->   70     96    false \n" +
                " [5] play   overcast 5  ->    72     90     true [12] play   sunny    0  ->    75     70     true \n" +
                " [6] play   overcast 6  ->    83     78    false [13] play   sunny    4  ->    69     70    false \n", group1.content());
        assertEquals("group by: class, outlook\n" +
                "group count: 5\n" +
                "\n" +
                "     class  outlook  row     temp humidity windy      class  outlook  row     temp humidity windy \n" +
                " [0] noplay rain     9  ->    71     80     true  [7] play   overcast 7  ->    64     65     true \n" +
                " [1] noplay rain     10  ->   65     70     true  [8] play   overcast 8  ->    81     75    false \n" +
                " [2] noplay sunny    1  ->    80     90     true  [9] play   rain     11  ->   75     80    false \n" +
                " [3] noplay sunny    2  ->    85     85    false [10] play   rain     12  ->   68     80    false \n" +
                " [4] noplay sunny    3  ->    72     95    false [11] play   rain     13  ->   70     96    false \n" +
                " [5] play   overcast 5  ->    72     90     true [12] play   sunny    0  ->    75     70     true \n" +
                " [6] play   overcast 6  ->    83     78    false [13] play   sunny    4  ->    69     70    false \n", group1.fullContent());
        assertEquals("group by: class, outlook\n" +
                "group count: 5\n" +
                "\n" +
                "     class  outlook  row     temp humidity windy      class  outlook  row     temp humidity windy \n" +
                " [0] noplay rain     9  ->    71     80     true  [7] play   overcast 7  ->    64     65     true \n" +
                " [1] noplay rain     10  ->   65     70     true  [8] play   overcast 8  ->    81     75    false \n" +
                " [2] noplay sunny    1  ->    80     90     true  [9] play   rain     11  ->   75     80    false \n" +
                " [3] noplay sunny    2  ->    85     85    false [10] play   rain     12  ->   68     80    false \n" +
                " [4] noplay sunny    3  ->    72     95    false [11] play   rain     13  ->   70     96    false \n" +
                " [5] play   overcast 5  ->    72     90     true [12] play   sunny    0  ->    75     70     true \n" +
                " [6] play   overcast 6  ->    83     78    false [13] play   sunny    4  ->    69     70    false \n",group1.summary());

        assertEquals("temp,humidity,windy", String.join(",", group1.getFeatureNameList()));

        Group group2 = Group.from(iris, "class");
        assertEquals("group by: class\n" +
                "group count: 3\n" +
                "\n" +
                "      class     row      sepal-length sepal-width petal-length petal-width \n" +
                "  [0] setosa    0  ->        5.1          3.5         1.4          0.2     \n" +
                "  [1] setosa    1  ->        4.9          3           1.4          0.2     \n" +
                "  [2] setosa    2  ->        4.7          3.2         1.3          0.2     \n" +
                "  [3] setosa    3  ->        4.6          3.1         1.5          0.2     \n" +
                "  [4] setosa    4  ->        5            3.6         1.4          0.2     \n" +
                "  [5] setosa    5  ->        5.4          3.9         1.7          0.4     \n" +
                "  [6] setosa    6  ->        4.6          3.4         1.4          0.3     \n" +
                "  [7] setosa    7  ->        5            3.4         1.5          0.2     \n" +
                "  [8] setosa    8  ->        4.4          2.9         1.4          0.2     \n" +
                "  [9] setosa    9  ->        4.9          3.1         1.5          0.1     \n" +
                " [10] setosa    10  ->       5.4          3.7         1.5          0.2     \n" +
                " [11] setosa    11  ->       4.8          3.4         1.6          0.2     \n" +
                " [12] setosa    12  ->       4.8          3           1.4          0.1     \n" +
                " [13] setosa    13  ->       4.3          3           1.1          0.1     \n" +
                " [14] setosa    14  ->       5.8          4           1.2          0.2     \n" +
                " [15] setosa    15  ->       5.7          4.4         1.5          0.4     \n" +
                " [16] setosa    16  ->       5.4          3.9         1.3          0.4     \n" +
                " [17] setosa    17  ->       5.1          3.5         1.4          0.3     \n" +
                " [18] setosa    18  ->       5.7          3.8         1.7          0.3     \n" +
                " [19] setosa    19  ->       5.1          3.8         1.5          0.3     \n" +
                " [20] setosa    20  ->       5.4          3.4         1.7          0.2     \n" +
                " [21] setosa    21  ->       5.1          3.7         1.5          0.4     \n" +
                " [22] setosa    22  ->       4.6          3.6         1            0.2     \n" +
                " [23] setosa    23  ->       5.1          3.3         1.7          0.5     \n" +
                " [24] setosa    24  ->       4.8          3.4         1.9          0.2     \n" +
                " [25] setosa    25  ->       5            3           1.6          0.2     \n" +
                " [26] setosa    26  ->       5            3.4         1.6          0.4     \n" +
                " [27] setosa    27  ->       5.2          3.5         1.5          0.2     \n" +
                " [28] setosa    28  ->       5.2          3.4         1.4          0.2     \n" +
                " [29] setosa    29  ->       4.7          3.2         1.6          0.2     \n" +
                " ...  ...                ...          ...         ...          ...         \n" +
                "[141] virginica 141  ->      6.9          3.1         5.1          2.3     \n" +
                "[142] virginica 142  ->      5.8          2.7         5.1          1.9     \n" +
                "[143] virginica 143  ->      6.8          3.2         5.9          2.3     \n" +
                "[144] virginica 144  ->      6.7          3.3         5.7          2.5     \n" +
                "[145] virginica 145  ->      6.7          3           5.2          2.3     \n" +
                "[146] virginica 146  ->      6.3          2.5         5            1.9     \n" +
                "[147] virginica 147  ->      6.5          3           5.2          2       \n" +
                "[148] virginica 148  ->      6.2          3.4         5.4          2.3     \n" +
                "[149] virginica 149  ->      5.9          3           5.1          1.8     \n", group2.content());
    }

    @Test
    public void testAggregate() {
        Group group1 = Group.from(iris, "class");
        Group.Aggregate agg1 = group1.aggregate(count("petal-width"));

        assertEquals("Group.Aggregate{group=GroupBy{keys:[class], group count:3, row count:150}, funs=[GroupByFunction{name=count,varNames=[petal-width]}]}", agg1.toString());
        assertEquals("group by: class\n" +
                "group count: 3\n" +
                "group by functions: GroupByFunction{name=count,varNames=[petal-width]}\n" +
                "\n" +
                "      class    petal-width_count \n" +
                "[0]     setosa                50 \n" +
                "[1] versicolor                50 \n" +
                "[2]  virginica                50 \n" +
                "\n", agg1.content());
        assertEquals("group by: class\n" +
                "group count: 3\n" +
                "group by functions: GroupByFunction{name=count,varNames=[petal-width]}\n" +
                "\n" +
                "      class    petal-width_count \n" +
                "[0]     setosa                50 \n" +
                "[1] versicolor                50 \n" +
                "[2]  virginica                50 \n" +
                "\n", agg1.fullContent());
        assertEquals("group by: class\n" +
                "group count: 3\n" +
                "group by functions: GroupByFunction{name=count,varNames=[petal-width]}\n" +
                "\n", agg1.summary());

        assertEquals("      class    petal-width_count \n" +
                "[0]     setosa                50 \n" +
                "[1] versicolor                50 \n" +
                "[2]  virginica                50 \n", agg1.toFrame().content());

        Group group2 = Group.from(iris, VRange.of("class", "petal-width"));
        Group.Aggregate agg2 = group2.aggregate(count("sepal-width"));

        assertEquals("      class    petal-width_0.1_sepal-width_count \n" +
                "[0]     setosa                                 7 \n" +
                "[1] versicolor                                 ? \n" +
                "[2]  virginica                                 ? \n" +
                "\n" +
                "    petal-width_0.2_sepal-width_count petal-width_0.3_sepal-width_count \n" +
                "[0]                                 5                                 7 \n" +
                "[1]                                 ?                                 ? \n" +
                "[2]                                 ?                                 ? \n" +
                "\n" +
                "    petal-width_0.4_sepal-width_count petal-width_0.5_sepal-width_count \n" +
                "[0]                                29                                 1 \n" +
                "[1]                                 ?                                 ? \n" +
                "[2]                                 ?                                 ? \n" +
                "\n" +
                "    petal-width_0.6_sepal-width_count petal-width_1.0_sepal-width_count \n" +
                "[0]                                 1                                 ? \n" +
                "[1]                                 ?                                 7 \n" +
                "[2]                                 ?                                 ? \n" +
                "\n" +
                "    petal-width_1.1_sepal-width_count petal-width_1.2_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                10                                 1 \n" +
                "[2]                                 ?                                 ? \n" +
                "\n" +
                "    petal-width_1.3_sepal-width_count petal-width_1.4_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                 5                                 7 \n" +
                "[2]                                 ?                                 3 \n" +
                "\n" +
                "    petal-width_1.5_sepal-width_count petal-width_1.6_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                 3                                13 \n" +
                "[2]                                 8                                 3 \n" +
                "\n" +
                "    petal-width_1.7_sepal-width_count petal-width_1.8_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                 1                                 3 \n" +
                "[2]                                 5                                 1 \n" +
                "\n" +
                "    petal-width_1.9_sepal-width_count petal-width_2.0_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                 ?                                 ? \n" +
                "[2]                                 2                                 6 \n" +
                "\n" +
                "    petal-width_2.1_sepal-width_count petal-width_2.2_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                 ?                                 ? \n" +
                "[2]                                 1                                11 \n" +
                "\n" +
                "    petal-width_2.3_sepal-width_count petal-width_2.4_sepal-width_count \n" +
                "[0]                                 ?                                 ? \n" +
                "[1]                                 ?                                 ? \n" +
                "[2]                                 3                                 6 \n" +
                "\n" +
                "    petal-width_2.5_sepal-width_count \n" +
                "[0]                                 ? \n" +
                "[1]                                 ? \n" +
                "[2]                                 1 \n" +
                "\n", agg2.toFrame(1).content());
    }

    @Test
    public void testGroupFunctions() {

        Group group = Group.from(play, "class");
        assertEquals("    class  outlook_count \n" +
                "[0] noplay             9 \n" +
                "[1]   play             5 \n", group.aggregate(count("outlook")).toFrame().content());
        assertEquals("    class  outlook_count_N1 \n" +
                "[0] noplay    0.6428571     \n" +
                "[1]   play    0.3571429     \n", group.aggregate(count(1, "outlook")).toFrame().content());

        assertEquals("    class  temp_sum \n" +
                "[0] noplay   657    \n" +
                "[1]   play   373    \n" +
                "\n" +
                "    temp_sum_N1 temp_mean \n" +
                "[0]  0.6378641    73      \n" +
                "[1]  0.3621359    74.6    \n" +
                "\n" +
                "    temp_mean_N1 windy_nunique \n" +
                "[0]  0.4945799               2 \n" +
                "[1]  0.5054201               2 \n" +
                "\n" +
                "    windy_nunique_N1 temp_min \n" +
                "[0]       0.5           64    \n" +
                "[1]       0.5           65    \n" +
                "\n" +
                "    temp_min_N1 temp_max \n" +
                "[0]  0.496124      83    \n" +
                "[1]  0.503876      85    \n" +
                "\n" +
                "    temp_max_N1 temp_skewness \n" +
                "[0]  0.4940476    0.3294078   \n" +
                "[1]  0.5059524    0.1894857   \n" +
                "\n" +
                "    temp_skewness_N1 temp_std  \n" +
                "[0]    0.6348274     5.8118653 \n" +
                "[1]    0.3651726     7.059745  \n" +
                "\n" +
                "    temp_std_N1 temp_kurtosis \n" +
                "[0]  0.4515259   -0.8914041   \n" +
                "[1]  0.5484741   -1.2885832   \n" +
                "\n" +
                "    temp_kurtosis_N1 \n" +
                "[0]    0.4089033     \n" +
                "[1]    0.5910967     \n" +
                "\n", group.aggregate(
                sum("temp"), sum(1, "temp"), mean("temp"), mean(1, "temp"), nunique("windy"), nunique(1, "windy"),
                min("temp"), min(1, "temp"), max("temp"), max(1, "temp"),skewness("temp"), skewness(1, "temp"),
                std("temp"), std(1, "temp"), kurtosis("temp"), kurtosis(1, "temp")
                ).toFrame().content());
    }
}