package xyz.thinktest.fastest.http.internal;

/**
 * @Date: 2021/12/18
 */
class EclipseInterface {
    public static final Character OPENING_CHARACTER = '[';
    public static final Character CLOSING_CHARACTER = ']';
    public static final String ASSERT_EQUAL_LEFT;
    public static final String ASSERT_UNEQUAL_LEFT;
    public static final String ASSERT_LEFT2;
    public static final String ASSERT_MIDDLE;
    public static final String ASSERT_RIGHT;

    public EclipseInterface() {
    }

    static {
        ASSERT_EQUAL_LEFT = "expected " + OPENING_CHARACTER;
        ASSERT_UNEQUAL_LEFT = "did not expect " + OPENING_CHARACTER;
        ASSERT_LEFT2 = "expected not same " + OPENING_CHARACTER;
        ASSERT_MIDDLE = CLOSING_CHARACTER + " but found " + OPENING_CHARACTER;
        ASSERT_RIGHT = Character.toString(CLOSING_CHARACTER);
    }
}
