import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserTest {
    @Test
    public void dummyTest() {
        User user = new User();
        assertNotNull(user.getEmail());
    }

    @Test
    public void fail() {
        assertEquals(true, false);
    }
}
