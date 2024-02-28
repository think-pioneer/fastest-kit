package xyz.think.fastest.common.json.jsonpath;

import com.jayway.jsonpath.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Date: 2020/10/21
 */
public abstract class Selector implements Predicate {
    /**
     * Creates a new Filter based on given criteria
     * @param predicate criteria
     * @return a new Filter
     */
    public static Selector selector(Predicate predicate) {
        return new SingleFilter(predicate);
    }

    /**
     * Create a new Filter based on given list of criteria.
     * @param predicates list of criteria all needs to evaluate to true
     * @return the filter
     */
    public static Selector selector(Collection<Predicate> predicates) {
        return new AndFilter(predicates);
    }

    @Override
    public abstract boolean apply(PredicateContext ctx);


    public Selector or(final Predicate other){
        return new OrFilter(this, other);
    }

    public Selector and(final Predicate other){
        return new AndFilter(this, other);
    }

    /**
     * Parses a selector. The selector must match <code>[?(<selector>)]</code>, white spaces are ignored.
     * @param selector selector string to parse
     * @return the selector
     */
    public static Selector parse(String selector){
        return SelectorCompiler.compile(selector);
    }

    private static final class SingleFilter extends Selector {

        private final Predicate predicate;

        private SingleFilter(Predicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean apply(PredicateContext ctx) {
            return predicate.apply(ctx);
        }

        @Override
        public String toString() {
            String predicateString = predicate.toString();
            if(predicateString.startsWith("(")){
                return "[?" + predicateString + "]";
            } else {
                return "[?(" + predicateString + ")]";
            }
        }
    }

    private static final class AndFilter extends Selector {

        private final Collection<Predicate> predicates;

        private AndFilter(Collection<Predicate> predicates) {
            this.predicates = predicates;
        }

        private AndFilter(Predicate left, Predicate right) {
            this(Arrays.asList(left, right));
        }

        public Selector and(final Predicate other){
            Collection<Predicate> newPredicates = new ArrayList<Predicate>(predicates);
            newPredicates.add(other);
            return new AndFilter(newPredicates);
        }

        @Override
        public boolean apply(PredicateContext ctx) {
            for (Predicate predicate : predicates) {
                if(!predicate.apply(ctx)){
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            Iterator<Predicate> i = predicates.iterator();
            StringBuilder sb = new StringBuilder();
            sb.append("[?(");
            while (i.hasNext()){
                String p = i.next().toString();

                if(p.startsWith("[?(")){
                    p = p.substring(3, p.length() - 2);
                }
                sb.append(p);

                if(i.hasNext()){
                    sb.append(" && ");
                }
            }
            sb.append(")]");
            return sb.toString();
        }
    }

    private static final class OrFilter extends Selector {

        private final Predicate left;
        private final Predicate right;

        private OrFilter(Predicate left, Predicate right) {
            this.left = left;
            this.right = right;
        }

        public Selector and(final Predicate other){
            return new OrFilter(left, new AndFilter(right, other));
        }

        @Override
        public boolean apply(PredicateContext ctx) {
            boolean a = left.apply(ctx);
            return a || right.apply(ctx);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[?(");

            String l = left.toString();
            String r = right.toString();

            if(l.startsWith("[?(")){
                l = l.substring(3, l.length() - 2);
            }
            if(r.startsWith("[?(")){
                r = r.substring(3, r.length() - 2);
            }

            sb.append(l).append(" || ").append(r);

            sb.append(")]");
            return sb.toString();
        }
    }
}
