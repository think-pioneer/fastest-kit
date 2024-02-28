package xyz.think.fastest.core.internal.scanner;

import javassist.bytecode.ClassFile;
import org.reflections.Configuration;
import org.reflections.ReflectionUtils;
import org.reflections.ReflectionsException;
import org.reflections.Store;
import org.reflections.scanners.MemberUsageScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;
import org.reflections.util.*;
import org.reflections.vfs.Vfs;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.reflections.scanners.Scanners.*;
import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;
import static org.reflections.util.ReflectionUtilsPredicates.withAnyParameterAnnotation;

/**
 * @author: aruba
 * @date: 2022-01-30
 */
public class Reflections extends org.reflections.Reflections implements NameHelper {
    protected final transient Configuration configuration;
    protected final Store store;

    public Reflections(Configuration configuration) {
        this.configuration = configuration;
        Map<String, Map<String, Set<String>>> storeMap = scan();
        if (configuration.shouldExpandSuperTypes()) {
            expandSuperTypes(storeMap.get(SubTypes.index()), storeMap.get(TypesAnnotated.index()));
        }
        store = new Store(storeMap);
    }

    public Reflections(Store store) {
        this.configuration = new ConfigurationBuilder();
        this.store = store;
    }

    public Reflections(String prefix, Scanner... scanners) {
        this((Object) prefix, scanners);
    }

    public Reflections(Object... params) {
        this(ConfigurationBuilder.build(params));
    }

    protected Reflections() {
        configuration = new ConfigurationBuilder();
        store = new Store(new HashMap<>());
    }

