package modelengine.fitframework.i18n;

import static modelengine.fitframework.inspection.Validation.notBlank;
import static modelengine.fitframework.inspection.Validation.notNull;

import modelengine.fit.http.server.HttpClassicServerRequest;
import modelengine.fit.http.server.dispatch.MappingTree;
import modelengine.fit.http.server.dispatch.support.DefaultMappingTree;
import modelengine.fit.http.util.i18n.DefualtLocaleResolver;
import modelengine.fit.http.util.i18n.LocaleResolver;
import modelengine.fit.http.util.i18n.RegisterLocaleResolverException;
import modelengine.fitframework.annotation.Component;
import modelengine.fitframework.ioc.BeanContainer;
import modelengine.fitframework.ioc.BeanFactory;
import modelengine.fitframework.plugin.Plugin;
import modelengine.fitframework.plugin.PluginStartedObserver;
import modelengine.fitframework.plugin.PluginStoppingObserver;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import modelengine.fitframework.resource.UrlUtils;
import modelengine.fitframework.util.OptionalUtils;
import modelengine.fitframework.util.StringUtils;
import modelengine.fitframework.util.wildcard.PathPattern;
import modelengine.fitframework.util.wildcard.Pattern;

@Component
public class LocaleResolverRegistry implements PluginStartedObserver, PluginStoppingObserver {
    private static final char PATH_SEPARATOR = '/';

    // 参考DefaultHttpDispatcher的三层结构
    private final Map<String, LocaleResolver> noPathVariableResolvers = new ConcurrentHashMap<>();
    private final Map<String, MappingTree<LocaleResolver>> pathVariableResolvers = new ConcurrentHashMap<>();
    private final Map<String, LocaleResolver> wildcardResolvers = new ConcurrentHashMap<>();

    private final LocaleResolver defaultLocaleResolver = new DefualtLocaleResolver();

    public LocaleResolverRegistry() {
        // 与 DefaultHttpDispatcher 保持一致，使用 ConcurrentHashMap 提供线程安全。
        this.pathVariableResolvers.put(DefaultMappingTree.PATH_SEPARATOR, new DefaultMappingTree<>());
    }

    public void register(LocaleResolver resolver) {
        notNull(resolver, "The locale resolver cannot be null.");
        String pathPattern = MappingTree.convertToMatchedPathPattern(resolver.getUrlPattern());
        notBlank(pathPattern, "The path pattern cannot be blank.");
        LocaleResolver preResolver;
        if (pathPattern.contains("**")) {
            preResolver = wildcardResolvers.put(pathPattern, resolver);
        } else if (pathPattern.contains("*")) {
            preResolver = pathVariableResolvers.get(DefaultMappingTree.PATH_SEPARATOR)
                    .register(pathPattern, resolver)
                    .orElse(null);
            ;
        } else {
            preResolver = noPathVariableResolvers.put(pathPattern, resolver);
        }
        if (preResolver != null) {
            String message = StringUtils.format("Locale resolver has been registered. [pattern={0}]", pathPattern);
            throw new RegisterLocaleResolverException(message);
        }
    }

    public void unregister(LocaleResolver resolver) {
        notNull(resolver, "The locale resolver cannot be null.");
        String pathPattern = MappingTree.convertToMatchedPathPattern(resolver.getUrlPattern());
        notBlank(pathPattern, "The path pattern cannot be blank.");
        if (pathPattern.contains("**")) {
            wildcardResolvers.remove(pathPattern);
        } else if (pathPattern.contains("*")) {
            pathVariableResolvers.get(DefaultMappingTree.PATH_SEPARATOR).unregister(pathPattern);
            ;
        } else {
            noPathVariableResolvers.remove(pathPattern);
        }
    }

    public LocaleResolver dispatch(HttpClassicServerRequest request) {
        String path = UrlUtils.decodePath(request.path());
        return (LocaleResolver) OptionalUtils.get(() -> selectFromNoPathVariableResolvers(path))
                .orElse(() -> selectFromPathVariableResolvers(path))
                .orElse(() -> selectFromWildcardResolvers(path)).orDefault(this.defaultLocaleResolver);
    }

    private Optional<LocaleResolver> selectFromNoPathVariableResolvers(String path) {
        LocaleResolver resolver = noPathVariableResolvers.get(path);
        return Optional.ofNullable(resolver);
    }

    private Optional<LocaleResolver> selectFromPathVariableResolvers(String path) {
        return pathVariableResolvers.get(DefaultMappingTree.PATH_SEPARATOR).search(path);
    }

    private Optional<LocaleResolver> selectFromWildcardResolvers(String path) {
        for (Map.Entry<String, LocaleResolver> entry : wildcardResolvers.entrySet()) {
            PathPattern pattern = Pattern.forPath(entry.getKey(), PATH_SEPARATOR);
            if (pattern.matches(path)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @Override
    public void onPluginStarted(Plugin plugin) {
        BeanContainer container = plugin.container();
        List<LocaleResolver> localeResolvers = container.all(LocaleResolver.class)
                .stream()
                .map(BeanFactory::<LocaleResolver>get)
                .collect(Collectors.toList());
        for(LocaleResolver localeResolver : localeResolvers) {
            this.register(localeResolver);
        }
    }

    @Override
    public void onPluginStopping(Plugin plugin) {
        BeanContainer container = plugin.container();
        List<LocaleResolver> localeResolvers = container.all(LocaleResolver.class)
                .stream()
                .map(BeanFactory::<LocaleResolver>get)
                .collect(Collectors.toList());
        for(LocaleResolver localeResolver : localeResolvers) {
            this.unregister(localeResolver);
        }
    }
}
