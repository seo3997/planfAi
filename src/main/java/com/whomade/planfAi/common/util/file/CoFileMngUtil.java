package com.whomade.planfAi.common.util.file;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.imageio.ImageIO;

import jakarta.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.whomade.planfAi.common.util.CoMessageSource;
import com.whomade.planfAi.common.util.CoStringUtils;
import com.whomade.planfAi.common.util.SysUtil;
import com.whomade.planfAi.common.util.file.vo.CoFileVO;

/**
 * 파일 저장 유틸(개편판)
 * - pathKey: "product" 또는 "board"
 * - Spring 설정(file.*)과 FilePathResolver를 사용
 */
@Component("CoFileMngUtil")
public class CoFileMngUtil {

    public static final int BUFF_SIZE = 2048;
    public static final String FILE_EXT_C = ".file";
    private static final Log log = LogFactory.getLog(CoFileMngUtil.class);

    @Resource(name = "coMessageSource")
    private CoMessageSource messageSource;

    private final FilePathResolver resolver;

    public CoFileMngUtil(FilePathResolver resolver) {
        this.resolver = resolver;
    }

    /* ========= 업로드 용량 제한 (Spring 설정) ========= */
    @Value("${file.max-size-total:50MB}")
    private String maxSizeTotalConf; // 요청 내 모든 파일 합계

    @Value("${file.max-size-each:10MB}")
    private String maxSizeEachConf; // 개별 파일 최대

    private long toBytes(String v) {
        if (v == null || v.trim().isEmpty())
            return 0L;
        try {
            return DataSize.parse(v).toBytes();
        } catch (Exception ignore) {
            try {
                return Long.parseLong(v.trim());
            } catch (Exception e) {
                return 0L;
            }
        }
    }

    /*
     * ===========================
     * Public API (기존 시그니처 호환)
     * ===========================
     */

    // 다중 파일 업로드 (비고 없음) - 기본 타입 "I"(file_id.원래확장자)
    public List<CoFileVO> parseFileInf(Map<String, MultipartFile> files,
            String atchDocId,
            String pathKey,
            String ss_user_id) throws Exception {
        return parseFileInf(files, atchDocId, pathKey, ss_user_id, "I");
    }

    // 다중 파일 업로드 (비고 없음, 타입 지정: O/C/I)
    public List<CoFileVO> parseFileInf(Map<String, MultipartFile> files,
            String atchDocId,
            String pathKey,
            String ss_user_id,
            String type) throws Exception {
        List<CoFileVO> result = new ArrayList<>();
        if (files == null || files.isEmpty())
            return result;

        for (Map.Entry<String, MultipartFile> e : files.entrySet()) {
            MultipartFile file = e.getValue();
            if (file == null || file.isEmpty())
                continue;
            result.add(chgSaveCoFileVO(file, atchDocId, pathKey, ss_user_id, "", type));
        }
        return result;
    }

    // 단일 파일 업로드 (비고 없음)
    public CoFileVO parseFileInf(MultipartFile file,
            String atchDocId,
            String pathKey,
            String ss_user_id) throws Exception {
        return chgSaveCoFileVO(file, atchDocId, pathKey, ss_user_id, "", "I");
    }

    // 단일 파일 업로드 (비고 있음)
    public CoFileVO chgSaveCoFileVO(MultipartFile file,
            String atchDocId,
            String pathKey,
            String ss_user_id,
            String file_rmk) throws Exception {
        return chgSaveCoFileVO(file, atchDocId, pathKey, ss_user_id, file_rmk, "I");
    }

    /**
     * 단일 파일 업로드 (비고/타입 지정)
     * type:
     * - O: 원래파일명 그대로 저장
     * - C: file_id + Globals.FILE_EXT_C (예: .file)
     * - I: file_id + 원래 확장자 ← 기본
     */
    public CoFileVO chgSaveCoFileVO(MultipartFile file,
            String atchDocId,
            String pathKey,
            String ss_user_id,
            String file_rmk,
            String type) throws Exception {

        if (file == null || file.isEmpty())
            throw new IllegalArgumentException("file is empty");
        if (CoStringUtils.nvl(pathKey).isEmpty())
            throw new IllegalArgumentException("pathKey is empty");

        FilePathResolver.Storage storage = resolver.resolve(pathKey);

        // 날짜 폴더 (yyyyMMdd)
        String dateFolder = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        // 절대 저장 경로 (예: E:/uploads/board/yyyyMMdd)
        Path absDir = Paths.get(storage.getUploadDir(), dateFolder);
        Files.createDirectories(absDir);

        String rawOriginalName = file.getOriginalFilename();
        String originalName = sanitizeFilename(rawOriginalName);
        String fileId = SysUtil.getFileId();
        String contentType = file.getContentType();
        long size = file.getSize();

        String ext = getExt(rawOriginalName);

        // 저장 파일명
        String storeName;
        if ("O".equalsIgnoreCase(type)) {
            storeName = originalName;
        } else if ("C".equalsIgnoreCase(type)) {
            storeName = fileId + FILE_EXT_C;
        } else { // "I" default
            storeName = fileId + (ext.isEmpty() ? "" : "." + ext);
        }

        // 저장
        Path target = absDir.resolve(storeName);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        // VO 세팅
        CoFileVO fvo = new CoFileVO();
        fvo.setFile_id(fileId);
        fvo.setDoc_id(atchDocId);
        fvo.setFile_rmk(file_rmk);
        fvo.setFile_nm(originalName);
        fvo.setFile_aslt_path(absDir.toString() + File.separator); // 절대 경로(끝에 / 유지)
        fvo.setFile_rltv_path(storage.getPublicUrl() + dateFolder + "/"); // 공개 URL(끝에 / 유지)
        fvo.setFile_size(size);
        fvo.setSs_user_id(ss_user_id);
        fvo.setContent_type(contentType);
        fvo.setFile_ext_nm(ext);

        return fvo;
    }

