import duke.Deadline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {

    @Test
    public void testStringConversion() {
        assertEquals("[D][✘] must eat the fruit (by: Oct 10 2020)",
                new Deadline("must eat the fruit ", "2020-10-10").toString());
    }
}
