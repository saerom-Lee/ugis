package ugis.service.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CISC015VO {

	@Getter
	public static class ObjChangeSearch {
		private String[] searchPotogrfVidoCd;
		private LocalDate searchPotogrfBeginDt;
		private LocalDate searchPotogrfEndDt;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class ObjChangeListRes {
		private Long vidoId;
		private String vidoNm;
		private String innerFileCoursNm;
		private LocalDate potogrfBeginDt;
		private LocalDate potogrfEndDt;
		private BigDecimal ltopCrdntX;
		private BigDecimal ltopCrdntY;
		private BigDecimal rbtmCrdntX;
		private BigDecimal rbtmCrdntY;
		private String fileNm;
		private String potogrfVidoCd;
		private String mapPrjctnCn;
		private String message;
	}

	@Getter
	@Setter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ObjChangeRsltListRes {
		private Long modelId;
		private Long aiModelId;
		private Long modelAnalsId;
		private Long analsVidoId;
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
		private BigDecimal ltopCrdntX;
		private BigDecimal ltopCrdntY;
		private BigDecimal rbtmCrdntX;
		private BigDecimal rbtmCrdntY;
		private String mapPrjctnCn;
		List<ObjChangeRsltListRes> groupList;
		List<ObjChangeRsltListRes> resultList;

		@Builder
		public ObjChangeRsltListRes(List<ObjChangeRsltListRes> groupList, List<ObjChangeRsltListRes> resultList) {
			this.groupList = groupList;
			this.resultList = resultList;
		}

		public void addGroupList(List<ObjChangeRsltListRes> groupList) {
			this.groupList.addAll(this.groupList.size(), groupList);
		}

		public void addResultList(List<ObjChangeRsltListRes> resultList) {
			this.resultList.addAll(this.resultList.size(), resultList);
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiModelAnals {
		private Long modelId;
		private Long aiModelId;
		private Long modelAnalsId;
		private String modelAnalsNm;
		private String progrsSttusCd;
		private String streYn;
		private List<TnAiModelAnalsVidoMapngResult> vidoList;

		@Builder
		public TnAiModelAnals(Long modelId, Long aiModelId, Long modelAnalsId, String modelAnalsNm,
				String progrsSttusCd, String streYn, List<TnAiModelAnalsVidoMapngResult> vidoList) {
			this.modelId = modelId;
			this.aiModelId = aiModelId;
			this.modelAnalsId = modelAnalsId;
			this.modelAnalsNm = modelAnalsNm;
			this.progrsSttusCd = progrsSttusCd;
			this.streYn = streYn;
			this.vidoList = vidoList;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiModelAnalsVidoMapngResult {
		private Long modelId;
		private Long aiModelId;
		private Long modelAnalsId;
		private Long analsVidoId;
		private Long vidoId;
		private String vidoNm;
		private String innerFileCoursNm;
		private String fileNm;
		private String allFileNm;
		private String fileCoursNm;
		private BigDecimal fileSize;
		private String fileExtsnNm;
		private String thumbVidoNm;
		private String thumbVidoCoursNm;
		private String extrlFileCoursNm;
		private String vectorFileNm;
		private String vectorFileCoursNm;
		private Integer upsamplingRate;
		private List<TnAiModelAnalsVidoMapngResult> vectorDeleteList;

		@Builder
		public TnAiModelAnalsVidoMapngResult(Long modelId, Long aiModelId, Long modelAnalsId, Long analsVidoId,
				Long vidoId, String vidoNm, String innerFileCoursNm, String fileNm, String allFileNm,
				String fileCoursNm, BigDecimal fileSize, String fileExtsnNm, String thumbVidoNm,
				String thumbVidoCoursNm, String extrlFileCoursNm, String vectorFileNm, String vectorFileCoursNm, Integer upsamplingRate) {
			this.modelId = modelId;
			this.aiModelId = aiModelId;
			this.modelAnalsId = modelAnalsId;
			this.analsVidoId = analsVidoId;
			this.vidoId = vidoId;
			this.vidoNm = vidoNm;
			this.innerFileCoursNm = innerFileCoursNm;
			this.fileNm = fileNm;
			this.allFileNm = allFileNm;
			this.fileCoursNm = fileCoursNm;
			this.fileSize = fileSize;
			this.fileExtsnNm = fileExtsnNm;
			this.thumbVidoNm = thumbVidoNm;
			this.thumbVidoCoursNm = thumbVidoCoursNm;
			this.extrlFileCoursNm = extrlFileCoursNm;
			this.vectorFileNm = vectorFileNm;
			this.vectorFileCoursNm = vectorFileCoursNm;
			this.upsamplingRate = upsamplingRate;
		}
	}

}