    /*
     * ===========================
     * 삭제
     * ===========================
     */

    public boolean deleteFile(List<CoFileVO> list) throws Exception {
        if (list == null || list.isEmpty())
            return true;
        boolean ok = true;
        for (CoFileVO fvo : list)
            ok &= deleteFile(fvo);
        return ok;
    }

    // 타입 지정 리스트 삭제 (O/C/I)
    public boolean deleteFile(List<CoFileVO> list, String type) throws Exception {
        if (list == null || list.isEmpty())
            return true;
        boolean ok = true;
        for (CoFileVO fvo : list)
            ok &= deleteFile(fvo, type);
        return ok;
    }

    // 타입 지정 단건 삭제 (O/C/I)
    public boolean deleteFile(CoFileVO fvo, String type) throws Exception {
        if (fvo == null)
            return false;
        String t = (type == null) ? "" : type.trim().toUpperCase(Locale.ROOT);

        Path base = Paths.get(String.valueOf(fvo.getFile_aslt_path()));

        String fileId = String.valueOf(fvo.getFile_id());
        String ext = String.valueOf(fvo.getFile_ext_nm());
        String origin = sanitizeFilename(String.valueOf(fvo.getFile_nm()));

        Path target;
        switch (t) {
            case "O":
                target = base.resolve(origin);
                break;
            case "C":
                target = base.resolve(fileId + FILE_EXT_C);
                break;
            case "I":
            default:
                target = base.resolve(fileId + (ext.isEmpty() ? "" : "." + ext));
                break;
        }

        try {
            return Files.deleteIfExists(target);
        } catch (IOException e) {
            log.warn("Delete failed: " + target + " - " + e.getMessage());
            return false;
        }
    }

    // 타입 모를 때 자동 시도(I -> C -> O)
    public boolean deleteFile(CoFileVO fvo) throws Exception {
        Path base = Paths.get(fvo.getFile_aslt_path());
        String ext = CoStringUtils.nvl(fvo.getFile_ext_nm());
        String fileId = fvo.getFile_id();
        String origin = sanitizeFilename(fvo.getFile_nm());

        Path pI = base.resolve(fileId + (ext.isEmpty() ? "" : "." + ext));
        Path pC = base.resolve(fileId + FILE_EXT_C);
        Path pO = base.resolve(origin);

        return deleteIfExists(pI) || deleteIfExists(pC) || deleteIfExists(pO);
    }

    private boolean deleteIfExists(Path p) {
        try {
            return Files.deleteIfExists(p);
        } catch (IOException e) {
            log.warn("Delete failed: " + p + " - " + e.getMessage());
            return false;
        }
    }

    /*
     * ===========================
     * 검증/유틸
     * ===========================
     */

    // 총 용량(요청 내 모든 파일 합계) 체크
    public boolean checkFileSize(List<MultipartFile> files) {
        long maxBytes = toBytes(maxSizeTotalConf);
        long sum = 0;
        for (MultipartFile mf : files) {
            if (mf != null && !mf.isEmpty())
                sum += mf.getSize();
        }
        return sum <= maxBytes;
    }

    /**
     * @deprecated propertyName은 사용하지 않습니다. Spring 설정(file.max-size-total)을 사용합니다.
     */
    @Deprecated
    public boolean checkFileSize(List<MultipartFile> files, String propertyName) {
        return checkFileSize(files);
    }

    // 개별 파일 용량 체크
    public boolean checkEachFileSize(List<MultipartFile> files) {
        long maxEach = toBytes(maxSizeEachConf);
        for (MultipartFile mf : files) {
            if (mf != null && !mf.isEmpty() && mf.getSize() > maxEach)
                return false;
        }
        return true;
    }

