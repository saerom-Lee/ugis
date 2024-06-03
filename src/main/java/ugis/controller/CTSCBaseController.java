package ugis.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ugis.service.CTSCBaseService;
import ugis.service.vo.CTPageVO;

public abstract class CTSCBaseController<T> {

	public CTSCBaseController() {}
	
	public abstract CTSCBaseService<T> getService();
	
	public ResponseEntity<T> add(T vo) throws Exception {
		T result;
		try {
			getService().add(vo);
			result = vo;
			return ResponseEntity.ok(result);
		}
		catch(SQLException e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		catch(Exception e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	public ResponseEntity<T> upt(T vo) throws Exception {
		T result;
		try {
			getService().upt(vo);
			result = vo;
			return ResponseEntity.ok(result);
		}
		catch(SQLException e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		catch(Exception e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	public ResponseEntity<T> del(T vo) throws Exception {
		T result;
		try {
			getService().del(vo);
			result = vo;
			return ResponseEntity.ok(result);
		}
		catch(SQLException e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		catch(Exception e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	public ResponseEntity<T> get(T vo) throws Exception {
		T result;
		try {
			result = getService().get(vo);
			return ResponseEntity.ok(result);
		}
		catch(SQLException e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		catch(Exception e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
	public ResponseEntity<List<T>> search(Integer page, T vo) throws Exception {
		CTPageVO<T> search = new CTPageVO<T>(page, vo);
		List<T> result;
		try {
			result = getService().search(search);
			return ResponseEntity.ok(result);
		}
		catch(SQLException e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
		catch(Exception e) {
			result = null;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}
	
}
