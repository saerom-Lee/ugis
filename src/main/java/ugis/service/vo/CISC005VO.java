package ugis.service.vo;

/**
 * @Class Name : CISC005VO.java
 * @Description : 절대/상대 방사보정
 * @Modification Information
 * @
 * @  수정일 / 수정자 / 수정내용
 * @ -------------------------------------------------
 * @ 2021.11.xx / psh / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
public class CISC005VO {

	private String vidoId;
	private String potogrfBeginDt;
	private String potogrfEndDt;	
	private String ltopCrdntX;
	private String ltopCrdntY;
	private String rbtmCrdntX;
	private String rbtmCrdntY;	
	private String innerFileCoursNm;
	private String potogrfVidoCd;	
	private String fileName;
	private String tifFileName;
	private String imgFullFileName;
	private String satName;
	private String gain = "Gain";
	private String offset = "Offset";
	private String radianceMult = "RADIANCE_MULT_BAND";
	private String radianceAdd = "RADIANCE_ADD_BAND";
	private String reflectanceMult = "REFLECTANCE_MULT_BAND";
	private String reflectanceAdd = "REFLECTANCE_ADD_BAND";
	private String metaData;	
	private String dirName;
	private String mapPrjctnCn;
	
	
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
	public String getPotogrfVidoCd() {
		return potogrfVidoCd;
	}
	public void setPotogrfVidoCd(String potogrfVidoCd) {
		this.potogrfVidoCd = potogrfVidoCd;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSatName() {
		return satName;
	}
	public void setSatName(String satName) {
		this.satName = satName;
	}
	
	public String getImgFullFileName() {
		return imgFullFileName;
	}
	public void setImgFullFileName(String imgFullFileName) {
		this.imgFullFileName = imgFullFileName;
	}
	public String getGain() {
		return gain;
	}
	public void setGain(String gain) {
		this.gain = gain;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getTifFileName() {
		return tifFileName;
	}
	public void setTifFileName(String tifFileName) {
		this.tifFileName = tifFileName;
	}
	public String getRadianceMult() {
		return radianceMult;
	}
	public void setRadianceMult(String radianceMult) {
		this.radianceMult = radianceMult;
	}
	public String getRadianceAdd() {
		return radianceAdd;
	}
	public void setRadianceAdd(String radianceAdd) {
		this.radianceAdd = radianceAdd;
	}
	public String getReflectanceMult() {
		return reflectanceMult;
	}
	public void setReflectanceMult(String reflectanceMult) {
		this.reflectanceMult = reflectanceMult;
	}
	public String getReflectanceAdd() {
		return reflectanceAdd;
	}
	public void setReflectanceAdd(String reflectanceAdd) {
		this.reflectanceAdd = reflectanceAdd;
	}
	public String getMetaData() {
		return metaData;
	}
	public void setMetaData(String metaData) {
		this.metaData = metaData;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public String getMapPrjctnCn() {
		return mapPrjctnCn;
	}
	public void setMapPrjctnCn(String mapPrjctnCn) {
		this.mapPrjctnCn = mapPrjctnCn;
	}
	
	
	
	
	
}
