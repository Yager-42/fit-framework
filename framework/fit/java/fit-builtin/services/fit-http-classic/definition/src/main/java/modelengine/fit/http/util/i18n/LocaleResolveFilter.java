package modelengine.fit.http.util.i18n;

import modelengine.fit.http.server.*;
import modelengine.fitframework.annotation.Scope;
import modelengine.fitframework.util.LocaleContext;
import modelengine.fitframework.util.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

public class LocaleResolveFilter implements HttpServerFilter {

    private LocaleResolver localeResolver = null;

    private List<String> matchPatterns = List.of("/**");

    private List<String> mismatchPatterns = List.of();

    private Scope scope = Scope.GLOBAL;

    public LocaleResolveFilter(LocaleResolver localeResolver) {
        localeResolver = localeResolver;
    }

    public LocaleResolveFilter(){
        this.localeResolver = new DefualtLocaleResolver();
    }

    @Override
    public String name() {
        return "LocaleResolveFilter";
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public List<String> matchPatterns() {
        return matchPatterns;
    }

    @Override
    public List<String> mismatchPatterns() {
        return mismatchPatterns;
    }

    @Override
    public void doFilter(HttpClassicServerRequest request, HttpClassicServerResponse response, HttpServerFilterChain chain) throws DoHttpServerFilterException {
        try {
            // 如果参数中带有地区，则直接设置地区
            String paramLocale = request.queries().first("locale").orElse( null);
            Locale responseLocale = null;
            if(paramLocale != null && !paramLocale.trim().isEmpty()){
                responseLocale = Locale.forLanguageTag(paramLocale);
                LocaleContextHolder.setLocaleContext(new LocaleContext(responseLocale));
            }
            // 如果参数中不包含地区，则解析
            else{
                Locale locale =localeResolver.resolveLocale(request);
                LocaleContextHolder.setLocaleContext(new LocaleContext(locale));
            }

            chain.doFilter(request, response);

            // responseLocale是用户期望设置的地区，不受 server 端处理的影响
            localeResolver.setLocale(response, responseLocale);
        }finally {
            LocaleContextHolder.clear();
        }

    }

    @Override
    public Scope scope() {
        return scope;
    }
}
