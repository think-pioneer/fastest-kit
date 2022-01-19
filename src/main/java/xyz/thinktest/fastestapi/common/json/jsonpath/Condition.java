package xyz.thinktest.fastestapi.common.json.jsonpath;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.internal.Path;
import com.jayway.jsonpath.internal.Utils;
import com.jayway.jsonpath.internal.filter.RelationalExpressionNode;
import com.jayway.jsonpath.internal.filter.RelationalOperator;
import com.jayway.jsonpath.internal.filter.ValueNode;
import com.jayway.jsonpath.internal.filter.ValueNodes;

import java.util.*;
import java.util.regex.Pattern;

import static com.jayway.jsonpath.internal.Utils.notNull;
import static com.jayway.jsonpath.internal.filter.ValueNodes.PredicateNode;
import static com.jayway.jsonpath.internal.filter.ValueNodes.ValueListNode;

/**
 * @Date: 2020/10/21
 */
public class Condition implements Predicate {
    private final List<Condition> conditionChain;
    private ValueNode left;
    private RelationalOperator criteriaType;
    private ValueNode right;

    private Condition(List<Condition> conditionChain, ValueNode left) {
        this.left = left;
        this.conditionChain = conditionChain;
        this.conditionChain.add(this);
    }

    private Condition(ValueNode left) {
        this(new LinkedList<Condition>(), left);
    }

