package ugis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.CISC001Service;
import ugis.service.mapper.CISC001Mapper;
import ugis.service.vo.CISC001AlgorithmVO;
import ugis.service.vo.CISC001ProjectLogVO;
import ugis.service.vo.CISC001ProjectVO;
import ugis.service.vo.CISC001ScriptVO;
import ugis.service.vo.CISC001WorkResultVO;
import ugis.service.vo.CMSC003InsertVO;

@Service("cisc001Service")
public class CISC001ServiceImpl extends EgovAbstractServiceImpl implements CISC001Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(CISC001ServiceImpl.class);

	@Resource(name = "cisc001Mapper")
	private CISC001Mapper cisc001Mapper;

	@Override
	public int insertProject(CISC001ProjectVO cisc001projectVO) {
		return cisc001Mapper.insertProject(cisc001projectVO);
	}

	@Override
	public int updateProject(CISC001ProjectVO cisc001projectVO) {
		return cisc001Mapper.updateProject(cisc001projectVO);
	}

	@Override
	public List<CISC001ProjectVO> selectProjectList(CISC001ProjectVO cisc001projectVO) throws Exception {
		return cisc001Mapper.selectProjectList(cisc001projectVO);
	}

	@Override
	public List<CISC001ProjectLogVO> selectProjectLogList(CISC001ProjectLogVO cisc001projectLogVO) throws Exception {
		return cisc001Mapper.selectProjectLogList(cisc001projectLogVO);
	}

	@Override
	public int insertProjectLog(CISC001ProjectLogVO cisc001projectLogVO) {
		return cisc001Mapper.insertProjectLog(cisc001projectLogVO);
	}

	@Override
	public int deleteProjectLog(CISC001ProjectLogVO cisc001projectLogVO) {
		return cisc001Mapper.deleteProjectLog(cisc001projectLogVO);
	}

	@Override
	public List<CISC001AlgorithmVO> selectAlgorithmRegList(CISC001AlgorithmVO cisc001algorithmVO) throws Exception {
		return cisc001Mapper.selectAlgorithmRegList(cisc001algorithmVO);
	}

	@Override
	public int insertAlgorithm(CISC001AlgorithmVO cisc001algorithmVO) {
		return cisc001Mapper.insertAlgorithm(cisc001algorithmVO);
	}

	@Override
	public int updateAlgorithm(CISC001AlgorithmVO cisc001algorithmVO) {
		return cisc001Mapper.updateAlgorithm(cisc001algorithmVO);
	}

	@Override
	public List<CISC001AlgorithmVO> selectAlgorithmList(CISC001AlgorithmVO cisc001algorithmVO) throws Exception {
		return cisc001Mapper.selectAlgorithmList(cisc001algorithmVO);
	}

	@Override
	public int insertScript(CISC001ScriptVO cisc001scriptVO) {
		return cisc001Mapper.insertScript(cisc001scriptVO);
	}

	@Override
	public List<CISC001ScriptVO> selectScriptList(CISC001ScriptVO cisc001scriptVO) throws Exception {
		return cisc001Mapper.selectScriptList(cisc001scriptVO);
	}

	@Override
	public int updateScript(CISC001ScriptVO cisc001scriptVO) {
		return cisc001Mapper.updateScript(cisc001scriptVO);
	}

	@Override
	public int deleteScript(CISC001ScriptVO cisc001scriptVO) {
		return cisc001Mapper.deleteScript(cisc001scriptVO);
	}

	@Override
	public int insertWorkResult(CISC001WorkResultVO CISC001workVO) {

		int ret = cisc001Mapper.insertWorkResult(CISC001workVO);

		if (ret < 1) {
			ret = cisc001Mapper.insertWorkResult2(CISC001workVO);
		}
		return ret; // cisc001Mapper.insertWorkResult(CISC001workVO);
	}

	@Override
	public void insertWorkResult2(CISC001WorkResultVO workVO) {
		cisc001Mapper.insertWorkResult2(workVO);
	}

	@Override
	public int insertUsgsOnlyVidoId(CMSC003InsertVO insertVO) throws Exception {
		return cisc001Mapper.insertUsgsOnlyVidoId(insertVO);
	}

	@Override
	public int selectLastUsgsId() throws Exception {
		return cisc001Mapper.selectLastUsgsId();
	}

	@Override
	public void insertUsgsMeta(CMSC003InsertVO insertVO) throws Exception {
		cisc001Mapper.insertUsgsMeta(insertVO);
	}

}
