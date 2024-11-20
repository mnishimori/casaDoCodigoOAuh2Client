package br.com.casadocodigo.usuarios;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UsuariosRepository extends CrudRepository<Usuario, Integer> {

  Optional<Usuario> findByLogin(String login);

  Optional<Usuario> findById(Integer id);
}
