package com.ufrn.dct.bsi.touchfy.adapters.outbound.storage;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;

import lombok.AllArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.IntegracaoExternaException;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


@AllArgsConstructor
public class GarageFileStorageImpl implements FileStorageService {
    private final S3Client s3Client;
    private final String bucketName;
    private final String bucketUrl;

    @Override
    public ArquivoArmazenamentoResponse store(final MultipartFile file, final String subDirectory) {
        final String nomeOriginal = Objects.requireNonNull(file.getOriginalFilename());
        final String extensao = extrairExtensao(nomeOriginal);
        final String chave = subDirectory + "/" + UUID.randomUUID() +  "." + extensao;

        try {
            final byte[] bytes = file.getBytes();
            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(chave)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(bytes));
        } catch (IOException e) {
            throw new IntegracaoExternaException("Falha ao ler o arquivo " + chave, e);
        } catch (S3Exception e) {
            throw new IntegracaoExternaException("Falha ao enviar o arquivo " + chave, e);
        }

        return new ArquivoArmazenamentoResponse(
                nomeOriginal,
                chave,
                extensao,
                (double) file.getSize()
        );
    }

    @Override
    public void delete(final String caminhoDoArquivo) {
        try {
            final DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(caminhoDoArquivo)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new IntegracaoExternaException("Falha ao deletar o arquivo " + caminhoDoArquivo, e);
        }
    }

    @Override
    public String getUrl(final String caminhoDoArquivo) {
        return bucketUrl + "/" + caminhoDoArquivo;
    }

    @Override
    public ArquivoRecuperadoResponse retrieve(final String caminhoDoArquivo) {
        try {
            final GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(caminhoDoArquivo)
                    .build();

            final ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);

            return new ArquivoRecuperadoResponse(
                    extrairNomeArquivo(caminhoDoArquivo),
                    caminhoDoArquivo,
                    response.response().contentType(),
                    response.asByteArray()
            );
        } catch (S3Exception e) {
            throw new IntegracaoExternaException("Falha ao buscar o arquivo " + caminhoDoArquivo, e);
        }
    }

    private String extrairExtensao(final String filename) {
        final int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            throw new IllegalArgumentException("Arquivo sem extensão não é permitido.");
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    private String extrairNomeArquivo(final String caminhoDoArquivo) {
        final int slashIndex = caminhoDoArquivo.lastIndexOf('/');
        if (slashIndex < 0) {
            return caminhoDoArquivo;
        }
        return caminhoDoArquivo.substring(slashIndex + 1);
    }
}
