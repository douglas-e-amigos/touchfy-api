package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarFotoPerfilUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.enums.DiretorioStorage;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class AtualizarFotoPerfilUsuarioUseCase {
  private final UsuarioRepository usuarioRepository;
  private final UploadArquivoUseCase uploadArquivoUseCase;
  private final DeletarArquivoUseCase deletarArquivoUseCase;

  public void execute(final UUID usuarioId, final AtualizarFotoPerfilUsuarioRequest request) {
    final var usuarioEntity =
        this.usuarioRepository
            .acharPeloId(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    if (usuarioEntity.getCaminhoDaImagemDePerfil() != null) {
      this.deletarArquivoUseCase.execute(usuarioEntity.getCaminhoDaImagemDePerfil());
    }
    final String diretorio =
        String.format(
            DiretorioStorage.FOTOS_DE_PERFIL.getDiretorio(), usuarioEntity.getNomeUsuario());
    final var metadadosArmazenamento = this.uploadArquivoUseCase.execute(request.foto(), diretorio);
    this.usuarioRepository.atualizarFotoPerfilUsuario(
        usuarioEntity, metadadosArmazenamento.caminhoDoArquivo());
  }
}
