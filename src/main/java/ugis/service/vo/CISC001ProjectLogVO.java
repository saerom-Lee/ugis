package ugis.service.vo;

/**
 * @Class Name : CISC001ProjectLogVO.java
 * @Description : 프로젝트 로그 관리
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
public class CISC001ProjectLogVO {
	private String projectLogId;
	private String projectId;
	private String workKind;	
	private String inputFileNm;   //입력, 기준영상
	private String metaDataNm;    //메타데이터
	private String outputFileNm;  //결과영상
	private String targetFileNm;  //대상 영상	
	private String algorithmNm;   //알고리즘
	private String redBand;       //RED BAND
	private String greenBand;     //GREEN BAND
	private String blueBand;      //BLUE BAND
	private String controlType;   //조정방식
	private String autoAreaControl;  //자동영역 설정방법
	private String radiatingFormula;
	private String toaOutputFileNm;  //toa 결과영상
	private String toaFormula;
	
	private String regDt;	
	private String useYn;
	public String getProjectLogId() {
		return projectLogId;
	}
	public void setProjectLogId(String projectLogId) {
		this.projectLogId = projectLogId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getWorkKind() {
		return workKind;
	}
	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}
	public String getInputFileNm() {
		return inputFileNm;
	}
	public void setInputFileNm(String inputFileNm) {
		this.inputFileNm = inputFileNm;
	}
	public String getMetaDataNm() {
		return metaDataNm;
	}
	public void setMetaDataNm(String metaDataNm) {
		this.metaDataNm = metaDataNm;
	}
	public String getOutputFileNm() {
		return outputFileNm;
	}
	public void setOutputFileNm(String outputFileNm) {
		this.outputFileNm = outputFileNm;
	}
	public String getTargetFileNm() {
		return targetFileNm;
	}
	public void setTargetFileNm(String targetFileNm) {
		this.targetFileNm = targetFileNm;
	}
	public String getAlgorithmNm() {
		return algorithmNm;
	}
	public void setAlgorithmNm(String algorithmNm) {
		this.algorithmNm = algorithmNm;
	}
	
	public String getRedBand() {
		return redBand;
	}
	public void setRedBand(String redBand) {
		this.redBand = redBand;
	}
	public String getGreenBand() {
		return greenBand;
	}
	public void setGreenBand(String greenBand) {
		this.greenBand = greenBand;
	}
	public String getBlueBand() {
		return blueBand;
	}
	public void setBlueBand(String blueBand) {
		this.blueBand = blueBand;
	}	
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getAutoAreaControl() {
		return autoAreaControl;
	}
	public void setAutoAreaControl(String autoAreaControl) {
		this.autoAreaControl = autoAreaControl;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getRadiatingFormula() {
		return radiatingFormula;
	}
	public void setRadiatingFormula(String radiatingFormula) {
		this.radiatingFormula = radiatingFormula;
	}	
	public String getToaOutputFileNm() {
		return toaOutputFileNm;
	}
	public void setToaOutputFileNm(String toaOutputFileNm) {
		this.toaOutputFileNm = toaOutputFileNm;
	}
	public String getToaFormula() {
		return toaFormula;
	}
	public void setToaFormula(String toaFormula) {
		this.toaFormula = toaFormula;
	}
	
	
	
	
}
