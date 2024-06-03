package ugis.service.vo;

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
public class CMSC003MetaVO {

	// 재난정보 id
	private Integer msfrtnInfoColctRequstId;

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

	public CMSC003MetaVO() {
		super();
	}

	public Integer getMsfrtnInfoColctRequstId() {
		return msfrtnInfoColctRequstId;
	}

	public void setMsfrtnInfoColctRequstId(Integer msfrtnInfoColctRequstId) {
		this.msfrtnInfoColctRequstId = msfrtnInfoColctRequstId;
	}

	public String getMsfrtnTyCd() {
		return msfrtnTyCd;
	}

	public void setMsfrtnTyCd(String msfrtnTyCd) {
		this.msfrtnTyCd = msfrtnTyCd;
	}

	public String getMsfrtnAreaSrchwrd() {
		return msfrtnAreaSrchwrd;
	}

	public void setMsfrtnAreaSrchwrd(String msfrtnAreaSrchwrd) {
		this.msfrtnAreaSrchwrd = msfrtnAreaSrchwrd;
	}

	public String getCtprvnNm() {
		return ctprvnNm;
	}

	public void setCtprvnNm(String ctprvnNm) {
		this.ctprvnNm = ctprvnNm;
	}

	public String getSsgNm() {
		return ssgNm;
	}

	public void setSsgNm(String ssgNm) {
		this.ssgNm = ssgNm;
	}

	public String getEmdNm() {
		return emdNm;
	}

	public void setEmdNm(String emdNm) {
		this.emdNm = emdNm;
	}

	public String getLnbrAddr() {
		return lnbrAddr;
	}

	public void setLnbrAddr(String lnbrAddr) {
		this.lnbrAddr = lnbrAddr;
	}

	public Double getSpceCrdntX() {
		return spceCrdntX;
	}

	public void setSpceCrdntX(Double spceCrdntX) {
		this.spceCrdntX = spceCrdntX;
	}

	public Double getSpceCrdntY() {
		return spceCrdntY;
	}

	public void setSpceCrdntY(Double spceCrdntY) {
		this.spceCrdntY = spceCrdntY;
	}

	public String getColctBeginDe() {
		return colctBeginDe;
	}

	public void setColctBeginDe(String colctBeginDe) {
		this.colctBeginDe = colctBeginDe;
	}

	public String getColctEndDe() {
		return colctEndDe;
	}

	public void setColctEndDe(String colctEndDe) {
		this.colctEndDe = colctEndDe;
	}

	public String getColctSttusCd() {
		return colctSttusCd;
	}

	public void setColctSttusCd(String colctSttusCd) {
		this.colctSttusCd = colctSttusCd;
	}

	public String getMsfrtnTySrchwrd() {
		return msfrtnTySrchwrd;
	}

	public void setMsfrtnTySrchwrd(String msfrtnTySrchwrd) {
		this.msfrtnTySrchwrd = msfrtnTySrchwrd;
	}

	public double getLtopCrdntX() {
		return ltopCrdntX;
	}

	public void setLtopCrdntX(double ltopCrdntX) {
		this.ltopCrdntX = ltopCrdntX;
	}

	public double getLtopCrdntY() {
		return ltopCrdntY;
	}

	public void setLtopCrdntY(double ltopCrdntY) {
		this.ltopCrdntY = ltopCrdntY;
	}

	public double getRtopCrdntX() {
		return rtopCrdntX;
	}

	public void setRtopCrdntX(double rtopCrdntX) {
		this.rtopCrdntX = rtopCrdntX;
	}

	public double getRtopCrdntY() {
		return rtopCrdntY;
	}

	public void setRtopCrdntY(double rtopCrdntY) {
		this.rtopCrdntY = rtopCrdntY;
	}

	public double getLbtmCrdntX() {
		return lbtmCrdntX;
	}

	public void setLbtmCrdntX(double lbtmCrdntX) {
		this.lbtmCrdntX = lbtmCrdntX;
	}

	public double getLbtmCrdntY() {
		return lbtmCrdntY;
	}

	public void setLbtmCrdntY(double lbtmCrdntY) {
		this.lbtmCrdntY = lbtmCrdntY;
	}

	public double getRbtmCrdntX() {
		return rbtmCrdntX;
	}

	public void setRbtmCrdntX(double rbtmCrdntX) {
		this.rbtmCrdntX = rbtmCrdntX;
	}

	public double getRbtmCrdntY() {
		return rbtmCrdntY;
	}

	public void setRbtmCrdntY(double rbtmCrdntY) {
		this.rbtmCrdntY = rbtmCrdntY;
	}

	public String getMapPrjctnCn() {
		return mapPrjctnCn;
	}

	public void setMapPrjctnCn(String mapPrjctnCn) {
		this.mapPrjctnCn = mapPrjctnCn;
	}

}
