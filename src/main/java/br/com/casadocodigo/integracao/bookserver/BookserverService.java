package br.com.casadocodigo.integracao.bookserver;

import br.com.casadocodigo.integracao.model.Livro;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class BookserverService {

  @Autowired
  private OAuth2RestTemplate restTemplate;

  public List<Livro> livros(String token) throws UsuarioSemAutorizacaoException {
    String endpoint = "http://localhost:8080/api/livros";

    try {
      Livro[] livros = restTemplate.getForObject(endpoint, Livro[].class);
      return listaFromArray(livros);
    } catch (HttpClientErrorException e) {
      throw new UsuarioSemAutorizacaoException("não foi possível obter os livros do usuário");
    }
  }

  private List<Livro> listaFromArray(Livro[] livros) {
    List<Livro> lista = new ArrayList<>();

    for (Livro livro : livros) {
      lista.add(livro);
    }

    return lista;
  }

}
