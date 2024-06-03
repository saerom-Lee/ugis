package ugis.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import lombok.RequiredArgsConstructor;
import ugis.service.CTSC004Service;
import ugis.service.mapper.CTSC004Mapper;
import ugis.service.vo.CTSC004VO;

@RequiredArgsConstructor
@Service("ctsc004Service")
public class CTSC004ServiceImpl implements CTSC004Service<CTSC004VO> {

	private final CTSC004Mapper mapper;
	
	@Override	
	public List<EgovMap> selectTnHistList(CTSC004VO vo) {
		// TODO Auto-generated method stub
		return mapper.selectTnHistList(vo);
	}

	@Override
	public List<EgovMap> selectTnLogList(CTSC004VO vo){
		// TODO Auto-generated method stub
		if(vo.getPotogrfVidoCd().equals("free")) {
			return mapper.selectTnLogFree(vo);
		} else if(vo.getPotogrfVidoCd().equals("emer")) {
			return mapper.selectTnLogEmer(vo);
		}
		return null;
		
	}

	@Override
	public List<EgovMap> selectTnMetaList(CTSC004VO vo) {
		// TODO Auto-generated method stub
		if(vo.getPotogrfVidoCd().equals("free")) {
			return mapper.selectTnMetaFree(vo);
		} else if(vo.getPotogrfVidoCd().equals("emer")) {
			return mapper.selectTnMetaEmer(vo);
		}
		
		return null;
	}

	
}
