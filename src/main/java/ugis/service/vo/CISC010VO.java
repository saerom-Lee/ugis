package ugis.service.vo;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ugis.service.vo.CISC010VO.TnAiLrnHist;

public class CISC010VO {

	/**
	 * AI 학습 공통 검색
	 */
	@Getter
	@Setter
	public static class AiLearningSearch {
		private String[] searchImageNm;
		private String searchAiDataSetNm;
		private String modelSeCd;
		private String[] searchProgrsSttusCd;
		private Long modelId;
		private Long aiModelId;
		private Long aiLrnId;
		private Long modelPrfomncEvlId;
		private String streYn;
	}

	/**
	 * AI 학습 공통 응답 처리
	 */
	@Getter
	public static class AiLearningRes {
		private TnAiModel model;
		private TnAiModelVer modelVer;
		private List<TnAiModelVer> baseModelList;
		private List<TnAiModelVer> userModelList;
		private List<TnAiDataSet> dataSetList;
		private List<TnAiLrnHist> lrnHistList;
		private List<ThAiModelPrfomncEvlVidoResult> prfomncEvlResultList;

		@Builder
		public AiLearningRes(TnAiModel model, TnAiModelVer modelVer, List<TnAiModelVer> baseModelList, List<TnAiModelVer> userModelList, List<TnAiDataSet> dataSetList, List<TnAiLrnHist> lrnHistList, List<ThAiModelPrfomncEvlVidoResult> prfomncEvlResultList) {
			this.model = model;
			this.modelVer = modelVer;
			this.baseModelList = baseModelList;
			this.userModelList = userModelList;
			this.dataSetList = dataSetList;
			this.lrnHistList = lrnHistList;
			this.prfomncEvlResultList = prfomncEvlResultList;
		}
	}

	@Getter
	@Setter
	public static class AiDataSetSearch {
		private String[] searchImageNm;
		private String searchAiDataSetNm;
	}

	@Getter
	public static class AiDataSetListRes {
		private Long aiDataSetId;
		private String aiDataSetNm;
	}

	@Getter
	public static class AiDataSetRes {

		private TnAiDataSet tnAiDataSet;
		private List<TnImageData> imageDataList;
		private List<TnLblData> lblDataList;

