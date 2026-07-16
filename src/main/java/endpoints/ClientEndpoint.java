package endpoints;

import models.client.ClientRegisterRequest;
import utils.RestHelper;

public class ClientEndpoint {
        private final String clientEndpoint = "/api-clients";

        public <T> T registerClient(Integer statusCode, Class<T> responseClass, ClientRegisterRequest request, String schemaPath) {
            return RestHelper.post(clientEndpoint, responseClass, statusCode, request);
        }
}