    protected Map<String, Map<String, Set<String>>> scan() {
        long start = System.currentTimeMillis();
        Map<String, Set<Map.Entry<String, String>>> collect = configuration.getScanners().stream().map(Scanner::index).distinct()
                .collect(Collectors.toMap(s -> s, s -> Collections.synchronizedSet(new HashSet<>())));
        Set<URL> urls = configuration.getUrls();

        (configuration.isParallel() ? urls.stream().parallel() : urls.stream()).forEach(url -> {
            Vfs.Dir dir = null;
            try {
                dir = Vfs.fromURL(url);
                for (Vfs.File file : dir.getFiles()) {
                    if (doFilter(file, configuration.getInputsFilter())) {
                        ClassFile classFile = null;
                        for (Scanner scanner : configuration.getScanners()) {
                            try {
                                if (doFilter(file, scanner::acceptsInput)) {
                                    List<Map.Entry<String, String>> entries = scanner.scan(file);
                                    if (entries == null) {
                                        if (classFile == null) classFile = getClassFile(file);
                                        entries = scanner.scan(classFile);
                                    }
                                    if (entries != null) collect.get(scanner.index()).addAll(entries);
                                }
                            } catch (Exception ignored) {}
                        }
                    }
                }
            } catch (Exception ignored) {} finally {
                if (dir != null) dir.close();
            }
        });

        // merge
        return collect.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().filter(e -> e.getKey() != null)
                                .collect(Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        HashMap::new,
                                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())))));
    }

    private boolean doFilter(Vfs.File file, @Nullable Predicate<String> predicate) {
        String path = file.getRelativePath();
        String fqn = path.replace('/', '.');
        return predicate == null || predicate.test(path) || predicate.test(fqn);
    }

    private ClassFile getClassFile(Vfs.File file) {
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(file.openInputStream()))) {
            return new ClassFile(dis);
        } catch (Exception e) {
            throw new ReflectionsException("could not create class object from file " + file.getRelativePath(), e);
        }
    }

    /** collect saved Reflection xml resources and merge it into a Reflections instance
     * <p>by default, resources are collected from all urls that contains the package META-INF/reflections
     * and includes files matching the pattern .*-reflections.xml
     * */
    public static Reflections collect() {
        return collect("META-INF/reflections/", new FilterBuilder().includePattern(".*-reflections\\.xml"));
    }

    /**
     * collect saved Reflections metadata from all urls that contains the given {@code packagePrefix} and matches the given {@code resourceNameFilter},
     * and deserialize using the default serializer {@link org.reflections.serializers.XmlSerializer}
     * <pre>{@code Reflections.collect("META-INF/reflections/",
     *   new FilterBuilder().includePattern(".*-reflections\\.xml")}</pre>
     * <i>prefer using a designated directory (for example META-INF/reflections but not just META-INF), so that collect can work much faster</i>
     */
    public static Reflections collect(String packagePrefix, Predicate<String> resourceNameFilter) {
        return collect(packagePrefix, resourceNameFilter, new XmlSerializer());
    }

    /**
     * collect saved Reflections metadata from all urls that contains the given {@code packagePrefix} and matches the given {@code resourceNameFilter},
     * and deserializes using the given {@code serializer}
     * <pre>{@code Reflections reflections = Reflections.collect(
     *   "META-INF/reflections/",
     *   new FilterBuilder().includePattern(".*-reflections\\.xml"),
     *   new XmlSerializer())}</pre>
     * <i>prefer using a designated directory (for example META-INF/reflections but not just META-INF), so that collect can work much faster</i>
     */
    public static Reflections collect(String packagePrefix, Predicate<String> resourceNameFilter, Serializer serializer) {
        Collection<URL> urls = ClasspathHelper.forPackage(packagePrefix);
        Iterable<Vfs.File> files = Vfs.findFiles(urls, packagePrefix, resourceNameFilter);
        Reflections reflections = new Reflections();
        StreamSupport.stream(files.spliterator(), false)
                .forEach(file -> {
                    try (InputStream inputStream = file.openInputStream()) {
                        reflections.collect(inputStream, serializer);
                    } catch (IOException e) {
                        throw new ReflectionsException("could not merge " + file, e);
                    }
                });
        return reflections;
    }

    /**
     * deserialize and merge saved Reflections metadata from the given {@code inputStream} and {@code serializer}
     * <p><i>useful if you know the serialized resource location and prefer not to look it up the classpath</i>
     */
    public Reflections collect(InputStream inputStream, Serializer serializer) {
        return (Reflections) merge(serializer.read(inputStream));
    }

    /**
     * deserialize and merge saved Reflections metadata from the given {@code file} and {@code serializer}
     * <p><i>useful if you know the serialized resource location and prefer not to look it up the classpath</i>
     */
    public Reflections collect(File file, Serializer serializer) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return collect(inputStream, serializer);
        } catch (IOException e) {
            throw new ReflectionsException("could not obtain input stream from file " + file, e);
        }
    }

    /** merges the given {@code reflections} instance metadata into this instance */
    public Reflections merge(Reflections reflections) {
        reflections.store.forEach((index, map) -> this.store.merge(index, map, (m1, m2) -> {
            m2.forEach((k, v) -> m1.merge(k, v, (s1, s2) -> { s1.addAll(s2); return s1;}));
            return m1;
        }));
        return this;
    }

    public void expandSuperTypes(Map<String, Set<String>> subTypesStore, Map<String, Set<String>> typesAnnotatedStore) {
        if (subTypesStore == null || subTypesStore.isEmpty()) return;
        Set<String> keys = new LinkedHashSet<>(subTypesStore.keySet());
        keys.removeAll(subTypesStore.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        keys.remove("java.lang.Object");
        for (String key : keys) {
            Class<?> type = forClass(key, loaders());
            if (type != null) {
                expandSupertypes(subTypesStore, typesAnnotatedStore, key, type);
            }
        }
    }

    private void expandSupertypes(Map<String, Set<String>> subTypesStore,
                                  Map<String, Set<String>> typesAnnotatedStore, String key, Class<?> type) {
        Set<Annotation> typeAnnotations = ReflectionUtils.getAnnotations(type);
        if (typesAnnotatedStore != null && !typeAnnotations.isEmpty()) {
            String typeName = type.getName();
            for (Annotation typeAnnotation : typeAnnotations) {
                String annotationName = typeAnnotation.annotationType().getName();
                typesAnnotatedStore.computeIfAbsent(annotationName, s -> new HashSet<>()).add(typeName);
            }
        }
        for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
            String supertypeName = supertype.getName();
            if (subTypesStore.containsKey(supertypeName)) {
                subTypesStore.get(supertypeName).add(key);
            } else {
                subTypesStore.computeIfAbsent(supertypeName, s -> new HashSet<>()).add(key);
                expandSupertypes(subTypesStore, typesAnnotatedStore, supertypeName, supertype);
            }
        }
    }

    public <T> Set<T> get(QueryFunction<Store, T> query) {
        return query.apply(store);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        //noinspection unchecked
        return (Set<Class<? extends T>>) get(SubTypes.of(type)
                .as((Class<? extends T>) Class.class, loaders()));
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        return get(SubTypes.of(TypesAnnotated.with(annotation)).asClass(loaders()));
    }

    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation, boolean honorInherited) {
        if (!honorInherited) {
            return getTypesAnnotatedWith(annotation);
        } else {
            if (annotation.isAnnotationPresent(Inherited.class)) {
                return get(TypesAnnotated.get(annotation)
                        .add(SubTypes.of(TypesAnnotated.get(annotation)
                                .filter(c -> !forClass(c, loaders()).isInterface())))
                        .asClass(loaders()));
            } else {
                return get(TypesAnnotated.get(annotation).asClass(loaders()));
            }
        }
    }

    public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation) {
        return get(SubTypes.of(
                TypesAnnotated.of(TypesAnnotated.get(annotation.annotationType())
                        .filter(c -> withAnnotation(annotation).test(forClass(c, loaders())))))
                .asClass(loaders()));
    }

    public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation, boolean honorInherited) {
        if (!honorInherited) {
            return getTypesAnnotatedWith(annotation);
        } else {
            Class<? extends Annotation> type = annotation.annotationType();
            if (type.isAnnotationPresent(Inherited.class)) {
                return get(TypesAnnotated.with(type).asClass(loaders()).filter(withAnnotation(annotation))
                        .add(SubTypes.of(TypesAnnotated.with(type).asClass(loaders()).filter(c -> !c.isInterface()))));
            } else {
                return get(TypesAnnotated.with(type).asClass(loaders()).filter(withAnnotation(annotation)));
            }
        }
    }

    public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        return get(MethodsAnnotated.with(annotation).as(Method.class, loaders()));
    }

    public Set<Method> getMethodsAnnotatedWith(Annotation annotation) {
        return get(MethodsAnnotated.with(annotation.annotationType()).as(Method.class, loaders())
                .filter(withAnnotation(annotation)));
    }

    public Set<Method> getMethodsWithSignature(Class<?>... types) {
        return get(MethodsSignature.with(types).as(Method.class, loaders()));
    }

    public Set<Method> getMethodsWithParameter(AnnotatedElement type) {
        return get(MethodsParameter.with(type).as(Method.class, loaders()));
    }

    public Set<Method> getMethodsReturn(Class<?> type) {
        return get(MethodsReturn.of(type).as(Method.class, loaders()));
    }

    public Set<Constructor> getConstructorsAnnotatedWith(Class<? extends Annotation> annotation) {
        return get(ConstructorsAnnotated.with(annotation).as(Constructor.class, loaders()));
    }

    public Set<Constructor> getConstructorsAnnotatedWith(Annotation annotation) {
        return get(ConstructorsAnnotated.with(annotation.annotationType()).as(Constructor.class, loaders())
                .filter(withAnyParameterAnnotation(annotation)));
    }

    public Set<Constructor> getConstructorsWithSignature(Class<?>... types) {
        return get(ConstructorsSignature.with(types).as(Constructor.class, loaders()));
    }

    public Set<Constructor> getConstructorsWithParameter(AnnotatedElement type) {
        return get(ConstructorsParameter.of(type).as(Constructor.class, loaders()));
    }

    public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        return get(FieldsAnnotated.with(annotation).as(Field.class, loaders()));
    }

    public Set<Field> getFieldsAnnotatedWith(Annotation annotation) {
        return get(FieldsAnnotated.with(annotation.annotationType()).as(Field.class, loaders())
                .filter(withAnnotation(annotation)));
    }

    public Set<String> getResources(String pattern) {
        return get(Resources.with(pattern));
    }

    public Set<String> getResources(Pattern pattern) {
        return getResources(pattern.pattern());
    }

    public List<String> getMemberParameterNames(Member member) {
        return store.getOrDefault(MethodParameterNamesScanner.class.getSimpleName(), Collections.emptyMap()).getOrDefault(toName((AnnotatedElement) member), Collections.emptySet())
                .stream().flatMap(s -> Stream.of(s.split(", "))).collect(Collectors.toList());
    }

    public Collection<Member> getMemberUsage(Member member) {
        Set<String> usages = store.getOrDefault(MemberUsageScanner.class.getSimpleName(), Collections.emptyMap()).getOrDefault(toName((AnnotatedElement) member), Collections.emptySet());
        return forNames(usages, Member.class, loaders());
    }

    @Deprecated
    public Set<String> getAllTypes() {
        return getAll(SubTypes);
    }

    public Set<String> getAll(Scanner scanner) {
        Map<String, Set<String>> map = store.getOrDefault(scanner.index(), Collections.emptyMap());
        return Stream.concat(map.keySet().stream(), map.values().stream().flatMap(Collection::stream)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Store getStore() {
        return store;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public File save(String filename) {
        return save(filename, new XmlSerializer());
    }

    public File save(String filename, Serializer serializer) {
        return serializer.save(this, filename);
    }

    ClassLoader[] loaders() { return configuration.getClassLoaders(); }
}
