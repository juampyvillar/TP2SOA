import java.util.Timer;

public final class ParametrosGenerales {

        private static ParametrosGenerales instancia;
        public String token;
        public String token_refresh;

        private ParametrosGenerales(String token, String token_refresh) {
            this.token = token;
            this.token_refresh = token_refresh;
        }

        public static ParametrosGenerales getInstancia(String token, String token_refresh) {
            if (instancia == null) {
                instancia = new ParametrosGenerales(token,token_refresh);
            }
            return instancia;
        }
        public void  actualizarTokenRefresh(String token_refresh){
            this.token_refresh = token_refresh;
        }
}

