package ugis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ugis.service.CTSCBaseService;
import ugis.service.mapper.CTSCBaseMapper;
import ugis.service.vo.CTSC003VO;

@Service("ctsc003Service")
public class CTSC003ServiceImpl extends CTSCBaseServiceImpl<CTSC003VO> implements CTSCBaseService<CTSC003VO> {

	@Resource(name="ctsc003Mapper")
	CTSCBaseMapper<CTSC003VO> mapper;
	
	@Override
	public CTSCBaseMapper<CTSC003VO> getMapper() {
		return mapper;
	}

	@Override
	public long insert(CTSC003VO vo) throws Exception {
		return 0;
	}
}
