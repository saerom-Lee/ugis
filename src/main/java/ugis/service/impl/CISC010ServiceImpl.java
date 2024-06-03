package ugis.service.impl;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.UriComponentsBuilder;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.RequiredArgsConstructor;
import ugis.cmmn.service.ApiService;
import ugis.exception.RestApiException;
import ugis.service.CISC010Service;
import ugis.service.CmmnFileService;
import ugis.service.mapper.CISC010Mapper;
import ugis.service.vo.CISC010VO.AiDataSetListRes;
import ugis.service.vo.CISC010VO.AiDataSetRes;
import ugis.service.vo.CISC010VO.AiDataSetSearch;
import ugis.service.vo.CISC010VO.AiLearningRes;
import ugis.service.vo.CISC010VO.AiLearningSearch;
import ugis.service.vo.CISC010VO.ThAiModelPrfomncEvlVidoResult;
import ugis.service.vo.CISC010VO.TnAiDataSet;
import ugis.service.vo.CISC010VO.TnAiDataSetImgMapng;
import ugis.service.vo.CISC010VO.TnAiDataSetLblMapng;
import ugis.service.vo.CISC010VO.TnAiModelPrfomncEvl;
import ugis.service.vo.CISC010VO.TnAiModelVer;
import ugis.service.vo.CISC010VO.TnCmmnFile;
import ugis.service.vo.CISC010VO.TnImageData;
import ugis.service.vo.CISC010VO.TnLblData;

@RequiredArgsConstructor
@Service("cisc010Service")
public class CISC010ServiceImpl extends EgovAbstractServiceImpl implements CISC010Service {

    private final ApiService<String> apiService;
	private final CISC010Mapper cisc010Mapper;
	private final CmmnFileService cmmnFileService;

	@Override
	public Long saveDataSet(TnAiDataSet dto) throws Exception {

		cisc010Mapper.saveDataSet(dto);
		Long aiDataSetId = dto.getAiDataSetId();

		List<TnCmmnFile> imageDataFile = null;
		try {
			imageDataFile = cmmnFileService.saveFile(dto.getImageDataFile());
		} catch (Exception e) {
			throw new RestApiException("이미지 파일 저장 중 오류가 발생하였습니다.", e);
		}
		imageDataFile.forEach(item -> {

			TnImageData tnImageData = TnImageData.builder()
					.fileNm(item.getFileNm())
					.allFileNm(item.getAllFileNm())
					.fileCoursNm(item.getFileCoursNm())
					.fileSize(item.getFileSize())
					.fileExtsnNm(item.getFileExtsnNm())
					.build();

			cisc010Mapper.saveImageData(tnImageData);
			Long imageDataId = tnImageData.getImageDataId();

			cisc010Mapper.saveImageDataMap(
				TnAiDataSetImgMapng.builder()
				.aiDataSetId(aiDataSetId)
				.imageDataId(imageDataId)
				.build()
			);
		});

		List<TnCmmnFile> lblDataFile = null;
		try {
			lblDataFile = cmmnFileService.saveFile(dto.getLblDataFile());
		} catch (Exception e) {
			throw new RestApiException("라벨 파일 저장 중 오류가 발생하였습니다.", e);
		}
		lblDataFile.forEach(item -> {
			TnLblData tnLblData = TnLblData.builder()
					.fileNm(item.getFileNm())
					.allFileNm(item.getAllFileNm())
					.fileCoursNm(item.getFileCoursNm())
					.fileSize(item.getFileSize())
					.fileExtsnNm(item.getFileExtsnNm())
					.build();

			cisc010Mapper.saveLblData(tnLblData);
			Long lblDataId = tnLblData.getLblDataId();

			cisc010Mapper.saveLblDataMap(
				TnAiDataSetLblMapng.builder()
				.aiDataSetId(aiDataSetId)
				.lblDataId(lblDataId)
				.build()
			);
		});

		cisc010Mapper.updateDataSetFileCours(aiDataSetId);

		return aiDataSetId;
	}

	@Override
	public int deleteDataSet(Long aiDataSetId) throws Exception {
		Long modelVerCnt = cisc010Mapper.selectModelVerCount(aiDataSetId);
		if (modelVerCnt > 0) {
			throw new RestApiException("해당 데이터셋을 사용하는 모델이 있어서 삭제할 수 없습니다.");
		}
		Long PrfomncEvlCnt = cisc010Mapper.selectPrfomncEvlCount(aiDataSetId);
		if (PrfomncEvlCnt > 0) {
			throw new RestApiException("해당 데이터셋을 사용하는 성능평가 있어서 삭제할 수 없습니다.");
		}
		cisc010Mapper.deleteImageData(aiDataSetId);
		cisc010Mapper.deleteLblData(aiDataSetId);
		cisc010Mapper.deleteImageMapng(aiDataSetId);
		cisc010Mapper.deleteLblMapng(aiDataSetId);

		return cisc010Mapper.deleteDataSet(aiDataSetId);
	}

