package ugis.service.vo;

/**
 * @Class Name : CISC005VO.java
 * @Description : 절대/상대 방사보정
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
public class CISC004VO {

	private String vidoId;
	private String potogrfBeginDt;
	private String potogrfEndDt;
	private String ltopCrdntX;
	private String ltopCrdntY;
	private String rbtmCrdntX;
	private String rbtmCrdntY;
	private String innerFileCoursNm;
	private String vidoNm;
	private String outputpath;
	private String algorithm;
	private String disasterId;

	public String getOutputpath() {
		return outputpath;
	}

	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}

	public String getVidoId() {
		return vidoId;
	}

	public void setVidoId(String vidoId) {
		this.vidoId = vidoId;
	}

	public String getPotogrfBeginDt() {
		return potogrfBeginDt;
	}

	public void setPotogrfBeginDt(String potogrfBeginDt) {
		this.potogrfBeginDt = potogrfBeginDt;
	}

	public String getPotogrfEndDt() {
		return potogrfEndDt;
	}

	public void setPotogrfEndDt(String potogrfEndDt) {
		this.potogrfEndDt = potogrfEndDt;
	}

	public String getLtopCrdntX() {
		return ltopCrdntX;
	}

	public void setLtopCrdntX(String ltopCrdntX) {
		this.ltopCrdntX = ltopCrdntX;
	}

	public String getLtopCrdntY() {
		return ltopCrdntY;
	}

	public void setLtopCrdntY(String ltopCrdntY) {
		this.ltopCrdntY = ltopCrdntY;
	}

	public String getRbtmCrdntX() {
		return rbtmCrdntX;
	}

	public void setRbtmCrdntX(String rbtmCrdntX) {
		this.rbtmCrdntX = rbtmCrdntX;
	}

	public String getRbtmCrdntY() {
		return rbtmCrdntY;
	}

	public void setRbtmCrdntY(String rbtmCrdntY) {
		this.rbtmCrdntY = rbtmCrdntY;
	}

	public String getInnerFileCoursNm() {
		return innerFileCoursNm;
	}

	public void setInnerFileCoursNm(String innerFileCoursNm) {
		this.innerFileCoursNm = innerFileCoursNm;
	}

	public String getVidoNm() {
		return vidoNm;
	}

	public void setVidoNm(String vidoNm) {
		this.vidoNm = vidoNm;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getDisasterId() {
		return disasterId;
	}

	public void setDisasterId(String disasterId) {
		this.disasterId = disasterId;
	}

}
