package ugis.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.CMSC003Service;
import ugis.service.CMSC003ServiceOthers;
import ugis.service.mapper.CMSC003Mapper;
import ugis.service.vo.CISC005VO;
import ugis.service.vo.CMSC003VO;
import ugis.service.vo.CMSC003VO2;
import ugis.util.XmlParser;

@Service("cmsc003ServiceOthers")
public class CMSC003ServiceOthersImpl extends EgovAbstractServiceImpl implements CMSC003ServiceOthers {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	@Resource(name = "cmsc003Service")
	private CMSC003Service cmsc003Service;

	@Resource(name = "cmsc003Mapper")
	private CMSC003Mapper cmsc003Mapper;

	@Override
	public List<?> selectDigitalMapList(CMSC003VO cmsc003VO) throws Exception {

		JSONArray digitalArr = new JSONArray();
		String disasterId = cmsc003VO.getDisasterId();
		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) { // 원본데이터 (TN_USGS)
				digitalArr.addAll(cmsc003Service.selectDigitalMapList(cmsc003VO));
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfo(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
							digitalArr.add(resObj);
						}
					}
				}
			}
		}
		return digitalArr;
	}

	@Override
	public List<?> selectAirOrientMap(CMSC003VO cmsc003VO) throws Exception {

		JSONArray airMapList = new JSONArray();
		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) { // 원본데이터 (gfra)
				List<EgovMap> airList = cmsc003Mapper.selectAirDatasetByDisasterId(cmsc003VO);
				if (airList.size() > 0) {
					for (EgovMap air : airList) {
						String path = (String) air.get("innerFileCoursNm");
						File file = new File(path);
						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");
						String potogrfBeginDt = air.get("potogrfBeginDt").toString(); // 발생년도
						String year = potogrfBeginDt.split("-")[0];

						JSONObject resMap = new JSONObject();
						resMap.put("vidoNm", fName);
						resMap.put("ltopCrdntX", air.get("ltopCrdntX"));
						resMap.put("ltopCrdntY", air.get("ltopCrdntY"));
						resMap.put("rbtmCrdntY", air.get("rbtmCrdntY"));
						resMap.put("rbtmCrdntX", air.get("rbtmCrdntX"));
						resMap.put("fullFileCoursNm", path);
						resMap.put("year", year);
						resMap.put("mapPrjctnCn", air.get("mapPrjctnCn"));
						resMap.put("usgsWorkId", air.get("usgsWorkId"));

						String thumbnailPath = path.replace(".tif", ".png");
						File tumbFile = new File(thumbnailPath);
						if (tumbFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", thumbnailPath);
						} else {
							String jpgPath = path.replace(".tif", ".jpg");
							File jpgFile = new File(jpgPath);
							if (jpgFile.exists()) {
								resMap.put("thumbnail", true);
								resMap.put("thumbnailFileCoursNm", jpgPath);
							} else {
								resMap.put("thumbnail", false);
							}
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
					}
				}
			} else if (dataType == 3) { // 처리데이터 (TN_USGS_WORK)
				List<EgovMap> airList = cmsc003Mapper.selectAirDatasetByDisasterId(cmsc003VO);
				if (airList.size() > 0) {
					for (EgovMap air : airList) {
						String path = (String) air.get("innerFileCoursNm");
						File file = new File(path);
						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");
						String potogrfBeginDt = air.get("potogrfBeginDt").toString(); // 발생년도
						String year = potogrfBeginDt.split("-")[0];

						JSONObject resMap = new JSONObject();
						resMap.put("vidoNm", fName);
						resMap.put("ltopCrdntX", air.get("ltopCrdntX"));
						resMap.put("ltopCrdntY", air.get("ltopCrdntY"));
						resMap.put("rbtmCrdntY", air.get("rbtmCrdntY"));
						resMap.put("rbtmCrdntX", air.get("rbtmCrdntX"));
						resMap.put("fullFileCoursNm", path);
						resMap.put("year", year);
						resMap.put("mapPrjctnCn", air.get("mapPrjctnCn"));
						resMap.put("usgsWorkId", air.get("usgsWorkId"));

						String thumbnailPath = path.replace(".tif", ".png");
						File tumbFile = new File(thumbnailPath);
						if (tumbFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", thumbnailPath);
						} else {
							String jpgPath = path.replace(".tif", ".jpg");
							File jpgFile = new File(jpgPath);
							if (jpgFile.exists()) {
								resMap.put("thumbnail", true);
								resMap.put("thumbnailFileCoursNm", jpgPath);
							} else {
								resMap.put("thumbnail", false);
							}
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
					}
				}

				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

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
												if (dpiIn.contentEquals(dpi)) {
													JSONArray map = (JSONArray) mapObj.get("map");
													map.add(resObj);
												}
											} else {
												JSONObject newMapObj = new JSONObject();
												newMapObj.put("dpi", dpi);
												JSONArray map = new JSONArray();
												map.add(resObj);
												newMapObj.put("map", map);
											}
										}
										exist = true;
										break;
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resObj);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
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
					}
				}

			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

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
												if (dpiIn.contentEquals(dpi)) {
													JSONArray map = (JSONArray) mapObj.get("map");
													map.add(resObj);
												}
											} else {
												JSONObject newMapObj = new JSONObject();
												newMapObj.put("dpi", dpi);
												JSONArray map = new JSONArray();
												map.add(resObj);
												newMapObj.put("map", map);
											}
										}
										exist = true;
										break;
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resObj);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
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
					}
				}
			}
		}
		return airMapList;
	}

	@Override
	public List<?> selectOrtOrientMap(CMSC003VO cmsc003VO) throws Exception {

		JSONArray ortMapList = new JSONArray();
		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
			Long dataType = cmsc003VO.getDataType();

			if (dataType == 1) { // 원본데이터 (gfra)
				List<EgovMap> ortList = cmsc003Mapper.selectOrtDatasetByDisasterId(cmsc003VO);
				for (EgovMap ort : ortList) {
					String path = (String) ort.get("innerFileCoursNm");
					File file = new File(path);
					String ext = FilenameUtils.getExtension(file.getName());
					String fName = file.getName().replace("." + ext, "");
					String potogrfBeginDt = ort.get("potogrfBeginDt").toString(); // 발생년도
					String year = potogrfBeginDt.split("-")[0];

					JSONObject resMap = new JSONObject();
					resMap.put("vidoNm", fName);
					resMap.put("ltopCrdntX", ort.get("ltopCrdntX"));
					resMap.put("ltopCrdntY", ort.get("ltopCrdntY"));
					resMap.put("rbtmCrdntY", ort.get("rbtmCrdntY"));
					resMap.put("rbtmCrdntX", ort.get("rbtmCrdntX"));
					resMap.put("fullFileCoursNm", path);
					resMap.put("year", year);
					resMap.put("mapPrjctnCn", ort.get("mapPrjctnCn"));
					resMap.put("usgsWorkId", ort.get("usgsWorkId"));

					String thumbnailPath = path.replace(".tif", ".png");
					File tumbFile = new File(thumbnailPath);
					if (tumbFile.exists()) {
						resMap.put("thumbnail", true);
						resMap.put("thumbnailFileCoursNm", thumbnailPath);
					} else {
						String jpgPath = path.replace(".tif", ".jpg");
						File jpgFile = new File(jpgPath);
						if (jpgFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", jpgPath);
						} else {
							resMap.put("thumbnail", false);
						}
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
				}
			} else if (dataType == 3) { // 처리데이터 (TN_USGS_WORK)
				List<EgovMap> ortList = cmsc003Mapper.selectOrtDatasetByDisasterId(cmsc003VO);
				for (EgovMap ort : ortList) {
					try {

						String path = (String) ort.get("innerFileCoursNm");
						File file = new File(path);
						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");
						String potogrfBeginDt = ort.get("potogrfBeginDt").toString(); // 발생년도
						String year = potogrfBeginDt.split("-")[0];

						JSONObject resMap = new JSONObject();
						resMap.put("vidoNm", fName);
						resMap.put("ltopCrdntX", ort.get("ltopCrdntX"));
						resMap.put("ltopCrdntY", ort.get("ltopCrdntY"));
						resMap.put("rbtmCrdntY", ort.get("rbtmCrdntY"));
						resMap.put("rbtmCrdntX", ort.get("rbtmCrdntX"));
						resMap.put("fullFileCoursNm", path);
						resMap.put("year", year);
						resMap.put("mapPrjctnCn", ort.get("mapPrjctnCn"));
						resMap.put("usgsWorkId", ort.get("usgsWorkId"));

						String thumbnailPath = path.replace(".tif", ".png");
						File tumbFile = new File(thumbnailPath);
						if (tumbFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", thumbnailPath);
						} else {
							String jpgPath = path.replace(".tif", ".jpg");
							File jpgFile = new File(jpgPath);
							if (jpgFile.exists()) {
								resMap.put("thumbnail", true);
								resMap.put("thumbnailFileCoursNm", jpgPath);
							} else {
								resMap.put("thumbnail", false);
							}
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
						continue;
					}
				}

				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {

						try {

							JSONObject resObj = new JSONObject();
							resObj.put("ltopCrdntY", dataset.get("ltopCrdntY"));
							resObj.put("ltopCrdntX", dataset.get("ltopCrdntX"));
							resObj.put("rbtmCrdntY", dataset.get("rbtmCrdntY"));
							resObj.put("rbtmCrdntX", dataset.get("rbtmCrdntX"));

							String fullFileCoursNm = (String) dataset.get("fullFileCoursNm");
							String thumbnailFileCoursNm = fullFileCoursNm.replace((String) dataset.get("fileTy"),
									"png");

							resObj.put("fullFileCoursNm", fullFileCoursNm);
							resObj.put("thumbnailFileCoursNm", thumbnailFileCoursNm);
							resObj.put("thumbnail", true);
							resObj.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
							resObj.put("vidoNm", dataset.get("fileNm"));

							String dataCd = (String) dataset.get("dataCd");
							String year = (String) dataset.get("year");

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
													if (dpi.contentEquals(dpiIn)) {
														JSONArray map = (JSONArray) mapObj.get("map");
														map.add(resObj);
													}
												} else {
													JSONObject newMapObj = new JSONObject();
													newMapObj.put("dpi", dpi);
													JSONArray map = new JSONArray();
													map.add(resObj);
													newMapObj.put("map", map);
												}
											}
											exist = true;
											break;
										} else {
											JSONArray mapps = new JSONArray();
											JSONObject newMapObj = new JSONObject();
											newMapObj.put("dpi", dpi);
											JSONArray map = new JSONArray();
											map.add(resObj);
											newMapObj.put("maps", map);
											mapps.add(newMapObj);
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
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}
				}
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

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
												if (dpi.contentEquals(dpiIn)) {
													JSONArray map = (JSONArray) mapObj.get("map");
													map.add(resObj);
												}
											} else {
												JSONObject newMapObj = new JSONObject();
												newMapObj.put("dpi", dpi);
												JSONArray map = new JSONArray();
												map.add(resObj);
												newMapObj.put("map", map);
											}
										}
										exist = true;
										break;
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resObj);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
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
					}
				}
			}
		}
		return ortMapList;
	}

	@Override
	public List<?> selectDemMap(CMSC003VO cmsc003VO) throws Exception {

		JSONArray demMapList = new JSONArray();
		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) { // 원본데이터 (gfra)
				List<EgovMap> demList = cmsc003Mapper.selectDemDatasetByDisasterId(cmsc003VO);
				for (EgovMap dem : demList) {
					String path = (String) dem.get("innerFileCoursNm");
					File file = new File(path);
					String ext = FilenameUtils.getExtension(file.getName());
					String fName = file.getName().replace("." + ext, "");
					String potogrfBeginDt = dem.get("potogrfBeginDt").toString(); // 발생년도
					String year = potogrfBeginDt.split("-")[0];

					JSONObject resMap = new JSONObject();
					resMap.put("vidoNm", fName);
					resMap.put("ltopCrdntX", dem.get("ltopCrdntX"));
					resMap.put("ltopCrdntY", dem.get("ltopCrdntY"));
					resMap.put("rbtmCrdntY", dem.get("rbtmCrdntY"));
					resMap.put("rbtmCrdntX", dem.get("rbtmCrdntX"));
					resMap.put("fullFileCoursNm", path);
					resMap.put("year", year);
					resMap.put("mapPrjctnCn", dem.get("mapPrjctnCn"));
					resMap.put("usgsWorkId", dem.get("usgsWorkId"));

					String thumbnailPath = path.replace(".tif", ".png");
					File tumbFile = new File(thumbnailPath);
					if (tumbFile.exists()) {
						resMap.put("thumbnail", true);
						resMap.put("thumbnailFileCoursNm", thumbnailPath);
					} else {
						String jpgPath = path.replace(".tif", ".jpg");
						File jpgFile = new File(jpgPath);
						if (jpgFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", jpgPath);
						} else {
							resMap.put("thumbnail", false);
						}
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
				}
			} else if (dataType == 3) { // 처리데이터 (TN_USGS_WORK)
				List<EgovMap> demList = cmsc003Mapper.selectDemDatasetByDisasterId(cmsc003VO);
				for (EgovMap dem : demList) {
					String path = (String) dem.get("innerFileCoursNm");
					File file = new File(path);
					String ext = FilenameUtils.getExtension(file.getName());
					String fName = file.getName().replace("." + ext, "");
					String potogrfBeginDt = dem.get("potogrfBeginDt").toString(); // 발생년도
					String year = potogrfBeginDt.split("-")[0];

					JSONObject resMap = new JSONObject();
					resMap.put("vidoNm", fName);
					resMap.put("ltopCrdntX", dem.get("ltopCrdntX"));
					resMap.put("ltopCrdntY", dem.get("ltopCrdntY"));
					resMap.put("rbtmCrdntY", dem.get("rbtmCrdntY"));
					resMap.put("rbtmCrdntX", dem.get("rbtmCrdntX"));
					resMap.put("fullFileCoursNm", path);
					resMap.put("year", year);
					resMap.put("mapPrjctnCn", dem.get("mapPrjctnCn"));
					resMap.put("usgsWorkId", dem.get("usgsWorkId"));

					String thumbnailPath = path.replace(".tif", ".png");
					File tumbFile = new File(thumbnailPath);
					if (tumbFile.exists()) {
						resMap.put("thumbnail", true);
						resMap.put("thumbnailFileCoursNm", thumbnailPath);
					} else {
						String jpgPath = path.replace(".tif", ".jpg");
						File jpgFile = new File(jpgPath);
						if (jpgFile.exists()) {
							resMap.put("thumbnail", true);
							resMap.put("thumbnailFileCoursNm", jpgPath);
						} else {
							resMap.put("thumbnail", false);
						}
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
				}

				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

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
												if (dpi.contentEquals(dpiIn)) {
													JSONArray map = (JSONArray) mapObj.get("map");
													map.add(resObj);
												}
											} else {
												JSONObject newMapObj = new JSONObject();
												newMapObj.put("dpi", dpi);
												JSONArray map = new JSONArray();
												map.add(resObj);
												newMapObj.put("map", map);
											}
										}
										exist = true;
										break;
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resObj);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
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
					}
				}
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

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
												if (dpi.contentEquals(dpiIn)) {
													JSONArray map = (JSONArray) mapObj.get("map");
													map.add(resObj);
												}
											} else {
												JSONObject newMapObj = new JSONObject();
												newMapObj.put("dpi", dpi);
												JSONArray map = new JSONArray();
												map.add(resObj);
												newMapObj.put("map", map);
											}
										}
										exist = true;
										break;
									} else {
										JSONArray mapps = new JSONArray();
										JSONObject newMapObj = new JSONObject();
										newMapObj.put("dpi", dpi);
										JSONArray map = new JSONArray();
										map.add(resObj);
										newMapObj.put("maps", map);
										mapps.add(newMapObj);
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
					}
				}
			}
		}
		return demMapList;
	}

	@Override
	public List<?> selectGraphicsList(CMSC003VO cmsc003VO) throws Exception {

		JSONArray graphicsArr = new JSONArray();
		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상
			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) { // 원본데이터 (TN_USGS)
				graphicsArr.addAll(cmsc003Service.selectGraphicsList(cmsc003VO));
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfo(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
							graphicsArr.add(resObj);
						}
					}
				}
			}
		}
		return graphicsArr;
	}

	@Override
	public List<?> selectUsgsSatelliteList(CMSC003VO cmsc003VO) throws Exception {

		JSONArray result = new JSONArray();
		if (cmsc003VO.getDataKindCurrent() != null && cmsc003VO.getDataKindCurrent().contentEquals("on")) { // 기존영상

			JSONArray kompsatArry = new JSONArray(); // Kompsat
			JSONArray landsatArry = new JSONArray(); // Landsat
			JSONArray sentinelArry = new JSONArray(); // Sentinel
			JSONArray casArry = new JSONArray(); // CAS

			JSONObject kompsatObj = new JSONObject();
			JSONObject landsatObj = new JSONObject();
			JSONObject sentinelObj = new JSONObject();
			JSONObject casObj = new JSONObject();

			kompsatObj.put("satNm", "Kompsat");
			kompsatObj.put("map", kompsatArry);

			landsatObj.put("satNm", "Landsat");
			landsatObj.put("map", landsatArry);

			sentinelObj.put("satNm", "Sentinel");
			sentinelObj.put("map", sentinelArry);

			casObj.put("satNm", "CAS");
			casObj.put("map", casArry);

			JSONArray sateArry = new JSONArray(); // 위성영상
			sateArry.add(kompsatObj);
			sateArry.add(landsatObj);
			sateArry.add(sentinelObj);
			sateArry.add(casObj);

			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) {
				List<EgovMap> satList = cmsc003Mapper.selectSatelliteDatasetByDisasterId(cmsc003VO);
				for (int i = 0; i < satList.size(); i++) {
					try {
						EgovMap sat = satList.get(i);
						String path = (String) sat.get("innerFileCoursNm");

						String[] pathArry = path.split("/");
						String dirNm = pathArry[5];
//						String dirNm = pathArry[4];

						File file = new File(path);
						File dir = file.getParentFile();

//						String dirNm = dir.getName();
						String date = sat.get("potogrfBeginDt").toString();

						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						String[] fNmArry = fName.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						JSONObject resObj = new JSONObject();

						resObj.put("ltopCrdntX", sat.get("ltopCrdntX"));
						resObj.put("ltopCrdntY", sat.get("ltopCrdntY"));
						resObj.put("rbtmCrdntX", sat.get("rbtmCrdntX"));
						resObj.put("rbtmCrdntY", sat.get("rbtmCrdntY"));
						resObj.put("mapPrjctnCn", sat.get("mapPrjctnCn"));
						resObj.put("innerFileCoursNm", path);
						resObj.put("fullFileCoursNm", path);
						resObj.put("potogrfBeginDt", sat.get("potogrfBeginDt").toString());

						if (sat.get("usgsWorkId") != null) {
							resObj.put("vidoNm", fName);
							resObj.put("usgsWorkId", sat.get("usgsWorkId"));
						} else {
							String bandCnUp = bandCn.toUpperCase();
							if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
									|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
									|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
								resObj.put("vidoNm", bandCn + ".tif");
							} else {
								resObj.put("vidoNm", fName);
							}
							resObj.put("vidoId", sat.get("vidoId"));
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

						String vidoCd = (String) sat.get("potogrfVidoCd");
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
							if (dataType == 1) {
								CISC005VO vo = seekFileInfo(vidoCd, path);
								resObj.put("radianceMult", vo.getRadianceMult());
								resObj.put("radianceAdd", vo.getRadianceAdd());
								resObj.put("reflectanceMult", vo.getReflectanceMult());
								resObj.put("reflectanceAdd", vo.getReflectanceAdd());
								resObj.put("metaData", vo.getMetaData());
								resObj.put("dirName", vo.getDirName());
								resObj.put("potogrfVidoCd", "1");
								resObj.put("tifFileName", file.getName());
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
						} else if (vidoCd.contentEquals("2")) { // Landsat
							if (dataType == 1) {
								CISC005VO vo = seekFileInfo(vidoCd, path);
								resObj.put("gain", vo.getGain());
								resObj.put("offset", vo.getOffset());
								resObj.put("metaData", vo.getMetaData());
								resObj.put("dirName", vo.getDirName());
								resObj.put("potogrfVidoCd", "2");
								resObj.put("tifFileName", file.getName());

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
						} else if (vidoCd.contentEquals("6")) { // Sentinel
//							if (dirNm.toUpperCase().contentEquals("IMG_DATA")) {
//								dirNm = dir.getParentFile().getParentFile().getParentFile().getName();
//							}
//							String[] pathArr = path.split("/");
//							dirNm = pathArr[5];

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
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			} else if (dataType == 3) { // 원본데이터 (TN_USGS) or 처리데이터 (TN_USGS_WORK)
				List<EgovMap> satList = cmsc003Mapper.selectSatelliteDatasetByDisasterId(cmsc003VO);
				for (int i = 0; i < satList.size(); i++) {
					try {
						EgovMap sat = satList.get(i);
						String path = (String) sat.get("innerFileCoursNm");

						File file = new File(path);
						File dir = file.getParentFile();

						String[] pathArry = path.split("/");
						String dirNm = pathArry[5];

//						String dirNm = dir.getName();
						String date = sat.get("potogrfBeginDt").toString();
//						dirNm = date;

						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						String[] fNmArry = fName.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						JSONObject resObj = new JSONObject();

						resObj.put("ltopCrdntX", sat.get("ltopCrdntX"));
						resObj.put("ltopCrdntY", sat.get("ltopCrdntY"));
						resObj.put("rbtmCrdntX", sat.get("rbtmCrdntX"));
						resObj.put("rbtmCrdntY", sat.get("rbtmCrdntY"));
						resObj.put("mapPrjctnCn", sat.get("mapPrjctnCn"));
						resObj.put("innerFileCoursNm", path);
						resObj.put("fullFileCoursNm", path);
						resObj.put("potogrfBeginDt", sat.get("potogrfBeginDt").toString());

						if (sat.get("usgsWorkId") != null) {
							resObj.put("vidoNm", fName);
							resObj.put("usgsWorkId", sat.get("usgsWorkId"));
						} else {
							String bandCnUp = bandCn.toUpperCase();
							if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
									|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
									|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
								resObj.put("vidoNm", bandCn + ".tif");
							} else {
								resObj.put("vidoNm", fName);
							}
							resObj.put("vidoId", sat.get("vidoId"));
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

						String vidoCd = (String) sat.get("potogrfVidoCd");
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
							dirNm = pathArr[5];

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
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
						resObj.put("ltopCrdntY", dataset.get("ltopCrdntY"));
						resObj.put("ltopCrdntX", dataset.get("ltopCrdntX"));
						resObj.put("rbtmCrdntY", dataset.get("rbtmCrdntY"));
						resObj.put("rbtmCrdntX", dataset.get("rbtmCrdntX"));

						String fullFileCoursNm = (String) dataset.get("fullFileCoursNm");
						String thumbnailFileCoursNm = fullFileCoursNm.replace((String) dataset.get("fileTy"), "png");

						File file = new File(fullFileCoursNm);
						String date = "";
						if (dataset.get("potogrfBeginDt") != null) {
							date = dataset.get("potogrfBeginDt").toString();
						}
						String dirNm = (String) dataset.get("satDir");

						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						String[] fNmArry = fName.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						String bandCnUp = bandCn.toUpperCase();

						String vidoNm = fName;
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							vidoNm = bandCn + ".tif";
						}
						resObj.put("vidoNm", vidoNm);
						resObj.put("fullFileCoursNm", fullFileCoursNm);
						resObj.put("thumbnailFileCoursNm", thumbnailFileCoursNm);
						resObj.put("thumbnail", true);
						resObj.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));

						String dataCd = (String) dataset.get("dataCd");
						// 위성영상
						if (dataCd.contentEquals("DSCD114")) { // Kompsat
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
					}
				}
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
						resObj.put("ltopCrdntY", dataset.get("ltopCrdntY"));
						resObj.put("ltopCrdntX", dataset.get("ltopCrdntX"));
						resObj.put("rbtmCrdntY", dataset.get("rbtmCrdntY"));
						resObj.put("rbtmCrdntX", dataset.get("rbtmCrdntX"));

						String fullFileCoursNm = (String) dataset.get("fullFileCoursNm");
						String thumbnailFileCoursNm = fullFileCoursNm.replace((String) dataset.get("fileTy"), "png");

						File file = new File(fullFileCoursNm);
//						File dir = file.getParentFile();
//						String dirNm = dir.getName();
						String dirNm = (String) dataset.get("satDir");

						String date = "";
						if (dataset.get("potogrfBeginDt") != null) {
							date = dataset.get("potogrfBeginDt").toString();
						}

						String ext = FilenameUtils.getExtension(file.getName());
						String fName = file.getName().replace("." + ext, "");

						String[] fNmArry = fName.split("_");
						String bandCn = fNmArry[fNmArry.length - 1];
						String bandCnUp = bandCn.toUpperCase();

						String vidoNm = fName;
						if (bandCnUp.startsWith("B") // B01, B1, B02, B2.....
								|| bandCnUp.equals("R") || bandCnUp.equals("G") || bandCnUp.equals("B")
								|| bandCnUp.equals("N") || bandCnUp.equals("P")) {
							vidoNm = bandCn + ".tif";
						}
						resObj.put("vidoNm", vidoNm);
						resObj.put("fullFileCoursNm", fullFileCoursNm);
						resObj.put("thumbnailFileCoursNm", thumbnailFileCoursNm);
						resObj.put("thumbnail", true);
						resObj.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));

						String dataCd = (String) dataset.get("dataCd");
						// 위성영상
						if (dataCd.contentEquals("DSCD114")) { // Kompsat
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
					}
				}
			}
			result.add(kompsatObj);
			result.add(landsatObj);
			result.add(sentinelObj);
			result.add(casObj);
		}
		return result;
	}

	public CISC005VO seekFileInfo(String dataType, String fileFullName) {

		String separator = "/";
		int idx = fileFullName.lastIndexOf(separator);
		String dirName = fileFullName.substring(0, idx + 1);
		String fileName = fileFullName.substring(idx + 1);

		idx = fileName.lastIndexOf("_");
		String filePrefixName = fileName.substring(0, idx);

		CISC005VO vo = null;
		if (dataType.equals("1")) { // Landsat txt
			try {
				File dir = new File(dirName);
				String[] filenames = dir.list((f, name) -> name.endsWith(".txt") && name.startsWith(filePrefixName));

				if (filenames == null) {
					System.out.println("메타데이터를 찾을수 없습니다." + "[" + filePrefixName + "*.txt]");
					return null;
				}
				for (int i = 0; i < filenames.length; i++) {
					vo = readTxtFileInfo(dirName + filenames[i], fileName);
					if (vo != null) {
						vo.setMetaData(filenames[i]);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (dataType.equals("2")) { // Kompsat Xml
			File dir = new File(dirName);
			String[] filenames = dir.list((f, name) -> name.endsWith(".xml") && name.startsWith(filePrefixName));
			if (filenames == null) {
				System.out.println("메타데이터를 찾을수 없습니다." + "[" + filePrefixName + "*.xml]");
				return null;
			}
			try {
				for (int i = 0; i < filenames.length; i++) {
					vo = readXmlFileInfo(dirName + filenames[i], fileName);
					if (vo != null) {
						vo.setMetaData(filenames[i]);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (vo != null) {
			vo.setDirName(dirName);
		}
		return vo;
	}

	public CISC005VO readTxtFileInfo(String txtFile, String tifFile) throws Exception {

		CISC005VO vo = new CISC005VO();
		boolean findOK = false;

		List<String> lines = Files.readAllLines(Paths.get(txtFile));

		String FILE_NAME_BAND = "";
		String RADIANCE_MULT_BAND_NAME = "";
		String RADIANCE_ADD_BAND_NAME = "";
		String REFLECTANCE_MULT_NAME = "";
		String REFLECTANCE_ADD_NAME = "";

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).indexOf(tifFile.toUpperCase()) > -1) {
				FILE_NAME_BAND = lines.get(i).substring(0, lines.get(i).indexOf("="));
				FILE_NAME_BAND = FILE_NAME_BAND.trim();
				break;
			}
		}
		// BAND 명을 찾았으면
		if (FILE_NAME_BAND.length() > 0) {
			RADIANCE_MULT_BAND_NAME = FILE_NAME_BAND.replace("FILE_NAME", "RADIANCE_MULT");
			RADIANCE_ADD_BAND_NAME = FILE_NAME_BAND.replace("FILE_NAME", "RADIANCE_ADD");
			REFLECTANCE_MULT_NAME = FILE_NAME_BAND.replace("FILE_NAME", "REFLECTANCE_MULT");
			REFLECTANCE_ADD_NAME = FILE_NAME_BAND.replace("FILE_NAME", "REFLECTANCE_ADD");

			String radianceMultVal = "";
			String radianceAddVal = "";
			String reflectanceMultVal = "";
			String reflectanceAddVal = "";

			for (int i = 0; i < lines.size(); i++) {

				if (radianceMultVal.equals("")) {
					if (lines.get(i).indexOf(RADIANCE_MULT_BAND_NAME) > -1) {
						radianceMultVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1, lines.get(i).length());
						vo.setRadianceMult(radianceMultVal.trim());
					}
				}
				if (radianceAddVal.equals("")) {
					if (lines.get(i).indexOf(RADIANCE_ADD_BAND_NAME) > -1) {
						radianceAddVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1, lines.get(i).length());
						vo.setRadianceAdd(radianceAddVal.trim());
					}
				}
				if (reflectanceMultVal.equals("")) {
					if (lines.get(i).indexOf(REFLECTANCE_MULT_NAME) > -1) {
						reflectanceMultVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1,
								lines.get(i).length());
						vo.setReflectanceMult(reflectanceMultVal.trim());
					}
				}
				if (reflectanceAddVal.equals("")) {
					if (lines.get(i).indexOf(REFLECTANCE_ADD_NAME) > -1) {
						reflectanceAddVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1,
								lines.get(i).length());
						vo.setReflectanceAdd(reflectanceAddVal.trim());
					}
				}

				if (!radianceMultVal.equals("") && !radianceAddVal.equals("") && !reflectanceMultVal.equals("")
						&& !reflectanceAddVal.equals("")) {
					findOK = true;
					break;
				}
			}

		}
		return vo;
	}

	public CISC005VO readXmlFileInfo(String xmlFile, String tifFile) throws Exception {

		CISC005VO vo = new CISC005VO();
		XmlParser p = new XmlParser(xmlFile);
		boolean findOK = false;
		// MS1~4까지 잧는다.
		for (int i = 1; i < 5; i++) {
			String ImageFileName = "//Image/" + "MS" + i;

			if (tifFile.equals(p.getString(ImageFileName + "/ImageFileName"))) {
				vo.setGain(p.getString(ImageFileName + "/RadianceConversion/Gain"));
				vo.setOffset(p.getString(ImageFileName + "/RadianceConversion/Offset"));
				findOK = true;
				break;
			}
		}

		return vo;

	}

	@Override
	public JSONObject selectUsgsDisaterList(CMSC003VO cmsc003VO) throws Exception {

		JSONObject result = new JSONObject();
		if (cmsc003VO.getDataKindEmergency() != null && cmsc003VO.getDataKindEmergency().contentEquals("on")) {

			JSONArray floodArry = new JSONArray(); // 수해
			JSONArray earthquakeArry = new JSONArray(); // 지진
			JSONArray maritimeDisasterArry = new JSONArray(); // 해양재난
			JSONArray landslideArry = new JSONArray(); // 산사태
			JSONArray forestFireArry = new JSONArray(); // 산불
			JSONArray redTideArry = new JSONArray(); // 적조

			result.put("Flood", floodArry);
			result.put("Earthquake", earthquakeArry);
			result.put("MaritimeDisaster", maritimeDisasterArry);
			result.put("Landslide", landslideArry);
			result.put("ForestFire", forestFireArry);
			result.put("RedTide", redTideArry);

			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) { // 원본데이터 (TN_USGS)
				List<EgovMap> disasterList = cmsc003Mapper.selectDisaterDatasetByDisasterId(cmsc003VO);
				for (int i = 0; i < disasterList.size(); i++) {
					EgovMap disaster = disasterList.get(i);
					String path = (String) disaster.get("innerFileCoursNm");
					File file = new File(path);
					String ext = FilenameUtils.getExtension(file.getName());
					String fName = file.getName().replace("." + ext, "");

					JSONObject resObj = new JSONObject();
					resObj.put("vidoNm", fName);
					resObj.put("ltopCrdntX", disaster.get("ltopCrdntX"));
					resObj.put("ltopCrdntY", disaster.get("ltopCrdntY"));
					resObj.put("rbtmCrdntX", disaster.get("rbtmCrdntX"));
					resObj.put("rbtmCrdntY", disaster.get("rbtmCrdntY"));
					resObj.put("mapPrjctnCn", disaster.get("mapPrjctnCn"));
					resObj.put("innerFileCoursNm", path);
					resObj.put("fullFileCoursNm", path);
					resObj.put("potogrfBeginDt", disaster.get("potogrfBeginDt").toString());

					if (disaster.get("usgsWorkId") != null) {
						resObj.put("usgsWorkId", disaster.get("usgsWorkId"));
					} else {
						resObj.put("vidoId", disaster.get("vidoId"));
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

					String vidoCd = (String) disaster.get("potogrfVidoCd");
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
					} else if (vidoCd.contentEquals("20")) { // REDTIDE
						JSONArray mapList = (JSONArray) result.get("RedTide");
						mapList.add(mapList.size(), resObj);
					}
				}
			} else if (dataType == 3) { // 처리데이터 (TN_USGS_WORK)
				List<EgovMap> disasterList = cmsc003Mapper.selectDisaterDatasetByDisasterId(cmsc003VO);
				for (int i = 0; i < disasterList.size(); i++) {
					EgovMap disaster = disasterList.get(i);
					String path = (String) disaster.get("innerFileCoursNm");
					File file = new File(path);
					String ext = FilenameUtils.getExtension(file.getName());
					String fName = file.getName().replace("." + ext, "");

					JSONObject resObj = new JSONObject();
					resObj.put("vidoNm", fName);
					resObj.put("ltopCrdntX", disaster.get("ltopCrdntX"));
					resObj.put("ltopCrdntY", disaster.get("ltopCrdntY"));
					resObj.put("rbtmCrdntX", disaster.get("rbtmCrdntX"));
					resObj.put("rbtmCrdntY", disaster.get("rbtmCrdntY"));
					resObj.put("mapPrjctnCn", disaster.get("mapPrjctnCn"));
					resObj.put("innerFileCoursNm", path);
					resObj.put("fullFileCoursNm", path);
					resObj.put("potogrfBeginDt", disaster.get("potogrfBeginDt").toString());

					if (disaster.get("usgsWorkId") != null) {
						resObj.put("usgsWorkId", disaster.get("usgsWorkId"));
					} else {
						resObj.put("vidoId", disaster.get("vidoId"));
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

					String vidoCd = (String) disaster.get("potogrfVidoCd");
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
					} else if (vidoCd.contentEquals("20")) { // REDTIDE
						JSONArray mapList = (JSONArray) result.get("RedTide");
						mapList.add(mapList.size(), resObj);
					}
				}
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);

				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

						// 긴급영상
						if (dataCd.contentEquals("DSCD130")) {
							String msfrtnTyCd = (String) dataset.get("msfrtnTyCd");
							if (msfrtnTyCd.contentEquals("DTC002")) { // 지진
								JSONArray mapList = (JSONArray) result.get("Earthquake");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC003")) { // 산불
								JSONArray mapList = (JSONArray) result.get("ForestFire");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC005")) { // 수해
								JSONArray mapList = (JSONArray) result.get("Flood");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC006")) { // 산사태
								JSONArray mapList = (JSONArray) result.get("Landslide");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC007")) { // 해양재난
								JSONArray mapList = (JSONArray) result.get("MaritimeDisaster");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC020")) { // 적조
								JSONArray mapList = (JSONArray) result.get("RedTide");
								mapList.add(mapList.size(), resObj);
							}
						}
					}
				}
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);

				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
						String year = (String) dataset.get("year");

						// 긴급영상
						if (dataCd.contentEquals("DSCD130")) {
							String msfrtnTyCd = (String) dataset.get("msfrtnTyCd");
							if (msfrtnTyCd.contentEquals("DTC002")) { // 지진
								JSONArray mapList = (JSONArray) result.get("Earthquake");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC003")) { // 산불
								JSONArray mapList = (JSONArray) result.get("ForestFire");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC005")) { // 수해
								JSONArray mapList = (JSONArray) result.get("Flood");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC006")) { // 산사태
								JSONArray mapList = (JSONArray) result.get("Landslide");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC007")) { // 해양재난
								JSONArray mapList = (JSONArray) result.get("MaritimeDisaster");
								mapList.add(mapList.size(), resObj);
							}
							if (msfrtnTyCd.contentEquals("DTC020")) { // 적조
								JSONArray mapList = (JSONArray) result.get("RedTide");
								mapList.add(mapList.size(), resObj);
							}
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public JSONObject selectUsgsAnalysisList(CMSC003VO cmsc003VO) throws Exception {

		JSONObject result = new JSONObject();
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

		result.put("objectExt", objObj);
		result.put("changeDet", chgObj);

		if (cmsc003VO.getDataKindResult() != null && cmsc003VO.getDataKindResult().contentEquals("on")) { // 분석결과
			Long dataType = cmsc003VO.getDataType();
			if (dataType == 1) { // 원본데이터
				result = cmsc003Service.selectUsgsAnalysisList(cmsc003VO);
			} else if (dataType == 2) { // 긴급공간정보 (TN_DATASET_INFO)
				String disasterId = cmsc003VO.getDisasterId();
				CMSC003VO2 cmsc003vo2 = new CMSC003VO2();
				cmsc003vo2.setMsfrtnInfoColctRequstId(Long.valueOf(disasterId));
				cmsc003vo2.setColctBeginDe(cmsc003VO.getDateFrom());
				cmsc003vo2.setColctEndDe(cmsc003VO.getDateTo());
				cmsc003vo2.setDataType(dataType);
				List<EgovMap> datasetList = cmsc003Mapper.selectAllDatasetInfoFromDate(cmsc003vo2);
				if (datasetList.size() > 0) {
					for (EgovMap dataset : datasetList) {
						JSONObject resObj = new JSONObject();
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
			}
		}
		return result;
	}
}