	@Override
	public List<AiDataSetListRes> selectAiDataSetList(AiDataSetSearch dto) throws Exception {
		return cisc010Mapper.selectAiDataSetList(dto);
	}

	@Override
	public AiDataSetRes selectAiDataSet(Long aiDataSetId) throws Exception {
		return AiDataSetRes.builder()
				.tnAiDataSet(cisc010Mapper.selectAiDataSet(aiDataSetId))
				.imageDataList(cisc010Mapper.selectImageDataList(aiDataSetId))
				.lblDataList(cisc010Mapper.selectLblDataList(aiDataSetId))
				.build();
	}

	@Override
	public AiLearningRes selectAiModelList(AiLearningSearch dto) throws Exception {
		dto.setModelSeCd("451");//기본 모델
		List<TnAiModelVer> baseModelList = cisc010Mapper.selectModelList(dto);
		dto.setModelSeCd("452");//사용자 모델
		List<TnAiModelVer> userModelList = cisc010Mapper.selectModelList(dto);

		return AiLearningRes.builder()
				.baseModelList(baseModelList)
				.userModelList(userModelList)
				.build();
	}

	@Override
	public AiLearningRes selectAiModel(TnAiModelVer vo) throws Exception {
		TnAiModelVer aiModelVer = cisc010Mapper.selectModelVer(vo);

		return AiLearningRes.builder()
//				.model(cisc010Mapper.selectModel(vo))
				.modelVer(aiModelVer)
				.lrnHistList(cisc010Mapper.selectLrnHistList(aiModelVer))
				.build();
	}

	@Override
	public int saveModelVer(TnAiModelVer vo) throws Exception {
		int count = cisc010Mapper.saveModelVer(vo);

        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

        apiService.get(
            UriComponentsBuilder.fromUriString("/model/train")
                .queryParam("model_id", vo.getModelId())
                .queryParam("ai_model_id", vo.getAiModelId())
                .queryParam("ai_data_set_id", vo.getAiDataSetId())
                .build().toString(), header);

		return count;
	}

	@Override
	public int savePrfomncEvl(TnAiModelPrfomncEvl vo) throws Exception {
		int count = cisc010Mapper.savePrfomncEvl(vo);

        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

        apiService.get(
            UriComponentsBuilder.fromUriString("/model/metric")
                .queryParam("model_id", vo.getModelId())
                .queryParam("ai_model_id", vo.getAiModelId())
                .queryParam("model_prfomnc_evl_id", vo.getModelPrfomncEvlId())
                .queryParam("ai_data_set_id", vo.getAiDataSetId())
                .build().toString(), header);

        return count;
	}

	@Override
	public AiLearningRes selectPrfomncEvlResultList(AiLearningSearch dto) throws Exception {
		return AiLearningRes.builder().prfomncEvlResultList(cisc010Mapper.selectPrfomncEvlResultList(dto)).build();
	}

	@Override
	public int overwriteAiLearning(TnAiModelVer vo) throws Exception {
		return cisc010Mapper.updateAiLearning(vo);
	}

	@Override
	public int saveAsAiLearning(TnAiModelVer vo) throws Exception {
		int count = 0;
		count += cisc010Mapper.saveAsModel(vo);
		count += cisc010Mapper.saveAsModelVer(vo);
		count += cisc010Mapper.saveAsLrnHist(vo);

		//이전버전 삭제
		vo.setModelId(vo.getBeforeModelId());
		vo.setAiModelId(vo.getBeforeAiModelId());
		count += cisc010Mapper.deleteLrnHist(vo);
		count += cisc010Mapper.deleteAiLearning(vo);
		return count;
	}

	@Override
	public int deleteAiLearning(TnAiModelVer vo) throws Exception {
		int count = 0;
		count += cisc010Mapper.deleteLrnHist(vo);
		count += cisc010Mapper.deleteAiLearning(vo);
		return count;
	}

	@Override
	public int updatePrfomncEvlResultComplete(ThAiModelPrfomncEvlVidoResult vo) throws Exception {
		int count = cisc010Mapper.updatePrfomncEvlResult(vo);
		return count;
	}
}
