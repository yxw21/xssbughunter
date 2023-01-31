import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.persistence.PersistedObject;
import burp.api.montoya.proxy.http.InterceptedRequest;
import burp.api.montoya.proxy.http.ProxyRequestHandler;
import burp.api.montoya.proxy.http.ProxyRequestReceivedAction;
import burp.api.montoya.proxy.http.ProxyRequestToBeSentAction;
import models.Request;
import utils.Utils;

import java.util.List;

public class RequestHandler implements ProxyRequestHandler {
    private final MontoyaApi api;
    private final PersistedObject persistedObject;

    RequestHandler(MontoyaApi api, PersistedObject persistedObject) {
        this.api = api;
        this.persistedObject = persistedObject;
    }

    String getString(String key) {
        String value = persistedObject.getString(key);
        if (value == null) {
            return "";
        }
        return value;
    }

    @Override
    public ProxyRequestReceivedAction handleRequestReceived(InterceptedRequest interceptedRequest) {
        String token = getString("token");
        String replace = getString("replace");
        String domain = String.format("%s.xss.cl", getString("domain"));
        String http = interceptedRequest.toString();
        if (http == null || token.isEmpty() || replace.isEmpty() || domain.isEmpty() || !http.contains(replace)) {
            return ProxyRequestReceivedAction.continueWith(interceptedRequest);
        }
        try {
            Request request = Utils.request(api.utilities().base64Utils().encodeToString(http.replace(replace, domain)), token);
            if (request.isSuccess()) {
                String id = request.getId();
                HttpRequest httpRequest = replace(interceptedRequest, String.format("https://%s-%s/", id, domain), replace);
                return ProxyRequestReceivedAction.continueWith(httpRequest);
            } else {
                api.logging().logToError(request.getMessage());
            }
        } catch (Exception e) {
            api.logging().logToError(e.getMessage());
        }
        return ProxyRequestReceivedAction.continueWith(interceptedRequest);
    }

    private HttpRequest replace(HttpRequest httpRequest, String domain, String replace) {
        String path = httpRequest.path().replace(replace, domain);
        httpRequest = httpRequest.withPath(path);
        List<HttpHeader> headers = httpRequest.headers();
        for (HttpHeader httpHeader : headers) {
            String name = httpHeader.name().replace(replace, domain);
            String value = httpHeader.value().replace(replace, domain);
            httpRequest = httpRequest.withRemovedHeader(httpHeader);
            httpRequest = httpRequest.withAddedHeader(HttpHeader.httpHeader(name, value));
        }
        String body = httpRequest.bodyToString().replace(replace, domain);
        httpRequest = httpRequest.withBody(body);
        return httpRequest;
    }

    @Override
    public ProxyRequestToBeSentAction handleRequestToBeSent(InterceptedRequest interceptedRequest) {
        return ProxyRequestToBeSentAction.continueWith(interceptedRequest);
    }
}
