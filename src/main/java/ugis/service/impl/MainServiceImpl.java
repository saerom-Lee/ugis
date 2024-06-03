package ugis.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.MainService;
import ugis.service.mapper.MainMapper;

@Service("mainService")
public class MainServiceImpl extends EgovAbstractServiceImpl implements MainService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);
	
	@Resource(name="mainMapper")
	private MainMapper mainMapper;
	
	@Override
	public List<?> selectRadarList() throws Exception {
		return mainMapper.selectRadarList();
	}

	@Override
	public List<?> selectFileDown(HashMap<String, Object> param) throws Exception {
		return mainMapper.selectFileDown(param);
	}
}
