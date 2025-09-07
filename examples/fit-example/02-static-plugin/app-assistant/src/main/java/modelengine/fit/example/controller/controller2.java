package modelengine.fit.example.controller;

import modelengine.fit.http.annotation.GetMapping;
import modelengine.fit.http.annotation.RequestMapping;
import modelengine.fitframework.annotation.Component;
import modelengine.fitframework.util.LocaleContextHolder;

@Component
@RequestMapping(path = "/1")
public class controller2 {
    @GetMapping(path = "/weather")
    public String getWeather() {
        return LocaleContextHolder.getLocale().getLanguage();
    }
}
