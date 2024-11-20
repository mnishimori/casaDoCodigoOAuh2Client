package br.com.casadocodigo.integracao.bookserver;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class ConfiguracaoResource {

  @Autowired
  private OAuth2ClientContext oAuth2ClientContext;
  @Autowired
  private ClientTokenServices clientTokenServices;
  @Autowired
  @Qualifier("accessTokenRequest")
  private AccessTokenRequest accessTokenRequest;

  @Bean
  public OAuth2RestTemplate oAuth2RestTemplate() {
    OAuth2ProtectedResourceDetails resourceDetails = bookServer();
    OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, oAuth2ClientContext);
    AccessTokenProviderChain accessTokenProviderChain = new AccessTokenProviderChain(Arrays.asList(new AuthorizationCodeAccessTokenProvider()));
    accessTokenProviderChain.setClientTokenServices(clientTokenServices);
    restTemplate.setAccessTokenProvider(accessTokenProviderChain);
    return restTemplate;
  }

  @Bean
  public OAuth2ProtectedResourceDetails bookServer() {

    AuthorizationCodeResourceDetails detailsForBookserver = new AuthorizationCodeResourceDetails();
    detailsForBookserver.setId("bookserver");
    detailsForBookserver.setTokenName("oauth_token");
    detailsForBookserver.setClientId("cliente-app");
    detailsForBookserver.setClientSecret("$2a$10$sTEY0A3Z3MdRZrJSRRDvhuOzm1q2gE.BKtX91MEvsm5XJtWPJZODu");
    detailsForBookserver.setAccessTokenUri("http://localhost:8080/oauth/token");
    detailsForBookserver.setUserAuthorizationUri("http://localhost:8080/oauth/authorize");
    detailsForBookserver.setScope(Arrays.asList("read", "write"));
    detailsForBookserver.setPreEstablishedRedirectUri(("http://localhost:9000/integracao/callback"));
    detailsForBookserver.setUseCurrentUri(false);
    detailsForBookserver.setClientAuthenticationScheme(AuthenticationScheme.header);
    return detailsForBookserver;
  }

}
