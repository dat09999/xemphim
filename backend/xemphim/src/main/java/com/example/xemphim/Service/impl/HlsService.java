package com.example.xemphim.Service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class HlsService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.video}")
    private String videoBucket;

    @Value("${minio.bucket.keys}")
    private String keysBucket;

    public String convertAndUpload(MultipartFile file, String videoId) throws Exception {
        Path tempDir = Files.createTempDirectory("hls_" + videoId);
        Path inputFile = tempDir.resolve("input.mp4");
        Path keyFile = tempDir.resolve("encrypt.key");
        Path keyInfoFile = tempDir.resolve("key.info");

        try {
            // 1. Lưu file upload vào thư mục tạm
            file.transferTo(inputFile);

            // 2. Tạo key ngẫu nhiên 16 bytes
            byte[] key = new byte[16];
            new SecureRandom().nextBytes(key);
            Files.write(keyFile, key);

            // 3. Tạo key.info
            Files.writeString(keyInfoFile,
                    "http://localhost:8080/api/key/" + videoId + "\n" +
                            keyFile.toAbsolutePath() + "\n"
            );

            // 4. Chạy ffmpeg convert
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-i", inputFile.toString(),
                    "-codec:", "copy",
                    "-hls_time", "10",
                    "-hls_key_info_file", keyInfoFile.toString(),
                    "-hls_segment_filename", tempDir + "/seg_%03d.ts",
                    "-hls_list_size", "0",
                    "-f", "hls",
                    tempDir + "/index.m3u8"
            );
            pb.redirectErrorStream(true); // gộp stderr vào stdout

            Process process = pb.start();

            // QUAN TRỌNG: Đọc output liên tục, không đọc thì buffer đầy → treo!
            StreamGobbler gobbler = new StreamGobbler(process.getInputStream());
            gobbler.start();

            int exitCode = process.waitFor();
            gobbler.join(); // Chờ gobbler đọc hết

            if (exitCode != 0) throw new RuntimeException("ffmpeg failed! Exit code: " + exitCode);

            // 5. Lưu key vào MinIO bucket riêng (private)
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(keysBucket)
                            .object(videoId + ".key")
                            .stream(new ByteArrayInputStream(key), key.length, -1)
                            .contentType("application/octet-stream")
                            .build()
            );

            // 6. Upload toàn bộ .m3u8 và .ts lên MinIO
            Files.list(tempDir)
                    .filter(p -> p.toString().endsWith(".m3u8") || p.toString().endsWith(".ts"))
                    .forEach(path -> {
                        try {
                            String fileName = path.getFileName().toString();
                            String contentType = fileName.endsWith(".m3u8")
                                    ? "application/vnd.apple.mpegurl"
                                    : "video/MP2T";

                            minioClient.putObject(
                                    PutObjectArgs.builder()
                                            .bucket(videoBucket)
                                            .object("hls/" + videoId + "/" + fileName)
                                            .stream(Files.newInputStream(path), Files.size(path), -1)
                                            .contentType(contentType)
                                            .build()
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

            return "hls/" + videoId + "/index.m3u8";

        } finally {
            // 7. Xóa file tạm
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> p.toFile().delete());
        }
    }

    // Đọc output của ffmpeg liên tục để tránh buffer đầy → treo
    private static class StreamGobbler extends Thread {
        private final InputStream is;

        StreamGobbler(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println("[ffmpeg] " + line);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }
}