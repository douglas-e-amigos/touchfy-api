package com.ufrn.dct.bsi.touchfy.adapters.outbound.storage;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarageFileStorageImplTest {

    private S3Client s3Client;
    private GarageFileStorageImpl storage;

    private final String bucketName = "touchfy-uploads";
    private final String bucketUrl = "http://garage:3900/touchfy-uploads";

    @BeforeEach
    void setUp() {
        s3Client = mock(S3Client.class);
        storage = new GarageFileStorageImpl(s3Client, bucketName, bucketUrl);
    }

    @Test
    void deveSalvarArquivoERetornarResponse() {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "foto.png",
                "image/png",
                "conteudo".getBytes()
        );

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        final ArquivoArmazenamentoResponse response = storage.store(file, "perfil");

        assertNotNull(response);
        assertEquals("foto.png", response.nome());
        assertEquals("png", response.extensao());
        assertEquals(8.0, response.tamanhoEmBytes());
        assertTrue(response.caminhoDoArquivo().startsWith("perfil/"));
        assertTrue(response.caminhoDoArquivo().endsWith(".png"));
    }

    @Test
    void deveLancarExcecaoQuandoArquivoSemExtensao() {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "arquivo-sem-extensao",
                "image/png",
                "conteudo".getBytes()
        );

        assertThrows(IllegalArgumentException.class,
                () -> storage.store(file, "perfil"));
    }

    @Test
    void deveDeletarArquivoCorretamente() {
        final String caminho = "perfil/uuid.png";

        storage.delete(caminho);

        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void deveRetornarUrlCorreta() {
        final String caminho = "perfil/uuid.png";
        final String url = storage.getUrl(caminho);

        assertEquals("http://garage:3900/touchfy-uploads/perfil/uuid.png", url);
    }

    @Test
    void deveLancarExcecaoQuandoFalhaNoUpload() {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "foto.png",
                "image/png",
                "conteudo".getBytes()
        );

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> storage.store(file, "perfil"));
    }
}