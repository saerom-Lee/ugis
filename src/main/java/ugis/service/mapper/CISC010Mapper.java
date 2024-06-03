package ugis.service.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import ugis.service.vo.CISC010VO.AiDataSetListRes;
import ugis.service.vo.CISC010VO.AiDataSetSearch;
import ugis.service.vo.CISC010VO.AiLearningSearch;
import ugis.service.vo.CISC010VO.ThAiModelPrfomncEvlVidoResult;
import ugis.service.vo.CISC010VO.TnAiDataSet;
import ugis.service.vo.CISC010VO.TnAiDataSetImgMapng;
import ugis.service.vo.CISC010VO.TnAiDataSetLblMapng;
import ugis.service.vo.CISC010VO.TnAiLrnHist;
import ugis.service.vo.CISC010VO.TnAiModel;
import ugis.service.vo.CISC010VO.TnAiModelPrfomncEvl;
import ugis.service.vo.CISC010VO.TnAiModelVer;
import ugis.service.vo.CISC010VO.TnImageData;
import ugis.service.vo.CISC010VO.TnLblData;

@Mapper("cisc010Mapper")
public interface CISC010Mapper {

	Long saveDataSet(TnAiDataSet dto);

	Long saveImageData(TnImageData dto);

	Long saveLblData(TnLblData dto);

	void saveImageDataMap(TnAiDataSetImgMapng dto);

	void saveLblDataMap(TnAiDataSetLblMapng dto);

	void updateDataSetFileCours(Long aiDataSetId);

	int deleteDataSet(Long aiDataSetId);

	int deleteImageData(Long aiDataSetId);

	int deleteLblData(Long aiDataSetId);

	int deleteImageMapng(Long aiDataSetId);

	int deleteLblMapng(Long aiDataSetId);

	Long selectModelVerCount(Long aiDataSetId);

	Long selectPrfomncEvlCount(Long aiDataSetId);

	List<AiDataSetListRes> selectAiDataSetList(AiDataSetSearch dto);

	TnAiDataSet selectAiDataSet(Long aiDataSetId);

	List<TnImageData> selectImageDataList(Long aiDataSetId);

	List<TnLblData> selectLblDataList(Long aiDataSetId);




	TnAiModel selectModel(TnAiModelVer vo);

	TnAiModelVer selectModelVer(TnAiModelVer vo);

	List<TnAiModelVer> selectModelList(AiLearningSearch dto);

	List<TnAiDataSet> selectDataSetList(AiLearningSearch dto);

	int saveModelVer(TnAiModelVer vo);



	int savePrfomncEvl(TnAiModelPrfomncEvl vo);

	List<TnAiLrnHist> selectLrnHistList(TnAiModelVer vo);

	List<ThAiModelPrfomncEvlVidoResult> selectPrfomncEvlResultList(AiLearningSearch dto);



	int saveAsModel(TnAiModelVer vo);

	int saveAsModelVer(TnAiModelVer vo);

	int saveAsLrnHist(TnAiModelVer vo);

	int updateAiLearning(TnAiModelVer vo);

	int deleteAiLearning(TnAiModelVer vo);

	int deleteLrnHist(TnAiModelVer vo);


	int updatePrfomncEvlResult(ThAiModelPrfomncEvlVidoResult vo);
}
