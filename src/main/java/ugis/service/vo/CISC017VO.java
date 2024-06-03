package ugis.service.vo;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CISC017VO {

	@Getter
	@Setter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ChangeDetctRsltListRes {
		private Long chngeDetctResultId;
		private Long chngeDetctVidoResultId;
		private String progrsSttusCd;
		private String streYn;
		private Long vidoId;
		private String vidoNm;
		private String innerFileCoursNm;
		private String extrlFileCoursNm;
		private String fileNm;
		private String allFileNm;
		private String fileCoursNm;
		private BigDecimal fileSize;
		private String fileExtsnNm;
		private String thumbVidoNm;
		private String thumbVidoCoursNm;
		private String vectorFileNm;
		private String vectorFileCoursNm;
		private String resultVidoYn;
		private BigDecimal ltopCrdntX;
		private BigDecimal ltopCrdntY;
		private BigDecimal rbtmCrdntX;
		private BigDecimal rbtmCrdntY;
		private String mapPrjctnCn;

		List<ChangeDetctRsltListRes> groupList;
		List<ChangeDetctRsltListRes> resultList;

		@Builder
		public ChangeDetctRsltListRes(List<ChangeDetctRsltListRes> groupList, List<ChangeDetctRsltListRes> resultList) {
			this.groupList = groupList;
			this.resultList = resultList;
		}

		public void addGroupList(List<ChangeDetctRsltListRes> groupList) {
			this.groupList.addAll(this.groupList.size(), groupList);
		}

		public void addResultList(List<ChangeDetctRsltListRes> resultList) {
			this.resultList.addAll(this.resultList.size(), resultList);
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ThChngeDetct {
		private Long chngeDetctResultId;
		private String chngeDetctAlgrthCd;
		private Long stdrVidoId;
		private BigDecimal cvaPt;
		private String progrsSttusCd;
		private String streYn;
		private List<ThChngeDetctVidoResult> vidoList;

		@Builder
		public ThChngeDetct(Long chngeDetctResultId, String chngeDetctAlgrthCd, Long stdrVidoId, BigDecimal cvaPt,
				String progrsSttusCd, String streYn, List<ThChngeDetctVidoResult> vidoList) {
			this.chngeDetctResultId = chngeDetctResultId;
			this.chngeDetctAlgrthCd = chngeDetctAlgrthCd;
			this.stdrVidoId = stdrVidoId;
			this.cvaPt = cvaPt;
			this.progrsSttusCd = progrsSttusCd;
			this.streYn = streYn;
			this.vidoList = vidoList;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ThChngeDetctVidoResult {
		private Long chngeDetctResultId;
		private Long chngeDetctVidoResultId;
		private Long vidoId;
		private String vidoNm;
		private String fileNm;
		private String allFileNm;
		private String fileExtsnNm;
		private BigDecimal fileSize;
		private String fileCoursNm;
		private String innerFileCoursNm;
		private String extrlFileCoursNm;
		private String thumbVidoNm;
		private String thumbVidoCoursNm;
		private String vectorFileNm;
		private String vectorFileCoursNm;
		private String resultVidoYn;
		private Integer upsamplingRate;
		private List<ThChngeDetctVidoResult> vectorDeleteList;

		@Builder
		public ThChngeDetctVidoResult(Long chngeDetctResultId, Long chngeDetctVidoResultId, Long vidoId, String vidoNm,
				String fileNm, String allFileNm, String fileExtsnNm, BigDecimal fileSize, String fileCoursNm,
				String innerFileCoursNm, String extrlFileCoursNm, String thumbVidoNm, String thumbVidoCoursNm,
				String vectorFileNm, String vectorFileCoursNm, String resultVidoYn, Integer upsamplingRate,
				List<ThChngeDetctVidoResult> vectorDeleteList) {
			this.chngeDetctResultId = chngeDetctResultId;
			this.chngeDetctVidoResultId = chngeDetctVidoResultId;
			this.vidoId = vidoId;
			this.vidoNm = vidoNm;
			this.fileNm = fileNm;
			this.allFileNm = allFileNm;
			this.fileExtsnNm = fileExtsnNm;
			this.fileSize = fileSize;
			this.fileCoursNm = fileCoursNm;
			this.innerFileCoursNm = innerFileCoursNm;
			this.extrlFileCoursNm = extrlFileCoursNm;
			this.thumbVidoNm = thumbVidoNm;
			this.thumbVidoCoursNm = thumbVidoCoursNm;
			this.vectorFileNm = vectorFileNm;
			this.vectorFileCoursNm = vectorFileCoursNm;
			this.resultVidoYn = resultVidoYn;
			this.vectorDeleteList = vectorDeleteList;
		}
	}

}
