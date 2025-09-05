package modelengine.fitframework.i18n;

import modelengine.fit.http.util.i18n.LocaleResolver;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocaleResolverEntry {
    private final LocaleResolver resolver;
    private final Pattern urlPattern;
    private final int priority;

    public LocaleResolverEntry(LocaleResolver resolver, String urlPattern, int priority) {
        this.resolver = resolver;
        this.priority = priority;
        this.urlPattern = this.compilePattern(urlPattern);
    }

    private Pattern compilePattern(String pattern) {
        // 支持Ant风格的路径匹配: /api/** -> /api/.*
        String regex = pattern
                .replace("**", ".*")
                .replace("*", "[^/]*")
                .replace("?", ".");
        return Pattern.compile("^" + regex + "$");
    }

    public boolean matches(String requestPath) {
        return urlPattern.matcher(requestPath).matches();
    }

    public  int getPriority() {
        return priority;
    }

    public LocaleResolver getResolver() {
        return resolver;
    }
}
