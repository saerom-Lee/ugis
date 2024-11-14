package ugis.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.Hints;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataCropper;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.ShpDataReader;
import ugis.service.CISC001Service;
import ugis.service.CMSC003Service;
import ugis.service.mapper.CMSC003Mapper;
import ugis.service.othermapper.CMSC003UgisMapper;
import ugis.service.vo.CISC001WorkResultVO;
import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CMSC003DataSetVO;
import ugis.service.vo.CMSC003InsertVO;
import ugis.service.vo.CMSC003VO;
import ugis.service.vo.CMSC003VO2;
import ugis.service.vo.CMSC003VO3;
import ugis.service.vo.CMSC003VO4;
import ugis.service.vo.DataCodeVO;
import ugis.util.GeoManager;
import ugis.util.HttpDownloader;
import ugis.util.HttpGeoserverAPI;
import ugis.util.HttpUploader;

@Service("cmsc003Service")
public class CMSC003ServiceImpl extends EgovAbstractServiceImpl implements CMSC003Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	// postgresql 접근 mapper
	@Resource(name = "cmsc003Mapper")
	private CMSC003Mapper cmsc003Mapper;

	// oracle 접근 mapper
	@Resource(name = "cmsc003UgisMapper")
	private CMSC003UgisMapper cmsc003UgisMapper;

	@Resource(name = "cisc001Service")
	CISC001Service cisc001Service;

	@Value("${ugis.file.download.path}")
	private String rootPath;

	@Value("${ugis.objectext_shp.path}")
	private String shpPath;

	@Value("${ugis.file.path}")
	private String filePath2;

	@Value("${geoserver2.url}")
	private String geoserverUrl;

	@Value("${geoserver2.id}")
	private String geoserverId;

	@Value("${geoserver2.pw}")
	private String geoserverPw;

	@Value("${ugis.http.header.contenttype}")
	private String httpContentType;

	@Value("${ugis.http.header.boundary}")
	private String httpBoundary;

	@Value("${ugis.gfra.download.path}")
	private String gFraDownPath;

	@Value("${ugis.gfra.url}")
	private String gFraUrl;

	@Value("${ugis.dataset.url}")
	private String datasetUrl;

	@Value("${ugis.dataset.delete.url}")
	private String datasetDeleteUrl;

	@Value("${ugis.dataset.upload.path}")
	private String datasetUploadPath;

	@Value("${ugis.dataset.db.path}")
	private String datasetDbPath;

	public static int tifWidth = 1000;

	public static String defaultEPSG = "EPSG:5179";

	@Override
	public JSONArray selectDigitalMapList(CMSC003VO cmsc003VO) throws UnsupportedEncodingException, IOException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		JSONArray digitalArr = new JSONArray();

		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상

			String workspace = "vector"; // 연속수치지도 작업공간

			// 벡터데이터 연속수치지도
			// BBOX 데이터 생성
			double bbox[] = new double[4];

			if (StringUtils.isNotEmpty(cmsc003VO.getLrx5179())) {
				bbox[0] = Double.parseDouble(cmsc003VO.getLrx5179());
				bbox[1] = Double.parseDouble(cmsc003VO.getLry5179());
				bbox[2] = Double.parseDouble(cmsc003VO.getUlx5179());
				bbox[3] = Double.parseDouble(cmsc003VO.getUly5179());

				// GeoManager 생성
				try {
					List<String> layers = new ArrayList();
					GeoManager geoManager = new GeoManager(geoserverUrl, geoserverId, geoserverPw);
					layers = geoManager.getIntersectionLayers(workspace, bbox);

					Collections.sort(layers);
					for (String layerName : layers) {
						String vidoNmKr = "";
						// polygon
						if (layerName.endsWith("N3A_A0010000")) {
							vidoNmKr = "도로";
						} else if (layerName.endsWith("N3A_A0053326")) {
							vidoNmKr = "안전지대";
						} else if (layerName.endsWith("N3A_A0063321")) {
							vidoNmKr = "육교";
						} else if (layerName.endsWith("N3A_A0070000")) {
							vidoNmKr = "교량";
						} else if (layerName.endsWith("N3A_A0090000")) {
							vidoNmKr = "입체교차부";
						} else if (layerName.endsWith("N3A_A0100000")) {
							vidoNmKr = "인터체인지";
						} else if (layerName.endsWith("N3A_A0110020")) {
							vidoNmKr = "터널";
						} else if (layerName.endsWith("N3A_A0160024")) {
							vidoNmKr = "철도경계";
						} else if (layerName.endsWith("N3A_A0191221")) {
							vidoNmKr = "버스승강장";
						} else if (layerName.endsWith("N3A_B0010000")) {
							vidoNmKr = "건물";
						} else if (layerName.endsWith("N3A_C0032254")) {
							vidoNmKr = "선착장";
						} else if (layerName.endsWith("N3A_C0290000")) {
							vidoNmKr = "묘지계";
						} else if (layerName.endsWith("N3A_C0390000")) {
							vidoNmKr = "계단";
						} else if (layerName.endsWith("N3A_C0423365")) {
							vidoNmKr = "주유소";
						} else if (layerName.endsWith("N3A_C0430000")) {
							vidoNmKr = "주차장";
						} else if (layerName.endsWith("N3A_C0443363")) {
							vidoNmKr = "휴게소";
						} else if (layerName.endsWith("N3A_D0010000")) {
							vidoNmKr = "경지계";
//							layerD001_A.add(layerName);
//							continue;
						} else if (layerName.endsWith("N3A_D0020000")) {
							vidoNmKr = "지류계";
						} else if (layerName.endsWith("N3A_E0010001")) {
							vidoNmKr = "하천";
						} else if (layerName.endsWith("N3A_E0032111")) {
							vidoNmKr = "실폭하천";
						} else if (layerName.endsWith("N3A_E0052114")) {
							vidoNmKr = "호수,저수지";
						} else if (layerName.endsWith("N3A_G0010000")) {
							vidoNmKr = "행정경계(시, 도)";
						} else if (layerName.endsWith("N3A_G0020000")) {
							vidoNmKr = "수부지형경계";
						} else if (layerName.endsWith("N3A_G0100000")) {
							vidoNmKr = "행정경계(시, 군구)";
						} else if (layerName.endsWith("N3A_G0110000")) {
							vidoNmKr = "행정경계(읍면동)";
						} else if (layerName.endsWith("N3A_H0010000")) {
							vidoNmKr = "도곽";
						}
						// linestring
						else if (layerName.endsWith("N3L_A0010000")) {
							vidoNmKr = "도로경계선";
						} else if (layerName.endsWith("N3L_A0020000")) {
							vidoNmKr = "도로중심선";
						} else if (layerName.endsWith("N3L_A0033320")) {
							vidoNmKr = "인도경계";
						} else if (layerName.endsWith("N3L_B0020000")) {
							vidoNmKr = "담장";
						} else if (layerName.endsWith("N3L_C0050000")) {
							vidoNmKr = "제방";
						} else if (layerName.endsWith("N3L_C0060000")) {
							vidoNmKr = "수문";
						} else if (layerName.endsWith("N3L_C0070000")) {
							vidoNmKr = "암거";
						} else if (layerName.endsWith("N3L_C0080000")) {
							vidoNmKr = "잔교";
						} else if (layerName.endsWith("N3L_C0325315")) {
							vidoNmKr = "성";
						} else if (layerName.endsWith("N3L_C0520000")) {
							vidoNmKr = "도로분리대";
						} else if (layerName.endsWith("N3L_E0020000")) {
							vidoNmKr = "하천중심선";
						} else if (layerName.endsWith("N3L_E0060000")) {
							vidoNmKr = "용수로";
						} else if (layerName.endsWith("N3L_E0080000")) {
							vidoNmKr = "해안선";
						} else if (layerName.endsWith("N3L_F0010000")) {
							vidoNmKr = "등고선";
						} else if (layerName.endsWith("N3L_F0030000")) {
							vidoNmKr = "성/절토";
						} else if (layerName.endsWith("N3L_F0040000")) {
							vidoNmKr = "옹벽";
						} else if (layerName.endsWith("N3L_G0030000")) {
							vidoNmKr = "기타경계";
						} else {
							vidoNmKr = layerName;
						}

						Map<String, Object> mapCMS00VO = BeanUtils.describe(cmsc003VO);
						JSONObject digitalObj = new JSONObject();
						digitalObj.put("ltopCrdntX", mapCMS00VO.get("uly5179"));
						digitalObj.put("ltopCrdntY", mapCMS00VO.get("ulx5179"));
						digitalObj.put("rbtmCrdntX", mapCMS00VO.get("lry5179"));
						digitalObj.put("rbtmCrdntY", mapCMS00VO.get("lrx5179"));
						digitalObj.put("vidoNmKr", vidoNmKr);
						digitalObj.put("disasterType", "DigitalMap");
						digitalObj.put("thumbnail", "true");
						digitalObj.put("vidoNm", layerName);

						digitalArr.add(digitalObj);
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return digitalArr;
	}

	@Override
	public List<?> selectGraphicsList(CMSC003VO cmsc003VO) throws UnsupportedEncodingException, IOException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		JSONArray graphicsArr = new JSONArray();

		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상

			// 벡터데이터 인구통계
			// BBOX 데이터 생성
			double bbox[] = new double[4];
			if (StringUtils.isNotEmpty(cmsc003VO.getLrx5179())) {
				bbox[0] = Double.parseDouble(cmsc003VO.getLrx5179());
				bbox[1] = Double.parseDouble(cmsc003VO.getLry5179());
				bbox[2] = Double.parseDouble(cmsc003VO.getUlx5179());
				bbox[3] = Double.parseDouble(cmsc003VO.getUly5179());

				// GeoManager 생성
				try {
					List<String> layers = new ArrayList();
					GeoManager geoManager = new GeoManager(geoserverUrl, geoserverId, geoserverPw);
					layers = geoManager.getIntersectionLayers("nlsp", bbox);

					for (String layerName : layers) {
						Map<String, Object> mapCMS00VO = BeanUtils.describe(cmsc003VO);
						mapCMS00VO.put("vidoNm", layerName);
						String vidoNmKr = "";
						if (layerName.endsWith("nlsp_030001001")) {
							vidoNmKr = "인구격자통계 레이어";
						}
						JSONObject graphicObj = new JSONObject();
						graphicObj.put("ltopCrdntX", mapCMS00VO.get("uly5179"));
						graphicObj.put("ltopCrdntY", mapCMS00VO.get("ulx5179"));
						graphicObj.put("rbtmCrdntX", mapCMS00VO.get("lry5179"));
						graphicObj.put("rbtmCrdntY", mapCMS00VO.get("lrx5179"));
						graphicObj.put("vidoNmKr", vidoNmKr);
						graphicObj.put("disasterType", "Demographics");
						graphicObj.put("thumbnail", "true");
						graphicObj.put("vidoNm", layerName);
						graphicsArr.add(graphicObj);
					}

					layers = geoManager.getIntersectionLayers("buld", bbox);
					for (String layerName : layers) {
						Map<String, Object> mapCMS00VO = BeanUtils.describe(cmsc003VO);
						mapCMS00VO.put("vidoNm", layerName);
						String vidoNmKr = "";
						if (layerName.endsWith("buld_021002021")) {
							vidoNmKr = "건물통계 레이어";
						}

						JSONObject graphicObj = new JSONObject();
						graphicObj.put("ltopCrdntX", mapCMS00VO.get("uly5179"));
						graphicObj.put("ltopCrdntY", mapCMS00VO.get("ulx5179"));
						graphicObj.put("rbtmCrdntX", mapCMS00VO.get("lry5179"));
						graphicObj.put("rbtmCrdntY", mapCMS00VO.get("lrx5179"));
						graphicObj.put("vidoNmKr", vidoNmKr);
						graphicObj.put("disasterType", "Buldgraphics");
						graphicObj.put("thumbnail", "true");
						graphicObj.put("vidoNm", layerName);
						graphicsArr.add(graphicObj);
					}

					layers = geoManager.getIntersectionLayers("land", bbox);
					for (String layerName : layers) {
						Map<String, Object> mapCMS00VO = BeanUtils.describe(cmsc003VO);
						mapCMS00VO.put("vidoNm", layerName);
						String vidoNmKr = "";
						if (layerName.endsWith("land_021004001")) {
							vidoNmKr = "지가통계 레이어";
						}
						JSONObject graphicObj = new JSONObject();
						graphicObj.put("ltopCrdntX", mapCMS00VO.get("uly5179"));
						graphicObj.put("ltopCrdntY", mapCMS00VO.get("ulx5179"));
						graphicObj.put("rbtmCrdntX", mapCMS00VO.get("lry5179"));
						graphicObj.put("rbtmCrdntY", mapCMS00VO.get("lrx5179"));
						graphicObj.put("vidoNmKr", vidoNmKr);
						graphicObj.put("disasterType", "Landgraphics");
						graphicObj.put("thumbnail", "true");
						graphicObj.put("vidoNm", layerName);
						graphicsArr.add(graphicObj);
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return graphicsArr;
	}

	@Override
	public JSONArray selectAirOrientMap(CMSC003VO cmsc003VO) {

		JSONArray airMapList = new JSONArray();

		if (cmsc003VO != null) {
			if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
				String dateFromStr = cmsc003VO.getDateFrom();
				String dateToStr = cmsc003VO.getDateTo();

				SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy");

				try {
					Date fromaDate;
					Date toDate;

					fromaDate = dtFormat.parse(dateFromStr);
					dateFromStr = newDtFormat.format(fromaDate);

					toDate = dtFormat.parse(dateToStr);
					dateToStr = newDtFormat.format(toDate);

					cmsc003VO.setDateYearFrom(dateFromStr);
					cmsc003VO.setDateYearTo(dateToStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				cmsc003VO.createPolygonWkt(cmsc003VO.getUlx5179(), cmsc003VO.getLry5179(), cmsc003VO.getLrx5179(),
						cmsc003VO.getUly5179());

				// gfra
				List<EgovMap> gfraAirMapList = new ArrayList<EgovMap>();
				try {
					gfraAirMapList = (List<EgovMap>) cmsc003UgisMapper.selectAirOrientMap(cmsc003VO);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				for (EgovMap egovMap : gfraAirMapList) {
					try {
						// 파일명
						String fileNam = (String) egovMap.get("fileNam");
						// 경로 구성 Param
						String stoDrv = (String) egovMap.get("stoDrv");
						if (!StringUtils.isEmpty(stoDrv)) {
							stoDrv = stoDrv.trim();
						}
						String folderNam = (String) egovMap.get("folderNam");
						String zoneCode = (String) egovMap.get("zoneCode");
						String zoneFCode = "0000";
						String zoneLCode = "0000";
						if (zoneCode != null) {
							zoneFCode = zoneCode.substring(0, 4);
							zoneLCode = zoneCode.substring(7, 11);
						}
						String phCourse = (String) egovMap.get("phCourse");
						StringBuilder filePath = new StringBuilder();

						// 저장경로
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
						filePath.append(fileNam + "." + "tif");
						String repFilePath = filePath.toString();

						// 리눅스 경로 변경
						repFilePath = repFilePath.replaceAll("\\\\", "/");
						egovMap.put("vidoNm", fileNam);
						egovMap.put("fullFileCoursNm", repFilePath);
						egovMap.put("year", zoneFCode);

						JSONObject resMap = new JSONObject();
						resMap.put("vidoNm", fileNam);
						resMap.put("ltopCrdntY", egovMap.get("xmin"));
						resMap.put("rbtmCrdntY", egovMap.get("xmax"));
						resMap.put("rbtmCrdntX", egovMap.get("ymin"));
						resMap.put("ltopCrdntX", egovMap.get("ymax"));
						resMap.put("fullFileCoursNm", repFilePath);
						resMap.put("year", zoneFCode);
						resMap.put("mapPrjctnCn", "EPSG:5179");
						resMap.put("potogrfBeginDt", zoneFCode + "-01-01");
						resMap.put("potogrfEndDt", zoneFCode + "-12-31");

						String year = (String) egovMap.get("year");
						String dpi12 = "12cm";
						// 썸네일 다운로드
						String gfraThumbPath = repFilePath.replace("/air/", "air_jpg/").replace(".tif", ".jpg");
						String downloadPath = gFraDownPath + gfraThumbPath; // ex)
																			// /home/TempData/01_GeoFraData/air/rgb/1966/0001/C/1966000001000C3600.jpg
						downloadPath = downloadPath.replace("/air_jpg/", "/air/"); // -> tif와 같은 위치에 다운로드
						File downloadFile = new File(downloadPath);

						if (!downloadFile.exists()) {
							// download file
							HttpDownloader downloader = new HttpDownloader();
							downloader.download(httpBoundary, gFraUrl, downloadPath, gfraThumbPath);
						}

						if (downloadFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", downloadPath);
						} else {
							resMap.put("thumbnail", false);
						}

						boolean exist = false;
						for (int j = 0; j < airMapList.size(); j++) {
							JSONObject airMap = (JSONObject) airMapList.get(j);
							String mapYear = (String) airMap.get("year");
							if (mapYear.contentEquals(year)) {
								if (airMap.get("maps") != null) {
									JSONArray mapList = (JSONArray) airMap.get("maps");
									for (int m = 0; m < mapList.size(); m++) {
										JSONObject mapObj = (JSONObject) mapList.get(m);
										if (mapObj.get("dpi") != null) {
											String dpiIn = (String) mapObj.get("dpi");
											JSONArray map = (JSONArray) mapObj.get("map");
											if (dpi12.contentEquals(dpiIn)) {
												map.add(resMap);
												exist = true;
												break;
											}
										}
									}
									if (!exist) {
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi12);
										JSONArray mapArry = new JSONArray();
										mapArry.add(resMap);
										newMapObj.put("map", mapArry);
										mapList.add(newMapObj);
										exist = true;
									}
								} else {
									JSONArray mapps = new JSONArray();
									JSONObject newMapObj = new JSONObject();
									newMapObj.put("dpi", dpi12);
									JSONArray map = new JSONArray();
									map.add(resMap);
									newMapObj.put("maps", map);
									mapps.add(newMapObj);
									exist = true;
								}
							}
						}
						if (!exist) {
							JSONObject mapYear = new JSONObject();
							mapYear.put("year", year);
							JSONArray mapsList = new JSONArray();
							JSONObject newMapObj = new JSONObject();
							newMapObj.put("dpi", dpi12);
							JSONArray mapArry = new JSONArray();
							mapArry.add(resMap);
							newMapObj.put("map", mapArry);
							mapsList.add(newMapObj);
							mapYear.put("maps", mapsList);
							airMapList.add(mapYear);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// usgs_work
				if (cmsc003VO.getSearchWork() != null) {
					List<EgovMap> usgsAirMapList = new ArrayList<EgovMap>();
					try {
						usgsAirMapList = (List<EgovMap>) cmsc003Mapper.selectUsgsWorkCurrentAirList(cmsc003VO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					for (EgovMap usgs : usgsAirMapList) {
						try {
							String path = (String) usgs.get("innerFileCoursNm");
							File file = new File(path);
							String ext = FilenameUtils.getExtension(file.getName());
							String fName = file.getName().replace("." + ext, "");
							String potogrfBeginDt = usgs.get("potogrfBeginDt").toString(); // 발생년도
							String year = potogrfBeginDt.split("-")[0];

							JSONObject resMap = new JSONObject();
							resMap.put("vidoNm", fName);
							resMap.put("ltopCrdntX", usgs.get("ltopCrdntX"));
							resMap.put("ltopCrdntY", usgs.get("ltopCrdntY"));
							resMap.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
							resMap.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
							resMap.put("fullFileCoursNm", path);
							resMap.put("year", year);
							resMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
							resMap.put("usgsWorkId", usgs.get("usgsWorkId"));

							String thumbPath = path.replace(".tif", ".png");
							if (new File(thumbPath).exists()) {
								resMap.put("thumbnail", true);
								resMap.put("thumbnailFileCoursNm", thumbPath);
							} else {
								resMap.put("thumbnail", false);
							}

							String dpi12 = "12cm";
							boolean exist = false;
							for (int j = 0; j < airMapList.size(); j++) {
								JSONObject airMap = (JSONObject) airMapList.get(j);
								String mapYear = (String) airMap.get("year");
								if (mapYear.contentEquals(year)) {
									if (airMap.get("maps") != null) {
										JSONArray mapList = (JSONArray) airMap.get("maps");
										for (int m = 0; m < mapList.size(); m++) {
											JSONObject mapObj = (JSONObject) mapList.get(m);
											if (mapObj.get("dpi") != null) {
												String dpiIn = (String) mapObj.get("dpi");
												JSONArray map = (JSONArray) mapObj.get("map");
												if (dpi12.contentEquals(dpiIn)) {
													map.add(resMap);
													exist = true;
													break;
												}
											}
										}
										if (!exist) {
											JSONObject newMapObj = new JSONObject();
											newMapObj.put("dpi", dpi12);
											JSONArray mapArry = new JSONArray();
											mapArry.add(resMap);
											newMapObj.put("map", mapArry);
											mapList.add(newMapObj);
											exist = true;
										}
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi12);
										JSONArray map = new JSONArray();
										map.add(resMap);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
										exist = true;
									}
								}
							}
							if (!exist) {
								JSONObject mapYear = new JSONObject();
								mapYear.put("year", year);
								JSONArray mapsList = new JSONArray();
								JSONObject newMapObj = new JSONObject();
								newMapObj.put("dpi", dpi12);
								JSONArray mapArry = new JSONArray();
								mapArry.add(resMap);
								newMapObj.put("map", mapArry);
								mapsList.add(newMapObj);
								mapYear.put("maps", mapsList);
								airMapList.add(mapYear);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return airMapList;
	}

	@Override
	public JSONObject selectOrtOrientMap(CMSC003VO cmsc003VO) {

		JSONObject ortJson = new JSONObject();
		JSONArray ortMapList = new JSONArray();
		List<String> mapNmList = new ArrayList<String>();
		if (cmsc003VO != null) {
			if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
				String dateFromStr = cmsc003VO.getDateFrom();
				String dateToStr = cmsc003VO.getDateTo();

				SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy");

				try {
					Date fromaDate;
					Date toDate;

					fromaDate = dtFormat.parse(dateFromStr);
					dateFromStr = newDtFormat.format(fromaDate);

					toDate = dtFormat.parse(dateToStr);
					dateToStr = newDtFormat.format(toDate);

					cmsc003VO.setDateYearFrom(dateFromStr);
					cmsc003VO.setDateYearTo(dateToStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				cmsc003VO.createPolygonWkt(cmsc003VO.getUlx5179(), cmsc003VO.getLry5179(), cmsc003VO.getLrx5179(),
						cmsc003VO.getUly5179());

				List<EgovMap> orientList;
				try {
					orientList = (List<EgovMap>) cmsc003UgisMapper.selectOrtOrientMap(cmsc003VO);
				} catch (Exception e) {
					LOGGER.error("오라클 연동 불가");
					e.printStackTrace();
					return new JSONObject();
				}

				for (EgovMap egovMap : orientList) {
					try {
						// 파일명
						String fileNam = (String) egovMap.get("fileNam");
						String zoneYy = String.valueOf(egovMap.get("zoneYy"));

						// 경로 구성 Param
						String mapNum = (String) egovMap.get("mapNum");
						String origin = (String) egovMap.get("origin");
						String stoDrv = (String) egovMap.get("stoDrv");

						if (!StringUtils.isEmpty(stoDrv)) {
							stoDrv = stoDrv.trim();
						}
						String folderNam = (String) egovMap.get("folderNam");

						String[] dpiArry = folderNam.split("/");
						String dpi = dpiArry[dpiArry.length - 1]; // 해상도

						String zoneCode = (String) egovMap.get("zoneCode");
						String zoneLCode = "0000";
						if (zoneCode != null) {
							zoneLCode = zoneCode.substring(7, 11);
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
						filePath.append(fileNam + "." + "tif");
						String repFilePath = filePath.toString();

						// 리눅스 경로 변경
						repFilePath = repFilePath.replaceAll("\\\\", "/");

						JSONObject resMap = new JSONObject();
						resMap.put("vidoNm", fileNam);
						resMap.put("ltopCrdntY", egovMap.get("xmin"));
						resMap.put("rbtmCrdntY", egovMap.get("xmax"));
						resMap.put("rbtmCrdntX", egovMap.get("ymin"));
						resMap.put("ltopCrdntX", egovMap.get("ymax"));
						resMap.put("fullFileCoursNm", repFilePath);
						resMap.put("year", zoneYy);
						resMap.put("mapPrjctnCn", "EPSG:5179");
						resMap.put("potogrfBeginDt", zoneYy + "-01-01");
						resMap.put("potogrfEndDt", zoneYy + "-12-31");

						// 썸네일 다운로드
						String gfraThumbPath = repFilePath.replace("/ort/", "ort_jpg/").replace(".tif", ".jpg");
						String downloadPath = gFraDownPath + gfraThumbPath; // ex)
																			// /home/TempData/01_GeoFraData/ort/rgb/1966/0001/C/1966000001000C3600.jpg
						downloadPath = downloadPath.replace("/ort_jpg/", "/ort/");
						File downloadFile = new File(downloadPath);
						if (!downloadFile.exists()) {
							// download file
							HttpDownloader downloader = new HttpDownloader();
							downloader.download(httpBoundary, gFraUrl, downloadPath, gfraThumbPath);
						}
						if (downloadFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", downloadPath);
						} else {
							resMap.put("thumbnail", false);
						}

						String map5000Num = (String) egovMap.get("map5000Num");
						mapNmList.add(map5000Num);

						boolean exist = false;
						for (int j = 0; j < ortMapList.size(); j++) {
							JSONObject airMap = (JSONObject) ortMapList.get(j);
							String mapYear = (String) airMap.get("year");
							if (mapYear.contentEquals(zoneYy)) {
								if (airMap.get("maps") != null) {
									JSONArray mapList = (JSONArray) airMap.get("maps");
									for (int m = 0; m < mapList.size(); m++) {
										JSONObject mapObj = (JSONObject) mapList.get(m);
										if (mapObj.get("dpi") != null) {
											String dpiIn = (String) mapObj.get("dpi");
											JSONArray map = (JSONArray) mapObj.get("map");
											if (dpi.contentEquals(dpiIn)) {
												map.add(resMap);
												exist = true;
												break;
											}
										}
									}
									if (!exist) {
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray mapArry = new JSONArray();
										mapArry.add(resMap);
										newMapObj.put("map", mapArry);
										mapList.add(newMapObj);
										exist = true;
									}
								} else {
									JSONArray mapps = new JSONArray();
									JSONObject newMapObj = new JSONObject();
									newMapObj.put("dpi", dpi);
									JSONArray map = new JSONArray();
									map.add(resMap);
									newMapObj.put("maps", map);
									mapps.add(newMapObj);
									exist = true;
								}
							}
						}
						if (!exist) {
							JSONObject mapYear = new JSONObject();
							mapYear.put("year", zoneYy);
							JSONArray mapsList = new JSONArray();
							JSONObject newMapObj = new JSONObject();
							newMapObj.put("dpi", dpi);
							JSONArray mapArry = new JSONArray();
							mapArry.add(resMap);
							newMapObj.put("map", mapArry);
							mapsList.add(newMapObj);
							mapYear.put("maps", mapsList);
							ortMapList.add(mapYear);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// usgs_work
				if (cmsc003VO.getSearchWork() != null) {
					List<EgovMap> usgsOrtMapList = new ArrayList<EgovMap>();
					try {
						usgsOrtMapList = (List<EgovMap>) cmsc003Mapper.selectUsgsWorkCurrentOrtList(cmsc003VO);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					for (EgovMap usgs : usgsOrtMapList) {
						try {
							String path = (String) usgs.get("innerFileCoursNm");
							File file = new File(path);
							String ext = FilenameUtils.getExtension(file.getName());
							String fName = file.getName().replace("." + ext, "");
							String potogrfBeginDt = usgs.get("potogrfBeginDt").toString(); // 발생년도
							String year = potogrfBeginDt.split("-")[0];

							JSONObject resMap = new JSONObject();
							resMap.put("vidoNm", fName);
							resMap.put("ltopCrdntX", usgs.get("ltopCrdntX"));
							resMap.put("ltopCrdntY", usgs.get("ltopCrdntY"));
							resMap.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
							resMap.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
							resMap.put("fullFileCoursNm", path);
							resMap.put("year", year);
							resMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
							resMap.put("usgsWorkId", usgs.get("usgsWorkId"));

							String thumbPath = path.replace(".tif", ".png");
							if (new File(thumbPath).exists()) {
								resMap.put("thumbnail", true);
								resMap.put("thumbnailFileCoursNm", thumbPath);
							} else {
								resMap.put("thumbnail", false);
							}

							String[] pathArry = path.split("/");
							String dpi = pathArry[5];

							boolean exist = false;
							for (int j = 0; j < ortMapList.size(); j++) {
								JSONObject airMap = (JSONObject) ortMapList.get(j);
								String mapYear = (String) airMap.get("year");
								if (mapYear.contentEquals(year)) {
									if (airMap.get("maps") != null) {
										JSONArray mapList = (JSONArray) airMap.get("maps");
										for (int m = 0; m < mapList.size(); m++) {
											JSONObject mapObj = (JSONObject) mapList.get(m);
											if (mapObj.get("dpi") != null) {
												String dpiIn = (String) mapObj.get("dpi");
												JSONArray map = (JSONArray) mapObj.get("map");
												if (dpi.contentEquals(dpiIn)) {
													map.add(resMap);
													exist = true;
													break;
												}
											}
										}
										if (!exist) {
											JSONObject newMapObj = new JSONObject();
											newMapObj.put("dpi", dpi);
											JSONArray mapArry = new JSONArray();
											mapArry.add(resMap);
											newMapObj.put("map", mapArry);
											mapList.add(newMapObj);
											exist = true;
										}
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resMap);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
										exist = true;
									}
								}
							}
							if (!exist) {
								JSONObject mapYear = new JSONObject();
								mapYear.put("year", year);
								JSONArray mapsList = new JSONArray();
								JSONObject newMapObj = new JSONObject();
								newMapObj.put("dpi", dpi);
								JSONArray mapArry = new JSONArray();
								mapArry.add(resMap);
								newMapObj.put("map", mapArry);
								mapsList.add(newMapObj);
								mapYear.put("maps", mapsList);
								ortMapList.add(mapYear);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		String mapNm = "";
		if (mapNmList.size() > 0) {
			Set<String> set = new HashSet<String>(mapNmList);
			List<String> newList = new ArrayList<String>(set);
			for (String nm : newList) {
				mapNm += nm + ",";
			}
			mapNm = mapNm.substring(0, mapNm.lastIndexOf(","));
		}
		ortJson.put("ortMapList", ortMapList);
		ortJson.put("mapNm", mapNm);

		return ortJson;
	}

	@Override
	public JSONArray selectDemMap(CMSC003VO cmsc003VO) {

		JSONArray demMapList = new JSONArray();

		if (cmsc003VO != null) {
			if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
				String dateFromStr = cmsc003VO.getDateFrom();
				String dateToStr = cmsc003VO.getDateTo();

				SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy");

				try {
					Date fromaDate;
					Date toDate;
					fromaDate = dtFormat.parse(dateFromStr);
					dateFromStr = newDtFormat.format(fromaDate);
					toDate = dtFormat.parse(dateToStr);
					dateToStr = newDtFormat.format(toDate);
					cmsc003VO.setDateYearFrom(dateFromStr);
					cmsc003VO.setDateYearTo(dateToStr);
				} catch (ParseException e) {
					e.printStackTrace();
					return new JSONArray();
				}

				cmsc003VO.createPolygonWkt(cmsc003VO.getUlx5179(), cmsc003VO.getLry5179(), cmsc003VO.getLrx5179(),
						cmsc003VO.getUly5179());

				List<EgovMap> demList = new ArrayList<EgovMap>();
				try {
					demList = (List<EgovMap>) cmsc003UgisMapper.selectDemMap(cmsc003VO);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				for (EgovMap egovMap : demList) {
					try {
						// 파일명
						String fileNam = (String) egovMap.get("fileNam");
						String map5000Na = (String) egovMap.get("map5000Na");
						String zoneYy = String.valueOf(egovMap.get("zoneYy"));

						String mapNum = (String) egovMap.get("mapNum");
						String origin = (String) egovMap.get("origin");
						String stoDrv = (String) egovMap.get("stoDrv");
						if (!StringUtils.isEmpty(stoDrv)) {
							stoDrv = stoDrv.trim();
						}
						String folderNam = (String) egovMap.get("folderNam");

						String[] dpiArry = folderNam.split("/");
						String dpi = dpiArry[dpiArry.length - 1]; // 해상도

						String zoneCode = (String) egovMap.get("zoneCode");
						String zoneLCode = "0000";
						if (zoneCode != null) {
							zoneLCode = zoneCode.substring(7, 11);
						}
						String fileFormat = (String) egovMap.get("fileFormat");
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
						filePath.append(fileNam + ".IMG"); // 2014 90m 급 임시
						// filePath.append(fileNam + "." + fileFormat);
						String repFilePath = filePath.toString();

						// 리눅스 경로 변경
						repFilePath = repFilePath.replaceAll("\\\\", "/");

						JSONObject resMap = new JSONObject();
						resMap.put("vidoNm", zoneYy + "_" + map5000Na + "_" + fileNam);
						resMap.put("ltopCrdntY", egovMap.get("xmin"));
						resMap.put("rbtmCrdntY", egovMap.get("xmax"));
						resMap.put("rbtmCrdntX", egovMap.get("ymin"));
						resMap.put("ltopCrdntX", egovMap.get("ymax"));
						resMap.put("fullFileCoursNm", repFilePath);
						resMap.put("year", zoneYy);
						resMap.put("mapPrjctnCn", "EPSG:5179");
						resMap.put("potogrfBeginDt", zoneYy + "-01-01");
						resMap.put("potogrfEndDt", zoneYy + "-12-31");

						// 썸네일 다운로드
						String gfraThumbPath = repFilePath.replace("/dem/", "dem_jpg/").replace(".IMG", ".jpg");
						String downloadPath = gFraDownPath + gfraThumbPath;

						String thumbPath = "";
						File downloadFile = new File(downloadPath);
						if (!downloadFile.exists()) {
							// download file
							HttpDownloader downloader = new HttpDownloader();
							downloader.download(httpBoundary, gFraUrl, downloadPath, gfraThumbPath);
							thumbPath = downloadPath;
						}
						if (downloadFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", downloadPath);
						} else {
							resMap.put("thumbnail", false);
						}

						String year = zoneYy;
						boolean exist = false;
						for (int j = 0; j < demMapList.size(); j++) {
							JSONObject demMap = (JSONObject) demMapList.get(j);
							String mapYear = (String) demMap.get("year");
							if (mapYear.contentEquals(year)) {
								if (demMap.get("maps") != null) {
									JSONArray mapList = (JSONArray) demMap.get("maps");
									for (int m = 0; m < mapList.size(); m++) {
										JSONObject mapObj = (JSONObject) mapList.get(m);
										if (mapObj.get("dpi") != null) {
											String dpiIn = (String) mapObj.get("dpi");
											JSONArray map = (JSONArray) mapObj.get("map");
											if (dpi.contentEquals(dpiIn)) {
												map.add(resMap);
												exist = true;
												break;
											}
										}
									}
									if (!exist) {
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray mapArry = new JSONArray();
										mapArry.add(resMap);
										newMapObj.put("map", mapArry);
										mapList.add(newMapObj);
										exist = true;
									}
								} else {
									JSONArray mapps = new JSONArray();
									JSONObject newMapObj = new JSONObject();
									newMapObj.put("dpi", dpi);
									JSONArray map = new JSONArray();
									map.add(resMap);
									newMapObj.put("maps", map);
									mapps.add(newMapObj);
									exist = true;
								}
							}
						}
						if (!exist) {
							JSONObject mapYear = new JSONObject();
							mapYear.put("year", year);
							JSONArray mapsList = new JSONArray();
							JSONObject newMapObj = new JSONObject();
							newMapObj.put("dpi", dpi);
							JSONArray mapArry = new JSONArray();
							mapArry.add(resMap);
							newMapObj.put("map", mapArry);
							mapsList.add(newMapObj);
							mapYear.put("maps", mapsList);
							demMapList.add(mapYear);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// usgs_work
				if (cmsc003VO.getSearchWork() != null) {
					List<EgovMap> usgsDemMapList = new ArrayList<EgovMap>();
					try {
						usgsDemMapList = (List<EgovMap>) cmsc003Mapper.selectUsgsWorkCurrentDemList(cmsc003VO);
					} catch (Exception e) {
						e.printStackTrace();
						return new JSONArray();
					}
					for (EgovMap usgs : usgsDemMapList) {
						try {
							String path = (String) usgs.get("innerFileCoursNm");
							File file = new File(path);
							String ext = FilenameUtils.getExtension(file.getName());
							String fName = file.getName().replace("." + ext, "");
							String potogrfBeginDt = usgs.get("potogrfBeginDt").toString(); // 발생년도
							String year = potogrfBeginDt.split("-")[0];

							JSONObject resMap = new JSONObject();
							resMap.put("vidoNm", fName);
							resMap.put("ltopCrdntX", usgs.get("ltopCrdntX"));
							resMap.put("ltopCrdntY", usgs.get("ltopCrdntY"));
							resMap.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
							resMap.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
							resMap.put("fullFileCoursNm", path);
							resMap.put("year", year);
							resMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
							resMap.put("usgsWorkId", usgs.get("usgsWorkId"));

							String thumbPath = path.replace(".tif", ".png");
							if (new File(thumbPath).exists()) {
								resMap.put("thumbnail", true);
								resMap.put("thumbnailFileCoursNm", thumbPath);
							} else {
								resMap.put("thumbnail", false);
							}

							String[] pathArry = path.split("/");
							String dpi = pathArry[5];

							boolean exist = false;
							for (int j = 0; j < demMapList.size(); j++) {
								JSONObject demMap = (JSONObject) demMapList.get(j);
								String mapYear = (String) demMap.get("year");
								if (mapYear.contentEquals(year)) {
									if (demMap.get("maps") != null) {
										JSONArray mapList = (JSONArray) demMap.get("maps");
										for (int m = 0; m < mapList.size(); m++) {
											JSONObject mapObj = (JSONObject) mapList.get(m);
											if (mapObj.get("dpi") != null) {
												String dpiIn = (String) mapObj.get("dpi");
												JSONArray map = (JSONArray) mapObj.get("map");
												if (dpi.contentEquals(dpiIn)) {
													map.add(resMap);
													exist = true;
													break;
												}
											}
										}
										if (!exist) {
											JSONObject newMapObj = new JSONObject();
											newMapObj.put("dpi", dpi);
											JSONArray mapArry = new JSONArray();
											mapArry.add(resMap);
											newMapObj.put("map", mapArry);
											mapList.add(newMapObj);
											exist = true;
										}
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resMap);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
										exist = true;
									}
								}
							}
							if (!exist) {
								JSONObject mapYear = new JSONObject();
								mapYear.put("year", year);
								JSONArray mapsList = new JSONArray();
								JSONObject newMapObj = new JSONObject();
								newMapObj.put("dpi", dpi);
								JSONArray mapArry = new JSONArray();
								mapArry.add(resMap);
								newMapObj.put("map", mapArry);
								mapsList.add(newMapObj);
								mapYear.put("maps", mapsList);
								demMapList.add(mapYear);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return demMapList;
	}

	//긴급공간정보 생성 검색 버튼 클릭시 트리 조회 컨트롤러 2024.08.05 sar
	@Override
	public JSONArray selectUsgsSatelliteList(CMSC003VO cmsc003vo) {

		JSONArray result = new JSONArray();
		if (cmsc003vo.getDataKindCurrent() != null && cmsc003vo.getDataKindCurrent().contentEquals("on")) {
			// postgres
			List<EgovMap> usgsList = new ArrayList<EgovMap>();

			try {
				usgsList.addAll(usgsList.size(), (List<EgovMap>) cmsc003Mapper.selectUsgsCurrentList(cmsc003vo));
				if (cmsc003vo.getSearchWork() != null) {
					usgsList.addAll(usgsList.size(),
							(List<EgovMap>) cmsc003Mapper.selectUsgsWorkCurrentList(cmsc003vo));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (usgsList != null) {
				JSONArray kompsatArry = new JSONArray(); // Kompsat
				JSONArray landsatArry = new JSONArray(); // Landsat
				JSONArray sentinelArry = new JSONArray(); // Sentinel
				JSONArray casArry = new JSONArray(); // CAS
				JSONArray sarArry = new JSONArray(); // SAR

				JSONObject kompsatObj = new JSONObject();
				JSONObject landsatObj = new JSONObject();
				JSONObject sentinelObj = new JSONObject();
				JSONObject casObj = new JSONObject();
				JSONObject sarObj = new JSONObject();

				kompsatObj.put("satNm", "Kompsat");
				kompsatObj.put("map", kompsatArry);

				landsatObj.put("satNm", "Landsat");
				landsatObj.put("map", landsatArry);

				sentinelObj.put("satNm", "Sentinel");
				sentinelObj.put("map", sentinelArry);

				casObj.put("satNm", "CAS");
				casObj.put("map", casArry);
				
				sarObj.put("satNm", "SAR");
				sarObj.put("map", sarArry);

				for (int i = 0; i < usgsList.size(); i++) {
					try {
						EgovMap usgs = usgsList.get(i);
						String path = (String) usgs.get("innerFileCoursNm");
						File file = new File(path);
						File dir = file.getParentFile();

						String date = usgs.get("potogrfBeginDt").toString();
						String dirNm = dir.getName();

						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						String[] fNmArry = fName.split("_");
					
						String bandCn = fNmArry[fNmArry.length - 1];
						
						//CAS영상만 "_"다섯번째부터 출력  : 새롬 
						//영상 타입
						String potogrfVidoTpye=  usgs.get("potogrfVidoCd").toString();
						
						
						System.out.println("----------긴급공간정보 생성 시작-------------------");
						System.out.println(potogrfVidoTpye);
						System.out.println(fName);
						
						
						
						// 0 : 촬영영상코드 국토위성영상 CAS
						if(potogrfVidoTpye.equals(("0"))) {
							
							for ( int aCnt = 5; aCnt< fNmArry.length; aCnt++) {
								
								bandCn += fNmArry[aCnt];
																						
								if(!fNmArry[fNmArry.length - 1].equals(fNmArry[aCnt])) {
									bandCn += "_";
								}

						      }		
						}
						
						System.out.println(bandCn);
						System.out.println("----------긴급공간정보 생성 끝-------------------");
					
					
						JSONObject resObj = new JSONObject();

						resObj.put("ltopCrdntX", usgs.get("ltopCrdntX"));
						resObj.put("ltopCrdntY", usgs.get("ltopCrdntY"));
						resObj.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
						resObj.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
						resObj.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
						
						//SENTINEL 영상만
						//path -> 경로에 . replace 새롬
						
						// 6 : 촬영영상코드 국토위성영상 SENTINEL
						//if(potogrfVidoTpye.equals(("6"))) {
						//	path = path.replace(".", "period");
						//}

						resObj.put("innerFileCoursNm", path);
						resObj.put("fullFileCoursNm", path);
						resObj.put("potogrfBeginDt", usgs.get("potogrfBeginDt").toString());
						resObj.put("potogrfEndDt", usgs.get("potogrfEndDt").toString());

						if (usgs.get("usgsWorkId") != null) {
							resObj.put("vidoNm", fName);
							resObj.put("usgsWorkId", usgs.get("usgsWorkId"));
						} else {
							String bandCnUp = bandCn.toUpperCase();
							if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
									|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
									|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
								resObj.put("vidoNm", bandCn + ".tif");
							} else {
								resObj.put("vidoNm", fName);
							}
							resObj.put("vidoId", usgs.get("vidoId"));
						}

						// set thumbnail
						File[] files = dir.listFiles(new FilenameFilter() {

							@Override
							public boolean accept(File dir, String name) {
								return name.toUpperCase().endsWith(fName.toUpperCase() + "." + "PNG");
							}
						});
						if (files != null) {
							if (files.length > 0) {
								String thumbPath = dir.getAbsolutePath() + File.separator + files[0].getName();
								resObj.put("thumbnail", true);
								resObj.put("thumbnailFileCoursNm", thumbPath);
							} else {
								resObj.put("thumbnail", false);
							}
						} else {
							resObj.put("thumbnail", false);
						}

						String vidoCd = (String) usgs.get("potogrfVidoCd");
						if (cmsc003vo.getDataKindCurrent() != null) {
							if (vidoCd.contentEquals("0")) { // CAS
								boolean exist = false;
								for (int j = 0; j < casArry.size(); j++) {
									JSONObject casMap = (JSONObject) casArry.get(j);
									String mapFolder = (String) casMap.get("date");
									if (mapFolder.contentEquals(date)) {
										JSONArray folderList = (JSONArray) casMap.get("folderList");
										boolean existin = false;
										for (int f = 0; f < folderList.size(); f++) {
											JSONObject folderObj = (JSONObject) folderList.get(f);
											String folderNm = (String) folderObj.get("folderNm");
											if (folderNm.contentEquals(dirNm)) {
												JSONArray fileList = (JSONArray) folderObj.get("fileList");
												fileList.add(fileList.size(), resObj);
												existin = true;
											}
										}
										if (!existin) {
											JSONObject newFolderObj = new JSONObject();
											JSONArray newFileList = new JSONArray();
											newFileList.add(resObj);
											newFolderObj.put("folderNm", dirNm);
											newFolderObj.put("fileList", newFileList);
											folderList.add(newFolderObj);
										}
										exist = true;
									}
								}
								if (!exist) {
									JSONObject casMap = new JSONObject();
									casMap.put("date", date);
									JSONArray folderList = new JSONArray();
									JSONObject folderObj = new JSONObject();
									folderObj.put("folderNm", dirNm);
									JSONArray fileList = new JSONArray();
									fileList.add(resObj);
									folderObj.put("fileList", fileList);
									folderList.add(folderObj);
									casMap.put("folderList", folderList);
									casArry.add(casMap);
								}
							} else if (vidoCd.contentEquals("1")) { // Kompsat
								boolean exist = false;
								for (int j = 0; j < kompsatArry.size(); j++) {
									JSONObject kompMap = (JSONObject) kompsatArry.get(j);
									String mapFolder = (String) kompMap.get("date");
									if (mapFolder.contentEquals(date)) {
										JSONArray folderList = (JSONArray) kompMap.get("folderList");
										boolean existin = false;
										for (int f = 0; f < folderList.size(); f++) {
											JSONObject folderObj = (JSONObject) folderList.get(f);
											String folderNm = (String) folderObj.get("folderNm");
											if (folderNm.contentEquals(dirNm)) {
												JSONArray fileList = (JSONArray) folderObj.get("fileList");
												fileList.add(fileList.size(), resObj);
												existin = true;
											}
										}
										if (!existin) {
											JSONObject newFolderObj = new JSONObject();
											JSONArray newFileList = new JSONArray();
											newFileList.add(resObj);
											newFolderObj.put("folderNm", dirNm);
											newFolderObj.put("fileList", newFileList);
											folderList.add(newFolderObj);
										}
										exist = true;
									}
								}
								if (!exist) {
									JSONObject kompMap = new JSONObject();
									kompMap.put("date", date);
									JSONArray folderList = new JSONArray();
									JSONObject folderObj = new JSONObject();
									folderObj.put("folderNm", dirNm);
									JSONArray fileList = new JSONArray();
									fileList.add(resObj);
									folderObj.put("fileList", fileList);
									folderList.add(folderObj);
									kompMap.put("folderList", folderList);
									kompsatArry.add(kompMap);
								}
							} else if (vidoCd.contentEquals("2")) { // Landsat
								boolean exist = false;
								for (int j = 0; j < landsatArry.size(); j++) {
									JSONObject landsatMap = (JSONObject) landsatArry.get(j);
									String mapFolder = (String) landsatMap.get("date");
									if (mapFolder.contentEquals(date)) {
										JSONArray folderList = (JSONArray) landsatMap.get("folderList");
										boolean existin = false;
										for (int f = 0; f < folderList.size(); f++) {
											JSONObject folderObj = (JSONObject) folderList.get(f);
											String folderNm = (String) folderObj.get("folderNm");
											if (folderNm.contentEquals(dirNm)) {
												JSONArray fileList = (JSONArray) folderObj.get("fileList");
												fileList.add(fileList.size(), resObj);
												existin = true;
											}
										}
										if (!existin) {
											JSONObject newFolderObj = new JSONObject();
											JSONArray newFileList = new JSONArray();
											newFileList.add(resObj);
											newFolderObj.put("folderNm", dirNm);
											newFolderObj.put("fileList", newFileList);
											folderList.add(newFolderObj);
										}
										exist = true;
									}
								}
								if (!exist) {
									JSONObject landsatMap = new JSONObject();
									landsatMap.put("date", date);
									JSONArray folderList = new JSONArray();
									JSONObject folderObj = new JSONObject();
									folderObj.put("folderNm", dirNm);
									JSONArray fileList = new JSONArray();
									fileList.add(resObj);
									folderObj.put("fileList", fileList);
									folderList.add(folderObj);
									landsatMap.put("folderList", folderList);
									landsatArry.add(landsatMap);
								}
							} else if (vidoCd.contentEquals("6")) { // Sentinel
								String[] pathArr = path.split("/");
//								dirNm = pathArr[5];
								dirNm = pathArr[4];

								boolean exist = false;
								for (int j = 0; j < sentinelArry.size(); j++) {
									JSONObject sentinelMap = (JSONObject) sentinelArry.get(j);
									String mapFolder = (String) sentinelMap.get("date");
									if (mapFolder.contentEquals(date)) {
										JSONArray folderList = (JSONArray) sentinelMap.get("folderList");
										boolean existin = false;
										for (int f = 0; f < folderList.size(); f++) {
											JSONObject folderObj = (JSONObject) folderList.get(f);
											String folderNm = (String) folderObj.get("folderNm");
											if (folderNm.contentEquals(dirNm)) {
												JSONArray fileList = (JSONArray) folderObj.get("fileList");
												fileList.add(fileList.size(), resObj);
												existin = true;
											}
										}
										if (!existin) {
											JSONObject newFolderObj = new JSONObject();
											JSONArray newFileList = new JSONArray();
											newFileList.add(resObj);
											newFolderObj.put("folderNm", dirNm);
											newFolderObj.put("fileList", newFileList);
											folderList.add(newFolderObj);
										}
										exist = true;
									}
								}
								if (!exist) {
									JSONObject sentinelMap = new JSONObject();
									sentinelMap.put("date", date);
									JSONArray folderList = new JSONArray();
									JSONObject folderObj = new JSONObject();
									folderObj.put("folderNm", dirNm);
									JSONArray fileList = new JSONArray();
									fileList.add(resObj);
									folderObj.put("fileList", fileList);
									folderList.add(folderObj);
									sentinelMap.put("folderList", folderList);
									sentinelArry.add(sentinelMap);
								}
							}  else if (vidoCd.contentEquals("9")) { //  2024.08.05 sar:9
								String[] pathArr = path.split("/");
//								dirNm = pathArr[5];
								dirNm = pathArr[4];

								boolean exist = false;
								for (int j = 0; j < sarArry.size(); j++) {
									JSONObject sarMap = (JSONObject) sarArry.get(j);
									String mapFolder = (String) sarMap.get("date");
									if (mapFolder.contentEquals(date)) {
										JSONArray folderList = (JSONArray) sarMap.get("folderList");
										boolean existin = false;
										for (int f = 0; f < folderList.size(); f++) {
											JSONObject folderObj = (JSONObject) folderList.get(f);
											String folderNm = (String) folderObj.get("folderNm");
											if (folderNm.contentEquals(dirNm)) {
												JSONArray fileList = (JSONArray) folderObj.get("fileList");
												fileList.add(fileList.size(), resObj);
												existin = true;
											}
										}
										if (!existin) {
											JSONObject newFolderObj = new JSONObject();
											JSONArray newFileList = new JSONArray();
											newFileList.add(resObj);
											newFolderObj.put("folderNm", dirNm);
											newFolderObj.put("fileList", newFileList);
											folderList.add(newFolderObj);
										}
										exist = true;
									}
								}
								if (!exist) {
									JSONObject sarMap = new JSONObject();
									sarMap.put("date", date);
									JSONArray folderList = new JSONArray();
									JSONObject folderObj = new JSONObject();
									folderObj.put("folderNm", dirNm);
									JSONArray fileList = new JSONArray();
									fileList.add(resObj);
									folderObj.put("fileList", fileList);
									folderList.add(folderObj);
									sarMap.put("folderList", folderList);
									sarArry.add(sarMap);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				result.add(kompsatObj);
				result.add(landsatObj);
				result.add(sentinelObj);
				result.add(casObj);
				result.add(sarObj);
			}
		}
		return result;
	}

	@Override
	public JSONObject selectUsgsDisaterList(CMSC003VO cmsc003vo) {

		JSONObject result = new JSONObject();
		result.put("Flood", new JSONArray()); // 수해
		result.put("Earthquake", new JSONArray()); // 지진
		result.put("MaritimeDisaster", new JSONArray()); // 해양재난
		result.put("Landslide", new JSONArray()); // 산사태
		result.put("ForestFire", new JSONArray()); // 산불
		result.put("RedTide", new JSONArray()); // 적조

		if (cmsc003vo.getDataKindEmergency() != null && cmsc003vo.getDataKindEmergency().contentEquals("on")) {
			// postgres
			List<EgovMap> usgsList = new ArrayList<EgovMap>();
			try {
				usgsList.addAll(usgsList.size(), (List<EgovMap>) cmsc003Mapper.selectUsgsEmergencyList(cmsc003vo));
				if (cmsc003vo.getSearchWork() != null) {
					usgsList.addAll(usgsList.size(),
							(List<EgovMap>) cmsc003Mapper.selectUsgsWorkEmergencyList(cmsc003vo));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (usgsList != null) {
				for (int i = 0; i < usgsList.size(); i++) {
					try {
						EgovMap usgs = usgsList.get(i);
						String path = (String) usgs.get("innerFileCoursNm");
						File file = new File(path);
						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						JSONObject resObj = new JSONObject();
						resObj.put("vidoNm", fName);
						resObj.put("ltopCrdntX", usgs.get("ltopCrdntX"));
						resObj.put("ltopCrdntY", usgs.get("ltopCrdntY"));
						resObj.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
						resObj.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
						resObj.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
						resObj.put("innerFileCoursNm", path);
						resObj.put("fullFileCoursNm", path);
						resObj.put("potogrfBeginDt", usgs.get("potogrfBeginDt").toString());
						resObj.put("potogrfEndDt", usgs.get("potogrfEndDt").toString());

						if (usgs.get("usgsWorkId") != null) {
							resObj.put("usgsWorkId", usgs.get("usgsWorkId"));
						} else {
							resObj.put("vidoId", usgs.get("vidoId"));
						}

						// set thumbnail
						File dir = file.getParentFile();
						File[] files = dir.listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.toUpperCase().endsWith(fName.toUpperCase() + "." + "PNG");
							}
						});
						if (files != null) {
							if (files.length > 0) {
								String thumbPath = dir.getAbsolutePath() + File.separator + files[0].getName();
								resObj.put("thumbnail", true);
								resObj.put("thumbnailFileCoursNm", thumbPath);
							} else {
								resObj.put("thumbnail", false);
							}
						} else {
							resObj.put("thumbnail", false);
						}

						String vidoCd = (String) usgs.get("potogrfVidoCd");
						if (cmsc003vo.getDataKindCurrent() != null) {
							if (vidoCd.contentEquals("3")) { // FLOOD
								JSONArray mapList = (JSONArray) result.get("Flood");
								mapList.add(mapList.size(), resObj);
							} else if (vidoCd.contentEquals("4")) { // LANDSLIDE
								JSONArray mapList = (JSONArray) result.get("Landslide");
								mapList.add(mapList.size(), resObj);
							} else if (vidoCd.contentEquals("5")) { // FOREST FIRE
								JSONArray mapList = (JSONArray) result.get("ForestFire");
								mapList.add(mapList.size(), resObj);
							} else if (vidoCd.contentEquals("10")) { // EARTHQUAKE
								JSONArray mapList = (JSONArray) result.get("Earthquake");
								mapList.add(mapList.size(), resObj);
							} else if (vidoCd.contentEquals("11")) { // MARITIMEDISASTER
								JSONArray mapList = (JSONArray) result.get("MaritimeDisaster");
								mapList.add(mapList.size(), resObj);
							} else if (vidoCd.contentEquals("20")) { // MARITIMEDISASTER
								JSONArray mapList = (JSONArray) result.get("RedTide");
								mapList.add(mapList.size(), resObj);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		return result;
	}

	@Override
	public JSONObject selectUsgsAnalysisList(CMSC003VO cmsc003vo) {

		JSONObject result = new JSONObject();

		JSONObject objObj = new JSONObject();
		JSONArray objRaster = new JSONArray();
		JSONArray objVector = new JSONArray();
		objObj.put("raster", objRaster);
		objObj.put("vector", objVector);

		JSONObject chgObj = new JSONObject();
		JSONArray chgRaster = new JSONArray();
		JSONArray chgVector = new JSONArray();
		chgObj.put("raster", chgRaster);
		chgObj.put("vector", chgVector);

		result.put("objectExt", objObj);
		result.put("changeDet", chgObj);

		if (cmsc003vo.getDataKindResult() != null && cmsc003vo.getDataKindResult().contentEquals("on")) { // 분석결과

			List<EgovMap> usgsList = new ArrayList<EgovMap>();
			try {
				usgsList.addAll((List<EgovMap>) cmsc003Mapper.selectUsgsCurrentList(cmsc003vo));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < usgsList.size(); i++) {
				EgovMap usgs = usgsList.get(i);
				int vidoId = (int) usgs.get("vidoId");
				// 객체추출
				try {
					List<EgovMap> objExtResultList = (List<EgovMap>) cmsc003Mapper.selectAIMdAnalsMapResut(vidoId);
					for (EgovMap analResult : objExtResultList) {
						// 영상 데이터
						String fileCoursNm = (String) analResult.get("fileCoursNm");
						String allFileNm = (String) analResult.get("allFileNm");
						String imagePath = fileCoursNm + "/" + allFileNm;
						File file = new File(imagePath);
						if (!file.exists()) {
							continue;
						}
						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						JSONObject resObj = new JSONObject();
						resObj.put("vidoNm", fName);
						resObj.put("ltopCrdntX", usgs.get("ltopCrdntX"));
						resObj.put("ltopCrdntY", usgs.get("ltopCrdntY"));
						resObj.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
						resObj.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
						resObj.put("innerFileCoursNm", usgs.get("innerFileCoursNm"));
						resObj.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
						resObj.put("fullFileCoursNm", imagePath);
						resObj.put("vidoId", vidoId);
						resObj.put("potogrfBeginDt", usgs.get("potogrfBeginDt").toString());
						resObj.put("potogrfEndDt", usgs.get("potogrfEndDt").toString());

						String tmpNm = allFileNm.toUpperCase().replace(".TIF", "");
						File analParent = new File(imagePath).getParentFile();
						File[] files = analParent.listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.toUpperCase().endsWith(tmpNm.toUpperCase() + "." + "PNG");
							}
						});
						if (files != null) {
							if (files.length > 0) {
								resObj.put("thumbnail", true);
								resObj.put("thumbnailFileCoursNm", imagePath.replace(".tif", ".png"));
							} else {
								// create thumbnail
								String thumbPath = imagePath.replace(".tif", ".png");
								GImageProcessor gImageProcessor = new GImageProcessor();
								gImageProcessor.createThumbnailImage(imagePath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

								resObj.put("thumbnail", true);
								resObj.put("thumbnailFileCoursNm", thumbPath);
							}
						} else {
							// create thumbnail
							String thumbPath = imagePath.replace(".tif", ".png");
							GImageProcessor gImageProcessor = new GImageProcessor();
							gImageProcessor.createThumbnailImage(imagePath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
									thumbPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
									GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

							resObj.put("thumbnail", true);
							resObj.put("thumbnailFileCoursNm", thumbPath);
						}
						objRaster.add(resObj);

						// 벡터 데이터
						String vectorNm = (String) analResult.get("vectorFileNm");
						String vectorPath = (String) analResult.get("vectorFileCoursNm");
						if (vectorNm != null && vectorPath != null) {
							String vectorFilePath = vectorPath.replace("\\", "/") + vectorNm;
							File vFile = new File(vectorFilePath);
							if (!vFile.exists()) {
								continue;
							}

							JSONObject resVecObj = new JSONObject();
							resVecObj.put("vidoNm", fName);
							resVecObj.put("ltopCrdntX", cmsc003vo.getUly5179());
							resVecObj.put("ltopCrdntY", cmsc003vo.getUlx5179());
							resVecObj.put("rbtmCrdntX", cmsc003vo.getLry5179());
							resVecObj.put("rbtmCrdntY", cmsc003vo.getLrx5179());
							resVecObj.put("mapPrjctnCn", defaultEPSG);
							resVecObj.put("vectorFileCoursNm", vectorFilePath);
							resVecObj.put("vidoId", vidoId);
							resVecObj.put("potogrfBeginDt", usgs.get("potogrfBeginDt").toString());
							resVecObj.put("potogrfEndDt", usgs.get("potogrfEndDt").toString());

							File vFile2 = new File(vectorFilePath);
							if (!vFile2.exists()) {
								resVecObj.put("thumbnail", false);
							} else {
								resVecObj.put("thumbnail", true);
								resVecObj.put("thumbnailFileCoursNm", vectorFilePath);
							}
							objVector.add(resVecObj);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				// 변화탐지
				try {
					List<EgovMap> chgDetResultList = (List<EgovMap>) cmsc003Mapper.selectChangeDetctResult(vidoId);
					for (EgovMap analResult : chgDetResultList) {
						// 영상 데이터
						String fileCoursNm = (String) analResult.get("fileCoursNm");
						String allFileNm = (String) analResult.get("allFileNm");
						String imagePath = fileCoursNm + "/" + allFileNm;
						File file = new File(imagePath);
						if (!file.exists()) {
							continue;
						}
						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						JSONObject resObj = new JSONObject();
						resObj.put("vidoNm", fName);
						resObj.put("ltopCrdntX", usgs.get("ltopCrdntX"));
						resObj.put("ltopCrdntY", usgs.get("ltopCrdntY"));
						resObj.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
						resObj.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
						resObj.put("innerFileCoursNm", usgs.get("innerFileCoursNm"));
						resObj.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
						resObj.put("fullFileCoursNm", imagePath);
						resObj.put("vidoId", vidoId);
						resObj.put("potogrfBeginDt", usgs.get("potogrfBeginDt").toString());
						resObj.put("potogrfEndDt", usgs.get("potogrfEndDt").toString());

						String tmpNm = allFileNm.toUpperCase().replace(".TIF", "");
						File analParent = new File(imagePath).getParentFile();
						File[] files = analParent.listFiles(new FilenameFilter() {

							@Override
							public boolean accept(File dir, String name) {
								return name.toUpperCase().endsWith(tmpNm.toUpperCase() + "." + "PNG");
							}
						});
						if (files != null) {
							if (files.length > 0) {
								resObj.put("thumbnail", true);
								resObj.put("thumbnailFileCoursNm", imagePath.replace(".tif", ".png"));
							} else {

								// create thumbnail
								String thumbPath = imagePath.replace(".tif", ".png");
								GImageProcessor gImageProcessor = new GImageProcessor();
								gImageProcessor.createThumbnailImage(imagePath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

								resObj.put("thumbnail", true);
								resObj.put("thumbnailFileCoursNm", thumbPath);
							}
						} else {
							// create thumbnail
							String thumbPath = imagePath.replace(".tif", ".png");
							GImageProcessor gImageProcessor = new GImageProcessor();
							gImageProcessor.createThumbnailImage(imagePath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
									thumbPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
									GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

							resObj.put("thumbnail", true);
							resObj.put("thumbnailFileCoursNm", thumbPath);
						}
						chgRaster.add(resObj);

						// 벡터 데이터
						String vectorNm = (String) analResult.get("vectorFileNm");
						String vectorPath = (String) analResult.get("vectorFileCoursNm");
						if (vectorNm != null && vectorPath != null) {
							String vectorFilePath = vectorPath.replace("\\", "/") + vectorNm;
							File vFile = new File(vectorFilePath);
							if (!vFile.exists()) {
								continue;
							}

							JSONObject resVecObj = new JSONObject();
							resVecObj.put("vidoNm", fName);
							resVecObj.put("ltopCrdntX", cmsc003vo.getUly5179());
							resVecObj.put("ltopCrdntY", cmsc003vo.getUlx5179());
							resVecObj.put("rbtmCrdntX", cmsc003vo.getLry5179());
							resVecObj.put("rbtmCrdntY", cmsc003vo.getLrx5179());
							resVecObj.put("mapPrjctnCn", defaultEPSG);
							resVecObj.put("vectorFileCoursNm", vectorFilePath);
							resVecObj.put("vidoId", vidoId);
							resVecObj.put("potogrfBeginDt", usgs.get("potogrfBeginDt").toString());
							resVecObj.put("potogrfEndDt", usgs.get("potogrfEndDt").toString());

							File vFile2 = new File(vectorFilePath);
							if (!vFile2.exists()) {
								resVecObj.put("thumbnail", false);
							} else {
								resVecObj.put("thumbnail", true);
								resVecObj.put("thumbnailFileCoursNm", vectorFilePath);
							}
							chgVector.add(resVecObj);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject selectUsgsWorkList(CMSC003VO cmsc003vo) {
		JSONObject result = new JSONObject();
		result.put("existing", new JSONObject()); // 기구축자료
		result.put("disaster", new JSONObject()); // 긴급영상
		result.put("analysis", new JSONObject()); // 분석결과

		List<EgovMap> usgsList = new ArrayList<EgovMap>();
		if (cmsc003vo.getDataKindCurrent() != null) {
			try {
				usgsList.addAll(usgsList.size(), (List<EgovMap>) cmsc003Mapper.selectUsgsWorkCurrentList(cmsc003vo));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if (cmsc003vo.getDataKindEmergency() != null) {
			try {
				usgsList.addAll(usgsList.size(), (List<EgovMap>) cmsc003Mapper.selectUsgsWorkEmergencyList(cmsc003vo));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (usgsList != null) {
			JSONObject existingJSON = (JSONObject) result.get("existing");
			JSONObject disasterJSON = (JSONObject) result.get("disaster");
			JSONObject analsJSON = (JSONObject) result.get("analysis");

			// 시연용(Postgre에서 행정망 데이터 조회) -> 필요없을 시 아래 7줄 주석
			List<EgovMap> airOrientalList = new ArrayList<EgovMap>(); // 항공영상
			List<EgovMap> ortOrientalList = new ArrayList<EgovMap>(); // 정사영상
			List<EgovMap> demList = new ArrayList<EgovMap>(); // DEM

			existingJSON.put("airOrientalMap", airOrientalList);
			existingJSON.put("ortOrientalMap", ortOrientalList);
			existingJSON.put("dem", demList);

			// 영상자료
			List<EgovMap> kompsatList = new ArrayList<EgovMap>(); // 위성영상
			List<EgovMap> landsatList = new ArrayList<EgovMap>(); // 위성영상
			List<EgovMap> sentinelList = new ArrayList<EgovMap>(); // 위성영상
			List<EgovMap> casList = new ArrayList<EgovMap>(); // 위성영상

			existingJSON.put("kompsat", kompsatList);
			existingJSON.put("landsat", landsatList);
			existingJSON.put("sentinel", sentinelList);
			existingJSON.put("cas", casList);
			// 재난영상
			List<EgovMap> floodList = new ArrayList<EgovMap>(); // 수해
			List<EgovMap> forestFireList = new ArrayList<EgovMap>(); // 산불
			List<EgovMap> landsList = new ArrayList<EgovMap>(); // 산사태
			List<EgovMap> earthquakeList = new ArrayList<EgovMap>(); // 지진
			List<EgovMap> maritimeDisasterList = new ArrayList<EgovMap>(); // 해양재난
			List<EgovMap> redTideList = new ArrayList<EgovMap>(); // 적조

			disasterJSON.put("Flood", floodList);
			disasterJSON.put("ForestFire", forestFireList);
			disasterJSON.put("Landslide", landsList);
			disasterJSON.put("Earthquake", earthquakeList);
			disasterJSON.put("MaritimeDisaster", maritimeDisasterList);
			disasterJSON.put("RedTide", redTideList);

			// 분석결과데이터
			// 객체추출결과
			JSONObject objectExt = new JSONObject();
			List<EgovMap> objectExtRasterList = new ArrayList<EgovMap>();
			List<EgovMap> objectExtVectorList = new ArrayList<EgovMap>();
			objectExt.put("raster", objectExtRasterList);
			objectExt.put("vector", objectExtVectorList);
			analsJSON.put("objectExt", objectExt);

			// 변화탐지결과
			JSONObject changeDet = new JSONObject();
			List<EgovMap> changeDetRasterList = new ArrayList<EgovMap>();
			List<EgovMap> changeDetVectorList = new ArrayList<EgovMap>();
			changeDet.put("raster", changeDetRasterList);
			changeDet.put("vector", changeDetVectorList);
			analsJSON.put("changeDet", changeDet);

			for (int i = 0; i < usgsList.size(); i++) {
				try {
					EgovMap usgs = usgsList.get(i);
					String path = (String) usgs.get("innerFileCoursNm");
					File file = new File(path);
					File dir = file.getParentFile();
					// set vidoNm
					String vidoCd = (String) usgs.get("potogrfVidoCd");
					String ext = FilenameUtils.getExtension(file.getName());
					String fName = file.getName().replace("." + ext, "");

					if (cmsc003vo.getDataKindCurrent() != null) {
						if (vidoCd.contentEquals("0")) {
							casList.add(usgs);
						} else if (vidoCd.contentEquals("1")) {
							kompsatList.add(usgs);
						} else if (vidoCd.contentEquals("2")) {
							landsatList.add(usgs);
						} else if (vidoCd.contentEquals("6")) {
							sentinelList.add(usgs);
						} else if (vidoCd.contentEquals("7")) {
							airOrientalList.add(usgs);
						} else if (vidoCd.contentEquals("8")) {
							ortOrientalList.add(usgs);
						} else if (vidoCd.contentEquals("9")) {
							demList.add(usgs);
						}
					}
					if (cmsc003vo.getDataKindEmergency() != null) {
						if (vidoCd.contentEquals("3")) {
							floodList.add(usgs);
						}
						if (vidoCd.contentEquals("4")) {
							landsList.add(usgs);
						}
						if (vidoCd.contentEquals("5")) {
							forestFireList.add(usgs);
						}
						if (vidoCd.contentEquals("20")) {
							redTideList.add(usgs);
						}
					}
					usgs.put("vidoNm", fName);
					usgs.put("fullFileCoursNm", path);

					// 분석결과 조회
					if (cmsc003vo.getDataKindResult() != null) {
						int vidoId = (int) usgs.get("vidoId");
						// 객체추출
						try {
							List<EgovMap> objExtResultList = (List<EgovMap>) cmsc003Mapper
									.selectAIMdAnalsMapResut(vidoId);
							for (EgovMap analResult : objExtResultList) {
								// 영상 데이터
								String fileCoursNm = (String) analResult.get("fileCoursNm");
								String allFileNm = (String) analResult.get("allFileNm");
								String imagePath = fileCoursNm + "/" + allFileNm;
								File fFile = new File(imagePath);
								if (!fFile.exists()) {
									continue;
								}

								EgovMap imgUsgs = new EgovMap();
								imgUsgs.putAll(analResult);
								imgUsgs.put("vidoNm", fName);
								imgUsgs.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));

								imgUsgs.put("ltopCrdntX", usgs.get("ltopCrdntX"));
								imgUsgs.put("ltopCrdntY", usgs.get("ltopCrdntY"));
								imgUsgs.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
								imgUsgs.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
								imgUsgs.put("fullFileCoursNm", imagePath);

								String tmpNm = allFileNm.toUpperCase().replace(".TIF", "");
								File analParent = new File(imagePath).getParentFile();
								File[] files = analParent.listFiles(new FilenameFilter() {
									@Override
									public boolean accept(File dir, String name) {
										return name.toUpperCase().endsWith(tmpNm.toUpperCase() + "." + "PNG");
									}
								});
								if (files != null) {
									if (files.length > 0) {
										imgUsgs.put("thumbnail", true);
										imgUsgs.put("thumbnailFileCoursNm", imagePath.replace(".tif", ".png"));
									} else {
										// create thumbnail
										String thumbPath = imagePath.replace(".tif", ".png");
										GImageProcessor gImageProcessor = new GImageProcessor();
										gImageProcessor.createThumbnailImage(imagePath,
												GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbPath,
												GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
												GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

										imgUsgs.put("thumbnail", true);
										imgUsgs.put("thumbnailFileCoursNm", thumbPath);
									}
								} else {
									// create thumbnail
									String thumbPath = imagePath.replace(".tif", ".png");
									GImageProcessor gImageProcessor = new GImageProcessor();
									gImageProcessor.createThumbnailImage(imagePath,
											GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbPath,
											GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
											GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

									imgUsgs.put("thumbnail", true);
									imgUsgs.put("thumbnailFileCoursNm", thumbPath);
								}
								objectExtRasterList.add(imgUsgs);

								// 벡터 데이터
								String vectorNm = (String) analResult.get("vectorFileNm");
								String vectorPath = (String) analResult.get("vectorFileCoursNm");
								if (vectorNm != null && vectorPath != null) {
									String vectorFilePath = vectorPath.replace("\\", "/") + vectorNm;
									File vFile = new File(vectorFilePath);
									if (!vFile.exists()) {
										continue;
									}
									EgovMap vectUsgs = new EgovMap();
									vectUsgs.putAll(analResult);
									vectUsgs.put("vidoNm", fName);
									vectUsgs.put("mapPrjctnCn", defaultEPSG);
									vectUsgs.put("ltopCrdntX", cmsc003vo.getUly5179());
									vectUsgs.put("ltopCrdntY", cmsc003vo.getUlx5179());
									vectUsgs.put("rbtmCrdntX", cmsc003vo.getLry5179());
									vectUsgs.put("rbtmCrdntY", cmsc003vo.getLrx5179());
									vectUsgs.put("vectorFileCoursNm", vectorFilePath);
									File vFile2 = new File(vectorFilePath);
									if (!vFile2.exists()) {
										vectUsgs.put("thumbnail", false);
									} else {
										vectUsgs.put("thumbnail", true);
										vectUsgs.put("thumbnailFileCoursNm", vectorFilePath);
									}
									objectExtVectorList.add(vectUsgs);
								}
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						// 변화탐지
						try {
							List<EgovMap> chgDetResultList = (List<EgovMap>) cmsc003Mapper
									.selectChangeDetctResult(vidoId);
							for (EgovMap analResult : chgDetResultList) {
								// 영상 데이터
								String fileCoursNm = (String) analResult.get("fileCoursNm");
								String allFileNm = (String) analResult.get("allFileNm");
								String imagePath = fileCoursNm + "/" + allFileNm;

								File fFile = new File(imagePath);
								if (!fFile.exists()) {
									continue;
								}

								EgovMap imgUsgs = new EgovMap();
								imgUsgs.putAll(analResult);
								imgUsgs.put("vidoNm", fName);
								imgUsgs.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
								imgUsgs.put("ltopCrdntX", usgs.get("ltopCrdntX"));
								imgUsgs.put("ltopCrdntY", usgs.get("ltopCrdntY"));
								imgUsgs.put("rbtmCrdntX", usgs.get("rbtmCrdntX"));
								imgUsgs.put("rbtmCrdntY", usgs.get("rbtmCrdntY"));
								imgUsgs.put("fullFileCoursNm", imagePath);

								String tmpNm = allFileNm.toUpperCase().replace(".TIF", "");

								File analParent = new File(imagePath).getParentFile();
								File[] files = analParent.listFiles(new FilenameFilter() {

									@Override
									public boolean accept(File dir, String name) {
										return name.toUpperCase().endsWith(tmpNm.toUpperCase() + "." + "PNG");
									}
								});
								if (files != null) {
									if (files.length > 0) {
										imgUsgs.put("thumbnail", true);
										imgUsgs.put("thumbnailFileCoursNm", imagePath.replace(".tif", ".png"));
									} else {

										// create thumbnail
										String thumbPath = imagePath.replace(".tif", ".png");
										GImageProcessor gImageProcessor = new GImageProcessor();
										gImageProcessor.createThumbnailImage(imagePath,
												GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbPath,
												GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
												GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

										imgUsgs.put("thumbnail", true);
										imgUsgs.put("thumbnailFileCoursNm", thumbPath);
									}
								} else {
									// create thumbnail
									String thumbPath = imagePath.replace(".tif", ".png");
									GImageProcessor gImageProcessor = new GImageProcessor();
									gImageProcessor.createThumbnailImage(imagePath,
											GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbPath,
											GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
											GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

									imgUsgs.put("thumbnail", true);
									imgUsgs.put("thumbnailFileCoursNm", thumbPath);
								}
								changeDetRasterList.add(imgUsgs);

								// 벡터 데이터
								String vectorNm = (String) analResult.get("vectorFileNm");
								String vectorPath = (String) analResult.get("vectorFileCoursNm");
								if (vectorNm != null && vectorPath != null) {
									String vectorFilePath = vectorPath.replace("\\", "/") + vectorNm;
									File vFile = new File(vectorFilePath);
									if (!vFile.exists()) {
										continue;
									}
									EgovMap vectUsgs = new EgovMap();
									vectUsgs.putAll(analResult);
									vectUsgs.put("vidoNm", fName);
									vectUsgs.put("mapPrjctnCn", defaultEPSG);
									vectUsgs.put("ltopCrdntX", cmsc003vo.getUly5179());
									vectUsgs.put("ltopCrdntY", cmsc003vo.getUlx5179());
									vectUsgs.put("rbtmCrdntX", cmsc003vo.getLry5179());
									vectUsgs.put("rbtmCrdntY", cmsc003vo.getLrx5179());
									vectUsgs.put("vectorFileCoursNm", vectorFilePath);

									File vFile2 = new File(vectorFilePath);
									if (!vFile2.exists()) {
										vectUsgs.put("thumbnail", false);
									} else {
										vectUsgs.put("thumbnail", true);
										vectUsgs.put("thumbnailFileCoursNm", vectorFilePath);
									}
									changeDetVectorList.add(vectUsgs);
								}
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

					// set thumbnail
					if (dir != null) {
						File[] files = dir.listFiles(new FilenameFilter() {

							@Override
							public boolean accept(File dir, String name) {
								return name.toUpperCase().endsWith(fName.toUpperCase() + "." + "PNG");
							}
						});
						if (files != null) {
							if (files.length > 0) {
								String thumbPath = dir.getAbsolutePath() + File.separator + files[0].getName();
								usgs.put("thumbnail", true);
								usgs.put("thumbnailFileCoursNm", thumbPath);
							} else {
								usgs.put("thumbnail", false);
							}
						} else {
							usgs.put("thumbnail", false);
						}
					}
				} catch (

				Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return result;

	}

	@Override
	public List<?> selectG001List() {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectG001List();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<?> selectG010List(CMSC003VO cmsc003VO) {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectG010List(cmsc003VO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<?> selectG011List(CMSC003VO cmsc003VO) {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectG011List(cmsc003VO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<?> selectG001Geom(CMSC003VO cmsc003VO) {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectG001Geom(cmsc003VO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<?> selectG010Geom(CMSC003VO cmsc003VO) {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectG010Geom(cmsc003VO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<?> selectG011Geom(CMSC003VO cmsc003VO) {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectG011Geom(cmsc003VO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	
	//긴급공간정보생성 impl
	@Override
	@Transactional
	public JSONObject cmsc003createData(JSONObject createObj)
			throws SQLException, IOException, NoSuchAuthorityCodeException, FactoryException, TransformException {

		// result
		JSONObject existingJSON = new JSONObject();
		JSONObject disasterJSON = new JSONObject();
		JSONObject analsJSON = new JSONObject();

		JSONArray digitalArry = new JSONArray(); // 수치지도
		JSONArray graphicArry = new JSONArray(); // 격자통계

		existingJSON.put("digitalMap", digitalArry);
		existingJSON.put("graphicsMap", graphicArry);

		JSONArray kompsatArry = new JSONArray(); // Kompsat
		JSONArray landsatArry = new JSONArray(); // Landsat
		JSONArray sentinelArry = new JSONArray(); // Sentinel
		JSONArray casArry = new JSONArray(); // CAS
		JSONArray sarArry = new JSONArray(); // SAR

		JSONObject kompsatObj = new JSONObject();
		JSONObject landsatObj = new JSONObject();
		JSONObject sentinelObj = new JSONObject();
		JSONObject casObj = new JSONObject();
		JSONObject sarObj = new JSONObject();

		kompsatObj.put("satNm", "Kompsat");
		kompsatObj.put("map", kompsatArry);

		landsatObj.put("satNm", "Landsat");
		landsatObj.put("map", landsatArry);

		sentinelObj.put("satNm", "Sentinel");
		sentinelObj.put("map", sentinelArry);

		casObj.put("satNm", "CAS");
		casObj.put("map", casArry);
		
		sarObj.put("satNm", "SAR");
		sarObj.put("map", sarArry);

		JSONArray sateArry = new JSONArray(); // 위성영상
		sateArry.add(kompsatObj);
		sateArry.add(landsatObj);
		sateArry.add(sentinelObj);
		sateArry.add(casObj);
		sateArry.add(sarObj);

		JSONArray airMapList = new JSONArray(); // 항공영상
		JSONArray ortMapList = new JSONArray(); // 정사영상
		JSONArray demMapList = new JSONArray(); // DEM

		JSONArray floodArry = new JSONArray(); // 수해
		JSONArray earthquakeArry = new JSONArray(); // 지진
		JSONArray maritimeDisasterArry = new JSONArray(); // 해양재난
		JSONArray landslideArry = new JSONArray(); // 산사태
		JSONArray forestFireArry = new JSONArray(); // 산불
		JSONArray redTideArry = new JSONArray(); // 적조

		disasterJSON.put("Flood", floodArry);
		disasterJSON.put("Earthquake", earthquakeArry);
		disasterJSON.put("MaritimeDisaster", maritimeDisasterArry);
		disasterJSON.put("Landslide", landslideArry);
		disasterJSON.put("ForestFire", forestFireArry);
		disasterJSON.put("RedTide", redTideArry);

		JSONObject objObj = new JSONObject(); // 객체추출
		JSONArray objRaster = new JSONArray();
		JSONArray objVector = new JSONArray();
		objObj.put("raster", objRaster);
		objObj.put("vector", objVector);

		JSONObject chgObj = new JSONObject(); // 변화탐지
		JSONArray chgRaster = new JSONArray();
		JSONArray chgVector = new JSONArray();
		chgObj.put("raster", chgRaster);
		chgObj.put("vector", chgVector);

		analsJSON.put("objectExt", objObj);
		analsJSON.put("changeDet", chgObj);

		JSONObject result = new JSONObject();
		result.put("existing", existingJSON); // 기구축자료
		result.put("disaster", disasterJSON); // 긴급영상
		result.put("analysis", analsJSON); // 분석결과

		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
		CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
		CoordinateReferenceSystem targetCrs = factory.createCoordinateReferenceSystem(defaultEPSG); // 5179

		// 긴급공간정보생성
		JSONObject info = (JSONObject) createObj.get("createInfo");
		JSONObject roi = (JSONObject) info.get("roi");

		// roi 5179 bbox
		JSONObject roi5179 = (JSONObject) roi.get("roi5179");
		double lrx5179 = (double) roi5179.get("lrx");
		double lry5179 = (double) roi5179.get("lry");
		double ulx5179 = (double) roi5179.get("ulx");
		double uly5179 = (double) roi5179.get("uly");

		String bbox = "bbox=" + ulx5179 + "," + lry5179 + "," + lrx5179 + "," + uly5179;
		ReferencedEnvelope envelope5179 = new ReferencedEnvelope(ulx5179, lrx5179, lry5179, uly5179, targetCrs);

		String outDir = "";
		outDir += rootPath;

		// 재난유형명 검색
		String disasterCd = (String) createObj.get("disasterCd");
		List disasterMap = cmsc003Mapper.selectTcMsfrtnTyInfo(disasterCd);
		EgovMap disasterInfo = (EgovMap) disasterMap.get(0);
		String disasterNm = (String) disasterInfo.get("msfrtnTyNm"); // 한글 재난명
		String disasterId = (String) createObj.get("disasterId");

		String disasterNmUser = (String) createObj.get("disasterNmUser"); // 사용자 입력 재난명

		String yy = disasterId.substring(0, 2);
		String mm = disasterId.substring(2, 4);
		String dd = disasterId.substring(4, 6);
		String dataStr = "20" + yy + "-" + mm + "-" + dd;

		EgovMap reqMap = selectByRequestId(disasterId);

		// disasterNm;
		String datasetNm = disasterNmUser;
		String addr = reqMap.get("ctprvnNm") + " " + reqMap.get("sggNm") + " " + reqMap.get("emdNm");

		String disasterNmEn = ""; // 영문 재난명
		if (disasterNm.contains("지진")) {
			disasterNmEn = "EARTHQUAKE";
		} else if (disasterNm.contains("산불")) {
			disasterNmEn = "FORESTFIRE";
		} else if (disasterNm.contains("수해")) {
			disasterNmEn = "FLOOD";
		} else if (disasterNm.contains("산사태")) {
			disasterNmEn = "LANDSLIDE";
		} else if (disasterNm.contains("해양재난")) {
			disasterNmEn = "MARITIMEDISASTER";
		} else if (disasterNm.contains("적조")) {
			disasterNmEn = "REDTIDE";
		} else {
			disasterNmEn = "TMP";
		}
		outDir += disasterNmEn + "/" + disasterId + "/" + disasterId;

		File outDirFile = new File(outDir);
		if (!outDirFile.exists()) {
			// 중복 폴더 존재 시 삭제
//			FileUtils.deleteDirectory(outDirFile);

			outDirFile.setExecutable(true, true);
			outDirFile.setReadable(true, true);
			outDirFile.setWritable(true, true);
			outDirFile.mkdirs();
		}
		
		System.out.println("데이터 셋 완료");

		// insert "TN_MSFRTN_INFO"
		CMSC003VO4 cmsc003vo4 = new CMSC003VO4();
		cmsc003vo4.setMsfrtnId(disasterId);
		cmsc003vo4.setDatasetCoursNm(rootPath + disasterNmEn + "/" + disasterId);
		cmsc003vo4.setDatasetNm(disasterNmUser);

		//긴급공간정보 생성 잘되는지 확인 필요 2024.02.26로그 찍기
		
		System.out.println(disasterId);
		System.out.println(rootPath + disasterNmEn + "/" + disasterId);
		System.out.println(disasterNmUser);
		
		EgovMap dataset = cmsc003Mapper.selectMsfrtnDataset(cmsc003vo4);
		if (dataset != null) { // 긴급공간정보가 존재할 경우
//			cmsc003Mapper.deleteMsfrtnDataset(cmsc003vo4);
//			cmsc003Mapper.deleteDatasetInfo(cmsc003vo4);
			cmsc003Mapper.updateMsfrtnDataset(cmsc003vo4);
			cmsc003Mapper.updateDatasetInfo2(cmsc003vo4);
			System.out.println("TN_MSFRTN_DATASET 업데이트 완료");
		} else { // 긴급공간정보가 존재하지 않는 경우
			cmsc003Mapper.insertMsfrtnDataset(cmsc003vo4);
			System.out.println("TN_MSFRTN_DATASET 인서트 완료");
		}

		// mapNm
		String mapNm = (String) createObj.get("mapNm");

		// Existing
		JSONObject existing = (JSONObject) createObj.get("existing");

		// 정사영상
		boolean autoMosa = (boolean) createObj.get("autoMosasic"); // 모자이크 여부
		if (autoMosa) { // 1. 자동모자이크
			// 정사영상
			JSONArray ortOrientalList = (JSONArray) existing.get("ortOrientalMap");
			if (ortOrientalList.size() > 0) {
				GridCoverageFactory gcf = new GridCoverageFactory();
				for (int a = 0; a < ortOrientalList.size(); a++) {
					JSONObject ortObj = (JSONObject) ortOrientalList.get(a);
					String year = (String) ortObj.get("year");
					JSONArray maps = (JSONArray) ortObj.get("maps");
					for (int i = 0; i < maps.size(); i++) {
						JSONObject map = (JSONObject) maps.get(i);
						String dpi = (String) map.get("dpi");
						String ortOriDir = outDir + "/Existing/Image/Aerial/" + year + "/" + dpi;

						// create directory
						File dirFile = new File(ortOriDir);
						dirFile.setExecutable(true, true);
						dirFile.setReadable(true, true);
						dirFile.setWritable(true, true);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}

						JSONArray files = (JSONArray) map.get("map");
						Map<String, GridCoverage2D> tifMap = new HashMap();
						String mosaicResultPath = ortOriDir + "/MosaicResult_" + year + "_" + dpi + ".tif";
						String mosaicOrgPath = "/MosaicResult_" + year + "_" + dpi + "_org.tif";
						try {
							for (int f = 0; f < files.size(); f++) {
								JSONObject file = (JSONObject) files.get(f);
								String mapPrjctnCn = (String) file.get("mapPrjctnCn"); // 원본파일좌표계
								String fullFileCoursNm = (String) file.get("fullFileCoursNm"); // 원본파일경로

								ReferencedEnvelope orgEnv = new ReferencedEnvelope(
										Double.valueOf(file.get("ltopCrdntY").toString()),
										Double.valueOf(file.get("rbtmCrdntY").toString()),
										Double.valueOf(file.get("rbtmCrdntX").toString()),
										Double.valueOf(file.get("ltopCrdntX").toString()),
										factory.createCoordinateReferenceSystem(mapPrjctnCn));

								CoordinateReferenceSystem sourceCrs = factory
										.createCoordinateReferenceSystem(mapPrjctnCn);
								MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
								Envelope transEnv = JTS.transform(orgEnv, transform);

								if (file.get("usgsWorkId") != null) { // usgs_work
									String downloadPath = fullFileCoursNm;
									File fullFile = new File(fullFileCoursNm);
									if (!fullFile.exists()) {
										continue;
									}
									BufferedImage image = ImageIO.read(fullFile);
									GridCoverage2D coverage = gcf.create("coverage", image, orgEnv);
									fullFile.delete();
									GTiffDataCropper.writeGeoTiff(coverage, downloadPath);
									tifMap.put(downloadPath, coverage);

									if (f == 0) {
										String dirNm = new File(downloadPath).getParent();
										mosaicOrgPath = dirNm + mosaicOrgPath;
										// 이름 중복 시
										File outFile = new File(mosaicOrgPath);
										String fileNm = outFile.getName().replace(".tif", "");
										if (outFile.exists()) {
											final String nm = outFile.getName().replace(".tif", "");
											File upFile = outFile.getParentFile();
											File[] infiles = upFile.listFiles(new FilenameFilter() {
												@Override
												public boolean accept(File dir, String name) {
													return name.startsWith(nm) && !name.endsWith(".tif");
												}
											});
											if (infiles != null) {
												int length = infiles.length;
												fileNm += "(" + length + ")";
											}
											mosaicOrgPath = outFile.getParent() + "/" + fileNm + ".tif";
										}
									}
								} else { // gfra
									// tif 다운로드
									fullFileCoursNm = fullFileCoursNm.replace("/ort/", "ort/");
//									String downloadPath = fullFileCoursNm;
									String downloadPath = gFraDownPath + fullFileCoursNm;
									File downloadFile = new File(downloadPath); // ex)
																				// /home/TempData/01_GeoFraData/air/rgb/1966/0001/C/1966000001000C3600.tif

									if (!downloadFile.exists()) {
										// download file
										HttpDownloader downloader = new HttpDownloader();
										downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);

										String tfwPath = fullFileCoursNm.replace(".tif", ".tfw");
										String tfwDownPath = downloadPath.replace(".tif", ".tfw");
										downloader.download(httpBoundary, gFraUrl, tfwDownPath, tfwPath); // twf
									}
									if (!downloadFile.exists()) {
										continue;
									}
									BufferedImage image = ImageIO.read(downloadFile);
									GridCoverage2D coverage = gcf.create("coverage", image, orgEnv);
									downloadFile.delete();
									GTiffDataCropper.writeGeoTiff(coverage, downloadPath);
									tifMap.put(downloadPath, coverage);

									if (f == 0) {
										String dirNm = new File(downloadPath).getParent();
										mosaicOrgPath = dirNm + mosaicOrgPath;

										// 이름 중복 시
										File outFile = new File(mosaicOrgPath);
										String fileNm = outFile.getName().replace(".tif", "");
										if (outFile.exists()) {
											final String nm = outFile.getName().replace(".tif", "");
											File upFile = outFile.getParentFile();
											File[] infiles = upFile.listFiles(new FilenameFilter() {
												@Override
												public boolean accept(File dir, String name) {
													return name.startsWith(nm) && !name.endsWith(".tif");
												}
											});
											if (infiles != null) {
												int length = infiles.length;
												fileNm += "(" + length + ")";
											}
											mosaicOrgPath = outFile.getParent() + "/" + fileNm + ".tif";
										}
									}
								}
							}

							// 자동 모자이크
							String dpitmp = dpi.replace("cm", "");
							int dipInt = Integer.valueOf(dpitmp);
							GImageProcessor g = new GImageProcessor();
							GImageProcessor.ProcessCode procCode = g.autoMosaic(tifMap, mosaicOrgPath, dipInt);

							// crop
							if ("SUCCESS".equals(procCode.name())) {
								CoordinateReferenceSystem mosaicCrs = g.getCrs();
								Integer epsg = CRS.lookupEpsgCode(mosaicCrs, false);
								String mosaicMapPrj = "EPSG:" + epsg;

								// insert usgs table
								CMSC003InsertVO insertVO = new CMSC003InsertVO();
								cisc001Service.insertUsgsOnlyVidoId(insertVO);
								int vidoId = cisc001Service.selectLastUsgsId();
								insertVO.setVidoId(vidoId);
								insertVO.setMapPrjctnCn(mosaicMapPrj);
								cisc001Service.insertUsgsMeta(insertVO);

								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
								String potogrfBeginDt = year + "-01-01";
								Date beginDate = formatter.parse(potogrfBeginDt);
								String potogrfEndDt = year + "-12-31";
								Date endDate = formatter.parse(potogrfEndDt);

								// insert usgs_work table
								CISC001WorkResultVO workVO = new CISC001WorkResultVO();
								workVO.setDisasterId(disasterId);
								workVO.setVidoId(vidoId);
								workVO.setPotogrfVidoCd("8");
								workVO.setPotogrfBeginDt(beginDate);
								workVO.setPotogrfEndDt(endDate);
								workVO.setVidoNm(new File(mosaicOrgPath).getName().replace(".tif", ""));
								workVO.setInnerFileCoursNm(mosaicOrgPath);
								workVO.setLtopCrdntX(g.getMaxY());
								workVO.setLtopCrdntY(g.getMinX());
								workVO.setRbtmCrdntX(g.getMinY());
								workVO.setRbtmCrdntY(g.getMaxX());
								workVO.setWorkKind("6"); // 모자이크
								cisc001Service.insertWorkResult2(workVO);

								// 긴급공간정보 생성
								GTiffDataCropper cropper = new GTiffDataCropper();
								String cropedPath = mosaicResultPath;

								ReferencedEnvelope orgEnv = new ReferencedEnvelope(g.getMinX(), g.getMaxX(),
										g.getMinY(), g.getMaxY(), g.getCrs());

								System.out.println(">>>>>>>>>>> orgEnv : " + orgEnv.toString());
								System.out.println(">>>>>>>>>>> envelope5179 : " + envelope5179.toString());

								GridCoverage2D cropedGc = cropper.referencingCrop(mosaicOrgPath, orgEnv, envelope5179,
										cropedPath, targetCrs);

								// create thumbnail
								GImageProcessor gImageProcessor = new GImageProcessor();
								String thumbnailPath = cropedPath.replace(".tif", ".png");
								gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

								// 좌표없는 tif 생성
								File cropFile = new File(cropedPath);
								BufferedImage image = ImageIO.read(cropFile);
								File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
								ImageIO.write(image, "tif", outputfile);

								Envelope2D cropedEnv = cropedGc.getEnvelope2D();
								double cminx = cropedEnv.getMinX();
								double cmaxx = cropedEnv.getMaxX();
								double cminy = cropedEnv.getMinY();
								double cmaxy = cropedEnv.getMaxY();

								// db insert
								CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
								dataSetVo.setDatasetNm(datasetNm);
								dataSetVo.setDataCd("DSCD121");
								dataSetVo.setLtopCrdntX(cmaxy);
								dataSetVo.setLtopCrdntY(cminx);
								dataSetVo.setRbtmCrdntX(cminy);
								dataSetVo.setRbtmCrdntY(cmaxx);

								dataSetVo.setRtopCrdntX(cmaxy);
								dataSetVo.setRtopCrdntY(cmaxx);
								dataSetVo.setLbtmCrdntX(cminy);
								dataSetVo.setLbtmCrdntY(cminx);

								dataSetVo.setMapPrjctnCn(defaultEPSG);
								dataSetVo.setMsfrtnTyNm(disasterNm);
								dataSetVo.setDatasetCoursNm(ortOriDir.replace(outDir, datasetDbPath + disasterId));
								dataSetVo.setRoiYn("Y");

								dataSetVo.setPotogrfBeginDt(year + "-01-01");
								dataSetVo.setPotogrfEndDt(year + "-12-31");

								Date date = java.sql.Date.valueOf(dataStr);
								dataSetVo.setUploadDt(date);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setAddr(addr);
								dataSetVo.setYear(year);
								dataSetVo.setDpi(dpi);
								dataSetVo.setFullFileCoursNm(cropedPath);

								String ext = cropedPath.substring(cropedPath.lastIndexOf(".") + 1);
								String fnm = cropFile.getName().replace("." + ext, "");
								dataSetVo.setFileNm(fnm);
								dataSetVo.setMsfrtnTyCd(disasterCd);
								dataSetVo.setFileTy(ext);
								dataSetVo.setFileKorNm("정사영상_" + year + "_" + dpi);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setMapNm(mapNm);
								
								
								//새롬  _RGB,CIR문자 추가  
//								String fnmLastStr = fnm.substring(fnm.length()-3, fnm.length()).toUpperCase();
//								
//								String korNm = "정사영상_" + year + "_" + dpi;
//								
//								if (fnmLastStr.contentEquals("RGB") || fnmLastStr.contentEquals("CIR")){
//									korNm = korNm + "_" + fnmLastStr;
//								} ;
//								
//								dataSetVo.setFileKorNm(korNm);
						
								// 긴급공간정보 생성 여부 확인
								EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
								
								System.out.println("정사영상 TN_DATASET_INFO 조회");
								System.out.println("selectDataset : " + selectDataset);
								
								if (selectDataset == null) { // 신규
									cmsc003Mapper.insertDatasetInfo(dataSetVo);
								} else { // 갱신
									dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
									cmsc003Mapper.updateDatasetInfo(dataSetVo);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else { // 2. 모자이크 x
			JSONArray ortOrientalList = (JSONArray) existing.get("ortOrientalMap");
			if (ortOrientalList.size() > 0) {
				for (int a = 0; a < ortOrientalList.size(); a++) {
					JSONObject ortObj = (JSONObject) ortOrientalList.get(a);
					String year = (String) ortObj.get("year");
					JSONArray maps = (JSONArray) ortObj.get("maps");
					for (int i = 0; i < maps.size(); i++) {
						JSONObject map = (JSONObject) maps.get(i);
						String dpi = (String) map.get("dpi");
						String ortOriDir = outDir + "/Existing/Image/Aerial/" + year + "/" + dpi;

						// create directory
						File dirFile = new File(ortOriDir);
						dirFile.setExecutable(true, true);
						dirFile.setReadable(true, true);
						dirFile.setWritable(true, true);
						if (!dirFile.exists()) {
							dirFile.mkdirs();
						}

						JSONArray files = (JSONArray) map.get("map");
						for (int f = 0; f < files.size(); f++) {
							JSONObject file = (JSONObject) files.get(f);
							try {
								String mapPrjctnCn = (String) file.get("mapPrjctnCn"); // 원본파일좌표계
								String fullFileCoursNm = (String) file.get("fullFileCoursNm"); // 원본파일경로

								ReferencedEnvelope orgEnv = new ReferencedEnvelope(
										Double.valueOf(file.get("ltopCrdntY").toString()),
										Double.valueOf(file.get("rbtmCrdntY").toString()),
										Double.valueOf(file.get("rbtmCrdntX").toString()),
										Double.valueOf(file.get("ltopCrdntX").toString()),
										factory.createCoordinateReferenceSystem(mapPrjctnCn));

								CoordinateReferenceSystem sourceCrs = factory
										.createCoordinateReferenceSystem(mapPrjctnCn);
								MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
								Envelope transEnv = JTS.transform(orgEnv, transform);

//								if (!transEnv.intersects(envelope5179)) {
//									continue;
//								} else 
								if (envelope5179.contains(transEnv)) {
									envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
											transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
								}

								if (file.get("usgsWorkId") != null) { // usgs_work
									String downloadPath = fullFileCoursNm;

									File fullFile = new File(fullFileCoursNm);
									if (!fullFile.exists()) {
										continue;
									}

									String potogrfBeginDt = (String) file.get("potogrfBeginDt"); // 촬영시작
									String potogrfEndDt = (String) file.get("potogrfEndDt"); // 촬영종료

									// crop & create file
									GTiffDataCropper cropper = new GTiffDataCropper();
									String fileName = (String) file.get("vidoNm");
									String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
									String cropedPath = ortOriDir + "/" + fileName + "_" + year + "_" + dpi + "." + ext;
									File cropFile = new File(cropedPath);

									GridCoverage2D cropedGc = cropper.referencingCrop(downloadPath, orgEnv,
											envelope5179, cropedPath, targetCrs);

									// create thumbnail
									GImageProcessor gImageProcessor = new GImageProcessor();
									String thumbnailPath = ortOriDir + "/" + fileName + "_" + year + "_" + dpi + ".png";
									gImageProcessor.createThumbnailImage(cropedPath,
											GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbnailPath,
											GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
											GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

									// 좌표없는 tif 생성
									BufferedImage image = ImageIO.read(cropFile);
									File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
									ImageIO.write(image, "tif", outputfile);

									// create response json
									Envelope2D cropedEnv = cropedGc.getEnvelope2D();
									double cminx = cropedEnv.getMinX();
									double cmaxx = cropedEnv.getMaxX();
									double cminy = cropedEnv.getMinY();
									double cmaxy = cropedEnv.getMaxY();

									// db insert
									CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
									dataSetVo.setDatasetNm(datasetNm);
									dataSetVo.setDataCd("DSCD121");
									dataSetVo.setLtopCrdntX(cmaxy);
									dataSetVo.setLtopCrdntY(cminx);
									dataSetVo.setRbtmCrdntX(cminy);
									dataSetVo.setRbtmCrdntY(cmaxx);

									dataSetVo.setRtopCrdntX(cmaxy);
									dataSetVo.setRtopCrdntY(cmaxx);
									dataSetVo.setLbtmCrdntX(cminy);
									dataSetVo.setLbtmCrdntY(cminx);

									dataSetVo.setMapPrjctnCn(defaultEPSG);
									dataSetVo.setMsfrtnTyNm(disasterNm);
									dataSetVo.setDatasetCoursNm(ortOriDir.replace(outDir, datasetDbPath + disasterId));
									dataSetVo.setRoiYn("Y");
									dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
									dataSetVo.setPotogrfEndDt(potogrfEndDt);

									Date date = java.sql.Date.valueOf(dataStr);
									dataSetVo.setUploadDt(date);
									dataSetVo.setMsfrtnId(disasterId);
									dataSetVo.setAddr(addr);
									dataSetVo.setYear(year);
									dataSetVo.setDpi(dpi);
									dataSetVo.setFullFileCoursNm(cropedPath);

									String fnm = cropFile.getName().replace("." + ext, "");
									dataSetVo.setFileNm(fnm);
									dataSetVo.setMsfrtnTyCd(disasterCd);
									dataSetVo.setFileTy(ext);
									dataSetVo.setFileKorNm("정사영상_" + year + "_" + dpi);
									dataSetVo.setMsfrtnId(disasterId);
									dataSetVo.setMapNm(mapNm);
									

									
									// 긴급공간정보 생성 여부 확인
									EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
									
									System.out.println("정사영상2 TN_DATASET_INFO 조회");
									System.out.println("selectDataset : " + selectDataset);
									
									if (selectDataset == null) { // 신규
										cmsc003Mapper.insertDatasetInfo(dataSetVo);
									} else { // 갱신
										dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
										cmsc003Mapper.updateDatasetInfo(dataSetVo);
									}
								} else { // gfra
									// tif 다운로드
									fullFileCoursNm = fullFileCoursNm.replace("/ort/", "ort/");
									String downloadPath = gFraDownPath + fullFileCoursNm;
									File downloadFile = new File(downloadPath); // ex)
																				// /home/TempData/01_GeoFraData/air/rgb/1966/0001/C/1966000001000C3600.tif
									if (!downloadFile.exists()) {
										// download file
										HttpDownloader downloader = new HttpDownloader();
										downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);

										String tfwPath = fullFileCoursNm.replace(".tif", ".tfw");
										String tfwDownPath = downloadPath.replace(".tif", ".tfw");
										downloader.download(httpBoundary, gFraUrl, tfwDownPath, tfwPath); // twf
									}
									if (!downloadFile.exists()) {
										continue;
									}

									// crop & create file
									GTiffDataCropper cropper = new GTiffDataCropper();
									String fileName = (String) file.get("vidoNm");
									String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
									String cropedPath = ortOriDir + "/" + fileName + "_" + year + "_" + dpi + "." + ext;
									File cropFile = new File(cropedPath);

									GridCoverage2D cropedGc = cropper.referencingCrop(downloadPath, orgEnv,
											envelope5179, cropedPath, targetCrs);

									// create thumbnail
									GImageProcessor gImageProcessor = new GImageProcessor();
									String thumbnailPath = ortOriDir + "/" + fileName + "_" + year + "_" + dpi + ".png";
									gImageProcessor.createThumbnailImage(cropedPath,
											GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbnailPath,
											GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
											GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

									// 좌표없는 tif 생성
									BufferedImage image = ImageIO.read(cropFile);
									File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
									ImageIO.write(image, "tif", outputfile);

									// create response json
									Envelope2D cropedEnv = cropedGc.getEnvelope2D();
									double cminx = cropedEnv.getMinX();
									double cmaxx = cropedEnv.getMaxX();
									double cminy = cropedEnv.getMinY();
									double cmaxy = cropedEnv.getMaxY();

									// db insert
									CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
									dataSetVo.setDatasetNm(datasetNm);
									dataSetVo.setDataCd("DSCD121");
									dataSetVo.setLtopCrdntX(cmaxy);
									dataSetVo.setLtopCrdntY(cminx);
									dataSetVo.setRbtmCrdntX(cminy);
									dataSetVo.setRbtmCrdntY(cmaxx);

									dataSetVo.setRtopCrdntX(cmaxy);
									dataSetVo.setRtopCrdntY(cmaxx);
									dataSetVo.setLbtmCrdntX(cminy);
									dataSetVo.setLbtmCrdntY(cminx);

									dataSetVo.setMapPrjctnCn(defaultEPSG);
									dataSetVo.setMsfrtnTyNm(disasterNm);
									dataSetVo.setDatasetCoursNm(ortOriDir.replace(outDir, datasetDbPath + disasterId));
									dataSetVo.setRoiYn("Y");
									dataSetVo.setPotogrfBeginDt(year + "-01-01");
									dataSetVo.setPotogrfEndDt(year + "-12-31");

									Date date = java.sql.Date.valueOf(dataStr);
									dataSetVo.setUploadDt(date);
									dataSetVo.setMsfrtnId(disasterId);
									dataSetVo.setAddr(addr);
									dataSetVo.setYear(year);
									dataSetVo.setDpi(dpi);
									dataSetVo.setFullFileCoursNm(cropedPath);

									String fnm = cropFile.getName().replace("." + ext, "");
									dataSetVo.setFileNm(fnm);
									dataSetVo.setMsfrtnTyCd(disasterCd);
									dataSetVo.setFileTy(ext);
									dataSetVo.setFileKorNm("정사영상_" + year + "_" + dpi);
									dataSetVo.setMsfrtnId(disasterId);
									dataSetVo.setMapNm(mapNm);
									
									

								
									// 긴급공간정보 생성 여부 확인 
									EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
									
									System.out.println("정사영상3 TN_DATASET_INFO 조회");
									System.out.println("selectDataset : " + selectDataset);
									
									if (selectDataset == null) { // 신규
										cmsc003Mapper.insertDatasetInfo(dataSetVo);
									} else { // 갱신
										dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
										cmsc003Mapper.updateDatasetInfo(dataSetVo);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			}
		}

		// 수치지도
		JSONArray digitalList = (JSONArray) existing.get("digitalMap");
		if (digitalList.size() > 0) {

			String digitalDir = outDir + "/" + "Existing" + "/" + "DigitalMap/tmp";
			String digitalResultDir = outDir + "/" + "Existing" + "/" + "DigitalMap";

			File digitalFile = new File(digitalDir);
			// create directory
			digitalFile.setExecutable(true, true);
			digitalFile.setReadable(true, true);
			digitalFile.setWritable(true, true);
			if (!digitalFile.exists()) {
				digitalFile.mkdirs();
			}
			File digitalRFile = new File(digitalResultDir);
			// create directory
			digitalRFile.setExecutable(true, true);
			digitalRFile.setReadable(true, true);
			digitalRFile.setWritable(true, true);
			if (!digitalRFile.exists()) {
				digitalRFile.mkdirs();
			}

			try {
				// geoserver 요청 주소
				String reqUrlStat = geoserverUrl + "vector/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=";
				String reqUrlend = "outputFormat=SHAPE-ZIP&" + bbox;
				for (int d = 0; d < digitalList.size(); d++) {
					JSONObject digital = (JSONObject) digitalList.get(d);
					String layerNm = (String) digital.get("vidoNm");
					String urlStr = reqUrlStat + layerNm + "&" + reqUrlend;
					URL url = new URL(urlStr);

					File savedFile = new File(digitalDir + "/" + layerNm + ".zip");
					HttpDownloader.downloadToFile(url, savedFile);

					// upzip file
					ZipUtil.unpack(savedFile, new File(digitalDir));
					savedFile.delete();

					String fileNm = "";
					String shpnm = "";
					String dataCd = "";
					String dataNm = "";

					File layerDir = new File(digitalDir);
					File[] files = layerDir.listFiles();
					for (int f = 0; f < files.length; f++) {
						String fileName = files[f].getName();
						if (fileName.contentEquals("vector")) {
							files[f].delete();
							continue;
						}
						if (fileName.contentEquals("README.txt")) {
							files[f].delete();
							continue;
						}
						String orgPath = files[f].getAbsolutePath();
						if (orgPath.contains("N3A_A0010000")) {
							fileNm = "N3A_A0010000_tmp";
							dataCd = "DSCD240";
							dataNm = "도로";
						} else if (orgPath.contains("N3A_A0053326")) {
							fileNm = "N3A_A0053326_tmp";
							dataCd = "DSCD201";
							dataNm = "안전지대";
						} else if (orgPath.contains("N3A_A0063321")) {
							fileNm = "N3A_A0063321_tmp";
							dataCd = "DSCD202";
							dataNm = "육교";
						} else if (orgPath.contains("N3A_A0070000")) {
							fileNm = "N3A_A0070000_tmp";
							dataCd = "DSCD203";
							dataNm = "교량";
						} else if (orgPath.contains("N3A_A0090000")) {
							fileNm = "N3A_A0090000_tmp";
							dataCd = "DSCD204";
							dataNm = "입체교차부";
						} else if (orgPath.contains("N3A_A0100000")) {
							fileNm = "N3A_A0100000_tmp";
							dataCd = "DSCD205";
							dataNm = "인터체인지";
						} else if (orgPath.contains("N3A_A0110020")) {
							fileNm = "N3A_A0110020_tmp";
							dataCd = "DSCD206";
							dataNm = "터널";
						} else if (orgPath.contains("N3A_A0160024")) {
							fileNm = "N3A_A0160024_tmp";
							dataCd = "DSCD207";
							dataNm = "철도경계";
						} else if (orgPath.contains("N3A_A0191221")) {
							fileNm = "N3A_A0191221_tmp";
							dataCd = "DSCD208";
							dataNm = "버스승강장";
						} else if (orgPath.contains("N3A_B0010000")) {
							fileNm = "N3A_B0010000_tmp";
							dataCd = "DSCD210";
							dataNm = "건물";
						} else if (orgPath.contains("N3A_C0032254")) {
							fileNm = "N3A_C0032254_tmp";
							dataCd = "DSCD209";
							dataNm = "선착장";
						} else if (orgPath.contains("N3A_C0290000")) {
							fileNm = "N3A_C0290000_tmp";
							dataCd = "DSCD211";
							dataNm = "묘지계";
						} else if (orgPath.contains("N3A_C0390000")) {
							fileNm = "N3A_C0390000_tmp";
							dataCd = "DSCD212";
							dataNm = "계단";
						} else if (orgPath.contains("N3A_C0423365")) {
							fileNm = "N3A_C0423365_tmp";
							dataCd = "DSCD213";
							dataNm = "주유소";
						} else if (orgPath.contains("N3A_C0430000")) {
							fileNm = "N3A_C0430000_tmp";
							dataCd = "DSCD214";
							dataNm = "주차장";
						} else if (orgPath.contains("N3A_C0443363")) {
							fileNm = "N3A_C0443363_tmp";
							dataCd = "DSCD215";
							dataNm = "휴게소";
						} else if (orgPath.contains("N3A_D0010000")) {
							fileNm = "N3A_D0010000_tmp";
							dataCd = "DSCD230";
							dataNm = "경지계(논, 밭, 과수원 등)";
						} else if (orgPath.contains("N3A_D0020000")) {
							fileNm = "N3A_D0020000_tmp";
							dataCd = "DSCD216";
							dataNm = "지류계";
						} else if (orgPath.contains("N3A_E0010001")) {
							fileNm = "N3A_E0010001_tmp";
							dataCd = "DSCD220";
							dataNm = "하천경계";
						} else if (orgPath.contains("N3A_E0032111")) {
							fileNm = "N3A_E0032111_tmp";
							dataCd = "DSCD217";
							dataNm = "실폭하천";
						} else if (orgPath.contains("N3A_E0052114")) {
							fileNm = "N3A_E0052114_tmp";
							dataCd = "DSCD218";
							dataNm = "호수,저수지";
						} else if (orgPath.contains("N3A_G0010000")) {
							fileNm = "N3A_G0010000_tmp";
							dataCd = "DSCD221";
							dataNm = "행정경계(시, 도)";
						} else if (orgPath.contains("N3A_G0020000")) {
							fileNm = "N3A_G0020000_tmp";
							dataCd = "DSCD219";
							dataNm = "수부지형경계";
						} else if (orgPath.contains("N3A_G0100000")) {
							fileNm = "N3A_G0100000_tmp";
							dataCd = "DSCD222";
							dataNm = "행정경계(시,군구)";
						} else if (orgPath.contains("N3A_G0110000")) {
							fileNm = "N3A_G0110000_tmp";
							dataCd = "DSCD223";
							dataNm = "행정경계(읍면동)";
						} else if (orgPath.contains("N3A_H0010000")) {
							fileNm = "N3A_H0010000_tmp";
							dataCd = "DSCD224";
							dataNm = "도곽";
						} else if (orgPath.contains("N3L_A0010000")) {
							fileNm = "N3L_A0010000_tmp";
							dataCd = "DSCD225";
							dataNm = "도로경계선";
						} else if (orgPath.contains("N3L_A0020000")) {
							fileNm = "N3L_A0020000_tmp";
							dataCd = "DSCD240";
							dataNm = "도로중심선";
						} else if (orgPath.contains("N3L_A0033320")) {
							fileNm = "N3L_A0033320_tmp";
							dataCd = "DSCD226";
							dataNm = "인도경계";
						} else if (orgPath.contains("N3L_B0020000")) {
							fileNm = "N3L_B0020000_tmp";
							dataCd = "DSCD227";
							dataNm = "담장";
						} else if (orgPath.contains("N3L_C0050000")) {
							fileNm = "N3L_C0050000_tmp";
							dataCd = "DSCD228";
							dataNm = "제방";
						} else if (orgPath.contains("N3L_C0060000")) {
							fileNm = "N3L_C0060000_tmp";
							dataCd = "DSCD229";
							dataNm = "수문";
						} else if (orgPath.contains("N3L_C0070000")) {
							fileNm = "N3L_C0070000_tmp";
							dataCd = "DSCD241";
							dataNm = "암거";
						} else if (orgPath.contains("N3L_C0080000")) {
							fileNm = "N3L_C0080000_tmp";
							dataCd = "DSCD231";
							dataNm = "잔교";
						} else if (orgPath.contains("N3L_C0325315")) {
							fileNm = "N3L_C0325315_tmp";
							dataCd = "DSCD232";
							dataNm = "성";
						} else if (orgPath.contains("N3L_C0520000")) {
							fileNm = "N3L_C0520000_tmp";
							dataCd = "DSCD233";
							dataNm = "도로분리대";
						} else if (orgPath.contains("N3L_E0020000")) {
							fileNm = "N3L_E0020000_tmp";
							dataCd = "DSCD234";
							dataNm = "하천중심선";
						} else if (orgPath.contains("N3L_E0060000")) {
							fileNm = "N3L_E0060000_tmp";
							dataCd = "DSCD235";
							dataNm = "용수로";
						} else if (orgPath.contains("N3L_E0080000")) {
							fileNm = "N3L_E0080000_tmp";
							dataCd = "DSCD236";
							dataNm = "해안선";
						} else if (orgPath.contains("N3L_F0010000_G")) {
							fileNm = "N3L_F0010000_tmp";
							dataCd = "DSCD237";
							dataNm = "등고선";
						} else if (orgPath.contains("N3L_F0030000")) {
							fileNm = "N3L_F0030000_tmp";
							dataCd = "DSCD238";
							dataNm = "성/절토";
						} else if (orgPath.contains("N3L_F0040000")) {
							fileNm = "N3L_F0040000_tmp";
							dataCd = "DSCD239";
							dataNm = "옹벽";
						} else if (orgPath.contains("N3L_G0030000")) {
							fileNm = "N3L_G0030000_tmp";
							dataCd = "DSCD299";
							dataNm = "기타경계";
						} else {
							continue;
						}

						String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
						String newPath = digitalResultDir + "/" + fileNm + "." + ext;
						Files.move(Paths.get(orgPath), Paths.get(newPath)); // 파일명 변경
						if (ext.contentEquals("shp")) {
							shpnm = newPath;
						}
					}

					File fullFile = new File(shpnm);
					if (!fullFile.exists()) {
						continue;
					}

					// crop shp
					ShpDataReader shpReader = new ShpDataReader(new File(shpnm));
					shpReader.setDigital(true);
					String cropedPath = shpReader.cropAndWrite2(ulx5179, uly5179, lrx5179, lry5179, defaultEPSG,
							shpnm.replace("_tmp", ""));

					if (cropedPath == null) {
						continue;
					}
					// create response json
					Date date = java.sql.Date.valueOf(dataStr);

					// db insert
					CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
					dataSetVo.setMsfrtnId(disasterId);

					String resultDir = new File(cropedPath).getParent();
					dataSetVo.setDatasetCoursNm(resultDir.replace(outDir, datasetDbPath + disasterId));
					dataSetVo.setFullFileCoursNm(cropedPath);

					dataSetVo.setDatasetNm(datasetNm);
					dataSetVo.setDataCd(dataCd);
					dataSetVo.setLtopCrdntX(Double.valueOf(uly5179));
					dataSetVo.setLtopCrdntY(Double.valueOf(ulx5179));
					dataSetVo.setRbtmCrdntX(Double.valueOf(lry5179));
					dataSetVo.setRbtmCrdntY(Double.valueOf(lrx5179));
					dataSetVo.setRtopCrdntX(Double.valueOf(uly5179));
					dataSetVo.setRtopCrdntY(Double.valueOf(lrx5179));
					dataSetVo.setLbtmCrdntX(Double.valueOf(lry5179));
					dataSetVo.setLbtmCrdntY(Double.valueOf(ulx5179));

					dataSetVo.setMapPrjctnCn(defaultEPSG);
					dataSetVo.setMsfrtnTyNm(disasterNm);
					dataSetVo.setRoiYn("Y");
					dataSetVo.setUploadDt(date);
					dataSetVo.setAddr(addr);
					dataSetVo.setYear("2021");

					String fnm = new File(cropedPath).getName();
					String ext = fnm.substring(fnm.lastIndexOf(".") + 1);
					String nm = fnm.replace("." + ext, "");

					dataSetVo.setFileNm(nm);
					dataSetVo.setMsfrtnTyCd(disasterCd);
					dataSetVo.setFileTy(ext);
					dataSetVo.setFileKorNm("수치지도(" + dataNm + ")");
					dataSetVo.setMapNm(mapNm);
					


					
					// 긴급공간정보 생성 여부 확인
					EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
					
					System.out.println("수치지도 TN_DATASET_INFO 조회");
					System.out.println("selectDataset : " + selectDataset);
					
					if (selectDataset == null) { // 신규
						cmsc003Mapper.insertDatasetInfo(dataSetVo);
					} else { // 갱신
						dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
						cmsc003Mapper.updateDatasetInfo(dataSetVo);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 격자통계
		JSONArray graphicsList = (JSONArray) existing.get("graphicsMap");
		if (graphicsList.size() > 0) {
			String graphicDir = outDir + "/" + "Existing" + "/" + "GraphicsMap";
			File graphicFile = new File(graphicDir);
			// create directory
			graphicFile.setExecutable(true, true);
			graphicFile.setReadable(true, true);
			graphicFile.setWritable(true, true);
			if (!graphicFile.exists()) {
				graphicFile.mkdirs();
			}

			for (int s = 0; s < graphicsList.size(); s++) {
				JSONObject graphicsObj = (JSONObject) graphicsList.get(s);
				String layerNm = (String) graphicsObj.get("vidoNm");

				String nameSpace = layerNm.split(":")[0];

				// geoserver 요청 주소
				String reqUrlStat = geoserverUrl + nameSpace
						+ "/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=";
				String reqUrlend = "outputFormat=SHAPE-ZIP&" + bbox;

				String urlStr = reqUrlStat + layerNm + "&" + reqUrlend;
				URL url = new URL(urlStr);

				File savedFile = new File(graphicDir + "/" + layerNm + ".zip");
				HttpDownloader.downloadToFile(url, savedFile);

				// upzip file
				ZipUtil.unpack(savedFile, new File(graphicDir));
				savedFile.delete();

				String fileNm = "";
				String shpnm = "";
				String dataCd = "";
				String dataNm = "";
				String folderDir = "";

				File layerDir = new File(graphicDir);
				File[] files = layerDir.listFiles();
				for (int f = 0; f < files.length; f++) {
					String fileName = files[f].getName();
					if (fileName.contentEquals("vector")) {
						files[f].delete();
						continue;
					}
					if (fileName.contentEquals("README.txt")) {
						files[f].delete();
						continue;
					}
					String orgPath = files[f].getAbsolutePath();
					String folderNm = null;
					if (orgPath.contains("nlsp_030001001")) {
						fileNm = "Demographics_as_tmp";
						folderNm = "Demographics";
						dataCd = "DSCD310";
						dataNm = "인구격자통계";
					} else if (orgPath.contains("buld_021002021")) {
						fileNm = "Buldgraphics_as_tmp";
						folderNm = "Buldgraphics";
						dataCd = "DSCD320";
						dataNm = "건물격자통계";
					} else if (orgPath.contains("land_021004001")) {
						fileNm = "Landgraphics_as_tmp";
						folderNm = "Landgraphics";
						dataCd = "DSCD330";
						dataNm = "지가격자통계";
					} else {
						continue;
					}
					if (folderNm != null) {
						folderDir = graphicDir + "/" + folderNm;
						File folderFile = new File(folderDir);
						// create directory
						folderFile.setExecutable(true, true);
						folderFile.setReadable(true, true);
						folderFile.setWritable(true, true);
						if (!folderFile.exists()) {
							folderFile.mkdirs();
						}

						String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
						String newPath = folderDir + "/" + fileNm + "." + ext;
						Files.move(Paths.get(orgPath), Paths.get(newPath)); // 파일명 변경
						if (ext.contentEquals("shp")) {
							shpnm = newPath;
						}
					}
				}

				File fullFile = new File(shpnm);
				if (!fullFile.exists()) {
					continue;
				}

				// crop shp
				ShpDataReader shpReader = new ShpDataReader(new File(shpnm));
				shpReader.setDigital(true);
				String cropedPath = shpReader.cropAndWrite2(ulx5179, uly5179, lrx5179, lry5179, defaultEPSG,
						shpnm.replace("_tmp", ""));

				if (cropedPath == null) {
					continue;
				}
				// create response json
				Date date = java.sql.Date.valueOf(dataStr);

				// db insert
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm(datasetNm);
				dataSetVo.setDataCd(dataCd);
				dataSetVo.setLtopCrdntX(Double.valueOf(uly5179));
				dataSetVo.setLtopCrdntY(Double.valueOf(ulx5179));
				dataSetVo.setRbtmCrdntX(Double.valueOf(lry5179));
				dataSetVo.setRbtmCrdntY(Double.valueOf(lrx5179));

				dataSetVo.setRtopCrdntX(Double.valueOf(uly5179));
				dataSetVo.setRtopCrdntY(Double.valueOf(lrx5179));
				dataSetVo.setLbtmCrdntX(Double.valueOf(lry5179));
				dataSetVo.setLbtmCrdntY(Double.valueOf(ulx5179));

				dataSetVo.setMapPrjctnCn(defaultEPSG);
				dataSetVo.setMsfrtnTyNm(disasterNm);
				dataSetVo.setDatasetCoursNm(folderDir.replace(outDir, datasetDbPath + disasterId));
				dataSetVo.setRoiYn("Y");
				dataSetVo.setUploadDt(date);
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr(addr);
				dataSetVo.setYear("2021");
				dataSetVo.setFullFileCoursNm(cropedPath);

				String fnm = new File(cropedPath).getName();
				String ext = fnm.substring(fnm.lastIndexOf(".") + 1);
				String nm = fnm.replace("." + ext, "");

				dataSetVo.setFileNm(nm);
				dataSetVo.setMsfrtnTyCd(disasterCd);
				dataSetVo.setFileTy(ext);
				dataSetVo.setFileKorNm(dataNm);
				dataSetVo.setMapNm(mapNm);
				

				

				// 긴급공간정보 생성 여부 확인
				EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
				
				System.out.println("뭔지 모르겠음 TN_DATASET_INFO 조회");
				System.out.println("selectDataset : " + selectDataset);
				if (selectDataset == null) { // 신규
					cmsc003Mapper.insertDatasetInfo(dataSetVo);
				} else { // 갱신
					dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
					cmsc003Mapper.updateDatasetInfo(dataSetVo);
				}
			}
		}

		// 위성영상
		JSONArray satellite = (JSONArray) existing.get("satellite");
		if (satellite.size() > 0) {
			for (int s = 0; s < satellite.size(); s++) {
				JSONObject satObj = (JSONObject) satellite.get(s);
				String satNm = (String) satObj.get("satNm");

				String satKrNm = "";
				String dataCd = "";
				if (satNm.contentEquals("Kompsat")) {
					satKrNm = "아리랑위성";
					dataCd = "DSCD114";
				}
				if (satNm.contentEquals("Landsat")) {
					satKrNm = "Landsat";
					dataCd = "DSCD111";
				}
				if (satNm.contentEquals("Sentinel")) {
					satKrNm = "Sentinel";
					dataCd = "DSCD112";
				}
				if (satNm.contentEquals("CAS")) {
					satKrNm = "국토위성";
					dataCd = "DSCD113";
				}
				if (satNm.contentEquals("SAR")) {
					satKrNm = "Sar";
					dataCd = "DSCD119";  //2024.08.05 sar:DSCD119로 임시
				}

				JSONArray mapArry = (JSONArray) satObj.get("map");
				for (int m = 0; m < mapArry.size(); m++) {
					JSONObject mapObj = (JSONObject) mapArry.get(m);
					String dateNm = (String) mapObj.get("date");
					JSONArray folderList = (JSONArray) mapObj.get("folderList");
					for (int f = 0; f < folderList.size(); f++) {
						JSONObject folder = (JSONObject) folderList.get(f);
						String folderNm = (String) folder.get("folderNm");
						JSONArray fileList = (JSONArray) folder.get("fileList");
						for (int j = 0; j < fileList.size(); j++) {
							JSONObject file = (JSONObject) fileList.get(j);
							String mapPrjctnCn = (String) file.get("mapPrjctnCn"); // 원본파일좌표계
							String fullFileCoursNm = (String) file.get("fullFileCoursNm"); // 원본파일경로

							File fullFile = new File(fullFileCoursNm);
							
							if (!fullFile.exists()) {
								continue;
							}

							String potogrfBeginDt = (String) file.get("potogrfBeginDt");
							String potogrfEndDt = (String) file.get("potogrfEndDt");

							String year = potogrfBeginDt.split("-")[0];
							String satDir = outDir + "/" + "Existing" + "/" + "Image" + "/" + "Satellite" + "/" + year
									+ "/" + satNm;

							// create directory
							File dirFile = new File(satDir);
							dirFile.setExecutable(true, true);
							dirFile.setReadable(true, true);
							dirFile.setWritable(true, true);
							if (!dirFile.exists()) {
								dirFile.mkdirs();
							}
							
							//여기가 문제 같은대 
							// crop & create file
							GTiffDataCropper cropper = new GTiffDataCropper();
//							String fileName = (String) file.get("vidoNm");
							String ext = fullFileCoursNm.substring(fullFileCoursNm.lastIndexOf(".") + 1);
							String fileName = FilenameUtils.removeExtension(fullFile.getName());
							String cropedPath = satDir + "/" + fileName + "." + ext;
							
							File cropedFile = new File(cropedPath);

							double ltopCrdntX = Double.valueOf(file.get("ltopCrdntY").toString());
							double ltopCrdntY = Double.valueOf(file.get("ltopCrdntX").toString());
							double rbtmCrdntX = Double.valueOf(file.get("rbtmCrdntY").toString());
							double rbtmCrdntY = Double.valueOf(file.get("rbtmCrdntX").toString());

							CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem(mapPrjctnCn);
							ReferencedEnvelope orgEnv = new ReferencedEnvelope(ltopCrdntX, rbtmCrdntX, rbtmCrdntY,
									ltopCrdntY, sourceCrs);
							MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
							Envelope transEnv = JTS.transform(orgEnv, transform);

//							if (!transEnv.intersects(envelope5179)) {
//								continue;
//							} else 
							if (envelope5179.contains(transEnv)) {
								envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
										transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
							}

							GridCoverage2D cropedGc = cropper.referencingCrop(fullFileCoursNm, orgEnv, envelope5179,
									cropedPath, targetCrs);

							GImageProcessor gImageProcessor = new GImageProcessor();
							String thumbnailPath = satDir + "/" + fileName + ".png";
							gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
									thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
									GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

							// 좌표없는 tif 생성
							BufferedImage image = ImageIO.read(cropedFile);
							File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
							ImageIO.write(image, "tif", outputfile);

							// create response json
							Envelope2D cropedEnv = cropedGc.getEnvelope2D();
							double cminx = cropedEnv.getMinX();
							double cmaxx = cropedEnv.getMaxX();
							double cminy = cropedEnv.getMinY();
							double cmaxy = cropedEnv.getMaxY();

							// db insert
							CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
							dataSetVo.setDatasetNm(datasetNm);
							dataSetVo.setDataCd(dataCd);
							dataSetVo.setLtopCrdntX(cmaxy);
							dataSetVo.setLtopCrdntY(cminx);
							dataSetVo.setRbtmCrdntX(cminy);
							dataSetVo.setRbtmCrdntY(cmaxx);

							dataSetVo.setRtopCrdntX(cmaxy);
							dataSetVo.setRtopCrdntY(cmaxx);
							dataSetVo.setLbtmCrdntX(cminy);
							dataSetVo.setLbtmCrdntY(cminx);

							dataSetVo.setMapPrjctnCn(defaultEPSG);
							dataSetVo.setMsfrtnTyNm(disasterNm);
							dataSetVo.setDatasetCoursNm(satDir.replace(outDir, datasetDbPath + disasterId));
							dataSetVo.setRoiYn("Y");
							dataSetVo.setYear(year);
							dataSetVo.setFullFileCoursNm(cropedPath);
							dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
							dataSetVo.setPotogrfEndDt(potogrfEndDt);

							Date date = java.sql.Date.valueOf(dataStr);
							dataSetVo.setUploadDt(date);
							dataSetVo.setMsfrtnId(disasterId);
							dataSetVo.setAddr(addr);

							String fnm = cropedFile.getName().replace("." + ext, "");
							dataSetVo.setFileNm(fnm);
							dataSetVo.setMsfrtnTyCd(disasterCd);
							dataSetVo.setFileTy(ext);

							String beginDate = potogrfBeginDt.replace("-", "");
							String korNm = satKrNm + "_" + beginDate;
							//dataSetVo.setFileKorNm(korNm);
							dataSetVo.setMsfrtnId(disasterId);
							dataSetVo.setSatDir(folderNm);
							dataSetVo.setMapNm(mapNm);

							//한글 파일명에 밴드 문자 추가  - 새롬
							//CAS영상(국토위성)에서 _B, _G, _R, _N, _B_PS, _G_PS, _R_PS, _N_PS
							//Sentinel 영상(국토위성)에서  _B02, _B03, _B04, _B08, _B02_10m, _B03_10m, _B04_10m, _B08_10m
							//밴드 타입 디비에서 가져오기
							List<DataCodeVO> dataCodoVO = new ArrayList<DataCodeVO>();
							dataCodoVO = cmsc003Mapper.selectDataCode();
					
							List<String> bandCasType = new ArrayList<String>();
							List<String> bandSentinelType = new ArrayList<String>();
									
							for (DataCodeVO dataCodo : dataCodoVO) {
								if(dataCodo.getdepthOne().contentEquals("CAS")) {
									bandCasType.add(dataCodo.getdepthTwo());
									
									System.out.println(dataCodo.getdepthTwo());
								}else {
									bandSentinelType.add(dataCodo.getdepthTwo());
								}
							}
							
							//밴드 붙이기
							if (satNm.contentEquals("CAS")) {
								
						        for (int bandTypeCnt = 0; bandTypeCnt< bandCasType.size(); bandTypeCnt++) {
						        	
						        	if (fnm.toUpperCase().endsWith(bandCasType.get(bandTypeCnt))) {
						        		//update일수도 있음으로 조건문 필요
						        		if(!korNm.toUpperCase().endsWith(bandCasType.get(bandTypeCnt))) {
						        			korNm = korNm + bandCasType.get(bandTypeCnt);						        			
										}
									}
						        }
							}
							
							
							if (satNm.contentEquals("Sentinel")) {
						        for (int bandTypeCnt = 0; bandTypeCnt< bandSentinelType.size(); bandTypeCnt++) {
						        	
						        	if (fnm.toUpperCase().endsWith(bandSentinelType.get(bandTypeCnt))) {
						        		
						        		//update일수도 있음으로 조건문 필요
						        		if(!korNm.toUpperCase().endsWith(bandSentinelType.get(bandTypeCnt))) {
						        			korNm = korNm + bandSentinelType.get(bandTypeCnt);
										}
									}
						        }
							}
							
							
							dataSetVo.setFileKorNm(korNm);
	
							// 긴급공간정보 생성 여부 확인
							EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
							
							System.out.println("국토위성 TN_DATASET_INFO 조회");
							System.out.println("selectDataset : " + selectDataset);
							
							if (selectDataset == null) { // 신규
								cmsc003Mapper.insertDatasetInfo(dataSetVo);
							} else { // 갱신
								dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
								cmsc003Mapper.updateDatasetInfo(dataSetVo);
							}
						}
					}
				}
			}
		}

		// 항공영상
		JSONArray airOrientalList = (JSONArray) existing.get("airOrientalMap");
		if (airOrientalList.size() > 0) {
			for (int a = 0; a < airOrientalList.size(); a++) {
				JSONObject airObj = (JSONObject) airOrientalList.get(a);
				String year = (String) airObj.get("year");
				JSONArray maps = (JSONArray) airObj.get("maps");
				for (int m = 0; m < maps.size(); m++) {
					JSONObject map = (JSONObject) maps.get(m);
					String dpi = (String) map.get("dpi");
					String airOriDir = outDir + "/Existing/Image/Aerial/" + year + "/" + dpi;

					// create directory
					File dirFile = new File(airOriDir);
					dirFile.setExecutable(true, true);
					dirFile.setReadable(true, true);
					dirFile.setWritable(true, true);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}

					JSONArray files = (JSONArray) map.get("map");
					for (int f = 0; f < files.size(); f++) {
						JSONObject file = (JSONObject) files.get(f);
						try {
							String mapPrjctnCn = (String) file.get("mapPrjctnCn"); // 원본파일좌표계
							String fullFileCoursNm = (String) file.get("fullFileCoursNm"); // 원본파일경로

							ReferencedEnvelope orgEnv = new ReferencedEnvelope(
									Double.valueOf(file.get("ltopCrdntY").toString()),
									Double.valueOf(file.get("rbtmCrdntY").toString()),
									Double.valueOf(file.get("rbtmCrdntX").toString()),
									Double.valueOf(file.get("ltopCrdntX").toString()),
									factory.createCoordinateReferenceSystem(mapPrjctnCn));

							CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem(mapPrjctnCn);
							MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
							Envelope transEnv = JTS.transform(orgEnv, transform);

//							if (!transEnv.intersects(envelope5179)) {
//								continue;
//							} else 
							if (envelope5179.contains(transEnv)) {
								envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
										transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
							}

							if (file.get("usgsWorkId") != null) { // usgs_work
								String downloadPath = fullFileCoursNm;

								File fullFile = new File(fullFileCoursNm);
								if (!fullFile.exists()) {
									continue;
								}

								String potogrfBeginDt = (String) file.get("potogrfBeginDt"); // 촬영시작
								String potogrfEndDt = (String) file.get("potogrfEndDt"); // 촬영종료

								// crop & create file
								GTiffDataCropper cropper = new GTiffDataCropper();
								String fileName = (String) file.get("vidoNm");
								String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
								String cropedPath = airOriDir + "/" + fileName + "." + ext;
								File cropFile = new File(cropedPath);

								GridCoverage2D cropedGc = cropper.referencingCrop(downloadPath, orgEnv, envelope5179,
										cropedPath, targetCrs);

								// create thumbnail
								GImageProcessor gImageProcessor = new GImageProcessor();
								String thumbnailPath = airOriDir + "/" + fileName + ".png";
								gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

								// 좌표없는 tif 생성
								BufferedImage image = ImageIO.read(cropFile);
								File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
								ImageIO.write(image, "tif", outputfile);

								// create response json
								Envelope2D cropedEnv = cropedGc.getEnvelope2D();
								double cminx = cropedEnv.getMinX();
								double cmaxx = cropedEnv.getMaxX();
								double cminy = cropedEnv.getMinY();
								double cmaxy = cropedEnv.getMaxY();

								// db insert
								CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
								dataSetVo.setDatasetNm(datasetNm);
								dataSetVo.setDataCd("DSCD120");
								dataSetVo.setLtopCrdntX(cmaxy);
								dataSetVo.setLtopCrdntY(cminx);
								dataSetVo.setRbtmCrdntX(cminy);
								dataSetVo.setRbtmCrdntY(cmaxx);

								dataSetVo.setRtopCrdntX(cmaxy);
								dataSetVo.setRtopCrdntY(cmaxx);
								dataSetVo.setLbtmCrdntX(cminy);
								dataSetVo.setLbtmCrdntY(cminx);

								dataSetVo.setMapPrjctnCn(defaultEPSG);
								dataSetVo.setMsfrtnTyNm(disasterNm);
								dataSetVo.setDatasetCoursNm(airOriDir.replace(outDir, datasetDbPath + disasterId));
								dataSetVo.setRoiYn("Y");
								dataSetVo.setFullFileCoursNm(cropedPath);

								Date date = java.sql.Date.valueOf(dataStr);
								dataSetVo.setUploadDt(date);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setAddr(addr);
								dataSetVo.setYear(year);
								dataSetVo.setDpi(dpi);
								dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
								dataSetVo.setPotogrfEndDt(potogrfEndDt);

								String fnm = cropFile.getName().replace("." + ext, "");
								dataSetVo.setFileNm(fnm);
								dataSetVo.setMsfrtnTyCd(disasterCd);
								dataSetVo.setFileTy(ext);
								dataSetVo.setFileKorNm("항공영상_" + year);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setMapNm(mapNm);
							
								cmsc003Mapper.insertDatasetInfo(dataSetVo);
							} else { // gfra
								// tif 다운로드
								fullFileCoursNm = fullFileCoursNm.replace("/air/", "air/");
								String downloadPath = gFraDownPath + fullFileCoursNm;
								File downloadFile = new File(downloadPath); // ex)
																			// /home/TempData/01_GeoFraData/air/rgb/1966/0001/C/1966000001000C3600.tif
								if (!downloadFile.exists()) {
									// download file
									HttpDownloader downloader = new HttpDownloader();
									downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);

									String tfwPath = fullFileCoursNm.replace(".tif", ".tfw");
									String tfwDownPath = downloadPath.replace(".tif", ".tfw");
									downloader.download(httpBoundary, gFraUrl, tfwDownPath, tfwPath); // twf
								}

								if (!downloadFile.exists()) {
									continue;
								}

								// crop & create file
								GTiffDataCropper cropper = new GTiffDataCropper();
								String fileName = (String) file.get("vidoNm");
								String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
								String cropedPath = airOriDir + "/" + fileName + "." + ext;
								File cropFile = new File(cropedPath);

								GridCoverage2D cropedGc = cropper.referencingCrop(downloadPath, orgEnv, envelope5179,
										cropedPath, targetCrs);

								// create thumbnail
								GImageProcessor gImageProcessor = new GImageProcessor();
								String thumbnailPath = airOriDir + "/" + fileName + ".png";
								gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

								// 좌표없는 tif 생성
								BufferedImage image = ImageIO.read(cropFile);
								File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
								ImageIO.write(image, "tif", outputfile);

								// create response json
								Envelope2D cropedEnv = cropedGc.getEnvelope2D();
								double cminx = cropedEnv.getMinX();
								double cmaxx = cropedEnv.getMaxX();
								double cminy = cropedEnv.getMinY();
								double cmaxy = cropedEnv.getMaxY();

								// db insert
								CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
								dataSetVo.setDatasetNm(datasetNm);
								dataSetVo.setDataCd("DSCD120");
								dataSetVo.setLtopCrdntX(cmaxy);
								dataSetVo.setLtopCrdntY(cminx);
								dataSetVo.setRbtmCrdntX(cminy);
								dataSetVo.setRbtmCrdntY(cmaxx);

								dataSetVo.setRtopCrdntX(cmaxy);
								dataSetVo.setRtopCrdntY(cmaxx);
								dataSetVo.setLbtmCrdntX(cminy);
								dataSetVo.setLbtmCrdntY(cminx);

								dataSetVo.setMapPrjctnCn(defaultEPSG);
								dataSetVo.setMsfrtnTyNm(disasterNm);
								dataSetVo.setDatasetCoursNm(airOriDir.replace(outDir, datasetDbPath + disasterId));
								dataSetVo.setRoiYn("Y");
								dataSetVo.setFullFileCoursNm(cropedPath);

								Date date = java.sql.Date.valueOf(dataStr);
								dataSetVo.setUploadDt(date);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setAddr(addr);
								dataSetVo.setYear(year);
								dataSetVo.setDpi(dpi);
								dataSetVo.setPotogrfBeginDt(year + "-01-01");
								dataSetVo.setPotogrfEndDt(year + "-12-31");

								String fnm = cropFile.getName().replace("." + ext, "");
								dataSetVo.setFileNm(fnm);
								dataSetVo.setMsfrtnTyCd(disasterCd);
								dataSetVo.setFileTy(ext);
								dataSetVo.setFileKorNm("항공영상_" + year);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setMapNm(mapNm);
							

								// 긴급공간정보 생성 여부 확인
								EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
								System.out.println("항공영상 TN_DATASET_INFO 조회");
								System.out.println("selectDataset : " + selectDataset);
								
								if (selectDataset == null) { // 신규
									cmsc003Mapper.insertDatasetInfo(dataSetVo);
								} else { // 갱신
									dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
									cmsc003Mapper.updateDatasetInfo(dataSetVo);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		}

		// DEM
		JSONArray demList = (JSONArray) existing.get("demMap");
		if (demList.size() > 0) {
			for (int a = 0; a < demList.size(); a++) {
				JSONObject demObj = (JSONObject) demList.get(a);
				String year = (String) demObj.get("year");
				JSONArray maps = (JSONArray) demObj.get("maps");
				for (int i = 0; i < maps.size(); i++) {
					JSONObject map = (JSONObject) maps.get(i);
					String dpi = (String) map.get("dpi");

					String demOriDir = outDir + "/Existing/Image/DEM/" + year;

					// create directory
					File dirFile = new File(demOriDir);
					dirFile.setExecutable(true, true);
					dirFile.setReadable(true, true);
					dirFile.setWritable(true, true);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					JSONArray files = (JSONArray) map.get("map");
					for (int f = 0; f < files.size(); f++) {
						JSONObject file = (JSONObject) files.get(f);
						try {
							String mapPrjctnCn = (String) file.get("mapPrjctnCn"); // 원본파일좌표계
							String fullFileCoursNm = (String) file.get("fullFileCoursNm"); // 원본파일경로
							String fileName = (String) file.get("vidoNm");

							ReferencedEnvelope orgEnv = new ReferencedEnvelope(
									Double.valueOf(file.get("ltopCrdntY").toString()),
									Double.valueOf(file.get("rbtmCrdntY").toString()),
									Double.valueOf(file.get("rbtmCrdntX").toString()),
									Double.valueOf(file.get("ltopCrdntX").toString()),
									factory.createCoordinateReferenceSystem(mapPrjctnCn));

							CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem(mapPrjctnCn);
							MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
							Envelope transEnv = JTS.transform(orgEnv, transform);

//							if (!transEnv.intersects(envelope5179)) {
//								continue;
//							} else 
							if (envelope5179.contains(transEnv)) {
								envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
										transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
							}

							if (file.get("usgsWorkId") != null) { // usgs_work
								String downloadPath = fullFileCoursNm;

								File fullFile = new File(fullFileCoursNm);
								if (!fullFile.exists()) {
									continue;
								}

								String potogrfBeginDt = (String) file.get("potogrfBeginDt"); // 촬영시작
								String potogrfEndDt = (String) file.get("potogrfEndDt"); // 촬영종료

								// crop & create file
								GTiffDataCropper cropper = new GTiffDataCropper();
								String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
								String cropedPath = demOriDir + "/" + fileName + "." + ext;
								File cropFile = new File(cropedPath);

								GridCoverage2D cropedGc = cropper.referencingCrop(downloadPath, orgEnv, envelope5179,
										cropedPath, targetCrs);

								// create thumbnail
								GImageProcessor gImageProcessor = new GImageProcessor();
								String thumbnailPath = demOriDir + "/" + fileName + ".png";
								gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

								// 좌표없는 img 생성
								BufferedImage image = ImageIO.read(new File(cropedPath));
								File outputfile = new File(cropedPath.replace(".img", "_s.img"));
								ImageIO.write(image, "img", outputfile);

								// create response json
								Envelope2D cropedEnv = cropedGc.getEnvelope2D();
								double cminx = cropedEnv.getMinX();
								double cmaxx = cropedEnv.getMaxX();
								double cminy = cropedEnv.getMinY();
								double cmaxy = cropedEnv.getMaxY();

								// db insert
								CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
								dataSetVo.setDatasetNm(datasetNm);
								dataSetVo.setDataCd("DSCD251"); // DSCD140
								dataSetVo.setLtopCrdntX(cmaxy);
								dataSetVo.setLtopCrdntY(cminx);
								dataSetVo.setRbtmCrdntX(cminy);
								dataSetVo.setRbtmCrdntY(cmaxx);

								dataSetVo.setRtopCrdntX(cmaxy);
								dataSetVo.setRtopCrdntY(cmaxx);
								dataSetVo.setLbtmCrdntX(cminy);
								dataSetVo.setLbtmCrdntY(cminx);

								dataSetVo.setMapPrjctnCn(defaultEPSG);
								dataSetVo.setMsfrtnTyNm(disasterNm);
								dataSetVo.setDatasetCoursNm(demOriDir.replace(outDir, datasetDbPath + disasterId));
								dataSetVo.setRoiYn("Y");
								dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
								dataSetVo.setPotogrfEndDt(potogrfEndDt);

								Date date = java.sql.Date.valueOf(dataStr);
								dataSetVo.setUploadDt(date);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setAddr(addr);
								dataSetVo.setYear(year);
								dataSetVo.setDpi(dpi);
								dataSetVo.setFullFileCoursNm(cropedPath);

								String fnm = cropFile.getName().replace("." + ext, "");
								dataSetVo.setFileNm(fnm);
								dataSetVo.setMsfrtnTyCd(disasterCd);
								dataSetVo.setFileTy(ext);
								dataSetVo.setFileKorNm("DEM_" + year);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setMapNm(mapNm);
								
								
								// 긴급공간정보 생성 여부 확인
								EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
								
								System.out.println("dem TN_DATASET_INFO 조회");
								System.out.println("selectDataset : " + selectDataset);
								if (selectDataset == null) { // 신규
									cmsc003Mapper.insertDatasetInfo(dataSetVo);
								} else { // 갱신
									dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
									cmsc003Mapper.updateDatasetInfo(dataSetVo);
								}

							} else { // gfra
								// tif 다운로드
								fullFileCoursNm = fullFileCoursNm.replace("/dem/", "dem/");
								String downloadPath = gFraDownPath + fullFileCoursNm;
								String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
								downloadPath = downloadPath.replace(ext, "IMG"); // 2014년도 dem만 적용 (임시)

								File downloadFile = new File(downloadPath);
								if (!downloadFile.exists()) {
									// download file
									HttpDownloader downloader = new HttpDownloader();
									downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);
								}

								if (!downloadFile.exists()) {
									continue;
								}

//								if (!transEnv.intersects(envelope5179)) {
//									continue;
//								} else 
								if (envelope5179.contains(transEnv)) {
									envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
											transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
								}

								// crop & create file
								GTiffDataCropper cropper = new GTiffDataCropper();
								String cropedPath = demOriDir + "/" + fileName + ".img";
								GridCoverage2D cropedGc = cropper.referencingCrop(downloadPath, orgEnv, envelope5179,
										cropedPath, targetCrs);

								// create thumbnail
								GImageProcessor gImageProcessor = new GImageProcessor();
								String thumbnailPath = demOriDir + "/" + fileName + ".png";
								gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
										thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
										GTiffDataReader.ResamplingMethod.BILINEAR);

								// 좌표없는 img 생성
								String sPath = cropedPath.replace(".img", "_s.img");
								GTiffDataCropper.writeGeoTiff(cropedGc, sPath);

								// create response json
								Envelope2D cropedEnv = cropedGc.getEnvelope2D();
								double cminx = cropedEnv.getMinX();
								double cmaxx = cropedEnv.getMaxX();
								double cminy = cropedEnv.getMinY();
								double cmaxy = cropedEnv.getMaxY();

								// db insert
								CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
								dataSetVo.setDatasetNm(datasetNm);
								dataSetVo.setDataCd("DSCD251");
								dataSetVo.setLtopCrdntX(cmaxy);
								dataSetVo.setLtopCrdntY(cminx);
								dataSetVo.setRbtmCrdntX(cminy);
								dataSetVo.setRbtmCrdntY(cmaxx);

								dataSetVo.setRtopCrdntX(cmaxy);
								dataSetVo.setRtopCrdntY(cmaxx);
								dataSetVo.setLbtmCrdntX(cminy);
								dataSetVo.setLbtmCrdntY(cminx);

								dataSetVo.setMapPrjctnCn(defaultEPSG);
								dataSetVo.setMsfrtnTyNm(disasterNm);
								dataSetVo.setDatasetCoursNm(demOriDir.replace(outDir, datasetDbPath + disasterId));
								dataSetVo.setRoiYn("Y");
								dataSetVo.setPotogrfBeginDt(year + "-01-01");
								dataSetVo.setPotogrfEndDt(year + "-12-31");

								Date date = java.sql.Date.valueOf(dataStr);
								dataSetVo.setUploadDt(date);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setAddr(addr);
								dataSetVo.setYear(year);
								dataSetVo.setDpi(dpi);
								dataSetVo.setFullFileCoursNm(cropedPath);

								String fnm = downloadFile.getName().replace("." + ext, "");
								dataSetVo.setFileNm(fnm);
								dataSetVo.setMsfrtnTyCd(disasterCd);
								dataSetVo.setFileTy(ext);
								dataSetVo.setFileKorNm("DEM_" + year);
								dataSetVo.setMsfrtnId(disasterId);
								dataSetVo.setMapNm(mapNm);
								
								// 긴급공간정보 생성 여부 확인
								EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
								
								System.out.println("dem2 TN_DATASET_INFO 조회");
								System.out.println("selectDataset : " + selectDataset);
								if (selectDataset == null) { // 신규
									cmsc003Mapper.insertDatasetInfo(dataSetVo);
								} else { // 갱신
									dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
									cmsc003Mapper.updateDatasetInfo(dataSetVo);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		}

		// 긴급영상
		JSONObject disaster = (JSONObject) createObj.get("disaster");
		Iterator iter = disaster.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next(); // 재난명
			JSONArray disasterList = (JSONArray) disaster.get(key);
			if (disasterList.size() > 0) { // Flood, Landslide, ForestFire
				String parent = outDir + "/" + "Disaster";
				for (int d = 0; d < disasterList.size(); d++) {
					JSONObject disaterObj = (JSONObject) disasterList.get(d);
					String fullFileCoursNm = (String) disaterObj.get("fullFileCoursNm");
					File fullFile = new File(fullFileCoursNm);
					if (!fullFile.exists()) {
						continue;
					}

					String potogrfBeginDt = (String) disaterObj.get("potogrfBeginDt"); // 촬영시작
					String potogrfEndDt = (String) disaterObj.get("potogrfEndDt"); // 촬영종료

					int idx = fullFileCoursNm.lastIndexOf("Image");
					String fileDir = fullFileCoursNm.substring(idx, fullFileCoursNm.length());
					String disasterDir = parent + "/" + fileDir;

					// dataCd
					String dataCd = "";
					String dataNm = "";
					if (fileDir.contains("Drone")) {
						dataCd = "DSCD130";
						dataNm = "드론영상";
					} else if (fileDir.contains("Aerial")) {
						dataCd = "DSCD120";
						dataNm = "항공영상";
					} else if (fileDir.contains("Satellite")) {
						dataCd = "DSCD113";
						dataNm = "위성영상";
					}

					File disFile = new File(disasterDir).getParentFile();
					String depth2 = disFile.getName();
					String year = depth2.substring(2, 4);
					String month = depth2.substring(4, 6);
					String day = depth2.substring(6, 8);
					disasterDir = disFile.getParent() + "/" + depth2 + "/" + new File(disasterDir).getName();

					// create directory
					File dirFile = new File(disasterDir).getParentFile();
					dirFile.setExecutable(true, true);
					dirFile.setReadable(true, true);
					dirFile.setWritable(true, true);
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}

					String mapPrjctnCn = (String) disaterObj.get("mapPrjctnCn"); // 원본파일좌표계
					GTiffDataCropper cropper = new GTiffDataCropper();
					String cropedPath = disasterDir;
					File file = new File(cropedPath);
					// crop & create file
					try {
						double ltopCrdntX = Double.valueOf(disaterObj.get("ltopCrdntY").toString());
						double ltopCrdntY = Double.valueOf(disaterObj.get("ltopCrdntX").toString());
						double rbtmCrdntX = Double.valueOf(disaterObj.get("rbtmCrdntY").toString());
						double rbtmCrdntY = Double.valueOf(disaterObj.get("rbtmCrdntX").toString());

						CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem(mapPrjctnCn);
						ReferencedEnvelope orgEnv = new ReferencedEnvelope(ltopCrdntX, rbtmCrdntX, rbtmCrdntY,
								ltopCrdntY, sourceCrs);
						MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
						Envelope transEnv = JTS.transform(orgEnv, transform);

						if (envelope5179.contains(transEnv)) {
							envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
									transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
						}

						// crop
						GridCoverage2D cropedGc = cropper.referencingCrop(fullFileCoursNm, orgEnv, envelope5179,
								cropedPath, targetCrs);

						// create thumbnail
						GImageProcessor gImageProcessor = new GImageProcessor();
						String thumbnailPath = disasterDir.replace(".tif", ".png");
						gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
								thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
								GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

						// 좌표없는 tif 생성
						BufferedImage image = ImageIO.read(file);
						File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
						ImageIO.write(image, "tif", outputfile);

						// create response json
						Envelope2D cropedEnv = cropedGc.getEnvelope2D();
						double cminx = cropedEnv.getMinX();
						double cmaxx = cropedEnv.getMaxX();
						double cminy = cropedEnv.getMinY();
						double cmaxy = cropedEnv.getMaxY();

						// db insert
						CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
						dataSetVo.setDatasetNm(datasetNm);
						dataSetVo.setDataCd(dataCd);
						dataSetVo.setLtopCrdntX(cmaxy);
						dataSetVo.setLtopCrdntY(cminx);
						dataSetVo.setRbtmCrdntX(cminy);
						dataSetVo.setRbtmCrdntY(cmaxx);

						dataSetVo.setRtopCrdntX(cmaxy);
						dataSetVo.setRtopCrdntY(cmaxx);
						dataSetVo.setLbtmCrdntX(cminy);
						dataSetVo.setLbtmCrdntY(cminx);

						dataSetVo.setMapPrjctnCn(defaultEPSG);
						dataSetVo.setMsfrtnTyNm(disasterNm);
						dataSetVo.setDatasetCoursNm(disFile.getPath().replace(outDir, datasetDbPath + disasterId));
						dataSetVo.setRoiYn("Y");

						Date date = java.sql.Date.valueOf(dataStr);
						dataSetVo.setUploadDt(date); // 재난 일자로 변경
						dataSetVo.setMsfrtnId(disasterId);
						dataSetVo.setAddr(addr);
						dataSetVo.setYear(year);
						dataSetVo.setFullFileCoursNm(cropedPath);
						dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
						dataSetVo.setPotogrfEndDt(potogrfEndDt);

						String fnm = file.getName();
						String ext = fnm.substring(fnm.lastIndexOf(".") + 1);
						String nm = fnm.replace("." + ext, "");

						dataSetVo.setFileNm(nm);
						dataSetVo.setMsfrtnTyCd(disasterCd);
						dataSetVo.setFileTy(ext);
						dataSetVo.setFileKorNm(dataNm + "_" + "20" + year);
						dataSetVo.setMsfrtnId(disasterId);
						dataSetVo.setMapNm(mapNm);


						// 긴급공간정보 생성 여부 확인
						EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
						
						System.out.println("뭔지모르겠음2 TN_DATASET_INFO 조회");
						System.out.println("selectDataset : " + selectDataset);
						
						if (selectDataset == null) { // 신규
							cmsc003Mapper.insertDatasetInfo(dataSetVo);
						} else { // 갱신
							dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
							cmsc003Mapper.updateDatasetInfo(dataSetVo);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		// 분석결과
		JSONObject analysis = (JSONObject) createObj.get("analysis");
		// 객체추출
		JSONObject objExt = (JSONObject) analysis.get("objectExt");
		JSONArray objRasterAnalList = (JSONArray) objExt.get("raster");

		String objDir = outDir + "/" + "Analysis" + "/" + "ObjectExt";
		// 영상데이터
		if (objRasterAnalList.size() > 0) {
			String rasterDir = objDir + "/" + "Raster";
			// create directory
			File dirFile = new File(rasterDir);
			dirFile.setExecutable(true, true);
			dirFile.setReadable(true, true);
			dirFile.setWritable(true, true);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			for (int k = 0; k < objRasterAnalList.size(); k++) {
				JSONObject raster = (JSONObject) objRasterAnalList.get(k);
				String mapPrjctnCn = (String) raster.get("mapPrjctnCn"); // 원본파일좌표계
				String fullFileCoursNm = (String) raster.get("fullFileCoursNm"); // 원본파일경로

				File fullFile = new File(fullFileCoursNm);
				if (!fullFile.exists()) {
					continue;
				}

				String potogrfBeginDt = (String) raster.get("potogrfBeginDt"); // 촬영시작
				String potogrfEndDt = (String) raster.get("potogrfEndDt"); // 촬영종료

				// crop & create file
				GTiffDataCropper cropper = new GTiffDataCropper();
				String fileName = (String) raster.get("vidoNm");
				int yIdx = fileName.indexOf("_");
				String year = fileName.substring(yIdx + 1, yIdx + 5);

				String ext = fullFileCoursNm.substring(fullFileCoursNm.lastIndexOf(".") + 1);
				String cropedPath = rasterDir + "/" + fileName + "_obj_raster." + ext;
				File file = new File(cropedPath);
				try {
					double ltopCrdntX = Double.valueOf(raster.get("ltopCrdntY").toString());
					double ltopCrdntY = Double.valueOf(raster.get("ltopCrdntX").toString());
					double rbtmCrdntX = Double.valueOf(raster.get("rbtmCrdntY").toString());
					double rbtmCrdntY = Double.valueOf(raster.get("rbtmCrdntX").toString());

					CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem(mapPrjctnCn);
					ReferencedEnvelope orgEnv = new ReferencedEnvelope(ltopCrdntX, rbtmCrdntX, rbtmCrdntY, ltopCrdntY,
							sourceCrs);
					MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
					Envelope transEnv = JTS.transform(orgEnv, transform);

//					if (!transEnv.intersects(envelope5179)) {
//						continue;
//					} else 
					if (envelope5179.contains(transEnv)) {
						envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
								transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
					}

					GridCoverage2D cropedGc = cropper.referencingCrop(fullFileCoursNm, orgEnv, envelope5179, cropedPath,
							targetCrs);

					// create thumbnail
					GImageProcessor gImageProcessor = new GImageProcessor();
					String thumbnailPath = rasterDir + "/" + fileName + "_obj_raster.png";
					gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
							thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
							GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

					// 좌표없는 tif 생성
					BufferedImage image = ImageIO.read(file);
					File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
					ImageIO.write(image, "tif", outputfile);

					// create response json
					Envelope2D cropedEnv = cropedGc.getEnvelope2D();
					double cminx = cropedEnv.getMinX();
					double cmaxx = cropedEnv.getMaxX();
					double cminy = cropedEnv.getMinY();
					double cmaxy = cropedEnv.getMaxY();

					// db insert
					CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
					dataSetVo.setDatasetNm(datasetNm);
					dataSetVo.setDataCd("DSCD411");
					dataSetVo.setLtopCrdntX(cmaxy);
					dataSetVo.setLtopCrdntY(cminx);
					dataSetVo.setRbtmCrdntX(cminy);
					dataSetVo.setRbtmCrdntY(cmaxx);

					dataSetVo.setRtopCrdntX(cmaxy);
					dataSetVo.setRtopCrdntY(cmaxx);
					dataSetVo.setLbtmCrdntX(cminy);
					dataSetVo.setLbtmCrdntY(cminx);

					dataSetVo.setMapPrjctnCn(defaultEPSG);
					dataSetVo.setMsfrtnTyNm(disasterNm);
					dataSetVo.setDatasetCoursNm(rasterDir.replace(outDir, datasetDbPath + disasterId));
					dataSetVo.setRoiYn("Y");

					Date date = java.sql.Date.valueOf(dataStr);
					dataSetVo.setUploadDt(date);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setAddr(addr);
					dataSetVo.setYear(year);
					dataSetVo.setFullFileCoursNm(cropedPath);

					String fnm = file.getName().replace("." + ext, "");
					dataSetVo.setFileNm(fnm);
					dataSetVo.setMsfrtnTyCd(disasterCd);
					dataSetVo.setFileTy(ext);
					dataSetVo.setFileKorNm("객체추출분석결과_래스터_" + year);
					dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
					dataSetVo.setPotogrfEndDt(potogrfEndDt);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setMapNm(mapNm);


					// 긴급공간정보 생성 여부 확인
					EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
					
					System.out.println("객체추출분석결과 레스터 TN_DATASET_INFO 조회");
					System.out.println("selectDataset : " + selectDataset);
					
					if (selectDataset == null) { // 신규
						cmsc003Mapper.insertDatasetInfo(dataSetVo);
					} else { // 갱신
						dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
						cmsc003Mapper.updateDatasetInfo(dataSetVo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		// 벡터데이터
		JSONArray objVectorAnalList = (JSONArray) objExt.get("vector");
		if (objVectorAnalList.size() > 0) {
			String vectorDir = objDir + "/" + "Vector";
			// create directory
			File dirFile = new File(vectorDir);
			dirFile.setExecutable(true, true);
			dirFile.setReadable(true, true);
			dirFile.setWritable(true, true);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			for (int k = 0; k < objVectorAnalList.size(); k++) {
				JSONObject vector = (JSONObject) objVectorAnalList.get(k);
				String innerFileCoursNm = (String) vector.get("vectorFileCoursNm"); // 원본파일경로

				File fullFile = new File(innerFileCoursNm);
				if (!fullFile.exists()) {
					continue;
				}

				String potogrfBeginDt = (String) vector.get("potogrfBeginDt"); // 촬영시작
				String potogrfEndDt = (String) vector.get("potogrfEndDt"); // 촬영종료

				String fileNm = new File(innerFileCoursNm).getName();
				int yIdx = fileNm.indexOf("_");
				String year = fileNm.substring(yIdx + 1, yIdx + 5);

				String vidoNm = (String) vector.get("vidoNm");
				File savedFile = new File(vectorDir + "/" + vidoNm + "_obj_vector.shp");
				try {
					// crop shp
					ShpDataReader shpReader = new ShpDataReader(new File(innerFileCoursNm));
					shpReader.setAnals(true);
					String cropedPath = shpReader.cropAndWrite2(ulx5179, uly5179, lrx5179, lry5179, defaultEPSG,
							savedFile.getPath());
					if (cropedPath == null) {
						continue;
					}

					// db insert
					CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
					dataSetVo.setDatasetNm(datasetNm);
					dataSetVo.setDataCd("DSCD412");
					dataSetVo.setLtopCrdntX(Double.valueOf(uly5179));
					dataSetVo.setLtopCrdntY(Double.valueOf(ulx5179));
					dataSetVo.setRbtmCrdntX(Double.valueOf(lry5179));
					dataSetVo.setRbtmCrdntY(Double.valueOf(lrx5179));

					dataSetVo.setRtopCrdntX(Double.valueOf(uly5179));
					dataSetVo.setRtopCrdntY(Double.valueOf(lrx5179));
					dataSetVo.setLbtmCrdntX(Double.valueOf(lry5179));
					dataSetVo.setLbtmCrdntY(Double.valueOf(ulx5179));

					dataSetVo.setMapPrjctnCn(defaultEPSG);
					dataSetVo.setMsfrtnTyNm(disasterNm);
					dataSetVo.setDatasetCoursNm(vectorDir.replace(outDir, datasetDbPath + disasterId));
					dataSetVo.setRoiYn("Y");

					Date date = java.sql.Date.valueOf(dataStr);
					dataSetVo.setUploadDt(date);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setAddr(addr);
					dataSetVo.setYear(year);
					dataSetVo.setFullFileCoursNm(cropedPath);
					dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
					dataSetVo.setPotogrfEndDt(potogrfEndDt);

					String fnm = new File(cropedPath).getName();
					String ext = fnm.substring(fnm.lastIndexOf(".") + 1);
					String nm = fnm.replace("." + ext, "");

					dataSetVo.setFileNm(nm);
					dataSetVo.setMsfrtnTyCd(disasterCd);
					dataSetVo.setFileTy(ext);

					dataSetVo.setFileKorNm("객체추출분석결과_벡터_" + year);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setMapNm(mapNm);

					
					// 긴급공간정보 생성 여부 확인
					EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
					System.out.println("객체추출분석결과 벡터 TN_DATASET_INFO 조회");
					System.out.println("selectDataset : " + selectDataset);
					
					if (selectDataset == null) { // 신규
						cmsc003Mapper.insertDatasetInfo(dataSetVo);
					} else { // 갱신
						dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
						cmsc003Mapper.updateDatasetInfo(dataSetVo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		// 변화탐지
		JSONObject chgDet = (JSONObject) analysis.get("changeDet");
		JSONArray chgRasterAnalList = (JSONArray) chgDet.get("raster");

		String chgDir = outDir + "/" + "Analysis" + "/" + "ChangeDet";
		// 영상데이터
		if (chgRasterAnalList.size() > 0) {
			String rasterDir = chgDir + "/" + "Raster";
			// create directory
			File dirFile = new File(rasterDir);
			dirFile.setExecutable(true, true);
			dirFile.setReadable(true, true);
			dirFile.setWritable(true, true);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			for (int k = 0; k < chgRasterAnalList.size(); k++) {
				JSONObject raster = (JSONObject) chgRasterAnalList.get(k);
				String mapPrjctnCn = (String) raster.get("mapPrjctnCn"); // 원본파일좌표계
				String fullFileCoursNm = (String) raster.get("fullFileCoursNm"); // 원본파일경로

				File fullFile = new File(fullFileCoursNm);
				if (!fullFile.exists()) {
					continue;
				}

				String potogrfBeginDt = (String) raster.get("potogrfBeginDt"); // 촬영시작
				String potogrfEndDt = (String) raster.get("potogrfEndDt"); // 촬영종료

				String fileName = new File(fullFileCoursNm).getName();
				int yIdx = fileName.indexOf("_");
				String year = fileName.substring(yIdx + 1, yIdx + 5);

				String vidoNm = (String) raster.get("vidoNm");
				String ext = fullFileCoursNm.substring(fullFileCoursNm.lastIndexOf(".") + 1);
				String cropedPath = rasterDir + "/" + vidoNm + "_chg_raster." + ext;
				File file = new File(cropedPath);

				GTiffDataCropper cropper = new GTiffDataCropper();
				// crop & create file
				try {
					double ltopCrdntX = Double.valueOf(raster.get("ltopCrdntY").toString());
					double ltopCrdntY = Double.valueOf(raster.get("ltopCrdntX").toString());
					double rbtmCrdntX = Double.valueOf(raster.get("rbtmCrdntY").toString());
					double rbtmCrdntY = Double.valueOf(raster.get("rbtmCrdntX").toString());

					CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem(mapPrjctnCn);
					ReferencedEnvelope orgEnv = new ReferencedEnvelope(ltopCrdntX, rbtmCrdntX, rbtmCrdntY, ltopCrdntY,
							sourceCrs);
					MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
					Envelope transEnv = JTS.transform(orgEnv, transform);

//					if (!transEnv.intersects(envelope5179)) {
//						continue;
//					} else 
					if (envelope5179.contains(transEnv)) {
						envelope5179 = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(),
								transEnv.getMinY(), transEnv.getMaxY(), targetCrs);
					}

					GridCoverage2D cropedGc = cropper.referencingCrop(fullFileCoursNm, orgEnv, envelope5179, cropedPath,
							targetCrs);

					// create thumbnail
					GImageProcessor gImageProcessor = new GImageProcessor();
					String thumbnailPath = cropedPath.replace(".tif", ".png");
					gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16,
							thumbnailPath, GImageProcessor.ImageFormat.IMG_PNG, tifWidth,
							GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);

					// 좌표없는 tif 생성
					BufferedImage image = ImageIO.read(file);
					File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));
					ImageIO.write(image, "tif", outputfile);

					// create response json
					Envelope2D cropedEnv = cropedGc.getEnvelope2D();
					double cminx = cropedEnv.getMinX();
					double cmaxx = cropedEnv.getMaxX();
					double cminy = cropedEnv.getMinY();
					double cmaxy = cropedEnv.getMaxY();

					// db insert
					CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
					dataSetVo.setDatasetNm(datasetNm);
					dataSetVo.setDataCd("DSCD421");
					dataSetVo.setLtopCrdntX(cmaxy);
					dataSetVo.setLtopCrdntY(cminx);
					dataSetVo.setRbtmCrdntX(cminy);
					dataSetVo.setRbtmCrdntY(cmaxx);

					dataSetVo.setRtopCrdntX(cmaxy);
					dataSetVo.setRtopCrdntY(cmaxx);
					dataSetVo.setLbtmCrdntX(cminy);
					dataSetVo.setLbtmCrdntY(cminx);

					dataSetVo.setMapPrjctnCn(defaultEPSG);
					dataSetVo.setMsfrtnTyNm(disasterNm);
					dataSetVo.setDatasetCoursNm(rasterDir.replace(outDir, datasetDbPath + disasterId));
					dataSetVo.setRoiYn("Y");

					Date date = java.sql.Date.valueOf(dataStr);
					dataSetVo.setUploadDt(date);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setAddr(addr);
					dataSetVo.setYear(year);
					dataSetVo.setFullFileCoursNm(cropedPath);
					dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
					dataSetVo.setPotogrfEndDt(potogrfEndDt);

					String fnm = file.getName();
					String nm = fnm.replace("." + ext, "");
					dataSetVo.setFileNm(nm);
					dataSetVo.setMsfrtnTyCd(disasterCd);
					dataSetVo.setFileTy(ext);

					dataSetVo.setFileKorNm("변화탐지분석결과_래스터_" + year);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setMapNm(mapNm);
					
					// 긴급공간정보 생성 여부 확인
					EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
					System.out.println("변화탐지분석결과 레스터 TN_DATASET_INFO 조회");
					System.out.println("selectDataset : " + selectDataset);
					if (selectDataset == null) { // 신규
						cmsc003Mapper.insertDatasetInfo(dataSetVo);
					} else { // 갱신
						dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
						cmsc003Mapper.updateDatasetInfo(dataSetVo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		// 벡터데이터
		JSONArray chgVectorAnalList = (JSONArray) chgDet.get("vector");
		if (chgVectorAnalList.size() > 0) {
			String vectorDir = chgDir + "/" + "Vector";
			// create directory
			File dirFile = new File(vectorDir);
			dirFile.setExecutable(true, true);
			dirFile.setReadable(true, true);
			dirFile.setWritable(true, true);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			for (int k = 0; k < chgVectorAnalList.size(); k++) {
				JSONObject vector = (JSONObject) chgVectorAnalList.get(k);
				String innerFileCoursNm = (String) vector.get("vectorFileCoursNm"); // 원본파일경로

				File fullFile = new File(innerFileCoursNm);
				if (!fullFile.exists()) {
					continue;
				}

				String potogrfBeginDt = (String) vector.get("potogrfBeginDt"); // 촬영시작
				String potogrfEndDt = (String) vector.get("potogrfEndDt"); // 촬영종료

				String fileNm = new File(innerFileCoursNm).getName();
				int yIdx = fileNm.indexOf("_");
				String year = new File(innerFileCoursNm).getName().substring(yIdx + 1, yIdx + 5);
				String vidoNm = (String) vector.get("vidoNm");
				File savedFile = new File(vectorDir + "/" + vidoNm + "_chg_vector.shp");
				try {
					// crop shp
					ShpDataReader shpReader = new ShpDataReader(new File(innerFileCoursNm));
					shpReader.setAnals(true);
					String cropedPath = shpReader.cropAndWrite2(ulx5179, uly5179, lrx5179, lry5179, defaultEPSG,
							savedFile.getPath());
					if (cropedPath == null) {
						continue;
					}

					// db insert
					CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
					dataSetVo.setDatasetNm(datasetNm);
					dataSetVo.setDataCd("DSCD422");
					dataSetVo.setLtopCrdntX(Double.valueOf(uly5179));
					dataSetVo.setLtopCrdntY(Double.valueOf(ulx5179));
					dataSetVo.setRbtmCrdntX(Double.valueOf(lry5179));
					dataSetVo.setRbtmCrdntY(Double.valueOf(lrx5179));

					dataSetVo.setRtopCrdntX(Double.valueOf(uly5179));
					dataSetVo.setRtopCrdntY(Double.valueOf(lrx5179));
					dataSetVo.setLbtmCrdntX(Double.valueOf(lry5179));
					dataSetVo.setLbtmCrdntY(Double.valueOf(ulx5179));

					dataSetVo.setMapPrjctnCn(defaultEPSG);
					dataSetVo.setMsfrtnTyNm(disasterNm);
					dataSetVo.setDatasetCoursNm(vectorDir.replace(outDir, datasetDbPath + disasterId));
					dataSetVo.setRoiYn("Y");

					Date date = java.sql.Date.valueOf(dataStr);
					dataSetVo.setUploadDt(date);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setAddr(addr);
					dataSetVo.setYear(year);
					dataSetVo.setFullFileCoursNm(cropedPath);
					dataSetVo.setPotogrfBeginDt(potogrfBeginDt);
					dataSetVo.setPotogrfEndDt(potogrfEndDt);

					String fnm = new File(cropedPath).getName();
					String ext = fnm.substring(fnm.lastIndexOf(".") + 1);
					String nm = fnm.replace("." + ext, "");

					dataSetVo.setFileNm(nm);
					dataSetVo.setMsfrtnTyCd(disasterCd);
					dataSetVo.setFileTy(ext);

					dataSetVo.setFileKorNm("변화탐지분석결과_벡터_" + year);
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setMapNm(mapNm);
					

					// 긴급공간정보 생성 여부 확인
					EgovMap selectDataset = cmsc003Mapper.selectDatasetInfo(dataSetVo);
					
					System.out.println("변화 탐지분석결과 벡터 TN_DATASET_INFO 조회");
					System.out.println("selectDataset : " + selectDataset);
					
					if (selectDataset == null) { // 신규
						cmsc003Mapper.insertDatasetInfo(dataSetVo);
					} else { // 갱신
						dataSetVo.setDatasetId((BigDecimal) selectDataset.get("datasetId"));
						cmsc003Mapper.updateDatasetInfo(dataSetVo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		String finDir = outDir;
		File finDirFile = new File(finDir);

		// delete empty directory
		deleteEmptyDir(finDirFile);
		// update disaster extent
		CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
		cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
		cmsc003vo2.setLtopCrdntX(Double.valueOf(ulx5179));
		cmsc003vo2.setLtopCrdntY(Double.valueOf(uly5179));
		cmsc003vo2.setRtopCrdntX(Double.valueOf(lrx5179));
		cmsc003vo2.setRtopCrdntY(Double.valueOf(uly5179));
		cmsc003vo2.setLbtmCrdntX(Double.valueOf(ulx5179));
		cmsc003vo2.setLbtmCrdntY(Double.valueOf(lry5179));
		cmsc003vo2.setRbtmCrdntX(Double.valueOf(lrx5179));
		cmsc003vo2.setRbtmCrdntY(Double.valueOf(lry5179));
		cmsc003vo2.setMapPrjctnCn(defaultEPSG);
		cmsc003Mapper.updateMsfrtnInfoColctRequst(cmsc003vo2);

		return result;
	}

	private static int deleteEmptyDir(File file) {
		if (!file.isDirectory())
			return 0;

		int delCnt = 0;

		for (File subFile : file.listFiles()) {
			if (subFile.isDirectory()) {
				delCnt += deleteEmptyDir(subFile);
			}
		}
		if (file.listFiles().length == 0) {
			System.out.println(">>>>>>>>>>>>>>>>>>>> deleted path " + file.getAbsolutePath());
			file.delete();
			delCnt++;
		}
		return delCnt;
	}

	@Override
	public List<?> selectDisasterInfoId(CMSC003VO2 cmsc003VO2) {
		List<?> result = null;
		try {
			result = cmsc003Mapper.selectDisasterInfoId(cmsc003VO2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public EgovMap selectByRequestId(String requestId) {
		EgovMap result = null;
		try {
			result = cmsc003Mapper.selectByRequestId(requestId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<?> selectAllUsgsWorkList(ObjChangeSearch dto) throws SQLException {
		return cmsc003Mapper.selectAllUsgsWorkList(dto);
	}

	@Override
	public List<ObjChangeListRes> selectDatasetWorkList(ObjChangeSearch dto) throws SQLException {
		return cmsc003Mapper.selectDatasetWorkList(dto);
	}

	@Override
	public JSONObject cmsc003saveData(JSONObject createObj) throws Exception {

		// 긴급공간정보생성
		JSONObject info = (JSONObject) createObj.get("createInfo");
		JSONObject roi = (JSONObject) info.get("roi");

		// roi 5179 bbox
		JSONObject roi5179 = (JSONObject) roi.get("roi5179");

		double lrx5179 = Double.parseDouble(roi5179.get("lrx").toString());
		double lry5179 = Double.parseDouble(roi5179.get("lry").toString());
		double ulx5179 = Double.parseDouble(roi5179.get("ulx").toString());
		double uly5179 = Double.parseDouble(roi5179.get("uly").toString());

//		double lrx5179 = (double) roi5179.get("lrx");
//		double lry5179 = (double) roi5179.get("lry");
//		double ulx5179 = (double) roi5179.get("ulx");
//		double uly5179 = (double) roi5179.get("uly");

		// 데이터셋 중복 여부 확인
		String disasterId = (String) createObj.get("disasterId");
//		Long idInt = Long.valueOf(disasterId);
		List datasetList = cmsc003Mapper.selectDatasetById(disasterId);
		if (datasetList.size() > 0) {
			// delete dataset
			cmsc003Mapper.deleteAllDatasetById(disasterId);
			for (int d = 0; d < datasetList.size(); d++) {
				EgovMap dataset = (EgovMap) datasetList.get(d);
				int vidoId = (int) dataset.get("vidoId");
				List workList = cmsc003Mapper.selectUsgsWorByVidoId(vidoId);
				if (workList.size() == 0) {
					// delete usgs
					int dcnt = cmsc003Mapper.deleteUsgsGfraByVidoId(vidoId);
					// delete usgs meta
					if (dcnt != 0) {
						cmsc003Mapper.deleteUsgsMetaByVidoId(vidoId);
					}
				}
			}
		}

		// 재난유형명 검색
		String disasterCd = (String) createObj.get("disasterCd");
		List disasterMap = cmsc003Mapper.selectTcMsfrtnTyInfo(disasterCd);
		EgovMap disasterInfo = (EgovMap) disasterMap.get(0);
		String disasterNm = (String) disasterInfo.get("msfrtnTyNm"); // 한글 재난명
		EgovMap reqMap = selectByRequestId(disasterId);

		// Existing
		JSONObject existing = (JSONObject) createObj.get("existing");

		// 위성영상
		JSONArray satellite = (JSONArray) existing.get("satellite");
		if (satellite.size() > 0) {
			for (int s = 0; s < satellite.size(); s++) {
				JSONObject satObj = (JSONObject) satellite.get(s);
				String satNm = (String) satObj.get("satNm");
				JSONArray mapArry = (JSONArray) satObj.get("map");
				for (int m = 0; m < mapArry.size(); m++) {
					JSONObject mapObj = (JSONObject) mapArry.get(m);
					String dateNm = (String) mapObj.get("date");
					JSONArray folderArry = (JSONArray) mapObj.get("folderList");
					for (int i = 0; i < folderArry.size(); i++) {
						JSONObject folderObj = (JSONObject) folderArry.get(i);
						String folderNm = (String) folderObj.get("folderNm");
						JSONArray fileArry = (JSONArray) folderObj.get("fileList");
						for (int f = 0; f < fileArry.size(); f++) {
							try {
								JSONObject file = (JSONObject) fileArry.get(f);
								Integer vidoId;
								Integer dataKind;
								if (file.get("usgsWorkId") != null) { // usgs_work인 경우
									vidoId = ((Long) file.get("usgsWorkId")).intValue();
									dataKind = 2;
								} else {
									vidoId = ((Long) file.get("vidoId")).intValue();
									dataKind = 1;
								}
								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(dataKind);
								cmsc003Mapper.insertDataset(cmsc003vo3);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				}
			}
		}

		// 항공영상
		JSONArray airOrientalList = (JSONArray) existing.get("airOrientalMap");
		if (airOrientalList.size() > 0) {
			for (int a = 0; a < airOrientalList.size(); a++) {
				JSONObject airObj = (JSONObject) airOrientalList.get(a);
				String year = (String) airObj.get("year");
				JSONArray maps = (JSONArray) airObj.get("maps");
				for (int m = 0; m < maps.size(); m++) {
					JSONObject map = (JSONObject) maps.get(m);
					String dpi = (String) map.get("dpi");
					JSONArray files = (JSONArray) map.get("map");
					for (int f = 0; f < files.size(); f++) {
						JSONObject file = (JSONObject) files.get(f);
						try {
							if (file.get("usgsWorkId") != null) { // usgs_work인 경우
								Integer vidoId = ((Long) file.get("usgsWorkId")).intValue();
								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(2);
								cmsc003Mapper.insertDataset(cmsc003vo3);
							} else { // gfra -> usgs insert
								CMSC003InsertVO insertVO = new CMSC003InsertVO();
								insertVO.setPotogrfBeginDt(year + "-01-01");
								insertVO.setPotogrfEndDt(year + "-12-31");
								insertVO.setLtopCrdntX((double) file.get("ltopCrdntX"));
								insertVO.setLtopCrdntY((double) file.get("ltopCrdntY"));
								insertVO.setRbtmCrdntX((double) file.get("rbtmCrdntX"));
								insertVO.setRbtmCrdntY((double) file.get("rbtmCrdntY"));
								insertVO.setMapPrjctnCn((String) file.get("mapPrjctnCn"));
								insertVO.setPotogrfVidoCd("7");

								// tif 다운로드
								String fullFileCoursNm = (String) file.get("fullFileCoursNm");
								fullFileCoursNm = fullFileCoursNm.replace("/air/", "air/");
								String downloadPath = gFraDownPath + fullFileCoursNm;
								File downloadFile = new File(downloadPath); // ex)
																			// /home/TempData/01_GeoFraData/air/rgb/1966/0001/C/1966000001000C3600.tif
								if (!downloadFile.exists()) {
									// download tif file
									HttpDownloader downloader = new HttpDownloader();
									downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);

									// download tfw file
									String tfwPath = fullFileCoursNm.replace(".tif", ".tfw");
									String tfwDownPath = downloadPath.replace(".tif", ".tfw");
									downloader.download(httpBoundary, gFraUrl, tfwDownPath, tfwPath); // twf

									// download jpg file
									if (new File(downloadPath).exists()) {
										String thumbnailPath = fullFileCoursNm.replace(".tif", ".jpg");
										String thumbdownloadPath = downloadPath.replace(".tif", ".jpg");
										File thumbFile = new File(thumbdownloadPath);
										if (!thumbFile.exists()) {
											downloader.download(httpBoundary, gFraUrl, thumbdownloadPath,
													thumbnailPath);
										}
									}
								}
								insertVO.setInnerFileCoursNm(downloadPath);
								cmsc003Mapper.insertUsgs(insertVO);
								int vidoId = cmsc003Mapper.selectLastUsgsId();

								insertVO.setVidoId(vidoId);
								cmsc003Mapper.insertUsgsMeta(insertVO);

								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(1);
								cmsc003Mapper.insertDataset(cmsc003vo3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// 정사영상
		JSONArray ortOrientalList = (JSONArray) existing.get("ortOrientalMap");
		if (ortOrientalList.size() > 0) {
			for (int a = 0; a < ortOrientalList.size(); a++) {
				JSONObject ortObj = (JSONObject) ortOrientalList.get(a);
				String year = (String) ortObj.get("year");
				JSONArray maps = (JSONArray) ortObj.get("maps");
				for (int i = 0; i < maps.size(); i++) {
					JSONObject map = (JSONObject) maps.get(i);
					String dpi = (String) map.get("dpi");
					JSONArray files = (JSONArray) map.get("map");
					for (int f = 0; f < files.size(); f++) {
						JSONObject file = (JSONObject) files.get(f);
						try {
							if (file.get("usgsWorkId") != null) { // usgs_work인 경우
								Integer vidoId = ((Long) file.get("usgsWorkId")).intValue();
								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(2);
								cmsc003Mapper.insertDataset(cmsc003vo3);

							} else { // gfra -> usgs insert
								CMSC003InsertVO insertVO = new CMSC003InsertVO();
								insertVO.setPotogrfBeginDt(year + "-01-01");
								insertVO.setPotogrfEndDt(year + "-12-31");
								insertVO.setLtopCrdntX((double) file.get("ltopCrdntX"));
								insertVO.setLtopCrdntY((double) file.get("ltopCrdntY"));
								insertVO.setRbtmCrdntX((double) file.get("rbtmCrdntX"));
								insertVO.setRbtmCrdntY((double) file.get("rbtmCrdntY"));
								insertVO.setMapPrjctnCn((String) file.get("mapPrjctnCn"));
								insertVO.setPotogrfVidoCd("8");

								// tif 다운로드
								String fullFileCoursNm = (String) file.get("fullFileCoursNm");
								fullFileCoursNm = fullFileCoursNm.replace("/ort/", "ort/");
								String downloadPath = gFraDownPath + fullFileCoursNm;
								File downloadFile = new File(downloadPath); // ex)
																			// /home/TempData/01_GeoFraData/air/rgb/1966/0001/C/1966000001000C3600.tif
								if (!downloadFile.exists()) {
									// download tif file
									HttpDownloader downloader = new HttpDownloader();
									downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);

									// download tfw file
									String tfwPath = fullFileCoursNm.replace(".tif", ".tfw");
									String tfwDownPath = downloadPath.replace(".tif", ".tfw");
									downloader.download(httpBoundary, gFraUrl, tfwDownPath, tfwPath); // twf

									// download jpg file
									if (new File(downloadPath).exists()) {
										String thumbnailPath = fullFileCoursNm.replace(".tif", ".jpg");
										String thumbdownloadPath = downloadPath.replace(".tif", ".jpg");
										File thumbFile = new File(thumbdownloadPath);
										if (!thumbFile.exists()) {
											downloader.download(httpBoundary, gFraUrl, thumbdownloadPath,
													thumbnailPath);
										}
									}
								}
								insertVO.setInnerFileCoursNm(downloadPath);
								cmsc003Mapper.insertUsgs(insertVO);
								int vidoId = cmsc003Mapper.selectLastUsgsId();

								insertVO.setVidoId(vidoId);
								cmsc003Mapper.insertUsgsMeta(insertVO);

								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(1);
								cmsc003Mapper.insertDataset(cmsc003vo3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// DEM
		JSONArray demList = (JSONArray) existing.get("demMap");
		if (demList.size() > 0) {
			for (int a = 0; a < demList.size(); a++) {
				JSONObject demObj = (JSONObject) demList.get(a);
				String year = (String) demObj.get("year");
				JSONArray maps = (JSONArray) demObj.get("maps");
				for (int i = 0; i < maps.size(); i++) {
					JSONObject map = (JSONObject) maps.get(i);
					String dpi = (String) map.get("dpi");
					JSONArray files = (JSONArray) map.get("map");
					for (int f = 0; f < files.size(); f++) {
						JSONObject file = (JSONObject) files.get(f);
						try {
							String mapPrjctnCn = (String) file.get("mapPrjctnCn"); // 원본파일좌표계
							String fullFileCoursNm = (String) file.get("fullFileCoursNm"); // 원본파일경로
							String fileName = (String) file.get("vidoNm");
							if (file.get("usgsWorkId") != null) { // usgs_work
								Integer vidoId = ((Long) file.get("usgsWorkId")).intValue();
								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(2);
								cmsc003Mapper.insertDataset(cmsc003vo3);
							} else { // gfra
								CMSC003InsertVO insertVO = new CMSC003InsertVO();
								insertVO.setPotogrfBeginDt(year + "-01-01");
								insertVO.setPotogrfEndDt(year + "-12-31");
								insertVO.setLtopCrdntX((double) file.get("ltopCrdntX"));
								insertVO.setLtopCrdntY((double) file.get("ltopCrdntY"));
								insertVO.setRbtmCrdntX((double) file.get("rbtmCrdntX"));
								insertVO.setRbtmCrdntY((double) file.get("rbtmCrdntY"));
								insertVO.setMapPrjctnCn((String) file.get("mapPrjctnCn"));
								insertVO.setPotogrfVidoCd("9");

								// img 다운로드
								fullFileCoursNm = fullFileCoursNm.replace("/dem/", "dem/");
								String downloadPath = gFraDownPath + fullFileCoursNm;
								String ext = downloadPath.substring(downloadPath.lastIndexOf(".") + 1);
								downloadPath = downloadPath.replace(ext, "IMG"); // 2014년도 dem만 적용 (임시)

								File downloadFile = new File(downloadPath);
								if (!downloadFile.exists()) {
									// download file
									HttpDownloader downloader = new HttpDownloader();
									downloader.download(httpBoundary, gFraUrl, downloadPath, fullFileCoursNm);
								}

								insertVO.setInnerFileCoursNm(downloadPath);
								cmsc003Mapper.insertUsgs(insertVO);
								int vidoId = cmsc003Mapper.selectLastUsgsId();

								insertVO.setVidoId(vidoId);
								cmsc003Mapper.insertUsgsMeta(insertVO);

								CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
								cmsc003vo3.setMsfrtnInfoColctRequstId(
										Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
								cmsc003vo3.setVidoId(vidoId);
								cmsc003vo3.setDataKind(1);
								cmsc003Mapper.insertDataset(cmsc003vo3);
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		}

		// 긴급영상
		JSONObject disaster = (JSONObject) createObj.get("disaster");
		Iterator iter = disaster.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next(); // Flood, Landslide, ForestFire, RedTide
			JSONArray disasterList = (JSONArray) disaster.get(key);
			if (disasterList.size() > 0) {
				for (int d = 0; d < disasterList.size(); d++) {
					JSONObject file = (JSONObject) disasterList.get(d);
					try {
						Integer vidoId;
						Integer dataKind;
						if (file.get("usgsWorkId") != null) { // usgs_work인 경우
							vidoId = ((Long) file.get("usgsWorkId")).intValue();
							dataKind = 2;
						} else {
							vidoId = ((Long) file.get("vidoId")).intValue();
							dataKind = 1;
						}
						CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
						cmsc003vo3.setMsfrtnInfoColctRequstId(
								Long.valueOf(reqMap.get("msfrtnInfoColctRequstId").toString()));
						cmsc003vo3.setVidoId(vidoId);
						cmsc003vo3.setDataKind(dataKind);
						cmsc003Mapper.insertDataset(cmsc003vo3);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		// update disaster extent
		CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
		cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
		cmsc003vo2.setLtopCrdntX(Double.valueOf(ulx5179));
		cmsc003vo2.setLtopCrdntY(Double.valueOf(uly5179));
		cmsc003vo2.setRtopCrdntX(Double.valueOf(lrx5179));
		cmsc003vo2.setRtopCrdntY(Double.valueOf(uly5179));
		cmsc003vo2.setLbtmCrdntX(Double.valueOf(ulx5179));
		cmsc003vo2.setLbtmCrdntY(Double.valueOf(lry5179));
		cmsc003vo2.setRbtmCrdntX(Double.valueOf(lrx5179));
		cmsc003vo2.setRbtmCrdntY(Double.valueOf(lry5179));
		cmsc003vo2.setMapPrjctnCn(defaultEPSG);
		cmsc003Mapper.updateMsfrtnInfoColctRequst(cmsc003vo2);

		return null;
	}

	@Override
	public List<EgovMap> selectDatasetByDisasterId(CMSC003VO3 cmsc003vo3) throws Exception {
		return cmsc003Mapper.selectDatasetByDisasterId(cmsc003vo3);
	}

	@Override
	public EgovMap selectUsgsByInFileCours(String fileCours) throws Exception {
		return cmsc003Mapper.selectUsgsByInFileCours(fileCours);
	}

	@Override
	public EgovMap selectUsgsWorkByInFileCours(String fileCours) throws Exception {
		return cmsc003Mapper.selectUsgsWorkByInFileCours(fileCours);
	}

	@Override
	public List<EgovMap> selectDatasetAirByDisasterId(CMSC003VO3 cmsc003vo3) throws Exception {
		return cmsc003Mapper.selectDatasetAirByDisasterId(cmsc003vo3);
	}

	@Override
	public List<EgovMap> selectDatasetOrtByDisasterId(CMSC003VO3 cmsc003vo3) throws Exception {
		return cmsc003Mapper.selectDatasetOrtByDisasterId(cmsc003vo3);
	}

	
	//긴급공간정보 관리 검색 버튼 클릭시 트리 조회 컨트롤러
	@Override
	public JSONObject cmsc003searchDataset(CMSC003VO2 cmsc003vo2) throws Exception {

		// result
		JSONObject existingJSON = new JSONObject();
		JSONObject disasterJSON = new JSONObject();
		JSONObject analsJSON = new JSONObject();

		JSONArray digitalArry = new JSONArray(); // 수치지도
		JSONArray graphicArry = new JSONArray(); // 격자통계

		existingJSON.put("digitalMap", digitalArry);
		existingJSON.put("graphicsMap", graphicArry);

		JSONArray kompsatArry = new JSONArray(); // Kompsat
		JSONArray landsatArry = new JSONArray(); // Landsat
		JSONArray sentinelArry = new JSONArray(); // Sentinel
		JSONArray casArry = new JSONArray(); // CAS
		JSONArray sarArry = new JSONArray(); // SAR

		JSONObject kompsatObj = new JSONObject();
		JSONObject landsatObj = new JSONObject();
		JSONObject sentinelObj = new JSONObject();
		JSONObject casObj = new JSONObject();
		JSONObject sarObj = new JSONObject();

		kompsatObj.put("satNm", "Kompsat");
		kompsatObj.put("map", kompsatArry);

		landsatObj.put("satNm", "Landsat");
		landsatObj.put("map", landsatArry);

		sentinelObj.put("satNm", "Sentinel");
		sentinelObj.put("map", sentinelArry);

		casObj.put("satNm", "CAS");
		casObj.put("map", casArry);

		sarObj.put("satNm", "SAR");
		sarObj.put("map", sarArry);

		JSONArray sateArry = new JSONArray(); // 위성영상
		sateArry.add(kompsatObj);
		sateArry.add(landsatObj);
		sateArry.add(sentinelObj);
		sateArry.add(casObj);
		sateArry.add(sarObj);

		existingJSON.put("satellite", sateArry);

		JSONArray airMapList = new JSONArray(); // 항공영상
		JSONArray ortMapList = new JSONArray(); // 정사영상
		JSONArray demMapList = new JSONArray(); // DEM

		existingJSON.put("airOrientalMap", airMapList);
		existingJSON.put("ortOrientalMap", ortMapList);
		existingJSON.put("demMap", demMapList);

		JSONArray floodArry = new JSONArray(); // 수해
		JSONArray earthquakeArry = new JSONArray(); // 지진
		JSONArray maritimeDisasterArry = new JSONArray(); // 해양재난
		JSONArray landslideArry = new JSONArray(); // 산사태
		JSONArray forestFireArry = new JSONArray(); // 산불
		JSONArray redTideArry = new JSONArray(); // 적조

		disasterJSON.put("Flood", floodArry);
		disasterJSON.put("Earthquake", earthquakeArry);
		disasterJSON.put("MaritimeDisaster", maritimeDisasterArry);
		disasterJSON.put("Landslide", landslideArry);
		disasterJSON.put("ForestFire", forestFireArry);
		disasterJSON.put("RedTide", redTideArry);

		JSONObject objObj = new JSONObject(); // 객체추출
		JSONArray objRaster = new JSONArray();
		JSONArray objVector = new JSONArray();
		objObj.put("raster", objRaster);
		objObj.put("vector", objVector);

		JSONObject chgObj = new JSONObject(); // 변화탐지
		JSONArray chgRaster = new JSONArray();
		JSONArray chgVector = new JSONArray();
		chgObj.put("raster", chgRaster);
		chgObj.put("vector", chgVector);

		analsJSON.put("objectExt", objObj);
		analsJSON.put("changeDet", chgObj);

		JSONObject result = new JSONObject();
		result.put("existing", existingJSON); // 기구축자료
		result.put("disaster", disasterJSON); // 긴급영상
		result.put("analysis", analsJSON); // 분석결과

		CMSC003VO4 cmsc003vo4 = new CMSC003VO4();
		cmsc003vo4.setMsfrtnId(Long.toString(cmsc003vo2.getMsfrtnInfoColctRequstId()));
		EgovMap msfrtnInfo = cmsc003Mapper.selectMsfrtnDataset(cmsc003vo4);
		result.put("datasetCoursNm", msfrtnInfo.get("datasetCoursNm"));
		result.put("datasetNm", msfrtnInfo.get("datasetNm"));

		List<EgovMap> datasetList = new ArrayList<EgovMap>();
		try {
			
			//"TN_DATASET_INFO" 디비에서 MSFRTN_ID로 전체 검색 2024.02.26
			datasetList = cmsc003Mapper.selectAllDatasetInfo(cmsc003vo2);
			if (datasetList.size() > 0) {
				for (EgovMap dataset : datasetList) {
					JSONObject resObj = new JSONObject();
					resObj.put("datasetId", dataset.get("datasetId"));
					resObj.put("ltopCrdntY", dataset.get("ltopCrdntY"));
					resObj.put("ltopCrdntX", dataset.get("ltopCrdntX"));
					resObj.put("rbtmCrdntY", dataset.get("rbtmCrdntY"));
					resObj.put("rbtmCrdntX", dataset.get("rbtmCrdntX"));

					String fullFileCoursNm = (String) dataset.get("fullFileCoursNm");
					String thumbnailFileCoursNm = fullFileCoursNm.replace((String) dataset.get("fileTy"), "png");

					resObj.put("fullFileCoursNm", fullFileCoursNm);
					resObj.put("thumbnailFileCoursNm", thumbnailFileCoursNm);
					resObj.put("thumbnail", true);
					resObj.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
					resObj.put("vidoNm", dataset.get("fileNm"));

					String dataCd = (String) dataset.get("dataCd");
//					File orgFile = new File(fullFileCoursNm);
//					File dir = orgFile.getParentFile();
//					String dirNm = dir.getName();
					String year = (String) dataset.get("year");
					String dirNm = (String) dataset.get("satDir");

					// 수치지도
					if (dataCd.startsWith("DSCD2") && !dataCd.contentEquals("DSCD251")) {
						String fnm = (String) dataset.get("fileNm");
						String layerNm = "";
						String vidoNmKr = "";
						if (fnm.contentEquals("N3A_A0010000")) {
							layerNm = "vector:N3A_A0010000";
							vidoNmKr = "도로";
						} else if (fnm.contentEquals("N3A_A0053326")) {
							layerNm = "vector:N3A_A0053326";
							vidoNmKr = "안전지대";
						} else if (fnm.contentEquals("N3A_A0063321")) {
							layerNm = "vector:N3A_A0063321";
							vidoNmKr = "육교";
						} else if (fnm.contentEquals("N3A_A0070000")) {
							layerNm = "vector:N3A_A0070000";
							vidoNmKr = "교량";
						} else if (fnm.contentEquals("N3A_A0090000")) {
							layerNm = "vector:N3A_A0090000";
							vidoNmKr = "입체교차부";
						} else if (fnm.contentEquals("N3A_A0100000")) {
							layerNm = "vector:N3A_A0100000";
							vidoNmKr = "인터체인지";
						} else if (fnm.contentEquals("N3A_A0110020")) {
							layerNm = "vector:N3A_A0110020";
							vidoNmKr = "터널";
						} else if (fnm.contentEquals("N3A_A0160024")) {
							layerNm = "vector:N3A_A0160024";
							vidoNmKr = "철도경계";
						} else if (fnm.contentEquals("N3A_A0191221")) {
							layerNm = "vector:N3A_A0191221";
							vidoNmKr = "버스승강장";
						} else if (fnm.contentEquals("N3A_B0010000")) {
							layerNm = "vector:N3A_B0010000";
							vidoNmKr = "건물";
						} else if (fnm.contentEquals("N3A_C0032254")) {
							layerNm = "vector:N3A_C0032254";
							vidoNmKr = "선착장";
						} else if (fnm.contentEquals("N3A_C0290000")) {
							layerNm = "vector:N3A_C0290000";
							vidoNmKr = "묘지계";
						} else if (fnm.contentEquals("N3A_C0390000")) {
							layerNm = "vector:N3A_C0390000";
							vidoNmKr = "계단";
						} else if (fnm.contentEquals("N3A_C0423365")) {
							layerNm = "vector:N3A_C0423365";
							vidoNmKr = "주유소";
						} else if (fnm.contentEquals("N3A_C0430000")) {
							layerNm = "vector:N3A_C0430000";
							vidoNmKr = "주차장";
						} else if (fnm.contentEquals("N3A_C0443363")) {
							layerNm = "vector:N3A_C0443363";
							vidoNmKr = "휴게소";
						} else if (fnm.contentEquals("N3A_D0010000")) {
							layerNm = "vector:N3A_D0010000";
							vidoNmKr = "경지계(논, 밭, 과수원 등)";
						} else if (fnm.contentEquals("N3A_D0020000")) {
							layerNm = "vector:N3A_D0020000";
							vidoNmKr = "지류계";
						} else if (fnm.contentEquals("N3A_E0010001")) {
							layerNm = "vector:N3A_E0010001";
							vidoNmKr = "하천";
						} else if (fnm.contentEquals("N3A_E0032111")) {
							layerNm = "vector:N3A_E0032111";
							vidoNmKr = "실폭하천";
						} else if (fnm.contentEquals("N3A_E0052114")) {
							layerNm = "vector:N3A_E0052114";
							vidoNmKr = "호수,저수지";
						} else if (fnm.contentEquals("N3A_G0010000")) {
							layerNm = "vector:N3A_G0010000";
							vidoNmKr = "행정경계(시, 도)";
						} else if (fnm.contentEquals("N3A_G0020000")) {
							layerNm = "vector:N3A_G0020000";
							vidoNmKr = "수부지형경계";
						} else if (fnm.contentEquals("N3A_G0100000")) {
							layerNm = "vector:N3A_G0100000";
							vidoNmKr = "행정경계(시, 군구)";
						} else if (fnm.contentEquals("N3A_G0110000")) {
							layerNm = "vector:N3A_G0110000";
							vidoNmKr = "행정경계(읍면동)";
						} else if (fnm.contentEquals("N3A_H0010000")) {
							layerNm = "vector:N3A_H0010000";
							vidoNmKr = "도곽";
						}
						// linestring
						else if (fnm.contentEquals("N3L_A0010000")) {
							layerNm = "vector:N3L_A0010000";
							vidoNmKr = "도로경계선";
						} else if (fnm.contentEquals("N3L_A0020000")) {
							layerNm = "vector:N3L_A0020000";
							vidoNmKr = "도로중심선";
						} else if (fnm.contentEquals("N3L_A0033320")) {
							layerNm = "vector:N3L_A0033320";
							vidoNmKr = "인도경계";
						} else if (fnm.contentEquals("N3L_B0020000")) {
							layerNm = "vector:N3L_B0020000";
							vidoNmKr = "담장";
						} else if (fnm.contentEquals("N3L_C0050000")) {
							layerNm = "vector:N3L_C0050000";
							vidoNmKr = "제방";
						} else if (fnm.contentEquals("N3L_C0060000")) {
							layerNm = "vector:N3L_C0060000";
							vidoNmKr = "수문";
						} else if (fnm.contentEquals("N3L_C0070000")) {
							layerNm = "vector:N3L_C0070000";
							vidoNmKr = "암거";
						} else if (fnm.contentEquals("N3L_C0080000")) {
							layerNm = "vector:N3L_C0080000";
							vidoNmKr = "잔교";
						} else if (fnm.contentEquals("N3L_C0325315")) {
							layerNm = "vector:N3L_C0325315";
							vidoNmKr = "성";
						} else if (fnm.contentEquals("N3L_C0520000")) {
							layerNm = "vector:N3L_C0520000";
							vidoNmKr = "도로분리대";
						} else if (fnm.contentEquals("N3L_E0020000")) {
							layerNm = "vector:N3L_E0020000";
							vidoNmKr = "하천중심선";
						} else if (fnm.contentEquals("N3L_E0060000")) {
							layerNm = "vector:N3L_E0060000";
							vidoNmKr = "용수로";
						} else if (fnm.contentEquals("N3L_E0080000")) {
							layerNm = "vector:N3L_E0080000";
							vidoNmKr = "해안선";
						} else if (fnm.contentEquals("N3L_F0010000")) {
							layerNm = "vector:N3L_F0010000";
							vidoNmKr = "등고선";
						} else if (fnm.contentEquals("N3L_F0030000")) {
							layerNm = "vector:N3L_F0030000";
							vidoNmKr = "성/절토";
						} else if (fnm.contentEquals("N3L_F0040000")) {
							layerNm = "vector:N3L_F0040000";
							vidoNmKr = "옹벽";
						} else if (fnm.contentEquals("N3L_G0030000")) {
							layerNm = "vector:N3L_G0030000";
							vidoNmKr = "기타경계";
						} else {
							vidoNmKr = fnm;
						}
						resObj.put("vidoNm", layerNm);
						resObj.put("vidoNmKr", vidoNmKr);
						digitalArry.add(resObj);
					}

					// 통계
					if (dataCd.startsWith("DSCD3")) {
						String fnm = (String) dataset.get("fileNm");
						String layerNm = "";
						String vidoNmKr = "";
						if (fnm.contentEquals("Demographics_as")) {
							layerNm = "nlsp:nlsp_030001001";
							vidoNmKr = "인구통계 레이어";
						}
						if (fnm.contentEquals("Buldgraphics_as")) {
							layerNm = "buld:buld_021002021";
							vidoNmKr = "건물통계 레이어";
						}
						if (fnm.contentEquals("Landgraphics_as")) {
							layerNm = "land:land_021004001";
							vidoNmKr = "지가통계 레이어";
						}
						resObj.put("vidoNm", layerNm);
						resObj.put("vidoNmKr", vidoNmKr);
						graphicArry.add(resObj);
					}

					// 위성영상
					if (dataCd.contentEquals("DSCD114")) { // Kompsat
						String date = dataset.get("potogrfBeginDt").toString();
						String fNm = (String) dataset.get("fileNm");
						String[] fNmArry = fNm.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						String bandCnUp = bandCn.toUpperCase();
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							resObj.put("vidoNm", bandCn + ".tif");
						} else {
							resObj.put("vidoNm", fNm + ".tif");
						}

						boolean exist = false;
						for (int j = 0; j < kompsatArry.size(); j++) {
							JSONObject kompMap = (JSONObject) kompsatArry.get(j);
							String mapFolder = (String) kompMap.get("date");
							if (mapFolder.contentEquals(date)) {
								JSONArray folderList = (JSONArray) kompMap.get("folderList");
								boolean existin = false;
								for (int f = 0; f < folderList.size(); f++) {
									JSONObject folderObj = (JSONObject) folderList.get(f);
									String folderNm = (String) folderObj.get("folderNm");
									if (folderNm.contentEquals(dirNm)) {
										JSONArray fileList = (JSONArray) folderObj.get("fileList");
										fileList.add(fileList.size(), resObj);
										existin = true;
									}
								}
								if (!existin) {
									JSONObject newFolderObj = new JSONObject();
									JSONArray newFileList = new JSONArray();
									newFileList.add(resObj);
									newFolderObj.put("folderNm", dirNm);
									newFolderObj.put("fileList", newFileList);
									folderList.add(newFolderObj);
								}
								exist = true;
							}
						}
						if (!exist) {
							JSONObject kompMap = new JSONObject();
							kompMap.put("date", date);
							JSONArray folderList = new JSONArray();
							JSONObject folderObj = new JSONObject();
							folderObj.put("folderNm", dirNm);
							JSONArray fileList = new JSONArray();
							fileList.add(resObj);
							folderObj.put("fileList", fileList);
							folderList.add(folderObj);
							kompMap.put("folderList", folderList);
							kompsatArry.add(kompMap);
						}
					}
					if (dataCd.contentEquals("DSCD111")) { // Landsat
						String date = dataset.get("potogrfBeginDt").toString();
						String fNm = (String) dataset.get("fileNm");
						String[] fNmArry = fNm.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						String bandCnUp = bandCn.toUpperCase();
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							resObj.put("vidoNm", bandCn + ".tif");
						} else {
							resObj.put("vidoNm", fNm + ".tif");
						}

						boolean exist = false;
						for (int j = 0; j < landsatArry.size(); j++) {
							JSONObject landsatMap = (JSONObject) landsatArry.get(j);
							String mapFolder = (String) landsatMap.get("date");
							if (mapFolder.contentEquals(date)) {
								JSONArray folderList = (JSONArray) landsatMap.get("folderList");
								boolean existin = false;
								for (int f = 0; f < folderList.size(); f++) {
									JSONObject folderObj = (JSONObject) folderList.get(f);
									String folderNm = (String) folderObj.get("folderNm");
									if (folderNm.contentEquals(dirNm)) {
										JSONArray fileList = (JSONArray) folderObj.get("fileList");
										fileList.add(fileList.size(), resObj);
										existin = true;
									}
								}
								if (!existin) {
									JSONObject newFolderObj = new JSONObject();
									JSONArray newFileList = new JSONArray();
									newFileList.add(resObj);
									newFolderObj.put("folderNm", dirNm);
									newFolderObj.put("fileList", newFileList);
									folderList.add(newFolderObj);
								}
								exist = true;
							}
						}
						if (!exist) {
							JSONObject landsatMap = new JSONObject();
							landsatMap.put("date", date);
							JSONArray folderList = new JSONArray();
							JSONObject folderObj = new JSONObject();
							folderObj.put("folderNm", dirNm);
							JSONArray fileList = new JSONArray();
							fileList.add(resObj);
							folderObj.put("fileList", fileList);
							folderList.add(folderObj);
							landsatMap.put("folderList", folderList);
							landsatArry.add(landsatMap);
						}
					}
					if (dataCd.contentEquals("DSCD112")) { // Sentinel
						
						
						//SENTINEL 영상만
						//path -> 경로에 . replace 새롬
						//dirNm = dirNm.replace(".", "period");
					
						String date = dataset.get("potogrfBeginDt").toString();
						String fNm = (String) dataset.get("fileNm");
						String[] fNmArry = fNm.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						String bandCnUp = bandCn.toUpperCase();
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							resObj.put("vidoNm", bandCn + ".tif");
						} else {
							resObj.put("vidoNm", fNm + ".tif");
						}

						boolean exist = false;
						for (int j = 0; j < sentinelArry.size(); j++) {
							JSONObject sentinelMap = (JSONObject) sentinelArry.get(j);
							String mapFolder = (String) sentinelMap.get("date");
							if (mapFolder.contentEquals(date)) {
								JSONArray folderList = (JSONArray) sentinelMap.get("folderList");
								boolean existin = false;
								for (int f = 0; f < folderList.size(); f++) {
									JSONObject folderObj = (JSONObject) folderList.get(f);
									String folderNm = (String) folderObj.get("folderNm");
									if (folderNm.contentEquals(dirNm)) {
										JSONArray fileList = (JSONArray) folderObj.get("fileList");
										fileList.add(fileList.size(), resObj);
										existin = true;
									}
								}
								if (!existin) {
									JSONObject newFolderObj = new JSONObject();
									JSONArray newFileList = new JSONArray();
									newFileList.add(resObj);
									newFolderObj.put("folderNm", dirNm);
									newFolderObj.put("fileList", newFileList);
									folderList.add(newFolderObj);
								}
								exist = true;
							}
						}
						if (!exist) {
							JSONObject sentinelMap = new JSONObject();
							sentinelMap.put("date", date);
							JSONArray folderList = new JSONArray();
							JSONObject folderObj = new JSONObject();
							folderObj.put("folderNm", dirNm);
							JSONArray fileList = new JSONArray();
							fileList.add(resObj);
							folderObj.put("fileList", fileList);
							folderList.add(folderObj);
							sentinelMap.put("folderList", folderList);
							sentinelArry.add(sentinelMap);
						}
					}
					if (dataCd.contentEquals("DSCD113")) { // CAS
						String date = dataset.get("potogrfBeginDt").toString();
						String fNm = (String) dataset.get("fileNm");
						String[] fNmArry = fNm.split("_");
						
						//String bandCn = fNmArry[fNmArry.length - 1];
						

						System.out.println("----------긴급공간정보 관리 시작-------------------");
						System.out.println(fNm);

						//CAS영상만 "_"다섯번째부터 출력  : 새롬 
						String bandCn = "";
						for ( int aCnt = 5; aCnt< fNmArry.length; aCnt++) {
								
							bandCn += fNmArry[aCnt];
																						
							if(!fNmArry[fNmArry.length - 1].equals(fNmArry[aCnt])) {
								bandCn += "_";
							}
						}	
						System.out.println(bandCn);
						System.out.println("----------긴급공간정보 관리 -------------------");
						
						String bandCnUp = bandCn.toUpperCase();
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							resObj.put("vidoNm", bandCn + ".tif");
						} else {
							resObj.put("vidoNm", fNm + ".tif");
						}

						boolean exist = false;
						for (int j = 0; j < casArry.size(); j++) {
							JSONObject casMap = (JSONObject) casArry.get(j);
							String mapFolder = (String) casMap.get("date");
							if (mapFolder.contentEquals(date)) {
								JSONArray folderList = (JSONArray) casMap.get("folderList");
								boolean existin = false;
								for (int f = 0; f < folderList.size(); f++) {
									JSONObject folderObj = (JSONObject) folderList.get(f);
									String folderNm = (String) folderObj.get("folderNm");
									if (folderNm.contentEquals(dirNm)) {
										JSONArray fileList = (JSONArray) folderObj.get("fileList");
										fileList.add(fileList.size(), resObj);
										existin = true;
									}
								}
								if (!existin) {
									JSONObject newFolderObj = new JSONObject();
									JSONArray newFileList = new JSONArray();
									newFileList.add(resObj);
									newFolderObj.put("folderNm", dirNm);
									newFolderObj.put("fileList", newFileList);
									folderList.add(newFolderObj);
								}
								exist = true;
							}
						}
						if (!exist) {
							JSONObject casMap = new JSONObject();
							casMap.put("date", date);
							JSONArray folderList = new JSONArray();
							JSONObject folderObj = new JSONObject();
							folderObj.put("folderNm", dirNm);
							JSONArray fileList = new JSONArray();
							fileList.add(resObj);
							folderObj.put("fileList", fileList);
							folderList.add(folderObj);
							casMap.put("folderList", folderList);
							casArry.add(casMap);
						}
					}
					
					if (dataCd.contentEquals("DSCD119")) { // SAR:119 2024.08.05
						String date = dataset.get("potogrfBeginDt").toString();
						String fNm = (String) dataset.get("fileNm");
						String[] fNmArry = fNm.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						String bandCnUp = bandCn.toUpperCase();
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							resObj.put("vidoNm", bandCn + ".tif");
						} else {
							resObj.put("vidoNm", fNm + ".tif");
						}

						boolean exist = false;
						for (int j = 0; j < sarArry.size(); j++) {
							JSONObject sarsatMap = (JSONObject) sarArry.get(j);
							String mapFolder = (String) sarsatMap.get("date");
							if (mapFolder.contentEquals(date)) {
								JSONArray folderList = (JSONArray) sarsatMap.get("folderList");
								boolean existin = false;
								for (int f = 0; f < folderList.size(); f++) {
									JSONObject folderObj = (JSONObject) folderList.get(f);
									String folderNm = (String) folderObj.get("folderNm");
									if (folderNm.contentEquals(dirNm)) {
										JSONArray fileList = (JSONArray) folderObj.get("fileList");
										fileList.add(fileList.size(), resObj);
										existin = true;
									}
								}
								if (!existin) {
									JSONObject newFolderObj = new JSONObject();
									JSONArray newFileList = new JSONArray();
									newFileList.add(resObj);
									newFolderObj.put("folderNm", dirNm);
									newFolderObj.put("fileList", newFileList);
									folderList.add(newFolderObj);
								}
								exist = true;
							}
						}
						if (!exist) {
							JSONObject sarsatMap = new JSONObject();
							sarsatMap.put("date", date);
							JSONArray folderList = new JSONArray();
							JSONObject folderObj = new JSONObject();
							folderObj.put("folderNm", dirNm);
							JSONArray fileList = new JSONArray();
							fileList.add(resObj);
							folderObj.put("fileList", fileList);
							folderList.add(folderObj);
							sarsatMap.put("folderList", folderList);
							sarArry.add(sarsatMap);
						}
					}
					
					// 항공영상
					if (dataCd.contentEquals("DSCD120")) {
						String dpi = (String) dataset.get("dpi");
						boolean exist = false;
						for (int j = 0; j < airMapList.size(); j++) {
							JSONObject airMap = (JSONObject) airMapList.get(j);
							String mapYear = (String) airMap.get("year");
							if (mapYear.contentEquals(year)) {
								if (airMap.get("maps") != null) {
									JSONArray mapList = (JSONArray) airMap.get("maps");
									for (int m = 0; m < mapList.size(); m++) {
										JSONObject mapObj = (JSONObject) mapList.get(m);
										if (mapObj.get("dpi") != null) {
											String dpiIn = (String) mapObj.get("dpi");
											JSONArray map = (JSONArray) mapObj.get("map");
											if (dpi.contentEquals(dpiIn)) {
												map.add(resObj);
												exist = true;
												break;
											}
										}
									}
									if (!exist) {
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray mapArry = new JSONArray();
										mapArry.add(resObj);
										newMapObj.put("map", mapArry);
										mapList.add(newMapObj);
										exist = true;
									}
								} else {
									JSONArray mapps = new JSONArray();
									JSONObject newMapObj = new JSONObject();
									newMapObj.put("dpi", dpi);
									JSONArray map = new JSONArray();
									map.add(resObj);
									newMapObj.put("maps", map);
									mapps.add(newMapObj);
									exist = true;
								}
							}
						}
						if (!exist) {
							JSONObject mapYear = new JSONObject();
							mapYear.put("year", year);
							JSONArray mapsList = new JSONArray();
							JSONObject newMapObj = new JSONObject();
							newMapObj.put("dpi", dpi);
							JSONArray mapArry = new JSONArray();
							mapArry.add(resObj);
							newMapObj.put("map", mapArry);
							mapsList.add(newMapObj);
							mapYear.put("maps", mapsList);
							airMapList.add(mapYear);
						}
					}
					// 정사영상
					if (dataCd.contentEquals("DSCD121")) {
						String dpi = (String) dataset.get("dpi");
						boolean exist = false;
						for (int j = 0; j < ortMapList.size(); j++) {
							JSONObject airMap = (JSONObject) ortMapList.get(j);
							String mapYear = (String) airMap.get("year");
							if (mapYear.contentEquals(year)) {
								if (airMap.get("maps") != null) {
									JSONArray mapList = (JSONArray) airMap.get("maps");
									for (int m = 0; m < mapList.size(); m++) {
										JSONObject mapObj = (JSONObject) mapList.get(m);
										if (mapObj.get("dpi") != null) {
											String dpiIn = (String) mapObj.get("dpi");
											JSONArray map = (JSONArray) mapObj.get("map");
											if (dpi.contentEquals(dpiIn)) {
												map.add(resObj);
												exist = true;
												break;
											}
										}
									}
									if (!exist) {
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray mapArry = new JSONArray();
										mapArry.add(resObj);
										newMapObj.put("map", mapArry);
										mapList.add(newMapObj);
										exist = true;
									}
								} else {
									JSONArray mapps = new JSONArray();
									JSONObject newMapObj = new JSONObject();
									newMapObj.put("dpi", dpi);
									JSONArray map = new JSONArray();
									map.add(resObj);
									newMapObj.put("maps", map);
									mapps.add(newMapObj);
									exist = true;
								}
							}
						}
						if (!exist) {
							JSONObject mapYear = new JSONObject();
							mapYear.put("year", year);
							JSONArray mapsList = new JSONArray();
							JSONObject newMapObj = new JSONObject();
							newMapObj.put("dpi", dpi);
							JSONArray mapArry = new JSONArray();
							mapArry.add(resObj);
							newMapObj.put("map", mapArry);
							mapsList.add(newMapObj);
							mapYear.put("maps", mapsList);
							ortMapList.add(mapYear);
						}
					}
					// DEM
					if (dataCd.contentEquals("DSCD251")) {
						String dpi = (String) dataset.get("dpi");
						boolean exist = false;
						for (int j = 0; j < demMapList.size(); j++) {
							JSONObject demMap = (JSONObject) demMapList.get(j);
							String mapYear = (String) demMap.get("year");
							if (mapYear.contentEquals(year)) {
								if (demMap.get("maps") != null) {
									JSONArray mapList = (JSONArray) demMap.get("maps");
									for (int m = 0; m < mapList.size(); m++) {
										JSONObject mapObj = (JSONObject) mapList.get(m);
										if (mapObj.get("dpi") != null) {
											String dpiIn = (String) mapObj.get("dpi");
											JSONArray map = (JSONArray) mapObj.get("map");
											if (dpi.contentEquals(dpiIn)) {
												map.add(resObj);
												exist = true;
												break;
											}
										}
									}
									if (!exist) {
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray mapArry = new JSONArray();
										mapArry.add(resObj);
										newMapObj.put("map", mapArry);
										mapList.add(newMapObj);
										exist = true;
									}
								} else {
									JSONArray mapps = new JSONArray();
									JSONObject newMapObj = new JSONObject();
									newMapObj.put("dpi", dpi);
									JSONArray map = new JSONArray();
									map.add(resObj);
									newMapObj.put("maps", map);
									mapps.add(newMapObj);
									exist = true;
								}
							}
						}
						if (!exist) {
							JSONObject mapYear = new JSONObject();
							mapYear.put("year", year);
							JSONArray mapsList = new JSONArray();
							JSONObject newMapObj = new JSONObject();
							newMapObj.put("dpi", dpi);
							JSONArray mapArry = new JSONArray();
							mapArry.add(resObj);
							newMapObj.put("map", mapArry);
							mapsList.add(newMapObj);
							mapYear.put("maps", mapsList);
							demMapList.add(mapYear);
						}
					}

					// 긴급영상
					if (dataCd.contentEquals("DSCD130")) {
						String msfrtnTyCd = (String) dataset.get("msfrtnTyCd");
						if (msfrtnTyCd.contentEquals("DTC002")) { // 지진
							JSONArray mapList = (JSONArray) disasterJSON.get("Earthquake");
							mapList.add(mapList.size(), resObj);
						}
						if (msfrtnTyCd.contentEquals("DTC003")) { // 산불
							JSONArray mapList = (JSONArray) disasterJSON.get("ForestFire");
							mapList.add(mapList.size(), resObj);
						}
						if (msfrtnTyCd.contentEquals("DTC001")) { // 수해
							JSONArray mapList = (JSONArray) disasterJSON.get("Flood");
							mapList.add(mapList.size(), resObj);
						}
						if (msfrtnTyCd.contentEquals("DTC004")) { // 산사태
							JSONArray mapList = (JSONArray) disasterJSON.get("Landslide");
							mapList.add(mapList.size(), resObj);
						}
						if (msfrtnTyCd.contentEquals("DTC007")) { // 해양재난
							JSONArray mapList = (JSONArray) disasterJSON.get("MaritimeDisaster");
							mapList.add(mapList.size(), resObj);
						}
						if (msfrtnTyCd.contentEquals("DTC020")) { // 적조
							JSONArray mapList = (JSONArray) disasterJSON.get("RedTide");
							mapList.add(mapList.size(), resObj);
						}
					}

					// 분석결과
					if (dataCd.contentEquals("DSCD411")) { // 객체추출 래스터
						objRaster.add(resObj);
					}
					if (dataCd.contentEquals("DSCD412")) { // 객체추출 벡터
						objVector.add(resObj);
					}
					if (dataCd.contentEquals("DSCD421")) { // 변화탐지 래스터
						chgRaster.add(resObj);
					}
					if (dataCd.contentEquals("DSCD422")) { // 변화탐지 벡터
						chgVector.add(resObj);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	// 국토정보플랫폼 / 국토영상정보공급시스템  전송
	public JSONObject cmsc003sendDataset(CMSC003VO2 cmsc003vo2) throws Exception {
				
		String disasterId = cmsc003vo2.getMsfrtnInfoColctRequstId().toString();
		
		//RlsTy type
		String rlsTy = cmsc003vo2.getRlsTy();
		
		//오라클 조회
		List<EgovMap> datasetList = (List<EgovMap>) cmsc003UgisMapper.selectDatasetInfo(disasterId);
		//List<EgovMap> datasetList =  null;
	
		//RlsTy = 2일 경우 오라클 insert 및 pgadmin update
		if ("2".equals(rlsTy)) {
			
			// 중복 데이터 삭제
			if (datasetList.size() > 0) {
				// delete geoserver
				JSONArray dArr = new JSONArray();
				for (EgovMap dataset : datasetList) {
					String fileNm = (String) dataset.get("fileNm");
					String msfrtnId = (String) dataset.get("msfrtnId");
					String fileTy = (String) dataset.get("fileTy");

					LinkedHashMap<String, String> tmp = new LinkedHashMap<String, String>();
					tmp.put("layername", fileNm);
					tmp.put("extention", fileTy);
					tmp.put("msfrtnid", msfrtnId);
					dArr.add(tmp);
				}
				JSONObject dataJson = new JSONObject();
				dataJson.put("data", dArr);

				// send delete API
				HttpGeoserverAPI api = new HttpGeoserverAPI();
				api.send(datasetDeleteUrl, dataJson.toString());

				// delete oracle datasetinfo
				cmsc003UgisMapper.deleteDatasetInfo(disasterId);
			}
			

			// select usgs dataset
			List<EgovMap> usgsDatasetList = cmsc003Mapper.selectDatasetInfoByIdList(cmsc003vo2);
			// db insert
			if (usgsDatasetList.size() > 0) {

				// zip & upload file
				CMSC003VO4 cmsc003vo4 = new CMSC003VO4();
				cmsc003vo4.setMsfrtnId(Long.toString(cmsc003vo2.getMsfrtnInfoColctRequstId()));
				EgovMap msfrtnInfo = cmsc003Mapper.selectMsfrtnDataset(cmsc003vo4);
				String dir = (String) msfrtnInfo.get("datasetCoursNm");
				String tmpPath = dir.replace(rootPath, rootPath + "tmp/");
				File tmpDir = new File(tmpPath);
				if (!tmpDir.exists()) {
					tmpDir.mkdirs();
				}

				for (EgovMap dataset : usgsDatasetList) {
					try {
						int ltopCrdntX = ((BigDecimal) dataset.get("ltopCrdntX")).intValue();
						int ltopCrdntY = ((BigDecimal) dataset.get("ltopCrdntY")).intValue();
						int rbtmCrdntX = ((BigDecimal) dataset.get("rbtmCrdntX")).intValue();
						int rbtmCrdntY = ((BigDecimal) dataset.get("rbtmCrdntY")).intValue();
						int rtopCrdntX = ((BigDecimal) dataset.get("rtopCrdntX")).intValue();
						int rtopCrdntY = ((BigDecimal) dataset.get("rtopCrdntY")).intValue();
						int lbtmCrdntX = ((BigDecimal) dataset.get("lbtmCrdntX")).intValue();
						int lbtmCrdntY = ((BigDecimal) dataset.get("lbtmCrdntY")).intValue();

						dataset.put("ltopCrdntX", ltopCrdntY);
						dataset.put("ltopCrdntY", ltopCrdntX);
						dataset.put("rbtmCrdntX", rbtmCrdntY);
						dataset.put("rbtmCrdntY", rbtmCrdntX);
						dataset.put("rtopCrdntX", rtopCrdntY);
						dataset.put("rtopCrdntY", rtopCrdntX);
						dataset.put("lbtmCrdntX", lbtmCrdntY);
						dataset.put("lbtmCrdntY", lbtmCrdntX);

						if (dataset.get("mapNm") == null) {
							dataset.put("mapNm", "");
						}

						String fnm = (String) dataset.get("fileNm");
						String korNm = (String) dataset.get("fileKorNm");
						
						
						//밴드 타입 디비에서 가져오기
						/*List<DataCodeVO> dataCodoVO = new ArrayList<DataCodeVO>();
						dataCodoVO = cmsc003Mapper.selectDataCode();

						List<String> bandType = new ArrayList<String>();
						
						for (DataCodeVO dataCodo : dataCodoVO) {
							
							bandType.add(dataCodo.getdepthTwo());
						}
						
					
					    //밴드 타입 삭제
					    for (int bandTypeCnt = 0; bandTypeCnt< bandType.size(); bandTypeCnt++) {
					        	
					        if (fnm.toUpperCase().endsWith(bandType.get(bandTypeCnt))) {
					        	korNm = korNm.replaceAll(bandType.get(bandTypeCnt), "");
							} 
						}*/
					    
					    
						// cir, rgb 구분 코드
						if (fnm.toUpperCase().endsWith("_CIR")) {
							if(!korNm.toUpperCase().endsWith("_CIR")) {
								korNm = korNm + "_CIR";
							}
							
						}
						
						if (fnm.toUpperCase().endsWith("_RGB")) {
							if(!korNm.toUpperCase().endsWith("_RGB")) {
								korNm = korNm + "_RGB";
							}
							
						}
						
						dataset.put("fileKorNm", korNm);

						
						//RLS_TY = 2일 경우 오라클 insert
						cmsc003UgisMapper.insertDatasetInfoMap(dataset);
						
						String disasterNm = (String) dataset.get("msfrtnTyNm");
						String disasterNmEn = ""; // 영문 재난명
						if (disasterNm.contains("지진")) {
							disasterNmEn = "EARTHQUAKE";
						} else if (disasterNm.contains("산불")) {
							disasterNmEn = "FORESTFIRE";
						} else if (disasterNm.contains("수해")) {
							disasterNmEn = "FLOOD";
						} else if (disasterNm.contains("산사태")) {
							disasterNmEn = "LANDSLIDE";
						} else if (disasterNm.contains("해양재난")) {
							disasterNmEn = "MARITIMEDISASTER";
						} else if (disasterNm.contains("적조")) {
							disasterNmEn = "REDTIDE";
						}

						// copy file
						String path = (String) dataset.get("fullFileCoursNm");
						File datadir = new File(path).getParentFile();
						String copyPath = path.replace(rootPath, rootPath + "tmp/");
						File copyDir = new File(copyPath).getParentFile();
						if (!copyDir.exists()) {
							copyDir.mkdirs();
						}
						File[] files = datadir.listFiles();
						for (int f = 0; f < files.length; f++) {
							File orgFile = files[f];
							File newFile = new File(
									orgFile.getPath().replace("\\", "/").replace(rootPath, rootPath + "tmp/"));
							Files.copy(orgFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				String finFile = dir + ".zip";
				ZipUtil.pack(new File(tmpPath), new File(finFile));

				HttpUploader uploader = new HttpUploader();
				boolean uploaded = uploader.upload(httpBoundary, datasetUrl, datasetUploadPath, finFile);
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> zip upload : " + uploaded);
				if (!uploaded) {
					cmsc003UgisMapper.deleteDatasetInfo(disasterId);
				}
				new File(finFile).delete();
				FileUtils.deleteDirectory(new File(rootPath + "tmp/"));
			}
		}else {
			
			//RlsTy = 1일 경우 오라클 조회 후 삭제 및 pgadmin update
			if (datasetList.size() > 0) {
				// delete geoserver
				JSONArray dArr = new JSONArray();
				for (EgovMap dataset : datasetList) {
					String fileNm = (String) dataset.get("fileNm");
					String msfrtnId = (String) dataset.get("msfrtnId");
					String fileTy = (String) dataset.get("fileTy");

					LinkedHashMap<String, String> tmp = new LinkedHashMap<String, String>();
					tmp.put("layername", fileNm);
					tmp.put("extention", fileTy);
					tmp.put("msfrtnid", msfrtnId);
					dArr.add(tmp);
				}
				JSONObject dataJson = new JSONObject();
				dataJson.put("data", dArr);

				// send delete API
				HttpGeoserverAPI api = new HttpGeoserverAPI();
				api.send(datasetDeleteUrl, dataJson.toString());

				// delete oracle datasetinfo
				cmsc003UgisMapper.deleteDatasetInfo(disasterId);
			}
			
		}
		
		
		List datasetIdList = cmsc003vo2.getDatasetId();
		
		if (datasetIdList.size() > 0) {
			
			for(int i = 0; i < datasetIdList.size(); i++) {
				
				Map<String, Object> rlsTyMap = new HashMap<String, Object>();
				rlsTyMap.put("rlsTy", rlsTy);
				rlsTyMap.put("datasetId", datasetIdList.get(i));
				
				EgovMap datasetFileNm = cmsc003Mapper.selectFileNm(rlsTyMap);
				String oriFileNm = (String) datasetFileNm.get("fileNm");
				String oriFileKorNm = (String) datasetFileNm.get("fileKorNm");
				
				if (oriFileNm.toUpperCase().endsWith("_CIR")) {
					if(!oriFileKorNm.toUpperCase().endsWith("_CIR")) {
						oriFileKorNm = oriFileKorNm+"_CIR";
					}
				}
				
				if (oriFileNm.toUpperCase().endsWith("_RGB")) {
					if(!oriFileKorNm.toUpperCase().endsWith("_RGB")) {
						oriFileKorNm = oriFileKorNm+"_RGB";
					}
				}
				
				rlsTyMap.put("fileKorNm", oriFileKorNm);
				
				//RLS_TY값 업데이트 
				cmsc003Mapper.updateDatasetRlsTy(rlsTyMap);
			}
		}
		

		return null;
	}
	

	@Override
	public EgovMap selectDatasetInfoByFileCoursNm(String input_vido) throws Exception {

		return cmsc003Mapper.selectDatasetInfoByFileCoursNm(input_vido);
	}

	@Override
	public List<String> cmsc003mosaicResult(JSONObject createObj) throws Exception {

		// 재난유형명 검색
		String disasterCd = (String) createObj.get("disasterCd");
		List disasterMap = cmsc003Mapper.selectTcMsfrtnTyInfo(disasterCd);
		EgovMap disasterInfo = (EgovMap) disasterMap.get(0);
		String disasterNm = (String) disasterInfo.get("msfrtnTyNm"); // 한글 재난명
		String disasterId = (String) createObj.get("disasterId");
		EgovMap reqMap = selectByRequestId(disasterId);

		String outDir = "";
		outDir += rootPath;

		String disasterNmEn = ""; // 영문 재난명
		if (disasterNm.contains("지진")) {
			disasterNmEn = "EARTHQUAKE";
		} else if (disasterNm.contains("산불")) {
			disasterNmEn = "FORESTFIRE";
		} else if (disasterNm.contains("수해")) {
			disasterNmEn = "FLOOD";
		} else if (disasterNm.contains("산사태")) {
			disasterNmEn = "LANDSLIDE";
		} else if (disasterNm.contains("해양재난")) {
			disasterNmEn = "MARITIMEDISASTER";
		} else if (disasterNm.contains("적조")) {
			disasterNmEn = "REDTIDE";
		} else {
			disasterNmEn = "TMP";
		}
		outDir += disasterNmEn + "/" + disasterId + "/" + disasterId;

		// Existing
		JSONObject existing = (JSONObject) createObj.get("existing");
		List<String> pathList = new ArrayList<String>();

		// 정사영상
		JSONArray ortOrientalList = (JSONArray) existing.get("ortOrientalMap");
		if (ortOrientalList.size() > 0) {
			for (int a = 0; a < ortOrientalList.size(); a++) {
				JSONObject ortObj = (JSONObject) ortOrientalList.get(a);
				String year = (String) ortObj.get("year");
				JSONArray maps = (JSONArray) ortObj.get("maps");
				for (int i = 0; i < maps.size(); i++) {
					JSONObject map = (JSONObject) maps.get(i);
					String dpi = (String) map.get("dpi");
					String ortOriDir = outDir + "/Existing/Image/Aerial/" + year + "/" + dpi;

					String path = ortOriDir + "/" + "MosaicResult_" + year + "_" + dpi + ".tif";
					pathList.add(path);
				}
			}
		}
		return pathList;
	}

	@Override
	public EgovMap selectDatasetByInFileCours(String input_vido) throws Exception {
		return cmsc003Mapper.selectDatasetInfoByFileCoursNm(input_vido);
	}

	@Override
	public void insertDatasetInfo(CMSC003DataSetVO dataSetVo) throws Exception {
		cmsc003Mapper.insertDatasetInfo(dataSetVo);
	}
	
}
