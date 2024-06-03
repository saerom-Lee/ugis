package ugis.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import cool.graph.cuid.Cuid;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import ugis.exception.RestApiException;
import ugis.service.vo.CISC010VO.TnCmmnFile;
import ugis.util.Util;

@Service
public class CmmnFileService extends EgovAbstractServiceImpl {

    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

//    @Value("${ugis.file.ai.path}")
//    private String aiPath;

    @Value("${ugis.file.ai.db.path}")
    private String aiDbPath;

//    @Value("${ugis.file.obj.path}")
//    private String objPath;

    @Value("${ugis.file.obj.db.path}")
    private String objDbPath;

//    @Value("${ugis.file.chg.path}")
//    private String chgPath;

    @Value("${ugis.file.chg.db.path}")
    private String chgDbPath;

    @Value("${file.upload.tempPath}")
    private String tempPath;

    @Value("${file.temp.clear-ago}")
    private int tempClearDayAgo;

    @Value("#{'${file.extensions}'.split(',')}")
    private List<String> allowExtensions;

    public List<TnCmmnFile> saveFile(MultipartFile file) throws Exception {

        if(file == null) {
            throw new RestApiException("Could not find file.");
        }

        List<TnCmmnFile> result = new ArrayList<>();

        String cuid = Cuid.createCuid();
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String path = getSaveFilePath() + File.separator + today + File.separator + cuid;
        String dbPath = getSaveFilePath() + File.separator + today + File.separator + cuid;

        String fileName = StringUtils.getFilename(StringUtils.cleanPath(file.getOriginalFilename()));

        String fileExtension = this.getExtension(fileName);
        if(!"zip".equals(fileExtension.toLowerCase())) {
            throw new RestApiException("zip 파일만 등록 가능 합니다.");
        }

        try {

            File tempDir = new File(dbPath);
            if(!tempDir.isDirectory()) {
                tempDir.mkdirs();
            }

            String savePath = tempDir.getAbsolutePath() + File.separator + fileName;
            file.transferTo(new File(savePath));

            this.uncompressZip(savePath, dbPath, "");

            File deleteFile = new File(savePath);
            if(deleteFile.exists()) {
            	deleteFile.delete();
            }

            List<Map<String, Object>> fileNameList = this.getUncompressFullFile(file.getBytes());

            for(Map<String, Object> map : fileNameList) {

                result.add(
                    TnCmmnFile.builder()
                    .fileNm(FilenameUtils.getBaseName(String.valueOf(map.get("fileNm"))))
                    .allFileNm(dbPath + File.separator + String.valueOf(map.get("fileNm")))
                    .fileCoursNm(dbPath)
                    .fileExtsnNm(String.valueOf(map.get("fileExtsnNm")))
                    .fileSize(Long.valueOf(String.valueOf(map.get("fileSize"))))
                    .build()
                );

            }

        } catch (IllegalStateException e) {
            throw new RestApiException("failed to upload file.");
        }

        return result;

    }

    public String getExtrlFileCoursNm(String filePath) {

        if ("".equals(filePath)) return "";

//        Path source = Paths.get(filePath);
//        String targetPath = objPath + File.separator + source.getFileName();
//        Path target = Paths.get(targetPath);

        File fromFile = new File(filePath);
        String targetPath = objDbPath + File.separator;
        File toDir = new File(targetPath);
        File toFile = new File(targetPath + fromFile.getName());

        String targetDbPath = objDbPath + File.separator + fromFile.getName();//DB 저장경로

        if (fromFile.isFile()) {
            try {
//                Files.createDirectories(target.getParent());
//                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            	if(!toDir.isDirectory()) {
            		toDir.mkdirs();
            	}

				FileUtils.copyFile(fromFile, toFile);

            }
            catch (NullPointerException e) {
                throw new RestApiException("[" + toFile.getPath() + "] 파일 복사 중 에러가 발생하였습니다.", e);
            }
            catch (Exception e) {
                throw new RestApiException("[" + toFile.getPath() + "] 파일 복사 중 에러가 발생하였습니다.", e);
            }
        } else {
            throw new RestApiException("[" + fromFile.getPath() + "] 파일이 존재 하지 않습니다.");
        }

