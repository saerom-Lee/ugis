package ugis.service.vo;

import java.util.List;

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
public class CMSC003VO2 {

	// 재난정보 id
	private Long msfrtnInfoColctRequstId;

	// 재난 타입 코드
	private String msfrtnTyCd;

	// 검색 지역 키워드
	private String msfrtnAreaSrchwrd;

	// 재난 지역명
	private String msfrtnTySrchwrd;

	// 도
	private String ctprvnNm;

	// 시
	private String ssgNm;

	// 동/면
	private String emdNm;

	private String lnbrAddr;

	// x좌표
	private Double spceCrdntX;

	// y좌표
	private Double spceCrdntY;

	// 시작기간
	private String colctBeginDe;

	// 종료기간
	private String colctEndDe;

	private String colctSttusCd;

	private double ltopCrdntX;

	private double ltopCrdntY;

	private double rtopCrdntX;

	private double rtopCrdntY;

	private double lbtmCrdntX;

	private double lbtmCrdntY;

	private double rbtmCrdntX;

	private double rbtmCrdntY;

	private String mapPrjctnCn;

	private List<Long> datasetId;

	private Long dataType; // 1: 원본데이터, 2: 긴급공간정보, 3: 처리데이터
	
	private String rlsTy;

	public CMSC003VO2() {
		super();
	}

}
