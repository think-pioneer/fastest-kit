package xyz.think.fastest.common.yaml;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.SequenceNode;

/**
 * yaml list解析器
 * @Date: 2021/12/25
 */
public class ListConstructor<T> extends Constructor {
    private final Class<T> clazz;

    public ListConstructor(final Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected Object constructObject(final Node node) {
        if (node instanceof SequenceNode && isRootNode(node)) {
            ((SequenceNode) node).setListType(clazz);
        }
        return super.constructObject(node);
    }

    private boolean isRootNode(final Node node) {
        return node.getStartMark().getIndex() == 0;
    }
}
