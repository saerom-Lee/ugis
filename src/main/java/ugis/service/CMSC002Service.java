package ugis.service;

import java.util.List;

/**
 * @Class Name : CMSC002Service.java
 * @Description : CMSC002 Service Interface
 * @Modification Information
 * @
 * @  수정일 / 수정자 / 수정내용
 * @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
public interface CMSC002Service {
    List<?> selectSiList();

	List<?> selectSggList(String si);

    List<?> selectEmdList(String sgg);
}