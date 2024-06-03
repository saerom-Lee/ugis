package ugis.service.vo;

import lombok.Data;

/**
 * @Class Name : CMSC003VO.java
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
public class CMSC003VO {

	// request
	// 종류
	// 기존 데이터
	private String dataKindCurrent;
	// 긴급 영상
	private String dataKindEmergency;
	// 분석결과 데이터
	private String dataKindResult;

	// 대상
	private Long dataType; // 1: 원본데이터, 2: 긴급공간정보, 3: 처리데이터

	/* 수집기간 */
	// 시작일
	private String dateFrom;
	// 종료일
	private String dateTo;

	// 재난 ID
	private String disasterId;

	private String searchWork;

	// 시작년도
	private String dateYearFrom;
	// 종료년도
	private String dateYearTo;

	/* 해상도 */
	// 최소값
	private String resolMin;
	// 최대값
	private String resolMax;

	// 재난유형
	private String disasterType;

	/* 재난지역 */
	// 시도
	private String sido;
	// 시군구
	private String sigungu;
	// 읍면동
	private String emd;

	/* roi 4326 */
	// minx
	private String ulx4326;
	// maxy
	private String uly4326;
	// maxx
	private String lrx4326;
	// miny
	private String lry4326;

	/* roi 5186 */
	// minx
	private String ulx5186;
	// maxy
	private String uly5186;
	// maxx
	private String lrx5186;
	// miny
	private String lry5186;

	/* roi 5179 */
	// minx
	private String ulx5179;
	// maxy
	private String uly5179;
	// maxx
	private String lrx5179;
	// miny
	private String lry5179;

	/* roi 32652 */
	// minx
	private String ulx32652;
	// maxy
	private String uly32652;
	// maxx
	private String lrx32652;
	// miny
	private String lry32652;

	/* roi 5187 */
	// minx
	private String ulx5187;
	// maxy
	private String uly5187;
	// maxx
	private String lrx5187;
	// miny
	private String lry5187;

	private String wkt;

	/* 썸네일 */
	// 썸네일 존재 여부
	private boolean thumbnail;
	// 썸네일 파일 경로
	private String thumbnailFileCoursNm;

	/* 재난지역 */
	// 시도 코드
	private String g001Bjcd;

	// 시군구 코드
	private String g010Bjcd;

	// 읍면동 코드
	private String g011Bjcd;

	public void createPolygonWkt(String xMin, String yMin, String xMax, String yMax) {
		StringBuilder result = new StringBuilder();

		result.append("POLYGON((");
		result.append(xMin + " " + yMin);
		result.append(",");
		result.append(xMin + " " + yMax);
		result.append(",");
		result.append(xMax + " " + yMax);
		result.append(",");
		result.append(xMax + " " + yMin);
		result.append(",");
		result.append(xMin + " " + yMin);
		result.append("))");

		wkt = result.toString();
	}

}
