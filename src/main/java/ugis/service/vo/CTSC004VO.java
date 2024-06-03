package ugis.service.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Class Name : CTSC004VO.java
 * @Description : CTSC004VO Class
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2021.10.14  			 최초생성 클래스 생성 
 * @ 2021.10.19  			 변수 및 함수 추가
 *
 * @author
 * @since 2021. 10.14
 * @version 1.0
 * @see
 *
 *  Copyright (C) by XXXXX All right reserved.
 */
public class CTSC004VO implements Serializable {
	
	/**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -3932051951699769972L;
	
	/** 이력ID */
	private String aeroLogId = "";
	
	/** 조회한 사용자 ID */
	private String userNm = "";
		
	/** 영상조회 */
	private String inqireKnd = "";
	
	/** 조회시작일 */	
	private Date inqireBeginDate = null;
	
	/** 조회종료일 */
	private Date inqireEndDate = null;

	/** 다운로드여부 */
	private String dwldAt = "";

	/** 다운로드경로 */
	private String dwldPath = "";

	/** 조회일 */
	private String dwldDate = "";

	/** 영상ID */
	private Integer vidoId ;

	/** 시작일시 */
	private String bgngDt = "";

	/** 종료일시 */
	private String endDt = "";

	/** 위성번호 */
	private String satlitNo = "";

	/** 지도투영내용 */
	private String mapPrjctnCn = "";

	/** 스페로이드내용 */
	private String spherCn = "";

	/** 취득연도 */
	private String acqsYr = "";

	/** 취득월 */
	private String acqsMm = "";

	/** 위도내용 */
	private String laCn = "";

	/** 경도내용 */
	private String loCn = "";
	
	/*ROI 영역*/
	private Double ulx;
	private Double uly;
	private Double lrx;
	private Double lry;
	
	/*내부파일경로*/
	private String satlitVidoInnerPath;
	/*외부파일경로*/
	private String satlitVidoExtrlPath;

	/** 대역수 */
	private int bandCo;

	/** 투영내용 */
	private String prjctnCn = "";

	/** 데이터 */
	private String datumCn = "";

	/** 영상센서명 */
	private String vidoSensorNm = "";

	/** 촬영시작일시 */
	private String potogrfBeginDt = "";

	/** 촬영종료일시 */
	private String potogrfEndDt = "";

	/** 도번호 */
	private int trackNo;

	/** 촬영모드명 */
	private String potogrfModeNm = "";

	/** RollAngle수 */
	private String rollangCo = "";

	/** PitchAngle수 */
	private String pitchangCo = "";

	/** 생산번호 */
	private String prdctnNo = "";

	/** 원시위성영상파일명 */
	private String rawSatlitVidoFileNm = "";

	/** 위성영상파일명 */
	private String satlitVidoFileNm = "";

	/** 위성영상헤더파일명 */
	private String satlitVidoHderFileNm = "";

	/** 위성영상요약파일명 */
	private String satlitVidoSumryFileNm = "";

	/** 스테레오영상파일명 */
	private String stereoVidoFileNm = "";

	/** 등록일시 */
	private String regDt = "";

	/** 수정일시 */
	private String mdfcnDt = "";

	/** 사용제약내용 */
	private String useRstrctCn = "";


	

	/** 코스번호명 */
	private String coseNoNm = "";

	/** 사진번호명 */
	private String photoNoNm = "";

	/** 파일식별자명 */
	private String fileIdntfrNm = "";

	/** 적용대상자료명 */
	private String applcTrgetDtaNm = "";

	/** 언어명 */
	private String langNm = "";

	/** 책임기관명 */
	private String rspnsblInsttNm = "";

	/** 책임부서명 */
	private String rspnsblDeptNm = "";

	/** 책임부서전화번호 */
	private String rspnsblDeptTelno = "";

	/** 책임부서팩스번호 */
	private String rspnsblDeptFxnum = "";

	/** 책임부서주소 */
	private String rspnsblDeptAdres = "";

	/** 책임부서우편번호 */
	private String rspnsblDeptZip = "";

	/** 메타데이터작성일시 */
	private String metdataWritngDt = "";