		@Builder
		public AiDataSetRes(TnAiDataSet tnAiDataSet, List<TnImageData> imageDataList, List<TnLblData> lblDataList) {
			this.tnAiDataSet = tnAiDataSet;
			this.imageDataList = imageDataList;
			this.lblDataList = lblDataList;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiDataSet {

		private Long aiDataSetId;
		private String aiDataSetNm;
		private String imageNm;
		private Long rsoltnValue;
		private String bandNm;
		private String imageDataCoursNm;
		private String lblDataCoursNm;
		private MultipartFile imageDataFile;
		private MultipartFile lblDataFile;

		@Builder
		public TnAiDataSet(Long aiDataSetId, String aiDataSetNm, String imageNm, Long rsoltnValue,
				String bandNm, String imageDataCoursNm, String lblDataCoursNm) {
			this.aiDataSetId = aiDataSetId;
			this.aiDataSetNm = aiDataSetNm;
			this.imageNm = imageNm;
			this.rsoltnValue = rsoltnValue;
			this.bandNm = bandNm;
			this.imageDataCoursNm = imageDataCoursNm;
			this.lblDataCoursNm = lblDataCoursNm;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnImageData {

		private Long imageDataId;
		private String fileNm;
	    private String allFileNm;
	    private String fileCoursNm;
	    private String fileExtsnNm;
	    private Long fileSize;

	    @Builder
		public TnImageData(Long imageDataId, String fileNm, String allFileNm, String fileCoursNm, String fileExtsnNm, Long fileSize) {
			this.imageDataId = imageDataId;
			this.fileNm = fileNm;
			this.allFileNm = allFileNm;
			this.fileCoursNm = fileCoursNm;
			this.fileExtsnNm = fileExtsnNm;
			this.fileSize = fileSize;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnLblData {

		private Long lblDataId;
		private String fileNm;
	    private String allFileNm;
	    private String fileCoursNm;
	    private String fileExtsnNm;
	    private Long fileSize;

	    @Builder
		public TnLblData(Long lblDataId, String fileNm, String allFileNm, String fileCoursNm, String fileExtsnNm, Long fileSize) {
			this.lblDataId = lblDataId;
			this.fileNm = fileNm;
			this.allFileNm = allFileNm;
			this.fileCoursNm = fileCoursNm;
			this.fileExtsnNm = fileExtsnNm;
			this.fileSize = fileSize;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiDataSetImgMapng {

		private Long aiDataSetId;
		private Long imageDataId;

		@Builder
		public TnAiDataSetImgMapng(Long aiDataSetId, Long imageDataId) {
			this.aiDataSetId = aiDataSetId;
			this.imageDataId = imageDataId;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiDataSetLblMapng {

		private Long aiDataSetId;
		private Long lblDataId;

		@Builder
		public TnAiDataSetLblMapng(Long aiDataSetId, Long lblDataId) {
			this.aiDataSetId = aiDataSetId;
			this.lblDataId = lblDataId;
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnCmmnFile {

		private String fileNm;
	    private String allFileNm;
	    private String fileCoursNm;
	    private String fileExtsnNm;
	    private Long fileSize;

	    @Builder
		public TnCmmnFile(String fileNm, String allFileNm, String fileCoursNm, String fileExtsnNm, Long fileSize) {
			this.fileNm = fileNm;
			this.allFileNm = allFileNm;
			this.fileCoursNm = fileCoursNm;
			this.fileExtsnNm = fileExtsnNm;
			this.fileSize = fileSize;
		}

	}

	@Getter
	@Setter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiModel {
		private Long modelId;
		private String modelNm;
		private String modelSeCd;
		private String algrthCd;

		private Long beforeModelId;

		@Builder
		public TnAiModel(Long modelId, String modelNm, String modelSeCd, String algrthCd) {
			this.modelId = modelId;
			this.modelNm = modelNm;
			this.modelSeCd = modelSeCd;
			this.algrthCd = algrthCd;
		}

	}

	@Getter
	@Setter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiModelVer extends TnAiModel {
		private Long aiModelId;
		private Long aiDataSetId;
		private String patchSizeCd;
		private String batchSizeCd;
		private Integer reptitCo;
		private Integer lrnRateSetup;
		private String progrsSttusCd;
		private String streYn;
		private String modelExecutCoursNm;
		private Long aiLrnId;
		private String modelStreCoursNm;

		private String imageNm;
		private String aiDataSetNm;
		private Long rsoltnValue;

		private Long beforeAiModelId;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiLrnHist {
		private Long modelId;
		private String modelNm;
		private Long aiModelId;
		private Long aiLrnId;
		private Long lrnCo;
		private Float acccuracyPt;
		private Float mlouPt;
		private Float precisionPt;
		private Float recallPt;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class TnAiModelPrfomncEvl {
		private Long modelId;
		private String modelNm;
		private Long aiModelId;
		private Long modelPrfomncEvlId;
		private Long aiDataSetId;
		private String aiDataSetNm;
		private String modelPrfomncEvlNm;
		private String progrsSttusCd;
		private String streYn;
		private Float acccuracyPt;
		private Float mlouPt;
		private Float precisionPt;
		private Float recallPt;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ThAiModelPrfomncEvlVidoResult extends TnAiModelPrfomncEvl {
		private Long vidoId;
		private String vidoNm;
		private String fileNm;
		private String allFileNm;
		private String fileCoursNm;
		private String fileSize;
		private String fileExtsnNm;
		private String thumbVidoNm;
		private String thumbVidoCoursNm;
	}


}