        return targetDbPath;
    }

    public String getChgExtrlFileCoursNm(String filePath) {

        if ("".equals(filePath)) return "";

        File fromFile = new File(filePath);
        String targetPath = chgDbPath + File.separator;
        File toDir = new File(targetPath);
        File toFile = new File(targetPath + fromFile.getName());

        String targetDbPath = chgDbPath + File.separator + fromFile.getName();//DB 저장경로

        if (fromFile.isFile()) {
            try {
            	if(!toDir.isDirectory()) {
            		toDir.mkdirs();
            	}

				FileUtils.copyFile(fromFile, toFile);

            } catch (Exception e) {
                throw new RestApiException("[" + toFile.getPath() + "] 파일 복사 중 에러가 발생하였습니다.", e);
            }
        } else {
            throw new RestApiException("[" + fromFile.getPath() + "] 파일이 존재 하지 않습니다.");
        }

        return targetDbPath;
    }

    public String uploadTempFile(MultipartFile file) throws Exception {

        if(file == null) {
            throw new RestApiException("Could not find file.");
        }

        String fileName = StringUtils.getFilename(StringUtils.cleanPath(file.getOriginalFilename()));

        String fileExtension = this.getExtension(fileName);
        if(allowExtensions.indexOf(fileExtension.toLowerCase()) < 0) {
            throw new RestApiException("This file format is Not Allowed.");
        }

        String cuid = Cuid.createCuid();
        String savePath = "";

        try {

            this.cleanTempFile(tempClearDayAgo);

            File tempDir = new File(getTempFilePath() + File.separator + cuid);
            if(!tempDir.isDirectory()) {
                tempDir.mkdirs();
            }

            savePath = tempDir.getAbsolutePath() + File.separator + fileName;
            file.transferTo(new File(savePath));

        } catch (IllegalStateException e) {
            throw new RestApiException("failed to upload file.");
        } catch (IOException e) {
            throw new RestApiException("failed to upload file.");
        }

        return savePath;

    }

    public void cleanTempFile(int clearDayAgo) {
        Calendar calendar = Calendar.getInstance();
        long todayMillis = calendar.getTimeInMillis();
        long oneDaymillis = TimeUnit.MILLISECONDS.convert(1,  TimeUnit.DAYS);

        Calendar fileCalendar = Calendar.getInstance();
        Date fileDate = null;

        File path = new File(this.getTempFilePath());
        File[] listOfFiles = path.listFiles();
        if(listOfFiles == null) {
            return;
        }

        for(int i=0; i < listOfFiles.length; i++) {
            if(!listOfFiles[i].exists()) {
                continue;
            }

            // 파일의 마지막 수정시간
            fileDate = new Date(listOfFiles[i].lastModified());

            // 현재시간과 파일 수정시간 시간차 계산
            fileCalendar.setTime(fileDate);
            long diffMillis = todayMillis - fileCalendar.getTimeInMillis();

            // 날짜로 계산
            int diffDay = (int) (diffMillis / oneDaymillis);

            // 지난 파일 삭제
            if(diffDay >= clearDayAgo) {
                try {
                    FileUtils.forceDelete(listOfFiles[i]);
                } catch (IOException e) {}
            }
        }
    }

    public File downloadFile(String filePathWithName) {
        File file = new File(getSaveFilePath() + File.separator + filePathWithName);
        if(!file.exists()) {
            return null;
        }
        if(file.isDirectory()) {
            return null;
        }

        return file;
    }

    public List<String> getUncompressFile(byte[] byteFile) throws Exception {

        List<String> result = new ArrayList<>();
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(byteFile), Charset.forName("UTF-8"));

        try {

            ZipEntry entry = null;

            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    result.add(FilenameUtils.getBaseName(entry.getName()));
                }
            }

        } catch (IOException e) {
            throw new RestApiException("failed to upload file.");
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    throw new RestApiException("failed to upload file.");
                }
            }
        }

        return result;

    }

    public List<Map<String, Object>> getUncompressFullFile(byte[] byteFile) throws Exception {

        List<Map<String, Object>> result = new ArrayList<>();
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(byteFile), Charset.forName("UTF-8"));

        try {

            ZipEntry entry = null;

            Map<String, Object> map = null;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                	map = new HashMap<>();
                    map.put("fileNm", entry.getName());
                    map.put("fileExtsnNm", this.getExtension(entry.getName()));
                    map.put("fileSize", entry.getSize());
                    result.add(map);
                }
            }

        } catch (IOException e) {
            throw new RestApiException("failed to upload file.");
        } finally {
            if(zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    throw new RestApiException("failed to upload file.");
                }
            }
        }

        return result;

    }

    public void uncompressZip(String file, String destination, String password) {

		ZipFile zipFile = null;
		try {

//			zipFile = new ZipFile(file);
//			if (zipFile.isEncrypted()) {
//				zipFile.setPassword(password.toCharArray());
//			}
//			zipFile.extractAll(destination);

			zipFile = new ZipFile(file);
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(password.toCharArray());
			}
	        List<FileHeader> fileHeaderList = zipFile.getFileHeaders();
	        UnzipParameters param = new UnzipParameters();
	        param.setExtractSymbolicLinks(false);
	        for (int i = 0; i < fileHeaderList.size(); i++) {
	            FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
	            String fileName = fileHeader.getFileName();

	            if(fileName.contains("/") || fileName.contains("\\")) {
	            	throw new RestApiException("[" + FilenameUtils.getName(file) + "] 압축 파일에 폴더가 포함되어 있습니다. 파일만 압축해 주세요.");
	            }

	            String fileExtension = this.getExtension(fileName);
	            if(!"tif".equals(fileExtension.toLowerCase())) {
	                throw new RestApiException("tif 파일만 등록 가능 합니다.");
	            }

                zipFile.extractFile(fileHeader, destination, param);
	        }
		} catch (ZipException e) {
			e.printStackTrace();
		} finally {
			if(zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

    private String getSaveFilePath() {
        if(StringUtils.isEmpty(aiDbPath)) {
            return System.getProperty(JAVA_IO_TMPDIR);
        }

        return Util.removeLastSeperator(aiDbPath);
    }

    private String getTempFilePath() {
        if(StringUtils.isEmpty(tempPath)) {
            return System.getProperty(JAVA_IO_TMPDIR);
        }

        return Util.removeLastSeperator(tempPath);
    }

    private String getExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf('.');
        if(lastIndexOf < 0) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }

}
