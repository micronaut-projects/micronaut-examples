package example.pets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FriendlyUrlTest {

    @Test
    public void testSanitizeWithDashes() {
        expect:
        assertEquals("harry-potter", new FriendlyUrl().sanitizeWithDashes("Harry Potter"));
        assertEquals("harry", new FriendlyUrl().sanitizeWithDashes("Harry "));
    }

}
