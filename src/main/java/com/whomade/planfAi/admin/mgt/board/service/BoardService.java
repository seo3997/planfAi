package com.whomade.planfAi.admin.mgt.board.service;

import com.whomade.planfAi.admin.mgt.board.mapper.BoardMapper;
import com.whomade.planfAi.common.util.SysUtil;
import com.whomade.planfAi.common.util.CoStringUtils;
import com.whomade.planfAi.common.util.file.CoFileMngUtil;
import com.whomade.planfAi.common.util.file.vo.CoFileVO;
import com.whomade.planfAi.common.vo.DataMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;
    private final CoFileMngUtil coFileMngUtil;

    public BoardService(BoardMapper boardMapper, CoFileMngUtil coFileMngUtil) {
        this.boardMapper = boardMapper;
        this.coFileMngUtil = coFileMngUtil;
    }

    public int selectTotCntBoard(DataMap param) {
        return boardMapper.selectTotCntBoard(param);
    }

    public List<DataMap> selectPageListBoard(DataMap param) {
        return boardMapper.selectPageListBoard(param);
    }

    public DataMap selectBoard(DataMap param) {
        DataMap board = boardMapper.selectBoard(param);
        if (board != null && board.get("ATCH_DOC_ID") != null) {
            param.put("atch_doc_id", board.get("ATCH_DOC_ID"));
            board.put("fileList", boardMapper.selectFileList(param));
        }
        return board;
    }

    @Transactional
    public void insertBoard(DataMap param, List<MultipartFile> fileList) throws Exception {
        if (fileList != null && !fileList.isEmpty()) {
            // 문서 ID 셋팅
            String doc_id = CoStringUtils.nvl(param.getString("atch_doc_id"), SysUtil.getDocId());
            param.put("atch_doc_id", doc_id);

            for (MultipartFile mfile : fileList) {
                if (!mfile.isEmpty()) {
                    // 파일을 서버에 물리적으로 저장하고
                    CoFileVO _reAtFile = coFileMngUtil.parseFileInf(mfile, doc_id, "board",
                            param.getString("ss_user_no"));
                    // 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
                    boardMapper.insertFile(_reAtFile);
                }
            }
        }
        boardMapper.insertBoard(param);
    }

    @Transactional
    public void updateBoard(DataMap param, List<MultipartFile> fileList) throws Exception {
        if (fileList != null && !fileList.isEmpty()) {
            // 문서 ID 셋팅
            String doc_id = CoStringUtils.nvl(param.getString("atch_doc_id"), SysUtil.getDocId());
            param.put("atch_doc_id", doc_id);

            for (MultipartFile mfile : fileList) {
                if (!mfile.isEmpty()) {
                    // 파일을 서버에 물리적으로 저장하고
                    CoFileVO _reAtFile = coFileMngUtil.parseFileInf(mfile, doc_id, "board",
                            param.getString("ss_user_no"));
                    // 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
                    boardMapper.insertFile(_reAtFile);
                }
            }
        }
        boardMapper.updateBoard(param);
    }

    @Transactional
    public void deleteBoard(DataMap param) {
        boardMapper.deleteBoard(param);
    }
}
