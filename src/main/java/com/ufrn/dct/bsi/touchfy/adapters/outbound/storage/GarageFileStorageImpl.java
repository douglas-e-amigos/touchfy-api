package com.ufrn.dct.bsi.touchfy.adapters.outbound.storage;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Objects;
import java.util.UUID;


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
            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(chave)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar o arquivo " + chave, e);
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
        } catch (Exception e) {
            throw new RuntimeException("Falha ao deletar o arquivo " + caminhoDoArquivo, e);
        }
    }

    @Override
    public String getUrl(final String caminhoDoArquivo) {
        return bucketUrl + "/" + caminhoDoArquivo;
    }

    private String extrairExtensao(final String filename) {
        final int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            throw new IllegalArgumentException("Arquivo sem extensão não é permitido.");
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }
}
