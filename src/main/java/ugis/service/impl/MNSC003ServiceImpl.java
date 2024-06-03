package ugis.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.MNSC003Service;
import ugis.service.mapper.MNSC003Mapper;
import ugis.util.HttpDownloader;

@Service("mnsc003Service")
public class MNSC003ServiceImpl extends EgovAbstractServiceImpl implements MNSC003Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	//
	@Resource(name = "mnsc003Mapper")
	private MNSC003Mapper mnsc003Mapper;

	@Value("${ugis.http.header.boundary}")
	private String httpBoundary;

	@Value("${ugis.gfra.download.path}")
	private String gFraDownPath;

	@Value("${ugis.gfra.url}")
	private String gFraUrl;

	@Override
	public JSONObject getAirThumbnail(JSONObject obj) {

		// 파일명
		String fileNam = (String) obj.get("FILE_NAM");

		// 경로 구성 Param
		String folderNam = (String) obj.get("FOLDER_NAM");
		String zoneCode = (String) obj.get("ZONE_CODE");

		String zoneFCode = "0000";
		String zoneLCode = "0000";
		if (zoneCode != null) {
			zoneFCode = zoneCode.substring(0, 4);
			zoneLCode = zoneCode.substring(7, 11);
		}
		String phCourse = (String) obj.get("PH_COURSE");
		String origin = (String) obj.get("ORIGIN");
		System.out.println(">>>>>>>>>>>>>> ORIGIN : " + origin);
		// 저장경로
		StringBuilder filePath = new StringBuilder();
		StringBuilder dir = new StringBuilder();
		dir.append(folderNam);
		dir.append("/");
		dir.append(zoneFCode);
		dir.append("/");
		dir.append(zoneLCode);
		dir.append("/");
		dir.append(phCourse);
		dir.append("/");

		filePath.append(dir);
		filePath.append(fileNam + "." + "jpg");
		String repFilePath = filePath.toString();

		// 썸네일 다운로드
		String gfraThumbPath = repFilePath.replace("/air/", "air_jpg/");
		String downloadPath = gFraDownPath + gfraThumbPath; // ex)
															// /home/TempData/01_GeoFraData/air_jpg/rgb/1966/0001/C/1966000001000C3600.jpg
		File downloadFile = new File(downloadPath);
		if (!downloadFile.exists()) {
			// download file
			HttpDownloader downloader = new HttpDownloader();
			downloader.download(httpBoundary, gFraUrl, downloadPath, gfraThumbPath);
		}
		if (!downloadFile.exists()) { // s.tif
			String sgfraThumbPath = gfraThumbPath.replace(".jpg", "s.jpg");
			String sdownloadPath = gFraDownPath + sgfraThumbPath;
			downloadPath = sdownloadPath;

			// download file
			HttpDownloader downloader = new HttpDownloader();
			downloader.download(httpBoundary, gFraUrl, sdownloadPath, sgfraThumbPath);
			downloadFile = new File(downloadPath);
		}
		if (downloadFile.exists()) {
			// 좌표추출
			String minx = String.valueOf(obj.get("XMIN"));
			String miny = String.valueOf(obj.get("YMIN"));
			String maxY = String.valueOf(obj.get("YMAX"));
			String maxX = String.valueOf(obj.get("XMAX"));

			// 좌표계
//			String mapPrjctnCn = "";
//			if (origin.contentEquals("중부")) {
//				mapPrjctnCn = "EPSG:5186";
//				obj.put("ulx5186", minx);
//				obj.put("uly5186", maxY);
//				obj.put("lrx5186", maxX);
//				obj.put("lry5186", miny);
//			} else if (origin.contentEquals("서부")) {
//				mapPrjctnCn = "EPSG:5185";
//				obj.put("ulx5185", minx);
//				obj.put("uly5185", maxY);
//				obj.put("lrx5185", maxX);
//				obj.put("lry5185", miny);
//			} else if (origin.contentEquals("동부")) {
//				mapPrjctnCn = "EPSG:5187";
//				obj.put("ulx5187", minx);
//				obj.put("uly5187", maxY);
//				obj.put("lrx5187", maxX);
//				obj.put("lry5187", miny);
//			}
			obj.put("mapPrjctnCn", "EPSG:5179");
			obj.put("ulx5179", minx);
			obj.put("uly5179", maxY);
			obj.put("lrx5179", maxX);
			obj.put("lry5179", miny);
			obj.put("thumbnailFileCoursNm", downloadPath);
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public JSONObject getOrtThumbnail(JSONObject obj) {
		// 파일명
		String fileNam = (String) obj.get("FILE_NAM");
		String zoneYy = String.valueOf(obj.get("ZONE_YY"));

		// 경로 구성 Param
		String mapNum = (String) obj.get("MAP_NUM");
		String origin = (String) obj.get("ORIGIN");
		String folderNam = (String) obj.get("FOLDER_NAM");
		String zoneCode = (String) obj.get("ZONE_CODE");

		String zoneLCode = "0000";
		if (zoneCode != null) {
			zoneLCode = zoneCode.substring(7, 11);
		}
		System.out.println(">>>>>>>>>>>>>> ORIGIN : " + origin);
		if (origin.contentEquals("중부")) {
			origin = "cent";
		} else if (origin.contentEquals("서부")) {
			origin = "west";
		} else if (origin.contentEquals("동부")) {
			origin = "east";
		} else {
			origin = "esea";
		}
		StringBuilder filePath = new StringBuilder();

		// 저장경로
		StringBuilder dir = new StringBuilder();
		dir.append(folderNam);
		dir.append("/");
		dir.append(zoneYy);
		dir.append("/");
		dir.append(zoneLCode);
		dir.append("/");
		dir.append(origin);
		dir.append("/");
		dir.append(mapNum);
		dir.append("/");

		filePath.append(dir);
		filePath.append(fileNam + "." + "jpg");
		String repFilePath = filePath.toString();

		// 썸네일 다운로드
		String gfraThumbPath = repFilePath.replace("/ort/", "ort_jpg/");
		String downloadPath = gFraDownPath + gfraThumbPath; // ex)
															// /home/TempData/01_GeoFraData/air_jpg/rgb/1966/0001/C/1966000001000C3600.jpg
		File downloadFile = new File(downloadPath);
		if (!downloadFile.exists()) {
			// download file
			HttpDownloader downloader = new HttpDownloader();
			downloader.download(httpBoundary, gFraUrl, downloadPath, gfraThumbPath);
		}
		if (!downloadFile.exists()) { // s.tif
			String sgfraThumbPath = gfraThumbPath.replace(".jpg", "s.jpg");
			String sdownloadPath = gFraDownPath + sgfraThumbPath;

			downloadPath = sdownloadPath;
			// download file
			HttpDownloader downloader = new HttpDownloader();
			downloader.download(httpBoundary, gFraUrl, sdownloadPath, sgfraThumbPath);
			
			downloadFile = new File(downloadPath);
		}
		if (downloadFile.exists()) {
			// 좌표추출
			String minx = String.valueOf(obj.get("XMIN"));
			String miny = String.valueOf(obj.get("YMIN"));
			String maxY = String.valueOf(obj.get("YMAX"));
			String maxX = String.valueOf(obj.get("XMAX"));

			// 좌표계
			String mapPrjctnCn = "";
			if (origin.contentEquals("cent")) {
				mapPrjctnCn = "EPSG:5186";
				obj.put("ulx5186", minx);
				obj.put("uly5186", maxY);
				obj.put("lrx5186", maxX);
				obj.put("lry5186", miny);
			} else if (origin.contentEquals("west")) {
				mapPrjctnCn = "EPSG:5185";
				obj.put("ulx5185", minx);
				obj.put("uly5185", maxY);
				obj.put("lrx5185", maxX);
				obj.put("lry5185", miny);
			} else if (origin.contentEquals("east")) {
				mapPrjctnCn = "EPSG:5187";
				obj.put("ulx5187", minx);
				obj.put("uly5187", maxY);
				obj.put("lrx5187", maxX);
				obj.put("lry5187", miny);
			}
			obj.put("mapPrjctnCn", mapPrjctnCn);
			obj.put("thumbnailFileCoursNm", downloadPath);
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public JSONObject getDemThumbnail(JSONObject obj) {
		// 파일명
		String fileNam = (String) obj.get("FILE_NAM");
		String zoneYy = String.valueOf(obj.get("ZONE_YY"));

		String mapNum = (String) obj.get("MAP_NUM");
		String origin = (String) obj.get("ORIGIN");
		String folderNam = (String) obj.get("FOLDER_NAM");
		String zoneCode = (String) obj.get("ZONE_CODE");
		String zoneLCode = "0000";
		if (zoneCode != null) {
			zoneLCode = zoneCode.substring(7, 11);
		}
		System.out.println(">>>>>>>>>>>>>> ORIGIN : " + origin);
		if (origin.contentEquals("중부")) {
			origin = "cent";
		} else if (origin.contentEquals("서부")) {
			origin = "west";
		} else if (origin.contentEquals("동부")) {
			origin = "east";
		} else {
			origin = "esea";
		}

		StringBuilder filePath = new StringBuilder();

		// 저장경로
		StringBuilder dir = new StringBuilder();
		dir.append(folderNam);
		dir.append("/");
		dir.append(zoneYy);
		dir.append("/");
		dir.append(zoneLCode);
		dir.append("/");
		dir.append(origin);
		dir.append("/");
		dir.append(mapNum);
		dir.append("/");

		filePath.append(dir);
		filePath.append(fileNam + "s." + "jpg");
		String repFilePath = filePath.toString();

		// 썸네일 다운로드
		String gfraThumbPath = repFilePath.replace("/dem/", "dem_jpg/");
		String downloadPath = gFraDownPath + gfraThumbPath; // ex)
															// /home/TempData/01_GeoFraData/air_jpg/rgb/1966/0001/C/1966000001000C3600.jpg
		File downloadFile = new File(downloadPath);
		if (!downloadFile.exists()) {
			// download file
			HttpDownloader downloader = new HttpDownloader();
			downloader.download(httpBoundary, gFraUrl, downloadPath, gfraThumbPath);
		}
		if (!downloadFile.exists()) { // s.tif
			String sgfraThumbPath = gfraThumbPath.replace(".jpg", "s.jpg");
			String sdownloadPath = gFraDownPath + sgfraThumbPath;
			downloadPath = sdownloadPath;

			// download file
			HttpDownloader downloader = new HttpDownloader();
			downloader.download(httpBoundary, gFraUrl, sdownloadPath, sgfraThumbPath);
			downloadFile = new File(downloadPath);
		}
		if (downloadFile.exists()) {
			// 좌표추출
			String minx = String.valueOf(obj.get("XMIN"));
			String miny = String.valueOf(obj.get("YMIN"));
			String maxY = String.valueOf(obj.get("YMAX"));
			String maxX = String.valueOf(obj.get("XMAX"));

			// 좌표계
			String mapPrjctnCn = "";
			if (origin.contentEquals("cent")) {
				mapPrjctnCn = "EPSG:5186";
				obj.put("ulx5186", minx);
				obj.put("uly5186", maxY);
				obj.put("lrx5186", maxX);
				obj.put("lry5186", miny);
			} else if (origin.contentEquals("west")) {
				mapPrjctnCn = "EPSG:5185";
				obj.put("ulx5185", minx);
				obj.put("uly5185", maxY);
				obj.put("lrx5185", maxX);
				obj.put("lry5185", miny);
			} else if (origin.contentEquals("east")) {
				mapPrjctnCn = "EPSG:5187";
				obj.put("ulx5187", minx);
				obj.put("uly5187", maxY);
				obj.put("lrx5187", maxX);
				obj.put("lry5187", miny);
			}
			obj.put("mapPrjctnCn", mapPrjctnCn);
			obj.put("thumbnailFileCoursNm", downloadPath);
			return obj;
		} else {
			return null;
		}
	}
}
