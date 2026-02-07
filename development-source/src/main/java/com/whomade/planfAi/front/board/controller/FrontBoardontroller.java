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


}
