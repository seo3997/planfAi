package com.whomade.planfAi.common.util.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePathResolver {

    @Value("${file.product.upload-dir:}")
    private String productUploadDir;

    @Value("${file.product.public-url:}")
    private String productPublicUrl;

    @Value("${file.board.upload-dir:}")
    private String boardUploadDir;

    @Value("${file.board.public-url:}")
    private String boardPublicUrl;

    public Storage resolve(String pathKey) {
        if ("product".equalsIgnoreCase(pathKey)) {
            return new Storage(ensureDir(productUploadDir), ensureUrl(productPublicUrl));
        } else if ("board".equalsIgnoreCase(pathKey)) {
            return new Storage(ensureDir(boardUploadDir), ensureUrl(boardPublicUrl));
        }
        // 필요하면 기본값이나 예외처리를 바꿔도 됨
        throw new IllegalArgumentException("Unknown pathKey: " + pathKey + " (use 'product' or 'board')");
    }

    private String ensureDir(String dir) {
        if (dir == null || dir.isEmpty())
            return dir;
        // Windows/Unix 모두 안전: 마지막 구분자 강제 제거 (Paths로 합칠 것이므로)
        if (dir.endsWith("/") || dir.endsWith("\\")) {
            return dir.substring(0, dir.length() - 1);
        }
        return dir;
    }

    private String ensureUrl(String url) {
        if (url == null || url.isEmpty())
            return url;
        return url.endsWith("/") ? url : (url + "/");
    }

    public static class Storage {
        private final String uploadDir;
        private final String publicUrl;

        public Storage(String uploadDir, String publicUrl) {
            this.uploadDir = uploadDir;
            this.publicUrl = publicUrl;
        }

        public String getUploadDir() {
            return uploadDir;
        }

        public String getPublicUrl() {
            return publicUrl;
        }
    }
}
