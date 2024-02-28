package xyz.think.fastest.http.internal;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @Date: 2019/12/18
 */
public class Assert {
    public static final String ARRAY_MISMATCH_TEMPLATE = "arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s";

    protected Assert() {
    }

    protected static void assertTrue(boolean condition, String message) {
        if (!condition) {
            failNotEquals(condition, Boolean.TRUE, message);
        }

    }

    protected static void assertTrue(boolean condition) {
        assertTrue(condition, (String)null);
    }

    protected static void assertFalse(boolean condition, String message) {
        if (condition) {
            failNotEquals(condition, Boolean.FALSE, message);
        }

    }

    protected static void assertFalse(boolean condition) {
        assertFalse(condition, (String)null);
    }

    protected static void fail(String message, Throwable realCause) {
        AssertionError ae = new AssertionError(message);
        ae.initCause(realCause);
        throw ae;
    }

    protected static void fail(String message) {
        throw new AssertionError(message);
    }

    protected static void fail() {
        fail((String)null);
    }

    protected static void assertEquals(Object actual, Object expected, String message) {
        if (expected != null && expected.getClass().isArray()) {
            assertArrayEquals(actual, expected, message);
        } else {
            assertEqualsImpl(actual, expected, message);
        }
    }

    private static boolean areEqual(Object actual, Object expected) {
        return expected != null && expected.getClass().isArray() ? areArraysEqual(actual, expected) : areEqualImpl(actual, expected);
    }

    private static void assertEqualsImpl(Object actual, Object expected, String message) {
        boolean equal = areEqualImpl(actual, expected);
        if (!equal) {
            failNotEquals(actual, expected, message);
        }

    }

    private static void assertNotEqualsImpl(Object actual, Object expected, String message) {
        boolean equal = areEqualImpl(actual, expected);
        if (equal) {
            failEquals(actual, expected, message);
        }

    }

    private static boolean areEqualImpl(Object actual, Object expected) {
        if (expected == null && actual == null) {
            return true;
        } else if (expected == null ^ actual == null) {
            return false;
        } else {
            return expected.equals(actual) && actual.equals(expected);
        }
    }

    private static String getArrayNotEqualReason(Object actual, Object expected) {
        if (Objects.equals(actual, expected)) {
            return null;
        } else if (null == expected) {
            return "expected a null array, but not null found";
        } else if (null == actual) {
            return "expected not null array, but null found";
        } else if (!actual.getClass().isArray()) {
            return "not an array";
        } else {
            int expectedLength = Array.getLength(expected);
            if (expectedLength != Array.getLength(actual)) {
                return "array lengths are not the same";
            } else {
                for(int i = 0; i < expectedLength; ++i) {
                    Object _actual = Array.get(actual, i);
                    Object _expected = Array.get(expected, i);
                    if (!areEqual(_actual, _expected)) {
                        return "(values at index " + i + " are not the same)";
                    }
                }

                return null;
            }
        }
    }

    private static boolean areArraysEqual(Object actual, Object expected) {
        return getArrayNotEqualReason(actual, expected) == null;
    }

    private static void assertArrayEquals(Object actual, Object expected, String message) {
        String reason = getArrayNotEqualReason(actual, expected);
        if (null != reason) {
            failNotEquals(actual, expected, message == null ? "" : message + " (" + message + ")");
        }

    }

    private static void assertArrayNotEquals(Object actual, Object expected, String message) {
        String reason = getArrayNotEqualReason(actual, expected);
        if (null == reason) {
            failEquals(actual, expected, message);
        }

    }

    protected static void assertEquals(byte[] actual, byte[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(byte[] actual, byte[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                if (expected[i] != actual[i]) {
                    fail(String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Byte.toString(expected[i]), Byte.toString(actual[i]), message));
                }
            }

        }
    }

    protected static void assertEquals(short[] actual, short[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(short[] actual, short[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                if (expected[i] != actual[i]) {
                    fail(String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Short.toString(expected[i]), Short.toString(actual[i]), message));
                }
            }

        }
    }

