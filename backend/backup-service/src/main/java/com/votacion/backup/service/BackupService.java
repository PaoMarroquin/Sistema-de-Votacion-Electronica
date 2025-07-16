package com.votacion.backup.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.votacion.backup.model.VotoBackup;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class BackupService {

    private final MinioClient minioClient;
    private final ObjectMapper objectMapper;
    
    @Value("${minio.bucket}")
    private String bucket; 

    public BackupService(MinioClient minioClient, ObjectMapper objectMapper) {
        this.minioClient = minioClient;
        this.objectMapper = objectMapper;
    }

    public void respaldarVoto(VotoBackup voto) throws Exception {
        // Convertir voto a JSON
        String json = objectMapper.writeValueAsString(voto);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);

        // Asegurar bucket
        //if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
          //  minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        //}

        // Subir archivo
        String nombreArchivo = "voto_" + voto.getId() + ".json";
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(nombreArchivo)
                .stream(new ByteArrayInputStream(jsonBytes), jsonBytes.length, -1)
                .contentType("application/json")
                .build());
    }

    public InputStream descargarBackup(String votoId) throws Exception {
        String nombreArchivo = "voto_" + votoId + ".json";
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(nombreArchivo)
                .build());
    }

    public InputStream descargarVoto(UUID id) throws Exception {
        String objectName = "voto_" + id + ".json";
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .build());
    }

    public boolean existeVoto(UUID id) {
        try {
            String objectName = "voto_" + id + ".json";
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia del respaldo", e);
        }
    }

    public void eliminarVoto(UUID id) throws Exception {
        String objectName = "voto_" + id + ".json";
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .build());
    }
}
