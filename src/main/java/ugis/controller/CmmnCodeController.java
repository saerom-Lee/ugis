package ugis.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ugis.service.CmmnCodeService;
import ugis.service.vo.CmmnCodeVO;

@RequiredArgsConstructor
@RestController
public class CmmnCodeController {

    private final CmmnCodeService cmmnCodeService;

    @GetMapping(value = "/getCmmnCode.do")
    public ResponseEntity<List<CmmnCodeVO>> getCmmnCode(@ModelAttribute CmmnCodeVO vo) throws Exception {
        return ResponseEntity.ok().body(cmmnCodeService.selectCmmnCodeList(vo));
    }

}
