package xyz.thinktest.fastestapi.core.enhance;

/**
 * @author: aruba
 * @date: 2022-01-30
 * 提供给用户自定义初始化的接口，会在系统初始化完成后执行用户自定义初始化内容
 */
public interface Initialize {
    void pre();
}
