package ugis.service;

import java.util.List;

import ugis.service.vo.CISC010VO.AiDataSetListRes;
import ugis.service.vo.CISC010VO.AiDataSetRes;
import ugis.service.vo.CISC010VO.AiDataSetSearch;
import ugis.service.vo.CISC010VO.AiLearningRes;
import ugis.service.vo.CISC010VO.AiLearningSearch;
import ugis.service.vo.CISC010VO.ThAiModelPrfomncEvlVidoResult;
import ugis.service.vo.CISC010VO.TnAiDataSet;
import ugis.service.vo.CISC010VO.TnAiModelPrfomncEvl;
import ugis.service.vo.CISC010VO.TnAiModelVer;

public interface CISC010Service {

	Long saveDataSet(TnAiDataSet dto) throws Exception;

	int deleteDataSet(Long aiDataSetId) throws Exception;

	List<AiDataSetListRes> selectAiDataSetList(AiDataSetSearch dto) throws Exception;

	AiDataSetRes selectAiDataSet(Long aiDataSetId) throws Exception;

	AiLearningRes selectAiModelList(AiLearningSearch dto) throws Exception;

	AiLearningRes selectAiModel(TnAiModelVer vo) throws Exception;

	int saveModelVer(TnAiModelVer vo) throws Exception;

	int savePrfomncEvl(TnAiModelPrfomncEvl vo) throws Exception;

	AiLearningRes selectPrfomncEvlResultList(AiLearningSearch dto) throws Exception;


	int overwriteAiLearning(TnAiModelVer vo) throws Exception;

	int saveAsAiLearning(TnAiModelVer vo) throws Exception;

	int deleteAiLearning(TnAiModelVer vo) throws Exception;

	int updatePrfomncEvlResultComplete(ThAiModelPrfomncEvlVidoResult vo) throws Exception;
}
