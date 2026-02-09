package com.whomade.planfAi.front.board.controller;

import com.whomade.planfAi.common.util.RequestUtil;
import com.whomade.planfAi.common.vo.DataMap;
import com.whomade.planfAi.front.board.mapper.FrontBoardMapper;
import com.whomade.planfAi.front.board.vo.FrontBoardVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/front/board")

public class FrontBoardontroller {
    private final FrontBoardMapper boardMapper;

    @RequestMapping(value = "/selectPageListBoard.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String boardList(HttpServletRequest request, Model model){ // Model 객체 추가
        //List<BoardVo> boardList = new ArrayList<>();
        /*
        String seChoice = request.getParameter("seChoice");
        String edTitle = request.getParameter("edTitle");
        // 초기 진입 시 null 처리 (기본값 설정)
        if (seChoice == null) seChoice = "99";
        if (edTitle == null) edTitle = "";
        // 로그 확인 (콘솔)
        System.out.println("Request로 받은 seChoice: " + seChoice);
        System.out.println("Request로 받은 edTitle: " + edTitle);
        DataMap paramMap = new DataMap();
        paramMap.put("seChoice", seChoice+"");
        paramMap.put("edTitle", edTitle);
        */

        DataMap paramMap = RequestUtil.getDataMap(request);
        List<FrontBoardVo> boardList = boardMapper.selectListBoard(paramMap);

        // 핵심: 화면으로 데이터 전달
        //model.addAttribute("edTitle",paramMap.getString("edTitle"));
        //model.addAttribute("seChoice",paramMap.getString("seChoice"));
        model.addAttribute("paramMap",paramMap);
        model.addAttribute("list", boardList);


        // .html 확장자는 보통 생략하며, templates 폴더 기준 경로를 적습니다.
        //return "forward:/front/board/boardList.html";
        return "front/board/boardList";
    }

    @RequestMapping(value = "/insertBoardForm.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String insertBoardForm(HttpServletRequest request, Model model){ // Model 객체 추가
        return "front/board/boardForm";
    }
    // 게시글 저장 로직
    @RequestMapping(value = "/insertBoard.do", method = {RequestMethod.POST})
    public String insertBoard(HttpServletRequest request, Model model) {
        // 1. 파라미터 받기 (DataMap 활용)
        String sj = request.getParameter("sj");
        System.out.println("**********sj["+sj+"]************");
        DataMap paramMap = RequestUtil.getDataMap(request);

        // 2. DB 저장 수행
        // boardMapper.insertBoard(paramMap); // 매퍼에 insert 문이 있다고 가정

        // 3. 저장 후 리스트 페이지로 강제 이동 (Redirect)
        // 이렇게 하면 브라우저의 주소창이 /selectPageListBoard.do로 바뀌면서 목록을 다시 조회합니다.
        return "redirect:/front/board/selectPageListBoard.do";
    }
}
