package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListarAlbunsUseCase {
  private final AlbumRepository repository;
  private final AlbumMapper albumMapper;

  public List<AlbumResponse> execute() {
    return repository.listar().stream().map(albumMapper::toResponse).toList();
  }
}
