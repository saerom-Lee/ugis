package ugis.service;

import org.json.simple.JSONObject;

/**
 * @Class Name : MNSC003Service.java
 * @Description : MNSC003 Service Interface
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
public interface MNSC003Service {

	JSONObject getAirThumbnail(JSONObject obj);
	
	JSONObject getOrtThumbnail(JSONObject obj);
	
	JSONObject getDemThumbnail(JSONObject obj);

}