package ugis.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import lombok.RequiredArgsConstructor;
import ugis.cmmn.service.ApiService;
import ugis.service.CISC015Service;
import ugis.service.mapper.CISC007Mapper;
//import ugis.service.CmmnFileService;
import ugis.service.mapper.CISC015Mapper;
import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeRsltListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CISC015VO.TnAiModelAnals;
import ugis.service.vo.CISC015VO.TnAiModelAnalsVidoMapngResult;

@RequiredArgsConstructor
@Service("cisc015Service")
public class CISC015ServiceImpl extends EgovAbstractServiceImpl implements CISC015Service {

	private final ApiService<String> apiService;
	// private final CmmnFileService cmmnFileService;
	private final CISC015Mapper cisc015Mapper;

	@Override
	public List<ObjChangeListRes> selectSateList(ObjChangeSearch dto) throws Exception {

		return cisc015Mapper.selectSateList(dto);
	}

	@Override
	public Long saveAiModelAnals(TnAiModelAnals dto) throws Exception {

		cisc015Mapper.saveAiModelAnals(dto);
		Long modelAnalsId = dto.getModelAnalsId();

		dto.getVidoList().forEach(item -> {
			cisc015Mapper.saveAiModelAnalsResult(
					TnAiModelAnalsVidoMapngResult.builder().modelId(dto.getModelId()).aiModelId(dto.getAiModelId())
							.modelAnalsId(modelAnalsId).vidoId(item.getVidoId()).vidoNm(item.getVidoNm())
							.innerFileCoursNm(item.getInnerFileCoursNm()).extrlFileCoursNm(item.getInnerFileCoursNm())
							// .extrlFileCoursNm(cmmnFileService.getExtrlFileCoursNm(item.getInnerFileCoursNm()))
							.build());
		});

		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

		apiService.get(UriComponentsBuilder.fromUriString("/model/predict").queryParam("model_id", dto.getModelId())
				.queryParam("ai_model_id", dto.getAiModelId()).queryParam("model_anals_id", modelAnalsId).build()
				.toString(), header);

		return modelAnalsId;

	}

	@Override
	public ObjChangeRsltListRes selectAiModelAnalsRsltList() throws Exception {
		return ObjChangeRsltListRes.builder().groupList(cisc015Mapper.selectAiModelAnalsList())
				.resultList(cisc015Mapper.selectAiModelAnalsRsltList()).build();
	}

	@Override
	public int updateAiModelAnalsStreYn(TnAiModelAnals dto) throws Exception {
		return cisc015Mapper.updateAiModelAnalsStreYn(dto);
	}

	@Override
	public int updateVector(TnAiModelAnalsVidoMapngResult dto) throws Exception {

		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

		apiService.get(UriComponentsBuilder.fromUriString("/model/vector").queryParam("model_id", dto.getModelId())
				.queryParam("ai_model_id", dto.getAiModelId()).queryParam("model_anals_id", dto.getModelAnalsId())
				.queryParam("anals_vido_id", dto.getAnalsVidoId())
				.queryParam("upsampling_rate", dto.getUpsamplingRate() + "%").build().toString(), header);

		return 0;
	}

	@Override
	public List<ObjChangeRsltListRes> selectAiModelAnalsVectorList() throws Exception {
		return cisc015Mapper.selectAiModelAnalsVectorList();
	}

	@Override
	public int deleteVector(TnAiModelAnalsVidoMapngResult dto) throws Exception {

		dto.getVectorDeleteList().forEach(item -> {
			cisc015Mapper.deleteVector(item);
		});

		return 0;
	}

	@Override
	public List<ObjChangeListRes> selectUsgsWorkList(ObjChangeSearch dto) {
		return cisc015Mapper.selectUsgsWorkList(dto);
	}

	@Override
	public ObjChangeRsltListRes selectAiModelAnalsRsltWorkList() throws Exception {
		return ObjChangeRsltListRes.builder().groupList(cisc015Mapper.selectAiModelAnalsList())
				.resultList(cisc015Mapper.selectAiModelAnalsRsltWorkList()).build();
	}

}
