package ugis.service;

import java.sql.SQLException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CMSC003DataSetVO;
import ugis.service.vo.CMSC003VO;
import ugis.service.vo.CMSC003VO2;
import ugis.service.vo.CMSC003VO3;

/**
 * @Class Name : CMSC003Service.java
 * @Description : CMSC003 Service Interface
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
public interface CMSC003Service {

	/**
	 * 연속수치지도 Geoserver 데이터 조회
	 * 
	 * @param cmsc003VO - 조회할 정보가 담긴 VO
	 * @return 데이터 목록
	 * @exception Exception
	 */
	List<?> selectDigitalMapList(CMSC003VO cmsc003VO) throws Exception;

	/**
	 * 인구격자통계 Geoserver 데이터 조회
	 * 
	 * @param cmsc003VO - 조회할 정보가 담긴 VO
	 * @return 데이터 목록
	 * @exception Exception
	 */
//	List<?> selectDemographicsList(CMSC003VO cmsc003VO) throws Exception;

	/**
	 * Oracle "AIR_ORIENTMAP_AS" 항공영상 데이터 조회
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	List<?> selectAirOrientMap(CMSC003VO cmsc003VO);

	/**
	 * Oracle "ORT_ORIENTMAP_AS" 항공영상 데이터 조회
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	JSONObject selectOrtOrientMap(CMSC003VO cmsc003VO);

	/**
	 * Oracle "DEM_ORIENTMAP_AS" DEM 데이터 조회
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	List<?> selectDemMap(CMSC003VO cmsc003VO);

	/**
	 * "TN_USGS" 데이터 조회
	 * 
	 * @param cmsc003VO
	 * @return "DEM" : [], "연속수치지도" : [], "영상자료" : [],
	 * 
	 * @throws Exception
	 */
//	JSONObject selectUsgsList(CMSC003VO cmsc003VO);

	/**
	 * "TN_USGS_WORK" 데이터 조회
	 * 
	 * @param cmsc003VO
	 * @return "DEM" : [], "연속수치지도" : [], "영상자료" : [],
	 * 
	 * @throws Exception
	 */
	JSONObject selectUsgsWorkList(CMSC003VO cmsc003VO);

	/**
	 * "n3a_g0010000" 데이터 조회
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	List<?> selectG001List();

	/**
	 * "n3a_g0100000" 데이터 조회
	 * 
	 * @param 시도 코드
	 * @return
	 * @throws Exception
	 */
	List<?> selectG010List(CMSC003VO cmsc003VO);

	/**
	 * "n3a_g0110000" 데이터 조회
	 * 
	 * @param 시군구 코드
	 * @return
	 * @throws Exception
	 */
	List<?> selectG011List(CMSC003VO cmsc003VO);

	/**
	 * "n3a_g0010000" Geom 데이터 조회
	 * 
	 * @param 시도 코드
	 * @return
	 * @throws Exception
	 */
	List<?> selectG001Geom(CMSC003VO cmsc003VO);

	/**
	 * "n3a_g0100000" Geom 데이터 조회
	 * 
	 * @param 시군구 코드
	 * @return
	 * @throws Exception
	 */
	List<?> selectG010Geom(CMSC003VO cmsc003VO);

	/**
	 * "n3a_g0110000" Geom 데이터 조회
	 * 
	 * @param 읍면동 코드
	 * @return
	 * @throws Exception
	 */
	List<?> selectG011Geom(CMSC003VO cmsc003VO);

	List<?> selectDisasterInfoId(CMSC003VO2 cmsc003VO2);

	JSONObject cmsc003createData(JSONObject createObj) throws Exception;

	EgovMap selectByRequestId(String requestId);

	List<?> selectAllUsgsWorkList(ObjChangeSearch dto) throws SQLException;

	List<ObjChangeListRes> selectDatasetWorkList(ObjChangeSearch dto) throws SQLException;

	JSONObject cmsc003saveData(JSONObject createObj) throws Exception;

	List<EgovMap> selectDatasetByDisasterId(CMSC003VO3 cmsc003vo3) throws Exception;

	EgovMap selectUsgsByInFileCours(String fileCours) throws Exception;

	EgovMap selectUsgsWorkByInFileCours(String input_vido) throws Exception;

	List<EgovMap> selectDatasetAirByDisasterId(CMSC003VO3 cmsc003vo3) throws Exception;

	List<EgovMap> selectDatasetOrtByDisasterId(CMSC003VO3 cmsc003vo3) throws Exception;

	List<?> selectGraphicsList(CMSC003VO cmsc003vo) throws Exception;

	JSONObject selectUsgsDisaterList(CMSC003VO cmsc003vo);

	JSONArray selectUsgsSatelliteList(CMSC003VO cmsc003vo);

	JSONObject selectUsgsAnalysisList(CMSC003VO cmsc003vo);

	JSONObject cmsc003searchDataset(CMSC003VO2 cmsc003vo2) throws Exception;

	JSONObject cmsc003sendDataset(CMSC003VO2 cmsc003vo2) throws Exception;

	EgovMap selectDatasetInfoByFileCoursNm(String input_vido) throws Exception;

	List<String> cmsc003mosaicResult(JSONObject createObj) throws Exception;

	EgovMap selectDatasetByInFileCours(String input_vido) throws Exception;

	void insertDatasetInfo(CMSC003DataSetVO dataSetVo) throws Exception;

}
