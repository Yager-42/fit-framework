package modelengine.fit.http.util.i18n;

import modelengine.fit.http.server.HttpServerException;

public class RegisterLocaleResolverException extends HttpServerException {
    public RegisterLocaleResolverException(String message) {
        super(message);
    }

    public RegisterLocaleResolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
