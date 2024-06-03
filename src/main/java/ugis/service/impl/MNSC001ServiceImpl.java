package ugis.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.MNSC001Service;
import ugis.service.mapper.MNSC001Mapper;

@Service("mnsc001Service")
public class MNSC001ServiceImpl extends EgovAbstractServiceImpl implements MNSC001Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);
	
	//
	@Resource(name = "mnsc001Mapper")
	private MNSC001Mapper mnsc001Mapper;
	
	
	
}
