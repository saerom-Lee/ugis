package ugis.service.vo;

import lombok.Data;

/**
 * @Class Name : CMSC003VO2.java
 * @Description : 긴급공간정보 생성
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
@Data
public class CMSC003VO3 {

	// id
	private Integer datasetId;

	// usgs vido id
	private Integer vidoId;

	// 재난정보 id
	private Long msfrtnInfoColctRequstId;

	// 시작일
	private String dateFrom;

	// 종료일
	private String dateTo;

	// 대상 1 : 수집데이터(usgs), 2 : 처리데이터(usgs_work)
	private Integer dataKind;

	public CMSC003VO3() {
		super();
	}

}
