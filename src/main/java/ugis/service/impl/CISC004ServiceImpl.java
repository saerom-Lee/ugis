package ugis.service.impl;


import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ugis.service.CISC004Service;
import ugis.service.mapper.CISC004Mapper;
import ugis.service.vo.CISC004VO;

import javax.annotation.Resource;

@Service("cisc004Service")
public class CISC004ServiceImpl extends EgovAbstractServiceImpl implements CISC004Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(CISC004ServiceImpl.class);

    @Resource(name="cisc004Mapper")
    private CISC004Mapper cisc004Mapper;


	@Override
	public CISC004VO select(CISC004VO cisc004VO) {

		return cisc004Mapper.select(cisc004VO);
	}

	@Override
	public long insert(CISC004VO cisc004VO) {
		return cisc004Mapper.insert(cisc004VO);
	}
}