    @Override
    public boolean apply(PredicateContext ctx) {
        for (RelationalExpressionNode expressionNode : toRelationalExpressionNodes()) {
            if(!expressionNode.apply(ctx)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return Utils.join(" && ", toRelationalExpressionNodes());
    }

    private Collection<RelationalExpressionNode> toRelationalExpressionNodes(){
        List<RelationalExpressionNode> nodes = new ArrayList<RelationalExpressionNode>(conditionChain.size());
        for (Condition condition : conditionChain) {
            nodes.add(new RelationalExpressionNode(condition.left, condition.criteriaType, condition.right));
        }
        return nodes;
    }

    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @param key filed name
     * @return the new criteria
     */
    @Deprecated
    //This should be private.It exposes internal classes
    public static Condition where(Path key) {
        return new Condition(ValueNode.createPathNode(key));
    }


    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @param key filed name
     * @return the new criteria
     */

    public static Condition where(String key) {
        return new Condition(ValueNode.toValueNode(prefixPath(key)));
    }

    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @param key ads new filed to criteria
     * @return the criteria builder
     */
    public Condition and(String key) {
        checkComplete();
        return new Condition(this.conditionChain, ValueNode.toValueNode(prefixPath(key)));
    }

    /**
     * Creates a criterion using equality
     *
     * @param o
     * @return the criteria
     */
    public Condition is(Object o) {
        this.criteriaType = RelationalOperator.EQ;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * Creates a criterion using equality
     *
     * @param o
     * @return the criteria
     */
    public Condition eq(Object o) {
        return is(o);
    }

    /**
     * Creates a criterion using the <b>!=</b> operator
     *
     * @param o
     * @return the criteria
     */
    public Condition ne(Object o) {
        this.criteriaType = RelationalOperator.NE;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * Creates a criterion using the <b>&lt;</b> operator
     *
     * @param o
     * @return the criteria
     */
    public Condition lt(Object o) {
        this.criteriaType = RelationalOperator.LT;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * Creates a criterion using the <b>&lt;=</b> operator
     *
     * @param o
     * @return the criteria
     */
    public Condition lte(Object o) {
        this.criteriaType = RelationalOperator.LTE;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * Creates a criterion using the <b>&gt;</b> operator
     *
     * @param o
     * @return the criteria
     */
    public Condition gt(Object o) {
        this.criteriaType = RelationalOperator.GT;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * Creates a criterion using the <b>&gt;=</b> operator
     *
     * @param o
     * @return the criteria
     */
    public Condition gte(Object o) {
        this.criteriaType = RelationalOperator.GTE;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * Creates a criterion using a Regex
     *
     * @param pattern
     * @return the criteria
     */
    public Condition regex(Pattern pattern) {
        notNull(pattern, "pattern can not be null");
        this.criteriaType = RelationalOperator.REGEX;
        this.right = ValueNode.toValueNode(pattern);
        return this;
    }

    /**
     * The <code>in</code> operator is analogous to the SQL IN modifier, allowing you
     * to specify an array of possible matches.
     *
     * @param o the values to match against
     * @return the criteria
     */
    public Condition in(Object... o) {
        return in(Arrays.asList(o));
    }

    /**
     * The <code>in</code> operator is analogous to the SQL IN modifier, allowing you
     * to specify an array of possible matches.
     *
     * @param c the collection containing the values to match against
     * @return the criteria
     */
    public Condition in(Collection<?> c) {
        notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.IN;
        this.right = new ValueListNode(c);
        return this;
    }

    /**
     * The <code>contains</code> operator asserts that the provided object is contained
     * in the result. The object that should contain the input can be either an object or a String.
     *
     * @param o that should exists in given collection or
     * @return the criteria
     */
    public Condition contains(Object o) {
        this.criteriaType = RelationalOperator.CONTAINS;
        this.right = ValueNode.toValueNode(o);
        return this;
    }

    /**
     * The <code>nin</code> operator is similar to $in except that it selects objects for
     * which the specified field does not have any value in the specified array.
     *
     * @param o the values to match against
     * @return the criteria
     */
    public Condition nin(Object... o) {
        return nin(Arrays.asList(o));
    }

    /**
     * The <code>nin</code> operator is similar to $in except that it selects objects for
     * which the specified field does not have any value in the specified array.
     *
     * @param c the values to match against
     * @return the criteria
     */
    public Condition nin(Collection<?> c) {
        notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.NIN;
        this.right = new ValueNodes.ValueListNode(c);
        return this;
    }

    /**
     * The <code>subsetof</code> operator selects objects for which the specified field is
     * an array whose elements comprise a subset of the set comprised by the elements of
     * the specified array.
     *
     * @param o the values to match against
     * @return the criteria
     */
    public Condition subsetof(Object... o) {
        return subsetof(Arrays.asList(o));
    }

    /**
     * The <code>subsetof</code> operator selects objects for which the specified field is
     * an array whose elements comprise a subset of the set comprised by the elements of
     * the specified array.
     *
     * @param c the values to match against
     * @return the criteria
     */
    public Condition subsetof(Collection<?> c) {
        notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.SUBSETOF;
        this.right = new ValueListNode(c);
        return this;
    }

    /**
     * The <code>anyof</code> operator selects objects for which the specified field is
     * an array that contain at least an element in the specified array.
     *
     * @param o the values to match against
     * @return the criteria
     */
    public Condition anyof(Object... o) {
        return subsetof(Arrays.asList(o));
    }

    /**
     * The <code>anyof</code> operator selects objects for which the specified field is
     * an array that contain at least an element in the specified array.
     *
     * @param c the values to match against
     * @return the criteria
     */
    public Condition anyof(Collection<?> c) {
        notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.ANYOF;
        this.right = new ValueListNode(c);
        return this;
    }

    /**
     * The <code>noneof</code> operator selects objects for which the specified field is
     * an array that does not contain any of the elements of the specified array.
     *
     * @param o the values to match against
     * @return the criteria
     */
    public Condition noneof(Object... o) {
        return noneof(Arrays.asList(o));
    }

    /**
     * The <code>noneof</code> operator selects objects for which the specified field is
     * an array that does not contain any of the elements of the specified array.
     *
     * @param c the values to match against
     * @return the criteria
     */
    public Condition noneof(Collection<?> c) {
        notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.NONEOF;
        this.right = new ValueListNode(c);
        return this;
    }

    /**
     * The <code>all</code> operator is similar to $in, but instead of matching any value
     * in the specified array all values in the array must be matched.
     *
     * @param o
     * @return the criteria
     */
    public Condition all(Object... o) {
        return all(Arrays.asList(o));
    }

    /**
     * The <code>all</code> operator is similar to $in, but instead of matching any value
     * in the specified array all values in the array must be matched.
     *
     * @param c
     * @return the criteria
     */
    public Condition all(Collection<?> c) {
        notNull(c, "collection can not be null");
        this.criteriaType = RelationalOperator.ALL;
        this.right = new ValueListNode(c);
        return this;
    }

    /**
     * The <code>size</code> operator matches:
     * <p/>
     * <ol>
     * <li>array with the specified number of elements.</li>
     * <li>string with given length.</li>
     * </ol>
     *
     * @param size
     * @return the criteria
     */
    public Condition size(int size) {
        this.criteriaType = RelationalOperator.SIZE;
        this.right = ValueNode.toValueNode(size);
        return this;
    }

    /**
     * The $type operator matches values based on their Java JSON type.
     *
     * Supported types are:
     *
     *  List.class
     *  Map.class
     *  String.class
     *  Number.class
     *  Boolean.class
     *
     * Other types evaluates to false
     *
     * @param clazz
     * @return the criteria
     */
    public Condition type(Class<?> clazz) {
        this.criteriaType = RelationalOperator.TYPE;
        this.right = ValueNode.createClassNode(clazz);
        return this;
    }

    /**
     * Check for existence (or lack thereof) of a field.
     *
     * @param shouldExist
     * @return the criteria
     */
    public Condition exists(boolean shouldExist) {
        this.criteriaType = RelationalOperator.EXISTS;
        this.right = ValueNode.toValueNode(shouldExist);
        this.left = left.asPathNode().asExistsCheck(shouldExist);
        return this;
    }

    /**
     * The <code>notEmpty</code> operator checks that an array or String is not empty.
     *
     * @return the criteria
     */
    @Deprecated
    public Condition notEmpty() {
        return empty(false);
    }

    /**
     * The <code>notEmpty</code> operator checks that an array or String is empty.
     *
     * @param empty should be empty
     * @return the criteria
     */
    public Condition empty(boolean empty) {
        this.criteriaType = RelationalOperator.EMPTY;
        this.right = empty ? ValueNodes.TRUE : ValueNodes.FALSE;
        return this;
    }

    /**
     * The <code>matches</code> operator checks that an object matches the given predicate.
     *
     * @param p
     * @return the criteria
     */
    public Condition matches(Predicate p) {
        this.criteriaType = RelationalOperator.MATCHES;
        this.right = new PredicateNode(p);
        return this;
    }

    /**
     * Parse the provided criteria
     *
     * Deprecated use {@link Selector#parse(String)}
     *
     * @param criteria
     * @return a criteria
     */
    @Deprecated
    public static Condition parse(String criteria) {
        if(criteria == null){
            throw new InvalidPathException("Criteria can not be null");
        }
        String[] split = criteria.trim().split(" ");
        if(split.length == 3){
            return create(split[0], split[1], split[2]);
        } else if(split.length == 1){
            return create(split[0], "EXISTS", "true");
        } else {
            throw new InvalidPathException("Could not parse criteria");
        }
    }

    /**
     * Creates a new criteria
     * @param left path to evaluate in criteria
     * @param operator operator
     * @param right expected value
     * @return a new Criteria
     */
    @Deprecated
    public static Condition create(String left, String operator, String right) {
        Condition condition = new Condition(ValueNode.toValueNode(left));
        condition.criteriaType = RelationalOperator.fromString(operator);
        condition.right = ValueNode.toValueNode(right);
        return condition;
    }


    private static String prefixPath(String key){
        if (!key.startsWith("$") && !key.startsWith("@")) {
            key = "@." + key;
        }
        return key;
    }

    private void checkComplete(){
        boolean complete = (left != null && criteriaType != null && right != null);
        if(!complete){
            throw new JsonPathException("Criteria build exception. Complete on criteria before defining next.");
        }
    }

}
