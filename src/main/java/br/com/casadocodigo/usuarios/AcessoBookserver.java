package br.com.casadocodigo.usuarios;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@ToString
public class AcessoBookserver {

  @Getter
  @Setter
  @Column(name = "token_bookserver")
  private String acessoToken;

  @Getter
  @Setter
  @Column(name = "expiracao_token")
  private Calendar dataDeExpiracao;

  @Getter
  @Setter
  @Column
  private String refreshToken;

}