	/** 메타데이터버전명 */
	private String metdataVerNm = "";

	/** 자료구축기관명 */
	private String dtaCnstcInsttNm = "";

	/** 입력회사명 */
	private String inputCmpnyNm = "";

	/** 공간표현방식내용 */
	private String spceExprsnMthdCn = "";

	/** 배포포맷명 */
	private String wdtbFrmatNm = "";

	/** 배포포맷버전명 */
	private String wdtbFrmatVerNm = "";

	/** 배포기관명 */
	private String wdtbInsttNm = "";

	/** 배포부서명 */
	private String wdtbDeptNm = "";

	

	/** 담당기관명 */
	private String chrgInsttNm = "";

	/** 담당자명 */
	private String picNm = "";

	/** 담당자연락처내용 */
	private String chargerCttpcCn = "";

	/** 담당자전화번호 */
	private String chargerTelno = "";

	/** 담당자주소 */
	private String chargerAdres = "";

	/** 행정구역내용 */
	private String administzoneCn = "";

	/** 우편번호 */
	private String zip = "";

	/** 국가명 */
	private String ntnNm = "";

	/** 이메일주소 */
	private String emlAddr = "";

	/** 인코딩명 */
	private String encdNm = "";

	/** 센서모델명 */
	private String sensorModelNm = "";

	
	
	/** 배포부서전화번호 */
	private String wdtbDeptTelNo = "";
	
	/** 해상도내용 */
	private String rsoltnCn = "";        
	
    /** 품질적용대상내용 */
	private String qlityApplcTrgetCn = "";   
	
    /** PitchAngle수 */
	private String pitchAngCo = "";        

    /** 매질종류내용 */
	private String mediumKndCn = "";           
	
    /** 평가기준규정내용 */
	private String evlStdrRegltnCn= "";        

    /** 행정구역명 */
	private String administZoneNm = "";  

    /** 행정구역코드 */
	private String administZoneCd = "";  

    /** 사업지구코드 */
	private String bsnsEarthCd = "";

	/** 촬영영상코드 */
	private String potogrfVidoCd ="";

	/** 영상명 */
	private String vidoNm ="";

	/** flag */
	private int flag ;

	//	위도내용(좌상단)
	private double lacnul;
	//	경도내용(좌상단)
	private double locnul;
	//	위도내용(우상단)
	private double lacnur;
	//	경도내용(우상단)
	private double locnur;
	//	위도내용(좌하단)
	private double lacnll;
	//	경도내용(좌하단)
	private double locnll;
	//	위도내용(우하단)
	private double lacnlr;
	//	경도내용(우하단)
	private double locnlr;

	private String acqsDt;
	private String vidoNmForSearch ;
	//해상도
	private double resolution;
	//운량
	private double cloud_cover;
	//영상명
	private String potogrf_vido_nm;

	public String getPotogrf_vido_nm() {
		return potogrf_vido_nm;
	}

	public void setPotogrf_vido_nm(String potogrf_vido_nm) {
		this.potogrf_vido_nm = potogrf_vido_nm;
	}

	public double getResolution() {
		return resolution;
	}

	public void setResolution(double resolution) {
		this.resolution = resolution;
	}

	public double getCloud_cover() {
		return cloud_cover;
	}

	public void setCloud_cover(double cloud_cover) {
		this.cloud_cover = cloud_cover;
	}

	public String getAcqsDt() {
		return acqsDt;
	}

	public void setAcqsDt(String acqsDt) {
		this.acqsDt = acqsDt;
	}

	public String getVidoNmForSearch() {
		return vidoNmForSearch;
	}

	public void setVidoNmForSearch(String vidoNmForSearch) {
		this.vidoNmForSearch = vidoNmForSearch;
	}

	public double getLacnul() {
		return lacnul;
	}

	public void setLacnul(double lacnul) {
		this.lacnul = lacnul;
	}

	public double getLocnul() {
		return locnul;
	}

	public void setLocnul(double locnul) {
		this.locnul = locnul;
	}

	public double getLacnur() {
		return lacnur;
	}

	public void setLacnur(double lacnur) {
		this.lacnur = lacnur;
	}

