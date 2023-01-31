import burp.api.montoya.MontoyaApi;
import burp.api.montoya.persistence.PersistedObject;
import models.Login;
import utils.Utils;

import java.util.TimerTask;

public class TokenTimerTask extends TimerTask {
    private final MontoyaApi api;

    private final PersistedObject persistedObject;

    TokenTimerTask(MontoyaApi api, PersistedObject persistedObject) {
        this.api = api;
        this.persistedObject = persistedObject;
    }

    @Override
    public void run() {
        String username = persistedObject.getString("username");
        String password = persistedObject.getString("password");
        String token = persistedObject.getString("token");
        if (username == null || password == null || token == null) {
            return;
        }
        String base64 = token.split("\\.")[1];
        String json = api.utilities().base64Utils().decode(base64).toString();
        String exps = json.split("\"exp\":")[1];
        String exp = exps.split("}")[0];
        int t = Integer.parseInt(exp) - 5 * 60;
        long n = System.currentTimeMillis() / 1000;
        if (n >= t) {
            try {
                Login login = Utils.login(username, password);
                if (login.isSuccess()) {
                    persistedObject.setString("token", login.getToken());
                    persistedObject.setString("domain", login.getUser().getDomain());
                } else {
                    api.logging().logToError(login.getMessage());
                }
            } catch (Exception e) {
                api.logging().logToError(e.getMessage());
            }
        }
    }
}
