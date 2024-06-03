package ugis.service.impl;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.RequiredArgsConstructor;
import ugis.cmmn.service.ApiService;
import ugis.service.CISC017Service;
//import ugis.service.CmmnFileService;
import ugis.service.mapper.CISC017Mapper;
import ugis.service.vo.CISC017VO.ChangeDetctRsltListRes;
import ugis.service.vo.CISC017VO.ThChngeDetct;
import ugis.service.vo.CISC017VO.ThChngeDetctVidoResult;

@RequiredArgsConstructor
@Service("cisc017Service")
public class CISC017ServiceImpl extends EgovAbstractServiceImpl implements CISC017Service {

	private final ApiService<String> apiService;
	// private final CmmnFileService cmmnFileService;
	private final CISC017Mapper cisc017Mapper;

	@Override
	public Long saveThChngeDetct(ThChngeDetct dto) throws Exception {

		cisc017Mapper.saveThChngeDetct(dto);
		Long chngeDetctResultId = dto.getChngeDetctResultId();

		dto.getVidoList().forEach(item -> {
			cisc017Mapper.saveThChngeDetctResult(ThChngeDetctVidoResult.builder().chngeDetctResultId(chngeDetctResultId)
					.vidoId(item.getVidoId()).vidoNm(item.getVidoNm()).innerFileCoursNm(item.getInnerFileCoursNm())
					.extrlFileCoursNm(item.getInnerFileCoursNm())
					// .extrlFileCoursNm(cmmnFileService.getChgExtrlFileCoursNm(item.getInnerFileCoursNm()))
					.build());
		});

		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

		apiService.get(UriComponentsBuilder.fromUriString("/change/detect")
				.queryParam("chnge_detct_result_id", chngeDetctResultId).build().toString(), header);

		return chngeDetctResultId;

	}

	@Override
	public int updateThChngeDetctStreYn(ThChngeDetct dto) throws Exception {
		return cisc017Mapper.updateThChngeDetctStreYn(dto);
	}

	@Override
	public ChangeDetctRsltListRes selectThChngeDetctRsltList() throws Exception {
		return ChangeDetctRsltListRes.builder().groupList(cisc017Mapper.selectThChngeDetctList())
				.resultList(cisc017Mapper.selectThChngeDetctRsltList()).build();
	}

	@Override
	public List<ChangeDetctRsltListRes> selectThChngeDetctVectorList() throws Exception {
		return cisc017Mapper.selectThChngeDetctVectorList();
	}

	@Override
	public int deleteVector(ThChngeDetctVidoResult dto) throws Exception {

		dto.getVectorDeleteList().forEach(item -> {
			cisc017Mapper.deleteVector(item);
		});

		return 0;
	}

	@Override
	public int updateVector(ThChngeDetctVidoResult dto) throws Exception {

		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

		apiService.get(UriComponentsBuilder.fromUriString("/change/vector")
				.queryParam("chnge_detct_result_id", dto.getChngeDetctResultId())
				.queryParam("chnge_detct_vido_result_id", dto.getChngeDetctVidoResultId())
				.queryParam("upsampling_rate", dto.getUpsamplingRate()).build().toString(), header);

		return 0;

	}

	@Override
	public ChangeDetctRsltListRes selectThChngeDetctRsltWorkList() {
		return ChangeDetctRsltListRes.builder().groupList(cisc017Mapper.selectThChngeDetctList())
				.resultList(cisc017Mapper.selectThChngeDetctRsltWorkList()).build();
	}

}