    protected static void assertEquals(int[] actual, int[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(int[] actual, int[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                if (expected[i] != actual[i]) {
                    fail(String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Integer.toString(expected[i]), Integer.toString(actual[i]), message));
                }
            }

        }
    }

    protected static void assertEquals(boolean[] actual, boolean[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(boolean[] actual, boolean[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                if (expected[i] != actual[i]) {
                    fail(String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Boolean.toString(expected[i]), Boolean.toString(actual[i]), message));
                }
            }

        }
    }

    protected static void assertEquals(char[] actual, char[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(char[] actual, char[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                if (expected[i] != actual[i]) {
                    fail(String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Character.toString(expected[i]), Character.toString(actual[i]), message));
                }
            }

        }
    }

    protected static void assertEquals(float[] actual, float[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(float[] actual, float[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                assertEquals(actual[i], expected[i], String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Float.toString(expected[i]), Float.toString(actual[i]), message));
            }

        }
    }

    protected static void assertEquals(float[] actual, float[] expected, float delta) {
        assertEquals(actual, expected, delta, "");
    }

    protected static void assertEquals(float[] actual, float[] expected, float delta, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                assertEquals(actual[i], expected[i], delta, String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Float.toString(expected[i]), Float.toString(actual[i]), message));
            }

        }
    }

    protected static void assertEquals(double[] actual, double[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(double[] actual, double[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                assertEquals(actual[i], expected[i], String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Double.toString(expected[i]), Double.toString(actual[i]), message));
            }

        }
    }

    protected static void assertEquals(double[] actual, double[] expected, double delta) {
        assertEquals(actual, expected, delta, "");
    }

    protected static void assertEquals(double[] actual, double[] expected, double delta, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                assertEquals(actual[i], expected[i], delta, String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Double.toString(expected[i]), Double.toString(actual[i]), message));
            }

        }
    }

    protected static void assertEquals(long[] actual, long[] expected) {
        assertEquals(actual, expected, "");
    }

    protected static void assertEquals(long[] actual, long[] expected, String message) {
        if (!checkRefEqualityAndLength(actual, expected, message)) {
            for(int i = 0; i < expected.length; ++i) {
                if (expected[i] != actual[i]) {
                    fail(String.format("arrays differ firstly at element [%d]; expected value is <%s> but was <%s>. %s", i, Long.toString(expected[i]), Long.toString(actual[i]), message));
                }
            }

        }
    }

    private static boolean checkRefEqualityAndLength(Object actualArray, Object expectedArray, String message) {
        if (expectedArray == actualArray) {
            return true;
        } else {
            if (null == expectedArray) {
                fail("expectedArray a null array, but not null found. " + message);
            }

            if (null == actualArray) {
                fail("expectedArray not null array, but null found. " + message);
            }

            assertEquals(Array.getLength(actualArray), Array.getLength(expectedArray), "arrays don't have the same size. " + message);
            return false;
        }
    }

    protected static void assertEquals(Object actual, Object expected) {
        assertEquals((Object)actual, (Object)expected, (String)null);
    }

    protected static void assertEquals(String actual, String expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(String actual, String expected) {
        assertEquals((String)actual, (String)expected, (String)null);
    }

    private static boolean areEqual(double actual, double expected, double delta) {
        if (Double.isInfinite(expected)) {
            if (expected != actual) {
                return false;
            }
        } else if (Double.isNaN(expected)) {
            if (!Double.isNaN(actual)) {
                return false;
            }
        } else if (Math.abs(expected - actual) > delta) {
            return false;
        }

        return true;
    }

    protected static void assertEquals(double actual, double expected, double delta, String message) {
        if (!areEqual(actual, expected, delta)) {
            failNotEquals(actual, expected, message);
        }

    }

    protected static void assertEquals(double actual, double expected, double delta) {
        assertEquals(actual, expected, delta, (String)null);
    }

    protected static void assertEquals(double actual, double expected, String message) {
        if (Double.isNaN(expected)) {
            if (!Double.isNaN(actual)) {
                failNotEquals(actual, expected, message);
            }
        } else if (actual != expected) {
            failNotEquals(actual, expected, message);
        }

    }

    protected static void assertEquals(double actual, double expected) {
        assertEquals(actual, expected, (String)null);
    }

    private static boolean areEqual(float actual, float expected, float delta) {
        if (Float.isInfinite(expected)) {
            if (expected != actual) {
                return false;
            }
        } else if (Float.isNaN(expected)) {
            if (!Float.isNaN(actual)) {
                return false;
            }
        } else if (Math.abs(expected - actual) > delta) {
            return false;
        }

        return true;
    }

    protected static void assertEquals(float actual, float expected, float delta, String message) {
        if (!areEqual(actual, expected, delta)) {
            failNotEquals(actual, expected, message);
        }

    }

    protected static void assertEquals(float actual, float expected, float delta) {
        assertEquals(actual, expected, delta, (String)null);
    }

    protected static void assertEquals(float actual, float expected, String message) {
        if (Float.isNaN(expected)) {
            if (!Float.isNaN(actual)) {
                failNotEquals(actual, expected, message);
            }
        } else if (actual != expected) {
            failNotEquals(actual, expected, message);
        }

    }

    protected static void assertEquals(float actual, float expected) {
        assertEquals(actual, expected, (String)null);
    }

    protected static void assertEquals(long actual, long expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(long actual, long expected) {
        assertEquals(actual, expected, (String)null);
    }

    protected static void assertEquals(boolean actual, boolean expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(boolean actual, boolean expected) {
        assertEquals(actual, expected, (String)null);
    }

    protected static void assertEquals(byte actual, byte expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(byte actual, byte expected) {
        assertEquals((byte)actual, (byte)expected, (String)null);
    }

    protected static void assertEquals(char actual, char expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(char actual, char expected) {
        assertEquals((char)actual, (char)expected, (String)null);
    }

    protected static void assertEquals(short actual, short expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(short actual, short expected) {
        assertEquals((short)actual, (short)expected, (String)null);
    }

    protected static void assertEquals(int actual, int expected, String message) {
        assertEquals((Object)actual, (Object)expected, message);
    }

    protected static void assertEquals(int actual, int expected) {
        assertEquals((int)actual, (int)expected, (String)null);
    }

    protected static void assertNotNull(Object object) {
        assertNotNull(object, (String)null);
    }

    protected static void assertNotNull(Object object, String message) {
        if (object == null) {
            String formatted = "";
            if (message != null) {
                formatted = message + " ";
            }

            fail(formatted + "expected object to not be null");
        }

    }

    protected static void assertNull(Object object) {
        assertNull(object, (String)null);
    }

    protected static void assertNull(Object object, String message) {
        if (object != null) {
            failNotSame(object, (Object)null, message);
        }

    }

    protected static void assertSame(Object actual, Object expected, String message) {
        if (!Objects.equals(actual, expected)) {
            failNotSame(actual, expected, message);
        }
    }

    protected static void assertSame(Object actual, Object expected) {
        assertSame(actual, expected, (String)null);
    }

    protected static void assertNotSame(Object actual, Object expected, String message) {
        if (Objects.equals(actual, expected)) {
            failSame(actual, expected, message);
        }

    }

    protected static void assertNotSame(Object actual, Object expected) {
        assertNotSame(actual, expected, (String)null);
    }

    private static void failSame(Object actual, Object expected, String message) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }

        fail(formatted + EclipseInterface.ASSERT_LEFT2 + expected + EclipseInterface.ASSERT_MIDDLE + actual + EclipseInterface.ASSERT_RIGHT);
    }

    private static void failNotSame(Object actual, Object expected, String message) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }

        fail(formatted + EclipseInterface.ASSERT_EQUAL_LEFT + expected + EclipseInterface.ASSERT_MIDDLE + actual + EclipseInterface.ASSERT_RIGHT);
    }

    private static void failNotEquals(Object actual, Object expected, String message) {
        fail(format(actual, expected, message, true));
    }

    private static void failEquals(Object actual, Object expected, String message) {
        fail(format(actual, expected, message, false));
    }

    static String format(Object actual, Object expected, String message, boolean isAssertEquals) {
        String formatted = "";
        if (null != message) {
            formatted = message + " ";
        }

        return isAssertEquals ? formatted + EclipseInterface.ASSERT_EQUAL_LEFT + expected + EclipseInterface.ASSERT_MIDDLE + actual + EclipseInterface.ASSERT_RIGHT : formatted + EclipseInterface.ASSERT_UNEQUAL_LEFT + expected + EclipseInterface.ASSERT_MIDDLE + actual + EclipseInterface.ASSERT_RIGHT;
    }

    protected static void assertEquals(Collection<?> actual, Collection<?> expected) {
        assertEquals((Collection)actual, (Collection)expected, (String)null);
    }

    protected static void assertEquals(Collection<?> actual, Collection<?> expected, String message) {
        if (!Objects.equals(actual, expected)) {
            if (actual == null || expected == null) {
                if (message != null) {
                    fail(message);
                } else {
                    fail("Collections not equal: expected: " + expected + " and actual: " + actual);
                }
            }

            assertEquals(actual.size(), expected.size(), (message == null ? "" : message + ": ") + "lists don't have the same size");
            Iterator<?> actIt = actual.iterator();
            Iterator<?> expIt = expected.iterator();
            int i = -1;

            while(actIt.hasNext() && expIt.hasNext()) {
                ++i;
                Object e = expIt.next();
                Object a = actIt.next();
                String explanation = "Lists differ at element [" + i + "]: " + e + " != " + a;
                String errorMessage = message == null ? explanation : message + ": " + explanation;
                assertEqualsImpl(a, e, errorMessage);
            }

        }
    }

    protected static void assertEquals(Iterator<?> actual, Iterator<?> expected) {
        assertEquals((Iterator)actual, (Iterator)expected, (String)null);
    }

    protected static void assertEquals(Iterator<?> actual, Iterator<?> expected, String message) {
        if (!Objects.equals(actual, expected)) {
            if (actual == null || expected == null) {
                String msg = message != null ? message : "Iterators not equal: expected: " + expected + " and actual: " + actual;
                fail(msg);
            }

            int i = -1;

            while(actual.hasNext() && expected.hasNext()) {
                ++i;
                Object e = expected.next();
                Object a = actual.next();
                String explanation = "Iterators differ at element [" + i + "]: " + e + " != " + a;
                String errorMessage = message == null ? explanation : message + ": " + explanation;
                assertEqualsImpl(a, e, errorMessage);
            }

            String explanation;
            String errorMessage;
            if (actual.hasNext()) {
                explanation = "Actual iterator returned more elements than the expected iterator.";
                errorMessage = message == null ? explanation : message + ": " + explanation;
                fail(errorMessage);
            } else if (expected.hasNext()) {
                explanation = "Expected iterator returned more elements than the actual iterator.";
                errorMessage = message == null ? explanation : message + ": " + explanation;
                fail(errorMessage);
            }

        }
    }

    protected static void assertEquals(Iterable<?> actual, Iterable<?> expected) {
        assertEquals((Iterable)actual, (Iterable)expected, (String)null);
    }

    protected static void assertEquals(Iterable<?> actual, Iterable<?> expected, String message) {
        if (!Objects.equals(actual, expected)) {
            if (actual == null || expected == null) {
                if (message != null) {
                    fail(message);
                } else {
                    fail("Iterables not equal: expected: " + expected + " and actual: " + actual);
                }
            }

            Iterator<?> actIt = actual.iterator();
            Iterator<?> expIt = expected.iterator();
            assertEquals(actIt, expIt, message);
        }
    }

    protected static void assertEquals(Object[] actual, Object[] expected, String message) {
        if (!Arrays.equals(actual, expected)) {
            if (actual == null && expected != null || actual != null && expected == null) {
                if (message != null) {
                    fail(message);
                } else {
                    fail("Arrays not equal: " + Arrays.toString(expected) + " and " + Arrays.toString(actual));
                }
            }

            if (actual.length != expected.length) {
                failAssertNoEqual("Arrays do not have the same size:" + actual.length + " != " + expected.length, message);
            }

            for(int i = 0; i < expected.length; ++i) {
                Object e = expected[i];
                Object a = actual[i];
                String explanation = "Arrays differ at element [" + i + "]: " + e + " != " + a;
                String errorMessage = message == null ? explanation : message + ": " + explanation;
                if (a != null || e != null) {
                    if (a == null && e != null || a != null && e == null) {
                        failNotEquals(a, e, message);
                    }

                    if (e.getClass().isArray()) {
                        assertEquals(a, e, errorMessage);
                    } else {
                        assertEqualsImpl(a, e, errorMessage);
                    }
                }
            }

        }
    }

    protected static void assertEqualsNoOrder(Object[] actual, Object[] expected, String message) {
        if (!Arrays.equals(actual, expected)) {
            if (actual == null && expected != null || actual != null && expected == null) {
                failAssertNoEqual("Arrays not equal: " + Arrays.toString(expected) + " and " + Arrays.toString(actual), message);
            }

            if (actual.length != expected.length) {
                failAssertNoEqual("Arrays do not have the same size:" + actual.length + " != " + expected.length, message);
            }

            List<Object> actualCollection = Lists.newArrayList();
            actualCollection.addAll(Arrays.asList(actual));
            Object[] var4 = expected;
            int var5 = expected.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Object o = var4[var6];
                actualCollection.remove(o);
            }

            if (actualCollection.size() != 0) {
                failAssertNoEqual("Arrays not equal: " + Arrays.toString(expected) + " and " + Arrays.toString(actual), message);
            }

        }
    }

    private static void failAssertNoEqual(String defaultMessage, String message) {
        if (message != null) {
            fail(message);
        } else {
            fail(defaultMessage);
        }

    }

    protected static void assertEquals(Object[] actual, Object[] expected) {
        assertEquals((Object[])actual, (Object[])expected, (String)null);
    }

    protected static void assertEqualsNoOrder(Object[] actual, Object[] expected) {
        assertEqualsNoOrder(actual, expected, (String)null);
    }

    protected static void assertEquals(Set<?> actual, Set<?> expected) {
        assertEquals((Set)actual, (Set)expected, (String)null);
    }

    private static String getNotEqualReason(Set<?> actual, Set<?> expected) {
        if (Objects.equals(actual, expected)) {
            return null;
        } else if (actual != null && expected != null) {
            return !Objects.equals(actual, expected) ? "Sets differ: expected " + expected + " but got " + actual : null;
        } else {
            return "Sets not equal: expected: " + expected + " and actual: " + actual;
        }
    }

    protected static void assertEquals(Set<?> actual, Set<?> expected, String message) {
        String notEqualReason = getNotEqualReason(actual, expected);
        if (null != notEqualReason) {
            if (message == null) {
                fail(notEqualReason);
            } else {
                fail(message);
            }
        }

    }

    private static String getNotEqualDeepReason(Set<?> actual, Set<?> expected) {
        if (Objects.equals(actual, expected)) {
            return null;
        } else if (actual != null && expected != null) {
            if (expected.size() != actual.size()) {
                return "Sets not equal: expected: " + expected + " and actual: " + actual;
            } else {
                Iterator<?> actualIterator = actual.iterator();
                Iterator expectedIterator = expected.iterator();

                while(expectedIterator.hasNext()) {
                    Object expectedValue = expectedIterator.next();
                    Object value = actualIterator.next();
                    if (expectedValue.getClass().isArray()) {
                        String arrayNotEqualReason = getArrayNotEqualReason(value, expectedValue);
                        if (arrayNotEqualReason != null) {
                            return arrayNotEqualReason;
                        }
                    } else if (!areEqualImpl(value, expected)) {
                        return "Sets not equal: expected: " + expectedValue + " and actual: " + value;
                    }
                }

                return null;
            }
        } else {
            return "Sets not equal: expected: " + expected + " and actual: " + actual;
        }
    }

    protected static void assertEqualsDeep(Set<?> actual, Set<?> expected, String message) {
        String notEqualDeepReason = getNotEqualDeepReason(actual, expected);
        if (notEqualDeepReason != null) {
            if (message == null) {
                fail(notEqualDeepReason);
            } else {
                fail(message);
            }
        }

    }

    protected static void assertEquals(Map<?, ?> actual, Map<?, ?> expected) {
        assertEquals((Map)actual, (Map)expected, (String)null);
    }

    private static String getNotEqualReason(Map<?, ?> actual, Map<?, ?> expected) {
        if (Objects.equals(actual, expected)) {
            return null;
        } else if (actual != null && expected != null) {
            if (actual.size() != expected.size()) {
                return "Maps do not have the same size:" + actual.size() + " != " + expected.size();
            } else {
                Set<?> entrySet = actual.entrySet();
                Iterator var3 = entrySet.iterator();

                Object value;
                Object expectedValue;
                String assertMessage;
                do {
                    if (!var3.hasNext()) {
                        return null;
                    }

                    Object anEntrySet = var3.next();
                    Map.Entry<?, ?> entry = (Map.Entry)anEntrySet;
                    Object key = entry.getKey();
                    value = entry.getValue();
                    expectedValue = expected.get(key);
                    assertMessage = "Maps do not match for key:" + key + " actual:" + value + " expected:" + expectedValue;
                } while(areEqualImpl(value, expectedValue));

                return assertMessage;
            }
        } else {
            return "Maps not equal: expected: " + expected + " and actual: " + actual;
        }
    }

    protected static void assertEquals(Map<?, ?> actual, Map<?, ?> expected, String message) {
        String notEqualReason = getNotEqualReason(actual, expected);
        if (notEqualReason != null) {
            if (message == null) {
                fail(notEqualReason);
            } else {
                fail(message);
            }
        }

    }

    protected static void assertEqualsDeep(Map<?, ?> actual, Map<?, ?> expected) {
        assertEqualsDeep((Map)actual, (Map)expected, (String)null);
    }

    private static String getNotEqualDeepReason(Map<?, ?> actual, Map<?, ?> expected) {
        if (Objects.equals(actual, expected)) {
            return null;
        } else if (actual != null && expected != null) {
            if (actual.size() != expected.size()) {
                return "Maps do not have the same size:" + actual.size() + " != " + expected.size();
            } else {
                Set<?> entrySet = actual.entrySet();
                Iterator var3 = entrySet.iterator();

                while(var3.hasNext()) {
                    Object anEntrySet = var3.next();
                    Map.Entry<?, ?> entry = (Map.Entry)anEntrySet;
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    Object expectedValue = expected.get(key);
                    String assertMessage = "Maps do not match for key:" + key + " actual:" + value + " expected:" + expectedValue;
                    if (expectedValue.getClass().isArray()) {
                        if (!areArraysEqual(value, expectedValue)) {
                            return assertMessage;
                        }
                    } else if (!areEqualImpl(value, expectedValue)) {
                        return assertMessage;
                    }
                }

                return null;
            }
        } else {
            return "Maps not equal: expected: " + expected + " and actual: " + actual;
        }
    }

    protected static void assertEqualsDeep(Map<?, ?> actual, Map<?, ?> expected, String message) {
        String notEqualDeepReason = getNotEqualDeepReason(actual, expected);
        if (notEqualDeepReason != null) {
            if (message == null) {
                fail(notEqualDeepReason);
            } else {
                fail(message);
            }
        }

    }

    protected static void assertNotEquals(Object actual, Object expected, String message) {
        if (expected != null && expected.getClass().isArray()) {
            assertArrayNotEquals(actual, expected, message);
        } else {
            assertNotEqualsImpl(actual, expected, message);
        }
    }

    protected static void assertNotEquals(Object actual1, Object actual2) {
        assertNotEquals((Object)actual1, (Object)actual2, (String)null);
    }

    static void assertNotEquals(String actual1, String actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(String actual1, String actual2) {
        assertNotEquals((String)actual1, (String)actual2, (String)null);
    }

    static void assertNotEquals(long actual1, long actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(long actual1, long actual2) {
        assertNotEquals(actual1, actual2, (String)null);
    }

    static void assertNotEquals(boolean actual1, boolean actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(boolean actual1, boolean actual2) {
        assertNotEquals(actual1, actual2, (String)null);
    }

    static void assertNotEquals(byte actual1, byte actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(byte actual1, byte actual2) {
        assertNotEquals((byte)actual1, (byte)actual2, (String)null);
    }

    static void assertNotEquals(char actual1, char actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(char actual1, char actual2) {
        assertNotEquals((char)actual1, (char)actual2, (String)null);
    }

    static void assertNotEquals(short actual1, short actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(short actual1, short actual2) {
        assertNotEquals((short)actual1, (short)actual2, (String)null);
    }

    static void assertNotEquals(int actual1, int actual2, String message) {
        assertNotEquals((Object)actual1, (Object)actual2, message);
    }

    static void assertNotEquals(int actual1, int actual2) {
        assertNotEquals((int)actual1, (int)actual2, (String)null);
    }

    protected static void assertNotEquals(float actual, float expected, float delta, String message) {
        if (areEqual(actual, expected)) {
            fail(format(actual, expected, message, false));
        }

    }

    protected static void assertNotEquals(float actual1, float actual2, float delta) {
        assertNotEquals(actual1, actual2, delta, (String)null);
    }

    protected static void assertNotEquals(double actual, double expected, double delta, String message) {
        if (areEqual(actual, expected, delta)) {
            fail(format(actual, expected, message, false));
        }

    }

    protected static void assertNotEquals(Set<?> actual, Set<?> expected) {
        assertNotEquals((Set)actual, (Set)expected, (String)null);
    }

    protected static void assertNotEquals(Set<?> actual, Set<?> expected, String message) {
        String notEqualReason = getNotEqualReason(actual, expected);
        if (notEqualReason == null) {
            fail(format(actual, expected, message, false));
        }

    }

    protected static void assertNotEqualsDeep(Set<?> actual, Set<?> expected) {
        assertNotEqualsDeep((Set)actual, (Set)expected, (String)null);
    }

    protected static void assertNotEqualsDeep(Set<?> actual, Set<?> expected, String message) {
        String notEqualDeepReason = getNotEqualDeepReason(actual, expected);
        if (notEqualDeepReason == null) {
            fail(format(actual, expected, message, false));
        }

    }

    protected static void assertNotEquals(Map<?, ?> actual, Map<?, ?> expected) {
        assertNotEquals((Map)actual, (Map)expected, (String)null);
    }

    protected static void assertNotEquals(Map<?, ?> actual, Map<?, ?> expected, String message) {
        String notEqualReason = getNotEqualReason(actual, expected);
        if (notEqualReason == null) {
            fail(format(actual, expected, message, false));
        }

    }

    protected static void assertNotEqualsDeep(Map<?, ?> actual, Map<?, ?> expected) {
        assertNotEqualsDeep((Map)actual, (Map)expected, (String)null);
    }

    protected static void assertNotEqualsDeep(Map<?, ?> actual, Map<?, ?> expected, String message) {
        String notEqualDeepReason = getNotEqualDeepReason(actual, expected);
        if (notEqualDeepReason == null) {
            fail(format(actual, expected, message, false));
        }

    }

    protected static void assertNotEquals(double actual1, double actual2, double delta) {
        assertNotEquals(actual1, actual2, delta, (String)null);
    }

    protected static void assertThrows(Assert.ThrowingRunnable runnable) {
        assertThrows(Throwable.class, runnable);
    }

    public static <T extends Throwable> void assertThrows(Class<T> throwableClass, Assert.ThrowingRunnable runnable) {
        expectThrows(throwableClass, runnable);
    }

    public static <T extends Throwable> T expectThrows(Class<T> throwableClass, Assert.ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable var4) {
            if (throwableClass.isInstance(var4)) {
                return throwableClass.cast(var4);
            }

            String mismatchMessage = String.format("Expected %s to be thrown, but %s was thrown", throwableClass.getSimpleName(), var4.getClass().getSimpleName());
            throw new AssertionError(mismatchMessage, var4);
        }

        String message = String.format("Expected %s to be thrown, but nothing was thrown", throwableClass.getSimpleName());
        throw new AssertionError(message);
    }

    public interface ThrowingRunnable {
        void run() throws Throwable;
    }
}
