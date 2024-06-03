package ugis.service;

import java.util.List;

import org.json.simple.JSONObject;

import ugis.service.vo.CMSC003VO;

/**
 * @Class Name : CMSC003Service2.java
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
public interface CMSC003ServiceOthers {

	List<?> selectDigitalMapList(CMSC003VO cmsc003vo) throws Exception;

	List<?> selectAirOrientMap(CMSC003VO cmsc003vo) throws Exception;

	List<?> selectOrtOrientMap(CMSC003VO cmsc003vo) throws Exception;

	List<?> selectDemMap(CMSC003VO cmsc003vo) throws Exception;

	List<?> selectGraphicsList(CMSC003VO cmsc003vo) throws Exception;

	List<?> selectUsgsSatelliteList(CMSC003VO cmsc003vo) throws Exception;

	JSONObject selectUsgsDisaterList(CMSC003VO cmsc003vo) throws Exception;

	JSONObject selectUsgsAnalysisList(CMSC003VO cmsc003vo) throws Exception;

}