	public double getLocnur() {
		return locnur;
	}

	public void setLocnur(double locnur) {
		this.locnur = locnur;
	}

	public double getLacnll() {
		return lacnll;
	}

	public void setLacnll(double lacnll) {
		this.lacnll = lacnll;
	}

	public double getLocnll() {
		return locnll;
	}

	public void setLocnll(double locnll) {
		this.locnll = locnll;
	}

	public double getLacnlr() {
		return lacnlr;
	}

	public void setLacnlr(double lacnlr) {
		this.lacnlr = lacnlr;
	}

	public double getLocnlr() {
		return locnlr;
	}

	public void setLocnlr(double locnlr) {
		this.locnlr = locnlr;
	}

	public String getVidoNm() {return vidoNm;}

	public void setVidoNm(String vidoNm) {this.vidoNm = vidoNm;}
	public int getFlag() {return flag;}

	public void setFlag(int flag) {this.flag = flag;}

	public String getAeroLogId() {
		return aeroLogId;
	}

	public void setAeroLogId(String aeroLogId) {
		this.aeroLogId = aeroLogId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getInqireKnd() {
		return inqireKnd;
	}

	public void setInqireKnd(String inqireKnd) {
		this.inqireKnd = inqireKnd;
	}

	public Date getInqireBeginDate() {
		return inqireBeginDate;
	}

	public void setInqireBeginDate(Date inqireBeginDate) {
		this.inqireBeginDate = inqireBeginDate;
	}

	public Date getInqireEndDate() {
		return inqireEndDate;
	}

	public void setInqireEndDate(Date inqireEndDate) {
		this.inqireEndDate = inqireEndDate;
	}

	public String getDwldAt() {
		return dwldAt;
	}

	public void setDwldAt(String dwldAt) {
		this.dwldAt = dwldAt;
	}

	public String getDwldPath() {
		return dwldPath;
	}

	public void setDwldPath(String dwldPath) {
		this.dwldPath = dwldPath;
	}

	public String getDwldDate() {
		return dwldDate;
	}

	public void setDwldDate(String dwldDate) {
		this.dwldDate = dwldDate;
	}

	public Integer getVidoId() {
		return vidoId;
	}

	public void setVidoId(Integer vidoId) {
		this.vidoId = vidoId;
	}

	public String getBgngDt() {
		return bgngDt;
	}

	public void setBgngDt(String bgngDt) {
		this.bgngDt = bgngDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getSatlitNo() {
		return satlitNo;
	}

	public void setSatlitNo(String satlitNo) {
		this.satlitNo = satlitNo;
	}

	public String getMapPrjctnCn() {
		return mapPrjctnCn;
	}

	public void setMapPrjctnCn(String mapPrjctnCn) {
		this.mapPrjctnCn = mapPrjctnCn;
	}

	public String getSpherCn() {
		return spherCn;
	}

	public void setSpherCn(String spherCn) {
		this.spherCn = spherCn;
	}

	public String getAcqsYr() {
		return acqsYr;
	}

	public void setAcqsYr(String acqsYr) {
		this.acqsYr = acqsYr;
	}

	public String getAcqsMm() {
		return acqsMm;
	}

	public void setAcqsMm(String acqsMm) {
		this.acqsMm = acqsMm;
	}

	public String getLaCn() {
		return laCn;
	}

	public void setLaCn(String laCn) {
		this.laCn = laCn;
	}

	public String getLoCn() {
		return loCn;
	}

	public void setLoCn(String loCn) {
		this.loCn = loCn;
	}

	public double getUlx() {
		return ulx;
	}

	public void setUlx(Double ulx) {
		this.ulx = ulx;
	}

	public double getUly() {
		return uly;
	}

	public void setUly(Double uly) {
		this.uly = uly;
	}

	public double getLrx() {
		return lrx;
	}

	public void setLrx(Double lrx) {
		this.lrx = lrx;
	}

	public double getLry() {
		return lry;
	}

	public void setLry(Double lry) {
		this.lry = lry;
	}

	public String getSatlitVidoInnerPath() {
		return satlitVidoInnerPath;
	}

	public void setSatlitVidoInnerPath(String satlitVidoInnerPath) {
		this.satlitVidoInnerPath = satlitVidoInnerPath;
	}

	public String getSatlitVidoExtrlPath() {
		return satlitVidoExtrlPath;
	}

	public void setSatlitVidoExtrlPath(String satlitVidoExtrlPath) {
		this.satlitVidoExtrlPath = satlitVidoExtrlPath;
	}

	public int getBandCo() {
		return bandCo;
	}

	public void setBandCo(int bandCo) {
		this.bandCo = bandCo;
	}

	public String getPrjctnCn() {
		return prjctnCn;
	}

	public void setPrjctnCn(String prjctnCn) {
		this.prjctnCn = prjctnCn;
	}

	public String getDatumCn() {
		return datumCn;
	}

	public void setDatumCn(String datumCn) {
		this.datumCn = datumCn;
	}

	public String getVidoSensorNm() {
		return vidoSensorNm;
	}

	public void setVidoSensorNm(String vidoSensorNm) {
		this.vidoSensorNm = vidoSensorNm;
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

	public int getTrackNo() {
		return trackNo;
	}

	public void setTrackNo(int trackNo) {
		this.trackNo = trackNo;
	}

	public String getPotogrfModeNm() {
		return potogrfModeNm;
	}

	public void setPotogrfModeNm(String potogrfModeNm) {
		this.potogrfModeNm = potogrfModeNm;
	}

	public String getRollangCo() {
		return rollangCo;
	}

	public void setRollangCo(String rollangCo) {
		this.rollangCo = rollangCo;
	}

	public String getPitchangCo() {
		return pitchangCo;
	}

	public void setPitchangCo(String pitchangCo) {
		this.pitchangCo = pitchangCo;
	}

	public String getPrdctnNo() {
		return prdctnNo;
	}

	public void setPrdctnNo(String prdctnNo) {
		this.prdctnNo = prdctnNo;
	}

	public String getRawSatlitVidoFileNm() {
		return rawSatlitVidoFileNm;
	}

	public void setRawSatlitVidoFileNm(String rawSatlitVidoFileNm) {
		this.rawSatlitVidoFileNm = rawSatlitVidoFileNm;
	}

	public String getSatlitVidoFileNm() {
		return satlitVidoFileNm;
	}

	public void setSatlitVidoFileNm(String satlitVidoFileNm) {
		this.satlitVidoFileNm = satlitVidoFileNm;
	}

	public String getSatlitVidoHderFileNm() {
		return satlitVidoHderFileNm;
	}

	public void setSatlitVidoHderFileNm(String satlitVidoHderFileNm) {
		this.satlitVidoHderFileNm = satlitVidoHderFileNm;
	}

	public String getSatlitVidoSumryFileNm() {
		return satlitVidoSumryFileNm;
	}

	public void setSatlitVidoSumryFileNm(String satlitVidoSumryFileNm) {
		this.satlitVidoSumryFileNm = satlitVidoSumryFileNm;
	}

	public String getStereoVidoFileNm() {
		return stereoVidoFileNm;
	}

	public void setStereoVidoFileNm(String stereoVidoFileNm) {
		this.stereoVidoFileNm = stereoVidoFileNm;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getMdfcnDt() {
		return mdfcnDt;
	}

	public void setMdfcnDt(String mdfcnDt) {
		this.mdfcnDt = mdfcnDt;
	}

	public String getUseRstrctCn() {
		return useRstrctCn;
	}

	public void setUseRstrctCn(String useRstrctCn) {
		this.useRstrctCn = useRstrctCn;
	}

	public String getCoseNoNm() {
		return coseNoNm;
	}

	public void setCoseNoNm(String coseNoNm) {
		this.coseNoNm = coseNoNm;
	}

	public String getPhotoNoNm() {
		return photoNoNm;
	}

	public void setPhotoNoNm(String photoNoNm) {
		this.photoNoNm = photoNoNm;
	}

	public String getFileIdntfrNm() {
		return fileIdntfrNm;
	}

	public void setFileIdntfrNm(String fileIdntfrNm) {
		this.fileIdntfrNm = fileIdntfrNm;
	}

	public String getApplcTrgetDtaNm() {
		return applcTrgetDtaNm;
	}

	public void setApplcTrgetDtaNm(String applcTrgetDtaNm) {
		this.applcTrgetDtaNm = applcTrgetDtaNm;
	}

	public String getLangNm() {
		return langNm;
	}

	public void setLangNm(String langNm) {
		this.langNm = langNm;
	}

	public String getRspnsblInsttNm() {
		return rspnsblInsttNm;
	}

	public void setRspnsblInsttNm(String rspnsblInsttNm) {
		this.rspnsblInsttNm = rspnsblInsttNm;
	}

	public String getRspnsblDeptNm() {
		return rspnsblDeptNm;
	}

	public void setRspnsblDeptNm(String rspnsblDeptNm) {
		this.rspnsblDeptNm = rspnsblDeptNm;
	}

	public String getRspnsblDeptTelno() {
		return rspnsblDeptTelno;
	}

	public void setRspnsblDeptTelno(String rspnsblDeptTelno) {
		this.rspnsblDeptTelno = rspnsblDeptTelno;
	}

	public String getRspnsblDeptFxnum() {
		return rspnsblDeptFxnum;
	}

	public void setRspnsblDeptFxnum(String rspnsblDeptFxnum) {
		this.rspnsblDeptFxnum = rspnsblDeptFxnum;
	}

	public String getRspnsblDeptAdres() {
		return rspnsblDeptAdres;
	}

	public void setRspnsblDeptAdres(String rspnsblDeptAdres) {
		this.rspnsblDeptAdres = rspnsblDeptAdres;
	}

	public String getRspnsblDeptZip() {
		return rspnsblDeptZip;
	}

	public void setRspnsblDeptZip(String rspnsblDeptZip) {
		this.rspnsblDeptZip = rspnsblDeptZip;
	}

	public String getMetdataWritngDt() {
		return metdataWritngDt;
	}

	public void setMetdataWritngDt(String metdataWritngDt) {
		this.metdataWritngDt = metdataWritngDt;
	}

	public String getMetdataVerNm() {
		return metdataVerNm;
	}

	public void setMetdataVerNm(String metdataVerNm) {
		this.metdataVerNm = metdataVerNm;
	}

	public String getDtaCnstcInsttNm() {
		return dtaCnstcInsttNm;
	}

	public void setDtaCnstcInsttNm(String dtaCnstcInsttNm) {
		this.dtaCnstcInsttNm = dtaCnstcInsttNm;
	}

	public String getInputCmpnyNm() {
		return inputCmpnyNm;
	}

	public void setInputCmpnyNm(String inputCmpnyNm) {
		this.inputCmpnyNm = inputCmpnyNm;
	}

	public String getSpceExprsnMthdCn() {
		return spceExprsnMthdCn;
	}

	public void setSpceExprsnMthdCn(String spceExprsnMthdCn) {
		this.spceExprsnMthdCn = spceExprsnMthdCn;
	}

	public String getWdtbFrmatNm() {
		return wdtbFrmatNm;
	}

	public void setWdtbFrmatNm(String wdtbFrmatNm) {
		this.wdtbFrmatNm = wdtbFrmatNm;
	}

	public String getWdtbFrmatVerNm() {
		return wdtbFrmatVerNm;
	}

	public void setWdtbFrmatVerNm(String wdtbFrmatVerNm) {
		this.wdtbFrmatVerNm = wdtbFrmatVerNm;
	}

	public String getWdtbInsttNm() {
		return wdtbInsttNm;
	}

	public void setWdtbInsttNm(String wdtbInsttNm) {
		this.wdtbInsttNm = wdtbInsttNm;
	}

	public String getWdtbDeptNm() {
		return wdtbDeptNm;
	}

	public void setWdtbDeptNm(String wdtbDeptNm) {
		this.wdtbDeptNm = wdtbDeptNm;
	}

	public String getChrgInsttNm() {
		return chrgInsttNm;
	}

	public void setChrgInsttNm(String chrgInsttNm) {
		this.chrgInsttNm = chrgInsttNm;
	}

	public String getPicNm() {
		return picNm;
	}

	public void setPicNm(String picNm) {
		this.picNm = picNm;
	}

	public String getChargerCttpcCn() {
		return chargerCttpcCn;
	}

	public void setChargerCttpcCn(String chargerCttpcCn) {
		this.chargerCttpcCn = chargerCttpcCn;
	}

	public String getChargerTelno() {
		return chargerTelno;
	}

	public void setChargerTelno(String chargerTelno) {
		this.chargerTelno = chargerTelno;
	}

	public String getChargerAdres() {
		return chargerAdres;
	}

	public void setChargerAdres(String chargerAdres) {
		this.chargerAdres = chargerAdres;
	}

	public String getAdministzoneCn() {
		return administzoneCn;
	}

	public void setAdministzoneCn(String administzoneCn) {
		this.administzoneCn = administzoneCn;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getNtnNm() {
		return ntnNm;
	}

	public void setNtnNm(String ntnNm) {
		this.ntnNm = ntnNm;
	}

	public String getEmlAddr() {
		return emlAddr;
	}

	public void setEmlAddr(String emlAddr) {
		this.emlAddr = emlAddr;
	}

	public String getEncdNm() {
		return encdNm;
	}

	public void setEncdNm(String encdNm) {
		this.encdNm = encdNm;
	}

	public String getSensorModelNm() {
		return sensorModelNm;
	}

	public void setSensorModelNm(String sensorModelNm) {
		this.sensorModelNm = sensorModelNm;
	}

	public String getWdtbDeptTelNo() {
		return wdtbDeptTelNo;
	}

	public void setWdtbDeptTelNo(String wdtbDeptTelNo) {
		this.wdtbDeptTelNo = wdtbDeptTelNo;
	}

	public String getRsoltnCn() {
		return rsoltnCn;
	}

	public void setRsoltnCn(String rsoltnCn) {
		this.rsoltnCn = rsoltnCn;
	}

	public String getQlityApplcTrgetCn() {
		return qlityApplcTrgetCn;
	}

	public void setQlityApplcTrgetCn(String qlityApplcTrgetCn) {
		this.qlityApplcTrgetCn = qlityApplcTrgetCn;
	}

	public String getPitchAngCo() {
		return pitchAngCo;
	}

	public void setPitchAngCo(String pitchAngCo) {
		this.pitchAngCo = pitchAngCo;
	}

	public String getMediumKndCn() {
		return mediumKndCn;
	}

	public void setMediumKndCn(String mediumKndCn) {
		this.mediumKndCn = mediumKndCn;
	}

	public String getEvlStdrRegltnCn() {
		return evlStdrRegltnCn;
	}

	public void setEvlStdrRegltnCn(String evlStdrRegltnCn) {
		this.evlStdrRegltnCn = evlStdrRegltnCn;
	}

	public String getAdministZoneNm() {
		return administZoneNm;
	}

	public void setAdministZoneNm(String administZoneNm) {
		this.administZoneNm = administZoneNm;
	}

	public String getAdministZoneCd() {
		return administZoneCd;
	}

	public void setAdministZoneCd(String administZoneCd) {
		this.administZoneCd = administZoneCd;
	}

	public String getBsnsEarthCd() {
		return bsnsEarthCd;
	}

	public void setBsnsEarthCd(String bsnsEarthCd) {
		this.bsnsEarthCd = bsnsEarthCd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPotogrfVidoCd() {return potogrfVidoCd;}

	public void setPotogrfVidoCd(String potogrfVidoCd) {this.potogrfVidoCd = potogrfVidoCd;}
	@Override
	public String toString() {
		return "CTSC004VO [aeroLogId=" + aeroLogId + ", userNm=" + userNm + ", inqireKnd=" + inqireKnd
				+ ", inqireBeginDate=" + inqireBeginDate + ", inqireEndDate=" + inqireEndDate + ", dwldAt=" + dwldAt
				+ ", dwldPath=" + dwldPath + ", dwldDate=" + dwldDate + ", vidoId=" + vidoId + ", bgngDt=" + bgngDt
				+ ", endDt=" + endDt + ", satlitNo=" + satlitNo + ", mapPrjctnCn=" + mapPrjctnCn + ", spherCn="
				+ spherCn + ", acqsYr=" + acqsYr + ", acqsMm=" + acqsMm + ", laCn=" + laCn + ", loCn=" + loCn + ", ulx="
				+ ulx + ", uly=" + uly + ", lrx=" + lrx + ", lry=" + lry + ", satlitVidoInnerPath="
				+ satlitVidoInnerPath + ", satlitVidoExtrlPath=" + satlitVidoExtrlPath + ", bandCo=" + bandCo
				+ ", prjctnCn=" + prjctnCn + ", datumCn=" + datumCn + ", vidoSensorNm=" + vidoSensorNm
				+ ", potogrfBeginDt=" + potogrfBeginDt + ", potogrfEndDt=" + potogrfEndDt + ", trackNo=" + trackNo
				+ ", potogrfModeNm=" + potogrfModeNm + ", rollangCo=" + rollangCo + ", pitchangCo=" + pitchangCo
				+ ", prdctnNo=" + prdctnNo + ", rawSatlitVidoFileNm=" + rawSatlitVidoFileNm + ", satlitVidoFileNm="
				+ satlitVidoFileNm + ", satlitVidoHderFileNm=" + satlitVidoHderFileNm + ", satlitVidoSumryFileNm="
				+ satlitVidoSumryFileNm + ", stereoVidoFileNm=" + stereoVidoFileNm + ", regDt=" + regDt + ", mdfcnDt="
				+ mdfcnDt + ", useRstrctCn=" + useRstrctCn + ", coseNoNm=" + coseNoNm + ", photoNoNm=" + photoNoNm
				+ ", fileIdntfrNm=" + fileIdntfrNm + ", applcTrgetDtaNm=" + applcTrgetDtaNm + ", langNm=" + langNm
				+ ", rspnsblInsttNm=" + rspnsblInsttNm + ", rspnsblDeptNm=" + rspnsblDeptNm + ", rspnsblDeptTelno="
				+ rspnsblDeptTelno + ", rspnsblDeptFxnum=" + rspnsblDeptFxnum + ", rspnsblDeptAdres=" + rspnsblDeptAdres
				+ ", rspnsblDeptZip=" + rspnsblDeptZip + ", metdataWritngDt=" + metdataWritngDt + ", metdataVerNm="
				+ metdataVerNm + ", dtaCnstcInsttNm=" + dtaCnstcInsttNm + ", inputCmpnyNm=" + inputCmpnyNm
				+ ", spceExprsnMthdCn=" + spceExprsnMthdCn + ", wdtbFrmatNm=" + wdtbFrmatNm + ", wdtbFrmatVerNm="
				+ wdtbFrmatVerNm + ", wdtbInsttNm=" + wdtbInsttNm + ", wdtbDeptNm=" + wdtbDeptNm + ", chrgInsttNm="
				+ chrgInsttNm + ", picNm=" + picNm + ", chargerCttpcCn=" + chargerCttpcCn + ", chargerTelno="
				+ chargerTelno + ", chargerAdres=" + chargerAdres + ", administzoneCn=" + administzoneCn + ", zip="
				+ zip + ", ntnNm=" + ntnNm + ", emlAddr=" + emlAddr + ", encdNm=" + encdNm + ", sensorModelNm="
				+ sensorModelNm + ", wdtbDeptTelNo=" + wdtbDeptTelNo + ", rsoltnCn=" + rsoltnCn + ", qlityApplcTrgetCn="
				+ qlityApplcTrgetCn + ", pitchAngCo=" + pitchAngCo + ", mediumKndCn=" + mediumKndCn
				+ ", evlStdrRegltnCn=" + evlStdrRegltnCn + ", administZoneNm=" + administZoneNm + ", administZoneCd="
				+ administZoneCd + ", bsnsEarthCd=" + bsnsEarthCd + "]";
	}

	public  CTSC004VO() {
		this.ulx = 0.0;
		this.uly = 0.0;
		this.lrx = 0.0;
		this.lry = 0.0;

	}
}
