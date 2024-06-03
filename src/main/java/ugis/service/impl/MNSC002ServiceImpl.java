package ugis.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.MNSC002Service;
import ugis.service.mapper.MNSC002Mapper;

@Service("mnsc002Service")
public class MNSC002ServiceImpl extends EgovAbstractServiceImpl implements MNSC002Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);
	
	//
	@Resource(name = "mnsc002Mapper")
	private MNSC002Mapper mnsc002Mapper;
	
	
	
}
