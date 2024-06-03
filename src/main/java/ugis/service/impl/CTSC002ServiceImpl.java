package ugis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import ugis.service.CTSC002Service;
import ugis.service.CTSCBaseService;
import ugis.service.mapper.CTSC001Mapper;
import ugis.service.mapper.CTSC002Mapper;
import ugis.service.mapper.CTSCBaseMapper;
import ugis.service.vo.CTSC002VO;

import java.util.List;

@Service("ctsc002Service")
public class CTSC002ServiceImpl extends CTSCBaseServiceImpl<CTSC002VO> implements CTSC002Service<CTSC002VO> {

	@Resource(name="ctsc002Mapper")
	CTSC002Mapper mapper;
	
	@Override
	public CTSCBaseMapper<CTSC002VO> getMapper() {
		return mapper;
	}

	@Override
	public long insert(CTSC002VO vo) throws Exception {
		return 0;
	}

	@Override
	public List<CTSC002VO> ctsc002_search(CTSC002VO vo) {
		 List<CTSC002VO> search = mapper.ctsc002_search(vo);
		return search;
	}
}
