package com.ufrn.dct.bsi.touchfy.adapters.outbound.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockMultipartFile;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

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
    void deveBuscarArquivoERetornarConteudo() {
        final String caminho = "perfil/uuid.png";
        final byte[] conteudo = "conteudo".getBytes();
        final GetObjectResponse getObjectResponse = GetObjectResponse.builder()
                .contentType("image/png")
                .build();

        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class)))
                .thenReturn(ResponseBytes.fromByteArray(getObjectResponse, conteudo));

        final ArquivoRecuperadoResponse response = storage.retrieve(caminho);

        assertNotNull(response);
        assertEquals("uuid.png", response.nome());
        assertEquals(caminho, response.caminhoDoArquivo());
        assertEquals("image/png", response.contentType());
        assertArrayEquals(conteudo, response.conteudo());
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

    @Test
    void deveLancarExcecaoQuandoFalhaNaBusca() {
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> storage.retrieve("perfil/uuid.png"));
    }
}