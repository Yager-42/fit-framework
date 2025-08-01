package modelengine.fitframework.validation.data;

import modelengine.fit.http.util.i18n.LocaleResolveFilter;
import modelengine.fitframework.annotation.Bean;
import modelengine.fitframework.annotation.Component;

@Component
public class localeResolveConfig {
    @Bean
    public LocaleResolveFilter localeResolveFilter() {
        return new LocaleResolveFilter();
    }
}
