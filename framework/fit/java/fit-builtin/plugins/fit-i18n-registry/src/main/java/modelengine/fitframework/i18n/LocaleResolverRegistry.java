/*---------------------------------------------------------------------------------------------
 *  Copyright (c) 2025 Huawei Technologies Co., Ltd. All rights reserved.
 *  This file is a part of the ModelEngine Project.
 *  Licensed under the MIT License. See License.txt in the project root for license information.
 *--------------------------------------------------------------------------------------------*/

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

/**
 * 地区解析器注册中心
 *
 * @author 阮睿
 * @since 2025-09-07
 */
@Component
public class LocaleResolverRegistry implements PluginStartedObserver, PluginStoppingObserver {
    private static final char PATH_SEPARATOR = '/';

    // 参考 DefaultHttpDispatcher 的三层结构
    private final Map<String, LocaleResolver> noPathVariableResolvers = new ConcurrentHashMap<>();
    private final Map<String, MappingTree<LocaleResolver>> pathVariableResolvers = new ConcurrentHashMap<>();
    private final Map<String, LocaleResolver> wildcardResolvers = new ConcurrentHashMap<>();

    private final LocaleResolver defaultLocaleResolver = new DefualtLocaleResolver();

    /**
     * 构造函数。
     */
    public LocaleResolverRegistry() {
        // 与 DefaultHttpDispatcher 保持一致，使用 ConcurrentHashMap 提供线程安全。
        this.pathVariableResolvers.put(DefaultMappingTree.PATH_SEPARATOR, new DefaultMappingTree<>());
    }

    /**
     * 注册地区解析器。
     *
     * @param resolver 表示将要被注册地区解析器的 {@link LocaleResolver}。
     */
    public void register(LocaleResolver resolver) {
        notNull(resolver, "The locale resolver cannot be null.");
        String pathPattern = MappingTree.convertToMatchedPathPattern(resolver.getUrlPattern());
        notBlank(pathPattern, "The path pattern cannot be blank.");
        LocaleResolver preResolver;
        if (pathPattern.contains("**")) {
            preResolver = this.wildcardResolvers.put(pathPattern, resolver);
        } else if (pathPattern.contains("*")) {
            preResolver = this.pathVariableResolvers.get(DefaultMappingTree.PATH_SEPARATOR)
                    .register(pathPattern, resolver)
                    .orElse(null);
        } else {
            preResolver = this.noPathVariableResolvers.put(pathPattern, resolver);
        }
        if (preResolver != null) {
            String message = StringUtils.format("Locale resolver has been registered. [pattern={0}]", pathPattern);
            throw new RegisterLocaleResolverException(message);
        }
    }

    /**
     * 取消注册地区解析器。
     *
     * @param resolver 表示待取消注册地区解析器的 {@link LocaleResolver}。
     */
    public void unregister(LocaleResolver resolver) {
        notNull(resolver, "The locale resolver cannot be null.");
        String pathPattern = MappingTree.convertToMatchedPathPattern(resolver.getUrlPattern());
        notBlank(pathPattern, "The path pattern cannot be blank.");
        if (pathPattern.contains("**")) {
            this.wildcardResolvers.remove(pathPattern);
        } else if (pathPattern.contains("*")) {
            this.pathVariableResolvers.get(DefaultMappingTree.PATH_SEPARATOR).unregister(pathPattern);
            ;
        } else {
            this.noPathVariableResolvers.remove(pathPattern);
        }
    }

    /**
     * 分派地区解析器。
     *
     * @param request 表示用于查找对应 {@link LocaleResolver} 请求的 {@link HttpClassicServerRequest}。
     * @return 返回匹配的 {@link LocaleResolver}。
     */
    public LocaleResolver dispatch(HttpClassicServerRequest request) {
        String path = UrlUtils.decodePath(request.path());
        return (LocaleResolver) OptionalUtils.get(() -> selectFromNoPathVariableResolvers(path))
                .orElse(() -> selectFromPathVariableResolvers(path))
                .orElse(() -> selectFromWildcardResolvers(path))
                .orDefault(this.defaultLocaleResolver);
    }

    /**
     * 从无路径参数的解析器中查找匹配的解析器。
     *
     * @param path 表示待匹配路径的 {@link String}。
     * @return 表示匹配对应路径解析器的 {@link Optional}{@code <}{@link LocaleResolver}{@code >}。。
     */
    private Optional<LocaleResolver> selectFromNoPathVariableResolvers(String path) {
        LocaleResolver resolver = this.noPathVariableResolvers.get(path);
        return Optional.ofNullable(resolver);
    }

    /**
     * 从路径参数的解析器中查找匹配的解析器。
     *
     * @param path 待匹配路径的 {@link String}。
     * @return 匹配对应路径解析器的 {@link Optional}{@code <}{@link LocaleResolver}{@code >}。
     */
    private Optional<LocaleResolver> selectFromPathVariableResolvers(String path) {
        return this.pathVariableResolvers.get(DefaultMappingTree.PATH_SEPARATOR).search(path);
    }

    /**
     * 从通配符的解析器中查找匹配的解析器。
     *
     * @param path 待匹配路径的 {@link String}。
     * @return 匹配对应路径解析器的 {@link Optional}{@code <}{@link LocaleResolver}{@code >}。
     */
    private Optional<LocaleResolver> selectFromWildcardResolvers(String path) {
        for (Map.Entry<String, LocaleResolver> entry : this.wildcardResolvers.entrySet()) {
            PathPattern pattern = Pattern.forPath(entry.getKey(), PATH_SEPARATOR);
            if (pattern.matches(path)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    /**
     * 当插件启动时，注册插件中的地区解析器。
     *
     * @param plugin 待注册插件的 {@link Plugin}。
     */
    @Override
    public void onPluginStarted(Plugin plugin) {
        BeanContainer container = plugin.container();
        List<LocaleResolver> localeResolvers = container.all(LocaleResolver.class)
                .stream()
                .map(BeanFactory::<LocaleResolver>get)
                .collect(Collectors.toList());
        for (LocaleResolver localeResolver : localeResolvers) {
            this.register(localeResolver);
        }
    }

    /**
     * 当插件停止时，取消注册插件中的地区解析器。
     *
     * @param plugin 待取消注册插件的 {@link Plugin}。
     */
    @Override
    public void onPluginStopping(Plugin plugin) {
        BeanContainer container = plugin.container();
        List<LocaleResolver> localeResolvers = container.all(LocaleResolver.class)
                .stream()
                .map(BeanFactory::<LocaleResolver>get)
                .collect(Collectors.toList());
        for (LocaleResolver localeResolver : localeResolvers) {
            this.unregister(localeResolver);
        }
    }
}
