package ugis.service.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.json.simple.JSONObject;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Class Name : CMSC004VO.java
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
@NoArgsConstructor
public class CMSC003DataSetVO {

	private BigDecimal datasetId;
	private String datasetNm;
	private double ltopCrdntX;
	private double ltopCrdntY;
	private double rtopCrdntX;
	private double rtopCrdntY;
	private double lbtmCrdntX;
	private double lbtmCrdntY;
	private double rbtmCrdntX;
	private double rbtmCrdntY;
	private String mapPrjctnCn;
	private String msfrtnTyNm;
	private String datasetCoursNm;
	private String roiYn;
	private Date uploadDt;
	private String msfrtnId;
	private String dataCd;
	private String addr;
	private String fileNm;
	private String msfrtnTyCd;
	private String fileTy;
	private String fileKorNm;

	private String year;
	private String dpi;

	private String fullFileCoursNm;
	private String potogrfBeginDt;
	private String potogrfEndDt;
	private String workKind;

	private String satDir;
	private String mapNm;

	public JSONObject toJSON() {

		JSONObject obj = new JSONObject();
		obj.put("datasetId", this.datasetId);
		obj.put("datasetNm", this.datasetNm);
		obj.put("ltopCrdntX", this.ltopCrdntX);
		obj.put("ltopCrdntY", this.ltopCrdntY);
		obj.put("rtopCrdntX", this.rtopCrdntX);
		obj.put("rtopCrdntY", this.rtopCrdntY);
		obj.put("lbtmCrdntX", this.lbtmCrdntX);
		obj.put("lbtmCrdntY", this.lbtmCrdntY);
		obj.put("rbtmCrdntX", this.rbtmCrdntX);
		obj.put("rbtmCrdntY", this.rbtmCrdntY);
		obj.put("mapPrjctnCn", this.mapPrjctnCn);
		obj.put("msfrtnTyNm", this.msfrtnTyNm);
		obj.put("datasetCoursNm", this.datasetCoursNm);
		obj.put("msfrtnTyNm", this.msfrtnTyNm);
		obj.put("roiYn", this.roiYn);
		obj.put("uploadDt", this.uploadDt);
		obj.put("dataCd", this.dataCd);
		obj.put("addr", this.addr);
		obj.put("fileNm", this.fileNm);
		obj.put("msfrtnTyCd", this.msfrtnTyCd);
		obj.put("fileTy", this.fileTy);
		obj.put("fileKorNm", this.fileKorNm);

		obj.put("year", this.year);

		return obj;
	}
}
