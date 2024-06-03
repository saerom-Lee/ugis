package ugis.service.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.vo.CISC015VO.ObjChangeListRes;
import ugis.service.vo.CISC015VO.ObjChangeSearch;
import ugis.service.vo.CMSC003DataSetVO;
import ugis.service.vo.CMSC003InsertVO;
import ugis.service.vo.CMSC003VO;
import ugis.service.vo.CMSC003VO2;
import ugis.service.vo.CMSC003VO3;
import ugis.service.vo.CMSC003VO4;
import ugis.service.vo.DataCodeVO;

@Mapper("cmsc003Mapper")
public interface CMSC003Mapper {

	List<?> selectAeroList(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectEmcList(CMSC003VO cmsc003vo) throws SQLException;

	// List<?> selectUsgsList(CMSC003VO cmsc003vo) throws Exception;

	List<?> selectG001List() throws SQLException;

	List<?> selectG001Geom(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectG010List(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectG010Geom(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectG011List(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectG011Geom(CMSC003VO cmsc003VO) throws SQLException;

	List<?> selectUsgsCurrentList(CMSC003VO cmsc003vo) throws SQLException;

	// 항공영상, 정사영상, DEM 임시
	List<?> selectUsgsCurrentList3(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectUsgsEmergencyList(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectUsgsWorkCurrentList(CMSC003VO cmsc003vo) throws SQLException;

	// 항공영상, 정사영상, DEM 임시
	List<?> selectUsgsWorkCurrentList3(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectUsgsWorkMosa(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectUsgsWorkEmergencyList(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectUsgsCurrentList2(CMSC003VO cmsc003vo) throws SQLException;

	List<?> selectAIMdAnalsMapResut(int vidoId) throws SQLException;

	List<?> selectChangeDetctResult(int vidoId) throws SQLException;

	void insertDatasetInfo(CMSC003DataSetVO cmsc004vo) throws SQLException;

	List<?> selectDisasterInfoId(CMSC003VO2 cmsc003VO2) throws SQLException;

	void updateMsfrtnInfoColctRequst(CMSC003VO2 cmsc003VO2) throws SQLException;

	List<?> selectTcMsfrtnTyInfo(String disasterCd) throws SQLException;

	EgovMap selectByRequestId(String requestId) throws SQLException;

	List<?> selectAllUsgsWorkList(ObjChangeSearch dto) throws SQLException;

	void insertDatasetWork(EgovMap digitalRes) throws SQLException;

	List<ObjChangeListRes> selectDatasetWorkList(ObjChangeSearch dto) throws SQLException;

	List<EgovMap> selectUsgsWorkCurrentList5186(CMSC003VO cmsc003vo) throws SQLException;

	Integer insertUsgs(CMSC003InsertVO cmsc003vo) throws SQLException;

	void insertUsgsMeta(CMSC003InsertVO vo) throws SQLException;

	int selectLastUsgsId() throws SQLException;

	void insertDataset(CMSC003VO3 cmsc003vo3) throws SQLException;

	List<EgovMap> selectDatasetById(String disasterId) throws SQLException;

	void deleteAllDatasetById(String disasterId) throws SQLException;

	List<EgovMap> selectDatasetByDisasterId(CMSC003VO3 cmsc003vo3) throws SQLException;

	EgovMap selectUsgsByInFileCours(String fileCours) throws SQLException;

	EgovMap selectUsgsWorkByInFileCours(String fileCours) throws SQLException;

	List<EgovMap> selectDatasetAirByDisasterId(CMSC003VO3 cmsc003vo3) throws SQLException;

	List<EgovMap> selectDatasetOrtByDisasterId(CMSC003VO3 cmsc003vo3) throws SQLException;

	List<EgovMap> selectUsgsWorkCurrentAirList(CMSC003VO cmsc003vo) throws SQLException;

	List<EgovMap> selectUsgsWorkCurrentOrtList(CMSC003VO cmsc003vo) throws SQLException;

	List<EgovMap> selectUsgsWorkCurrentDemList(CMSC003VO cmsc003vo) throws SQLException;

	List<EgovMap> selectAllDatasetInfo(CMSC003VO2 cmsc003vo2) throws SQLException;

	void insertMsfrtnDataset(CMSC003VO4 cmsc003vo4) throws SQLException;

	EgovMap selectMsfrtnDataset(CMSC003VO4 cmsc003vo4) throws SQLException;

	void deleteMsfrtnDataset(CMSC003VO4 cmsc003vo4);

	void deleteDatasetInfo(CMSC003VO4 cmsc003vo4);

	// 재난영역 조회
	// 위성영상 조회
	List<EgovMap> selectSatelliteDatasetByDisasterId(CMSC003VO cmsc003vo) throws SQLException;

	// 긴급영상조회
	List<EgovMap> selectDisaterDatasetByDisasterId(CMSC003VO cmsc003vo) throws SQLException;

	// 항공영상 조회
	List<EgovMap> selectAirDatasetByDisasterId(CMSC003VO cmsc003vo) throws SQLException;

	// 정사영상 조회
	List<EgovMap> selectOrtDatasetByDisasterId(CMSC003VO cmsc003vo) throws SQLException;

	// DEM 조회
	List<EgovMap> selectDemDatasetByDisasterId(CMSC003VO cmsc003vo) throws SQLException;

	EgovMap selectDatasetInfoByFileCoursNm(String input_vido) throws SQLException;

	List<EgovMap> selectAllDatasetInfoFromDate(CMSC003VO2 cmsc003vo2) throws SQLException;

	List<EgovMap> selectDatasetInfoByIdList(CMSC003VO2 cmsc003vo2) throws SQLException;

	int deleteUsgsGfraByVidoId(int vidoId) throws SQLException;

	void deleteUsgsMetaByVidoId(int vidoId) throws SQLException;

	List<EgovMap> selectUsgsWorByVidoId(int vidoId) throws SQLException;

	void updateMsfrtnDataset(CMSC003VO4 cmsc003vo4) throws SQLException;

	EgovMap selectDatasetInfo(CMSC003DataSetVO dataSetVo) throws SQLException;
	
	
	void updateDatasetInfo(CMSC003DataSetVO dataSetVo) throws SQLException;

	void updateDatasetInfo2(CMSC003VO4 cmsc003vo4) throws SQLException;
	
	//국토영상공급시스템 디비 delete - 미사용
	void deleteDatasetSaveInfo(String disasterId) throws SQLException;
	
	//국토영상공급시스템 디비 select - 미사용
	List<?> selectDatasetSaveInfo(String disasterId) throws SQLException;
	
	//국토영상공급시스템 디비 insert - 미사용
	void insertDatasetSaveInfoMap(EgovMap dataset) throws SQLException;
	
	//국토영상공급시스템 RlsTy update
	void updateDatasetRlsTy(Map<String, Object> rlsTyMap);

	//파일명 조회
	EgovMap selectFileNm(Map<String, Object> rlsTyMap);

	//데이터 코드 조회
	List<DataCodeVO> selectDataCode();
	

}
