package ugis.service.vo;

import java.util.Date;

import lombok.Data;

/**
 * @Class Name : CISC001ProjectVO.java
 * @Description : 상대대기,절대보정,상대보정,컬러합성,히스토그램조정,모자이크 처리내역
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.11.xx / psh / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
@Data
public class CISC001WorkResultVO {
	private Integer usgsWorkId;
	private Integer vidoId;
	private Date potogrfBeginDt;
	private Date potogrfEndDt;
	private double ltopCrdntX;
	private double ltopCrdntY;
	private double rtopCrdntX;
	private double rtopCrdntY;
	private double lbtmCrdntX;
	private double lbtmCrdntY;
	private double rbtmCrdntX;
	private double rbtmCrdntY;
	private String innerFileCoursNm;
	private String extrlFileCoursNm;
	private String outFileCoursNm;
	private String dwldDt;
	private String potogrfVidoCd;
	private String flag;
	private String vidoNm;
	private String workKind;
	private String regDt;
	private String disasterId;

}