    /**
     * @deprecated propertyName은 사용하지 않습니다. Spring 설정(file.max-size-each)을 사용합니다.
     */
    @Deprecated
    public boolean checkEachFileSize(List<MultipartFile> files, String propertyName) {
        return checkEachFileSize(files);
    }

    // 업로드 파라미터에서 빈 파일 제거하여 가져오기
    public static List<MultipartFile> getFiles(MultipartHttpServletRequest request) {
        return getFiles(request, "upload");
    }

    public static List<MultipartFile> getFiles(MultipartHttpServletRequest request, String name) {
        List<MultipartFile> out = new ArrayList<>();
        if (name == null)
            return out;
        List<MultipartFile> in = request.getFiles(name);
        for (MultipartFile mf : in) {
            if (mf != null && !mf.isEmpty())
                out.add(mf);
        }
        return out;
    }

    // 확장자 블록 리스트 체크
    public String checkFileExt(List<MultipartFile> files) {
        String exceptExtNames = "EXE,BAT,COM,JSP,ASP,HTML,PHP,SH";
        String checkMsg = "";
        for (MultipartFile mf : files) {
            if (mf != null && !mf.isEmpty()) {
                String ext = getExt(mf.getOriginalFilename()).toUpperCase(Locale.ROOT);
                if (exceptExtNames.contains(ext)) {
                    checkMsg = messageSource.getMessage("error.file.ext", new String[] { ext });
                    break;
                }
            }
        }
        return checkMsg;
    }

    // 허용 확장자 화이트리스트 체크 (대문자 CSV 문자열)
    public String checkFileAcceptExt(List<MultipartFile> files, String acceptExtCsvUppercase) {
        String checkMsg = "";
        for (MultipartFile mf : files) {
            if (mf != null && !mf.isEmpty()) {
                String ext = getExt(mf.getOriginalFilename()).toUpperCase(Locale.ROOT);
                if (!acceptExtCsvUppercase.contains(ext)) {
                    checkMsg = messageSource.getMessage("error.file.ext", new String[] { ext });
                    break;
                }
            }
        }
        return checkMsg;
    }

    // 이미지 해시
    public String getImageHash(String filePath) throws Exception {
        String hashStr = "";
        File mfile = new File(filePath);
        if (mfile.exists()) {
            BufferedImage bufImg = ImageIO.read(mfile);
            try (java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream()) {
                ImageIO.write(bufImg, "JPG", outputStream);
                byte[] data = outputStream.toByteArray();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(data);
                byte[] hash = md.digest();
                hashStr = returnHex(hash);
            }
        }
        return hashStr;
    }

    // 파일 해시(호환성 위해 이미지 방식 재사용)
    public String getFileHash(String filePath) throws Exception {
        return getImageHash(filePath);
    }

    public String returnHex(byte[] inBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : inBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String checksum(String filepath, MessageDigest md) throws IOException {
        try (InputStream is = Files.newInputStream(Paths.get(filepath))) {
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        }
    }

    public static String checksumString(String fileStr, MessageDigest md) {
        md.update(fileStr.getBytes());
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest())
            result.append(String.format("%02x", b));
        return result.toString();
    }

    /*
     * ===========================
     * 내부 헬퍼
     * ===========================
     */

    private static String getExt(String filename) {
        if (filename == null)
            return "";
        String name = filename.trim();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1)
            return "";
        return name.substring(dot + 1)
                .replaceAll("[^A-Za-z0-9]", "")
                .toLowerCase(Locale.ROOT);
    }

    private static String sanitizeFilename(String filename) {
        if (filename == null)
            return "file";
        // Windows path separator handling
        String name = filename.replace("\\", "/");
        // Extract only the filename
        name = name.substring(name.lastIndexOf('/') + 1);
        // Remove control characters (fixed bug where 'r', 'n', 't' were removed)
        return name.replaceAll("[\\r\\n\\t]", "_");
    }

    @SuppressWarnings("unused")
    private static String getTimeStampLegacy() {
        String pattern = "yyyyMMdd";
        SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return sdfCurrent.format(ts.getTime());
    }

    protected static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException ignore) {
            }
        }
    }

    public String humanReadable(String sizeOrDataSize) {
        long bytes = toBytes(sizeOrDataSize); // "10MB" 또는 "10485760" 모두 처리
        if (bytes <= 0)
            return "0B";
        final String[] u = { "B", "KB", "MB", "GB", "TB" };
        int i = (int) Math.floor(Math.log(bytes) / Math.log(1024));
        double v = bytes / Math.pow(1024, i);
        return String.format(Locale.KOREA, "%.1f%s", v, u[i]);
    }
}
