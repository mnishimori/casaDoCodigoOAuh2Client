package br.com.casadocodigo.integracao.bookserver;

import br.com.casadocodigo.configuracao.seguranca.UsuarioLogado;
import br.com.casadocodigo.usuarios.AcessoBookserver;
import br.com.casadocodigo.usuarios.Usuario;
import br.com.casadocodigo.usuarios.UsuariosRepository;
import java.util.Calendar;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

@Service
public class BookServerClientTokenServices implements ClientTokenServices {

  @Autowired
  private UsuariosRepository usuariosRepository;

  @Override
  public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
    UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
    Usuario usuario = getUsuario(usuarioLogado);
    if (usuario == null) {
      return null;
    }
    AcessoBookserver acessoBookserver = usuario.getAcessoBookserver();
    if (acessoBookserver == null || acessoBookserver.getAcessoToken() == null) {
      return null;
    }
    String accessToken = acessoBookserver.getAcessoToken();
    Calendar dataDeExpiracao = acessoBookserver.getDataDeExpiracao();
    if (accessToken == null || dataDeExpiracao == null) {
      return null;
    }
    DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
    oAuth2AccessToken.setExpiration(dataDeExpiracao.getTime());
    return oAuth2AccessToken;
  }

  @Override
  public void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication,
      OAuth2AccessToken accessToken) {
    UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
    Usuario usuario = getUsuario(usuarioLogado);
    if (usuario != null) {
      AcessoBookserver acessoBookserver = new AcessoBookserver();
      acessoBookserver.setAcessoToken(accessToken.getValue());
      acessoBookserver.setRefreshToken(accessToken.getRefreshToken().getValue());
      Calendar expirationDate = Calendar.getInstance();
      expirationDate.setTime(accessToken.getExpiration());
      acessoBookserver.setDataDeExpiracao(expirationDate);

      usuario.setAcessoBookserver(acessoBookserver);
      usuariosRepository.save(usuario);
    }
  }

  @Override
  public void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
    UsuarioLogado usuarioLogado = (UsuarioLogado) authentication.getPrincipal();
    Usuario usuario = getUsuario(usuarioLogado);
    usuario.setAcessoBookserver(null);
    usuariosRepository.save(usuario);
  }

  private Usuario getUsuario(UsuarioLogado usuarioLogado) {
    Optional<Usuario> usuarioOptional = usuariosRepository.findById(usuarioLogado.getId());
    return usuarioOptional.orElse(null);
  }
}
