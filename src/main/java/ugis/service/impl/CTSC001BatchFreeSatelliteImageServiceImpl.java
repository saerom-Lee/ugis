package ugis.service.impl;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.service.CTSC001Service;
import ugis.service.vo.CTSC001VO;
import ugis.service.vo.CTSC001VOInsertVO;
import ugis.service.vo.SatelliteImageMeta;
import ugis.util.UnzipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component("ctsc001BatchFreeSatelliteImageServiceImpl")
public class CTSC001BatchFreeSatelliteImageServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CTSC001BatchFreeSatelliteImageServiceImpl.class);
    static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    @Autowired
    private ugis.util.fileUtil fileUtil;
    @Resource(name = "ctsc001Service")
    private CTSC001Service<CTSC001VO> service;

    @Value("#{fileProperties['satellite.search.url']}")
    private String satelliteSearchUrl;

    @Value("#{fileProperties['satellite.download.url']}")
    private String satelliteDownloadUrl;

    @Value("#{fileProperties['satellite.remove.url']}")
    private String satelliteRemoveUrl;

    @Value("#{fileProperties['satellite.removes.url']}")
    private String satelliteRemovesUrl;

    @Value("#{fileProperties['satellite.query.url']}")
    private String satelliteQueryurl;
    private CTSC001VO ctsc001VO;

    private static final int BUUFER_SIZE = 4096;

    private static final String MetaDataFileName = "metadata.json";

    public void startDownload(CTSC001VO ctsc001VO) {
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                downloadSatelliteImage(ctsc001VO);
//            }
//        });
        Thread satelliteDownloadThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        downloadSatelliteImage(ctsc001VO);
                    }
                }
        );
        satelliteDownloadThread.start();
    }
    public void setCTSC001VO(CTSC001VO vo) {
        this.ctsc001VO = vo;
    }
    public boolean downloadSatelliteImage(CTSC001VO ctsc001VO) {
        LOGGER.debug("satellite image download start");

        if(!requestSatelliteImage(ctsc001VO)) {
            LOGGER.warn("satellite image request failure");
            return false;
        }

        String[] satelliteImageFiles = requestSatelliteImageFiles();
        Arrays.stream(satelliteImageFiles).forEach(satelliteImageFileName -> {
            String downloadSatelliteFileName = downloadSatelliteImage(satelliteImageFileName, ctsc001VO);
            if(downloadSatelliteFileName == null || downloadSatelliteFileName.length() ==0) {
                return;
            }
            extractSatelliteImge(Paths.get(downloadSatelliteFileName), ctsc001VO);
        });
        removeSatelliteImage();

        return true;
    }


    private boolean requestSatelliteImage(CTSC001VO ctsc001VO) {
        try {
            String param = buildParemeter(ctsc001VO);
            URL url = new URL(satelliteSearchUrl + "?" + param);
            LOGGER.debug("satellite  image request url :" + url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }
            return true;
        } catch (MalformedURLException e) {
            e.getStackTrace();
            return false;
        } catch (IOException e) {
            e.getStackTrace();
            return false;
        }
    }

    private String[] requestSatelliteImageFiles() {
        RestTemplate restTemplate = new RestTemplate();
        final String[] files = restTemplate.getForObject(satelliteQueryurl, String[].class);
        Arrays.stream(files).forEach(file -> {
            LOGGER.debug("satellite image download file : " + file);
        });
        return files;
    }

    private String downloadSatelliteImage(String fileName, CTSC001VO ctsc001VO) {
        LOGGER.debug("satellite image download start : " + fileName);
        String incommingPath = ctsc001VO.getSatlitVidoInnerPath();
        try {
            URL urlDownloadFile = new URL(satelliteDownloadUrl + "?fileName=" + fileName);
            HttpURLConnection connection = (HttpURLConnection) urlDownloadFile.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = connection.getInputStream();
            String saveFilePath = Paths.get(incommingPath, fileName).toString();
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            int bytesRead = -1;
            byte[] buffer = new byte[BUUFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            LOGGER.debug("satellite image download end : " + saveFilePath);
            return saveFilePath;
        } catch (MalformedURLException e) {
            e.getStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.getStackTrace();
            return null;
        } catch (IOException e) {
            e.getStackTrace();
            return null;
        }
    }

    private boolean updateDB(CTSC001VOInsertVO vo, String targetFolder, String imageFileName) {
        try {
            LOGGER.info("satellite image insert db: " + imageFileName);
            int maxVidoId = service.selectMaxVidoId();
            vo.setVIDO_ID(maxVidoId+1);
            vo.setImageFileName(targetFolder, imageFileName);
            service.insertUsgsMetaEssential(vo);
            service.insertTnUsgsEssential(vo);

            return true;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }

    private boolean extractSatelliteImge(Path sourceZip, CTSC001VO ctsc001VO) {
        try {
            Path metadataFileName = UnzipFile.unzipByFileName(sourceZip, MetaDataFileName);

            Gson gson = new Gson();
            Reader reader = new FileReader(metadataFileName.toString());
            CTSC001VOInsertVO vo = gson.fromJson(reader, CTSC001VOInsertVO.class);
            reader.close();
            boolean result = Files.deleteIfExists(metadataFileName);
            if(result == false) {
                LOGGER.debug("meta file delete failure", metadataFileName.toString());
            }

            String extractPath = vo.getExtract_Path();
            List<SatelliteImageMeta> satelliteImageMetas = vo.getSatelliteImageMetas();

            String targetFolder = sourceZip.toString().substring(0, sourceZip.toString().length()-11);
            UnzipFile.unzipFile(sourceZip, Paths.get(extractPath, targetFolder));
            satelliteImageMetas.forEach(satelliteImageMeta -> updateDB(vo, targetFolder, satelliteImageMeta.getFileName()));

            return true;
        } catch (IOException e) {
            e.getStackTrace();
            return false;
        }
    }

    private boolean removeSatelliteImage() {
//        RestTemplate restTemplate = new RestTemplate();
//        String urlDownloadLists = satelliteRemovesUrl;
//        final Boolean result = restTemplate.getForObject(satelliteQueryurl, Boolean.class);
//        return result;

        try {
            URL url = new URL(satelliteRemovesUrl);
            LOGGER.debug("satellite  image remove all url :" + url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            if(connection.getResponseMessage().compareToIgnoreCase("true") == 0){
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
            return false;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    private String buildParemeter(CTSC001VO ctsc001VO) {
        StringBuilder sb = new StringBuilder();

        if(ctsc001VO.getPotogrf_vido_nm().indexOf("landsat") > -1){
            ctsc001VO.setPotogrfVidoCd("1");
        }
        else{
            ctsc001VO.setPotogrfVidoCd("6");
        }

        sb.append(URLEncoder.encode("datasetName") + "=");
        sb.append(ctsc001VO.getPotogrf_vido_nm());

        sb.append("&");
        sb.append(URLEncoder.encode("llx") + "=");
        sb.append(ctsc001VO.getUlx());

        sb.append("&");
        sb.append(URLEncoder.encode("lly") + "=");
        sb.append(ctsc001VO.getLry());

        sb.append("&");
        sb.append(URLEncoder.encode("rux")  + "=");
        sb.append(ctsc001VO.getLrx());

        sb.append("&");
        sb.append(URLEncoder.encode("ruy")   + "=");
        sb.append(ctsc001VO.getUly());

        sb.append("&");
        sb.append(URLEncoder.encode("startDate")   + "=");
        sb.append(ctsc001VO.getPotogrfBeginDt());

        sb.append("&");
        sb.append(URLEncoder.encode("endDate")   + "=");
        sb.append(ctsc001VO.getPotogrfEndDt());

        sb.append("&");
        sb.append(URLEncoder.encode("incommingPath")   + "=");
        sb.append(URLEncoder.encode(ctsc001VO.getSatlitVidoInnerPath()));

        sb.append("&");
        sb.append(URLEncoder.encode("outgoingPath")   + "=");
        sb.append(URLEncoder.encode(ctsc001VO.getSatlitVidoExtrlPath()));

        sb.append("&");
        sb.append(URLEncoder.encode("jobTime")   + "=");
        sb.append(URLEncoder.encode(ctsc001VO.getDwldDate()));

        return sb.toString();
    }
}
