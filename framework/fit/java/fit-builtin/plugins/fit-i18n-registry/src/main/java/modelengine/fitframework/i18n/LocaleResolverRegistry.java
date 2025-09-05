package modelengine.fitframework.i18n;

import modelengine.fit.http.server.HttpClassicServerRequest;
import modelengine.fit.http.server.HttpClassicServerResponse;
import modelengine.fit.http.server.dispatch.MappingTree;
import modelengine.fit.http.server.dispatch.support.DefaultMappingTree;
import modelengine.fit.http.util.i18n.LocaleResolver;
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
import modelengine.fitframework.util.wildcard.PathPattern;
import modelengine.fitframework.util.wildcard.Pattern;

@Component
public class LocaleResolverRegistry implements PluginStartedObserver, PluginStoppingObserver {
    private static final char PATH_SEPARATOR = '/';

    // 参考DefaultHttpDispatcher的三层结构
    private final Map<String, LocaleResolver> noPathVariableResolvers = new ConcurrentHashMap<>();
    private final MappingTree<LocaleResolver> pathVariableResolvers = new DefaultMappingTree<>();
    private final Map<String, LocaleResolver> wildcardResolvers = new ConcurrentHashMap<>();


    public void register(String pluginName, LocaleResolver resolver, String urlPattern) {
        String pathPattern = MappingTree.convertToMatchedPathPattern(urlPattern);
        if (pathPattern.contains("**")) {
            wildcardResolvers.put(pathPattern, resolver);
        } else if (pathPattern.contains("*")) {
            pathVariableResolvers.register(pathPattern, resolver);
        } else {
            noPathVariableResolvers.put(pathPattern, resolver);
        }
    }

    public void unregister(String pluginName, List<String> urlPatterns) {
        for (String pattern : urlPatterns) {
            String pathPattern = MappingTree.convertToMatchedPathPattern(pattern);
            if (pathPattern.contains("**")) {
                wildcardResolvers.remove(pathPattern);
            } else if (pathPattern.contains("*")) {
                pathVariableResolvers.unregister(pathPattern);
            } else {
                noPathVariableResolvers.remove(pathPattern);
            }
        }
    }

    public LocaleResolver dispatch(HttpClassicServerRequest request) {
        String path = UrlUtils.decodePath(request.path());
        return (LocaleResolver) OptionalUtils.get(() -> selectFromNoPathVariableResolvers(path))
                .orElse(() -> selectFromPathVariableResolvers(path))
                .orElse(() -> selectFromWildcardResolvers(path))
                .orElse(null);
    }

    private Optional<LocaleResolver> selectFromNoPathVariableResolvers(String path) {
        LocaleResolver resolver = noPathVariableResolvers.get(path);
        return Optional.ofNullable(resolver);
    }

    private Optional<LocaleResolver> selectFromPathVariableResolvers(String path) {
        return pathVariableResolvers.search(path);
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
            this.register(localeResolver.getName(),localeResolver,localeResolver.getUrlPattern(),localeResolver.getPriority());
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
            this.unregister(localeResolver.getName());
        }
    }
}
