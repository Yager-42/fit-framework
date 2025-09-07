package modelengine.fit.example;

import modelengine.fit.http.server.HttpClassicServerRequest;
import modelengine.fit.http.server.HttpClassicServerResponse;
import modelengine.fit.http.util.i18n.DefualtLocaleResolver;
import modelengine.fit.http.util.i18n.LocaleResolver;
import modelengine.fitframework.annotation.Bean;
import modelengine.fitframework.annotation.Component;

import java.util.Locale;

@Component
public class OtherLocaleResolveConfig {
    @Bean
    public LocaleResolver localeResolver1() {
        LocaleResolver localeResolver = new LocaleResolver() {
            @Override
            public Locale resolveLocale(HttpClassicServerRequest request) {
                return Locale.forLanguageTag("cn");
            }

            @Override
            public void setLocale(HttpClassicServerResponse response, Locale locale) {

            }

            @Override
            public String getUrlPattern() {
                return "/locale";
            }

            @Override
            public void setUrlPattern(String urlPattern) {

            }
        };
        return localeResolver;
    }
}
